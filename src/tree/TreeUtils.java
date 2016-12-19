package tree;

import java.util.List;

/**
 * Created by Blamad on 19.12.2016.
 */
public class TreeUtils {

    public static Tree copyTree(Tree tree)
    {
        Node node = tree.getRootNode().clone();
        return new Tree(node);
    }

    private static void deepCopy(Node node, Node copy)
    {
        for(Node child : node.getChildren())
        {
            Node newNode = child.clone();
            copy.setParent(newNode);
            deepCopy(child, newNode);
        }
    }

    public static Tree pruneTreeLeavingLeaves(Tree tree, List<String> leaves)
    {
        Node newRoot = tree.getRootNode().clone();
        Tree prunedTree = new Tree(newRoot);

        pruneTree(tree.getRootNode(), newRoot, leaves);
        //TODO to jeszcze nie dziala. Ale bedzie
        //cleanUpStructure(newRoot);

        return prunedTree;
    }

    //Sprzatamy wiszace galezie bez lisci, bezsensowne przedluzenia galezi itp.
    private static void cleanUpStructure(Node node)
    {
        if(node instanceof Leaf)
            return;

        //Wiszaca galaz
        if(node.getChildren().size() == 0 && node.getParent() != null)
        {
            node.getParent().getChildren().remove(node);
            cleanUpStructure(node.getParent());
        } else
        //Galaz przedluzajaca (jedyny potomek rodzica i z pojedynczym dzickiem po drugiej stronie
        if(node.getChildren().size() == 1 && node.getParent() != null && node.getParent().getChildren().size() == 1)
        {
            Node onlyChild = node.getChildren().get(0);
            onlyChild.setDistanceToParent(onlyChild.getDistanceToParent() + node.getDistanceToParent());

            //Wyciagniecie ze struktury zbednego wezla
            node.getParent().getChildren().clear();
            node.getParent().getChildren().add(onlyChild);
            onlyChild.setParent(node.getParent());
            node.setParent(null);
        }

        for(Node child : node.getChildren())
        {
            cleanUpStructure(child);
        }
    }

    private static void pruneTree(Node node, Node copy, List<String> leaves)
    {
        for(Node child : node.getChildren()) {

            //Jezeli wpadlismy na lisc do usuniecia to go nie kopiujemy
            if (child instanceof Leaf && !leaves.contains(((Leaf) child).getName()))
                return;
            else {
                Node newNode = child.clone();
                copy.addChild(newNode);
                pruneTree(child, newNode, leaves);
            }
        }
    }
}
