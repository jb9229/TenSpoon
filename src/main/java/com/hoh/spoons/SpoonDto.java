package com.hoh.spoons;

import lombok.Data;

/**
 * Created by test on 2016-02-13.
 */
public class SpoonDto {


    @Data
    public static class Update {
        private int riceTol;
        private int ricePercent;
    }
}
