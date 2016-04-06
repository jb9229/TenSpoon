package com.hoh.bowls;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by jeong on 2016-02-24.
 */
public class BowlDto {

    @Data
    public static class Create{

        private BowlType theme;

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

        private String imgthumbnail;

        private Date startday;

        private Date endday;
    }

    @Data
    public static class Update{
        private BowlType theme;

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

        private Date startday;

        private Date endday;
    }

    @Data
    public static class Response{
        private Long id;

        private BowlType theme;

        private String org;

        private String title;

        private String summary;

        private String contents;

        private String imgthumbnail;

        private Date startday;

        private Date endday;

        private int riceTol;

        private int riceAim;

        private boolean existSpoon;
    }
}
