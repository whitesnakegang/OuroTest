package com.c102.ourotest.controller;

import com.c102.ourotest.dto.CreateMemberDTO;
import com.c102.ourotest.dto.MemberResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.ArrayList;
import java.util.List;
import kr.co.ouroboros.core.global.annotation.ApiState;
import kr.co.ouroboros.core.global.annotation.ApiState.State;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/compare/members")
public class CompareController {

    @PostMapping
    @ApiState(state = State.COMPLETED)
    @ApiResponse(responseCode = "200")
    public ResponseEntity<String> signup(@RequestBody CreateMemberDTO createMemberDTO) {
        return ResponseEntity.ok("success");
    }

    @GetMapping
    @ApiState(state = State.COMPLETED)
    public ResponseEntity<List<MemberResponse>> getMembers(
            @RequestParam int page,
            @RequestParam int size
    ) {
        List<MemberResponse> members = new ArrayList<>();
        return ResponseEntity.ok(members);
    }

    @GetMapping("/{memberId}")
    @ApiState(state = State.COMPLETED)
    public ResponseEntity<MemberResponse> getMember(@PathVariable String memberId) {
        return  ResponseEntity.ok(new MemberResponse());
    }

    @DeleteMapping("/{memberId}")
    @ApiState(state = State.COMPLETED)
    public ResponseEntity<MemberResponse> deleteMember(@PathVariable String memberId) {
        return ResponseEntity.ok(new MemberResponse());
    }
}
