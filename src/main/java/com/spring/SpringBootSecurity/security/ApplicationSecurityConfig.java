package com.spring.SpringBootSecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static com.spring.SpringBootSecurity.security.ApplicationUserPermission.*;
import static com.spring.SpringBootSecurity.security.ApplicationUserRole.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpMethod.PUT;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                //authorize all the requests received
                .authorizeRequests()
                //we can specify some requests to be ignored from authentication
                //this is called white listing some requests
                .antMatchers("/","index","css/*","js/*")
                .permitAll()
                //next i am going to implement permission authentication
//                .antMatchers(DELETE,"/management/api/**").hasAuthority(STUDENT_WRITE.getPermission())
//                .antMatchers(POST,"/management/api/**").hasAuthority(STUDENT_WRITE.getPermission())
//                .antMatchers(PUT,"/management/api/**").hasAuthority(STUDENT_WRITE.getPermission())
//                .antMatchers(GET,"/management/api/**").hasAnyRole(ADMINTRAINEE.name(), ADMIN.name())
                //next line I will allow only students to access any path start with "api"
                .antMatchers("/api/**").hasRole(STUDENT.name())
                //any Request received
                .anyRequest()
                //must be authenticated
                .authenticated()
                //and
                .and()
                //has the authentication type to be Basic
                .httpBasic();
}
    /*
    steps to create a User:
        1- @override the method userDetailsService and make it a @Bean
            UserDetailsService is used to retrieve users from database
        2- create the required user inside as shown of type UserDetails
        3- return new InMemoryUserDetailsManager(user1,user2,......userN);
            as it is the class of interest that implement USerDetailsService interface.
            ----> if you try now run project you get Exception of "There is no PasswordEncoder mapped for the id "null" ??????????!!!!
            ----> next is how to solve this
        4- create class PasswordConfig to add password Encryptor act like @Configuration
        5- make @Bean method that return the required PasswordEncoder
        6- return the required encoder "BCryptPasswordEncoder"
        7- inject passwordEncoder in our class "ApplicationSecurityConfig" and make it @Autowired
        8- make a constructor for it.

     */

    @Override
    @Bean
    protected UserDetailsService userDetailsService(){
        UserDetails studentUser = User.builder()
                .username("student")
                .password(passwordEncoder.encode("password"))
//                .roles(STUDENT.name())  //ROLE_STUDENT

                .authorities(STUDENT.getGrantedAuthorirties())
                .build();

        UserDetails adminUser = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("password123"))
//                .roles(ADMIN.name())  //ROLE_ADMIN
                .authorities(ADMIN.getGrantedAuthorirties())
                .build();

        UserDetails adminTraineeUser = User.builder()
                .username("adminTrainee")
                .password(passwordEncoder.encode("password123"))
//                .roles(ADMINTRAINEE.name()) //ROLE_ADMINTRAINEE
                .authorities(ADMINTRAINEE.getGrantedAuthorirties())
                .build();

        return new InMemoryUserDetailsManager(
                studentUser,
                adminUser,
                adminTraineeUser
        );
    }

}
