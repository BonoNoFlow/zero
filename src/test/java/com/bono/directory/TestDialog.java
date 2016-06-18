package com.bono.directory;

import com.bono.api.Config;
import com.bono.view.ConfigOptionsView;
import com.bono.view.ConnectionDialog;

/**
 * Created by hendriknieuwenhuis on 18/06/16.
 */
public class TestDialog {

    public static void main(String[] args) {
        Config config = new Config();
        ConfigOptionsView configOptionsView = new ConfigOptionsView(config);
        ConnectionDialog connectionDialog = new ConnectionDialog(configOptionsView);
    }
}
