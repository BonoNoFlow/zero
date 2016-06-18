package com.bono.view;



import javax.swing.*;

/**
 * Created by hendriknieuwenhuis on 18/06/16.
 */
public class ConfigView extends JDialog {

    private JTabbedPane tabbedPane;

    public ConfigView() {
        super();
        tabbedPane = new JTabbedPane();
        JPanel connPanel = new JPanel();
        tabbedPane.addTab("Conn", null);
    }


}
