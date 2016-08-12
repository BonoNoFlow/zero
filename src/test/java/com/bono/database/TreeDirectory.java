package com.bono.database;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.*;
import java.util.List;

/**
 * Created by hendriknieuwenhuis on 10/03/16.
 */
public class TreeDirectory implements TreeWillExpandListener {

    private DefaultTreeModel treeModel;

    @Override
    public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {

        DefaultMutableTreeNode lazyNode = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();

        if (lazyNode.getChildCount() != 0 && lazyNode.getAllowsChildren()) {
            return;
        }

        new SwingWorker<List<MutableTreeNode>, Void>(){

            @Override
            protected List<MutableTreeNode> doInBackground() throws Exception {
                // TODO Auto-generated method stub
                return  null;//lazyNode.loadChildren();
            }

            protected void done() {
                try {
                    for (MutableTreeNode node : get()) {
                        treeModel.insertNodeInto(node, lazyNode,
                                lazyNode.getChildCount());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();

    }

    @Override
    public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {

    }
}
