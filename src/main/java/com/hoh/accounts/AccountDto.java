package com.hoh.accounts;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by test on 2015-10-18.
 */
public class AccountDto {


    @Data
    public static class Create{

        @NotBlank
        @Size(min=5, max=100)
        @Email
        private String email;


        @NotBlank
        @Size(min=5, max = 45)
        private String password;

        @NotBlank
        @Size(max = 45)
        private String username;


        private boolean gender;

        private boolean single;

        private boolean birth;

        private String residence;
    }

    @Data
    public static class Response{
        private Long id;
        private String username;
        private String fullName;
        private Date joined;
        private Date updated;
    }

    @Data
    public static class Update {
        private String password;
        private String fullName;
    }
}
