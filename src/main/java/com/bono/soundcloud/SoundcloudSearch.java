package com.bono.soundcloud;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by hendriknieuwenhuis on 15/02/16.
 */
public class SoundcloudSearch {

    private SoundcloudController soundcloudController;

    public SoundcloudSearch() {};

    public SoundcloudSearch(SoundcloudController soundcloudController) {
        this.soundcloudController = soundcloudController;
    }

    /**
     * SoundcloudSearch for tracks in the soundcloud database.
     * @param value
     * @return JSONArray of JSONObjects with track information.
     */
    public JSONArray searchTracks(String value) {
        String search = "https://api.soundcloud.com/tracks.json?client_id=93624d1dac08057730320d42ba5a0bdc&q=" +
                constructSearchString(value) + "&limit=50";

        JSONArray response = null;

        List<Result> results = new ArrayList<Result>();
        try {
            URL soundcloud = new URL(search);
            URLConnection soundcloudConnection = soundcloud.openConnection();
            JSONTokener jsonTokener = new JSONTokener(soundcloudConnection.getInputStream());
            response = new JSONArray(jsonTokener);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    // Replace spaces between words with '+'.
    private String constructSearchString(String value) {
        if (value.contains(" ")) {
            value = value.replaceAll(" ", "+");
        }
        return value;
    }

}
