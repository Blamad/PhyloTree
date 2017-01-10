/**
 * Created by Blamad on 18.12.2016.
 */

import forester.Forester;
import tree.Tree;
import tree.utils.ClusterUtils;
import tree.utils.TreeUtils;
import tree.exporter.NewickTreeExporter;
import tree.utils.ClusterFamily;
import utils.PhyloTreeException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
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
    public static LinkedList<Tree> Forrest;

    private String newick;
    private Tree tree;
    private ClusterFamily clusterFamily = new ClusterFamily();
    private ClusterFamily clusterFamilySecond = new ClusterFamily();

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
                        //System.out.println("To load second tree from file stored in res type \"L <filename>\"");
                        System.out.println("To load custom tree from commandline type \"c <your_tree>\"");
                        //System.out.println("To load second custom tree from commandline type \"C <your_tree>\"");
                        System.out.println("To display loaded tree type \"d\"");
                        System.out.println("To prune current tree to subset of leaves type \"p <leaves_comma_separated>\"");
                        System.out.println("To display loaded tree using Forester lib type \"f\"");
                        System.out.println("To generate cluster type \"t\"");
                        System.out.println("To generate cluster from second tree type \"T\"");
                        System.out.println("To generate tree from trivial cluster type \"r\"");
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
                    case 't':
                        if (tree == null)
                            System.out.println("No tree loaded in memory yet!");
                        else {
                            clusterFamily.clear();
                            clusterFamily = tree.transformToTrivialCluster();
                            clusterFamily.print();
                        }
                        break;
                    case 'r':
                        if (clusterFamily.getTrivialClusters().isEmpty())
                            System.out.println("No trivial cluster!");
                        else {
                            Tree convertedTree = ClusterUtils.convertTrivialClusterToTree(clusterFamily);
                            convertedTree.printTreeToConsole();
                        }
                        break;
                    case 'm':
                        if (Forrest.size()<2)
                            System.out.println("First or second tree not loaded in memory yet!");
                        else
                            merge_trees();
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

    private void merge_trees() {
        while(Forrest.size() >=2) {
            Tree t1 = Forrest.pop();
            Tree t2 = Forrest.pop();
            ClusterFamily mergedCluster = ClusterUtils.mergeTwoClusters(t1.transformToTrivialCluster(), t2.transformToTrivialCluster());
            mergedCluster.print();
            Tree mergedTree = ClusterUtils.convertTrivialClusterToTree(mergedCluster);
            Forrest.push(mergedTree);
        }
    }

    private void loadFromFile(String fileName, boolean isSecondTree)
    {
        try {
            newick = new String(Files.readAllBytes(Paths.get(fileName)));
            try
            {
                tree = NewickTreeExporter.importTree(newick);
                System.out.println(fileName + " loaded");
                Forrest.add(tree);
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

    public static void main(String [] args)
    {
        Forrest = new LinkedList<>();
        new PhyloTree().run();
    }
}