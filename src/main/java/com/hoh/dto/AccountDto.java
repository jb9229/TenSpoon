package com.hoh.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * Created by test on 2015-10-18.
 */
public class AccountDto {

    @Data
    public static class Create{
        @NotBlank
        @Size(min=5)
        private String username;
        @NotEmpty
        private String password;
    }
}
