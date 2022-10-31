package site.metacoding.white.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.metacoding.white.config.auth.JwtAuthenticationFilter;
import site.metacoding.white.domain.UserRepository;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class FilterConfig {

    private final UserRepository userRepository; // 최초실행시 띄워서 사용할 필터에 넣어줌

    // 최초 서버 실행시 실행
    @Bean // ioc에 등록해야 됨
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtAuthenticationFilterResister() {
        log.debug("디버그 : 인증필터실행");
        FilterRegistrationBean<JwtAuthenticationFilter> bean = new FilterRegistrationBean<>(
                new JwtAuthenticationFilter(userRepository)); // 필터 등록을 위한 생성
        bean.addUrlPatterns("/login");
        return bean;
    }
}

// // /hello 요청시
// @Slf4j
// class HelloFilter implements Filter {

// @Override
// public void doFilter(ServletRequest request, ServletResponse response,
// FilterChain chain)
// throws IOException, ServletException {
// HttpServletRequest req = (HttpServletRequest) request;
// HttpServletResponse resp = (HttpServletResponse) response;

// log.debug("디버그 : 필터테스트");
// chain.doFilter(req, resp); // 다음 필터로 넘기든, ds에 전달하든 // 이거 안 하면 통신 종료됨.

// // 특정 방식 요청에 적용 가능
// if (req.getMethod().equals("POST")) {
// log.debug("디버그 : 헬로필터 실행");
// } else {
// log.debug("디버그 : 포스트 요청이 아님");
// }
// }
