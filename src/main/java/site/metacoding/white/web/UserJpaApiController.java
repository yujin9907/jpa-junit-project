package site.metacoding.white.web;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.metacoding.white.domain.User;
import site.metacoding.white.domain.UserJpaRepository;
import site.metacoding.white.dto.ResponseDto;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
public class UserJpaApiController {
    // 서비스 건너뛰고 여기다 다 때려박아서 작성

    private final UserJpaRepository userJpaRepository;

    @GetMapping("/jpa/user/{id}")
    public ResponseDto<?> findById(@PathVariable Long id) {
        Optional<User> userOP = userJpaRepository.findById(id);
        userOP.orElseThrow(() -> new RuntimeException("오류"));
        return new ResponseDto<>(1, "성공", userOP);
        // userJpaRepository.findOne(null)
    }

    @PostMapping("/jpa/join")
    public ResponseDto<?> save(@RequestBody User user) {
        User userPS = userJpaRepository.save(user);
        return new ResponseDto<>(1, "성공", userPS);
    }

    @PostMapping("/jpa/login")
    public ResponseDto<?> login(@RequestBody User user) {
        User userPS = userJpaRepository.findByUsername(user.getUsername());
        if (userPS.getPassword().equals(user.getPassword())) {
            return new ResponseDto<>(1, "성공", userPS);
        } else {
            throw new RuntimeException("틀림");
        }
    }

    @GetMapping(value = "/jpa/user")
    public ResponseDto<?> findall(Integer page) { // 쿼리스트링으로밖에 못 함 => 어노테이션으로 처리 가능
        PageRequest pageRequest = PageRequest.of(page, 2); // 페이징 처리
        Page<User> userPS = userJpaRepository.findAll(pageRequest);
        return new ResponseDto<>(1, "성공", userPS);
    }

}
