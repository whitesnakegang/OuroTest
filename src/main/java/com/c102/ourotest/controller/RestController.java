package com.c102.ourotest.controller;
import kr.co.ouroboros.core.global.annotation.ApiState;
import kr.co.ouroboros.core.global.annotation.ApiState.State;
import kr.co.ouroboros.core.rest.tryit.util.TryContext;

import org.springframework.web.bind.annotation.*;

import com.c102.ourotest.service.RestService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {

    private final RestService restService;
    
    public RestController(RestService restService) {
        this.restService = restService;
    }

    @GetMapping("/hello")
    @ApiState(state = State.COMPLETED , owner = "sin", description = "park")
    public String hello() {
        return "hello";
    }

    /**
     * TryContext가 제대로 설정되었는지 확인하는 테스트 엔드포인트
     */
    @GetMapping("/test")
    public Map<String, Object> test() {
        Map<String, Object> response = new HashMap<>();
        
        boolean hasTryId = TryContext.hasTryId();
        response.put("hasTryId", hasTryId);
        
        if (hasTryId) {
            UUID tryId = TryContext.getTryId();
            response.put("message", "Try request detected: " + tryId);
            response.put("timestamp", LocalDateTime.now());
        } else {
            response.put("message", "Normal request (no Try ID)");
            response.put("timestamp", LocalDateTime.now());
        }
        
        return response;
    }

    @GetMapping("/test/{id}")
    public Map<String, Object> getUser(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();

        response.put("id", id);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        response.put("name", restService.getTestName(id));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        response.put("email", restService.getTestEmail(id));
        
        return response;
    }

}
