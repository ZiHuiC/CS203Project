//package com.csd;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//public class CsdController {
//    @GetMapping(value = "/index")
//    public String index(){
//        return "index";
//    }
//
//    // Remove this 大便
//    @Configuration
//    @EnableWebSecurity
//    public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//        @Override
//        protected void configure(HttpSecurity security) throws Exception
//        {
//            security.httpBasic().disable();
//        }
//    }
//}
