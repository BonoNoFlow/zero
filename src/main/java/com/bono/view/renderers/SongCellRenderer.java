package com.bono.view.renderers;

import com.bono.Utils;
import com.bono.api.Song;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.time.Duration;

/**
 * Created by bono on 8/18/16.
 */
public class SongCellRenderer extends JPanel implements TableCellRenderer {

    private final String FONTNAME = "Times Roman";

    private final Color barColor = new Color(251, 244, 250);

    private JLabel artist = new JLabel();
    private JLabel title = new JLabel();
    private JLabel time = new JLabel();

    int f = 0;

    int timeColumnWidth = 0;

    public SongCellRenderer() {
        super();
        setOpaque(true);
        setLayout(new GridLayout(2, 1));
        title.setFont(new Font(FONTNAME, Font.BOLD, 12));
        artist.setFont(new Font(FONTNAME, Font.PLAIN, 10));
        time.setFont(new Font(FONTNAME, Font.BOLD, 12));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //System.out.println(f++);
        Song song = (Song) value;

        Graphics g = table.getGraphics();
        Font font = g.getFont();

        if (column == 0) {
            //System.out.println("Column 0: " + song.getTitle());

            if (!song.getTitle().startsWith("http")) {
                title.setText(song.getTitle());

            } else {
                /*
                if (!song.getFilePath().startsWith("http")) {
                    String[] path = song.getFile().split("/");
                    String file = path[(path.length - 1)];
                    title.setText(file);
                } else {
                    title.setText(song.getFile());
                }*/
                title.setText(song.getName());
            }

            if (song.getAlbum() != null && song.getArtist() != null) {
                artist.setText(song.getArtist() + " - " + song.getAlbum());
            } else if (song.getAlbum() == null && song.getArtist() != null){
                artist.setText(song.getArtist());
            } else {
                artist.setText(song.getFilePath());
            }
            add(title);
            add(artist);
        } else if (column == 1) {

            if (song.getTime() != -1L) {
                ;

                time.setText(song.getFormattedTime(song.getTime()));

                // set column width
                /*
                if (time.getPreferredSize().width > timeColumnWidth) {
                    timeColumnWidth = time.getPreferredSize().width;
                }
                table.getColumnModel().getColumn(column).setPreferredWidth(timeColumnWidth);
                System.out.println(timeColumnWidth);*/
            }
            add(time);
        }

        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
            //topPanel.setBackground(list.getSelectionBackground());
            //topPanel.setForeground(list.getSelectionForeground());
        } else {
            if (row % 2 == 0) {
                setBackground(barColor);
                setForeground(barColor);
                //topPanel.setBackground(barColor);
                //topPanel.setForeground(barColor);
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
                //topPanel.setBackground(list.getBackground());
                //topPanel.setForeground(list.getForeground());
            }
        }

        return this;
    }
}
