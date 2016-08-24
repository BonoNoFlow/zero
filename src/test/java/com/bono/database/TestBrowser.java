package com.bono.database;

import com.bono.api.ClientExecutor;
import com.bono.api.Command;
import com.bono.api.DefaultCommand;
import com.bono.api.protocol.MPDDatabase;
import com.bono.api.protocol.MPDPlaylist;
import com.bono.soundcloud.SoundcloudController;
import com.bono.view.SoundcloudView;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.io.File;
import java.util.*;
import java.util.List;

/**
 * Created by bono on 8/11/16.
 */
public class TestBrowser extends MouseAdapter implements ActionListener {

    private enum BrowserMode {
        FILES, ARTISTS
    }

    static final String DIRECTORY_PREFIX  = "directory";
    static final String FILE_PREFIX       = "file";
    static final String ARTIST = "Artist: ";

    JFrame frame;
    JTabbedPane parentPane;

    JTree tree;
    JScrollPane scrollPane;

    SoundcloudView soundcloudView;

    SoundcloudController soundcloudController;

    ClientExecutor clientExecutor = new ClientExecutor("192.168.2.4", 6600, 4000);

    DefaultListModel<String> artistsModel;
    DefaultMutableTreeNode root = new DefaultMutableTreeNode();
    DefaultMutableTreeNode artistRoot = new DefaultMutableTreeNode();

    FilesWillExpandListener filesListener = new FilesWillExpandListener();
    ArtistsWillExpandListener artistsListener = new ArtistsWillExpandListener();

    BrowserMode mode;

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

            tree = new JTree(root);
            tree.getSelectionModel().setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
            tree.setRootVisible(false);
            tree.addMouseListener(this);
            tree.addTreeWillExpandListener(filesListener);
            scrollPane = new JScrollPane();
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.getViewport().add(tree);
            parentPane.addTab("database", scrollPane);

            soundcloudView = new SoundcloudView();
            parentPane.addTab("soundcloud", soundcloudView);

            soundcloudController.setSoundcloudView(soundcloudView);

