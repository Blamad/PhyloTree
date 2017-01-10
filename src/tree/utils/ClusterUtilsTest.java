package tree.utils;

import tree.rooted.tree.Tree;
import tree.exporter.NewickTreeExporter;
import tree.rooted.cluster.ClusterFamily;

/**
 * Created by baton on 1/8/2017.
 */
public class ClusterUtilsTest {

    @org.junit.Test
    public void testMergeTwoClusters() throws Exception {
        Tree t1, t2;
        String s = "(A,((B,C),D))R;";
        t1 = NewickTreeExporter.importTree(s);
       // t1.printTreeToConsole();
        s = "(A,((D,C),B))R;";
        t2 = NewickTreeExporter.importTree(s);
      //  t2.printTreeToConsole();
        t1.transformToTrivialCluster().print();
        t2.transformToTrivialCluster().print();
        ClusterFamily mergedCluster = ClusterUtils.mergeTwoClusters(t1.transformToTrivialCluster(), t2.transformToTrivialCluster());
        System.out.println("PO ZLACZENIU");
        mergedCluster.print();
        System.out.println("Zamiana na drzewo");
        Tree mergedTree = ClusterUtils.convertClusterToTree(mergedCluster);

    }

    @org.junit.Test
    public void testConvertTrivialClusterToTree() throws Exception {
        Tree t1;
        //String s = "(A,(B,C,D))R;";
        String s = "(C,(B,(A,(D,E))))R;";
        t1 = NewickTreeExporter.importTree(s);
        t1.printTreeToConsole();
        System.out.println(s);
        System.out.println("Zamiana na drzewo");
        Tree mergedTree = ClusterUtils.convertClusterToTree(t1.transformToTrivialCluster());
        mergedTree.printTreeToConsole();
    }

    @org.junit.Test
    public void testConvertTrivialClusterToTree2() throws Exception {
        Tree t1;
        //String s = "(A,(B,C,D))R;";
        String s = "(((F,G)D,(J,(N,L)K,T)E)B,((X,Y,S)H,(W,V,M)I)C)A;";
        t1 = NewickTreeExporter.importTree(s);
        t1.printTreeToConsole();
        System.out.println(s);
        System.out.println("Zamiana na drzewo");
        Tree mergedTree = ClusterUtils.convertClusterToTree(t1.transformToTrivialCluster());
        mergedTree.printTreeToConsole();
    }
}