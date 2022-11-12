package com.csd.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private static final String[] GET_WHITELIST_URL = {
        "/user*", "/listingpage**", "/listingpage/*",
        "/application/*", "/user/applications*"
    };
    private static final String[] PUT_WHITELIST_URL = {
        "/user/resetting/**", "/listingpage/edit/{id}"
    };
    private static final String[] POST_WHITELIST_URL = {
        "/listingpage/newlisting**", "/listingpage/*/newapplication*"
    };
    private static final String[] DELETE_WHITELIST_URL = {
        "/listingpage/removal/*", "/listingpage/application/removal/*"
    };

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
                .and() 
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/profiles").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, GET_WHITELIST_URL).hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/user/removal/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, PUT_WHITELIST_URL).hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, POST_WHITELIST_URL).hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, DELETE_WHITELIST_URL).hasAnyRole("USER", "ADMIN")
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
        configuration.setAllowedOrigins(List.of("https://cs-203-frontend.vercel.app"));
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