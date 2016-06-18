package com.bono.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created by hendriknieuwenhuis on 18/06/16.
 */
public class ConnectionDialog extends JDialog {

    private JPanel panel = new JPanel(new BorderLayout());

    private JButton button = new JButton("save");

    private JTextField message = new JTextField("Host or Port, false or missing!");

    public ConnectionDialog(JPanel panel) {
        super();
        message.setOpaque(false);
        message.setBorder(null);
        message.setHighlighter(null);
        message.setEditable(false);
        message.setForeground(Color.RED);

        panel.setBorder(new EmptyBorder(5,5,5,5));
        panel.add(message, BorderLayout.NORTH);
        add(panel);
        JPanel bPanel = new JPanel(new GridLayout(1,2));
        bPanel.add(button);
        bPanel.add(new JPanel());
        panel.add(bPanel, BorderLayout.SOUTH);
        add(panel);
        getContentPane().add(panel);
        setResizable(false);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }

}
