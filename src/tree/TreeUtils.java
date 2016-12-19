package tree;

import java.util.List;

/**
 * Created by Blamad on 19.12.2016.
 */
public class TreeUtils {

    public static Tree copyTree(Tree tree)
    {
        Node node = tree.getRootNode().clone();
        return new Tree(node);
    }

    private static void deepCopy(Node node, Node copy)
    {
        for(Node child : node.getChildren())
        {
            Node newNode = child.clone();
            copy.setParent(newNode);
            deepCopy(child, newNode);
        }
    }

    public Tree pruneTreeLeavingLeaves(List<Integer> leavesNubmers)
    {
        Tree tree;

        return null;
    }
}
