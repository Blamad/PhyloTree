/**
 * Created by Blamad on 18.12.2016.
 */

import forester.Forester;
import tree.Tree;
import tree.exporter.NewickTreeExporter;
import utils.PhyloTreeException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 11. „Kalkulator” drzew filogenetycznych ukorzenionych. Typ A.
 + Drzewa wczytywane są z pliku tekstowego (można użyć jakiegoś gotowego parsera np. formatu NEWICK).
 Operacje:
 -  konwersja reprezentacji „rodzina zgodnych klastrów” ⇔ drzewo jako graf z jakąś jego wizualizacją
 (oraz test poprawności danych tj. „czy podana rodzina była zgodna?”).
 -  Wyznaczanie odległości topologicznej RF między parą drzew, drzewa konsensusu (o podanym poziomie procentowym)
 dla zadanego zbioru drzew  oraz jej wspólne rozszerzenie (jeśli takie istnieje).
 - Obcięcie podanego drzewa do drzewa filogenetycznego do zadanego podzbioru liści.
 */

public class PhyloTree {

    private Logger logger;
    private String newick;
    private Tree tree;

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

            switch(input.charAt(0)) {
                case 'h':
                    System.out.println("To load tree from file stored in res type \"l <filename>\"");
                    System.out.println("To load custom tree from commandline type \"c <your_tree>\"");
                    System.out.println("To display loaded tree type \"d\"");
                    System.out.println("To display loaded tree using Forester lib type \"f\"");
                    System.out.println("To exit PhyloTree type \"q\"");
                    System.out.println("To display help again type \"h\"");
                    break;
                case 'l':
                    String fileName = "res\\" + input.substring(2).trim();
                    try {
                        newick = new String(Files.readAllBytes(Paths.get(fileName)));
                        try
                        {
                            tree = NewickTreeExporter.importTree(newick);
                            System.out.println(fileName + " loaded");
                        }
                        catch (PhyloTreeException e) {
                            System.out.println("Given pattern is not a NEWICK tree format!");
                        }
                    } catch (IOException e) {
                        System.out.println("No such file!");
                    }
                    break;
                case 'c':
                    String customTree = input.substring(2).trim();
                    try
                    {
                        tree = NewickTreeExporter.importTree(customTree);
                        newick = customTree;
                        System.out.println("custom pattern loaded");
                    }
                    catch (PhyloTreeException e) {
                        System.out.println("Given pattern is not a NEWICK tree format!");
                    }
                    break;
                case 'f':
                    new Forester(newick).drawTree();
                    break;
                case 'd':
                    if(tree != null)
                        tree.printTreeToConsole();
                    else
                        logger.log(Level.WARNING, "No tree loaded in memory yet!");
                    break;
                case 'q':
                    return;
                default:
                    System.out.println("Input not recognized, try again or type \"h\" for help.");
                    break;
            }
            System.out.print(">");
            input = sc.nextLine();
        }
    }

    private void importTreeAndWriteOut(String newick)
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
    }

    public static void main(String [] args)
    {
        new PhyloTree().run();
    }
}


