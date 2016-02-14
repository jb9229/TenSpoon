package com.hoh.spoons;

import com.hoh.bowls.Bowl;
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
public class Spoon {
    @Id
    @GeneratedValue
    private Long id;

    private int riceTol;

    private int ricePercent;

    private Long accountId;

    private Bowl bowl;
}
