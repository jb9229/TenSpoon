package com.hoh.AdTS;

/**
 * Created by test on 2018-03-23.
 */
public class AdTSNotFoundException extends RuntimeException {

    int orderNum;
    public AdTSNotFoundException(int orderNum) {
        this.orderNum   =   orderNum;
    }

    public int getOrderNum() {
        return orderNum;
    }

    @Override
    public String getMessage() {
        return "["+orderNum+"] Can not find order number";
    }
}
