package com.bono.playlist;

import com.bono.api.Reply;
import com.bono.api.Song;
import com.bono.events.PlaylistEvent;
import com.bono.events.PlaylistListener;

import javax.swing.*;
import java.util.*;

/**
 * Created by hendriknieuwenhuis on 29/07/15.
 *
 * moet aangepast worden om in api package te kunnen.
 *
 * changelistener implementere!
 */
public class Playlist {

    private List<PlaylistListener> listeners = new ArrayList<>();

    private DefaultListModel<Song> songList;

    public Playlist() {}

    public Playlist(String entry) {
        populate(entry);
    }
    public Song getSong(int index) {
        return songList.get(index);
    }

    public void populate(String entry) {
        songList = new DefaultListModel<>();
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
                    songList.addElement(song);
                    break;
                default:
                    System.out.println("Not a property: " + line[0]);
                    break;
            }

        }
        fireListeners();
    }

    private void fireListeners() {
        for (PlaylistListener playlistListener : listeners) {
            playlistListener.playlistChange(new PlaylistEvent(this));
        }
    }

    public DefaultListModel<Song> getPlaylistModel() {
        return songList;
    }

    public void addPlaylistListener(PlaylistListener playlistListener) {
        listeners.add(playlistListener);
    }

    public void printPlaylist() {
        Enumeration<Song> enumeration = songList.elements();

        while (enumeration.hasMoreElements()) {
            System.out.println(((Song) enumeration.nextElement()).toString());
        }
    }


    public int getSize() {
        return songList.size();
    }
}
