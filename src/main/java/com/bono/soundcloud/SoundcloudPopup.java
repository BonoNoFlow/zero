package com.bono.soundcloud;

import com.bono.api.Playlist;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

/**
 * Created by hendriknieuwenhuis on 22/09/16.
 */
public class SoundcloudPopup implements ActionListener {

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
            JMenuItem menuItem = new JMenuItem("load");
            menuItem.addActionListener(this);
            popupMenu.add(menuItem);
        }
        popupMenu.show(list, event.getX(), event.getY());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int[] selected = list.getSelectedIndices();

        Playlist.CommandList commandList = playlist.sendCommandList();
        DefaultListModel<Result> model = (DefaultListModel) list.getModel();
        for (int i : selected) {
            commandList.add(Playlist.LOAD, loadUrl(model.get(i).getUrl()));
        }
        try {
            commandList.send();
        } catch (Exception ex) {
            ex.printStackTrace();
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
