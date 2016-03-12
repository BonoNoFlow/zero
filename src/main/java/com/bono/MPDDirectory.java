package com.bono;

import com.bono.api.DBExecutor;
import com.bono.api.MPDCommand;
import com.bono.api.Reply;
import com.bono.view.DirectoryView;
import com.bono.view.MPDPopup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.*;


// implement TreeWillExpandListener voor verwijderen nodes
// waneer collapsed!
public class MPDDirectory extends MouseAdapter implements TreeWillExpandListener {
	
	/**
	 * String prefixes to recognize or remove from the return messages from the server.
	 * Used in method makeModel(String[] path).
	 */
	private final String DIRECTORY_PREFIX  = "directory";
	private final String FILE_PREFIX       = "file";
	private final String PLAYLIST_PREFIX   = "playlist";
	private final String TOKEN             = "/";

	private DirectoryView directoryView;

	private DBExecutor dbExecutor;

	public MPDDirectory(DBExecutor dbExecutor, DirectoryView directoryView) {
		this.dbExecutor = dbExecutor;
		this.directoryView = directoryView;
	}

	private List<MutableTreeNode> loadNodes(DefaultMutableTreeNode current) {
		List<MutableTreeNode> list = new ArrayList<>();
		DefaultMutableTreeNode node;
		String[] name;
		String response = "";


		if (!current.isRoot()) {
			try {
				response = dbExecutor.execute(new MPDCommand("lsinfo", listfilesUrl(current.getPath())));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				response = dbExecutor.execute(new MPDCommand("lsinfo"));
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
	public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
		System.out.println("Tree expanded");

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
		System.out.println("Tree collapsed");
		//node.removeAllChildren();
		DefaultMutableTreeNode current = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();
		current.add(new DefaultMutableTreeNode("loading..."));
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);

		if (e.getButton() == MouseEvent.BUTTON3) {
			System.out.println("pushed");
			MPDPopup popup = new MPDPopup();
			popup.addMenuItem("add", new AddListener());
			popup.show(directoryView.getDirectory(), e.getX(), e.getY());
		}
	}

	private class AddListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

		}
	}
}
