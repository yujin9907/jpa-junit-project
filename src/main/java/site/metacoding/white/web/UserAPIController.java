package site.metacoding.white.web;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.metacoding.white.domain.Board;
import site.metacoding.white.domain.BoardRepository;
import site.metacoding.white.domain.User;
import site.metacoding.white.service.BoardService;
import site.metacoding.white.service.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@RestController
public class UserAPIController {

    private final UserService userService;
    private final HttpSession session;

    @PostMapping("/join")
    public String save(@RequestBody User user) {
        userService.save(user);
        return "ok";
    }

    @PostMapping("/login") // assertcation 관련된 (로그인 관련된) 인증은 도메인명 붙이지 않음.
    public String login(@RequestBody User user) {
        User userPS = userService.login(user);
        session.setAttribute("principal", userPS);
        return "ok";
    }
}
