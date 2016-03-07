package com.bono.directory;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * Created by hendriknieuwenhuis on 07/03/16.
 */
public class Folder {

    private DefaultTreeModel directory;         // stores the directory structure as a tree model.

    private DefaultMutableTreeNode root;       // folder node

    public Folder(DefaultTreeModel directory, String folder) {
        this.directory = directory;
        //this.root = root;
        addFolder(folder);
    }

    private void addFolder(String folder) {
        root = (DefaultMutableTreeNode) directory.getRoot();
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(folder, true);
        root.add(node);
    }
}
