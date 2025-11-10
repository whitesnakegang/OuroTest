package com.c102.ourotest.controller;

import com.c102.ourotest.dto.CreateMemberDTO;
import com.c102.ourotest.dto.MemberResponse;
import com.c102.ourotest.service.MemberService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import kr.co.ouroboros.core.global.annotation.ApiState;
import kr.co.ouroboros.core.global.annotation.ApiState.State;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class CompareController {

    private final MemberService memberService;

    @PostMapping
    @ApiState(state = State.COMPLETED)
    @ApiResponse(responseCode = "201")
    public ResponseEntity<String> signup(@RequestBody CreateMemberDTO createMemberDTO) {
        String memberId = memberService.createMember(createMemberDTO);
        return ResponseEntity.ok(memberId);
    }

    @GetMapping
    @ApiState(state = State.COMPLETED)
    public ResponseEntity<List<MemberResponse>> getMembers(
            @RequestParam int page,
            @RequestParam int size
    ) {
        List<MemberResponse> members = memberService.getMembers(page, size);
        return ResponseEntity.ok(members);
    }

    @GetMapping("/{memberId}")
    @ApiState(state = State.COMPLETED)
    public ResponseEntity<MemberResponse> getMember(@PathVariable String memberId) {
        MemberResponse member = memberService.getMember(memberId);
        return ResponseEntity.ok(member);
    }

    @DeleteMapping("/{memberId}")
    @ApiState(state = State.COMPLETED)
    public ResponseEntity<MemberResponse> deleteMember(@PathVariable String memberId) {
        MemberResponse deletedMember = memberService.deleteMember(memberId);
        return ResponseEntity.ok(deletedMember);
    }
}
