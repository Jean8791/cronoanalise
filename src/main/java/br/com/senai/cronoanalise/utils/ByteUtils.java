package br.com.senai.cronoanalise.utils;


import org.apache.tomcat.util.codec.binary.Base64;

public class ByteUtils {

    public static String byteArrayToBase64(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    public static byte[] base64ToByteArray(String base64) {
        return Base64.decodeBase64(base64);
    }

    public static String base64StringToString(String base64String) {
        byte[] bytes = java.util.Base64.getDecoder().decode(base64String);
        return new String(bytes);
    }

}
