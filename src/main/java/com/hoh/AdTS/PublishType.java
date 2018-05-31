package com.hoh.AdTS;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.Serializable;

/**
 * Created by test on 2018-03-24.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PublishType implements Serializable {
    AdTS(1, "AdTS"), AdMob(2, "AdMob");


    @JsonSerialize(using = ToStringSerializer.class)
    private Integer adType;
    @JsonSerialize(using = ToStringSerializer.class)
    private String adName;


    //Constuctor
    PublishType(Integer adType, final String adName) {
        this.adType =   adType;
        this.adName =   adName;
    }


    //Method
    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public Integer getAdType() {
        return adType;
    }

    public void setAdType(Integer adType) {
        this.adType = adType;
    }
}
