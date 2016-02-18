package com.mathilde.appmessage.utils;

import android.util.Log;

/**
 * @author mathilde on 18/02/16.
 */
public class Numbers {

    public static boolean isSameNumber(String one, String two) {
        String temp1 = null;
        String temp2 = null;
        if(one.substring(0,1).equals("+")) {
            temp1 = one.substring(3, one.length());
        } else if(one.substring(0,1).equals("0")) {
            temp1 = one.substring(1, one.length());
        }

        if(two.substring(0,1).equals("+")) {
            temp2 = two.substring(3, two.length());
        } else if(two.substring(0,1).equals("0")) {
            temp2 = two.substring(1, two.length());
        }

        return temp1 != null && temp1.equals(temp2);
    }
}
