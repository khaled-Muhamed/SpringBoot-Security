package com.spring.SpringBootSecurity.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                //authorize all the requests received
                .authorizeRequests()
                //we can specify some requests to be ignored from authentication
                //this is called white listing some requests
                .antMatchers("/","index","css/*","js/*")
                .permitAll()
                //any Request received
                .anyRequest()
                //must be authenticated
                .authenticated()
                //and
                .and()
                //has the authentication type to be Basic
                .httpBasic();
}

}
