package com.bono.soundcloud;

import com.bono.Utils;
import com.bono.view.SoundcloudPanel;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;

/**
 * Created by hendriknieuwenhuis on 19/02/16.
 */
public class SoundcloudController {

    private SoundcloudPanel soundcloudPanel;
    private SoundcloudSearch soundcloudSearch;
    private DefaultListModel<Result> listModel;

    private ExecutorService executorService;

    public SoundcloudController() {
        init();
    }

    public SoundcloudController(ExecutorService executorService, SoundcloudPanel soundcloudPanel) {
        this.executorService = executorService;
        this.soundcloudPanel = soundcloudPanel;
        init();
    }

    private void init() {
        listModel = new DefaultListModel<>();
        soundcloudSearch = new SoundcloudSearch(this);
        soundcloudPanel.addSearchListener(new SearchListener());
        soundcloudPanel.addMouseListener(new ResultMouseListener());
    }

    public SoundcloudSearch getSoundcloudSearch() {
        return soundcloudSearch;
    }

    public void setSoundcloudPanel(SoundcloudPanel soundcloudPanel) {
        this.soundcloudPanel = soundcloudPanel;
        soundcloudPanel.getSearchField().addActionListener(new SearchListener());
    }

    public DefaultListModel<Result> getListModel() {
        return listModel;
    }

    public void setListModel(DefaultListModel<Result> listModel) {
        this.listModel = listModel;
    }

    public void setSoundcloudSearch(SoundcloudSearch soundcloudSearch) {
        this.soundcloudSearch = soundcloudSearch;
    }

    public SoundcloudPanel getSoundcloudPanel() {
        return soundcloudPanel;
    }


    private class SearchListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            listModel = new DefaultListModel<>();
            soundcloudPanel.clearSearchField();

            JSONArray response = soundcloudSearch.searchTracks(e.getActionCommand());

            Iterator iterator = response.iterator();
            while (iterator.hasNext()) {
                JSONObject object = (JSONObject) iterator.next();
                int seconds = (Integer) object.get("duration");
                Duration duration = Duration.ofMillis(seconds);

                String time = Utils.time(duration);

                Result result = new Result(object.getString("permalink_url"), object.getString("title"), time);

                String urlString = "";
                if (!object.get("artwork_url").equals(null)) {
                    urlString = String.valueOf(object.get("artwork_url"));

                    urlString = urlString.replaceAll("large", "tiny");

                    Image image = null;
                    try {
                        URL url = new URL(urlString);
                        image = ImageIO.read(url);
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    image = image.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                    result.setImage(image);
                } else {
                    System.out.println("artwork url null! ");

                    Image image = null;
                    try {
                        URL url = this.getClass().getResource("/default-icon.png");
                        image = ImageIO.read(url);
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    image = image.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                    result.setImage(image);

                }

                listModel.addElement(result);
            }
            soundcloudPanel.getResultList().setModel(listModel);
        }
    }

    private class ResultMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            System.out.println("clicked" + e.getButton() + MouseEvent.BUTTON3);
            if (e.getButton() == MouseEvent.BUTTON3) {
                System.out.println("clicked!!!!!");
                JPopupMenu popupMenu = new JPopupMenu();
                JMenuItem addItem = new JMenuItem("add");
                    addItem.addActionListener(event -> {

                        // TODO add the selected track !!!!!!!
                        ListSelectionModel model = soundcloudPanel.getResultList().getSelectionModel();
                        int track = model.getAnchorSelectionIndex();

                        // TODO load this url!!
                        System.out.println(Utils.loadUrl(listModel.get(track).getUrl()));
                });
                popupMenu.add(addItem);
                popupMenu.show(soundcloudPanel.getResultList(), e.getX(), e.getY());

            }
        }


    }
}
