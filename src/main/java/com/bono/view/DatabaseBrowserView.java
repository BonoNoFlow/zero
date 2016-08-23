package com.bono.view;

import javax.swing.*;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseListener;

/**
 * Created by bono on 8/20/16.
 */
public class DatabaseBrowserView extends JScrollPane implements BrowserView {

    private JTree tree;

    public DatabaseBrowserView() {
        super();
        build();
    }

    private void build() {
        tree = new JTree(new DefaultMutableTreeNode());
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
        tree.setRootVisible(false);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        getViewport().add(tree);
    }

    @Override
    public void setRoot(TreeNode root) {
        tree.setModel(new DefaultTreeModel(root));
    }

    @Override
    public TreeModel getModel() {
        return tree.getModel();
    }

    @Override
    public Component getComponent() {
        return tree;
    }

    @Override
    public TreeSelectionModel getBrowserTreeSelectionModel() {
        return tree.getSelectionModel();
    }

    @Override
    public void addBrowserMouseListener(MouseListener l) {
        tree.addMouseListener(l);
    }

    @Override
    public void addBrowserTreeWillExpandListener(TreeWillExpandListener l) {
        tree.addTreeWillExpandListener(l);
    }

    @Override
    public void removeBrowserTreeWillExpandListener(TreeWillExpandListener l) {
        tree.removeTreeWillExpandListener(l);
    }
}
