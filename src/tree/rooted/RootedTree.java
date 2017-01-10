package tree.rooted;

import tree.rooted.cluster.ClusterFamily;
import tree.rooted.tree.Tree;
import tree.utils.ClusterUtils;
import tree.utils.TreeUtils;

/**
 * Created by Blamad on 10.01.2017.
 */
public class RootedTree {

    private Tree tree;
    private ClusterFamily cluster;

    private Boolean treeUpdate = false;
    private Boolean clusterUpdate = false;

    public RootedTree(Tree tree) {
        this.tree = tree;
        this.clusterUpdate = true;
    }

    public RootedTree(ClusterFamily cluster) {
        this.cluster = cluster;
        this.treeUpdate = true;
    }

    public void setClusterOutdated() {
        this.clusterUpdate = true;
    }

    public void setTreeOutdated() {
        this.treeUpdate = true;
    }

    public Tree getTree() {
        if(treeUpdate) {
            treeUpdate = false;
            tree = ClusterUtils.convertClusterToTree(cluster);
        }
        return tree;
    }

    public ClusterFamily getCluster() {
        if(clusterUpdate) {
            clusterUpdate = false;
            cluster = TreeUtils.convertTreeToCluster(tree);
        }
        return cluster;
    }
}
