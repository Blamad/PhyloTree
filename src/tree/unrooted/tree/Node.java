package tree.unrooted.tree;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Blamad on 14.01.2017.
 */
public class Node {

    private String label = "";
    private Set<Node> neighbours = new HashSet();

    public Node(String s){
        this.label = s;
    }

    public Boolean addNeighbour(Node node) {
        return neighbours.add(node);
    }

    public Set<Node> getNeighbours() {
        return neighbours;
    }

    public Boolean isExternal() {
        return neighbours.size() == 1;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        if(!label.isEmpty())
            return label;
        else
            return "internalNode";
    }

    @Override
    public Node clone()
    {
        return new Node(label);
    }
}
