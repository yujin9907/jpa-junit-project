package site.metacoding.white.web;

import java.net.HttpURLConnection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import site.metacoding.white.dto.UserReqDto.JoinReqDto;
import site.metacoding.white.service.UserService;

// 통합 테스트
// 테스트 전엔 회원가입의 주소 하나 테스트하려고 오만 인풋 다 넣고 버튼 눌러서 터지는지 안 터지는지.. ㅇㅈㄹ함
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserApiControllerTest {

    // JUNIT은 메서드 단위로 메모리에 띄움 => 여기서 DI 불가
    // 필요시 다음과 같이 사용

    // 단위 테스트에선 DI하기 위해서 다음과 같이 사용해야 됨. 생성자 주입이 아닌 리플렉션, IOC 컨테이너에서 끌고 옴
    // @Autowired
    // private UserService userService;

    @Autowired
    private TestRestTemplate rt;

    private static ObjectMapper om; // 잭슨을 들고 있는 매퍼(메시지컨버터도 이걸로 함)
    private static HttpHeaders headers;

    @BeforeAll // 어떤 메서드가 실행되기 전 한번만 띄운다. beforeall은 static을 붙임
    public static void init() {
        // json 형식으로 변환하기 위함
        om = new ObjectMapper();
        // 요청시 헤더를 넣어줘야 되기 때문에 필요
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 이 두가지는 ioc 컨테이너에 없음 + 테스트 메서드 실행전 한번만 띄우면 됨 => 그래서 beforeall에 이렇게 넣어줘야 됨
        // ioc에 있으면 autowire 쓰면 되지만 얘네 없음
        // 테스트 메서드 실행마다 new할 필요없이 한번만 띄우기 위해
    }

    @Test
    public void join_test() throws JsonProcessingException {
        // HttpURLConnection 대신 rt 사용-스프링에서 제공

        // given
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setUsername("papa1");
        joinReqDto.setPassword("123");

        String json = om.writeValueAsString(joinReqDto); // 이거 사용하면 위의 제이슨프로세싱익셉션 처리해줘야됨
        System.out.println(json);

        // when
        HttpEntity<String> request = new HttpEntity<>(json, headers); // 바디+헤더에 넣어줌
        // 주소, 통신방식, 바디+헤더, 응답결과 들어가야됨
        // HttpMethod.post => "post"로 해도 되지만 오타 없게 하려고 바로 적어주기
        ResponseEntity<String> response = rt.exchange("/join", HttpMethod.POST, request, String.class); // 통신시 사용하는
                                                                                                        // 라이브러리

        // then
        // 리턴 결과로 code=1임을 확인하면 됨.
        System.out.println(response.getStatusCode()); // 성공하면 200 뜸
        System.out.println(request.getBody()); // json으로 결과 반환
        // 상태코드로 검증해도 되고 우리 리턴 타입code를 사용

        // 스트링을 다시 파싱하기 위함
        DocumentContext dc = JsonPath.parse(response.getBody()); // 다시 om으로 파싱해도 되지만, 이게 더 편함
        int code = dc.read("$.code"); // $. 최상위라는 뜻
        Assertions.assertThat(code).isEqualTo(1);
    }
}
