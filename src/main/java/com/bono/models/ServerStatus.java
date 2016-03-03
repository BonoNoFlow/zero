package com.bono.models;

import java.util.List;
import com.bono.models.Status;

/**
 * Created by hendriknieuwenhuis on 28/07/15.
 *
 * Controller / Logic class to rule the server status!
 *
 * Maybe create a wrapper runnable class 1!!!!!1
 */
@Deprecated
public class ServerStatus {

    private static final String VOLUME = "volume:";
    private static final String REPEAT = "repeat:";
    private static final String RANDOM = "random:";
    private static final String SINGLE = "single:";
    private static final String CONSUME = "consume:";
    private static final String PLAYLIST = "playlist:";
    private static final String PLAYLISTLENGTH = "playlistlength:";
    private static final String STATE = "state:";
    private static final String SONG = "song:";
    private static final String SONGID = "songid:";
    private static final String NEXTSONG = "nextsong:";
    private static final String NEXTSONGID = "nextsongid:";
    private static final String TIME = "time:";
    private static final String ELAPSED = "elapsed:";
    private static final String DURATION = "duration:";
    private static final String BITRATE = "bitrate:";
    private static final String XFADE = "xfade:";
    private static final String MIXRAMPDB = "mixrampdb:";
    private static final String MIXRAMPDELAY = "mixrampdelay:";
    private static final String AUDIO = "audio:";
    private static final String UPDATING = "updating:";
    private static final String ERROR = "error:";

    private Object lock = new Object();

    private Status status;

    /*
    Creates a wrapper 'ServerStatus' for a 'Status'
    object. The wrapper contains a 'setStatus' method
    that writes the variables of the 'Status' object.
    Also there is a 'getStatus' method to return
    the 'Status' object.
    Both methods are synchronized to 'Object' lock.
    */
    public ServerStatus() {
        status = new Status();
    }

    /*
    public ServerStatus(List<String> status) {
        this();
        setStatus(status);
    }*/



    /*
        Set the status directly from queried list.
         */
    public void setStatus(String entry) {
        //synchronized (lock) {
        String[] elements = entry.split("\n");
            for (String line : elements) {
                //System.out.println(line);
                if (line.startsWith(VOLUME)) {
                    status.setVolume(line.substring((VOLUME.length() + 1)));
                } else if (line.startsWith(REPEAT)) {
                    status.setRepeat(line.substring((REPEAT.length() + 1)));
                } else if (line.startsWith(RANDOM)) {
                    status.setRandom(line.substring((RANDOM.length() + 1)));
                } else if (line.startsWith(SINGLE)) {
                    status.setSingle(line.substring((SINGLE.length() + 1)));
                } else if (line.startsWith(CONSUME)) {
                    status.setConsume(line.substring((CONSUME.length() + 1)));
                } else if (line.startsWith(PLAYLIST)) {
                    status.setPlaylist(line.substring((PLAYLIST.length() + 1)));
                } else if (line.startsWith(PLAYLISTLENGTH)) {
                    status.setPlaylistlength(line.substring((PLAYLISTLENGTH.length() + 1)));
                } else if (line.startsWith(STATE)) {
                    status.setState(line.substring((STATE.length() + 1)));
                } else if (line.startsWith(SONG)) {
                    status.setSong(line.substring((SONG.length() + 1)));
                } else if (line.startsWith(SONGID)) {
                    status.setSongid(line.substring((SONGID.length() + 1)));
                } else if (line.startsWith(NEXTSONG)) {
                    status.setNextsong(line.substring((NEXTSONG.length() + 1)));
                } else if (line.startsWith(NEXTSONGID)) {
                    status.setNextsongid(line.substring((NEXTSONGID.length() + 1)));
                } else if (line.startsWith(TIME)) {
                    status.setTime(line.substring((TIME.length() + 1)));
                } else if (line.startsWith(ELAPSED)) {
                    status.setElapsed(line.substring((ELAPSED.length() + 1)));
                } else if (line.startsWith(DURATION)) {
                    status.setDuration(line.substring((DURATION.length() + 1)));
                } else if (line.startsWith(BITRATE)) {
                    status.setBitrate(line.substring((BITRATE.length() + 1)));
                } else if (line.startsWith(XFADE)) {
                    status.setXfade(line.substring((XFADE.length() + 1)));
                } else if (line.startsWith(MIXRAMPDB)) {
                    status.setMixrampdb(line.substring((MIXRAMPDB.length() + 1)));
                } else if (line.startsWith(MIXRAMPDELAY)) {
                    status.setMixrampdelay(line.substring((MIXRAMPDELAY.length() + 1)));
                } else if (line.startsWith(AUDIO)) {
                    status.setAudio(line.substring((AUDIO.length() + 1)));
                } else if (line.startsWith(UPDATING)) {
                    status.setUpdating(line.substring((UPDATING.length() + 1)));
                } else if (line.startsWith(ERROR)) {
                    status.setError(line.substring((ERROR.length() + 1)));
                } else {
                    // if a line is not a variable.
                    System.out.println(line);
                }
            }
        //}
        //System.out.printf("%s: %s\n", getClass().getName(), "update");

    }

    public Status getStatus() {
        //synchronized (lock) {
            return  status;
        //}
    }


}
