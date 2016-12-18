package tree;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Blamad on 18.12.2016.
 */
public class Node {

    protected Double distanceToParent;

    private Node parent;
    private Set<Node> listOfChildren = new HashSet();

    public Node(Double distanceToParent)
    {
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

    public Set<Node> getChildren()
    {
        return listOfChildren;
    }

    public void setParent(Node node)
    {
        parent = node;
    }

}
