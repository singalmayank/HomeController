package com.iothome.app.homecontroller.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class Customer implements Serializable{
    long cid;
    String cemail;
    Collection<Room> rooms;

    public Customer(long cid, String cemail, Collection<Room> rooms) {
        this.cid = cid;
        this.cemail = cemail;
        this.rooms = rooms;
    }

    public Customer (){}

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public String getCemail() {
        return cemail;
    }

    public void setCemail(String cemail) {
        this.cemail = cemail;
    }

    public Collection<Room> getRooms() {
        if (rooms == null)
            rooms = new ArrayList<Room>();
        return rooms;
    }

    public void setRooms(Collection<Room> rooms) {
        this.rooms = rooms;
    }
}
