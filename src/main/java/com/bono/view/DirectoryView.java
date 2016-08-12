package com.bono.view;



import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

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
        directory = new JTree(new DefaultMutableTreeNode());
        ((DefaultMutableTreeNode)directory.getModel().getRoot()).add(new DefaultMutableTreeNode("loading..."));
        //database.setShowsRootHandles(true);
        directory.setDragEnabled(true);

        //database.setCellRenderer(new DirectoryCellRenderer());
        scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().add(directory);
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public JTree getDirectory() {
        return directory;
    }


}
