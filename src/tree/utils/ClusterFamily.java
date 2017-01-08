package tree.utils;

import tree.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Aga on 2016-12-28.
 */
public class ClusterFamily {
    private Set<String> trivialClusters = new HashSet<>();
    private String clusterFromOneNode = "";
    public ArrayList<String> clusterNodes = new ArrayList<>();

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
        System.out.println("AAAA");
        for (String cluster : clusterNodes)
            System.out.println("{" + cluster + "} ");
    }

    /*clear trivial cluster */
    public void clear() {
        clusterFromOneNode = "";
        trivialClusters.clear();
    }

    public ArrayList<String> getTrivialClusters() {
        return new ArrayList<>(trivialClusters);
    }

}
