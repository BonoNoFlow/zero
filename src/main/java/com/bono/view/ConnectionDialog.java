package com.bono.view;

import com.bono.ConfigPresenter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by hendriknieuwenhuis on 18/06/16.
 */
public class ConnectionDialog extends JDialog {

    private JPanel panel = new JPanel(new BorderLayout());

    private JButton button = new JButton(ConfigPresenter.SAVE);

    private JTextField message = new JTextField("Host or Port, false or missing!");

    private ConfigConnectionView configConnectionView;

    public ConnectionDialog(JPanel panel) {
        super();
        build();
    }

    public ConnectionDialog(Dimension dimension) {
        super();
        configConnectionView = new ConfigConnectionView();
        build();
        int x = 0;
        int y = 0;
        if (dimension != null) {
            x = (dimension.width / 2) - (this.getWidth() / 2);
            y = (dimension.height / 2) - (this.getHeight() / 2);
        }
        setLocation(x, y);
    }

    public ConnectionDialog(JPanel panel, Dimension dimension) {
        this(panel);
        int x = 0;
        int y = 0;
        if (dimension != null) {
            x = (dimension.width / 2) - (this.getWidth() / 2);
            y = (dimension.height / 2) - (this.getHeight() / 2);
        }
        setLocation(x, y);
    }

    private void build() {
        message.setOpaque(false);
        message.setBorder(null);
        message.setHighlighter(null);
        message.setEditable(false);
        message.setForeground(Color.RED);

        panel.setBorder(new EmptyBorder(5,5,5,5));
        panel.add(message, BorderLayout.NORTH);

        JPanel bPanel = new JPanel(new GridLayout(1,2));
        button.setActionCommand(ConfigPresenter.SAVE);
        bPanel.add(button);
        bPanel.add(new JPanel());
        panel.add(bPanel, BorderLayout.SOUTH);
        getContentPane().add(panel);
        setResizable(false);

        panel.add(configConnectionView, BorderLayout.CENTER);

        add(panel);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        pack();
    }

    public void showDialog() {
        setVisible(true);
    }

    public void addSaveActionListener(ActionListener listener) {
        button.addActionListener(listener);
    }

    public ConfigConnectionView getConfigConnectionView() {
        return configConnectionView;
    }

}
