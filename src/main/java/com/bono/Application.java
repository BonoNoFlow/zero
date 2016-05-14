package com.bono;

import com.bono.api.DBExecutor;
import com.bono.api.Status;
import com.bono.view.ApplicationView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by hendriknieuwenhuis on 11/05/16.
 */
public class Application extends WindowAdapter {

    private Dimension dimension;

    private ApplicationView applicationView;

    private DBExecutor dbExecutor;

    private Status status;

    /*
    Sets up contact with the server by, loading a config file that
    on absence portraits a config view to obtain the config values.
    */
    private void setupContact() throws Exception {

    }

    // setting the  frame dimension.
    private void initFrameDimension() {
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        double width = (graphicsDevice.getDisplayMode().getWidth() * 0.8);
        double height = (graphicsDevice.getDisplayMode().getHeight() * 0.8);
        dimension =  new Dimension((int) width, (int)height);
    }
    // build the view.
    private void buildView() {
        SwingUtilities.invokeLater(() -> {
            applicationView = new ApplicationView(dimension, this);
        });
    }


    /*
    Implementation of the extended WindowsAdapter.
     */
    @Override
    public void windowOpened(WindowEvent e) {
        super.windowOpened(e);
        Utils.Log.print(getClass().getName()+" window opened.");
    }

    @Override
    public void windowClosing(WindowEvent e) {
        super.windowClosing(e);
        Utils.Log.print(getClass().getName()+" window closing.");
        System.exit(0);
    }

    @Override
    public void windowClosed(WindowEvent e) {
        super.windowClosed(e);
        Utils.Log.print(getClass().getName()+" window closed.");
    }

    @Override
    public void windowIconified(WindowEvent e) {
        super.windowIconified(e);
        //iconified = true;
        Utils.Log.print(getClass().getName()+" window iconified.");
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        super.windowDeiconified(e);
        Utils.Log.print(getClass().getName()+" window deiconified.");
    }

    @Override
    public void windowActivated(WindowEvent e) {
        super.windowActivated(e);
        Utils.Log.print(getClass().getName()+" window activated.");
        //initIdle();
    }

    /*
    Exit the application.
     */
    @Override
    public void windowDeactivated(WindowEvent e) {
        super.windowDeactivated(e);
        Utils.Log.print(getClass().getName()+" window deactivated.");
        //if (iconified) {
        //    Utils.Log.print("iconified");
        //    return;
        //}
        //System.exit(0);
    }

    @Override
    public void windowStateChanged(WindowEvent e) {
        super.windowStateChanged(e);
        Utils.Log.print(getClass().getName()+" window state changed.");
    }

    @Override
    public void windowGainedFocus(WindowEvent e) {
        super.windowGainedFocus(e);
        Utils.Log.print(getClass().getName()+" window gained focus.");
    }

    @Override
    public void windowLostFocus(WindowEvent e) {
        super.windowLostFocus(e);
        Utils.Log.print(getClass().getName()+" window lost focus.");
    }
}