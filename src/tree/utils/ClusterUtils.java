package tree.utils;

import tree.rooted.tree.Leaf;
import tree.rooted.tree.Node;
import tree.rooted.tree.Tree;
import tree.rooted.cluster.ClusterFamily;
import tree.utils.comparators.StringLengthComparator;
import tree.utils.comparators.StringValueComparator;

import java.util.*;

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

    private static void makeSomethingLikeHesseGraph(Node root, List<String> tab) {
        for(int i = 1; i < tab.size(); i++){
            String s = tab.get(i);
            Node tmpNode = new Node(s);
            if(root.label.contains(s)) {
                root.addChild(tmpNode);
                makeSomethingLikeHesseGraph(tmpNode, tab.subList(i,tab.size()));
            }
        }
    }
    private static void cleanThatShitBFS(Node node)
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
            cleanThatShitBFS(n);
        }
    }

    private static void removeNodeCoverdByChildren(Node n)
    {
        for (Node node :n.getChildren()) {
            n.label = n.label.replace(node.getLabel(), "");
            removeNodeCoverdByChildren(node);
        }
    }

    public static Tree convertClusterToTree(ClusterFamily mergedCluster) {

        ArrayList<String> tab = new ArrayList<>(mergedCluster.getTrivialClusters());
        tab.sort(new StringLengthComparator());

        Node root = new Node(tab.get(0));
        makeSomethingLikeHesseGraph(root, tab.subList(0, tab.size()));
        cleanThatShitBFS(root);
        removeNodeCoverdByChildren(root);
        makeLeavesFromNodes(root);

        return new Tree(root);
    }

    private static void makeLeavesFromNodes(Node root) {
        for (Node n: root.getChildren()) {
            makeLeavesFromNodes(n);
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

    public static String sortClusterRow(String clusterRow)
    {
        Boolean braces = clusterRow.charAt(0) == '{' && clusterRow.charAt(clusterRow.length()-1) == '}';
        Boolean comma = clusterRow.contains(",");

        String tmp = clusterRow.replaceAll("[{},]", "").trim(); //Wywala przecinki, nawiasy i zostawia tylko slowa oddzielone spacjami
        String nodes[] = tmp.split(" "); // rozbija slowa na spacjach w tablice
        List<String> nodesList = Arrays.asList(nodes);
        Collections.sort(nodesList, new StringValueComparator()); //sort na liscie slow

        String wynik = "";
        String separator = " ";
        if(comma)
            separator += ",";

        for(String node : nodesList)
            wynik += node + separator;

        wynik = wynik.substring(0, wynik.length()-separator.length());

        if(braces)
            return "{"+ wynik+"}";
        else
            return wynik;
    }

    public static ClusterFamily createCluster(String[] clusters)
    {
        List<String> clustersList = new ArrayList();

        for(String cluster : clusters)
            clustersList.add(sortClusterRow(cluster));
        //Lista posortowana malejąco
        Collections.sort(clustersList, new StringLengthComparator());

        //Walidacja
        for(String cluster : clustersList) {
            Set<String> walidatedCluster = wordsToSet(cluster);
            Boolean isValidCluster = false;

            for(String clusterer : clustersList) {
                if(clusterer.equals(cluster))
                    continue;

                Set<String> lookedAtCluster = wordsToSet(clusterer);
                if(walidatedCluster.containsAll(lookedAtCluster) || lookedAtCluster.containsAll(walidatedCluster)) {
                    isValidCluster = true;
                    break;
                }
            }
            if(!isValidCluster)
                return null;
        }

        //Tworzenie klastra
        ClusterFamily clusterFamily = new ClusterFamily();
        for(String cluster : clustersList)
            clusterFamily.add(cluster);

        return clusterFamily;
    }

    public static int countRF(ClusterFamily first, ClusterFamily second) {
        int sumRF = 0;
        for (String cluster : first.getTrivialClusters())
            if (!second.getTrivialClusters().contains(cluster))
                sumRF++;
        for (String cluster : second.getTrivialClusters())
            if (!first.getTrivialClusters().contains(cluster))
                sumRF++;

        return sumRF/2;
    }

    private static HashSet<String> wordsToSet(String words) {
        HashSet<String> set = new HashSet();
        set.addAll(Arrays.asList(words.split(" ")));
        return set;
    }
}
