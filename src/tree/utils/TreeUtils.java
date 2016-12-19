package tree.utils;

import tree.Leaf;
import tree.Node;
import tree.Tree;

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
        cleanUpStructure(newRoot);

        return prunedTree;
    }

    //Sprzatamy wiszace galezie bez lisci, bezsensowne przedluzenia galezi itp.
    private static void cleanUpStructure(Node node)
    {
        if(node instanceof Leaf)
            return;

        Node parent = node.getParent();
        //Skoro to nie jest lisc to sprawdzamy jak można to uporządkować. Chcemy usunąć:

        //Wiszaca galaz, sama sie odpina.
        if(node.getChildren().size() == 0 && parent != null) {
            parent.getChildren().remove(node);

            node.setParent(null);
            cleanUpStructure(parent);
            return;
        }

        //Galaz przedluzajaca (jedyny potomek rodzica i z pojedynczym dzickiem po drugiej stronie
        if(node.getChildren().size() == 1 && parent != null && parent.getChildren().size() == 1)
        {
            Node onlyChild = node.getChildren().get(0);
            onlyChild.setDistanceToParent(onlyChild.getDistanceToParent() + node.getDistanceToParent());
            //Wyciagniecie ze struktury zbednego wezla
            parent.getChildren().clear();
            parent.getChildren().add(onlyChild);
            onlyChild.setParent(parent);

            node.setParent(null);
            cleanUpStructure(parent);
            return;
        }

        //Czemu tak? Bo przez rekursje nasza lista może się zmieniać w trakcie pętli.
        //Java tego nie lubi więc trzeba to jakoś obejść i mądrze obsłużyć.
        //Nad mądrością rozwiązania mozna polemizować.. Ale działa.
        List<Node> childrenList = node.getChildren();
        for(int i = 0; i < childrenList.size(); i++)
        {
            Integer sizeBefore = childrenList.size();
            cleanUpStructure(childrenList.get(i));

            if(childrenList.size() != sizeBefore)
                i--;
        }
    }

    private static void pruneTree(Node node, Node copy, List<String> leaves)
    {
        for(Node child : node.getChildren()) {

            //Jezeli wpadlismy na lisc do usuniecia to go nie kopiujemy
            if (child instanceof Leaf && !leaves.contains(((Leaf) child).getName()))
                continue;
            else {
                Node newNode = child.clone();
                copy.addChild(newNode);
                pruneTree(child, newNode, leaves);
            }
        }
    }
}
