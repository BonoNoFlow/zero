package com.bono.view;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * Created by hendriknieuwenhuis on 29/02/16.
 */
public class DirectoryView extends JTree {

    public DirectoryView() {
        super();
        setModel(new DefaultTreeModel(new DefaultMutableTreeNode("music")));
    }
}
