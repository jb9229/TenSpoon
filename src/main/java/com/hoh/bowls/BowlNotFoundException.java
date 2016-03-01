package com.hoh.bowls;

/**
 * Created by jeong on 2016-03-01.
 */
public class BowlNotFoundException extends RuntimeException {
    Long id;
    String title;
    
    public BowlNotFoundException(Long id) {
        this.id =   id;
    }

    public Long getId() {
        return id;
    }
}
