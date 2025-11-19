package com.c102.ourotest.job;

import com.c102.ourotest.job.dto.SMessage;
import com.c102.ourotest.job.service.JobService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import kr.co.ouroboros.core.global.annotation.ApiState;
import kr.co.ouroboros.core.global.annotation.ApiState.State;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

//    @PostMapping("/samsung")
//    @ApiResponse(responseCode = "201")
//    @ApiState(state = State.COMPLETED)
//    public ResponseEntity<EmploymentResponse> hireEmployeeToSamsung(@RequestBody Employee employee) {
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(new EmploymentResponse("samsung", "backend"));
//    }

    @PostMapping("/ssafy")
    @ApiResponse(responseCode = "201")
    @ApiState(state = State.COMPLETED)
    public ResponseEntity<String> helloSsafy(@RequestBody SMessage message) {
        jobService.service();
        return ResponseEntity.status(HttpStatus.CREATED).body("자율 프로젝트 성공!");
    }
}
