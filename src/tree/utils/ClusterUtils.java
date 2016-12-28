package tree.utils;

import tree.Tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Aga on 2016-12-28.
 */
public class ClusterUtils {

    public static TrivialCluster mergeTwoClusters(TrivialCluster trivialClusterFirst, TrivialCluster trivialClusterSecond) {
        TrivialCluster mergedCluster = new TrivialCluster();
        ArrayList<String> firstClusters = copyCluster(trivialClusterFirst).getTrivialClusters();
        //Collections.sort(firstClusters, new LengthComparator());
        int firstClusterSize = firstClusters.size();
        ArrayList<String> secondClusters = copyCluster(trivialClusterSecond).getTrivialClusters();
        //Collections.sort(secondClusters, new LengthComparator());
        int secondClusterSize = secondClusters.size();

        //add all equals clusters
        for (int first = 0; first < firstClusterSize; first++) {
            for (int sec = 0; sec < secondClusterSize; sec++) {
                if (clustersAreTheSame(firstClusters.get(first), secondClusters.get(sec))) {
                    mergedCluster.add(firstClusters.get(first));
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
