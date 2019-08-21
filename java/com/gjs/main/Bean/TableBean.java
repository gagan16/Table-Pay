package com.gjs.opentable.Bean;

/**
 * Created by Gagan on 11/25/2016.
 */

public class TableBean {
    int Sno_id,Quantity,Price;
    String Item;

    public TableBean(int sno_id, int quantity, int price, String item) {
        Sno_id = sno_id;
        Quantity = quantity;
        Price = price;
        Item = item;
    }

    public int getSno_id() {
        return Sno_id;
    }

    public void setSno_id(int sno_id) {
        Sno_id = sno_id;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getItem() {
        return Item;
    }

    public void setItem(String item) {
        Item = item;
    }
}
