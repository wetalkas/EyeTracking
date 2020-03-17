package com.wet.eyetracking.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by wet on 26.05.17.
 */

public class ExperimentItem extends RealmObject {
    public static final String ID = "id";
    public static final String DATE = "date";

    @PrimaryKey
    private long id;
    private String name = "";
    private long date;

    private RealmList<PointRealmObject> points = new RealmList<>();

    public ExperimentItem() {

    }

    public ExperimentItem(String name, long date, RealmList<PointRealmObject> points) {
        this.name = name;
        this.date = date;
        this.points = points;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public RealmList<PointRealmObject> getPoints() {
        return points;
    }


    public void setPoints(RealmList<PointRealmObject> points) {
        this.points = points;
    }

}
