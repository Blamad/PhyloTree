package tree.utils;

import java.util.ArrayList;

/**
 * Created by Aga on 2016-12-28.
 */
public class TrivialCluster {
    private ArrayList<String> trivialClusters = new ArrayList<>();
    private String clusterFromOneNode = "";

    public void addToClusterFromOneNode(String nodeName) {
        this.clusterFromOneNode += nodeName + " ";
    }

    public void saveClusterFromOneNode() {
        //remove last char
        StringBuilder sb = new StringBuilder(clusterFromOneNode);
        sb.deleteCharAt(clusterFromOneNode.length()-1);
        this.trivialClusters.add(sb.toString());
        clusterFromOneNode = "";
    }

    public void print() {
        for (String cluster : trivialClusters)
            System.out.println("{" + cluster + "} ");
    }
}
