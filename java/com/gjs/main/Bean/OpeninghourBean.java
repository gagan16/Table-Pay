package com.gjs.opentable.Bean;

/**
 * Created by Gagan on 11/19/2016.
 */

public class OpeninghourBean {
    String open_now=" ";
    //String[] weekday_text;

    public OpeninghourBean(String open_now) {
        this.open_now = open_now;
       // this.weekday_text = weekday_text;
    }

    public String getOpen_now() {
        return open_now;
    }

    public void setOpen_now(String open_now) {
        this.open_now = open_now;
    }

  /*  public String[] getWeekday_text() {
        return weekday_text;
    }

    public void setWeekday_text(String[] weekday_text) {
        this.weekday_text = weekday_text;
    }*/
}
