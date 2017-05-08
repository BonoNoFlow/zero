package com.bono.view;

import com.bono.icons.BonoIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by bono on 8/11/16.
 */
public class ControlButton extends JButton implements Button {

    private Color background = new Color(220, 220, 220);
    private Color pressed = new Color(211, 211, 211);
    private Color rollover = new Color(196, 196, 196);
    private Color icon = new Color(128, 128, 128);
    private Color border = new Color(168, 169, 169);

    public ControlButton() {
        super();
        setBorderPainted(false);
    }

    public ControlButton(Icon icon) {
        this();
        setIcon(icon);
        setOpaque(true);
    }


    public ControlButton(String actionCommand) {
        this();
        setActionCommand(actionCommand);
        setOpaque(true);


    }

    public ControlButton(String actionCommand, String text, Icon icon) {
        this(actionCommand);
        setText(text);
        setIcon(icon);
    }


    @Override
    public void setButtonIcon(Icon icon) {
        setIcon(icon);
    }

    @Override
    public void setButtonText(String text) {
        setText(text);
    }

    @Override
    public void addButtonActionListener(ActionListener l) {
        this.addActionListener(l);
    }
}
