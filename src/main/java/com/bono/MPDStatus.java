package com.bono;

import com.bono.api.DBExecutor;
import com.bono.api.MPDCommand;
import com.bono.api.Reply;
import com.bono.api.Status;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hendriknieuwenhuis on 02/03/16.
 */
public class MPDStatus extends Status implements ChangeListener {

    private List<ChangeListener> listeners = new ArrayList<>();

    private DBExecutor dbExecutor;
    public MPDStatus() {
        super();
    }

    public MPDStatus(DBExecutor dbExecutor) {
        super();
        this.dbExecutor = dbExecutor;
    }

    public MPDStatus(DBExecutor dbExecutor, String entry) {
        this(dbExecutor);
        setStatus(entry);
    }

    public MPDStatus(String entry) {
        super();
        setStatus(entry);
    }

    public void setStatus(String entry) {
        clear();

        Reply reply = new Reply(entry);
        Iterator<String> i = reply.iterator();
        while (i.hasNext()) {
            String[] state = i.next().split(Reply.SPLIT_LINE);

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
                    //System.out.println("Not a status property: " + state[0]);
                    break;
            }

        }
        fireListeners();
    }



    private void fireListeners() {
        Iterator i = listeners.iterator();
        while (i.hasNext()) {
            ((ChangeListener) i.next()).stateChanged(new ChangeEvent(this));
        }
    }

    public void addListener(ChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        String message = (String) e.getSource();

        if (message.equals("status")) {
            // update
            try {
                this.setStatus(dbExecutor.execute(new MPDCommand("status")));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
