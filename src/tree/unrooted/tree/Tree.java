package tree.unrooted.tree;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Blamad on 14.01.2017.
 */
public class Tree {

    private Set<Node> nodes = new HashSet();
    private Node totallyNotRootNode = null;

    public Tree(Collection<Node> nodes) {
        this.nodes.addAll(nodes);
    }

    public Set<Node> getNodes() {
        return nodes;
    }

    public void setTotallyNotRootNode(Node node) {
        this.totallyNotRootNode = node;
    }

    public Set<Node> getExternalNodes() {
        return nodes.stream().filter(n -> n.isExternal()).collect(Collectors.<Node> toSet());
    }

    public Node getNodeByLabel(String label) {
        for(Node node : nodes)
            if(node.getLabel().equals(label))
                return node;
        return null;
        //return nodes.stream().filter(n -> n.getLabel().equals(label)).findFirst().get();
    }

    public Node getTotallyNotRootNode() {
        return totallyNotRootNode;
    }

    public void print() {
        //TODO
    }
}
