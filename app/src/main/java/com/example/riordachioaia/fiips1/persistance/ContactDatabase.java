package com.example.riordachioaia.fiips1.persistance;

import java.util.ArrayList;

/**
 * Created by riordachioaia on 19.03.2016.
 */
public class ContactDatabase {



    public static ArrayList<Contact> contacts;
    static{
        contacts = new ArrayList<>();
        contacts.add(new Contact("Ior","Radu","0758845933","eu"));
        contacts.add(new Contact("A","B","0758845933","eu"));
        contacts.add(new Contact("C","D","1758845933","acasa"));
        contacts.add(new Contact("E","F","2758845933","acasa"));
        contacts.add(new Contact("G","H","3758845933","acasa"));
        contacts.add(new Contact("I","J","4758845933","acasa"));
        contacts.add(new Contact("K","L","5758845933","work"));
        contacts.add(new Contact("M","N","6758845933","work"));
        contacts.add(new Contact("O","P","7","work"));
    }
}