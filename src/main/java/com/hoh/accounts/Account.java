package com.hoh.accounts;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Getter
@Setter
public class Account  {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique=true)
    private String email;

    private String password;
    private String username;
    private String profilePhoto;
    private boolean female;
    private boolean single;
    private int birth;
    private String residence;
    private Double authMailkey;

    @Temporal(TemporalType.TIMESTAMP)
    private Date joined;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    private boolean admin;
    private int riceTol;
    private int riceTemp;
    private int riceMonth;
    private int riceYear;


}
