package com.hoh.spoons;

/**
 * Created by jeong on 2016-04-06.
 */
public class SpoonNotFoundException extends RuntimeException {
    Long id;

    public SpoonNotFoundException(Long id){
        this.id     =   id;
    }


    public Long getId() {
        return id;
    }

}