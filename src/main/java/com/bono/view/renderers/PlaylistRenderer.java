package com.bono.view.renderers;

import com.bono.api.Song;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created by bono on 8/27/16.
 */
public class PlaylistRenderer implements ListCellRenderer, PlayingRenderer {

    private final String FONTNAME = "Times Roman";

    private final Color barColor = new Color(251, 244, 250);
    private final Color playingColor = new Color(255, 204, 255);

    private JPanel mainPanel;
    private JPanel songPanel;
    private JLabel titleField;
    private JLabel artistField;
    private JLabel timeField;

    private int playing;

    public PlaylistRenderer() {
        super();
        mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        songPanel = new JPanel();
        songPanel.setLayout(new BoxLayout(songPanel, BoxLayout.Y_AXIS));
        mainPanel.add(songPanel);
        titleField = buildTextField(JLabel.LEFT, new Font(FONTNAME, Font.BOLD, 12));
        songPanel.add(titleField);
        artistField = buildTextField(JLabel.LEFT, new Font(FONTNAME, Font.BOLD, 10));
        songPanel.add(artistField);

        timeField = buildTextField(SwingConstants.LEFT, new Font(FONTNAME, Font.BOLD, 12));
        timeField.setBorder(BorderFactory.createEmptyBorder(0,0,0,2));
        mainPanel.add(Box.createHorizontalGlue());
        mainPanel.add(timeField);
    }

    private JLabel buildTextField(int alignment, Font font) {
        JLabel field = new JLabel();

        field.setHorizontalAlignment(alignment);

        field.setFont(font);

        field.setBorder(null);

        return field;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Song song = (Song) value;

        if (!song.getTitle().startsWith("http")) {
            titleField.setText(song.getTitle());

        } else {

            titleField.setText(song.getName());
        }

        if (song.getAlbum() != null && song.getArtist() != null) {
            artistField.setText(song.getArtist() + " - " + song.getAlbum());
        } else if (song.getAlbum() == null && song.getArtist() != null){
            artistField.setText(song.getArtist());
        } else {
            artistField.setText(song.getFilePath());
        }



        if (song.getTime() != -1L) {


            timeField.setText(song.getFormattedTime(song.getTime()));


        }


        if (isSelected) {
            mainPanel.setBackground(list.getSelectionBackground());
            mainPanel.setForeground(list.getSelectionForeground());
            songPanel.setBackground(list.getSelectionBackground());
            songPanel.setForeground(list.getSelectionForeground());
        } else {
            if (index % 2 == 0 && playing != index) {
                mainPanel.setBackground(barColor);
                mainPanel.setForeground(barColor);
                songPanel.setBackground(barColor);
                songPanel.setForeground(barColor);

            } else if (playing != index) {
                mainPanel.setBackground(list.getBackground());
                mainPanel.setForeground(list.getForeground());
                songPanel.setBackground(list.getBackground());
                songPanel.setForeground(list.getForeground());
            } else if (playing == index && playing != -1) {
                mainPanel.setBackground(playingColor);
                mainPanel.setForeground(playingColor);
                songPanel.setBackground(playingColor);
                songPanel.setForeground(playingColor);
            }
        }
        return mainPanel;
    }

    @Override
    public void setPlaying(int playing) {
        this.playing = playing;
    }
}
