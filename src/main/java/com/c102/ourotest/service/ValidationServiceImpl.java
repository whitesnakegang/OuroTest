package com.c102.ourotest.service;

import com.c102.ourotest.dto.MemberResponse;
import com.c102.ourotest.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class ValidationServiceImpl implements ValidationService {
    
    private final MemberRepository memberRepository;
    private final DataProcessingService dataProcessingService;
    
    public ValidationServiceImpl(
            MemberRepository memberRepository,
            DataProcessingService dataProcessingService) {
        this.memberRepository = memberRepository;
        this.dataProcessingService = dataProcessingService;
    }
    
    @Override
    public void validateMember(String memberId) {
        try {
            // Depth 2: ValidationService
            Thread.sleep(200); // Depth 2 구분을 위한 sleep
            if (memberId == null || memberId.isBlank()) {
                throw new IllegalArgumentException("회원 ID가 유효하지 않습니다.");
            }
            
            Thread.sleep(100); // Depth 3로 이동 전 sleep
            // Depth 3로 이동
            dataProcessingService.processMemberId(memberId);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public MemberResponse getMemberDetails(String memberId) {
        try {
            // Depth 2: ValidationService
            Thread.sleep(200); // Depth 2 구분을 위한 sleep
            dataProcessingService.validateFormat(memberId);
            
            Thread.sleep(100); // Repository 호출 전 sleep
            MemberResponse response = memberRepository.findById(memberId)
                    .map(member -> new MemberResponse(
                            member.getEmail(),
                            member.getName(),
                            member.getAge(),
                            member.getPhone()
                    ))
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
            
            Thread.sleep(50); // 메서드 종료 전 sleep
            return response;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void enrichData(MemberResponse member) {
        try {
            // Depth 2: ValidationService
            Thread.sleep(200); // Depth 2 구분을 위한 sleep
            dataProcessingService.enrichMemberResponse(member);
            Thread.sleep(50); // 메서드 종료 전 sleep
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}

