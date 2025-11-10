package com.c102.ourotest.repository;

import com.c102.ourotest.entity.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepositoryImpl implements MemberRepository {
    
    private final Map<String, Member> store = new ConcurrentHashMap<>();
    
    @Override
    public Member save(Member member) {
        if (member.getMemberId() == null) {
            member.setMemberId(UUID.randomUUID().toString());
        }
        store.put(member.getMemberId(), member);
        return member;
    }
    
    @Override
    public Optional<Member> findById(String memberId) {
        return Optional.ofNullable(store.get(memberId));
    }
    
    @Override
    public Optional<Member> findByEmail(String email) {
        return store.values().stream()
                .filter(member -> member.getEmail().equals(email))
                .findFirst();
    }
    
    @Override
    public List<Member> findAll(int page, int size) {
        return store.values().stream()
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(String memberId) {
        store.remove(memberId);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return store.values().stream()
                .anyMatch(member -> member.getEmail().equals(email));
    }
}

