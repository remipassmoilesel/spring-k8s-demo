package org.remipassmoilesel.k8sdemo.commons.comm.utils;

import java.util.regex.Pattern;

public class Helpers {

    private static Pattern badSubjectRegex = Pattern.compile("[^a-zA-Z0-9_\\.]");

    public static void checkSubjectString(String str) {
        if (str == null || badSubjectRegex.matcher(str).find()) {
            throw new IllegalArgumentException("Invalid string: " + str);
        }
    }

}
