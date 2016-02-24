package com.bono;

/**
 * Created by hendriknieuwenhuis on 12/01/16.
 */
public class MPDUrl {

    private static String HTTP = "http";
    private static String HTTPS = "https";
    private static String SOUNDCLOUD = "soundcloud";
    private static String WWW = "www";

    public static String getUrl(String url) {
        if (url.startsWith(HTTP) || url.startsWith(HTTPS)) {
            return url;
        }
        return null;
    }
}
