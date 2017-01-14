package tree.unrooted.split;

import tree.unrooted.tree.Node;

import java.util.*;

/**
 * Created by Blamad on 14.01.2017.
 */
public class Split {

    private List<Set<Node>> subSets = new ArrayList();

    public void addSubSet(Collection<Node> subSet) {
        HashSet<Node> subset = new HashSet();
        subset.addAll(subSet);
        subSets.add(subset);
    }

    @Override
    public String toString() {
        String split = "";
        String subSplit;

        for(Set<Node> sub : subSets) {
            subSplit = "";
            for(Node node : sub) {
                if(node.isExternal())
                    subSplit += ","+node;
            }
            split += "|{"+ subSplit.substring(1) +"}";
        }

        return split.substring(1);
    }
}
