package com.c102.ourotest.job;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import kr.co.ouroboros.core.global.annotation.ApiState;
import kr.co.ouroboros.core.global.annotation.ApiState.State;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @PostMapping("/samsung")
    @ApiResponse(responseCode = "201")
//    @ApiState(state = State.COMPLETED)
    public ResponseEntity<EmploymentResponse> hireEmployeeToSamsung(@RequestBody Employee employee) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new EmploymentResponse("samsung", "backend"));
    }
}
