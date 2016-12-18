package tree.exporter;

import jebl.evolution.graphs.Graph;
import jebl.evolution.graphs.Node;
import jebl.evolution.io.ImportException;
import jebl.evolution.io.NewickImporter;
import jebl.evolution.trees.SimpleRootedTree;
import tree.Tree;
import utils.PhyloTreeException;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

/**
 * Created by Blamad on 18.12.2016.
 */
public class NewickTreeExporter {

    public static Tree importTree(String newick) throws PhyloTreeException
    {
        Tree tree = null;

        StringReader sr = new StringReader(newick);
        NewickImporter ni = new NewickImporter(sr, true);

        try {
            SimpleRootedTree srt = (SimpleRootedTree) ni.importNextTree();
            Node rootNode = srt.getRootNode();

            tree = new Tree(createNode(srt, rootNode));
            populateTree(srt, rootNode, tree.getRootNode());

        } catch (IOException e) {
            throw new PhyloTreeException("Padam bo mogę.");
        } catch (ImportException e) {
            throw new PhyloTreeException("Podany łańcuch znaków nie spełnia wymagań formatu NEWICK!");
        }

        return tree;
    }

    private static tree.Node createNode(SimpleRootedTree srt, Node node)
    {
        String name = null;
        Double distance = null;

        if(srt.getTaxon(node) != null)
            name = srt.getTaxon(node).getName();
        try {
            if (srt.getParent(node) != null)
                distance = srt.getEdgeLength(srt.getParent(node), node);
        } catch (Graph.NoEdgeException e) { }

        return new tree.Node(name, distance);
    }

    private static void populateTree(SimpleRootedTree srt, Node node, tree.Node parentNode)
    {
        List<Node> children = srt.getChildren(node);
        tree.Node childNode = createNode(srt, node);
        parentNode.addChild(createNode(srt, node));

        for(Node child : children)
        {
            populateTree(srt, child, childNode);
        }
    }

    private void test2(String newick) {
        StringReader sr = new StringReader(newick);
        NewickImporter ni = new NewickImporter(sr, true);

        try {
            SimpleRootedTree srt = (SimpleRootedTree) ni.importNextTree();
            Node rootNode = srt.getRootNode();
            processNode(srt, rootNode, 0);
        } catch (Exception e) { }
    }

    private void processNode(SimpleRootedTree srt, Node node, Integer depth)
    {
        List<Node> children = srt.getChildren(node);
        if(children.isEmpty())
        {
            String result = "";
            while(--depth > 0)
                result += "\t";
            result += srt.getTaxon(node);
            System.out.println(result);
        }

        for(Node childNode : children)
        {
            processNode(srt, childNode, depth+1);
        }
    }
}