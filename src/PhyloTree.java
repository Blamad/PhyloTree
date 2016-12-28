/**
 * Created by Blamad on 18.12.2016.
 */

import forester.Forester;
import tree.Tree;
import tree.utils.ClusterUtils;
import tree.utils.TreeUtils;
import tree.exporter.NewickTreeExporter;
import tree.utils.TrivialCluster;
import utils.PhyloTreeException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private Logger logger;
    private String newick;
    private Tree tree;
    private String newickSecond;
    private Tree treeSecond;
    private TrivialCluster trivialCluster = new TrivialCluster();
    private TrivialCluster trivialClusterSecond = new TrivialCluster();

    public PhyloTree()
    {
        logger = Logger.getLogger(getClass().getSimpleName());
    }

    public void run()
    {
        String input = "h";
        System.out.println("Welcome to PhyloTree!");

        Scanner sc = new Scanner(System.in);

        while(true) {

            try {
                switch (input.charAt(0)) {
                    case 'h':
                        System.out.println("To load tree from file stored in res type \"l <filename>\"");
                        System.out.println("To load second tree from file stored in res type \"L <filename>\"");
                        System.out.println("To load custom tree from commandline type \"c <your_tree>\"");
                        System.out.println("To load second custom tree from commandline type \"C <your_tree>\"");
                        System.out.println("To display loaded tree type \"d\"");
                        System.out.println("To prune current tree to subset of leaves type \"p <leaves_comma_separated>\"");
                        System.out.println("To display loaded tree using Forester lib type \"f\"");
                        System.out.println("To generate trivial cluster type \"t\"");
                        System.out.println("To generate trivial cluster from second tree type \"t\"");
                        System.out.println("To merge two different trees type \"m\"");
                        System.out.println("To exit PhyloTree type \"q\"");
                        System.out.println("To display help again type \"h\"");
                        break;
                    case 'l':
                        if (!devLoad(input, false)) {
                            String fileName = "res\\" + input.substring(2).trim();
                            loadFromFile(fileName, false); //is not second tree
                        }
                        break;
                    case 'L':
                        if (!devLoad(input, true)) {
                            String fileName = "res\\" + input.substring(2).trim();
                            loadFromFile(fileName, true); //is second tree
                        }
                        break;
                    case 'c':
                        String customTree = input.substring(2).trim();
                        try {
                            tree = NewickTreeExporter.importTree(customTree);
                            newick = customTree;
                            System.out.println("custom pattern loaded");
                        } catch (PhyloTreeException e) {
                            System.out.println("Given pattern is not a NEWICK tree format!");
                        }
                        break;
                    case 'C':
                        String customTree2 = input.substring(2).trim();
                        try {
                            treeSecond = NewickTreeExporter.importTree(customTree2);
                            newickSecond = customTree2;
                            System.out.println("custom pattern loaded");
                        } catch (PhyloTreeException e) {
                            System.out.println("Given pattern is not a NEWICK tree format!");
                        }
                        break;
                    case 't':
                        if (tree == null)
                            System.out.println("No tree loaded in memory yet!");
                        else {
                            trivialCluster.clear();
                            trivialCluster = tree.transformToTrivialCluster();
                            trivialCluster.print();
                        }
                        break;
                    case 'T':
                        if (treeSecond == null)
                            System.out.println("No tree loaded in memory yet!");
                        else {
                            trivialClusterSecond.clear();
                            trivialClusterSecond = treeSecond.transformToTrivialCluster();
                            trivialClusterSecond.print();
                        }
                        break;
                    case 'm':
                        if (tree == null || treeSecond == null)
                            System.out.println("First or second tree not loaded in memory yet!");
                        else {
                            if (trivialCluster == null)
                                trivialCluster = tree.transformToTrivialCluster();
                            if (trivialClusterSecond == null)
                                trivialClusterSecond = treeSecond.transformToTrivialCluster();
                            TrivialCluster mergedCluster = ClusterUtils.mergeTwoClusters(trivialCluster, trivialClusterSecond);
                            mergedCluster.print();
                            //Tree mergedTree = ClusterUtils.convertTrivialClusterToTree(mergedCluster);
                            //mergedTree.printTreeToConsole();
                        }
                        break;
                    case 'f':
                        new Forester(newick).drawTree();
                        break;
                    case 'd':
                        if (tree == null) {
                            System.out.println("No tree loaded in memory yet!");
                            break;
                        }
                        tree.printTreeToConsole();
                        break;
                    case 'q':
                        return;
                    case 'p':
                        if(tree == null)
                        {
                            System.out.println("No tree loaded in memory yet!");
                            break;
                        }
                        String[] leaves = input.substring(2).split(",");
                        Tree prunedTree = TreeUtils.pruneTreeLeavingLeaves(tree, Arrays.asList(leaves));
                        prunedTree.printTreeToConsole();
                        break;
                    default:
                        System.out.println("Input not recognized, try again or type \"h\" for help.");
                        break;
                }
            } catch(Exception e) {
                System.out.println("Sth went wrong. Try again.");
            }
            System.out.print(">");
            input = sc.nextLine();
        }
    }

    private void loadFromFile(String fileName, boolean isSecondTree)
    {
        try {
            if (!isSecondTree)
                newick = new String(Files.readAllBytes(Paths.get(fileName)));
            else
                newickSecond = new String(Files.readAllBytes(Paths.get(fileName)));
            try
            {
                if (!isSecondTree)
                    tree = NewickTreeExporter.importTree(newick);
                else
                    treeSecond = NewickTreeExporter.importTree(newickSecond);
                System.out.println(fileName + " loaded");
            }
            catch (PhyloTreeException e) {
                System.out.println("Given pattern is not a NEWICK tree format!");
            }
        } catch (IOException e) {
            System.out.println("No such file!");
        }
    }

    private Boolean devLoad(String input, boolean isSecondTree)
    {
        String in = input.substring(2);
        try {
            Integer testNo = Integer.parseInt(in);
            switch (testNo)
            {
                case 1:
                    loadFromFile("res/tree_1.txt", isSecondTree);
                    break;
                case 2:
                    loadFromFile("res/tree_2.txt", isSecondTree);
                    break;
                case 3:
                    loadFromFile("res/tree_3.txt", isSecondTree);
                    break;
                case 4:
                    loadFromFile("res/tree_4.txt", isSecondTree);
                    break;
                case 5:
                    loadFromFile("res/tree_zyciowe.txt", isSecondTree);
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

    /*TODO: to delete? - not used */
    /*private void importTreeAndWriteOut(String newick)
    {
        try
        {
            tree = NewickTreeExporter.importTree(newick);
        }
        catch (PhyloTreeException e) {
            logger.log(Level.WARNING, e.getInfo());
            return;
        }

        tree.printTreeToConsole();
    }*/

    public static void main(String [] args)
    {
        new PhyloTree().run();
    }
}