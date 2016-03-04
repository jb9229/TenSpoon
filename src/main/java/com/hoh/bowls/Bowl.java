package com.hoh.bowls;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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

    @Enumerated(EnumType.ORDINAL)
    private BowlType theme;

    private String org;

    private String title;

    private String summary;

    private String contents;

    private String photo1;

    private String photo2;

    private String photo3;

    private String photo4;

    private String photo5;

    private int riceTol;

    private int riceAim;


    public boolean isFull(){
        return riceAim >= riceTol;
    }
}
