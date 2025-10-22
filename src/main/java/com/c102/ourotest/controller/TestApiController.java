package com.c102.ourotest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.c102.ourotest.dto.TestDto;
import com.c102.ourotest.dto.TestDtoResponse;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Slf4j
@RestController
public class TestApiController {
    
    @GetMapping("/api/test")
    public String testEndpoint() {
        log.debug("Test endpoint called");
        return "Test successful!";
    }

    @PostMapping("/api/test")
    public TestDtoResponse testEndpoint(@RequestBody TestDto testDto) {
        return new TestDtoResponse(testDto.getName(), testDto.getAge());
    }
}
