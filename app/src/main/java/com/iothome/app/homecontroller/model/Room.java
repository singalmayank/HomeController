package com.iothome.app.homecontroller.model;

/**
 * Created by gandhim on 10/20/2017.
 */
import java.util.Collection;
import java.util.ArrayList;

public class Room {
    private long rid;
    private String name;
    private Collection<Thing> things;

    public Room(long rid, String name, Collection<Thing> things) {
        this.rid = rid;
        this.name = name;
        this.things = things;
    }

    public Room(){}

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Thing> getThings() {
        if (things == null)
            things = new ArrayList<Thing>();
        return things;
    }

    public void setThings(Collection<Thing> things) {
        this.things = things;
    }
}
