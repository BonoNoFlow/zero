package com.bono.directory;

import javax.swing.*;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.TreeModel;
import java.awt.event.MouseListener;

/**
 * Created by hendriknieuwenhuis on 07/03/16.
 */
public class ViewDir {

    private JFrame frame;
    private JTree tree;

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

    public void addMouseListener(MouseListener listener) {
        tree.addMouseListener(listener);
    }

    public void addTreeWillExpandListener(TreeWillExpandListener listener) {
        tree.addTreeWillExpandListener(listener);
    }
}
