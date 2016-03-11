package com.bono;

import com.bono.api.DBExecutor;
import com.bono.api.MPDCommand;
import com.bono.api.Reply;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.*;


// implement TreeWillExpandListener voor verwijderen nodes
// waneer collapsed!
public class MPDDirectory extends MouseAdapter implements TreeWillExpandListener, TreeExpansionListener {
	
	/**
	 * String prefixes to recognize or remove from the return messages from the server.
	 * Used in method makeModel(String[] path).
	 */
	private final String DIRECTORY_PREFIX  = "directory";
	private final String FILE_PREFIX       = "file";
	private final String PLAYLIST_PREFIX   = "playlist";
	private final String TOKEN             = "/";
	
	private DefaultTreeModel directory;         // stores the directory structure as a tree model.
	
	private DefaultMutableTreeNode music;       // root folder, mounted to the server

	private DBExecutor dbExecutor;

	private DefaultMutableTreeNode node;        // variable node used only in this scope.
	
	public MPDDirectory(DBExecutor dbExecutor) {
		this.dbExecutor = dbExecutor;
		music = new DefaultMutableTreeNode("music",true);
		directory = new DefaultTreeModel(music);
	}

	/*
	public MPDDirectory(String entry) {
		this();
		setDirectory(entry);
	}*/

	@Deprecated
	public TreeModel getDirectory() {
		return directory;
	}

	public TreeModel getModel() {
		return directory;
	}


	public String loadChildren() {
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
		directory.nodeStructureChanged((TreeNode) directory.getRoot());
	}

	public DefaultMutableTreeNode getRoot() {
		return (DefaultMutableTreeNode) directory.getRoot();
	}


	private void directoryNode(String entry) {
		String[] name = entry.split(java.io.File.separator);
		DefaultMutableTreeNode nodeNew = new DefaultMutableTreeNode(name[(name.length -1)], true);

		node.add(nodeNew);

	}

	private void fileNode(String entry) {
		String[] name = entry.split(java.io.File.separator);
		DefaultMutableTreeNode nodeNew = new DefaultMutableTreeNode(name[(name.length -1)], false);
		node.add(nodeNew);
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

	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);

		JTree tree = (JTree) e.getSource();

		if (e.getClickCount() == 2) {

			TreeSelectionModel model = tree.getSelectionModel();

			TreePath path = model.getSelectionPath();

			node = (DefaultMutableTreeNode) path.getLastPathComponent();

			if (!node.getAllowsChildren()) {
				System.out.println("file");
				return;
			}

			if (node.getChildCount() > 0) {
				node.removeAllChildren();
				return;
			}

			if (node.isRoot() && node.getAllowsChildren()) {
				//System.out.println("u klikte root!");
				try {

					populate(dbExecutor.execute(new MPDCommand("lsinfo")));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (node.getAllowsChildren()) {
				try {
					populate(dbExecutor.execute(new MPDCommand("lsinfo", listfilesUrl(path.getPath()))));
				} catch (Exception ex) {
					ex.printStackTrace();
				}


			}

			// keep focused.
			//tree.setSelectionPath(path);
			tree.expandPath(path);

		}

	}

	@Override
	public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
		System.out.println("Tree expanded");
	}

	@Override
	public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
		System.out.println("Tree collapsed");
		//node.removeAllChildren();
	}

	@Override
	public void treeExpanded(TreeExpansionEvent event) {

	}

	@Override
	public void treeCollapsed(TreeExpansionEvent event) {
		System.out.println("tree collapsed!");
	}
}
