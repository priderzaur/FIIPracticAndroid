package com.example.riordachioaia.fiips1.persistance;

import java.io.Serializable;

/**
 * Created by riordachioaia on 19.03.2016.
 */
public class Contact implements Serializable{

    public static final String contact_key = "contact";

    private String name;
    private String surname;
    private String phoneNumber;
    private String group = "None";
    private int id;

    public Contact(String name, String surname, String phoneNo, String group){
        this.name=name;
        this.surname=surname;
        this.phoneNumber=phoneNo;
        this.group=group;
    }

    public Contact(int id, String name, String surname, String phoneNo, String group){
        this.id=id;
        this.name=name;
        this.surname=surname;
        this.phoneNumber=phoneNo;
        this.group=group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Contact) {
            return Integer.valueOf(((Contact) o).id).equals(this.id);
        }

        return super.equals(o);
    }
}