package com.c102.ourotest.member.service;

import com.c102.ourotest.member.dto.CreateMemberDTO;
import com.c102.ourotest.member.dto.MemberResponse;
import com.c102.ourotest.member.entity.Member;
import com.c102.ourotest.member.repository.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    
    private final MemberRepository memberRepository;
    
    @Override
    public String createMember(CreateMemberDTO createMemberDTO) {
        // 이메일 중복 체크
        if (memberRepository.existsByEmail(createMemberDTO.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        
        Member member = Member.builder()
                .email(createMemberDTO.getEmail())
                .password(createMemberDTO.getPassword())
                .name(createMemberDTO.getName())
                .age(createMemberDTO.getAge())
                .phone(createMemberDTO.getPhone())
                .build();
        
        Member savedMember = memberRepository.save(member);
        return savedMember.getMemberId();
    }
    
    @Override
    public List<MemberResponse> getMembers(int page, int size) {
        List<Member> members = memberRepository.findAll(page, size);
        return members.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public MemberResponse getMember(String memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        return convertToResponse(member);
    }
    
    @Override
    public MemberResponse deleteMember(String memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        
        MemberResponse response = convertToResponse(member);
        memberRepository.deleteById(memberId);
        
        return response;
    }
    
    private MemberResponse convertToResponse(Member member) {
        return new MemberResponse(
                member.getEmail(),
                member.getName(),
                member.getAge(),
                member.getPhone(),
                "존재하지 않는 필드"
        );
    }
}

