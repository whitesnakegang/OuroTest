package com.c102.ourotest.service;

import com.c102.ourotest.dto.MemberResponse;

public interface DataProcessingService {
    
    void processMemberId(String memberId);
    
    void validateFormat(String memberId);
    
    void enrichMemberResponse(MemberResponse member);
}

