package com.dicomimage.api.dao.CoordinateServiceImpl;

import javax.persistence.*;

@Entity
public class Coordinates {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @Column
    private Float x;

    @Column
    private Float y;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Coordinates(){
        this.x =0f;
        this.y=0f;
    }
    public Coordinates(Float x, Float y){
        this.x = x;
        this.y =y;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

}
