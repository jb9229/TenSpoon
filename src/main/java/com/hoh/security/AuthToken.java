package com.hoh.security;

import com.hoh.accounts.Account;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by jeong on 2015-11-24.
 */
@Entity
@Getter
@Setter
public class AuthToken {
    private static final long serialVersionUID  =   -9001508296580395084L;
    @Id
    @GeneratedValue(strategy   =    GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(nullable = false)
    private String series;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Account user;

}
