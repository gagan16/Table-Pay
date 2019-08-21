package com.gjs.opentable.Bean;

/**
 * Created by Gagan on 11/19/2016.
 */

public class ReviewBean {
    String author_name, time,  text;
    Double rating;

    public ReviewBean(){
        author_name="";
        text="";time="";
        rating=0.0;
    }
    public ReviewBean(String author_name, String time,String text, Double rating) {
        this.author_name = author_name;
        this.text = text;
        this.time=time;
        this.rating = rating;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
