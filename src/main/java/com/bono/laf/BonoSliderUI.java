package com.bono.laf;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;

/**
 * Created by bono on 12/11/16.
 */
public class BonoSliderUI extends BasicSliderUI {

    private JSlider slider;

    public BonoSliderUI(JSlider b) {
        super(b);
        this.slider = b;
    }


    public static ComponentUI createUI(JComponent c) {
        return new BonoSliderUI((JSlider) c);
    }

    @Override
    protected Dimension getThumbSize() {
        return new Dimension(12, 16);
    }


    @Override
    public void paintTrack(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(brightness(slider.getBackground(), 0.95));
        g2d.fillRect(trackRect.x , (trackRect.y + (trackRect.height /2) /2), trackRect.width, (trackRect.height /2));
    }

    @Override
    public void paintThumb(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int x = (int)thumbRect.getX();
        int y = (int)thumbRect.getY();
        int width = (int)thumbRect.getWidth();
        int height = (int)thumbRect.getHeight();

        g2d.setColor(brightness(slider.getBackground(), 0.8));
        g2d.fillRect(x, y, width, height);

        x = x + ((int)thumbRect.getWidth() / 4);
        width = (width / 2);

        g2d.setColor(brightness(slider.getBackground(), 0.7));
        g2d.fillRect(x, y, width, height);
    }

    private static Color brightness(Color c, double scale) {
        int r = Math.min(255, (int) (c.getRed() * scale));
        int g = Math.min(255, (int) (c.getGreen() * scale));
        int b = Math.min(255, (int) (c.getBlue() * scale));
        return new Color(r,g,b);
    }
}
