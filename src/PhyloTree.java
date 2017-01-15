/**
 * Created by Blamad on 18.12.2016.
 */

import forester.Forester;
import tree.rooted.RootedTree;
import tree.rooted.tree.DirectedTree;
import tree.unrooted.UnrootedTree;
import tree.unrooted.split.SplitFamily;
import tree.unrooted.tree.Tree;
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

    private UnrootedTree[] unrootedTreesArray = new UnrootedTree[10];
    private String[] unrootedNewicks = new String[10];

    private String input;

    private Boolean ROOTED_MODE = false;

    public void run()
    {
        input = "h";
        System.out.println("Welcome to PhyloTree!");

        Scanner sc = new Scanner(System.in);

        while(true) {

            try {
                switch (getCommand()) {
                    case "h":
                        printHelp();
                        break;
                    case "l": //load newick from file
                        loadNewickFromResFile();
                        break;
                    case "n": // load newick from commandline
                        loadCustomNewick();
                        break;
                    case "c": //load cluster fam
                    case "s": //load split fam
                        if(ROOTED_MODE)
                            loadCluster();
                        else
                            loadSplit();
                        break;
                    case "m": //Merge
                        if(ROOTED_MODE)
                            mergeTrees();
                        else
                            mergeUnrootedTrees();
                        break;
                    case "crf": //count RF
                        if(ROOTED_MODE)
                            countRF();
                        else
                            countUnrootedRF();
                        break;
                    case "f": //Rysuj foresterem
                        drawForester();
                        break;
                    case "d": //wyswietlanie
                        if(ROOTED_MODE)
                            drawTree();
                        else
                            drawUnrootedTree();
                        break;
                    case "p":
                        if(ROOTED_MODE)
                            pruneTree();
                        else
                            pruneUnrootedTree();
                        break;
                    case "r":
                        removeTree();
                        break;
                    case "q":
                        return;
                    default:
                        System.out.println("Input not recognized, try again or type \"h\" for help.");
                        break;
                }
            } catch(Exception e) {
                e.printStackTrace();
                System.out.println("Sth went wrong. Try again.");
            }
            finally {
                System.out.print(">");
                input = sc.nextLine();
            }
        }
    }

    private void printHelp() {
        System.out.println("System has 10 cells to remember each tree.");

        if (ROOTED_MODE) {
            System.out.println("\"l <0-9> <filenumber>\" - load rooted tree in newick from file stored in res");
            System.out.println("\"n <0-9> <your_tree>\" - load rooted tree in newick from commandline");
            System.out.println("\"c <0-9> {A, B, C};{B, C}\" - load cluster from commandline type");
            System.out.println("\"d <0-9> <t;c>\" - display loaded tree as t - tree struct, c - cluster struct");
        } else {
            System.out.println("\"l <0-9> <filenumber>\" - load unrooted tree in newick from file stored in res");
            System.out.println("\"n <0-9> <your_tree>\" - load unrooted tree in newick from commandline");
            System.out.println("\"s <0-9> {A | C};{B | C}\" - load split family from commandline type");
            System.out.println("\"d <0-9>\" - display loaded tree");
            System.out.println("\"d <0-9> s <node_label>\" - display loaded tree as split family form selected node");
        }

        System.out.println("\"r <0-9>\" = remove tree from memory");
        System.out.println("\"p <0-9> <leaves_comma_separated>\" - prune current tree to subset of leaves");
        System.out.println("\"crf\" - display Robinson Foulds distance between tree 1 and 2");
        System.out.println("\"m <0-9> <consensus value>\" - create consensus tree from loaded trees into cell");

        System.out.println("\"f <0-9>\" - display loaded newick tree using Forester lib");
        System.out.println("\"q\" - exit PhyloTree");
        System.out.println("To display help again type \"h\"");
    }

    private String getCommand() {
        String[] split = input.split(" ", 2);
        if(split.length == 2)
            input = split[1];
        return split[0];
    }

    private int getNumberFromInput() {
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

        catch(Exception e) { }
        return -1;
    }

    private Boolean validateIndex(int index) {
        if(index > -1 && index < 10) {
            return true;
        }
        else {
            System.out.println("Number must be in 0-9 range!");
            return false;
        }
    }

    private void loadCluster() {
        Integer index = getNumberFromInput();
        if(!validateIndex(index))
            return;

        input = input.replaceAll("[{},]", "");
        String[] values = input.split(";");
        ClusterFamily clusterFamily = ClusterUtils.createCluster(values);
        if(clusterFamily == null)
            System.out.println("Could not create cluster from input.");
        else {
            treesArray[index] = new RootedTree(clusterFamily);
            System.out.println("Custom cluster loaded.");
        }
    }

    private void removeTree() {
        Integer index = getNumberFromInput();
        if(!validateIndex(index))
            return;

        treesArray[index] = null;
        System.out.println("Cell " + index + " empty.");
    }

    private void countRF() {
        if (treesArray[0] == null || treesArray[1] == null)
            System.out.println("Wrong number of trees. Should be 2.");
        else {
            System.out.println(ClusterUtils.countRF(treesArray[0].getCluster(), treesArray[1].getCluster()));
        }
    }

    private void mergeTrees() {
        Integer index = getNumberFromInput();
        if(!validateIndex(index))
            return;

        Integer value = getNumberFromInput();
        if(value < 0 || value > 100) {
            System.out.println("Consensus value must be in 0-100 range!");
            return;
        }

        RootedTree mergedTree = new RootedTree(ClusterUtils.mergeTwoClusters(treesArray,value/100));
        if(mergedTree == null) {
            System.out.println("Falied to merge trees, empty memory!");
        }
        else {
            System.out.println("Trees merged and saved under " + index);
            treesArray[index] = mergedTree;
        }
    }

    private void pruneTree() {
        Integer index = getNumberFromInput();
        if(!validateIndex(index))
            return;

        if(treesArray[index] == null) {
            System.out.println("No tree under index " + index);
            return;
        }

        String[] leaves = input.split(",");
        DirectedTree prunedDirectedTree = TreeUtils.pruneTreeLeavingLeaves(treesArray[index].getDirectedTree(), Arrays.asList(leaves));
        prunedDirectedTree.print();
    }

    private void drawForester() {
        Integer index = getNumberFromInput();
        if(!validateIndex(index))
            return;

        if(newicks[index] == null || newicks[index].isEmpty()) {
            System.out.println("No newick under index " + index);
            return;
        }
        new Forester(newicks[index]).drawTree();
    }

    private void drawTree() {
        Integer index = getNumberFromInput();
        if(!validateIndex(index))
            return;

        if(treesArray[index] == null) {
            System.out.println("No tree under index " + index);
            return;
        }

        switch (input.charAt(0)) {
            case 'c':
                treesArray[index].getCluster().print();
                break;
            case 't':
                treesArray[index].getDirectedTree().print();
                break;
            default:
                System.out.println("Uknown print type");
                break;
        }
    }

    private void loadCustomNewick() {
        Integer index = getNumberFromInput();
        if(!validateIndex(index))
            return;

        try {
            DirectedTree directedTree = NewickTreeExporter.importRootedTree(input);
            treesArray[index] = new RootedTree(directedTree);
            newicks[index] = input;
            System.out.println("custom pattern loaded");
        } catch (PhyloTreeException e) {
            System.out.println("Given pattern is not a NEWICK tree format!");
        }
    }

    private void loadNewickFromResFile() {
        Integer index = getNumberFromInput();
        if(!validateIndex(index))
            return;

        if (!devLoad(input, index)) {
            String fileName = "res\\" + input;
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
                if(ROOTED_MODE) {
                    DirectedTree directedTree = NewickTreeExporter.importRootedTree(newick);
                    treesArray[treeIndex] = new RootedTree(directedTree);
                }
                else {
                    Tree tree = NewickTreeExporter.importUnrootedTree(newick);
                    unrootedTreesArray[treeIndex] = new UnrootedTree(tree);
                }

                System.out.println(fileName + " loaded");
            }
            catch (PhyloTreeException e) {
                System.out.println("Given pattern is not a proper NEWICK tree format!");
            }

        } catch (IOException e) {
            System.out.println("No such file!");
        }
    }

    private Boolean devLoad(String input, int treeIndex)
    {
        try {
            Integer testNo = Integer.parseInt(input);
            if(ROOTED_MODE && testNo > 5)
                return false;
            if(!ROOTED_MODE)
                testNo += 5;

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
                case 6:
                    loadFromFile("res/utree_1.txt", treeIndex);
                    break;
                case 7:
                    loadFromFile("res/utree_2.txt", treeIndex);
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

    //////////////////////////////////////////////////
    //                                              //
    //              Unrooted tree stuff             //
    //                                              //
    //////////////////////////////////////////////////

    private void drawUnrootedTree(){
        //TODO wyswietlanie drzewa

        Integer index = getNumberFromInput();
        if(!validateIndex(index))
            return;

        if (unrootedTreesArray[index] == null) {
            System.out.println("Empty tree slot under " + index);
            return;
        }

        String[] args = input.split(" ");
        if(args.length == 2) {
            SplitFamily split = unrootedTreesArray[index].getSplit(args[1]);
            if(split != null)
                split.print();
            else
                System.out.println("Cannot create split!");
        }
    }

    private void loadSplit() {
        //TODO
        System.out.println("TODO");
    }

    private void mergeUnrootedTrees() {
        //TODO
        System.out.println("TODO");
    }

    private void countUnrootedRF(){
        //TODO
        System.out.println("TODO");
    }

    private void pruneUnrootedTree() {
        //TODO
        System.out.println("TODO");
    }

    public static void main(String [] args)
    {
        new PhyloTree().run();
    }
}