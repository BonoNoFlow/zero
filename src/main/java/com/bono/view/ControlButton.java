package com.bono.view;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Created by bono on 8/11/16.
 */
public class ControlButton extends JButton implements Button {



    public ControlButton(String actionCommand) {
        super();
        setActionCommand(actionCommand);

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