            frame.getContentPane().add(parentPane);
            frame.pack();
            frame.setVisible(true);
        });
    }

    public void initFiles() {
        mode = BrowserMode.FILES;
        Collection<String> dir = new ArrayList<>();
        try {
            dir = clientExecutor.execute(new DefaultCommand(MPDDatabase.LSINFO));
        } catch (Exception e) {
            e.printStackTrace();
        }
        root.removeAllChildren();
        //for (String s: dir) {
        //    System.out.println(s);
        //}
        populate(root, dir);



    }

    public void initArtists() {
        mode = BrowserMode.ARTISTS;
        Collection<String> artists = new ArrayList<>();
        try {
            artists = clientExecutor.execute(new DefaultCommand(MPDDatabase.LIST, "artist"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        root.removeAllChildren();

        populateArtist(root, artists);
    }

    private List<MutableTreeNode> createNodes(Collection<String> directory) {
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

    private void populate(DefaultMutableTreeNode parent, Collection<String> dir) {
        Iterator<MutableTreeNode> i = createNodes(dir).iterator();
        while (i.hasNext()) {
            parent.add(i.next());
        }
    }

    private void populateArtist(DefaultMutableTreeNode parent, Collection<String> list) {
        for (String s: list) {
            String [] as = s.split(": ");

            DefaultMutableTreeNode node;
            switch (as[0]) {
                case "Artist":
                    if (as.length > 1) {
                        node = new DefaultMutableTreeNode(as[1], true);
                        node.add(new DefaultMutableTreeNode("loading..."));
                        parent.add(node);

                    } //else {
                    //    node = new DefaultMutableTreeNode("", true);
                    //    node.add(new DefaultMutableTreeNode("loading..."));
                    //    parent.add(node);
                    //}
                    break;
                default:
                    System.out.println("Unknown: " + as[1]);
                    break;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //System.out.println(e.getActionCommand());
        switch (e.getActionCommand()) {
            case "files":
                tree.removeTreeWillExpandListener(filesListener);
                tree.removeTreeWillExpandListener(artistsListener);
                initFiles();
                tree.setModel(new DefaultTreeModel(root));
                tree.addTreeWillExpandListener(filesListener);
                break;
            case "artists":
                tree.removeTreeWillExpandListener(filesListener);
                tree.removeTreeWillExpandListener(artistsListener);
                initArtists();
                tree.setModel(new DefaultTreeModel(root));
                tree.addTreeWillExpandListener(artistsListener);
                break;
            default:
                break;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        showPopup(tree, e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        showPopup(tree, e);
    }

    private void showPopup(Component c, MouseEvent e) {
        if (e.isPopupTrigger()) {
            JPopupMenu p = new JPopupMenu();
            JMenuItem files = new JMenuItem("files");
            files.setActionCommand("files");
            files.addActionListener(this);
            p.add(files);
            JMenuItem artists = new JMenuItem("artists");
            artists.setActionCommand("artists");
            artists.addActionListener(this);
            p.add(artists);
            JSeparator separator = new JSeparator();
            p.add(separator);
            JMenuItem add = new JMenuItem("add");
            add.addActionListener(new AddListener());
            p.add(add);
            p.show(c, e.getX(), e.getY());
        }
    }

    private class AddListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            DefaultTreeSelectionModel model = (DefaultTreeSelectionModel) tree.getSelectionModel();
            TreePath[] paths = model.getSelectionPaths();
            //String url;
            List<Command> commands = new ArrayList<>();
            commands.add(new DefaultCommand(DefaultCommand.COMMAND_LIST_BEGIN));
            if (mode == BrowserMode.FILES) {
                //System.out.println("files..");

                for (TreePath t: paths) {
                   // System.out.println(prepareURL(t));
                    commands.add(new DefaultCommand(MPDPlaylist.ADD, prepareURL(t)));
                }

            } else if (mode == BrowserMode.ARTISTS) {


                for (TreePath t: paths) {
                    // when the artist folder isselected.
                    // use findadd command
                    if (t.getPathCount() == 2) {
                        //System.out.println("Artist selected: " + t.getPath()[1].toString());
                        commands.add(new DefaultCommand(MPDDatabase.FINDADD, "artist", "\"" + t.getPath()[1].toString() + "\""));
                    } else if (t.getPathCount() > 2) {
                        //System.out.println("Folder selected: " + prepareURL(t));
                        commands.add(new DefaultCommand(MPDPlaylist.ADD, prepareURL(t)));
                    }
                    //System.out.println(t.toString());
                }

            }
            model.clearSelection();
            commands.add(new DefaultCommand(DefaultCommand.COMMAND_LIST_END));
            try {
                clientExecutor.executeList(commands);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        private String prepareURL(TreePath t) {
            String url = "\"";
            int i = 0;
            switch (mode) {
                case FILES:
                    i = 1;
                    break;
                case ARTISTS:
                    i = 2;
                    break;
            }
            for (; i < t.getPath().length; i++) {
                url += t.getPathComponent(i).toString();

                if (i < (t.getPathCount() - 1)) {
                    url += "/";
                }
            }
            url += "\"";
            return url;
        }


    }

    private class FilesWillExpandListener implements TreeWillExpandListener {

        @Override
        public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
            System.out.println("Tree will expand.");
            DefaultMutableTreeNode current = (DefaultMutableTreeNode )event.getPath().getLastPathComponent();
            TreePath path = event.getPath();
            //System.out.println(prepareURL(path));
            List<MutableTreeNode> nodes = loadNodes(path);
            current.removeAllChildren();
            Iterator<MutableTreeNode> i = nodes.iterator();
            while (i.hasNext()) {
                current.add(i.next());
            }

        }

        @Override
        public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
            //System.out.println("Tree will collapse.");
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();
            Enumeration<DefaultMutableTreeNode> e = node.depthFirstEnumeration();
            while (e.hasMoreElements()) {
                DefaultMutableTreeNode eNode = e.nextElement();
                if (eNode.getChildCount() > 0) {
                    eNode.removeFromParent();
                }
            }
            node.add(new DefaultMutableTreeNode("loading...", false));
        }

        private String prepareURL(TreePath path) {
            String url = "\"";

            for (int i = 1; i < path.getPathCount(); i++) {
                url += path.getPathComponent(i).toString();

                if (i < (path.getPathCount() - 1)) {
                    url += "/";
                }
            }
            url += "\"";
            return url;
        }

        private List<MutableTreeNode> loadNodes(TreePath path) {
            List<MutableTreeNode> list = new ArrayList<>();
            DefaultMutableTreeNode node;
            String[] name;

            Collection<String> response = new ArrayList<>();

            try {

                response = clientExecutor.execute(new DefaultCommand(MPDDatabase.LSINFO, prepareURL(path)));
            } catch (Exception e) {
                e.printStackTrace();
            }

            Iterator<String> i = response.iterator();
            while (i.hasNext()) {
                String[] line = i.next().split(": ");

                switch (line[0]) {
                    case DIRECTORY_PREFIX:
                        name = line[1].split("/");
                        node = new DefaultMutableTreeNode(name[(name.length -1)]);
                        node.add(new DefaultMutableTreeNode("loading...", false));
                        list.add(node);
                        break;
                    case FILE_PREFIX:
                        name = line[1].split("/");
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

    private class ArtistsWillExpandListener implements TreeWillExpandListener {

        @Override
        public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
            //System.out.println("Tree will expand.");
            DefaultMutableTreeNode artistNode = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();
            //System.out.println(artistNode.toString());
            //System.out.println(artistNode.getFirstChild());

            if (artistNode.getFirstChild().toString().equals("loading...")) {
                System.out.println("inside loading...");
                artistNode.removeAllChildren();
                Collection<String> files = new ArrayList<>();

                try {
                    files = clientExecutor.execute(new DefaultCommand(MPDDatabase.FIND, "artist", "\""+artistNode.toString()+"\""));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                boolean hasChildren = false;
                for (String s : files) {

                    DefaultMutableTreeNode node = artistNode;
                    if (s.startsWith("file")) {
                        s = s.substring("file: ".length());
                        //System.out.println(s);
                        String[] path = s.split("/");

                        for (int i = 0; i < path.length; i++) {
                            DefaultMutableTreeNode temp;
                            if (i < (path.length -1)) {
                                hasChildren = true;
                            } else if (i == (path.length -1)) {
                                hasChildren = false;
                            }
                            temp = new DefaultMutableTreeNode(path[i], hasChildren);
                            int index = checkNode(node, temp);

                            if (index != -1) {
                                // node exists so 'node' has to point to existing node
                                node = (DefaultMutableTreeNode) node.getChildAt(index);
                            } else {
                                node.add(temp);
                                node = temp;
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {

        }

        private int checkNode(DefaultMutableTreeNode parent, DefaultMutableTreeNode check) {
            Enumeration<DefaultMutableTreeNode> e = parent.children();
            int i = -1;
            while (e.hasMoreElements()) {
                i++;
                if (e.nextElement().toString().equals(check.toString())) {
                    return i;
                }
            }
            return -1;
        }
    }

    public static void main(String[] args) {
        new TestBrowser();


    }
}
