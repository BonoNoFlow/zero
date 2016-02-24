package com.bono.models;

import com.bono.events.PlaylistEvent;
import com.bono.events.PlaylistListener;
import com.bono.models.Song;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by hendriknieuwenhuis on 29/07/15.
 */
public class Playlist {

    private static final String FILE = "file:";
    private static final String LAST_MODIFIED = "Last-Modified:";
    private static final String TITLE = "Title:";
    private static final String ALBUM = "Album:";
    private static final String ARTIST = "Artist:";
    private static final String GENRE = "Genre:";
    private static final String DATE = "Date:";
    private static final String TRACK = "Track:";
    private static final String ALBUM_ARTIST = "AlbumArtist:";
    private static final String TIME = "Time:";
    private static final String POS = "Pos:";
    private static final String ID = "Id:";
    private static final String NAME = "Name:";

    private HashMap<String, Song> playlist;

    private LinkedList<Song> list;

    private List<PlaylistListener> listeners = new ArrayList<>();

    public Playlist() {}

    public Playlist(String entry) {
        populate(entry);
    }

    public void populate(String entry) {
        list = new LinkedList<>();
        Song song = null;
        String[] songs = entry.split("\n");
        int count = 0;
        for (String line : songs) {
            System.out.println(line);
            //if (song != null) list.addLast(song);

            if (line.startsWith(FILE)) {
                System.out.println(count);
                song = new Song();
                song.setFile(line.substring((FILE.length() + 1)));
            } else if (line.startsWith(LAST_MODIFIED)) {
                song.setLastModified(line.substring((LAST_MODIFIED.length() + 1)));
            } else if (line.startsWith(TITLE)) {
                song.setTitle(line.substring((TITLE.length() + 1)));
            } else if (line.startsWith(ALBUM)) {
                song.setAlbum(line.substring((ALBUM.length() + 1)));
            } else if (line.startsWith(ARTIST)) {
                song.setArtist(line.substring((ARTIST.length() + 1)));
            } else if (line.startsWith(GENRE)) {
                song.setGenre(line.substring((GENRE.length() + 1)));
            } else if (line.startsWith(DATE)) {
                song.setDate(line.substring((DATE.length() + 1)));
            } else if (line.startsWith(TRACK)) {
                song.setTrack(line.substring((TRACK.length() + 1)));
            } else if (line.startsWith(ALBUM_ARTIST)) {
                song.setAlbumArtist(line.substring((ALBUM_ARTIST.length() + 1)));
            } else if (line.startsWith(NAME)){
                song.setName(line.substring((NAME.length() + 1)));
                //System.out.println(line + " " + count);
            } else if (line.startsWith(TIME)) {
                song.setTime(line.substring((TIME.length() + 1)));
            } else if (line.startsWith(POS)) {
                song.setPos(line.substring((POS.length() + 1)));
            } else if (line.startsWith(ID)) {
                song.setId(line.substring((ID.length() + 1)));
                list.addLast(song);
            }
            count++;
        }
        fireListeners();
    }

    public void fireListeners() {
        for (PlaylistListener playlistListener : listeners) {
            playlistListener.playlistChange(new PlaylistEvent(this));
        }
    }

    public LinkedList<Song> getList() {
        return list;
    }

    public void addPlaylistListener(PlaylistListener playlistListener) {
        listeners.add(playlistListener);
    }



    @Deprecated
    public HashMap<String, Song> getPlaylist() {
        return playlist;
    }

    /*
    Get a song, key is id of song.
     */
    public Song getSong(int index) {
        return list.get(index);
    }

    public int getSize() {
        return list.size();
    }
}
