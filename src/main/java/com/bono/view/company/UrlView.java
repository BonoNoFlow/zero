package com.bono.view.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by hendriknieuwenhuis on 12/01/16.
 */
public class UrlView extends JPanel {

    private JLabel urlLabel;
    private JTextField urlValue;

    public UrlView() {
        build();
    }

    private void build() {
        urlLabel = new JLabel("url: ");

        urlValue = new JTextField();
        urlValue.setEditable(true);

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(urlLabel);
        add(urlValue);
    }

    public void addUrlViewListener(ActionListener listener) {
        urlValue.addActionListener(listener);
    }
}
