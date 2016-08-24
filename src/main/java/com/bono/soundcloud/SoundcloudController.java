package com.bono.soundcloud;

import com.bono.api.*;
import com.bono.Utils;
import com.bono.api.protocol.MPDPlaylist;
import com.bono.view.SoundcloudView;
import com.bono.view.MPDPopup;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.util.*;
import java.util.List;

/**
 * Created by hendriknieuwenhuis on 19/02/16.
 */
public class SoundcloudController extends MouseAdapter implements ActionListener, ChangeListener {

    private static final String HTTP = "http://api.soundcloud.com/tracks";
    private static final String HTTPS = "https://api.soundcloud.com/tracks";

    private static final String SPLIT = "\\?";

    public static final String CLIENTID = "93624d1dac08057730320d42ba5a0bdc";

    private SoundcloudView soundcloudView;
    private SoundcloudSearch soundcloudSearch;
    private DefaultListModel<Result> listModel;

    //private DBExecutor dbExecutor;
    private ClientExecutor clientExecutor;

    private Config config;

    private int results = 50;

    @Deprecated
    public SoundcloudController() {
        init();
    }

    public SoundcloudController(ClientExecutor clientExecutor) {
        this.clientExecutor = clientExecutor;
        //init();
    }

    public SoundcloudController(ClientExecutor clientExecutor, SoundcloudView soundcloudView) {
        this.clientExecutor = clientExecutor;
        this.soundcloudView = soundcloudView;
        init();
    }

    public SoundcloudController(Config config, ClientExecutor clientExecutor, SoundcloudView soundcloudView) {
        this.config = config;
        this.clientExecutor = clientExecutor;
        this.soundcloudView = soundcloudView;
        init();
    }

    public SoundcloudController(int results, ClientExecutor clientExecutor, SoundcloudView soundcloudView) {
        this.results = results;
        this.clientExecutor = clientExecutor;
        this.soundcloudView = soundcloudView;
        init();
    }

    private void init() {
        listModel = new DefaultListModel<>();
        if (soundcloudView != null) {
            soundcloudView.addSearchListener(this);
            soundcloudView.addMouseListener(this);
        }
    }

    public SoundcloudSearch getSoundcloudSearch() {
        return soundcloudSearch;
    }

    public void setSoundcloudView(SoundcloudView soundcloudView) {
        this.soundcloudView = soundcloudView;
        //soundcloudView.getSearchField().addActionListener(this);
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

    public int getResults() {
        return results;
    }

    public void setResults(int results) {
        this.results = results;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        /*
        Het laden van de artwork moet een class worden.
         */
        listModel = new DefaultListModel<>();
        soundcloudSearch = new SoundcloudSearch(SoundcloudController.CLIENTID, "50");
        soundcloudView.clearSearchField();
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
                    // unsupported image so image is null.
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
        }
        soundcloudView.getResultList().setModel(listModel);
    }

    // adds artist and title to song object in the playlist
    // when the song is a soundcloud file and only the
    // url is known.
    @Override
    public void stateChanged(EventObject eventObject) {
        Song song = (Song) eventObject.getSource();

        SoundcloudSong sSong = new SoundcloudSong(song.getAlbum(), song.getAlbumArtist(),
                song.getArtist(), song.getComposer(), song.getDate(), song.getDisc(),
                song.getFilePath(), song.getGenre(), song.getId(), song.getLastModified(),
                song.getName(), song.getPos(), song.getTime(), song.getTitle(), song.getTrack());

        if (song.getFilePath().startsWith(HTTP) || song.getFilePath().startsWith(HTTPS)) {

            String[] urlBuild = song.getFilePath().split("=");
            String url = urlBuild[0].replaceAll("/stream", "") + "=" +SoundcloudController.CLIENTID;

            JSONObject response = null;
            try {
                URL soundcloud = new URL(url);
                URLConnection soundcloudConnection = soundcloud.openConnection();
                JSONTokener jsonTokener = new JSONTokener(soundcloudConnection.getInputStream());
                response = new JSONObject(jsonTokener);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            sSong.setTitle(response.getString("title"));
            sSong.setArtist(response.getString("permalink"));
            song = sSong;

        }
    }

    /*
        Shows a JPopupMenu that contains an 'load' function
        to load the tracks to the playlist.

        Also the result amount can be adjusted.
         */
    private void showPopup(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            JList list = (JList) e.getSource();

            JPopupMenu popupMenu;
            if (!list.isSelectionEmpty()) {

                popupMenu = new JPopupMenu();
                JMenuItem load = new JMenuItem("load");
                load.addActionListener(new AddListener(list));
                popupMenu.add(load);
                JMenuItem results = new JMenuItem("results");
                // TODO add listener that makes user possible to change result amount.
                popupMenu.add(results);
                popupMenu.show(soundcloudView.getResultList(), e.getX(), e.getY());
            } else {
                popupMenu = new JPopupMenu();
                JMenuItem results = new JMenuItem("results");
                // TODO add listener that makes user possible to change result amount.
                popupMenu.add(results);
                popupMenu.show(soundcloudView.getResultList(), e.getX(), e.getY());
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
            List<Command> commands = new ArrayList<>();
            commands.add(new DefaultCommand(DefaultCommand.COMMAND_LIST_BEGIN));
            DefaultListModel<Result> model = (DefaultListModel) list.getModel();
            for (int i : selected) {
                commands.add(new DefaultCommand(MPDPlaylist.LOAD, loadUrl(model.get(i).getUrl())));
            }
            commands.add(new DefaultCommand(DefaultCommand.COMMAND_LIST_END));

            List<String> reply = new ArrayList<>();
            try {
                reply = clientExecutor.executeList(commands);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }



        private String loadUrl(String http) {
            String param = "";
            int httpIndex = 0;
            if (http.contains(HTTP)) {
                httpIndex = http.lastIndexOf(HTTP) + HTTP.length();
            } else if (http.contains(HTTPS)) {
                httpIndex = http.lastIndexOf(HTTPS) + HTTPS.length();
            }
            param = "soundcloud://url/" + http.substring(httpIndex);

            return param;
        }
    }
}
