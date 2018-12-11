package com.appdevloop.bessem.hikerswatch.databases;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * Created by AppDevloop on 11/12/2018.
 */
@Entity(tableName = "locs")
public class LocEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private Date date;
    private String text;
    private double lat;
    private double lng;
    private int icon;

    public LocEntity() {
    }

    public LocEntity(Date date, String text, double lat, double lng, int icon) {
        this.date = date;
        this.text = text;
        this.lat = lat;
        this.lng = lng;
        this.icon = icon;
    }

    public LocEntity(int id, Date date, String text, double lat, double lng, int icon) {
        this.id = id;
        this.date = date;
        this.text = text;
        this.lat = lat;
        this.lng = lng;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
