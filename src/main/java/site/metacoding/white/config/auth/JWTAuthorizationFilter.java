package site.metacoding.white.config.auth;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.metacoding.white.domain.User;
import site.metacoding.white.domain.UserRepository;
import site.metacoding.white.dto.ResponseDto;
import site.metacoding.white.dto.SessionUser;

@Slf4j
public class JWTAuthorizationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // 헤더 authoirization 키값에 bearer로 적힌 값이 있는지 체크
        String jwtToken = req.getHeader("Authorization");
        log.debug("디버그 : " + jwtToken);
        if (jwtToken == null) {
            customResponse("토큰이 없음", resp);
            return;
        }

        // 토큰 검증
        jwtToken = jwtToken.replace("Bearer ", "");
        // jwtToken = jwtToken.trim(); // 앞 뒤 공백을 날림 // 혹시나 모를 미연의 실수를 방지하기 위해
        try {
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256("암호아무거나")).build().verify(jwtToken);
            Long userId = decodedJWT.getClaim("id").asLong();
            String username = decodedJWT.getClaim("username").asString();
            SessionUser sessionUser = new SessionUser(User.builder().id(userId).username(username).build());
            HttpSession session = req.getSession();
            session.setAttribute("sessionUser", sessionUser);
            log.debug("디버그 : " + userId);
        } catch (Exception e) {
            customResponse("토큰검증실패", resp);
        }

        // 디스패처 서블릿(컨트룰러)로 혹은 filter 체인 진행
        chain.doFilter(req, resp);

    }

    private void customResponse(String msg, HttpServletResponse resp) throws IOException, JsonProcessingException {
        resp.setContentType("application/json:charset=utf-8");
        PrintWriter out = resp.getWriter(); // 직접 버퍼 라이트를 생성함
        resp.setStatus(400); // 디폴트가 200으로 응답되기 때문에 설정을 따로 해줌
        ResponseDto<?> responseDto = new ResponseDto<>(-1, msg, null);
        ObjectMapper om = new ObjectMapper();
        String body = om.writeValueAsString(responseDto);
        out.println(body);
        out.flush();
    }

}
