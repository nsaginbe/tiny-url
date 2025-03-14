package org.nurgisa.tinyurl.exceptions;

public class ExistingShortUrlException extends RuntimeException {
    public ExistingShortUrlException(String message) {
        super(message);
    }
}
