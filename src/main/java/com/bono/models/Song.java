package com.bono.models;

/**
 * Created by hendriknieuwenhuis on 29/07/15.
 */
public class Song {

    /*
    TODO String vervangen met Property.
    TODO getFile return fileProperty.getValue().
    TODO setFile(String value) fileProperty.setValue(value).
    TODO getFilePropertyName() return fileProperty.getName().
     */

    private String file;
    private String lastModified;
    private String title;
    private String album;
    private String artist;
    private String genre;
    private String date;
    private String track;
    private String albumArtist;
    private String time;
    private String pos;
    private String id;
    private String Name;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getAlbumArtist() {
        return albumArtist;
    }

    public void setAlbumArtist(String albumArtist) {
        this.albumArtist = albumArtist;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public String toString() {
        return "Song{" +
                "file='" + file + '\'' +
                ", lastModified='" + lastModified + '\'' +
                ", title='" + title + '\'' +
                ", album='" + album + '\'' +
                ", artist='" + artist + '\'' +
                ", genre='" + genre + '\'' +
                ", date='" + date + '\'' +
                ", track='" + track + '\'' +
                ", albumArtist='" + albumArtist + '\'' +
                ", time='" + time + '\'' +
                ", pos='" + pos + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
