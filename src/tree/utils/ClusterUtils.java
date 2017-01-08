package tree.utils;

import tree.Leaf;
import tree.Node;
import tree.Tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Aga on 2016-12-28.
 */
public class ClusterUtils {

    public static TrivialCluster mergeTwoClusters(TrivialCluster trivialClusterFirst, TrivialCluster trivialClusterSecond) {
        TrivialCluster mergedCluster = new TrivialCluster();
        ArrayList<String> firstClusters = copyCluster(trivialClusterFirst).getTrivialClusters();
        ArrayList<String> secondClusters = copyCluster(trivialClusterSecond).getTrivialClusters();

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


    public static Tree convertTrivialClusterToTree(TrivialCluster mergedCluster) {
        return null;
    }

    private static TrivialCluster copyCluster(TrivialCluster cluster) {
        TrivialCluster newCluster = new TrivialCluster();
        for (String clus : cluster.getTrivialClusters()) {
            newCluster.add(clus);
        }
        return newCluster;
    }
}
