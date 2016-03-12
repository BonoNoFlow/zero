package com.bono.directory;

import com.bono.api.DBExecutor;
import com.bono.api.MPDCommand;
import com.bono.api.Reply;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import java.io.File;
import java.util.Iterator;

/**
 * Created by hendriknieuwenhuis on 07/03/16.
 */
public class Directory implements TreeExpansionListener, TreeWillExpandListener {

    private final String DIRECTORY_PREFIX  = "directory";
    private final String FILE_PREFIX       = "file";

    private DefaultMutableTreeNode root;       // root folder, mounted to the server

    private DBExecutor dbExecutor;

    private JTree tree;

    public Directory(JTree tree, DBExecutor dbExecutor) {
        this.dbExecutor = dbExecutor;
        this.tree = tree;
    }

    public Directory(JTree tree, DBExecutor dbExecutor, DefaultMutableTreeNode root) {
        this(tree, dbExecutor);
        this.root = root;
    }

    public void setRoot(DefaultMutableTreeNode root) {
        this.root = root;
    }

    @Override
    public void treeExpanded(TreeExpansionEvent event) {

    }

    @Override
    public void treeCollapsed(TreeExpansionEvent event) {

    }

    @Override
    public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
        System.out.println("Tree will expand!");
    }

    @Override
    public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {

    }

    public void loadChildren() {
        String response = "";

        try {
            response = dbExecutor.execute(new MPDCommand("lsinfo"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        populate(response);

        ((DefaultTreeModel) tree.getModel()).nodeStructureChanged(root);
    }

    public void populate(String entry) {



        Reply reply = new Reply(entry);
        Iterator<String> i = reply.iterator();
        while (i.hasNext()) {

            String[] line = i.next().split(Reply.SPLIT_LINE);

            switch (line[0]) {
                case DIRECTORY_PREFIX:
                    directoryNode(line[1]);

                    //System.out.println(line[0] + " " + line[1]);

                    break;
                case FILE_PREFIX:
                    fileNode(line[1]);

                    //System.out.println(line[0] + " " + line[1]);

                    break;
            }
        }
        //directory.reload();
    }

    private void directoryNode(String entry) {
        String[] name = entry.split(File.separator);
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(name[(name.length -1)], true);

        //root.add(node);

    }

    private void fileNode(String entry) {
        String[] name = entry.split(File.separator);
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(name[(name.length -1)], false);
        root.add(node);
    }


}
