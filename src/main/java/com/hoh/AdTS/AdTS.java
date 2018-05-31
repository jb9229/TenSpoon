package com.hoh.AdTS;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * Created by test on 2018-03-12.
 */
@Entity
@Table(name = "adts" )
@Data
public class AdTS {
    @Id
    @GeneratedValue
    private Long id;


    Integer orderNum;

    @Size(max = 100)
    String imgUrl;

    @Size(max = 100)
    String link;

    @Size(max = 80)
    String title;

    @Size(max = 150)
    String summary;

    @Size(max = 500)
    String contents;
}
