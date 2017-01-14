package tree.utils;

import tree.rooted.RootedTree;
import tree.rooted.tree.Leaf;
import tree.rooted.tree.Node;
import tree.rooted.tree.DirectedTree;
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

    public static ClusterFamily mergeTwoClusters(RootedTree[] tab, float precent) {
        ClusterFamily mergedCluster = new ClusterFamily();

        int counter = 0;
        //add all equals clusters
        for (RootedTree tree : tab) {
            if (tree == null ) continue;
            for(String cluster: tree.getCluster().getTrivialClusters()){
                mergedCluster.add(sortClusterRow(cluster));
            }
            counter++;
        }

        /// wrzucamy wszytki klastry i zliczamy wystapienia
        Map<String, Integer> map = new HashMap<>();
        for (String s : mergedCluster.getTrivialClusters()){
            if(map.containsKey(s))
                map.replace(s,map.get(s)+1);
            else
                map.put(s,1);
        }

        for (String s : mergedCluster.getTrivialClusters())
        {
            if(map.get(s) / counter > precent)
                ;
            else
                s = "";
        }

        //TODO // sprawdzic czy klastry wyszly poprawne tak jak przy wczytywaniu
        List<String> clustersList = new ArrayList(mergedCluster.clusterNodes);

        if(!validateCluster(clustersList))
            return null;

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

    public static DirectedTree convertClusterToTree(ClusterFamily mergedCluster) {

        ArrayList<String> tab = new ArrayList<>(mergedCluster.getTrivialClusters());
        tab.sort(new StringLengthComparator());

        Node root = new Node(tab.get(0));
        makeSomethingLikeHesseGraph(root, tab.subList(0, tab.size()));
        cleanThatShitBFS(root);
        removeNodeCoverdByChildren(root);
        makeLeavesFromNodes(root);

        return new DirectedTree(root);
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

        if(!validateCluster(clustersList))
            return null;

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

    private static Boolean validateCluster(List<String> clustersList) {
        //Lista posortowana malejąco
        Collections.sort(clustersList, new StringLengthComparator());

        //Walidacja NOWA
        List<Set<String>> listOfSplitWorlds = new ArrayList();
        for(String cluster : clustersList)
            listOfSplitWorlds.add(wordsToSet(cluster));

        for(int i = listOfSplitWorlds.size()-1; i >= 0 ; i--) {
            Set<String> walidatedCluster = listOfSplitWorlds.get(i);
            Boolean isValidCluster = false;

            for(int j = i - 1; j >= 0; j--) {
                Set<String> lookedAtCluster = listOfSplitWorlds.get(j);

                //Walidowany klaster jest nad przegladanym zbiorem a przegladany jest podzbiorem walidowanego
                if(lookedAtCluster.containsAll(walidatedCluster)) {
                    isValidCluster = true;
                    lookedAtCluster.removeAll(walidatedCluster);
                }
            }
            if(isValidCluster)
                walidatedCluster.clear();
        }

        //Sprawdzamy czy wszystko mialo relacje
        for(Set cluster : listOfSplitWorlds)
            if(!cluster.isEmpty())
                return false;
        return true;
    }

    private static HashSet<String> wordsToSet(String words) {
        HashSet<String> set = new HashSet();
        set.addAll(Arrays.asList(words.split(" ")));
        return set;
    }
}
