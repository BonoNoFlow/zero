package com.bono.directory;

import javax.swing.*;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import java.awt.event.MouseListener;

/**
 * Created by hendriknieuwenhuis on 07/03/16.
 */
public class ViewDir {

    private JFrame frame;
    private JTree tree;

    public ViewDir() {
        frame = new JFrame("test dir");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JScrollPane pane = new JScrollPane();
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        tree = new JTree(new DefaultMutableTreeNode("root", true));
        //((DefaultMutableTreeNode)tree.getModel().getRoot()).setAllowsChildren(true);
        tree.setRootVisible(false);
        tree.setShowsRootHandles(false);
        tree.setCellRenderer(new TestTreeCellRenderer());
        pane.getViewport().add(tree);
        frame.getContentPane().add(pane);
    }

    public ViewDir(TreeModel treeModel) {
        frame = new JFrame("test dir");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JScrollPane pane = new JScrollPane();
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        tree = new JTree(treeModel);
        tree.setShowsRootHandles(true);
        tree.setCellRenderer(new TestTreeCellRenderer());
        pane.getViewport().add(tree);
        frame.getContentPane().add(pane);

        frame.pack();
        frame.setVisible(true);
    }

    public DefaultMutableTreeNode getRoot() {
        return (DefaultMutableTreeNode) tree.getModel().getRoot();
    }

    public JTree getTree() {
        return tree;
    }
    public void addMouseListener(MouseListener listener) {
        tree.addMouseListener(listener);
    }

    public void addTreeWillExpandListener(TreeWillExpandListener listener) {
        tree.addTreeWillExpandListener(listener);
    }

    public void show() {
        frame.pack();
        frame.setVisible(true);
    }
}
