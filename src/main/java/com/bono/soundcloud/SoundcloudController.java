package com.bono.soundcloud;

import com.bono.api.*;
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

/**
 * Created by hendriknieuwenhuis on 19/02/16.
 */
public class SoundcloudController extends MouseAdapter implements ActionListener {

    public static final String CLIENTID = "93624d1dac08057730320d42ba5a0bdc";

    private SoundcloudView soundcloudView;
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

    public void setSoundcloudView(SoundcloudView soundcloudView) {
        this.soundcloudView = soundcloudView;
        init();
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

        Thread thread = new Thread(new SearchWorker(SearchWorker.QUERY, e.getActionCommand()));
        thread.start();

    }

    // must be synchronized.
    private void populateModel(JSONArray jsonArray) {
        synchronized (listModel) {
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

                SwingUtilities.invokeLater(() -> {
                    listModel.addElement(result);
                });

                updateProgressBar(counter);
                counter++;

            }
            endPopulating();

            setProgressBarVisible(false);
        }
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

            if (!list.isSelectionEmpty()) {
                SwingUtilities.invokeLater(() -> {
                    new SoundcloudPopup(list, e, playlist);
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

    private class MoreButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // if next_hfer !null.
            // load next batch.
            Thread thread = new Thread(new SearchWorker(SearchWorker.NEXT_HREF, nextHref));
            thread.start();
        }
    }

    // swingworker
    private class SearchWorker implements Runnable {

        public static final int NEXT_HREF = 0;
        public static final int QUERY = 1;

        private int searchType;
        private String searchValue;
        private SoundcloudSearch soundcloudSearch;

        public SearchWorker(int searchType, String searchValue) {
            this.searchType = searchType;
            this.searchValue = searchValue;
            soundcloudSearch = new SoundcloudSearch(CLIENTID);
        }
        @Override
        public void run() {
            if (searchValue == null || searchValue.isEmpty()) {
                return;
            }

            setProgressBarVisible(true);
            updateProgressBar(25);
            JSONObject queryResult = null;
            if (searchType == QUERY) {
                queryResult = soundcloudSearch.searchPartitioned(searchValue);
            } else if (searchType == NEXT_HREF) {
                queryResult = soundcloudSearch.nextHref(searchValue);
            } else {
                return;
            }

            if (((String) queryResult.get(SoundcloudSearch.NEXT_HREF)).isEmpty()) {
                SwingUtilities.invokeLater(() -> {
                    soundcloudView.enableMore(false);
                });
            } else {
                nextHref = (String) queryResult.get(SoundcloudSearch.NEXT_HREF);
                SwingUtilities.invokeLater(() -> {
                     soundcloudView.enableMore(true);
                });
            }

            populateModel(queryResult.getJSONArray(SoundcloudSearch.COLLECTION));
        }
    }
}
