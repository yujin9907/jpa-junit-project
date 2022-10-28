package site.metacoding.white.web;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import site.metacoding.white.domain.User;
import site.metacoding.white.dto.SessionUser;
import site.metacoding.white.dto.BoardReqDto.BoardSaveReqDto;

@ActiveProfiles("test") // 테스트 설정 파일 => 메서드마다 설정파일을 다르게 해줄 수 도 있음 그럴리는 아마 없겠지만
@Sql("classpath:truncate.sql") // 슬래쉬랑 classpath 다른 점?
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) // 통합 테스트 : 모든 걸 ioc에 띄움
public class BoardApiControllerTest {

    @Autowired
    private ObjectMapper om;
    @Autowired
    private TestRestTemplate rt;
    // @Autowired
    // // private static HttpSession session; // 이거 스태틱이어도 되나? 안 됨
    // private MockMvc mockMvc;

    // private MockHttpSession session;

    // 이건 오토와이어드 지원 안 됨. 왜?
    private static HttpHeaders headers; // 임포트 두개임 스프링 걸로 (주의)

    @BeforeAll // 특정 메서드 앞에서만 실행되도록 할 수 있음. session의 경우 필요한 곳만 설정하면 됨
    public static void init() {
        // // User user = User.builder().id(1L).username("ssar").build();
        // // SessionUser sessionUserData = new SessionUser(user);
        // // session.setAttribute("sessionUser", sessionUser); // 세션 만들어줌

        // MockHttpSession session = new MockHttpSession();
        // session.setAttribute("sessionUser", sessionUserData);

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // "application/json"을 넣어주기 위함
    }

    @Test
    public void save_test() throws JsonProcessingException {
        // given : 테스트 코드 매개변수에 들어올 데이터 생성
        BoardSaveReqDto boardSaveReqDto = new BoardSaveReqDto();
        boardSaveReqDto.setTitle("스프링 테스트");
        boardSaveReqDto.setContent("테스트 내용");

        // json화
        String body = om.writeValueAsString(boardSaveReqDto);
        System.out.println("디버그 확인" + body);

        // 통합테스트 세션 값 어떻게? 검색
        // 스프링부트 통합테스트 세션 로그인 [검색]

        // when
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        // mockMvc.perform(post("/board")
        // .content(request)
        // .contentType(MediaType.APPLICATION_JSON)
        // .andExpect(status)
        // );

        ResponseEntity<String> response = rt.exchange("/board", HttpMethod.POST,
                request, String.class);

        // 세션이 잘 들어갔는지 검증
        System.out.println("상태코드" + response.getStatusCode());

        // then
    }

}
