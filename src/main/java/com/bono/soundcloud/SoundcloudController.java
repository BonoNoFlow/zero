package com.bono.soundcloud;

import com.bono.api.*;
import com.bono.Utils;
import com.bono.api.protocol.MPDPlaylist;
import com.bono.view.SoundcloudView;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.util.*;
import java.util.List;
import java.util.concurrent.RunnableFuture;

/**
 * Created by hendriknieuwenhuis on 19/02/16.
 */
// TODO implement playlist class instead of clientexecutor to load urls to the playlist.
public class SoundcloudController extends MouseAdapter implements ActionListener, ChangeListener {

    private static final String HTTP = "http://api.soundcloud.com/tracks";
    private static final String HTTPS = "https://api.soundcloud.com/tracks";
    private static final String NEXT_HREF = "next_href";

    private static final String SPLIT = "\\?";

    public static final String CLIENTID = "93624d1dac08057730320d42ba5a0bdc";

    private SoundcloudView soundcloudView;
    private SoundcloudSearch soundcloudSearch;
    private DefaultListModel<Result> listModel;

    private MPDClient mpdClient;

    private Playlist playlist;
    private String nextHref = "";

    public SoundcloudController(MPDClient mpdClient) {
        this.playlist = mpdClient.getPlaylist();
        this.mpdClient = mpdClient;

    }

    private void init() {
        listModel = new DefaultListModel<>();
        if (soundcloudView != null) {
            soundcloudView.addSearchListener(this);
            soundcloudView.addMouseListener(this);
            soundcloudView.addMorelistener(new MoreButtonListener());
        }
    }

    public SoundcloudSearch getSoundcloudSearch() {
        return soundcloudSearch;
    }

