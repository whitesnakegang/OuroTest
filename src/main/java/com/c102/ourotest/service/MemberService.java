package com.c102.ourotest.service;

import com.c102.ourotest.dto.CreateMemberDTO;
import com.c102.ourotest.dto.MemberResponse;
import java.util.List;

public interface MemberService {
    
    String createMember(CreateMemberDTO createMemberDTO);
    
    List<MemberResponse> getMembers(int page, int size);
    
    MemberResponse getMember(String memberId);
    
    MemberResponse deleteMember(String memberId);
}

