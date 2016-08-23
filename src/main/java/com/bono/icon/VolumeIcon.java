package com.bono.icon;

import javax.swing.*;
import javax.swing.plaf.UIResource;
import java.awt.*;
import java.io.Serializable;

/**
 * Created by bono on 8/21/16.
 */
public class VolumeIcon implements Icon, UIResource, Serializable {

    private int width;
    private int height;
    private Color uc;
    private Color pc;

    public VolumeIcon() {
        this.width = 16;
        this.height = 16;
        this.uc = Color.DARK_GRAY;
        this.pc = Color.BLACK;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        AbstractButton model = (AbstractButton)c;
        Graphics2D g2d = (Graphics2D)g;

        int pointx1 = (c.getWidth() / 2) - (this.getIconWidth() / 2);
        int pointy1 = (c.getHeight() / 2) - (int) (this.getIconHeight() * 0.250);
        int pointx2 = c.getWidth() / 2;
        int pointy2 = pointy1;
        int pointx3 = pointx2;
        int pointy3 = c.getHeight() / 2;
        int pointx4 = (c.getWidth() / 2) + (this.getIconWidth() / 2);
        int pointy4 = (c.getHeight() / 2) - (this.getIconHeight() /2);
        int pointx5 = pointx4;
        int pointy5 = (c.getHeight() / 2) + (this.getIconHeight() /2);
        int pointx6 = pointx3;
        int pointy6 = pointy3;
        int pointx7 = pointx2;
        int pointy7 = (c.getHeight() / 2) + (int) (this.getIconHeight() * 0.250);
        int pointx8 = pointx1;
        int pointy8 = pointy7;

        int[] pointsx = new int[]{pointx1, pointx2, pointx3, pointx4, pointx5, pointx6, pointx7, pointx8};
        int[] pointsy = new int[]{pointy1, pointy2, pointy3, pointy4, pointy5, pointy6, pointy7, pointy8};
        Polygon triangle = new Polygon(pointsx, pointsy, 8);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(this.getUnpressedColor());
        g2d.fillPolygon(triangle);
        if(model.getModel().isPressed()) {
            g2d.setColor(this.getPressedColor());
            g2d.fillPolygon(triangle);
        }
    }

    @Override
    public int getIconWidth() {
        return this.width;
    }

    public void setIconWidth(int width) {
        this.width = width;
    }

    @Override
    public int getIconHeight() {
        return this.height;
    }

    public void setIconHeight(int height) {
        this.height = height;
    }

    public Color getUnpressedColor() {
        return this.uc;
    }

    public void setUnpressedColor(Color newColor) {
        this.uc = newColor;
    }

    public Color getPressedColor() {
        return this.pc;
    }

    public void setPressedColor(Color newColor) {
        this.pc = newColor;
    }
}
