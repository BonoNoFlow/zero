package com.bono.api;

/**
 * Created by hendriknieuwenhuis on 25/07/15.
 */
public class StatusProperties {

    // Clears the current error message in status,
    // this is also accomplished by any command
    // that starts playback.
    public static final String CLEARERROR = "clearerror";

    // Display the song info of the current song.
    public static final String CURRENTSONG = "currentsong";

    // Wait, keep connection bind, until a subsystem is changed.
    // Response is the subsystem that is changed. After response the
    // connection is closed.
    // A subsystem can also be given as argument, then only a response
    // will follow after that subsystem is changed.
    // Example:
    // idle [idleSubsystem],
    //
    // idle
    //
    public static final String IDLE = "idle";

    // Reports the current status of the player
    public static final String STATUS = "status";

    // Displays the statistics
    public static final String STATS = "stats";


    /*
     The subsystems of the idle command.
     These can be given as argument so
     'idle' only listens to one of these
     changes.
    */
    public class IdleSubsystems {

        // The song database
        public static final String DATABASE = "database";

        // A Database update
        public static final String IPDATE = "update";

        // A stored play list modification
        public static final String STORED_PLAYLIST = "stored_playlist";

        // Current playlist modification
        public static final String PLAYLIST = "playlist";

        // Player started, stopped or seeked.
        public static final String PLAYER = "player";

        // Volume change
        public static final String VOLUME = "volume";

        // Audio output enabled / disabled change
        public static final String OUTPUT = "output";

        // Options change
        public static final String OPTIONS = "options";

        // Modification in sticker database
        public static final String STICKER = "sticker";

        // client subscribed or unsubscribe changes
        public static final String SUBSCRIPTION = "subscription";

        // A message is recieved this client is
        // subscribed to.
        public static final String MESSAGE = "message";
    }

    private StatusProperties() {}

}
