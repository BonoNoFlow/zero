package com.bono.database;

import com.bono.soundcloud.Result;
import com.bono.soundcloud.SoundcloudController;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hendriknieuwenhuis on 18/09/16.
 */
public class TestSoundcloud {

    private String id = SoundcloudController.CLIENTID;
    private String limit = "20";
    //private String offset = "0";
    private int offset = 1;

    public TestSoundcloud() {
        List<JSONArray> response = searchTracks();
        System.out.println("List length = " + response.size());

        Result result = null;
        List<Result> results = new ArrayList<>();

        Iterator i = response.iterator();
        while (i.hasNext()) {
            JSONArray array = (JSONArray) i.next();
            Iterator i1 = array.iterator();
            while (i1.hasNext()) {
                JSONObject object = (JSONObject) i1.next();
                int seconds = (Integer) object.get("duration");
                Duration duration = Duration.ofMillis(seconds);

                String time = SoundcloudController.time(duration);

                result = new Result(object.getString("permalink_url"), object.getString("title"), time);
                results.add(result);
            }
        }
        System.out.println(results.size());

        Iterator<Result> iR = results.iterator();
        while (iR.hasNext()) {
            System.out.println(iR.next().toString());
        }
        //}
    }

    /**
     * SoundcloudSearch for tracks in the soundcloud database.
     *
     * @return JSONArray of JSONObjects with track information.
     */
    public List<JSONArray> searchTracks() {

        JSONArray response = null;

        List<JSONArray> results = new ArrayList<>();
        while (offset < 200) {
            try {
                System.out.println(offset);
                String search = urlOffset(offset);
                URL soundcloud = new URL(search);
                URLConnection soundcloudConnection = soundcloud.openConnection();
                JSONTokener jsonTokener = new JSONTokener(soundcloudConnection.getInputStream());
                response = new JSONArray(jsonTokener);
                //offset += response.length();
                offset += 20;

                results.add(response);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    // Replace spaces between words with '+'.
    private String constructSearchString(String value) {
        if (value.contains(" ")) {
            value = value.replaceAll(" ", "+");
        }
        return value;
    }

    private String urlOffset(int offset) {
        return "https://api.soundcloud.com/tracks?q=autechre&limit=" + limit + "&offset=" + Integer.toString(offset) + "&q=U2&client_id=" + id + "&format=json";
    }

    public static void main(String[] args) {
        new TestSoundcloud();
    }
}
