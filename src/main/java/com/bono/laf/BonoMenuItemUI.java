package com.bono.laf;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuItemUI;
import java.awt.*;

/**
 * Created by bono on 12/19/16.
 */
public class BonoMenuItemUI extends BasicMenuItemUI {

    public BonoMenuItemUI(JMenuItem c) {
        super();
    }

    public static ComponentUI createUI(JComponent c) {
        return new BonoMenuItemUI((JMenuItem) c);
    }

    @Override
    protected void paintBackground(Graphics g, JMenuItem menuItem, Color bgColor) {

        ButtonModel model = menuItem.getModel();
                Color oldColor = g.getColor();
                int menuWidth = menuItem.getWidth();
                int menuHeight = menuItem.getHeight();


                if(menuItem.isOpaque()) {
                        if (model.isArmed()|| (menuItem instanceof JMenu && model.isSelected())) {
                                g.setColor(BonoLafUtils.brightness(menuItem.getBackground(), 0.8));
                                g.fillRect(0,0, menuWidth, menuHeight);
                            } else {
                                g.setColor(menuItem.getBackground());
                                g.fillRect(0,0, menuWidth, menuHeight);
                            }
                        g.setColor(oldColor);
                    }
                else if (model.isArmed() || (menuItem instanceof JMenu &&
                                                     model.isSelected())) {
                        g.setColor(Color.RED);
                        g.fillRect(0,0, menuWidth, menuHeight);
                        g.setColor(oldColor);
                    }
    }
}
