package com.bono.database;

import com.bono.api.ClientExecutor;
import com.bono.api.Command;
import com.bono.api.DefaultCommand;
import com.bono.api.protocol.MPDDatabase;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 * Created by bono on 8/9/16.
 */
public class TestDatabase implements TreeWillExpandListener, TreeExpansionListener {

    static final String ARTIST = "Artist: ";

    ClientExecutor clientExecutor = new ClientExecutor("192.168.2.4", 6600, 4000);

    JTree tree;

    JList<String> artists;
    DefaultListModel<String> artistsModel;
    DefaultMutableTreeNode root = new DefaultMutableTreeNode();

    public TestDatabase() {
        initArtists();

        build();

        //clientExecutor.shutdownExecutor();
    }

    public void build() {

        SwingUtilities.invokeLater(() -> {
            //tree = new JTree(root);
            //tree.setRootVisible(false);
            //tree.addTreeWillExpandListener(this);
            //tree.addTreeExpansionListener(this);
            //tree.addMouseListener(new ArtistsMouseListener());

            //root.add(new DefaultMutableTreeNode("loading..."));
            artists = new JList<String>(artistsModel);
            artists.addMouseListener(new ArtistsMouseListener());
            JScrollPane pane = new JScrollPane();
            pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            pane.getViewport().add(artists);

            //searchField = new JTextField();
            //searchField.addActionListener(new SearchFieldListener());
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //frame.getContentPane().add(searchField, BorderLayout.NORTH);
            frame.getContentPane().add(pane, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);
        });
    }

    public void initArtists() {
        List<String> artists = new ArrayList<>();
        try {
            artists = clientExecutor.execute(new DefaultCommand(MPDDatabase.LIST, "artist"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Collections.sort(artists, String.CASE_INSENSITIVE_ORDER);
        artistsModel = new DefaultListModel<>();
        //Collection<String> coll = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        Iterator<String> i = artists.iterator();
        while (i.hasNext()) {
            String artist = i.next();

            if (!artist.startsWith(ARTIST)) {
                continue;
            }

            artist = artist.substring(ARTIST.length());

            //DefaultMutableTreeNode node = new DefaultMutableTreeNode(artist, true);
            //node.add(new DefaultMutableTreeNode("loading..."));
            //root.add(node);
            //coll.add(artist);
            artistsModel.addElement(artist);
        }
        //artistsModel.
        //for (String s : coll) {
        //    System.out.println(s);
        //}
    }

    @Override
    public void treeExpanded(TreeExpansionEvent event) {

    }

    @Override
    public void treeCollapsed(TreeExpansionEvent event) {

    }

    @Override
    public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
        /*
        List<String> files = new ArrayList<>();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();

        if (!((DefaultMutableTreeNode) node.getParent()).isRoot()) {
            throw new ExpandVetoException(event);
        }
        String artist = "\"" + node.toString() + "\"";
        try {
            files = clientExecutor.execute(new DefaultCommand(MPDDatabase.FIND, "artist", artist));
        } catch (Exception e) {
            e.printStackTrace();
        }

        node.removeAllChildren();

        DefaultMutableTreeNode temp = null;

        Iterator<String> i = files.iterator();
        while (i.hasNext()) {
            String file = i.next();
            if (!file.startsWith("file: ")) {
                continue;
            }
            file = file.substring("file: ".length());
            String[] path = file.split("/");

            System.out.println("---- NEW STRING ----");
            System.out.println("Path: " + Arrays.toString(path));

            temp = node;
            for (int j = 0; j < path.length; j++) {
                DefaultMutableTreeNode child = findChild(temp, path[j]);

                if (j < (path.length - 1)) {
                    if (child == null) {
                        child = new DefaultMutableTreeNode(path[j], true);
                        System.out.println("New Child: " + child);
                        temp.add(child);
                    }
                    temp = child;
                    System.out.println("Temp: " + temp.getUserObject());
                } else {
                    child = new DefaultMutableTreeNode(path[j], false);
                    temp.add(child);
                }


            }
        }*/
    }
    @Override
    public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {

    }

    private DefaultMutableTreeNode findChild(DefaultMutableTreeNode parent, String s) {
        System.out.println("Parent: " + parent.getUserObject());
        for (int i = 0; i < parent.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) parent.getChildAt(i);
            if (s.equals(child.getUserObject())) {
                return child;
            }
        }
        return null;
    }

    private class ArtistsMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);

            if (e.getButton() == 3) {
                JList<String> list = (JList<String>) e.getSource();
                int[] selected = list.getSelectedIndices();
                ListModel model = list.getModel();

                List<Command> commands = new ArrayList<>();
                commands.add(new DefaultCommand(DefaultCommand.COMMAND_LIST_BEGIN));
                for (int i : selected) {
                    //System.out.println(model.getElementAt(i));
                    commands.add(new DefaultCommand(MPDDatabase.LIST, "album", "\"" + model.getElementAt(i) + "\""));
                }
                commands.add(new DefaultCommand(DefaultCommand.COMMAND_LIST_END));

                List<String> query = new ArrayList<>();
                //List<Command> commands = new ArrayList<>();
                //commands.add(new DefaultCommand(DefaultCommand.COMMAND_LIST_BEGIN));
                //commands.add(new DefaultCommand(MPDDatabase.LIST, "album", "\"Alva Noto\""));
                //commands.add(new DefaultCommand(MPDDatabase.LIST, "album", "\"speedy j\""));
                //commands.add(new DefaultCommand(DefaultCommand.COMMAND_LIST_END));
                try {
                    query = clientExecutor.executeList(commands);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                for (String s : query) {
                    System.out.println(s);
                }
            }



            //TreePath path = tree.getPathForLocation(e.getX(), e.getY());

            //System.out.println(path);
        }
    }

    public static void main(String[] args) {
        new TestDatabase();
    }
}
