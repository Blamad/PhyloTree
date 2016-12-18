package forester;

import org.forester.archaeopteryx.Archaeopteryx;
import org.forester.phylogeny.Phylogeny;

import java.io.IOException;

/**
 * Created by Blamad on 18.12.2016.
 *
 * Pomocnicza klasa rysująca drzewko za nas.
 */

public class Forester {

    private String newickTree;

    public Forester(String newick)
    {
        this.newickTree = newick;
    }

    public void drawTree()
    {
        try
        {
            Phylogeny ph = Phylogeny.createInstanceFromNhxString(newickTree);
            Archaeopteryx.createApplication(ph);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
