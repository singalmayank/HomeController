package com.iothome.app.homecontroller.model;

/**
 * Created by gandhim on 10/20/2017.
 */

public class Thing {
    long tid;
    String name;
    String state;

    public Thing(long tid, String name, String state) {
        this.tid = tid;
        this.name = name;
        this.state = state;
    }

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
