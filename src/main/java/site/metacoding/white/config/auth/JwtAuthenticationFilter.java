package site.metacoding.white.config.auth;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.AlgorithmConstraints;
import java.util.Date;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.metacoding.white.domain.User;
import site.metacoding.white.domain.UserRepository;
import site.metacoding.white.dto.ResponseDto;
import site.metacoding.white.dto.SessionUser;
import site.metacoding.white.dto.UserReqDto.LoginReqDto;
import site.metacoding.white.util.SHA256;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter implements Filter { // javax.servlete 서블릿톰캣 필터 임포트 주의

    // 필터는 무조건 하나 실행됨으로 전역 변수로 om을 띄워주거나 그런 식으로 하지 않음
    // 세션, 동시성 처리, 읽기 전용 쓰기 전용 그런 처리는 ...?
    // 메서드가 동시에 실행될때
    // 병렬적 처리, 동기화가 돼있으면 문제가 안 됨...
    // 라이브러리가 파악이 안 된 상태에선 내부적으로 스레드를 만드는지 동기화하는지 알아 봐야 됨

    private final UserRepository userRepository; // DI 필터 컨피그로부터 주입받음

    // login 요청시
    // post
    // username, password 받기 (json)
    // db확인
    // 토큰 생성
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // 포스트가 아닐 때 직접 응답
        if (!req.getMethod().equals("POST")) {
            customResponse("post 요청 오류", resp);
            return;
        } // => 컨트룰러의 역할을 그대로 적은 것. ds 가기 전 동작함으로 이렇게 실행해야 됨.
          // 리턴을 동작시키기 위해 메서드로 따로 빼지 않음

        // body 값 받기
        ObjectMapper om = new ObjectMapper();
        LoginReqDto loginReqDto = om.readValue(req.getInputStream(), LoginReqDto.class); // om 사용 없이 그냥 json으로 하면, 버퍼리더를
                                                                                         // 통해 스트림 안 값 읽어오고, while을 통해
                                                                                         // 한줄씩 내려가며 값을 받아줘야 됨
        // log.debug("디버그 : " + loginReqDto.getUsername());

        // 유저 존재하는지 체크
        User userPS = userRepository.findByUsername(loginReqDto.getUsername());
        if (userPS == null) {
            customResponse("아이디 오류", resp);
            return;
        }

        // 비밀번호 체크
        // SHA256 sh = new SHA256();
        String encPassword = loginReqDto.getPassword();
        if (!userPS.getPassword().equals(encPassword)) {
            customResponse("비밀번호 오류", resp);
            return;
        }

        // 토큰 생성
        Date expire = new Date(System.currentTimeMillis() + (1000) * 60 * 60); // java.util, 현재 시간 // 1000=1분
        String jwtToken = JWT.create()
                .withSubject("메타코딩")
                .withExpiresAt(expire) // 만료 날짜, date 타입
                .withClaim("id", userPS.getId()) // body 값이기 때문에 여러개 넣을 수 있음
                .withClaim("username", userPS.getUsername())
                .sign(Algorithm.HMAC256("암호아무거나"));

        // 토큰 응답
        customJWTResponse("로그인 성공", jwtToken, userPS, resp);
    }

    // private void denyElesPostRequest(HttpServletRequest req, HttpServletResponse
    // resp)
    // throws IOException, JsonProcessingException {
    // } 리턴을 수행해야 되기 때문에 메서드로 빼지 않음

    private void customResponse(String msg, HttpServletResponse resp) throws IOException, JsonProcessingException {
        resp.setContentType("application/json; charset=utf-8");
        PrintWriter out = resp.getWriter(); // 직접 버퍼 라이트를 생성함
        resp.setStatus(400); // 디폴트가 200으로 응답되기 때문에 설정을 따로 해줌
        ResponseDto<?> responseDto = new ResponseDto<>(-1, msg, null);
        ObjectMapper om = new ObjectMapper();
        String body = om.writeValueAsString(responseDto);
        out.println(body);
        out.flush();
    }

    // 로그인 인증 성공 후 토큰 응답을 위한 오버로딩? 라이딩이었나?
    private void customJWTResponse(String msg, String token, User user, HttpServletResponse resp)
            throws IOException, JsonProcessingException {
        resp.setContentType("application/json; charset=utf-8");
        resp.setHeader("Authorization", "Bearer " + token); // 띄어쓰기까지 정확히 적어야 됨
        PrintWriter out = resp.getWriter(); // 직접 버퍼 라이트를 생성함
        resp.setStatus(200); // 디폴트가 200으로 응답되기 때문에 설정을 따로 해줌
        ResponseDto<?> responseDto = new ResponseDto<>(1, msg, new SessionUser(user));
        ObjectMapper om = new ObjectMapper();
        String body = om.writeValueAsString(responseDto);
        out.println(body);
        out.flush();
    }
}