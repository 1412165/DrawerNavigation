package com.nthieuitus.findplaces.Utility;

/**
 * Created by Nguyen Trung Hieu on 06-May-17.
 */

public class StringIsEmptyOrNull {
    public static boolean check(String value) {
        if (value == null || value.isEmpty())
            return true;

        return false;
    }
}
