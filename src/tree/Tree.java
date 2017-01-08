package tree;

import tree.utils.ClusterFamily;

import java.text.DecimalFormat;
import java.util.Collections;

/**
 * Created by Blamad on 18.12.2016.
 */
public class Tree {

    private Node rootNode;
    private ClusterFamily clusterFamily = new ClusterFamily();

    public Tree(Node rootNode)
    {
        this.rootNode = rootNode;
    }

    public Node getRootNode()
    {
        return rootNode;
    }

    public void printTreeToConsole()
    {
        processNode(rootNode, 0);
    }

    private void processNode(Node node, Integer depth)
    {
        String result = "";
        for(int d = depth; d > 0; d--)
            result += "\t";

        if(node instanceof Leaf)
        {
            Leaf leaf = (Leaf) node;
            result += leaf.getName();
        }
        else {
            result += "+";
            if(node.getLabel() != null)
                result += "("+node.getLabel()+")";
        }

        if(node.getDistanceToParent() != null && node != rootNode)
            result += " [" + new DecimalFormat("0.##").format(node.getDistanceToParent()) + "]";

        System.out.println(result);

        Collections.sort(node.getChildren());
        for(Node childNode : node.getChildren())
        {
            processNode(childNode, depth+1);
        }
    }

    public ClusterFamily transformToTrivialCluster() {
        Node node = rootNode;
        if (node != null) {
            getAllNodes(node);
        }
        return clusterFamily;
    }

    private void getAllNodes(Node node) {
        preOrder(node);
        clusterFamily.saveClusterFromOneNode();
        if (node instanceof Leaf)
            return;
        else {
            for (Node children : node.getChildren()) {
                if (!(children instanceof Leaf))
                    getAllNodes(children);
            }
        }
    }

    private void preOrder(Node node) {
        if (node instanceof Leaf) {
            Leaf leaf = (Leaf) node;
            clusterFamily.addToClusterFromOneNode(leaf.getName());
        }
        else {
            for (Node children : node.getChildren()) {
                preOrder(children);
            }
        }
    }
}