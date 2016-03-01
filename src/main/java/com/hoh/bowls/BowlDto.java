package com.hoh.bowls;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * Created by jeong on 2016-02-24.
 */
public class BowlDto {

    @Data
    public static class Create{

        private short theme;

        @NotBlank
        @Size(min=1, max = 45)
        private String org;

        @NotBlank
        @Size(min=1, max = 45)
        private String title;

        @NotBlank
        @Size(min=1, max = 100)
        private String summary;

        @NotBlank
        @Size(min=1, max = 5000)
        private String contents;

        private String photo1;

        private String photo2;

        private String photo3;

        private String photo4;

        private String photo5;
    }

    @Data
    public static class Update{
        private short theme;

        @NotBlank
        @Size(min=1, max = 45)
        private String org;

        @NotBlank
        @Size(min=1, max = 45)
        private String title;

        @NotBlank
        @Size(min=1, max = 100)
        private String summary;

        @NotBlank
        @Size(min=1, max = 5000)
        private String contents;

        private String photo1;

        private String photo2;

        private String photo3;

        private String photo4;

        private String photo5;
    }

    @Data
    public static class Response{
        private short theme;

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
    }
}
