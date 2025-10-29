package com.c102.ourotest.service;

import com.c102.ourotest.domain.User;
import com.c102.ourotest.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements CommandLineRunner {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 초기 데이터 생성
        if (userRepository.count() == 0) {
            createInitialUsers();
        }
    }

    @Transactional
    private void createInitialUsers() {
        // 초기 유저 10000명 생성
        for (int i = 0; i < 10000; i++) {
            User user = new User("홍길동" + i, "hong@example.com" + i);
            userRepository.save(user);
        }

        System.out.println("초기 사용자 데이터 생성 완료");
    }

    // 전체 사용자 조회
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ID로 사용자 조회
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Email로 사용자 조회
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // 사용자 생성
    @Transactional
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다: " + user.getEmail());
        }
        return userRepository.save(user);
    }

    // 사용자 수정
    @Transactional
    public User updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + id));

        // 이메일 변경 시 중복 체크
        if (!existingUser.getEmail().equals(updatedUser.getEmail())) {
            if (userRepository.existsByEmail(updatedUser.getEmail())) {
                throw new RuntimeException("이미 존재하는 이메일입니다: " + updatedUser.getEmail());
            }
        }

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        
        return userRepository.save(existingUser);
    }

    // 사용자 삭제
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("사용자를 찾을 수 없습니다: " + id);
        }
        userRepository.deleteById(id);
    }

    // 사용자 개수 조회
    public long getUserCount() {
        return userRepository.count();
    }
}

