package com.bono;

import com.bono.api.*;
import com.bono.api.protocol.MPDStatus;
import com.bono.config.MenuBarController;
import com.bono.controls.*;

import com.bono.database.MusicDatabase;
import com.bono.playlist.PlaylistPresenter;
import com.bono.view.ApplicationView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.*;

/**
 * Created by hendriknieuwenhuis on 11/05/16.
 */
public class Application extends WindowAdapter {

    private String version;

    private ApplicationView applicationView;

    private PlaybackPresenter playbackPresenter;

    private PlaylistPresenter playlistPresenter;

    private MusicDatabase musicDatabase;

    private MenuBarController menuBarController;

    private ClientExecutor clientExecutor;

    private Status status;

    private Properties properties;

    private Idle idle;

    public Application() {
        loadProperties();
        initModels();
        buildView();
    }

    /*
    Method loads the properties via the Configloader class. When
    the ClientExecutor is already initialized the host and port
    properties of the clientexecutor are set and data reloaded.
    TODO init models if clinetExecutor != null.
     */
    public void loadProperties() {
        properties = ConfigLoader.loadconfig();
        if (clientExecutor != null) {
            clientExecutor.setHost(properties.getProperty(ConfigLoader.HOST));
            clientExecutor.setPort(Integer.parseInt(properties.getProperty(ConfigLoader.PORT)));
        }
    }


    public static Dimension appDimension() {
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        double width = (graphicsDevice.getDisplayMode().getWidth() * 0.8);
        double height = (graphicsDevice.getDisplayMode().getHeight() * 0.8);
        return  new Dimension((int) width, (int)height);
    }

    public static Dimension screenDimension() {
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        double width = (graphicsDevice.getDisplayMode().getWidth());
        double height = (graphicsDevice.getDisplayMode().getHeight());
        return  new Dimension((int) width, (int)height);
    }


    // build the view.
    private void buildView() {
        SwingUtilities.invokeLater(() -> {
            applicationView = new ApplicationView(Application.appDimension(), this);

            playbackPresenter.addPlaybackView(applicationView.getPlaybackControlsView());

            musicDatabase.initDatabaseBrowserView(applicationView.getDatabaseBrowserView());
            musicDatabase.setSoundcloudView(applicationView.getSoundcloudView());

            applicationView.getCurrentPlaylistView().addMouseListener(playlistPresenter);
            playlistPresenter.addView(applicationView.getCurrentPlaylistView());

            playlistPresenter.initPlaylist();

            applicationView.getVersionPanel().setVersion(version);
            applicationView.addConfigmenuItemlistener(menuBarController.configMenuItemListener());

            applicationView.show();
        });
    }

    private void initModels() {
        clientExecutor = new ClientExecutor(properties.getProperty(ConfigLoader.HOST), properties.getProperty(ConfigLoader.PORT), 4000);
        try {
            version = clientExecutor.testConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        status = new Status();
        playbackPresenter = new PlaybackPresenter(clientExecutor, status);
        playlistPresenter = new PlaylistPresenter(clientExecutor);
        musicDatabase = new MusicDatabase(clientExecutor, status);

        menuBarController = new MenuBarController(this);
    }

    private void updateStatus() {
        try {
            status.populate(clientExecutor.execute(new DefaultCommand(MPDStatus.STATUS)));
        } catch (ACKException ack) {
            ack.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
    Implementation of the extended WindowAdapter.
     */

    // Adapter stuff
    private boolean closing = false;

    // Update status so the listeners are
    // triggered and various values are set.
    // Start the idle thread listening for
    // changes in the server.
    @Override
    public void windowOpened(WindowEvent e) {
        super.windowOpened(e);
        updateStatus();

        idle = new Idle(properties);
        idle.addListener(playlistPresenter.getIdleListener());
        // --- for testing only ---
        // idle.addListener(new IdleListener());
        // ------------------------
        idle.addListener(eventObject -> updateStatus());
        idle.start();
    }

    // set the closing boolean to true so
    // in windowDeactivated it will end
    // the application.
    @Override
    public void windowClosing(WindowEvent e) {
        super.windowClosing(e);

        closing = true;
    }


    // stop idle runner while iconified.
    // it is newly initialized when the
    // window is activated again after
    // being deiconified.
    @Override
    public void windowIconified(WindowEvent e) {
        super.windowIconified(e);

        idle.removeListeners();
        idle.close();
        idle = null;
    }

    // Same as windowOpened. Application is
    // coming back from being iconified.
    @Override
    public void windowDeiconified(WindowEvent e) {
        super.windowDeiconified(e);
        updateStatus();

        idle = new Idle(properties);
        idle.addListener(playlistPresenter.getIdleListener());
        idle.addListener(new IdleListener());
        idle.addListener(eventObject -> updateStatus());
        idle.start();
    }

    // End the application if closing boolean is true.
    @Override
    public void windowDeactivated(WindowEvent e) {
        super.windowDeactivated(e);

        if (closing) {
            System.exit(0);
        }
    }

    // For testing purpose only.
    private class IdleListener implements ChangeListener {

        @Override
        public void stateChanged(EventObject eventObject) {
            String state = (String) eventObject.getSource();
            System.out.println(state);
        }
    }

    public static void main(String[] args) {

        new Application();
    }
}
