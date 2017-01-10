package tree.rooted.tree;

/**
 * Created by Blamad on 18.12.2016.
 */
public class Leaf extends Node {

    private String name;

    public Leaf(String name, Double distanceToParent)
    {
        super(null, distanceToParent);
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public Node clone()
    {
        return new Leaf(name, distanceToParent);
    }

    @Override
    public int compareTo(Node o) {
        if (o instanceof Leaf)
            return this.getName().compareTo(((Leaf) o).getName());
        else
            return 1;
    }
}
