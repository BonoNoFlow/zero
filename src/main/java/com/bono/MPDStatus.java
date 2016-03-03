package com.bono;

import com.bono.api.Status;

import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hendriknieuwenhuis on 02/03/16.
 */
public class MPDStatus extends Status {

    private List<ChangeListener> listeners = new ArrayList<>();

    public MPDStatus() {
        super();
    }

    public MPDStatus(String entry) {
        this();
        setStatus(entry);
    }

    public void setStatus(String entry) {
        String[] stats = entry.split("\n");
        for (String stat : stats) {
            String[] state = stat.split(": ");

            switch (state[0]) {
                case VOLUME:
                    setVolume(state[1]);
                    break;
                case REPEAT:
                    setRepeat(state[1]);
                    break;
                case RANDOM:
                    setRandom(state[1]);
                    break;
                case SINGLE:
                    setSingle(state[1]);
                    break;
                case CONSUME:
                    setConsume(state[1]);
                    break;
                case PLAYLIST:
                    setPlaylist(state[1]);
                    break;
                case PLAYLISTLENGTH:
                    setPlaylistlength(state[1]);
                    break;
                case MIXRAMPDB:
                    setMixrampdb(state[1]);
                    break;
                case STATE:
                    setState(state[1]);
                    break;
                case SONG:
                    setSong(state[1]);
                    break;
                case SONGID:
                    setSongid(state[1]);
                    break;
                case TIME:
                    setTime(state[1]);
                    break;
                case ELAPSED:
                    setElapsed(state[1]);
                    break;
                case BITRATE:
                    setBitrate(state[1]);
                    break;
                case AUDIO:
                    setAudio(state[1]);
                    break;
                case NEXTSONG:
                    setNextsong(state[1]);
                    break;
                case NEXTSONGID:
                    setNextsongid(state[1]);
                    break;
                default:
                    System.out.println("Not a status property: " + state[0]);
                    break;
            }

        }

    }
}
