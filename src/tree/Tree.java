package tree;

import tree.exporter.Leaf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
        if(node instanceof Leaf)
        {
            Leaf leaf = (Leaf) node;
            String result = "";
            while(--depth > 0)
                result += "\t";
            result += leaf.getName();
            System.out.println(result);
            return;
        }

        List<Node> listOfChildren = new ArrayList();
        listOfChildren.addAll(node.getChildren());
        Collections.sort(listOfChildren);

        for(Node childNode : listOfChildren)
        {
            processNode(childNode, depth+1);
        }
    }
}
