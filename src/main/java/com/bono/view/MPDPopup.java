package com.bono.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by bono on 3/4/16.
 */
@Deprecated
public class MPDPopup {

    protected JPopupMenu popupMenu;

    public MPDPopup() {
        popupMenu = new JPopupMenu();
        init();
    }

    public void addMenuItem(String name, ActionListener listener) {
        JMenuItem item = new JMenuItem(name);
        item.addActionListener(listener);
        popupMenu.add(item);
    }

    public void init() {}

    public void show(Component invoker, int x, int y) {
        popupMenu.show(invoker, x, y);
    }
}
