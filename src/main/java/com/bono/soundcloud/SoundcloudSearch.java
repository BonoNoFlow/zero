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

    public static final String COLLECTION = "collection";
    public static final String NEXT_HREF = "next_href";

    private String clientId;

    private String resultsAmount;

    public SoundcloudSearch(String clientId) {
        this.clientId = clientId;
    }


    public SoundcloudSearch(String clientId, String resultsAmount) {
        this(clientId);
        this.resultsAmount = resultsAmount;
    }

    /**
     * Method searchPartitioned queries the database by the given
     * url. It returns an JsonObject containing a collection of
     * results, a JsonArray of JsonObjects, and it contains a url
     * for the next set of results if there are next results.
     * <p>
     * The results JSONArray can be accessed by using the key
     * 'collection' or Soundcloud.COLLECTION.
     * The url for the next results can be accessed by using
     * key 'next_href or Soundcloud.NEXT_HREF'
     *
     * @param queryURL Url to reach database with query request.
     *                 Method createPartitionedQueryURL can be used to create
     *                 this url.
     * @return JSONObject containing results and url for next results.
     */
    public JSONObject searchPartitioned(String queryURL) {
        JSONTokener jsonTokener = null;
        try {
            URL soundcloudURL = new URL(queryURL);
            URLConnection soundcloudConnection = soundcloudURL.openConnection();
            jsonTokener = new JSONTokener(soundcloudConnection.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONObject(jsonTokener);
    }

    /**
     * SoundcloudSearch for tracks in the soundcloud database.
     *
     * @param value
     * @return JSONArray of JSONObjects with track information.
     */
    public JSONArray searchTracks(String value) {

        String search = "https://api.soundcloud.com/tracks.json?client_id=" + clientId + "&q=" +
                constructSearchString(value) + "&limit=";

        if (resultsAmount != null) {
            search += resultsAmount;
        } else {
            search += "20";
        }

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

    public String createPartitionedQueryURL(String search) {
        return "https://api.soundcloud.com/tracks.json?linked_partitioning=1&client_id="
            + clientId + "&q=" + constructSearchString(search) + "&limit=50";
    }



    // Replace spaces between words with '+'.
    private String constructSearchString(String value) {
        if (value.contains(" ")) {
            value = value.replaceAll(" ", "+");
        }
        return value;
    }

}
