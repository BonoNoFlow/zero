package com.bono.directory;

import com.bono.Utils;
import com.bono.api.DBExecutor;
import com.bono.api.MPDCommand;
import com.bono.api.Reply;
import com.bono.view.DirectoryView;
import com.bono.view.MPDPopup;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.activation.DataHandler;
import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.*;

public class MPDDirectory extends MouseAdapter implements TreeWillExpandListener, Transferable {
	
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
		this.directoryView.getDirectory().setTransferHandler(new TransferablePathHandler());

	}

	private List<MutableTreeNode> loadNodes(DefaultMutableTreeNode current) {
		List<MutableTreeNode> list = new ArrayList<>();
		DefaultMutableTreeNode node;
		String[] name;
		String response = "";


		if (!current.isRoot()) {
			try {
				response = dbExecutor.execute(new MPDCommand("lsinfo", Utils.filesUrl(current.getPath())));
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




	@Override
	public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
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
		DefaultMutableTreeNode current = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();
		current.add(new DefaultMutableTreeNode("loading..."));
	}

	/*

	MouseAdapter.

	 */

	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);

		// root can not be added!
		if ( directoryView.getDirectory().getSelectionPath().getPathCount() > 1 ) {
			MPDPopup popup = new MPDPopup();
			popup.addMenuItem("add", new AddListener());
			popup.show(directoryView.getDirectory(), e.getX(), e.getY());
		}

		/*
		switch (e.getButton()) {
			case MouseEvent.BUTTON1:

				break;
			case MouseEvent.BUTTON2:

				break;
			case MouseEvent.BUTTON3:
				// root can not be added!
				if ( directoryView.getDirectory().getSelectionPath().getPathCount() > 1 ) {
					MPDPopup popup = new MPDPopup();
					popup.addMenuItem("add", new AddListener());
					popup.show(directoryView.getDirectory(), e.getX(), e.getY());
				}
				break;
			default:
				break;
		}*/

	}

	@Override
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);

		if (e.getButton() == MouseEvent.BUTTON1) {
			JComponent component = (JComponent) e.getSource();
			TransferHandler transferHandler = new TransferHandler("tree");
		}

		/*
		switch (e.getButton()) {

		}*/
	}

	private class AddListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			DefaultTreeSelectionModel model = (DefaultTreeSelectionModel) directoryView.getDirectory().getSelectionModel();

			if (!model.isSelectionEmpty()) {
				TreePath path = model.getSelectionPath();
				String response = null;
				try {
					response = dbExecutor.execute(new MPDCommand("add", Utils.filesUrl(path.getPath())));
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				Utils.Log.print(response);
			}
		}
	}

	// drag stuff.


	private DataFlavor flavor = new DataFlavor(TreePath.class, "treePath");
	private DataFlavor[] pathFlavor = new DataFlavor[]{flavor};

	public class TransferablePathHandler extends TransferHandler {

		@Override
		protected Transferable createTransferable(JComponent c) {

			return new DataHandler(directoryView.getDirectory().getSelectionPath(), flavor.getMimeType());
		}
	}





	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return pathFlavor;
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return pathFlavor[0].equals(flavor);
	}

	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		return directoryView.getDirectory().getSelectionPath();
	}


	private class DirectoryDragRecognizer extends DragGestureRecognizer {

		public DirectoryDragRecognizer(DragSource ds, Component c) {
			super(ds, c);
		}

		@Override
		protected void registerListeners() {

		}

		@Override
		protected void unregisterListeners() {

		}
	}
}
