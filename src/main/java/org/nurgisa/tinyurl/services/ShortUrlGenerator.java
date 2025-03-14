package org.nurgisa.tinyurl.services;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

public class ShortUrlGenerator {
    public static String generate(String url) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(url.getBytes(StandardCharsets.UTF_8));

            return new BigInteger(1, hash)
                    .toString(36)
                    .substring(0, 6);
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
}
