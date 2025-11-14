package com.c102.ourotest.service;

import com.c102.ourotest.member.dto.MemberResponse;

public interface ValidationService {
    
    void validateMember(String memberId);
    
    MemberResponse getMemberDetails(String memberId);
    
    void enrichData(MemberResponse member);
}

