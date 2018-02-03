package com.hoh.bowls;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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

    private String imgPath;

    private String imgthumbnail;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startday;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endday;

    private int riceTol;

    private int riceAim;


    public boolean isFull(){
        return riceAim >= riceTol;
    }
}
