package com.c102.ourotest.service;

import com.c102.ourotest.member.dto.MemberResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class AnalysisServiceImpl implements AnalysisService {
    
    private final ValidationService validationService;
    private final AnalysisService self;
    
    public AnalysisServiceImpl(
            ValidationService validationService,
            @Lazy AnalysisService self) {
        this.validationService = validationService;
        this.self = self;
    }
    
    @Override
    public MemberResponse analyzeMember(String memberId) {
        try {
            // Depth 1: AnalysisService 시작
            Thread.sleep(100); // Depth 1 구분을 위한 sleep
            validationService.validateMember(memberId);
            
            Thread.sleep(100); // Self-invocation 전 sleep
            // Self-invocation: 자기 자신을 호출하여 AOP 추적 가능하도록
            MemberResponse member = self.fetchMemberData(memberId);
            
            Thread.sleep(100); // 두 번째 Self-invocation 전 sleep
            // 추가 분석 로직
            return self.enrichMemberData(member);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
    
    public MemberResponse fetchMemberData(String memberId) {
        try {
            // Self-invocation을 위한 메서드
            Thread.sleep(150); // Self-invocation 메서드 내부 sleep
            return validationService.getMemberDetails(memberId);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
    
    public MemberResponse enrichMemberData(MemberResponse member) {
        try {
            // Self-invocation을 위한 메서드
            Thread.sleep(150); // Self-invocation 메서드 내부 sleep
            validationService.enrichData(member);
            Thread.sleep(50); // 메서드 종료 전 sleep
            return member;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}

