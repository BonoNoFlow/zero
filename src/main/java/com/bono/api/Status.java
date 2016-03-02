package com.bono.api;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hendriknieuwenhuis on 01/03/16.
 *
 * Status varies in type of stats. So listeners
 * should always check whether a stat is null or not!
 */
public class Status {

    public static final String VOLUME = "volume";
    public static final String REPEAT = "repeat";
    public static final String RANDOM = "random";
    public static final String SINGLE = "single";
    public static final String CONSUME = "consume";
    public static final String PLAYLIST = "playlist";
    public static final String PLAYLISTLENGTH = "playlistlength";
    public static final String MIXRAMPDB = "mixrampdb";
    public static final String STATE = "state";
    public static final String SONG = "song";
    public static final String SONGID = "songid";
    public static final String TIME = "time";
    public static final String ELAPSED = "elapsed";
    public static final String BITRATE = "bitrate";
    public static final String AUDIO = "audio";
    public static final String NEXTSONG = "nextsong";
    public static final String NEXTSONGID = "nextsongid";

    private String volume;
    private String repeat;
    private String random;
    private String single;
    private String consume;
    private String playlist;
    private String playlistlength;
    private String mixrampdb;
    private String state;
    private String song;
    private String songid;
    private String time;
    private String elapsed;
    private String bitrate;
    private String audio;
    private String nextsong;
    private String nextsongid;

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public String getSingle() {
        return single;
    }

    public void setSingle(String single) {
        this.single = single;
    }

    public String getConsume() {
        return consume;
    }

    public void setConsume(String consume) {
        this.consume = consume;
    }

    public String getPlaylist() {
        return playlist;
    }

    public void setPlaylist(String playlist) {
        this.playlist = playlist;
    }

    public String getPlaylistlength() {
        return playlistlength;
    }

    public void setPlaylistlength(String playlistlength) {
        this.playlistlength = playlistlength;
    }

    public String getMixrampdb() {
        return mixrampdb;
    }

    public void setMixrampdb(String mixrampdb) {
        this.mixrampdb = mixrampdb;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getSongid() {
        return songid;
    }

    public void setSongid(String songid) {
        this.songid = songid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getElapsed() {
        return elapsed;
    }

    public void setElapsed(String elapsed) {
        this.elapsed = elapsed;
    }

    public String getBitrate() {
        return bitrate;
    }

    public void setBitrate(String bitrate) {
        this.bitrate = bitrate;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getNextsong() {
        return nextsong;
    }

    public void setNextsong(String nextsong) {
        this.nextsong = nextsong;
    }

    public String getNextsongid() {
        return nextsongid;
    }

    public void setNextsongid(String nextsongid) {
        this.nextsongid = nextsongid;
    }
}
