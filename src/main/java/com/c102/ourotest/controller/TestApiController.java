package com.c102.ourotest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestApiController {
    
    @GetMapping("/api/test")
    public String testEndpoint() {
        log.debug("Test endpoint called");
        return "Test successful!";
    }
}
