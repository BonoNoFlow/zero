package com.bono;

import com.bono.api.*;
import com.bono.api.protocol.MPDStatus;
import com.bono.controls.*;
import com.bono.database.DirectoryPresenter;

import com.bono.soundcloud.SoundcloudController;
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

    private DirectoryPresenter directoryPresenter;

    private SoundcloudController soundcloudController;

    private ClientExecutor clientExecutor;

    private Status status;

    private Properties properties;

    private IdleRunner idleRunner;

    public Application() {
        loadProperties();
        initModels();
        buildView();
    }

    private void loadProperties() {
        properties = ConfigLoader.loadconfig();
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

            playbackPresenter.addPlaybackView(applicationView.getPlaybackView());

            directoryPresenter = new DirectoryPresenter(clientExecutor, applicationView.getDirectoryView());
            applicationView.getDirectoryView().getDirectory().addTreeWillExpandListener(directoryPresenter);
            applicationView.getDirectoryView().getDirectory().addTreeExpansionListener(directoryPresenter);

            soundcloudController = new SoundcloudController(10, clientExecutor, applicationView.getSoundcloudView());

            applicationView.getPlaylistView().addMouseListener(playlistPresenter);
            playlistPresenter.addView(applicationView.getPlaylistView());
            playlistPresenter.initPlaylist();
            playlistPresenter.addSongListener(soundcloudController);

            applicationView.getVersionPanel().setVersion(version);
            updateStatus();
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

    @Override
    public void windowOpened(WindowEvent e) {
        super.windowOpened(e);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        super.windowClosing(e);

        closing = true;
    }

    @Override
    public void windowClosed(WindowEvent e) {
        super.windowClosed(e);

    }

    @Override
    public void windowIconified(WindowEvent e) {
        super.windowIconified(e);
        // stop idle runner while iconified.
        // it is newly initialized when the
        // window is activated again after
        // being deiconified.
        idleRunner.removeListeners();
        idleRunner = null;
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        super.windowDeiconified(e);

    }

    @Override
    public void windowActivated(WindowEvent e) {
        super.windowActivated(e);

        idleRunner = new IdleRunner(clientExecutor);
        idleRunner.addListener(playlistPresenter.getIdleListener());
        idleRunner.addListener(new IdleListener());
        idleRunner.addListener(eventObject -> updateStatus());
        idleRunner.start();
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        super.windowDeactivated(e);

        if (closing) {
            System.exit(0);
        }
    }

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
