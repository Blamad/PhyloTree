/**
 * Created by Blamad on 18.12.2016.
 */

import forester.Forester;
import jebl.evolution.graphs.Node;
import jebl.evolution.io.NewickImporter;
import jebl.evolution.trees.SimpleRootedTree;
import tree.Tree;
import tree.exporter.NewickTreeExporter;
import utils.PhyloTreeException;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 11. „Kalkulator” drzew filogenetycznych ukorzenionych. Typ A.
 + Drzewa wczytywane są z pliku tekstowego (można użyć jakiegoś gotowego parsera np. formatu NEWICK).
 Operacje:
 -  konwersja reprezentacji „rodzina zgodnych klastrów” ⇔ drzewo jako graf z jakąś jego wizualizacją
 (oraz importTreeAndWriteOut poprawności danych tj. „czy podana rodzina była zgodna?”).
 -  Wyznaczanie odległości topologicznej RF między parą drzew, drzewa konsensusu (o podanym poziomie procentowym)
 dla zadanego zbioru drzew  oraz jej wspólne rozszerzenie (jeśli takie istnieje).
 - Obcięcie podanego drzewa do drzewa filogenetycznego do zadanego podzbioru liści.
 */

public class PhyloTree {

    private Logger logger;
    private String newick;

    public PhyloTree(String fileContent)
    {
        this.newick = fileContent;
        logger = Logger.getLogger(getClass().getSimpleName());
    }

    public void run()
    {
        importTreeAndWriteOut(this.newick);

        wypiszNaSurowo(this.newick);
        new Forester(this.newick).drawTree();
    }

    private void importTreeAndWriteOut(String newick)
    {
        Tree tree = null;

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

    private void wypiszNaSurowo(String newick) {
        StringReader sr = new StringReader(newick);
        NewickImporter ni = new NewickImporter(sr, true);

        try {
            SimpleRootedTree srt = (SimpleRootedTree) ni.importNextTree();
            Node rootNode = srt.getRootNode();
            processNode(srt, rootNode, 0);
        } catch (Exception e) { }
    }

    private void processNode(SimpleRootedTree srt, Node node, Integer depth)
    {
        List<Node> children = srt.getChildren(node);
        if(children.isEmpty())
        {
            String result = "";
            while(--depth > 0)
                result += "\t";
            result += srt.getTaxon(node);
            System.out.println(result);
        }

        for(Node childNode : children)
        {
            processNode(srt, childNode, depth+1);
        }
    }

    public static void main(String [] args)
    {
        if(args.length != 1) {
            System.out.println("No file path given");
            return;
        }

        try {
            new PhyloTree(new String(Files.readAllBytes(Paths.get(args[0])))).run();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("No such file!");
        }
    }
}


