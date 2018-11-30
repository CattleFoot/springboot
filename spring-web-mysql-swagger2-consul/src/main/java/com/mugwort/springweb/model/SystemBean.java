package com.mugwort.springweb.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class SystemBean {
    private String name;
    private String lastaudit;
    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;


    public String getLastaudit() {
        return lastaudit;
    }

    public void setLastaudit(String lastaudit) {
        this.lastaudit = lastaudit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String toString() {
        return id + " | " + name + " | " + lastaudit;
    }

}
