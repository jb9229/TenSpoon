package com.hoh.configs;

import com.hoh.security.RestLoginFailureHandler;
import com.hoh.security.RestLoginSuccessHandler;
import com.hoh.security.RestLogoutSuccessHandler;
import com.hoh.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;

import javax.sql.DataSource;

/**
 * Created by jeong on 2015-11-05.
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final static String REMEMBER_ME_KEY    =   "tenspoon";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RestLoginFailureHandler loginFailureHandler;

    @Autowired
    private RestLoginSuccessHandler loginSuccessHandler;

    @Autowired
    private RestLogoutSuccessHandler logoutSuccessHandler;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.httpBasic();

        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/accounts/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/accounts/**").hasRole("USER")
                .antMatchers(HttpMethod.DELETE, "/accounts/**").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/auth/accounts/**").permitAll()
                .anyRequest().permitAll()
                .and()
            .formLogin()
                .loginPage("/auth/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .failureHandler(loginFailureHandler)
                .successHandler(loginSuccessHandler)
                .and()
            .logout()
                .logoutUrl("/auth/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .and()
            .rememberMe().key(REMEMBER_ME_KEY).rememberMeServices(tokenBasedRememberMeServices());
    }

    @Bean
    public PersistentTokenBasedRememberMeServices tokenBasedRememberMeServices(){
        PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices =
                new PersistentTokenBasedRememberMeServices(REMEMBER_ME_KEY, userDetailsService, jdbcTokenRepository());
                persistentTokenBasedRememberMeServices.setTokenValiditySeconds(2678400);      //60*60*24*31
        return persistentTokenBasedRememberMeServices;
    }

    @Bean
    public JdbcTokenRepositoryImpl jdbcTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository     =       new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setCreateTableOnStartup(false);
        jdbcTokenRepository.setDataSource(dataSource);

        return jdbcTokenRepository;
    }

}
