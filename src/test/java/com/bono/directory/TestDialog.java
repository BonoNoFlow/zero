package com.bono.directory;

import com.bono.Application;
import com.bono.ConfigPresenter;
import com.bono.api.Config;
import com.bono.view.ConfigOptionsView;
import com.bono.view.ConnectionDialog;

/**
 * Created by hendriknieuwenhuis on 18/06/16.
 */
public class TestDialog {

    public static void main(String[] args) {
        Config config = new Config();
        ConfigPresenter configPresenter = new ConfigPresenter(config);

        ConnectionDialog connectionDialog = new ConnectionDialog(Application.screenDimension());

        connectionDialog.getConfigConnectionView().setHostField("wrong");
        connectionDialog.getConfigConnectionView().setPortField("0000");

        configPresenter.setConfigConnectionView(connectionDialog.getConfigConnectionView());
        connectionDialog.addSaveActionListener(configPresenter.getSaveActionListener());
        connectionDialog.showDialog();
    }
}
