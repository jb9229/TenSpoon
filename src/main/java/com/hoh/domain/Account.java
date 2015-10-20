package com.hoh.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by test on 2015-10-18.
 */
@Entity
@Getter
@Setter
public class Account {

    @Id @GeneratedValue
    private Long id;

    private String loginId;

    private String password;
}
