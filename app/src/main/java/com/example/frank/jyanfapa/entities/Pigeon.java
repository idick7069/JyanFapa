package com.example.frank.jyanfapa.entities;

/**
 * Created by Frank on 2018/2/10.
 */

public class Pigeon {
    private int id;
    private String eye;
    private String feather;
    private String birth;
    private String name;
    private String photo;
    private String circle;
    private String source;
    private String type;
    private int rating;
    private String ps;
    private int father;
    private int mother;
    private int live;

    public Pigeon() {

    }

    public Pigeon(int id ,String eye,String feather, String birth,String name,String photo,String circle, String source,String type,int rating,String ps,int father,int mother,int live)
    {
        this.id = id;
        this.eye = eye;
        this.feather = feather;
        this.birth = birth;
        this.name = name;
        this.photo = photo;
        this.circle = circle;
        this.source = source;
        this.type = type;
        this.rating = rating;
        this.ps = ps;
        this.father = father;
        this.mother = mother;
        this.live = live;
    }

    public int getId() {
        return id;
    }

    public String getEye()
    {
        return eye;
    }

    public String getFeather() {
        return feather;
    }

    public String getBirth() {
        return birth;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public String getCircle() {
        return circle;
    }

    public String getSource() {
        return source;
    }

    public String getType() {
        return type;
    }

    public int getRating() {
        return rating;
    }

    public String getPs() {
        return ps;
    }

    public int getFather() {
        return father;
    }

    public int getMother() {
        return mother;
    }

    public int getLive() {
        return live;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setEye(String eye) {
        this.eye = eye;
    }

    public void setFeather(String feather) {
        this.feather = feather;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFather(int father) {
        this.father = father;
    }

    public void setPs(String ps) {
        this.ps = ps;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setMother(int mother) {
        this.mother = mother;
    }

    public void setLive(int live) {
        this.live = live;
    }
}

