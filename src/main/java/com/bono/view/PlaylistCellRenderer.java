package com.bono.view;

import com.bono.Utils;
import com.bono.api.Song;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;

/**
 * hallo blbabla
 * Created by bono on 2/28/16.
 */
public class PlaylistCellRenderer extends JPanel implements ListCellRenderer {

    private JLabel artist = new JLabel();
    private JLabel title = new JLabel();
    private JPanel topPanel = new JPanel();
    private JLabel time = new JLabel();

    public PlaylistCellRenderer() {
        super();
        setOpaque(true);
        topPanel.setLayout(new GridLayout(1,2));
        setLayout(new GridLayout(2,1));
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Song song = (Song) value;

        if (song.getArtist() != null) {
            artist.setText(song.getArtist());
        } else {
            artist.setText(" - no artist - ");
        }
        if (song.getTitle() != null) {
            title.setText(song.getTitle());
        } else {
            title.setText(song.getFile());
        }

        time.setText(Utils.time(Duration.ofSeconds(Long.parseLong(song.getTime()))));
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
            topPanel.setBackground(list.getSelectionBackground());
            topPanel.setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
            topPanel.setBackground(list.getBackground());
            topPanel.setForeground(list.getForeground());
        }

        topPanel.add(artist);
        topPanel.add(title);
        add(topPanel);
        add(time);
        return this;

    }
}
