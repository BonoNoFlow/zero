package com.bono.directory;

import com.bono.api.DBExecutor;
import com.bono.api.MPDCommand;
import com.bono.api.Reply;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.*;
import java.io.*;
import java.util.Iterator;

/**
 * Created by hendriknieuwenhuis on 11/03/16.
 */
public class TestMPDDirectory implements TreeWillExpandListener {

    private final String DIRECTORY_PREFIX  = "directory";
    private final String FILE_PREFIX       = "file";

    private DefaultTreeModel directory;         // stores the directory structure as a tree model.

    private DBExecutor dbExecutor;

    public TestMPDDirectory(DBExecutor dbExecutor) {
        this.dbExecutor = dbExecutor;
        directory = new DefaultTreeModel(new DefaultMutableTreeNode("music", true));

    }

    @Override
    public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
        TreePath path = event.getPath();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();

        populate(loadChildren(node), node);
    }

    @Override
    public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {

    }

    public DefaultTreeModel getDirectory() {
        return directory;
    }

    public String loadChildren(DefaultMutableTreeNode node) {
        String para = node.toString();
        //if (para.equals("music")) para = null;
        String response = null;
        try {
            response = dbExecutor.execute(new MPDCommand("lsinfo", para));
            //System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public void populate(String entry, DefaultMutableTreeNode  node) {
        Reply reply = new Reply(entry);
        Iterator<String> i = reply.iterator();
        while (i.hasNext()) {

            String[] line = i.next().split(Reply.SPLIT_LINE);

            switch (line[0]) {
                case DIRECTORY_PREFIX:
                    directoryNode(line[1], node);

                    //System.out.println(line[0] + " " + line[1]);

                    break;
                case FILE_PREFIX:
                    fileNode(line[1], node);

                    //System.out.println(line[0] + " " + line[1]);

                    break;
            }
        }
        //directory.reload();
        directory.nodeStructureChanged((TreeNode) directory.getRoot());
    }

    public DefaultMutableTreeNode getRoot() {
        return (DefaultMutableTreeNode) directory.getRoot();
    }


    private void directoryNode(String entry, DefaultMutableTreeNode node) {
        String[] name = entry.split(java.io.File.separator);
        DefaultMutableTreeNode nodeNew = new DefaultMutableTreeNode(name[(name.length -1)], true);

        node.add(nodeNew);

    }

    private void fileNode(String entry, DefaultMutableTreeNode node) {
        String[] name = entry.split(java.io.File.separator);
        DefaultMutableTreeNode nodeNew = new DefaultMutableTreeNode(name[(name.length -1)], false);
        node.add(nodeNew);
    }

}
