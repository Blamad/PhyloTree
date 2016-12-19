/**
 * Created by Blamad on 18.12.2016.
 */

import tree.Tree;
import tree.exporter.NewickTreeExporter;
import utils.PhyloTreeException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        //new Forester(this.newick).drawTree();
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


