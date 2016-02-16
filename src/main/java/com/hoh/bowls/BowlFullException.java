package com.hoh.bowls;

/**
 * Created by jeong on 2016-02-16.
 */
public class BowlFullException extends RuntimeException {
    String title;

    public BowlFullException(String message, String title) {
        super(message);
        this.title = title;
    }

    public BowlFullException(String message) {
        super(message);
    }
}
