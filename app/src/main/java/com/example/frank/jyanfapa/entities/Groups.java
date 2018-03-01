package com.example.frank.jyanfapa.entities;

import java.util.List;

/**
 * Created by Frank on 2018/2/10.
 */

public class Groups {
    private int groupid;
    private String groupname;
    private List<Integer> grouplist;

    public  Groups(){}

    public Groups(int groupid , String groupname,List grouplist)
    {
        this.groupid = groupid;
        this.groupname = groupname;
        this.grouplist = grouplist;
    }

    public int getGroupidd() {
        return groupid;
    }

    public String getGroupname() {
        return groupname;
    }

    public List<Integer> getGrouplist() {
        return grouplist;
    }

    public void setGroupid(int id) {
        this.groupid = id;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public void setGrouplist(List<Integer> grouplist) {
        this.grouplist = grouplist;
    }
}
