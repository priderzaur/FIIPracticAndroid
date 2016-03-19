package com.example.riordachioaia.fiips1.util;

import android.text.TextUtils;

/**
 * Created by riordachioaia on 14.03.2016.
 */
public class Utils {

    public final static boolean isValidEmail(CharSequence email) {
       /* if (TextUtils.isEmpty(email)) {
            return false;
        } else*/ {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    public final static boolean isValidNewPassword(CharSequence password){
        if (password.length()<6){
            return false;
        }
        return true;
    }

    public final static boolean isValidAge(int age){
        if (age<=12){
            return false;
        }
        return true;
    }
}
