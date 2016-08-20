package com.bono.view;

import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.MouseListener;

/**
 * Created by bono on 8/20/16.
 */
public interface BrowserView {

    void setRoot(TreeNode root);

    Component getComponent();

    TreeSelectionModel getBrowserTreeSelectionModel();

    void addBrowserMouseListener(MouseListener l);

    void addBrowserTreeWillExpandListener(TreeWillExpandListener l);

    void removeBrowserTreeWillExpandListener(TreeWillExpandListener l);


}
