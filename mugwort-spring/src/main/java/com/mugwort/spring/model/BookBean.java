package com.mugwort.spring.model;


public class BookBean {
    private String name;
    private String lastaudit;

    private long id=0;

    public BookBean() {
    }

    public BookBean(String name, String lastaudit) {
        this.name = name;
        this.lastaudit = lastaudit;
    }

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
