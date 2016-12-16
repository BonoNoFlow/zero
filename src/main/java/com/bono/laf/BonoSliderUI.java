package com.bono.laf;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;

/**
 * Created by bono on 12/11/16.
 */
public class BonoSliderUI extends BasicSliderUI {



    public BonoSliderUI(JSlider b) {
        super(b);

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
        g2d.setColor(BonoLafUtils.brightness(slider.getBackground(), 0.95));

        if (slider.getOrientation() == JSlider.HORIZONTAL) {
            g2d.fillRect(trackRect.x, (trackRect.y + (trackRect.height / 4)), trackRect.width, (trackRect.height / 2));
        } else {
            g2d.fillRect((trackRect.x + (trackRect.width / 4)), trackRect.y, (trackRect.width /2), trackRect.height);
        }
    }

    @Override
    public void paintThumb(Graphics g) {
        int x = (int)thumbRect.getX();
        int y = (int)thumbRect.getY();
        int width = (int)thumbRect.getWidth();
        int height = (int)thumbRect.getHeight();

        g.translate(x,  y);
        if (slider.getOrientation() == JSlider.HORIZONTAL) {
            g.setColor(BonoLafUtils.brightness(slider.getBackground(), 0.8));
            g.fillRect(0, 0, width, height);
        } else {
            g.setColor(BonoLafUtils.brightness(slider.getBackground(), 0.8));
            g.fillRect(0, 0, width, height);
        }
    }


}
