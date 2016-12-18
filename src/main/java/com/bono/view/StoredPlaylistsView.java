package com.bono.view;

import javax.swing.*;
import java.awt.*;

/**
 * Created by bono on 12/18/16.
 */
public class StoredPlaylistsView extends JPanel {

    private JList playlists;
    private JTextField nameField;
    //private JButton load;
    private JButton save;


    public StoredPlaylistsView() {
        super();
        build();
    }

    private void build() {
        setLayout(new BorderLayout());

        playlists = new JList();
        JScrollPane scrollPane = new JScrollPane(playlists);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        save = new JButton("save");
        panel.add(save, constraints);
        constraints.gridx = 1;
        panel.add(Box.createRigidArea(new Dimension(5,0)), constraints);
        constraints.gridx = 2;
        constraints.weightx = 0.8;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        nameField = new JTextField();
        panel.add(nameField, constraints);
        panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        add(panel, BorderLayout.SOUTH) ;
    }


}
