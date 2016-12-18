package tree;

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
        Set<Node> children = node.getChildren();
        if(children.isEmpty())
        {
            String result = "";
            while(--depth > 0)
                result += "\t";
            result += node.getName();
            System.out.println(result);
        }

        for(Node childNode : children)
        {
            processNode(childNode, depth+1);
        }
    }
}
