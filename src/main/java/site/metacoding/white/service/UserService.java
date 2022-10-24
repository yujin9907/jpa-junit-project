package site.metacoding.white.service;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.metacoding.white.domain.User;
import site.metacoding.white.domain.UserRepository;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional // 트랜잭션을 붙이지 않으면, 영속화돼 있는 객체가 플러시가 안 됨.
    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User login(User user) {
        User userPS = userRepository.findByUsername(user.getUsername());
        if (userPS.getPassword().equals(user.getPassword())) {
            return userPS; // 세션에 넣어줘야 됨으로 객체 리턴 필요
        } else {
            // throw new IllegalArgumentException(); // 패스워드 인자가 잘못 들어왔다는 익셉션
            throw new RuntimeException("아이디 또는 패스워드 오류");
        }
    }
}