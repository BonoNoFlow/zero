package com.bono.database;

import com.bono.api.*;

import com.bono.api.protocol.MPDDatabase;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hendriknieuwenhuis on 12/03/16.
 */
public class SimpleTestTree implements TreeWillExpandListener, TreeExpansionListener {

    private final String DIRECTORY_PREFIX  = "directory";
    private final String FILE_PREFIX       = "file";

    DefaultMutableTreeNode root = new DefaultMutableTreeNode();

    JTree tree;

    JTextField searchField;

    ClientExecutor clientExecutor;

    public SimpleTestTree() {

        clientExecutor = new ClientExecutor("192.168.2.4", 6600, 4000);
    }

    public void build() {
        initFiles();
        SwingUtilities.invokeLater(() -> {
            tree = new JTree(root);
            tree.setRootVisible(false);
            tree.addTreeWillExpandListener(this);
            tree.addTreeExpansionListener(this);
            tree.addMouseListener(new DatabaseMouseListener());

            root.add(new DefaultMutableTreeNode("loading..."));
            JScrollPane pane = new JScrollPane();
            pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            pane.getViewport().add(tree);

            searchField = new JTextField();
            searchField.addActionListener(new SearchFieldListener());
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(searchField, BorderLayout.NORTH);
            frame.getContentPane().add(pane, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);
        });
    }

    public void initFiles() {
        List<String> dir = new ArrayList<>();
        try {
            dir = clientExecutor.execute(new DefaultCommand(MPDDatabase.LSINFO));
        } catch (Exception e) {
            e.printStackTrace();
        }
        root.removeAllChildren();

        populate(root, dir);
    }

    @Override
    public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
        //System.out.println("Tree will expand");

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();

        if (node.toString().isEmpty()) throw new ExpandVetoException(new TreeExpansionEvent(tree, event.getPath()));

        TreePath path = event.getPath();

        //for (int i = 0; i < path.getPathCount(); i++) {
        //    System.out.println((path.getPathComponent(i)).toString());
        //}

        //System.out.println(listfilesUrl(path.getPath()));

        List<String> dir = new ArrayList<>();

        String url = listfilesUrl(path.getPath());

        try {
            dir = clientExecutor.execute(new DefaultCommand(MPDDatabase.LSINFO, url));
        } catch (Exception e) {
            e.printStackTrace();
        }
        node.removeAllChildren();
        populate(node, dir);
    }

    @Override
    public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
        System.out.println("Tree will collapse");
        //DefaultMutableTreeNode current = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();
        //current.add(new DefaultMutableTreeNode("loading..."));
    }

    @Override
    public void treeExpanded(TreeExpansionEvent event) {
        System.out.println("Tree expanded");
        //((DefaultMutableTreeNode) event.getPath().getLastPathComponent()).removeAllChildren();
    }

    @Override
    public void treeCollapsed(TreeExpansionEvent event) {

        /*
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();
        node.removeAllChildren();
        node.add(new DefaultMutableTreeNode("loading", false));*/
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

    private List<String> getDirectory(TreePath path) {
        List<String> response = new ArrayList<>();

        String url = listfilesUrl(path.getPath());

        if (!((DefaultMutableTreeNode)path.getLastPathComponent()).isRoot()) {
            try {
                response = clientExecutor.execute(new DefaultCommand("lsinfo", url));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {

                response = clientExecutor.execute(new DefaultCommand("lsinfo"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    private List<MutableTreeNode> createNodes(List<String> directory) {
        List<MutableTreeNode> list = new ArrayList<>();
        DefaultMutableTreeNode node;
        String[] name;
        Iterator<String> i = directory.iterator();
        while (i.hasNext()) {
            String[] line = i.next().split(": ");

            switch (line[0]) {
                case DIRECTORY_PREFIX:
                    //System.out.println(line[0]);
                    name = line[1].split(File.separator);
                    System.out.println(Arrays.toString(name));
                    node = new DefaultMutableTreeNode(name[(name.length -1)]);
                    node.add(new DefaultMutableTreeNode("loading..."));
                    list.add(node);
                    break;
                case FILE_PREFIX:
                    name = line[1].split(File.separator);
                    System.out.println(Arrays.toString(name));
                    node = new DefaultMutableTreeNode(name[(name.length -1)]);
                    list.add(node);

                    break;
                default:
                    break;
            }
        }
        return list;
    }

    private void populate(DefaultMutableTreeNode parent, List<String> dir) {
        Iterator<MutableTreeNode> i = createNodes(dir).iterator();
        while (i.hasNext()) {
            parent.add(i.next());
        }
    }

    private class SearchFieldListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(((JTextField) e.getSource()).getText());
        }
    }

    private class DatabaseMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);

            if (SwingUtilities.isRightMouseButton(e)) {
                System.out.println(e.getButton());
            }
        }
    }

    public static void main(String[] args) {
        SimpleTestTree simpleTestTree = new SimpleTestTree();
        simpleTestTree.build();

    }


}
