package com.bono.laf;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

/**
 * Created by hendriknieuwenhuis on 10/12/2016.
 */
public class BonoButtonUI extends BasicButtonUI {

    private static final BonoButtonUI bonoButtonUI = new BonoButtonUI();

    public static ComponentUI createUI(JComponent c) {
        return bonoButtonUI;
    }

    @Override
    protected void paintButtonPressed(Graphics g, AbstractButton b) {
        Dimension size = b.getSize();
        g.setColor(BonoLafUtils.brightness(b.getBackground(), 0.7));
        g.fillRect(0, 0, b.getWidth(), b.getHeight());
    }

    @Override
    public void update(Graphics g, JComponent c) {
        AbstractButton button = (AbstractButton) c;
        ButtonModel model = button.getModel();

        if (model.isRollover()) {
            g.setColor(BonoLafUtils.brightness(button.getBackground(), 0.9));
            g.fillRect(0, 0, button.getWidth(), button.getHeight());
            g.setColor(BonoLafUtils.brightness(button.getBackground(), 0.8));
            g.drawRect(0, 0, (button.getWidth() -1), (button.getHeight() -1));
            paint(g, c);
            return;
        }
        super.update(g, c);
    }



}
