package com.bono.soundcloud;

import com.bono.api.Song;

/**
 * Created by bono on 8/24/16.
 */
public class SoundcloudSong extends Song {


    public SoundcloudSong(String album, String albumArtist, String artist, String composer, String date, String disc, String filePath, String genre, int id, String lastModified, String name, int pos, long time, String title, int track) {
        super(album, albumArtist, artist, composer, date, disc, filePath, genre, id, lastModified, name, pos, time, title, track);
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