    public void setSoundcloudView(SoundcloudView soundcloudView) {
        this.soundcloudView = soundcloudView;

        init();
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

    public SoundcloudView getSoundcloudView() {
        return soundcloudView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (listModel == null) {
            listModel = new DefaultListModel<>();
        }
        SwingUtilities.invokeLater(() -> {
            listModel.clear();
            soundcloudView.getResultList().setModel(listModel);


        });


        String query = "https://api.soundcloud.com/tracks.json?linked_partitioning=1&client_id="
                + CLIENTID + "&q=" + constructSearchString(e.getActionCommand()) + "&limit=50";

        Thread thread = new Thread(new SearchWorker(query));
        thread.start();

    }

    // adds artist and title to song object in the playlist
    // when the song is a soundcloud file and only the
    // url is known.
    @Deprecated
    @Override
    public void stateChanged(EventObject eventObject) {
        //Song song = (Song) eventObject.getSource();
        System.out.println(getClass().getName() + " calls stateChanged");
        Playlist playlist = (Playlist) eventObject.getSource();

        for (int i = 0; i < playlist.getSize(); i++) {


            if (playlist.getSong(i).getFilePath().startsWith(HTTP) ||
                    playlist.getSong(i).getFilePath().startsWith(HTTPS)) {
                Song song = playlist.getSong(i);
                String[] urlBuild = song.getFilePath().split("=");
                String url = urlBuild[0].replaceAll("/stream", "") + "=" + SoundcloudController.CLIENTID;

                JSONObject response = null;
                try {
                    URL soundcloud = new URL(url);
                    URLConnection soundcloudConnection = soundcloud.openConnection();
                    JSONTokener jsonTokener = new JSONTokener(soundcloudConnection.getInputStream());
                    response = new JSONObject(jsonTokener);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }
        }
    }

    private JSONObject searchPartitioned(String value) {
        try {
            URL soundcloudURL = new URL(value);
            URLConnection soundcloudConnection = soundcloudURL.openConnection();
            JSONTokener jsonTokener = new JSONTokener(soundcloudConnection.getInputStream());
            return new JSONObject(jsonTokener);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void populateModel(JSONArray jsonArray) {
        int bar = soundcloudView.getVerticalBarValue();
        //System.out.println(soundcloudView.getVerticalBarValue() + bar);
        int counter = 25;
        Iterator iterator = jsonArray.iterator();

        while (iterator.hasNext()) {
            JSONObject object = (JSONObject) iterator.next();
            int seconds = (Integer) object.get("duration");
            Duration duration = Duration.ofMillis(seconds);

            String time = time(duration);
            //String time = Song.getFormattedTime(seconds); // TODO fix this

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
                    // unsupported image so image is null.
                    // TODO squarepusher search geeft filenotfound exception. moet afgehandelt worden.
                    if (ioe.getMessage().equals("Unsupported Image Type")) {
                        image = null;
                    } else {
                        ioe.printStackTrace();
                    }
                }


                if (image != null) {
                    image = image.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                    result.setImage(image);
                } else {

                    try {
                        URL url = this.getClass().getResource("/default-icon.png");
                        image = ImageIO.read(url);
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    image = image.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                    result.setImage(image);
                }
            } else {

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
            //setProgressBarVisible(true);
            updateProgressBar(counter);
            counter++;

        }
        endPopulating();
        SwingUtilities.invokeLater(() -> {
            soundcloudView.setVerticalBar(bar);
        });
        setProgressBarVisible(false);
    }

    private String constructSearchString(String value) {
        if (value.contains(" ")) {
            value = value.replaceAll(" ", "+");
        }
        return value;
    }

    private String nextHref(JSONObject queryResult) {
        return (queryResult.get(NEXT_HREF).equals(null)) ? "" : (String) queryResult.get(NEXT_HREF);
    }

    public static String loadUrl(String http) {
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

    public static String time(Duration duration) {
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        String positive = String.format(
                "%d:%02d:%02d",
                absSeconds / 3600,
                (absSeconds % 3600) / 60,
                absSeconds % 60);
        return seconds < 0 ? "-" + positive : positive;
    }

    private void setProgressBarVisible(final boolean bool) {
        SwingUtilities.invokeLater(() -> {
            if (soundcloudView.isProgressBarVisible() && !bool) {
                soundcloudView.setProgressbarVisible(bool);
            } else if (!soundcloudView.isProgressBarVisible() && bool) {
                soundcloudView.setProgressbarVisible(bool);
            }
        });
    }

    private void updateProgressBar(final int value) {
        SwingUtilities.invokeLater(() -> {
            soundcloudView.setProgressValue(value);
        });
    }

    private void endPopulating() {
        SwingUtilities.invokeLater(() -> {
            soundcloudView.setProgressValue(75);
            soundcloudView.setProgressValue(0);
        });
    }
    /*
        Shows a JPopupMenu that contains an 'load' function
        to load the tracks to the playlist.

        Also the result amount can be adjusted.
         */
    private void showPopup(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            JList list = (JList) e.getSource();

            JPopupMenu popupMenu = new JPopupMenu();
            if (!list.isSelectionEmpty()) {
                SwingUtilities.invokeLater(() -> {

                    JMenuItem load = new JMenuItem("load");
                    load.addActionListener(new AddListener(list));
                    popupMenu.add(load);
                    popupMenu.show(soundcloudView.getResultList(), e.getX(), e.getY());
                });

            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        showPopup(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        showPopup(e);
    }

    private class AddListener implements ActionListener {

        private final String HTTP = "http://";
        private final String HTTPS = "https://";

        private ListSelectionModel model;
        private JList list;

        public AddListener(JList list) {
            this.list = list;
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
    }

    private class EndlistListener implements AdjustmentListener {

        @Override
        public void adjustmentValueChanged(AdjustmentEvent e) {
            //e.
        }
    }

    private class MoreButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // if next_hfer !null.
            // load next batch.
            Thread thread = new Thread(new SearchWorker(nextHref));
            thread.start();
        }
    }



    // swingworker
    private class SearchWorker implements Runnable {

        private String url;

        public SearchWorker(String url) {
            this.url = url;
        }
        @Override
        public void run() {
            if (url == null) {
                return;
            }

            setProgressBarVisible(true);
            updateProgressBar(25);
            JSONObject queryResult = searchPartitioned(url);

            nextHref = nextHref(queryResult);
            if (nextHref.isEmpty()) {
                SwingUtilities.invokeLater(() -> {
                    soundcloudView.enableMore(false);
                });
            } else {
                SwingUtilities.invokeLater(() -> {
                    soundcloudView.enableMore(true);
                });
            }

            populateModel(queryResult.getJSONArray("collection"));


        }
    }
}
