package br.edu.ifsp.galaxyfood.server.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Cripto {

    public static String md5(String source){
        try {
            var digest = MessageDigest.getInstance("MD5");
            digest.update(source.getBytes(), 0, source.length());
            var data = new BigInteger(1, digest.digest());
            return String.format("%1$032X", data);

        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
