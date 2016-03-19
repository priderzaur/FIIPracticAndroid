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

    public Contact(String surname, String name, String phoneNo){
        this.name=name;
        this.surname=surname;
        this.phoneNumber=phoneNo;
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


}