package com.bono.icon;

import javax.swing.*;
import javax.swing.plaf.UIResource;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.Serializable;

/**
 * Created by bono on 8/22/16.
 */
public class OptionIcon implements Icon, UIResource, Serializable {

    private double middleX;
    private double middleY;

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g;

        middleX = (double) c.getWidth() / 2;
        middleY = (double) c.getHeight() / 2;
        // circle
        double xC = (double) ((c.getWidth() /2) - (getIconWidth() /2)) + 2.5;
        double yC = (double) ((c.getHeight() / 2) - (getIconHeight() / 2)) + 2.5;
        double width = (double) getIconWidth() -4;
        double height = (double) getIconHeight() - 4;

        // lines
        // value x,y of line end with length half icon width / height
        // with start point 0,0
        double radiusBeg = (double) getIconWidth() / 2.4;
        double radiusEnd = (double) getIconWidth() / 2;

        double valueX1b = pointX(radiusBeg, 45);
        double valueY1b = pointY(radiusBeg, 45);
        double valueX1e = pointX(radiusEnd, 45);
        double valueY1e = pointY(radiusEnd, 45);
        double valueX2b = pointX(radiusBeg, -45);
        double valueY2b = pointY(radiusBeg, -45);
        double valueX2e = pointX(radiusEnd, -45);
        double valueY2e = pointY(radiusEnd, -45);
        double valueX3b = pointX(radiusBeg, -135);
        double valueY3b = pointY(radiusBeg, -135);
        double valueX3e = pointX(radiusEnd, -135);
        double valueY3e = pointY(radiusEnd, -135);
        double valueX4b = pointX(radiusBeg, 135);
        double valueY4b = pointY(radiusBeg, 135);
        double valueX4e = pointX(radiusEnd, 135);
        double valueY4e = pointY(radiusEnd, 135);


        Line2D line1 = new Line2D.Double(middleX, middleY - radiusBeg, middleX, middleY - radiusEnd);
        Line2D line2 = new Line2D.Double(middleX + radiusBeg, middleY, middleX  + radiusEnd, middleY);
        Line2D line3 = new Line2D.Double(middleX, middleY + radiusBeg, middleX, middleY + radiusEnd);
        Line2D line4 = new Line2D.Double(middleX - radiusBeg, middleY, middleX  - radiusEnd, middleY);
        Line2D line5 = new Line2D.Double(valueX1b, valueY1b, valueX1e, valueY1e);
        Line2D line6 = new Line2D.Double(valueX2b, valueY2b, valueX2e, valueY2e);
        Line2D line7 = new Line2D.Double(valueX3b, valueY3b, valueX3e, valueY3e);
        Line2D line8 = new Line2D.Double(valueX4b, valueY4b, valueX4e, valueY4e);

        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        Ellipse2D ellipse2D = new Ellipse2D.Double(xC, yC, width, height);
        //g2d.drawRect(((int)middleX - (getIconWidth() / 2)), ((int)middleY - (getIconHeight() / 2)), getIconWidth(), getIconHeight());
        Stroke stroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(3.0f));
        g2d.draw(line1);
        g2d.draw(line2);
        g2d.draw(line3);
        g2d.draw(line4);
        g2d.draw(line5);
        g2d.draw(line6);
        g2d.draw(line7);
        g2d.draw(line8);
        //g2d.setStroke(stroke);
        g2d.draw(ellipse2D);
    }

    private double pointX(double radius, double angle) {
        return middleX + Math.round(radius * (Math.cos(Math.toRadians(angle))));
    }

    private double pointY(double radius, double angle) {
        return middleY - Math.round(radius * (Math.sin(Math.toRadians(angle))));
    }

    @Override
    public int getIconWidth() {
        return 16;
    }

    @Override
    public int getIconHeight() {
        return 16;
    }
}
