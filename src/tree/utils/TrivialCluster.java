package tree.utils;

import tree.Node;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Aga on 2016-12-28.
 */
public class TrivialCluster {
    private ArrayList<String> trivialClusters = new ArrayList<>();
    private String clusterFromOneNode = "";
    private ArrayList<String> clusterNodes = new ArrayList<>();

    /*function to add clusters to array of clusters */
    public void add(String cluster) {
        trivialClusters.add(cluster);
    }

    /*build one cluster */
    public void addToClusterFromOneNode(String nodeName) {
        this.clusterFromOneNode += nodeName + " ";
    }

    /*save simple one cluster */
    public void saveClusterFromOneNode() {
        //remove last char
        StringBuilder sb = new StringBuilder(clusterFromOneNode);
        sb.deleteCharAt(clusterFromOneNode.length()-1);
        this.trivialClusters.add(sb.toString());
        clusterFromOneNode = "";
    }

    /*print all clusters */
    public void print() {
        for (String cluster : trivialClusters)
            System.out.println("{" + cluster + "} ");
    }

    /*clear trivial cluster */
    public void clear() {
        clusterFromOneNode = "";
        trivialClusters.clear();
    }

    public ArrayList<String> getTrivialClusters() {
        return trivialClusters;
    }

    public ArrayList<String> getNodesCluster(int clusterID) {
        String[] nodes = trivialClusters.get(clusterID).split(" ");
        clusterNodes.addAll(Arrays.asList(nodes)); //get leaves from cluster
        return clusterNodes;
    }
}
