package com.c102.ourotest.repository;

import com.c102.ourotest.entity.Member;
import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    
    Member save(Member member);
    
    Optional<Member> findById(String memberId);
    
    Optional<Member> findByEmail(String email);
    
    List<Member> findAll(int page, int size);
    
    void deleteById(String memberId);
    
    boolean existsByEmail(String email);
}

