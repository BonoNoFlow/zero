package com.bono.view.company;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Created by hendriknieuwenhuis on 29/11/15.
 */
public class SettingsMenu extends JPopupMenu {

    private JRadioButtonMenuItem singleButton;
    private JRadioButtonMenuItem consumeButton;

    public SettingsMenu() {
        super();
        build();
    }

    private void build() {
        singleButton = new JRadioButtonMenuItem("single");
        add(singleButton);

        consumeButton = new JRadioButtonMenuItem("consume");
        add(consumeButton);
        pack();
    }

    public void addSingleButtonActionListener(ActionListener listener) {
        singleButton.addActionListener(listener);
    }

    public void addConsumeButtonActionListener(ActionListener listener) {
        consumeButton.addActionListener(listener);
    }

    public void setSingleSelected(boolean value) {
        singleButton.setSelected(value);
    }

    public void setConsumeSelected(boolean value) {
        consumeButton.setSelected(value);
    }

}
