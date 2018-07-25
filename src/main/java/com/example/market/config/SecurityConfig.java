package com.example.market.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Override
    public void configure(HttpSecurity security) throws Exception {
        security.authorizeRequests().antMatchers("/register").permitAll()
                .antMatchers("/home").hasAnyAuthority("ADMIN", "USER")
                .antMatchers("/admin/**").hasAnyAuthority("ADMIN")
                .anyRequest().authenticated().and().formLogin().defaultSuccessUrl("/home")
                .loginPage("/login")
                .permitAll().and().logout().permitAll();
    }

    @Autowired
    public void securityUsers(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("SELECT username,password,enabled FROM users WHERE username = ?")
                .authoritiesByUsernameQuery("SELECT username,role FROM role WHERE username = ?");
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SessionRegistry sessionRegistry()
    {
        return new SessionRegistryImpl();
    }
}
