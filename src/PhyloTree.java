/**
 * Created by Blamad on 18.12.2016.
 */

import forester.Forester;
import tree.rooted.RootedTree;
import tree.rooted.tree.Tree;
import tree.utils.ClusterUtils;
import tree.utils.TreeUtils;
import tree.exporter.NewickTreeExporter;
import tree.rooted.cluster.ClusterFamily;
import utils.PhyloTreeException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

/**
 11. „Kalkulator” drzew filogenetycznych ukorzenionych. Typ A.
 + Drzewa wczytywane są z pliku tekstowego (można użyć jakiegoś gotowego parsera np. formatu NEWICK).
 Operacje:
 +  konwersja reprezentacji „rodzina zgodnych klastrów” ⇔ drzewo jako graf z jakąś jego wizualizacją
 (oraz test poprawności danych tj. „czy podana rodzina była zgodna?”). - zgodność jest zdaje się walidowana przy wczytywaniu łańcucha.
 + Obcięcie podanego drzewa do drzewa filogenetycznego do zadanego podzbioru liści.

 //TODO
 -  Wyznaczanie odległości topologicznej RF między parą drzew, drzewa konsensusu (o podanym poziomie procentowym)
 dla zadanego zbioru drzew  oraz jej wspólne rozszerzenie (jeśli takie istnieje).
 */

public class PhyloTree {

    private RootedTree[] treesArray = new RootedTree[10];
    private String[] newicks = new String[10];
    private String input;

    public void run()
    {
        input = "h";
        System.out.println("Welcome to PhyloTree!");

        Scanner sc = new Scanner(System.in);

        while(true) {

            try {
                switch (input.charAt(0)) {
                    case 'h':
                        System.out.println("System has 10 cells to remember each tree.");
                        System.out.println("To load newick tree from file stored in res type \"l <0-9> <filename>\"");
                        System.out.println("To load newick tree from commandline type \"n <0-9> <your_tree>\"");
                        System.out.println("To load cluster from commandline type \"c <0-9> {A, B, C};{B, C}\"");
                        System.out.println("To remove tree from memory type \"r <0-9>\"");
                        System.out.println("To display loaded tree \"d <0-9> <t;c>\" where t - tree struct, c - cluster struct");
                        System.out.println("To prune current tree to subset of leaves type \"p <0-9> <leaves_comma_separated>\"");
                        System.out.println("To display loaded newick tree using Forester lib type \"f <0-9>\"");
                        System.out.println("To merge trees into new tree type \"m <0-9>\"");
                        System.out.println("To count RF type \"a\"");
                        System.out.println("To create consensus tree into new tree type \"m <0-9> <consensus value>\"");
                        System.out.println("To exit PhyloTree type \"q\"");
                        System.out.println("To display help again type \"h\"");
                        break;
                    case 'l': //load newick from file
                        loadNewick();
                        break;
                    case 'n': // load newick from commandline
                        loadCustomNewick();
                        break;
                    case 'c': //load  cluster tree
                        loadCluster();
                        break;
                    case 'm': //Merge
                        mergeTrees();
                        break;
                    case 'a': //count RF
                        countRF();
                        break;
                    case 'f': //Rysuj foresterem
                        drawForester();
                        break;
                    case 'd': //wyswietlanie
                        drawTree();
                        break;
                    case 'p':
                        pruneTree();
                        break;
                    case 'r':
                        removeTree();
                        break;
                    case 'q':
                        return;
                    default:
                        System.out.println("Input not recognized, try again or type \"h\" for help.");
                        break;
                }
            } catch(Exception e) {
                e.printStackTrace();
                System.out.println("Sth went wrong. Try again.");
            }
            System.out.print(">");
            input = sc.nextLine();
        }
    }

    private void removeCommand() {
        input = input.substring(2);
    }

    private int getIndex() {
        try {
            int firstSpace = input.indexOf(" ");
            if(firstSpace == -1)
                firstSpace = input.length();

            String number = input.substring(0, firstSpace);
            int index = Integer.parseInt(number);
            if(firstSpace != input.length())
                input = input.substring(firstSpace+1);
            return index;
        }
        catch(Exception e) { e.printStackTrace(); }
        return -1;
    }

    private Boolean validateIndex(int index) {
        return index > -1 && index < 10;
    }

    private void loadCluster() {
        removeCommand();
        Integer index = getIndex();
        if(!validateIndex(index)) {
            System.out.println("Number must be in 0-9 range!");
            return;
        }

    }

    private void removeTree() {
        removeCommand();
        Integer index = getIndex();
        if(!validateIndex(index)) {
            System.out.println("Number must be in 0-9 range!");
            return;
        }
        treesArray[index] = null;
        System.out.println("Tree removed.");
    }

