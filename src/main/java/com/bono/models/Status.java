package com.bono.models;


/**
 * Created by hendriknieuwenhuis on 30/07/15.
 */
@Deprecated
public class Status {

    //volume: 0-100
    private Property<String> volume = new Property<>("volume");

    //repeat: 0 or 1
    private Property<String> repeat = new Property<>("repeat");

    //random: 0 or 1
    private Property<String> random = new Property<>("random");

    //single: [2] 0 or 1
    private Property<String> single = new Property<>("single");

    //consume: [2] 0 or 1
    private Property<String> consume = new Property<>("consume");

    //playlist: 31-bit unsigned integer, the playlist version number
    private Property<String> playlist = new Property<>("playlist");

    //playlistlength: integer, the length of the playlist
    private Property<String> playlistlength = new Property<>("playlistlength");

    //state: play, stop, or pause
    private Property<String> state = new Property<>("state");

    //song: playlist song number of the current song stopped on or playing
    private Property<String> song = new Property<>("song");

    //songid: playlist songid of the current song stopped on or playing
    private Property<String> songid = new Property<>("songid");

    //nextsong: [2] playlist song number of the next song to be played
    private Property<String> nextsong = new Property<>("nextsong");

    //nextsongid: [2] playlist songid of the next song to be played
    private Property<String> nextsongid = new Property<>("nextsongid");

    //time: total time elapsed (of current playing/paused song)
    private Property<String> time = new Property<>("time");

    //elapsed: [3] Total time elapsed within the current song, but with higher resolution.
    private Property<String> elapsed = new Property<>("elapsed");

    //duration: [4] Duration of the current song in seconds.
    private Property<String> duration = new Property<>("duration");

    //bitrate: instantaneous bitrate in kbps
    private Property<String> bitrate = new Property<>("bitrate");

    //xfade: crossfade in seconds
    private Property<String> xfade = new Property<>("xfade");

    //mixrampdb: mixramp threshold in dB
    private Property<String> mixrampdb = new Property<>("mixrampdb");

    //mixrampdelay: mixrampdelay in seconds
    private Property<String> mixrampdelay = new Property<>("mixrampdelay");

    //audio: sampleRate:bits:channels
    private Property<String> audio = new Property<>("audio");

    //updating_db: job id
    private Property<String> updating = new Property<>("updating");

    //error: if there is an error, returns message here
    private Property<String> error = new Property<>("error");


    public Property<String> getErrorProperty() {
        return error;
    }

    public String getError() {
        return error.getValue();
    }

    public void setError(String value) {
        error.setValue(value);
    }


    public Property<String> getVolumeProperty() {
        return volume;
    }

    public String getVolume() {
        return volume.getValue();
    }

    public void setVolume(String value) {
        volume.setValue(value);
    }


    public Property<String> getRepeatProperty() {
        return repeat;
    }

    public String getRepeat() {
        return repeat.getValue();
    }

    public void setRepeat(String value) {
        repeat.setValue(value);
    }


    public Property<String> getRandomProperty() {
        return random;
    }

    public String getRandom() {
        return random.getValue();
    }

    public void setRandom(String value) {
        random.setValue(value);
    }


    public Property<String> getSingleProperty() {
        return single;
    }

    public String getSingle() {
        return single.getValue();
    }



    public void setSingle(String value) {
        single.setValue(value);
    }


    public Property<String> getConsumeProperty() {
        return consume;
    }

    public String getConsume() {
        return consume.getValue();
    }

    public void setConsume(String value) {
        consume.setValue(value);
    }


    public Property<String> getPlaylistProperty() {
        return playlist;
    }

    public String getPlaylist() {
        return playlist.getValue();
    }

    public void setPlaylist(String value) {
        playlist.setValue(value);
    }

    public Property<String> getPlaylistlengthProperty() {
        return playlistlength;
    }

    public String getPlaylistlength() {
        return playlistlength.getValue();
    }

    public void setPlaylistlength(String value) {
        playlistlength.setValue(value);
    }

