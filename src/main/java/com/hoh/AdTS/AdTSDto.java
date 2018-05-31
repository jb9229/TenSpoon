package com.hoh.AdTS;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * Created by test on 2018-03-31.
 */
public class AdTSDto {
    @Data
    public static class Create {
        @NotBlank
        int orderNum;

        @NotBlank
        @Size(min=7, max = 100)
        private String imgUrl;

        private String link;
        private String title;
        private String summary;
        private String contents;
    }
}
