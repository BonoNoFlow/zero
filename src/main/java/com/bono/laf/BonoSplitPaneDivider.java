package com.bono.laf;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;

/**
 * Created by bono on 10/12/16.
 */
public class BonoSplitPaneDivider extends BasicSplitPaneDivider {

    private int dividerDragSize = 9;
    private int dividerDragoffset = 4;

    public BonoSplitPaneDivider(BasicSplitPaneUI ui) {
        super(ui);
        super.setBorder(null);
    }

    @Override
    public void setBorder(Border border) {
        //super.setBorder(border);
    }

    @Override
    public void paint(Graphics g) {
        //super.paint(g);
        g.setColor(getBackground());
        if (orientation == JSplitPane.HORIZONTAL_SPLIT) {
            g.drawLine(dividerDragoffset, 0, dividerDragoffset, (getHeight() -1));
            // maybe as draw border and set border with a border..
            g.setColor(Color.DARK_GRAY);
            g.drawLine(0, 0, 0, (getHeight() -1));
        } else {
            g.drawLine(0, dividerDragoffset, (getWidth() -1), dividerDragoffset);
        }
    }

    @Override
    protected void dragDividerTo(int location) {
        super.dragDividerTo(location + dividerDragoffset);
    }

    @Override
    protected void finishDraggingTo(int location) {
        super.finishDraggingTo(location + dividerDragoffset);
    }
}
