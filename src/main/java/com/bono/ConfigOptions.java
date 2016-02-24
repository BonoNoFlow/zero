package com.bono;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by hendriknieuwenhuis on 17/02/16.
 */
public class ConfigOptions {

    private ApplicationConfig applicationConfig;

    private JDialog optionPane;

    private JTextField hostField;
    private JTextField portField;

    private JButton button;

    public ConfigOptions(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
        build();
    }

    private void build() {
        optionPane = new JDialog();
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3,2));
        panel.add(new JLabel("host:"));

        hostField = new JTextField();
        panel.add(hostField);
        panel.add(new JLabel("port:"));
        portField = new JTextField();
        panel.add(portField);

        button = new JButton("save");
        button.addActionListener(new ButtonListener());
        panel.add(button);

        optionPane.setContentPane(panel);

        optionPane.pack();
        optionPane.setVisible(true);
    }

    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            applicationConfig.setHost(hostField.getText());
            applicationConfig.setPort(new Integer(portField.getText()));
            applicationConfig.saveParamChanges();
            optionPane.dispose();
        }
    }
}
