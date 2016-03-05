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

    private JLabel title = new JLabel();
    private JLabel time = new JLabel();

    public PlaylistCellRenderer() {
        super();
        setOpaque(true);
        setLayout(new GridLayout(2,1));
        add(title);
        add(time);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        Song song = (Song) value;
        JLabel label = new JLabel();
        //setLayout(new GridLayout(1,2));
        if (song.getTitle() != null) {
            title.setText(song.getTitle());
        } else {
            title.setText(song.getFile());
        }
        //System.out.println(song.toString());
        //if ()
        time.setText(Utils.time(Duration.ofSeconds(Long.parseLong(song.getTime()))));
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }


        return this;
    }
}
