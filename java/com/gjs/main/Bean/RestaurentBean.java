package com.gjs.opentable.Bean;

import java.util.ArrayList;

/**
 * Created by Gagan on 11/18/2016.
 */

public class RestaurentBean {

    public  String name;
    public  String place_id;
    public  String reference;
    public  Double rating;
    public  String vicinity;

    public OpeninghourBean opening_hours;
    public GeometryBean geometry;
    public ArrayList<PhotosBean> photos;



    public RestaurentBean(){
        this.name = "";
        this.place_id ="";
        this.reference = "";

       this.rating =2.5;

        this.vicinity = "";


    }


    public RestaurentBean(String name, String placeid, String icon, String category, Double rating, String opennow, String vicinity, double latitude, double longitude) {
        this.name = name;
        this.place_id = placeid;
        this.reference = icon;

        this.rating = rating;

        this.vicinity = vicinity;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceid() {
        return place_id;
    }

    public void setPlaceid(String placeid) {
        this.place_id = placeid;
    }

    public String getIcon() {
        return reference;
    }

    public void setIcon(String icon) {
        this.reference = icon;
    }


    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }


}

