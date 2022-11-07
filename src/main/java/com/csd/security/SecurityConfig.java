package com.csd.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userSvc){
        this.userDetailsService = userSvc;
    }

    protected void configure(HttpSecurity security) throws Exception
    {
        security.httpBasic().disable();
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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .and() //  "and()"" method allows us to continue configuring the parent
                //jwt authentication
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/profiles").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/user*").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/user/removal/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/user/resetting/**").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/listingpage**").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.PUT, "/listingpage/edit/{id}").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/listingpage/newlisting**").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/listingpage/removal/*").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/listingpage/*").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/application/*").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/user/applications*").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/listingpage/*/newapplication*").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/listingpage/application/removal/*").hasAnyRole("USER", "ADMIN")
                .and()
                .authenticationProvider(authenticationProvider()) //specifies the authentication provider for HttpSecurity
                .csrf().disable() // CSRF protection is needed only for browser based attacks
                .formLogin().disable()
                .headers().disable()
                .cors(Customizer.withDefaults())
        ;
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        // Add in address for react
        configuration.setAllowedOrigins(List.of("http://127.0.0.1:5173"));
        configuration.setAllowedMethods(
                Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type",
                "X-Requested-With", "accept", "Origin",
                "Access-Control-Request-Method", "Access-Control-Request-Headers",
                "Access-Control-Allow-Origin"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}