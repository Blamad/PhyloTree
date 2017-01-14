package tree.utils;

/**
 * Created by baton on 1/8/2017.
 */
public class ClusterUtilsTest {

    /*@org.junit.Test
    public void testMergeTwoClusters() throws Exception {
        DirectedTree t1, t2;
        String s = "(A,((B,C),D))R;";
        t1 = NewickTreeExporter.importRootedTree(s);
       // t1.print();
        s = "(A,((D,C),B))R;";
        t2 = NewickTreeExporter.importRootedTree(s);
      //  t2.print();
        RootedTree tree1 = new RootedTree(t1);
        RootedTree tree2 = new RootedTree(t2);

        tree1.getCluster().print();
        tree2.getCluster().print();

        ClusterFamily mergedCluster = ClusterUtils.mergeTwoClusters(tree1.getCluster(), tree2.getCluster());
        System.out.println("PO ZLACZENIU");
        mergedCluster.print();
        System.out.println("Zamiana na drzewo");
        DirectedTree mergedTree = ClusterUtils.convertClusterToTree(mergedCluster);

    }

    /*
    @org.junit.Test
    public void testConvertTrivialClusterToTree() throws Exception {
        DirectedTree t1;
        //String s = "(A,(B,C,D))R;";
        String s = "(C,(B,(A,(D,E))))R;";
        t1 = NewickTreeExporter.importRootedTree(s);
        t1.print();
        System.out.println(s);
        System.out.println("Zamiana na drzewo");
        DirectedTree mergedTree = ClusterUtils.convertClusterToTree(t1.transformToTrivialCluster());
        mergedTree.print();
    }

    @org.junit.Test
    public void testConvertTrivialClusterToTree2() throws Exception {
        DirectedTree t1;
        //String s = "(A,(B,C,D))R;";
        String s = "(((F,G)D,(J,(N,L)K,T)E)B,((X,Y,S)H,(W,V,M)I)C)A;";
        t1 = NewickTreeExporter.importRootedTree(s);
        t1.print();
        System.out.println(s);
        System.out.println("Zamiana na drzewo");
        DirectedTree mergedTree = ClusterUtils.convertClusterToTree(t1.transformToTrivialCluster());
        mergedTree.print();
    }
    */

}