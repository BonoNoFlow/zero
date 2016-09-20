package com.bono.soundcloud;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by hendriknieuwenhuis on 15/02/16.
 */
public class SoundcloudSearch {

    private String clientId;


    private String resultsAmount;

    public SoundcloudSearch(String clientId) {
        this.clientId = clientId;
    }


    public SoundcloudSearch(String clientId, String resultsAmount) {
        this(clientId);
        this.resultsAmount = resultsAmount;
    }

    public JSONObject searchPartitioned(String value) {
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

    /**
     * SoundcloudSearch for tracks in the soundcloud database.
     * @param value
     * @return JSONArray of JSONObjects with track information.
     */
    public JSONArray searchTracks(String value) {

        String search = "https://api.soundcloud.com/tracks.json?client_id=" + clientId + "&q=" +
                constructSearchString(value) + "&limit=";

        //if (config == null) {
            search += resultsAmount;
        //} else {
            // TODO search results aantal in config opnemen.
            //search += 10;
        //}

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
