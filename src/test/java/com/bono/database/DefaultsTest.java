package com.bono.database;

import javax.swing.*;
import java.util.Map;
import java.util.Set;

/**
 * Created by bono on 12/16/16.
 */
public class DefaultsTest {

    public static void main(String[] args) throws Exception {
        UIManager.getDefaults().put("ScrollBar.width", 12);
        Set<Map.Entry<Object, Object>> entries =  UIManager.getLookAndFeelDefaults().entrySet();
        for (Map.Entry entry : entries) {

            if (entry.getKey().toString().startsWith("Scroll")) {
                System.out.print(entry.getKey() + " = ");
                System.out.print(entry.getValue() + "\n");
            }
        }
    }
}
