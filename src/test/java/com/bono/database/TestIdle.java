package com.bono.database;

import com.bono.ConfigLoader;
import com.bono.Idle;

import java.util.Properties;

/**
 * Created by bono on 8/21/16.
 */
public class TestIdle {

    Properties properties;

    public TestIdle() {
        properties = new Properties();
        properties.setProperty(ConfigLoader.HOST, "192.168.2.4");
        properties.setProperty(ConfigLoader.PORT, "6600");

        Idle idle = new Idle(properties);
        idle.addListener(eventObject -> {
            System.out.println((String) eventObject.getSource());
        });
        idle.start();
    }

    public static void main(String[] args) {
        new TestIdle();
    }
}
