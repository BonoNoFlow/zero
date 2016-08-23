package com.bono.database;

/**
 * Created by bono on 8/22/16.
 */
public class TestOptionIcon {

    public TestOptionIcon() {
        System.out.println(pointX(45.0, 8.0, 0.0));
        System.out.println(pointY(45.0, 8.0, 0.0));
    }

    private double pointX(double angle, double radius, double middleX) {
        return (middleX + (radius * Math.cos(angle)));
    }

    private double pointOppositeX(double angle, double radius, double middleX) {
        return 0.0;
    }

    private double pointY(double angle, double radius, double middleY) {
        return (middleY + (radius * Math.sin(angle)));
    }

    private double pointOppositeY(double angle, double radius, double middleY) {
        return 0.0;
    }

    public static void main(String[] args) {
        new TestOptionIcon();
    }
}
