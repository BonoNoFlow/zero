package com.bono.database;

import com.bono.api.ClientExecutor;
import com.bono.api.DefaultCommand;
import com.bono.api.protocol.MPDDatabase;
import com.bono.soundcloud.SoundcloudController;
import com.bono.view.SoundcloudView;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.MutableTreeNode;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by bono on 8/11/16.
 */
public class TestBrowser {

    static final String DIRECTORY_PREFIX  = "directory";
    static final String FILE_PREFIX       = "file";
    static final String ARTIST = "Artist: ";

    JFrame frame;
    JTabbedPane parentPane;

    FilesPanel filesP;
    ArtistsPanel artistsP;
    SoundcloudView soundcloudView;

    SoundcloudController soundcloudController;

    ClientExecutor clientExecutor = new ClientExecutor("192.168.2.4", 6600, 4000);

    DefaultListModel<String> artistsModel;
    DefaultMutableTreeNode root = new DefaultMutableTreeNode();

    public TestBrowser() {
        initControllers();
        initFiles();
        buidlFrame();
    }

    private void initControllers() {
        soundcloudController = new SoundcloudController(clientExecutor);
    }

    private void buidlFrame() {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("browser");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            parentPane = new JTabbedPane();


            filesP = new FilesPanel();
            filesP.addTreeWillExpandListener(new FilesWillExpandListener());
            parentPane.addTab("files", filesP);

            artistsP = new ArtistsPanel();
            parentPane.addTab("artists", artistsP);

            soundcloudView = new SoundcloudView();
            parentPane.addTab("soundcloud", soundcloudView);

            soundcloudController.setSoundcloudView(soundcloudView);
            parentPane.addChangeListener(new TabFocus());


            frame.getContentPane().add(parentPane);
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
                    name = line[1].split(java.io.File.separator);
                    //System.out.println(Arrays.toString(name));
                    node = new DefaultMutableTreeNode(name[(name.length -1)]);
                    node.add(new DefaultMutableTreeNode("loading..."));
                    list.add(node);
                    break;
                case FILE_PREFIX:
                    name = line[1].split(java.io.File.separator);
                    //System.out.println(Arrays.toString(name));
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


    private class FilesPanel extends JScrollPane {

        JTree tree;

        public FilesPanel() {
            super();
            tree = new JTree(root);
            tree.setRootVisible(false);
            setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            getViewport().add(tree);

        }

        public void setRoot(DefaultMutableTreeNode root) {
            tree.setModel(new DefaultTreeModel(root));
            //tree.treeDidChange();
        }

        public DefaultMutableTreeNode getRoot() {
            return root;
        }

        public void addTreeWillExpandListener(TreeWillExpandListener l) {
            tree.addTreeWillExpandListener(l);
        }

        public void addTreeExpansionListener(TreeExpansionListener l) {
            tree.addTreeExpansionListener(l);
        }
    }

    private class ArtistsPanel extends JPanel {

        JList<String> artists;


        public ArtistsPanel() {
            super();
            artists = new JList<String>();
            JScrollPane pane = new JScrollPane();
            pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            pane.getViewport().add(artists);
        }

        public void setModel(ListModel<String> model) {
            artists.setModel(model);
        }

        public void addMouseListener(MouseListener l) {
            artists.addMouseListener(l);
        }
    }

    private class TabFocus implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            JTabbedPane p = (JTabbedPane) e.getSource();
            System.out.println("Tab: " + p.getSelectedIndex());
            if (p.getSelectedIndex() == 0) {
                initFiles();
                filesP.setRoot(root);
            }
        }
    }

    private class FilesWillExpandListener implements TreeWillExpandListener {

        @Override
        public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
            System.out.println("Tree will expand.");
        }

        @Override
        public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
            System.out.println("Tree will collapse.");
        }
    }

    public static void main(String[] args) {
        new TestBrowser();
    }
}
