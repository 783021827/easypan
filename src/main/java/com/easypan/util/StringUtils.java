package com.easypan.util;

import org.apache.commons.lang3.RandomStringUtils;

public class StringUtils {
    public static String getFileNameNoSuffix(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return fileName;
        }
        return fileName.substring(0, lastIndexOf);
    }

    public static String rename(String fileName) {
        String fileNameNoSuffix = getFileNameNoSuffix(fileName);
        return fileNameNoSuffix + "_" + RandomStringUtils.random(5, false, true) + fileName.substring(fileName.lastIndexOf("."), fileName.length());
    }
}
