package com.bono.view;

import oracle.jvm.hotspot.jfr.JFRStackTraceRepository;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * Created by hendriknieuwenhuis on 29/02/16.
 */
public class DirectoryView {

    private JTree directory;
    private JScrollPane scrollPane;

    public DirectoryView() {
        build();
    }

    private void build() {
        directory = new JTree();
        scrollPane = new JScrollPane(directory);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public JTree getDirectory() {
        return directory;
    }
}
