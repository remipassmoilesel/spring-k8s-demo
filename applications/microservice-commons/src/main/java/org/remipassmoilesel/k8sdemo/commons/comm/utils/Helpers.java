package org.remipassmoilesel.k8sdemo.commons.comm.utils;

public class Helpers {

    public static void checkSubjectString(String str) {
        if (str == null || str.matches("[^a-zA-Z0-9_]")) {
            throw new IllegalArgumentException("Invalid string: " + str);
        }
    }

}
