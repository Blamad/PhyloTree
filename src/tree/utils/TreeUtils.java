package tree.utils;

import tree.rooted.cluster.ClusterFamily;
import tree.rooted.tree.Leaf;
import tree.rooted.tree.Node;
import tree.rooted.tree.DirectedTree;
import tree.unrooted.split.Split;
import tree.unrooted.split.SplitFamily;
import tree.unrooted.tree.Tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Blamad on 19.12.2016.
 */
public class TreeUtils {

    public static DirectedTree copyTree(DirectedTree directedTree)
    {
        Node node = directedTree.getRootNode().clone();
        return new DirectedTree(node);
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

    public static DirectedTree pruneTreeLeavingLeaves(DirectedTree directedTree, List<String> leaves)
    {
        Node newRoot = directedTree.getRootNode().clone();
        DirectedTree prunedDirectedTree = new DirectedTree(newRoot);

        pruneTree(directedTree.getRootNode(), newRoot, leaves);
        cleanUpStructure(newRoot);

        return prunedDirectedTree;
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

    public static ClusterFamily convertTreeToCluster(DirectedTree directedTree) {
        ClusterFamily cluster = new ClusterFamily();

        Node node = directedTree.getRootNode();
        if (node != null) {
            getAllNodes(node, cluster);
        }

        return cluster;
    }

    private static void getAllNodes(Node node, ClusterFamily cluster) {
        preOrder(node, cluster);
        cluster.saveClusterFromOneNode();
        if (node instanceof Leaf)
            return;
        else {
            for (Node childNode : node.getChildren()) {
                if (!(childNode instanceof Leaf))
                    getAllNodes(childNode, cluster);
            }
        }
    }

    private static void preOrder(Node node, ClusterFamily cluster) {
        if (node instanceof Leaf) {
            Leaf leaf = (Leaf) node;
            cluster.addToClusterFromOneNode(leaf.getName());
        }
        else {
            for (Node childNode : node.getChildren()) {
                preOrder(childNode, cluster);
            }
        }
    }

    //////////////////////////////////////////////////
    //                                              //
    //              Unrooted tree stuff             //
    //                                              //
    //////////////////////////////////////////////////

    public static SplitFamily convertTreeToSplit(Tree tree, tree.unrooted.tree.Node startNode) {
        SplitFamily splitFamily = new SplitFamily(startNode);
        Split split;

        List<tree.unrooted.tree.Node> internalNodes = new ArrayList();
        internalNodes.addAll(tree.getNodes());
        internalNodes.removeAll(tree.getExternalNodes()); //usuwamy wszystkie liscie

        Stack<tree.unrooted.tree.Node> nodeStack = new Stack();
        for(tree.unrooted.tree.Node node : startNode.getNeighbours())
            if(!node.isExternal()) {
                nodeStack.push(node);
                internalNodes.remove(node);
            }

        tree.unrooted.tree.Node splitedNode;
        //Liscie dla kazdego wezla.
        List<tree.unrooted.tree.Node> externalNodes = new ArrayList();

        while(!nodeStack.isEmpty()) {
            split = new Split();
            splitFamily.addSplit(split);
            externalNodes.clear();
            splitedNode = nodeStack.pop();

            for(tree.unrooted.tree.Node node : splitedNode.getNeighbours()) {
                if(node.isExternal())
                    externalNodes.add(node);
                else {
                    split.addSubSet(getAllExternalNodes(node, splitedNode));
                    if(internalNodes.contains(node)) {
                        nodeStack.push(node);
                        internalNodes.remove(node);
                    }
                }
            }
            if(!externalNodes.isEmpty())
                split.addSubSet(externalNodes);
        }

        return splitFamily;
    }

    private static List<tree.unrooted.tree.Node> getAllExternalNodes(tree.unrooted.tree.Node startNode, tree.unrooted.tree.Node previousNode) {
        List<tree.unrooted.tree.Node> nodes = new ArrayList();
        for(tree.unrooted.tree.Node node : startNode.getNeighbours()) {
            if(node.equals(previousNode))
                continue;

            if(node.isExternal())
                nodes.add(node);
            else
                nodes.addAll(getAllExternalNodes(node, startNode));
        }

        return nodes;
    }
}
