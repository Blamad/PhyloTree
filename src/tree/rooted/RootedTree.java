package tree.rooted;

import tree.rooted.cluster.ClusterFamily;
import tree.rooted.tree.DirectedTree;
import tree.utils.ClusterUtils;
import tree.utils.TreeUtils;

/**
 * Created by Blamad on 10.01.2017.
 */
public class RootedTree {

    private DirectedTree directedTree;
    private ClusterFamily cluster;

    private Boolean treeUpdate = false;
    private Boolean clusterUpdate = false;

    public RootedTree(DirectedTree directedTree) {
        this.directedTree = directedTree;
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

    public DirectedTree getDirectedTree() {
        if(treeUpdate) {
            treeUpdate = false;
            directedTree = ClusterUtils.convertClusterToTree(cluster);
        }
        return directedTree;
    }

    public ClusterFamily getCluster() {
        if(clusterUpdate) {
            clusterUpdate = false;
            cluster = TreeUtils.convertTreeToCluster(directedTree);
        }
        return cluster;
    }
}
