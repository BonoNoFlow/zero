package com.bono.soundcloud;

import com.bono.api.Playlist;
import com.bono.view.InfoDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

/**
 * Created by hendriknieuwenhuis on 22/09/16.
 */
public class SoundcloudPopup implements ActionListener {

    private static final String LOAD = "load";
    private static final String INFO = "info";

    private JList list;
    private Playlist playlist;

    private JPopupMenu popupMenu;


    public SoundcloudPopup(JList list, MouseEvent event, Playlist playlist) {
        this.list = list;
        this.playlist = playlist;
        init(event);
    }

    private void init(MouseEvent event) {
        if (popupMenu == null) {
            popupMenu = new JPopupMenu();
            JMenuItem loadItem = new JMenuItem(LOAD);
            loadItem.addActionListener(this);
            popupMenu.add(loadItem);
            JMenuItem infoItem = new JMenuItem(INFO);
            infoItem.addActionListener(this);
            popupMenu.add(infoItem);
        }
        popupMenu.show(list, event.getX(), event.getY());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JMenuItem menuItem = (JMenuItem) e.getSource();
        DefaultListModel<Result> model = (DefaultListModel) list.getModel();

        if (menuItem.getText().equals(LOAD)) {
            int[] selected = list.getSelectedIndices();
            Playlist.CommandList commandList = playlist.sendCommandList();
            for (int i : selected) {
                commandList.add(Playlist.LOAD, loadUrl(model.get(i).getUrl()));
            }
            try {
                commandList.send();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (menuItem.getText().equals(INFO)) {
            int selected = list.getSelectedIndex();
            //JOptionPane.showMessageDialog(null, model.get(selected).getDescription());
            JDialog dialog = new JDialog();

            /*
            JTextArea textArea = new JTextArea();
            textArea.setEditable(false);
            textArea.setColumns(40);
            textArea.setRows(10);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);*/
            JEditorPane textArea = new JEditorPane();
            textArea.setEditable(false);
            textArea.setPreferredSize(new Dimension(240, 120));
            if (model.get(selected).getDescription().isEmpty()) {
                textArea.setText("no info");
            } else {
                textArea.setContentType("text/html");
                textArea.setText(model.get(selected).getDescription());
            }
            JScrollPane scrollPane = new JScrollPane(textArea);
            dialog.getContentPane().add(scrollPane);
            dialog.setLocationRelativeTo(list);
            dialog.pack();
            dialog.setVisible(true);

        }

    }

    private String loadUrl(String http) {
        String param = "";
        int httpIndex = 0;
        if (http.contains("http://")) {
            httpIndex = http.lastIndexOf("http://") + "http://".length();
        } else if (http.contains("https://")) {
            httpIndex = http.lastIndexOf("https://") + "https://".length();
        }
        param = "soundcloud://url/" + http.substring(httpIndex);

        return param;
    }


}
