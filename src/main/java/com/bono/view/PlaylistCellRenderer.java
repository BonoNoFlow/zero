package com.bono.view;

import com.bono.Utils;
import com.bono.playlist.Song;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;

/**
 * hallo blbabla
 * Created by bono on 2/28/16.
 */
public class PlaylistCellRenderer extends JPanel implements ListCellRenderer {

    public PlaylistCellRenderer() {
        super();
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        Song song = (Song) value;

        setLayout(new GridLayout(1,2));
        if (song.getTitle() != null) {
            add(new JLabel(song.getTitle()));
        } else {
            add(new JLabel(song.getFile()));
        }

        add(new JLabel(Utils.time(Duration.ofSeconds(Integer.parseInt(song.getTime())))));
        return null;
    }
}
