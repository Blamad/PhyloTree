package tree.exporter;

import jebl.evolution.graphs.Graph;
import jebl.evolution.graphs.Node;
import jebl.evolution.io.ImportException;
import jebl.evolution.io.NewickImporter;
import jebl.evolution.trees.SimpleRootedTree;
import jebl.evolution.trees.SimpleTree;
import tree.rooted.tree.Leaf;
import tree.rooted.tree.Tree;
import utils.PhyloTreeException;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

/**
 * Created by Blamad on 18.12.2016.
 */
public class NewickTreeExporter {

    public static Tree importRootedTree(String newick) throws PhyloTreeException
    {
        Tree tree = null;

        StringReader sr = new StringReader(newick);
        NewickImporter ni = new NewickImporter(sr, true);

        try {
            SimpleRootedTree srt = (SimpleRootedTree) ni.importNextTree();
            Node rootNode = srt.getRootNode();

            tree.rooted.tree.Node treeRootNode = createNode(srt, rootNode, false);
            populateTree(srt, rootNode, treeRootNode);

            tree = new Tree(treeRootNode.getChildren().get(0));

        } catch (IOException e) {
            throw new PhyloTreeException("Padam bo mogę.");
        } catch (ImportException e) {
            throw new PhyloTreeException("Podany łańcuch znaków nie spełnia wymagań formatu NEWICK!");
        }

        return tree;
    }

    private static tree.rooted.tree.Node createNode(SimpleRootedTree srt, Node node, Boolean leaf)
    {
        String name = null;
        String label = null;
        Double distance = null;

        if(srt.getTaxon(node) != null)
            name = srt.getTaxon(node).getName();
        else if (node.getAttribute("label") != null)
            label = (String) node.getAttribute("label");
        try {
            if (srt.getParent(node) != null)
                distance = srt.getEdgeLength(srt.getParent(node), node);
        } catch (Graph.NoEdgeException e) { }

        if(srt.hasLengths())
            distance = srt.getLength(node);

        if(leaf)
            return new Leaf(name, distance);
        else
            return new tree.rooted.tree.Node(label, distance);
    }

    private static void populateTree(SimpleRootedTree srt, Node node, tree.rooted.tree.Node parentNode)
    {
        List<Node> children = srt.getChildren(node);
        tree.rooted.tree.Node childNode = createNode(srt, node, children.isEmpty());
        parentNode.addChild(childNode);

        for(Node child : children)
        {
            populateTree(srt, child, childNode);
        }
    }
}