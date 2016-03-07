package com.bono.directory;

import javax.swing.*;
import javax.swing.tree.TreeModel;

/**
 * Created by hendriknieuwenhuis on 07/03/16.
 */
public class ViewDir {

    private JFrame frame;
    private JTree tree;

    public ViewDir(TreeModel treeModel) {
        frame = new JFrame("test dir");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tree = new JTree(treeModel);
        tree.setCellRenderer(new TestTreeCellRenderer());
        frame.getContentPane().add(tree);

        frame.pack();
        frame.setVisible(true);
    }
}
