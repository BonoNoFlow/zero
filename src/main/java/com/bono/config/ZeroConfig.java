package com.bono.config;

import com.bono.api.Config;

/**
 * Created by hendriknieuwenhuis on 31/05/16.
 *
 *  TODO controller / logic voor config!!!
 */
public class ZeroConfig extends Config {



    public ZeroConfig() {
        super();
    }

    public ZeroConfig(String host, int port) {
        super(host, port);
    }

    public int getSoundcloudResults() {
        return 0;
    }

    public void setSoundcloudResults(int soundcloudResults) {

    }
}
