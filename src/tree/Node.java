package tree;

import tree.exporter.Leaf;

import java.util.*;

/**
 * Created by Blamad on 18.12.2016.
 */
public class Node implements Comparable<Node>{

    protected Double distanceToParent;
    protected Integer depth;

    private Node parent;
    private List<Node> listOfChildren = new ArrayList();

    public Node(Double distanceToParent)
    {
        this.distanceToParent = distanceToParent;
        this.depth = 0;
    }

    public void addChild(Node child)
    {
        listOfChildren.add(child);
        child.setParent(this);
        child.depth += 1;
    }

    public Node getParent()
    {
        return parent;
    }

    public Double getDistanceToParent()
    {
        return distanceToParent;
    }

    public List<Node> getChildren()
    {
        return listOfChildren;
    }

    public void setParent(Node node)
    {
        parent = node;
    }

    @Override
    public int compareTo(Node o) {
        return this.depth.compareTo(o.depth);
    }
}
