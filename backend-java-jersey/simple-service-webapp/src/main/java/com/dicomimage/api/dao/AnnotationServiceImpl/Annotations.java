package com.dicomimage.api.dao.AnnotationServiceImpl;

import javax.persistence.*;

@Entity
public class Annotations {

    @Column
    private String username;
    @Column
    private String imagename;
    @Column
    private String aimname;
    @Column
    private String coordinateId;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    public String getAimname() {
        return aimname;
    }

    public void setAimname(String aimname) {
        this.aimname = aimname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCoordinateId() {

        return coordinateId;
    }

    public void setCoordinateId(String coordinateId) {
        this.coordinateId = coordinateId;
    }

}
