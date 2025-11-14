package com.c102.ourotest.service;

import com.c102.ourotest.dto.MemberResponse;

public interface AnalysisService {
    
    MemberResponse analyzeMember(String memberId);
    
    MemberResponse fetchMemberData(String memberId);
    
    MemberResponse enrichMemberData(MemberResponse member);
}

