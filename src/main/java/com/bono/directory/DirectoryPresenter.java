package com.bono.directory;

import com.bono.api.DBExecutor;
import com.bono.api.Database;
import com.bono.api.DefaultCommand;
import com.bono.api.Reply;
import com.bono.view.DirectoryView;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.MutableTreeNode;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hendriknieuwenhuis on 26/05/16.
 */
public class DirectoryPresenter extends Database implements TreeWillExpandListener {

    private final String DIRECTORY_PREFIX  = "directory";
    private final String FILE_PREFIX       = "file";

    private DefaultMutableTreeNode root = null;

    private JTree tree;

    public DirectoryPresenter(DBExecutor dbExecutor, DirectoryView directoryView) {
        super(dbExecutor);
        tree = directoryView.getDirectory();
        root = (DefaultMutableTreeNode) tree.getModel().getRoot();
    }

    @Override
    public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
        System.out.println("Tree will expand");

        DefaultMutableTreeNode current = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();
        current.removeAllChildren();
        List<MutableTreeNode> list = loadNodes(current);
        Iterator<MutableTreeNode> i = list.iterator();
        while (i.hasNext()) {
            current.add(i.next());
        }
    }

    @Override
    public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
        System.out.println("Tree will collapse");
        DefaultMutableTreeNode current = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();
        current.add(new DefaultMutableTreeNode("loading..."));
    }

    private String listfilesUrl(Object[] path) {

        if ( (path == null)) {
            return null;
        }

        String url = "\"";
        for (int i = 1; i < path.length; i++) {
            DefaultMutableTreeNode n = (DefaultMutableTreeNode) path[i];

            if (i == (path.length - 1)) {
                url = url + n.toString() + "\"";
                return url;
            }
            url = url + n.toString() + File.separator;
        }
        return url;
    }

    private List<MutableTreeNode> loadNodes(DefaultMutableTreeNode current) {
        List<MutableTreeNode> list = new ArrayList<>();
        DefaultMutableTreeNode node;
        String[] name;
        String response = "";


        if (!current.isRoot()) {
            try {
                //response = dbExecutor.execute(new DefaultCommand("lsinfo", listfilesUrl(current.getPath())));
                response = lsinfo(listfilesUrl(current.getPath()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                //response = dbExecutor.execute(new DefaultCommand("lsinfo"));
                response = lsinfo("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Reply reply = new Reply(response);

        Iterator<String> i = reply.iterator();
        while (i.hasNext()) {
            String[] line = i.next().split(Reply.SPLIT_LINE);

            switch (line[0]) {
                case DIRECTORY_PREFIX:
                    //System.out.println(line[0]);
                    name = line[1].split(File.separator);
                    node = new DefaultMutableTreeNode(name[(name.length -1)]);
                    node.add(new DefaultMutableTreeNode("loading..."));
                    list.add(node);
                    break;
                case FILE_PREFIX:
                    name = line[1].split(File.separator);
                    node = new DefaultMutableTreeNode(name[(name.length -1)]);
                    list.add(node);
                    break;
                default:
                    break;
            }
        }
        return list;
    }
}
