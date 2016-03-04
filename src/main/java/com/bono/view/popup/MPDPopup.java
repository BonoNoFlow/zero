package com.bono.view.popup;

import javax.swing.*;
import java.awt.*;

/**
 * Created by bono on 3/4/16.
 */
abstract class MPDPopup {

    protected JPopupMenu popupMenu;

    public MPDPopup() {
        popupMenu = new JPopupMenu();
        init();
    }

    abstract void init();

    public void show(Component invoker, int x, int y) {
        popupMenu.show(invoker, x, y);
    }
}
