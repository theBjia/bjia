package com.sunny.util;

import java.security.MessageDigest;


public class MD5Util {

    public static String getStringMD5(String str) {

        char[] charArray = str.toCharArray();

        return getCharArrayMD5(charArray);
    }


    public static String getCharArrayMD5(char[] charArray) {
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }

        return getByteArrayMD5(byteArray);
    }


    public static String getByteArrayMD5(byte[] byteArray) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteArray);
            byte[] md5Bytes = md5.digest();
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = (md5Bytes[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
