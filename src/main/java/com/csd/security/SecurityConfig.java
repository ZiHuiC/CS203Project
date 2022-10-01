package com.csd.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    protected void configure(HttpSecurity security) throws Exception
    {
        security.httpBasic().disable();
    }
    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userSvc){
        this.userDetailsService = userSvc;
    }

    /**
     * Exposes a bean of DaoAuthenticationProvider, a type of AuthenticationProvider
     * Attaches the user details and the password encoder
     * @return
     */

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());

        return authProvider;
    }

    /**
     * User role: can create/delete their listings, and sign up for others
     * Admin role: can create/sign up/delete listings
     * Anyone can view book/review
     *
     * Note: '*' matches zero or more characters, e.g., /books/* matches /books/20
     '**' matches zero or more 'directories' in a path, e.g., /books/** matches /books/1/reviews
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic()
//                .and() //  "and()"" method allows us to continue configuring the parent
//                .authorizeRequests()
//                .antMatchers(HttpMethod.POST, "/books").hasRole("ADMIN")
//                .antMatchers(HttpMethod.PUT, "/books/*").hasRole("ADMIN")
//                .antMatchers(HttpMethod.DELETE, "/books/*").hasRole("ADMIN")
//                // your code here
//                .antMatchers(HttpMethod.POST, "/books/*/reviews").hasAnyRole("ADMIN", "USER")
//                .antMatchers(HttpMethod.PUT, "/books/*/reviews/*").hasRole("ADMIN")
//                .antMatchers(HttpMethod.DELETE, "/books/*/reviews/*").hasRole("ADMIN")
//                .antMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
//                .antMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")
                .and()
                .authenticationProvider(authenticationProvider()) //specifies the authentication provider for HttpSecurity
                .csrf().disable() // CSRF protection is needed only for browser based attacks
                .formLogin().disable()
                .headers().disable()
        ;
        return http.build();
    }

    /**
     * @Bean annotation is used to declare a PasswordEncoder bean in the Spring application context.
     * Any calls to encoder() will then be intercepted to return the bean instance.
     */
    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}