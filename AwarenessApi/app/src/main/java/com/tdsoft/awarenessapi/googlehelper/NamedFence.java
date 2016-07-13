package com.tdsoft.awarenessapi.googlehelper;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.annotations.Expose;

/**
 * Created by Admin on 7/11/2016.
 */
public class NamedFence {
    @Expose
    private double latitude, longitude;
    @Expose
    private String address;
    @Expose
    private boolean enable;

    @Expose(serialize = false, deserialize = false)
    private Circle circle;

    @Expose(serialize = false, deserialize = false)
    private Marker marker;

    public NamedFence(){

    }

    public String getId() {
        return address   ;
    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }
}
