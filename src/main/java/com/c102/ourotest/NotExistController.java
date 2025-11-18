package com.c102.ourotest;

import kr.co.ouroboros.core.global.annotation.ApiState;
import kr.co.ouroboros.core.global.annotation.ApiState.State;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class NotExistController {

    @GetMapping("/api/not/exist")
//    @ApiState(state = State.COMPLETED)
    public ResponseEntity<String> notExistAPI(@RequestParam String param) {
        return ResponseEntity.ok("Not Exist API");
    }
}
