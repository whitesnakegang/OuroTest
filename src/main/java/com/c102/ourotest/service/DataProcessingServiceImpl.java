package com.c102.ourotest.service;

import com.c102.ourotest.member.dto.MemberResponse;
import org.springframework.stereotype.Service;

@Service
public class DataProcessingServiceImpl implements DataProcessingService {
    
    @Override
    public void processMemberId(String memberId) {
        try {
            // Depth 3: DataProcessingService
            Thread.sleep(300); // Depth 3 구분을 위한 sleep
            // ID 형식 검증
            if (!memberId.matches("^[a-zA-Z0-9]+$")) {
                throw new IllegalArgumentException("회원 ID 형식이 올바르지 않습니다.");
            }
            
            Thread.sleep(100); // 내부 메서드 호출 전 sleep
            // 추가 처리 로직
            performDeepProcessing(memberId);
            Thread.sleep(50); // 메서드 종료 전 sleep
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void validateFormat(String memberId) {
        try {
            // Depth 3: DataProcessingService
            Thread.sleep(300); // Depth 3 구분을 위한 sleep
            if (memberId.length() < 3 || memberId.length() > 50) {
                throw new IllegalArgumentException("회원 ID 길이가 유효하지 않습니다.");
            }
            Thread.sleep(50); // 메서드 종료 전 sleep
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void enrichMemberResponse(MemberResponse member) {
        try {
            // Depth 3: DataProcessingService
            Thread.sleep(300); // Depth 3 구분을 위한 sleep
            // 데이터 보강 로직
            performDataEnrichment(member);
            Thread.sleep(50); // 메서드 종료 전 sleep
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
    
    private void performDeepProcessing(String memberId) {
        try {
            // Depth 3 내부의 추가 처리
            Thread.sleep(150); // 내부 처리 sleep
            // 실제로는 더 복잡한 로직이 들어갈 수 있음
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
    
    private void performDataEnrichment(MemberResponse member) {
        try {
            // Depth 3 내부의 데이터 보강 처리
            Thread.sleep(150); // 내부 처리 sleep
            // 실제로는 더 복잡한 로직이 들어갈 수 있음
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}

