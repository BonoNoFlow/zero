package com.bono.database;

import javax.swing.*;
import java.awt.*;

/**
 * Created by bono on 8/17/16.
 */
public class DropTest {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(300, 300));
        DropDownComponent dr = new DropDownComponent(new JSlider(JSlider.VERTICAL), null);
        panel.add(dr);
        frame.getContentPane().add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
