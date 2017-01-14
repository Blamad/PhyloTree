package tree.unrooted;

import tree.unrooted.split.SplitFamily;
import tree.unrooted.tree.Node;
import tree.unrooted.tree.Tree;
import tree.utils.ClusterUtils;
import tree.utils.TreeUtils;

/**
 * Created by Blamad on 14.01.2017.
 */
public class UnrootedTree {

    private Tree tree;
    private SplitFamily split;

    private Boolean treeUpdate = false;
    private Boolean splitUpdate = false;

    public UnrootedTree(Tree tree) {
        this.tree = tree;
        this.splitUpdate = true;
    }

    public UnrootedTree(SplitFamily split) {
        this.split = split;
        this.treeUpdate = true;
    }

    public void setSplitOutdated() {
        this.splitUpdate = true;
    }

    public void setTreeOutdated() {
        this.treeUpdate = true;
    }

    public Tree getTree() {
        if(treeUpdate) {
            treeUpdate = false;
            tree = ClusterUtils.convertSplitToTree(split);
        }
        return tree;
    }

    public SplitFamily getSplit(String nodeLabel) {
        Node node = tree.getNodeByLabel(nodeLabel);
        if(node == null)
            return null;

        if(splitUpdate || !split.getStartNode().equals(node)) {
            splitUpdate = false;
            split = TreeUtils.convertTreeToSplit(tree, node);
        }
        return split;
    }
}
