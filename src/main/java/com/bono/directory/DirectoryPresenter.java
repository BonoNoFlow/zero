package com.bono.directory;

import com.bono.Utils;
import com.bono.api.DBExecutor;
import com.bono.api.Database;
import com.bono.api.DefaultCommand;
import com.bono.api.Reply;
import com.bono.view.DirectoryView;
import com.bono.view.MPDPopup;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hendriknieuwenhuis on 26/05/16.
 */
public class DirectoryPresenter extends Database implements TreeWillExpandListener, MouseListener {

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
        //System.out.println("Tree will expand");

        DefaultMutableTreeNode current = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();
        //current.removeAllChildren();
        //System.out.println(current.toString());
        List<MutableTreeNode> list = loadNodes(current);

        current.removeAllChildren();

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
                response = lsinfo(listfilesUrl(current.getPath()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
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


    /*

	MouseAdapter.

	 */
    @Override
    public void mouseClicked(MouseEvent e) {

        if (e.getButton() == MouseEvent.BUTTON3) {
            // root can not be added!
            if (tree.getSelectionPath().getPathCount() > 1) {
                MPDPopup popup = new MPDPopup();
                popup.addMenuItem("add", new AddListener());
                popup.show(tree, e.getX(), e.getY());
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        /*
        if (e.getButton() == MouseEvent.BUTTON1) {
            JComponent component = (JComponent) e.getSource();
            TransferHandler transferHandler = new TransferHandler("tree");
        }*/
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private class AddListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            DefaultTreeSelectionModel model = (DefaultTreeSelectionModel) tree.getSelectionModel();

            if (!model.isSelectionEmpty()) {
                TreePath path = model.getSelectionPath();
                String response = null;
                try {
                    response = dbExecutor.execute(new DefaultCommand("add", Utils.filesUrl(path.getPath())));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                Utils.Log.print(response);
            }
        }
    }

}
