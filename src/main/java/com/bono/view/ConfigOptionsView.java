package com.bono.view;

import com.bono.api.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;

/**
 * Created by hendriknieuwenhuis on 17/02/16.
 */
public class ConfigOptionsView {

    private Config config;

    private JDialog optionPane;

    private JTextField hostField;
    private JTextField portField;

    private JButton button;

    public ConfigOptionsView() {
        build();
    }

    public ConfigOptionsView(Config config) {
        this.config = config;
        build();
    }

    private void build() {
        optionPane = new JDialog();
        //optionPane.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //optionPane.setDefaultCloseOperation();
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3,2));
        panel.add(new JLabel("host:"));

        hostField = new JTextField();
        panel.add(hostField);
        panel.add(new JLabel("port:"));
        portField = new JTextField();
        panel.add(portField);

        button = new JButton("save");
        //button.addActionListener(new ButtonListener());
        panel.add(button);

        optionPane.setContentPane(panel);

        //optionPane.pack();
        //optionPane.setVisible(true);
    }

    public void placement(int x, int y) {
        optionPane.setLocation(x, y);
    }

    public void addWindowListener(WindowListener windowListener) {
        optionPane.addWindowListener(windowListener);
    }

    public Dimension getSize() {
        return optionPane.getSize();
    }

    public void addListener(ActionListener listener) {
        button.addActionListener(listener);
    }

    public String getHostField() {
        return hostField.getText();
    }

    public String getPortField() {
        return portField.getText();
    }

    public void dispose() {
        optionPane.dispose();
    }

    public void show() {
        optionPane.pack();
        optionPane.setVisible(true);
    }

    /*
    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            config.setHost(hostField.getText());
            config.setPort(new Integer(portField.getText()));
            try {
                config.saveParamChanges();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            optionPane.dispose();
        }
    }*/
}
