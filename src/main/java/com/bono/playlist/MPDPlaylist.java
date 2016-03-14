package com.bono.playlist;

import com.bono.api.Playlist;
import com.bono.api.Reply;
import com.bono.api.Song;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Iterator;

/**
 * Created by bono on 3/4/16.
 */
public class MPDPlaylist extends Playlist {

    private DefaultListModel<Song> songList = new DefaultListModel<>();

    public MPDPlaylist() {
        super();
    }

    @Override
    public Song getSong(int index) {
        return songList.get(index);
    }



    public void populate(String entry) {
        songList.clear();
        //clear();
        //songVector.clear();

        Song song = null;

        Reply reply = new Reply(entry);

        Iterator i = reply.iterator();

        while (i.hasNext()) {

            String[] line = ((String) i.next()).split(Reply.SPLIT_LINE);
            System.out.println(getClass() + " " + line[1]);
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
                    //songs.add(song);
                    //songVector.addElement(song);
                    break;
                default:
                    //System.out.println("Not a property: " + line[0]);
                    break;
            }

        }
        fireListeners();
    }



    public DefaultListModel<Song> getModel() {
        return songList;
    }
}
