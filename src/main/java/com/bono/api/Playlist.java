package com.bono.api;

import com.bono.api.Reply;
import com.bono.api.Song;
import com.bono.events.PlaylistEvent;
import com.bono.events.PlaylistListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.*;

/**
 * Created by hendriknieuwenhuis on 29/07/15.
 *
 * moet aangepast worden om in api package te kunnen.
 *
 * changelistener implementere!
 */
public class Playlist {

    private List<ChangeListener> listeners = new ArrayList<>();

    protected List<Song> songs = new ArrayList<>();

    public Playlist() {}

    public Playlist(String entry) {
        populate(entry);
    }

    public Song getSong(int index) {
        return songs.get(index);
    }

    public void populate(String entry) {
        songs.clear();
        Song song = null;

        Reply reply = new Reply(entry);

        Iterator i = reply.iterator();

        while (i.hasNext()) {

            String[] line = ((String) i.next()).split(Reply.SPLIT_LINE);
            switch (line[0]) {
                case Song.FILE:
                    song = new Song();
                    song.setFile(line[1]);
                    break;
                case Song.LAST_MODIFIED:
                    song.setLastModified(line[1]);
                    break;
                case Song.TITLE:
                    song.setTitle(line[1]);
                    break;
                case Song.ALBUM:
                    song.setAlbum(line[1]);
                    break;
                case Song.ARTIST:
                    song.setArtist(line[1]);
                    break;
                case Song.GENRE:
                    song.setGenre(line[1]);
                    break;
                case Song.DATE:
                    song.setDate(line[1]);
                    break;
                case Song.TRACK:
                    song.setTrack(line[1]);
                    break;
                case Song.ALBUM_ARTIST:
                    song.setAlbumArtist(line[1]);
                    break;
                case Song.NAME:
                    song.setName(line[1]);
                    break;
                case Song.TIME:
                    song.setTime(line[1]);
                    break;
                case Song.POS:
                    song.setPos(line[1]);
                    break;
                case Song.ID:
                    song.setId(line[1]);
                    //songList.addElement(song);
                    songs.add(song);
                    break;
                default:
                    System.out.println("Not a property: " + line[0]);
                    break;
            }

        }
        fireListeners();
    }

    public void clear() {
        songs.clear();
    }

    private void fireListeners() {
        for (ChangeListener listener : listeners) {
            listener.stateChanged(new ChangeEvent(this));
        }
    }

    public void addListener(ChangeListener listener) {
        listeners.add(listener);
    }

    public void printPlaylist() {
        Iterator i = songs.iterator();

        while (i.hasNext()) {
            System.out.println(((Song) i.next()).toString());
        }
    }


    public int getSize() {
        return songs.size();
    }
}