    public Property<String> getStateProperty() {
        return state;
    }

    public String getState() {
        return state.getValue();
    }

    public void setState(String value) {
        state.setValue(value);
    }

    public Property<String> getSongProperty() {
        return song;
    }

    public String getSong() {
        return song.getValue();
    }

    public void setSong(String value) {
        song.setValue(value);
    }

    public Property<String> getSongidProperty() {
        return songid;
    }

    public String getSongid() {
        return songid.getValue();
    }

    public void setSongid(String value) {
        songid.setValue(value);
    }

    public Property<String> getNextsongProperty() {
        return nextsong;
    }

    public String getNextsong() {
        return nextsong.getValue();
    }

    public void setNextsong(String value) {
        nextsong.setValue(value);
    }

    public Property<String> getNextsongidProperty() {
        return nextsongid;
    }

    public String getNextsongid() {
        return nextsongid.getValue();
    }

    public void setNextsongid(String value) {
        nextsongid.setValue(value);
    }

    public Property<String> getTimeProperty() {
        return time;
    }

    public String getTime() {
        return time.getValue();
    }

    public void setTime(String value) {
        time.setValue(value);
    }

    public Property<String> getElapsedProperty() {
        return elapsed;
    }

    public String getElapsed() {
        return elapsed.getValue();
    }

    public void setElapsed(String value) {
        elapsed.setValue(value);
    }

    public Property<String> getDurationProperty() {
        return duration;
    }

    public String getDuration() {
        return duration.getValue();
    }

    public void setDuration(String value) {
        duration.setValue(value);
    }

    public Property<String> getBitrateProperty() {
        return bitrate;
    }

    public String getBitrate() {
        return bitrate.getValue();
    }

    public void setBitrate(String value) {
        bitrate.setValue(value);
    }

    public Property<String> getXfadeProperty() {
        return xfade;
    }

    public String getXfade() {
        return xfade.getValue();
    }

    public void setXfade(String value) {
        xfade.setValue(value);
    }

    public Property<String> getMixrampdbProperty() {
        return mixrampdb;
    }

    public String getMixrampdb() {
        return mixrampdb.getValue();
    }

    public void setMixrampdb(String value) {
        mixrampdb.setValue(value);
    }

    public Property<String> getMixrampdelayProperty() {
        return mixrampdelay;
    }

    public String getMixrampdelay() {
        return mixrampdelay.getValue();
    }

    public void setMixrampdelay(String value) {
        mixrampdelay.setValue(value);
    }

    public Property<String> getAudioProperty() {
        return audio;
    }

    public String getAudio() {
        return audio.getValue();
    }

    public void setAudio(String value) {
        audio.setValue(value);
    }

    public Property<String> getUpdatingProperty() {
        return updating;
    }

    public String getUpdating() {
        return updating.getValue();
    }

    public void setUpdating(String value) {
        updating.setValue(value);
    }

    @Override
    public String toString() {
        return "Status{" +
                "volume=" + volume.toString() +
                ", repeat=" + repeat.toString() +
                ", random=" + random.toString() +
                ", single=" + single.toString() +
                ", consume=" + consume.toString() +
                ", playlist=" + playlist.toString() +
                ", playlistlength=" + playlistlength.toString() +
                ", state=" + state.toString() +
                ", song=" + song.toString() +
                ", songid=" + songid.toString() +
                ", nextsong=" + nextsong.toString() +
                ", nextsongid=" + nextsongid.toString() +
                ", time=" + time.toString() +
                ", elapsed=" + elapsed.toString() +
                ", duration=" + duration.toString() +
                ", bitrate=" + bitrate.toString() +
                ", xfade=" + xfade.toString() +
                ", mixrampdb=" + mixrampdb.toString() +
                ", mixrampdelay=" + mixrampdelay.toString() +
                ", audio=" + audio.toString() +
                ", updating=" + updating.toString() +
                ", error=" + error.toString() +
                '}';
    }
}
