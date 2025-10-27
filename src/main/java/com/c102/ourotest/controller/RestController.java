package com.c102.ourotest.controller;
import kr.co.ouroboros.core.global.annotation.ApiState;
import kr.co.ouroboros.core.global.annotation.ApiState.State;
import org.springframework.web.bind.annotation.*;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {

    @GetMapping("/hello")
    @ApiState(state = State.COMPLETED , owner = "sin", description = "park")
    public String hello() {
        return "hello";
    }

}
