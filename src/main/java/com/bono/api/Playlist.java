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

    protected List<ChangeListener> listeners = new ArrayList<>();

    protected List<Song> songs = new ArrayList<>();

    // protected Vector<Song> songVector = new Vector();

    public Playlist() {}

    //public Playlist(String entry) {
     //   populate(entry);
    //}

    public Song getSong(int index) {
        return songs.get(index);
    }

    //public Song getVectorSong(int index) {
    //    return songVector.get(index);
    //}



    //public Vector getSongVector() {
    //    return songVector;
    //}

    public Iterator iterator() {
        return songs.iterator();
    }

    public void clear() {
        songs.clear();
    }

    protected void fireListeners() {
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
