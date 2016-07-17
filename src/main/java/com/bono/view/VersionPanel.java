package com.bono.view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by hendriknieuwenhuis on 15/07/16.
 */
public class VersionPanel extends JPanel {

    private JTextField field;

    public VersionPanel() {
        super();
        field = new JTextField() {
            @Override
            public void setBorder(Border border) {
                // No!
            }
        };
        field.setOpaque(false);
        field.setFont(new Font("SansSerif", Font.PLAIN, 8));
        //field.
        setLayout(new BorderLayout());
        add(field, BorderLayout.EAST);
    }

    public void setVersion(String text) {
        field.setText(text);
    }
}
