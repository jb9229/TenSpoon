package com.hoh.bowls;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by test on 2016-01-31.
 */
public class Bowl {
    @Id
    @GeneratedValue
    private Long id;

    private int rice_tol;

    private int rice_aim;
}
