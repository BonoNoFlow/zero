package com.bono.view;

import com.bono.Utils;
import com.bono.api.Song;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;

/**
 * hallo blbabla
 * Created by bono on 2/28/16.
 *
 * TODO Do some move to paint method in ListUIManager???
 */
public class PlaylistCellRenderer extends JPanel implements ListCellRenderer {

    private final String FONTNAME = "Times Roman";

    private Font font = null;

    private Color barColor = new Color(251, 244, 250);

    private JLabel artist = new JLabel();
    private JLabel title = new JLabel();
    private JPanel topPanel = new JPanel();
    private JLabel time = new JLabel();

    public PlaylistCellRenderer() {
        super();
        setOpaque(true);
        topPanel.setLayout(new GridLayout(1,2));
        setLayout(new GridLayout(2,1));
        artist.setFont(new Font(FONTNAME, Font.BOLD, 12));

        time.setFont(new Font(FONTNAME, Font.PLAIN, 10));

        title.setFont(new Font(FONTNAME, Font.BOLD, 12));
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Song song = (Song) value;

        Graphics g = list.getGraphics();
        Font font = g.getFont();

        String fontName = "Verdana";

        if (song.getTitle() != null) {

            artist.setText(song.getTitle());

        } else {
            title.setText(song.getFileName());
        }

        if (song.getAlbum() != null) {

            time.setText(song.getArtist() + " - " + song.getAlbum());

        } else {

            time.setText(song.getArtist());
        }

        if (song.getTime() > -1L) {
            Duration duration = Duration.ofSeconds(song.getTime());

            title.setText(Utils.time(duration));
        }

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
            topPanel.setBackground(list.getSelectionBackground());
            topPanel.setForeground(list.getSelectionForeground());
        } else {
            if (index %2 == 0) {
                setBackground(barColor);
                setForeground(barColor);
                topPanel.setBackground(barColor);
                topPanel.setForeground(barColor);
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
                topPanel.setBackground(list.getBackground());
                topPanel.setForeground(list.getForeground());
            }
        }

        topPanel.add(artist);
        topPanel.add(title);
        add(topPanel);
        add(time);
        return this;

    }
}
