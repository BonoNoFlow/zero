package com.bono.soundcloud;

import com.bono.Utils;
import com.bono.api.Playlist;
import com.bono.api.Song;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.MouseAdapter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

/**
 * Created by bono on 3/5/16.
 *
 * Get additional track info for soundcloud tracks. In
 * this case the title is set because only stream url
 * is given by the server.
 *
 * The class functions as a change listener thats called
 * by the playlist.
 */
public class AdditionalTrackInfo implements ChangeListener {

    private static final String HTTP = "http://api.soundcloud.com/tracks";
    private static final String HTTPS = "https://api.soundcloud.com/tracks";

    private static final String SPLIT = "\\?";

    private String clientId = "";

    public AdditionalTrackInfo(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        Playlist playlist = (Playlist) e.getSource();

        Iterator<Song> i = playlist.iterator();

        while (i.hasNext()) {
            Song song = i.next();
            if (song.getFile().startsWith(HTTP) || song.getFile().startsWith(HTTPS)) {

                String[] urlBuild = song.getFile().split("=");
                String url = urlBuild[0].replaceAll("/stream", "") + "=" +SoundcloudController.CLIENTID;
                //System.out.println(url);
                JSONObject response = null;
                try {
                    URL soundcloud = new URL(url);
                    URLConnection soundcloudConnection = soundcloud.openConnection();
                    JSONTokener jsonTokener = new JSONTokener(soundcloudConnection.getInputStream());
                    response = new JSONObject(jsonTokener);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                song.setTitle(response.getString("title"));
                song.setArtist(response.getString("permalink"));
                //JSONObject user = response.getJSONObject("user");
                //System.out.println(user.getString("permalink"));
                Utils.Log.print(getClass().getName() + ": info added!");

            }
            //System.out.println("Additional info added!");

        }
    }
}
