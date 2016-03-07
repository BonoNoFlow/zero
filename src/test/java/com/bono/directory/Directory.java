package com.bono.directory;

import com.bono.api.Reply;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.Iterator;

/**
 * Created by hendriknieuwenhuis on 07/03/16.
 */
public class Directory {

    private final String DIRECTORY_PREFIX  = "directory";
    private final String FILE_PREFIX       = "file";

    private DefaultTreeModel directory;         // stores the directory structure as a tree model.

    private DefaultMutableTreeNode root;       // root folder, mounted to the server

    public Directory() {
        root = new DefaultMutableTreeNode("music",true);
        directory = new DefaultTreeModel(root);

    }



    public void populate(String entry) {



        Reply reply = new Reply(entry);
        Iterator<String> i = reply.iterator();
        while (i.hasNext()) {

            String[] line = i.next().split(Reply.SPLIT_LINE);

            switch (line[0]) {
                case DIRECTORY_PREFIX:
                    directoryNode(line[1]);
                    break;
                case FILE_PREFIX:
                    fileNode(line[1]);
                    break;
            }
        }

    }

    private void directoryNode(String entry) {
        //root = (DefaultMutableTreeNode) directory.getRoot();
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(entry, true);

        root.add(node);

    }

    private void fileNode(String entry) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(entry, false);
        root.add(node);
    }

    public DefaultTreeModel getDirectory() {
        return directory;
    }
}
