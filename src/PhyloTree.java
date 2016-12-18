/**
 * Created by Blamad on 18.12.2016.
 */

import jebl.evolution.graphs.Node;
import jebl.evolution.io.NewickImporter;
import jebl.evolution.trees.SimpleRootedTree;
import tree.Tree;
import tree.exporter.NewickTreeExporter;
import utils.PhyloTreeException;

import java.io.StringReader;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 11. „Kalkulator” drzew filogenetycznych ukorzenionych. Typ A.
 Drzewa wczytywane są z pliku tekstowego (można użyć jakiegoś gotowego parsera np. formatu NEWICK).
 Operacje:
 -  konwersja reprezentacji „rodzina zgodnych klastrów” ⇔ drzewo jako graf z jakąś jego wizualizacją (oraz test poprawności danych tj. „czy podana rodzina była zgodna?”).
 -  Wyznaczanie odległości topologicznej RF między parą drzew, drzewa konsensusu (o podanym poziomie procentowym) dla zadanego zbioru drzew
 oraz jej wspólne rozszerzenie (jeśli takie istnieje).
 - Obcięcie podanego drzewa do drzewa filogenetycznego do zadanego podzbioru liści.
 */

//http://stackoverflow.com/questions/39153587/annotating-tree-nodes-with-data-for-analysis-with-the-java-evolutionary-biology - jebl
//http://stackoverflow.com/questions/3891041/java-library-or-code-to-parse-newick-format - Archaeopteryx

public class PhyloTree {

    private Logger logger;

    public PhyloTree()
    {
        logger = Logger.getLogger(getClass().getSimpleName());
    }

    public void run()
    {
        String newick = "(Ssak,(Żółw,Płaszczka,(Żaba,Salamandra)));";
        test2(newick);

        test(newick);

        //Narysuj.
        //new Forester(newick).drawTree();
    }

    private void test(String newick)
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

    private void test2(String newick) {
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
        new PhyloTree().run();
    }
}


