package org.remipassmoilesel.k8sdemo.services.signature.utils;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

    public static byte[] loadTestFileAsByteArray(String path) throws IOException {
        ClassLoader classLoader = FileUtils.class.getClassLoader();
        InputStream resource = classLoader.getResourceAsStream(path);
        return IOUtils.toByteArray(resource);
    }

    public static String loadTestFileAsString(String path) throws IOException {
        ClassLoader classLoader = FileUtils.class.getClassLoader();
        InputStream resource = classLoader.getResourceAsStream(path);
        return IOUtils.toString(resource);
    }

}
