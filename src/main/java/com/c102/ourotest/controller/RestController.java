package com.c102.ourotest.controller;
import kr.co.ouroboros.core.global.annotation.ApiState;
import kr.co.ouroboros.core.global.annotation.ApiState.State;
import kr.co.ouroboros.core.rest.tryit.util.TryContext;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {

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
            response.put("tryId", tryId.toString());
            response.put("message", "Try request detected: " + tryId);
            response.put("timestamp", LocalDateTime.now());
        } else {
            response.put("message", "Normal request (no Try ID)");
            response.put("timestamp", LocalDateTime.now());
        }
        
        return response;
    }

    @GetMapping("/users/{id}")
    public Map<String, Object> getUser(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        
        response.put("id", id);
        response.put("name", "Test User");
        response.put("email", "test@example.com");
        
        // TryContext 정보 포함
        if (TryContext.hasTryId()) {
            response.put("_tryId", TryContext.getTryId().toString());
        }
        
        return response;
    }

}
