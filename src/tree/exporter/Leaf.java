package tree.exporter;

import tree.Node;

/**
 * Created by Blamad on 18.12.2016.
 */
public class Leaf extends Node {

    private String name;

    public Leaf(String name, Double distanceToParent)
    {
        super(distanceToParent);
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public int compareTo(Node o) {
        if (this instanceof Leaf)
            return -1;
        if (o instanceof Leaf)
            return 1;

        return 0;
    }
}
