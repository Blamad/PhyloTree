package tree;

import tree.exporter.Leaf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Blamad on 18.12.2016.
 */
public class Tree {

    private Node rootNode;

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
        else
            result += "+";

        System.out.println(result);

        Collections.sort(node.getChildren());
        for(Node childNode : node.getChildren())
        {
            processNode(childNode, depth+1);
        }
    }
}
