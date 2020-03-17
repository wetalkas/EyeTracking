package com.wet.eyetracking.model;

import org.opencv.core.Point;

import io.realm.RealmObject;

/**
 * Created by wet on 26.05.17.
 */

public class PointRealmObject extends RealmObject {
    public static final String X = "x";
    public static final String Y = "y";

    public double x;
    public double y;
    public PointRealmObject another;

    public PointRealmObject() {

    }

    public PointRealmObject(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public PointRealmObject(Point point) {
        if (point == null) {
            this.x = -12345;
            this.y = -12345;
        } else {
            this.x = point.x;
            this.y = point.y;
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}
