package com.bono.view;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hendriknieuwenhuis on 20/06/16.
 */
public class ConfigConnectionView extends JPanel {

    private JTextField hostField;
    private JTextField portField;

    public ConfigConnectionView() {
        super();
        build();
    }

    private void build() {
        setLayout(new GridLayout(2,2));
        add(new JLabel("host:"));
        hostField = new JTextField();
        add(hostField);
        add(new JLabel("port:"));
        portField = new JTextField();
        add(portField);

    }

    public String getHostField() {
        return hostField.getText();
    }

    public void setHostField(String host) {
        this.hostField.setText(host);
    }

    public String getPortField() {
        return portField.getText();
    }

    public void setPortField(String port) { this.portField.setText(port); }

}
