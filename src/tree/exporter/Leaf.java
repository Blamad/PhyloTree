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

}
