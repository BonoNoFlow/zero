package com.bono.view;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * Created by hendriknieuwenhuis on 11/03/16.
 */
public class DirectoryCellRenderer extends DefaultTreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

        if (node.getAllowsChildren() && !expanded) {

            setIcon(getClosedIcon());

            // to draw open folder

        } else if (expanded) {

            setIcon(getOpenIcon());
        } else {
            setIcon(getLeafIcon());
        }
        return this;
    }
}
