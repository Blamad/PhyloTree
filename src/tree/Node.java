package tree;

import tree.exporter.Leaf;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Blamad on 18.12.2016.
 */
public class Node implements Comparable<Node>{

    protected Double distanceToParent;
    protected Integer depth;

    private Node parent;
    private Set<Node> listOfChildren = new HashSet();

    public Node(Double distanceToParent)
    {
        this.distanceToParent = distanceToParent;
        this.depth = 0;
    }

    public Boolean addChild(Node child)
    {
        if(listOfChildren.add(child)) {
            child.setParent(this);
            child.depth += 1;
            return true;
        }
        return false;
    }

    public Node getParent()
    {
        return parent;
    }

    public Double getDistanceToParent()
    {
        return distanceToParent;
    }

    public Set<Node> getChildren()
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
