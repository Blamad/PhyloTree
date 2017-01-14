package tree.unrooted.split;

import tree.unrooted.tree.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blamad on 14.01.2017.
 */
public class SplitFamily {

    private Node startNode;
    private List<Split> splitList = new ArrayList();

    public SplitFamily(Node node) {
        startNode = node;
    }

    public void addSplit(Split split) {
        splitList.add(split);
    }

    public Node getStartNode() {
        return startNode;
    }

    public void print() {
        for(Split split : splitList) {
            System.out.println(split);
        }
    }
}