    private void countRF() {
        if (treesArray[0] == null || treesArray[1] == null)
            System.out.println("Wrong number of trees. Shoulb be 2.");
        else {
            System.out.println(ClusterUtils.countRF(treesArray[0].getCluster(), treesArray[1].getCluster()));
        }
    }

    private void mergeTrees() {

        removeCommand();
        Integer index = getIndex();
        if(!validateIndex(index)) {
            System.out.println("Number must be in 0-9 range!");
            return;
        }

        Integer value = getIndex();
        if(value < 0 || value > 100) {
            System.out.println("Consensus value must be in 0-100 range!");
            return;
        }

        RootedTree mergedTree = null;
        for(RootedTree rootedTree : treesArray) {
            if(rootedTree == null)
                continue;
            ClusterFamily cluster = rootedTree.getCluster();

            if(mergedTree == null)
                mergedTree = new RootedTree(cluster);
            else
                mergedTree = new RootedTree(ClusterUtils.mergeTwoClusters(mergedTree.getCluster(), rootedTree.getCluster()));
        }

        treesArray[index] = mergedTree;
        if(mergedTree == null)
            System.out.println("Falied to merge trees, empty memory!");
        else
            System.out.println("Trees merged and saved under " + index);
    }

    private void pruneTree() {
        removeCommand();
        Integer index = getIndex();
        if(!validateIndex(index)) {
            System.out.println("Number must be in 0-9 range!");
            return;
        }
        if(treesArray[index] == null) {
            System.out.println("No tree under index " + index);
            return;
        }

        String[] leaves = input.substring(2).split(",");
        Tree prunedTree = TreeUtils.pruneTreeLeavingLeaves(treesArray[index].getTree(), Arrays.asList(leaves));
        prunedTree.print();
    }

    private void drawForester() {
        removeCommand();
        Integer index = getIndex();
        if(!validateIndex(index)) {
            System.out.println("Number must be in 0-9 range!");
            return;
        }
        if(newicks[index] == null || newicks[index].isEmpty()) {
            System.out.println("No newick under index " + index);
            return;
        }
        new Forester(newicks[index]).drawTree();
    }

    private void drawTree() {
        removeCommand();
        Integer index = getIndex();
        if(!validateIndex(index)) {
            System.out.println("Number must be in 0-9 range!");
            return;
        }
        if(treesArray[index] == null) {
            System.out.println("No tree under index " + index);
            return;
        }

        switch (input.charAt(0)) {
            case 'c':
                treesArray[index].getCluster().print();
                break;
            case 't':
                treesArray[index].getTree().print();
                break;
            default:
                System.out.println("Uknown print type");
                break;
        }
    }

    private void loadCustomNewick() {
        removeCommand();
        Integer index = getIndex();
        if(!validateIndex(index)) {
            System.out.println("Number must be in 0-9 range!");
            return;
        }

        try {
            Tree tree = NewickTreeExporter.importTree(input);
            treesArray[index] = new RootedTree(tree);
            newicks[index] = input;
            System.out.println("custom pattern loaded");
        } catch (PhyloTreeException e) {
            System.out.println("Given pattern is not a NEWICK tree format!");
        }
    }

    private void loadNewick() {
        removeCommand();
        Integer index = getIndex();
        if(!validateIndex(index)) {
            System.out.println("Number must be in 0-9 range!");
            return;
        }
        if (!devLoad(input, index)) {
            String fileName = "res\\" + input.substring(2).trim();
            loadFromFile(fileName, index);
        }

    }

    private void loadFromFile(String fileName, int treeIndex)
    {
        try {
            String newick = new String(Files.readAllBytes(Paths.get(fileName)));
            newicks[treeIndex] = newick;

            try
            {
                Tree tree = NewickTreeExporter.importTree(newick);
                treesArray[treeIndex] = new RootedTree(tree);
                System.out.println(fileName + " loaded");
            }
            catch (PhyloTreeException e) {
                System.out.println("Given pattern is not a NEWICK tree format!");
            }

        } catch (IOException e) {
            System.out.println("No such file!");
        }
    }

    private Boolean devLoad(String input, int treeIndex)
    {
        try {
            Integer testNo = Integer.parseInt(input);
            switch (testNo)
            {
                case 1:
                    loadFromFile("res/tree_1.txt", treeIndex);
                    break;
                case 2:
                    loadFromFile("res/tree_2.txt", treeIndex);
                    break;
                case 3:
                    loadFromFile("res/tree_3.txt", treeIndex);
                    break;
                case 4:
                    loadFromFile("res/tree_4.txt", treeIndex);
                    break;
                case 5:
                    loadFromFile("res/tree_zyciowe.txt", treeIndex);
                    break;
                default:
                    return false;
            }
        } catch(NumberFormatException e)
        {
            return false;
        }

        return true;
    }

    public static void main(String [] args)
    {
        new PhyloTree().run();
    }
}