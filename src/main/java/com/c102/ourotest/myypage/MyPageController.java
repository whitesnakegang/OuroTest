package com.c102.ourotest.myypage;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import kr.co.ouroboros.core.global.annotation.ApiState;
import kr.co.ouroboros.core.global.annotation.ApiState.State;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MyPageController {

    @ApiState(state = State.COMPLETED)
    @ApiResponse(responseCode = "200")
    @GetMapping("/api/me")
    public ResponseEntity<MyPageDto> getMyInfo(@RequestParam("id") String id, @RequestParam("pw") String pw ){
        return ResponseEntity.ok(new MyPageDto("방준엽", "장덕동", "010-1234-1234"));
    }
}
