package org.nurgisa.tinyurl.services;

import java.security.SecureRandom;

public class ShortUrlGenerator {
    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generate(String url) {
        StringBuilder sb = new StringBuilder(6);

        for (int i = 0; i < 3; i++) {
            sb.append(LETTERS.charAt(RANDOM.nextInt(LETTERS.length())));
        }

        for (int i = 0; i < 3; i++) {
            sb.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
        }

        return sb.toString();
    }
}
