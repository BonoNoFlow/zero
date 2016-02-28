package com.bono.models;

import com.bono.events.PlaylistEvent;
import com.bono.events.PlaylistListener;
import com.bono.models.Song;

import java.util.*;

/**
 * Created by hendriknieuwenhuis on 29/07/15.
 */
public class Playlist {

    private HashMap<String, Song> playlist;

    private LinkedList<Song> list;

    private List<PlaylistListener> listeners = new ArrayList<>();

    public Playlist() {}

    public Playlist(String entry) {
        populate(entry);
    }

    public void populate(String entry) {
        // mischien moet dit in de endpoint gechecked wordem
        // om ACK's te tackelen.
        if (entry.endsWith("OK\n")) {
            entry = entry.replaceAll("OK\n", "");
        }
        list = new LinkedList<>();
        Song song = null;
        String[] songs = entry.split("\n");

        for (String line : songs) {

            String[] lineArray = line.split(": ");

            switch (lineArray[0]) {
                case Song.FILE:
                    song = new Song();
                    song.setFile(lineArray[1]);
                    break;
                case Song.LAST_MODIFIED:
                    song.setLastModified(lineArray[1]);
                    break;
                case Song.TITLE:
                    song.setTitle(lineArray[1]);
                    break;
                case Song.ALBUM:
                    song.setAlbum(lineArray[1]);
                    break;
                case Song.ARTIST:
                    song.setArtist(lineArray[1]);
                    break;
                case Song.GENRE:
                    song.setGenre(lineArray[1]);
                    break;
                case Song.DATE:
                    song.setDate(lineArray[1]);
                    break;
                case Song.TRACK:
                    song.setTrack(lineArray[1]);
                    break;
                case Song.ALBUM_ARTIST:
                    song.setAlbumArtist(lineArray[1]);
                    break;
                case Song.NAME:
                    song.setName(lineArray[1]);
                    break;
                case Song.TIME:
                    song.setTime(lineArray[1]);
                    break;
                case Song.POS:
                    song.setPos(lineArray[1]);
                    break;
                case Song.ID:
                    song.setId(lineArray[1]);
                    list.addLast(song);
                    break;
                default:
                    System.out.println("Not a property: " + lineArray[0]);
                    break;
            }

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

    public void printPlaylist() {
        Iterator i = list.iterator();

        while (i.hasNext()) {
            System.out.println(((Song) i.next()).toString());
        }
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
