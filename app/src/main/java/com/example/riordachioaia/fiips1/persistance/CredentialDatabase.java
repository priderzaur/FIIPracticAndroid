package com.example.riordachioaia.fiips1.persistance;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by riordachioaia on 14.03.2016.
 */
public class CredentialDatabase {
    public static HashMap<String,String> credentials;
    static {
        credentials = new HashMap<>();
        credentials.put("a@b.c","a12345");
        credentials.put("a@a.a","a");
        credentials.put("qwe@asd.zxc","x123456");
    }

    public static boolean verifyExistingCredentials(String email, String password){
        String value = credentials.get(email);
       // Log.d("CredentialDatabase", value);
        if (value!=null && value.equals(password)){
            return true;
        }
        return false;
    }
}
