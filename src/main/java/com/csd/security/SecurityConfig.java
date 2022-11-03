package com.csd.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    // @Value("${auth0.audience}")
    // private String audience;

    // @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    // private String issuer;

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
                .and() //  "and()"" method allows us to continue configuring the parent
                //jwt authentication
                .authorizeRequests()
                // .mvcMatchers("/signup").permitAll()
                // .mvcMatchers("/profiles").authenticated()
                // .and().cors()
                // .and().oauth2ResourceServer().jwt()
                
                .antMatchers(HttpMethod.GET, "/profiles").hasRole("ADMIN")
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

    // @Bean
    // JwtDecoder jwtDecoder() {
    //     /*
    //     By default, Spring Security does not validate the "aud" claim of the token, to ensure that this token is
    //     indeed intended for our app. Adding our own validator is easy to do:
    //     */

    //     NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder)
    //             JwtDecoders.fromOidcIssuerLocation(issuer);

    //     OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(audience);
    //     OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
    //     OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

    //     jwtDecoder.setJwtValidator(withAudience);

    //     return jwtDecoder;
    // }
}