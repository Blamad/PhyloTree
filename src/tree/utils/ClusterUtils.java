package tree.utils;

import tree.Leaf;
import tree.Node;
import tree.Tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Aga on 2016-12-28.
 */
public class ClusterUtils {

    public static ClusterFamily mergeTwoClusters(ClusterFamily clusterFamilyFirst, ClusterFamily clusterFamilySecond) {
        ClusterFamily mergedCluster = new ClusterFamily();
        ArrayList<String> firstClusters = copyCluster(clusterFamilyFirst).getTrivialClusters();
        ArrayList<String> secondClusters = copyCluster(clusterFamilySecond).getTrivialClusters();

        //add all equals clusters
        for (String firstCluster : firstClusters) {
            for (String secondCluster : secondClusters) {
                if (clustersAreTheSame(firstCluster, secondCluster)) {
                    mergedCluster.add(firstCluster);
                }
            }
        }

        return mergedCluster;
    }

    private static boolean clustersAreTheSame(String first, String second) {
        ArrayList<String> firstList = new ArrayList<>();
        firstList.addAll(Arrays.asList(first.split(" ")));
        ArrayList<String> secondList = new ArrayList<>();
        secondList.addAll(Arrays.asList(second.split(" ")));
        return secondList.containsAll(firstList) && firstList.containsAll(secondList);
    }


    public static class MyComparator implements java.util.Comparator<String> {

        public MyComparator() {
            super();
        }

        public int compare(String s1, String s2) {
            int dist1 = Math.abs(s1.length());
            int dist2 = Math.abs(s2.length());

            return dist2 - dist1;
        }
    }
    public static void make_tree_from_this_shit(Node root, List<String> tab) {
        for(int i = 1; i < tab.size(); i++){
            String s = tab.get(i);
            Node tmpNode = new Node(s);
            if(root.label.contains(s)) {
                root.addChild(tmpNode);
                make_tree_from_this_shit(tmpNode, tab.subList(i,tab.size()));
            }
        }
    }
    public static void clean_that_shit_BFS(Node node)
    {
        for(int i = 0; i < node.getChildren().size(); i++)
        {
            for(int j = i+1; j < node.getChildren().size();j++)
            {
                if(node.getChildren().get(i).label.contains(node.getChildren().get(j).label))
                {
                    node.getChildren().remove(j);
                    i = 0;
                    j = 0;
                }
            }
        }
        for (Node n: node.getChildren()) {
            clean_that_shit_BFS(n);
        }
    }

    public static void grow_fucking_tree_BFS(Node n)
    {
        for (Node node :n.getChildren()) {
            n.label = n.label.replace(node.getLabel(), "");
            grow_fucking_tree_BFS(node);
        }
    }

    public static Tree convertTrivialClusterToTree(ClusterFamily mergedCluster) {

        MyComparator com = new MyComparator();
        ArrayList<String> tab = new ArrayList<>(mergedCluster.getTrivialClusters());
        tab.sort(com);
        for (String clus :tab) {
            System.out.println(clus);
        }

        Node root = new Node(tab.get(0));
        Node tmpRoot = root;
        make_tree_from_this_shit(root, tab.subList(0, tab.size()));
        clean_that_shit_BFS(root);
        grow_fucking_tree_BFS(root);
        make_it_amazing(root);


        return new Tree(root);
    }

    private static void make_it_amazing(Node root) {
        for (Node n: root.getChildren()) {
            make_it_amazing(n);
        }
        if (root.getLabel() != null) {
            for (char s : root.getLabel().toCharArray()) {
                //System.out.println(s);
                if (s != ' ') {
                    root.addChild(new Leaf("" + s, root.getDistanceToParent()));
                }
            }
            root.label = "";
        }
    }


    private static ClusterFamily copyCluster(ClusterFamily cluster) {
        ClusterFamily newCluster = new ClusterFamily();
        for (String clus : cluster.getTrivialClusters()) {
            newCluster.add(clus);
        }
        return newCluster;
    }
}
