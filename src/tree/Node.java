package tree;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Blamad on 18.12.2016.
 */
public class Node {

    private Double distanceToParent;
    private String name;

    private Node parent;
    private Set<Node> listOfChildren = new HashSet();

    public Node(String name, Double distanceToParent)
    {
        this.name = name;
        this.distanceToParent = distanceToParent;
    }

    public Boolean addChild(Node child)
    {
        if(listOfChildren.add(child)) {
            child.setParent(this);
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

    public String getName()
    {
        return name;
    }

    public Set<Node> getChildren()
    {
        return listOfChildren;
    }

    public void setParent(Node node)
    {
        parent = node;
    }

}
