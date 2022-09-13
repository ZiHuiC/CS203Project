package com.csd.springboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ListingController {

    @GetMapping("/")
    public String index() {
        return "<html> <h1> hello </h1> </html>";
    }

    @GetMapping("/error")
    public String error() {
        return "<html> <h1> hello, error </h1> </html>";
    }

}