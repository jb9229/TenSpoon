package com.hoh.bowls;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by test on 2016-01-31.
 */
@Entity
@Getter
@Setter
public class Bowl {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private int riceTol;

    private int riceAim;


    public boolean isFull(){
        return riceAim >= riceTol;
    }
}
