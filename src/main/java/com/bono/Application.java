package com.bono;

import com.bono.api.*;
import com.bono.api.protocol.MPDStatus;
import com.bono.controls.*;
import com.bono.directory.DirectoryPresenter;

import com.bono.soundcloud.SoundcloudController;
import com.bono.view.ApplicationView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.file.NoSuchFileException;
import java.util.*;
import java.util.List;

/**
 * Created by hendriknieuwenhuis on 11/05/16.
 */
public class Application extends WindowAdapter {

    private String version;

    private Dimension dimension;

    private ApplicationView applicationView;

    private Player player;
    //private PlaylistControl playlistControl;
    private PlaylistPresenter playlistPresenter;

    private CurrentSong currentSong;
    private DirectoryPresenter directoryPresenter;

    private SoundcloudController soundcloudController;

    private ClientExecutor clientExecutor;

    private Status status;

    private Config config;

    private Properties properties;

    private IdleRunner idleRunner;

    private Object object;

    private List<String> configLoader;

    public Application() {
        //setupContact();
        loadconfig();
        initModels();
        buildView();
    }


    private void loadconfig() {
        int x = 1;
        while (true) {
             System.out.println(x++);
            try {
                configLoader = ConfigLoader.loadConfig();
            } catch (NoSuchFileException nsf) {
                ConfigLoader.showDialog("No file. Give info.");
                continue;
            } catch (IOException e) {
                e.printStackTrace();
            }
            properties = new Properties();
            for (String s : configLoader) {
                String[] param = s.split(" ");
                //properties.setProperty(param[0], param[1]);

                if (param.length > 1) properties.setProperty(param[0], param[1]);
            }
            if (properties.containsKey(ConfigLoader.HOST) && properties.containsKey(ConfigLoader.PORT)) {
                //dbExecutor = new DBExecutor(properties.getProperty(ConfigLoader.HOST),
                        //Integer.parseInt(properties.getProperty(ConfigLoader.PORT)));
                clientExecutor = new ClientExecutor(properties.getProperty(ConfigLoader.HOST),
                        properties.getProperty(ConfigLoader.PORT), 4000);
                try {
                    version = clientExecutor.testConnection();
                    System.out.println(version);
                } catch (SocketTimeoutException ste) {
                    ConfigLoader.showDialog("Time out, wrong settings.");
                    continue;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                ConfigLoader.showDialog("Please give HOST and PORT");
                continue;
            }
            break;
        }

    }

    public static Dimension frameDimension() {
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
            applicationView = new ApplicationView(Application.frameDimension(), this);

            player.addView(applicationView.getControlView());
            currentSong.addView(applicationView.getControlView());

            directoryPresenter = new DirectoryPresenter(clientExecutor, applicationView.getDirectoryView());
            applicationView.getDirectoryView().getDirectory().addTreeWillExpandListener(directoryPresenter);

            soundcloudController = new SoundcloudController(10, clientExecutor, applicationView.getSoundcloudView());

            applicationView.getControlView().addNextListener(player);
            applicationView.getControlView().addStopListener(player);
            applicationView.getControlView().addPlayListener(player);
            applicationView.getControlView().addPreviousListener(player);

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
        status = new Status();
        player = new Player(clientExecutor, status);
        playlistPresenter = new PlaylistPresenter(clientExecutor, player);
        currentSong = new CurrentSong(playlistPresenter);
        status.addListener(currentSong);
    }

    private void updateStatus() {
        try {
            status.populate(clientExecutor.execute(new DefaultCommand(MPDStatus.STATUS)));
            //status.populateStatus(dbExecutor.execute(new DefaultCommand(Status.STATUS)));
        } catch (ACKException ack) {
            ack.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    private void showConfigView() {
        try {
            ConfigOptions configOptions = new ConfigOptions(config);
        } catch (InvocationTargetException inv) {
            inv.printStackTrace();
        } catch (InterruptedException in) {
            in.printStackTrace();
        }
    }*/


    /*
    Implementation of the extended WindowsAdapter.
     */
    // Adapter stuff
    private boolean closing = false;

    @Override
    public void windowOpened(WindowEvent e) {
        super.windowOpened(e);
        //System.out.println("windowOpened");
    }

    @Override
    public void windowClosing(WindowEvent e) {
        super.windowClosing(e);
        System.out.println("windowClosing");

        closing = true;
    }

    @Override
    public void windowClosed(WindowEvent e) {
        super.windowClosed(e);
        //System.out.println("windowClosed");
    }

    @Override
    public void windowIconified(WindowEvent e) {
        super.windowIconified(e);
        //System.out.println("windowIconified");
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        super.windowDeiconified(e);
        //System.out.println("windowDeiconified");
    }

    @Override
    public void windowActivated(WindowEvent e) {
        super.windowActivated(e);
        System.out.println("windowActivated");

        idleRunner = new IdleRunner(clientExecutor);
        //idleRunner.addListener(new StatusUpdate());
        idleRunner.addListener(player);
        idleRunner.addListener(playlistPresenter.getIdleListener());
        idleRunner.addListener(new IdleListener());
        idleRunner.start();
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        super.windowDeactivated(e);
        System.out.println("windowDeactivated");

        // kan ook in iconified!!!!
        idleRunner.removeListeners();
        idleRunner = null;
        /// !!!!!!!!!!!!!!!!!!!!!!!

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
