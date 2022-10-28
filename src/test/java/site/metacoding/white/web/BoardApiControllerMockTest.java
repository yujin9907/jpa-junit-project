package site.metacoding.white.web;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import site.metacoding.white.domain.Board;
import site.metacoding.white.domain.BoardRepository;
import site.metacoding.white.domain.Comment;
import site.metacoding.white.domain.CommentRepository;
import site.metacoding.white.domain.User;
import site.metacoding.white.domain.UserRepository;
import site.metacoding.white.dto.BoardReqDto;
import site.metacoding.white.dto.SessionUser;
import site.metacoding.white.dto.BoardReqDto.BoardSaveReqDto;
import site.metacoding.white.dto.BoardReqDto.BoardUpdateReqDto;
import site.metacoding.white.dto.UserReqDto.JoinReqDto;
import site.metacoding.white.service.BoardService;
import site.metacoding.white.service.UserService;
import site.metacoding.white.util.SHA256;

// @ActiveProfiles("test") 
// @Sql("classpath:truncate.sql")
// @AutoConfigureMockMvc // MOCKMVC를 IOC 에 등록
// @SpringBootTest(webEnvironment = WebEnvironment.MOCK) // 단위 테스트 - mock

@ActiveProfiles("test") // 설정 파일
@Sql("classpath:truncate.sql") // 메서드 실행직전 초기화
@Transactional // 트랜잭션 안붙이면 영속성 컨텍스트에서 DB로 flush 안됨 (Hibernate 사용시) / mybatis 는 넣어도 되긴 하는데, 필요
               // 없음
@AutoConfigureMockMvc // MockMvc Ioc 컨테이너에 등록
@SpringBootTest(webEnvironment = WebEnvironment.MOCK) // mock 환경으로 실행
public class BoardApiControllerMockTest {

    private static final String CONENT__TYPE = "application/json; charset=utf-8";

    @Autowired // mock ioc에 넣는 것
    private MockMvc mvc;
    @Autowired
    private ObjectMapper om;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private SHA256 sha256;

    private MockHttpSession session;
    private static HttpHeaders headers; // 임포트 두개임 스프링 걸로 (주의)

    // @BeforeAll // 특정 메서드 앞에서만 실행되도록 할 수 있음. session의 경우 필요한 곳만 설정하면 됨
    // public static void init() {
    // headers = new HttpHeaders();
    // headers.setContentType(MediaType.APPLICATION_JSON); // "application/json"을
    // 넣어주기 위함
    // }

    @BeforeEach
    public void sessionInit() {
        session = new MockHttpSession();
        User user = User.builder().id(1L).username("ssar").build();
        session.setAttribute("sessionUser", new SessionUser(user));
    }

    @BeforeEach
    public void dataInit() {
        String encPassword = sha256.encrypt("1234");
        User user = User.builder().username("ssar").password(encPassword).build();
        User userPS = userRepository.save(user);

        Board board = Board.builder()
                .title("스프링1강")
                .content("트랜잭션관리")
                .user(userPS)
                .build();
        Board boardPS = boardRepository.save(board);

        Comment comment = Comment.builder()
                .content("내용좋아요")
                .board(boardPS)
                .user(userPS)
                .build();
        Comment commentPS = commentRepository.save(comment);
        // commentRepository.save(comment2); // 하나 더 넣어줄 거면, this에 들어감으로 커멘트 객체를 다른 걸
        // 넣어줘야 됨. 이게 무슨 소리?

        // boardPS.addComment(commentPS);
        // 이건 실수할 수 있는 로직이기 때문에 comment 생성자에 보드를 넣어주도록 하면 좋음
    }

    // @Test
    // public void findByid_test() throws Exception {
    // // data init
    // JoinReqDto joinReqDto = new JoinReqDto();
    // joinReqDto.setUsername("very");
    // joinReqDto.setPassword("1234");
    // userService.save(joinReqDto);

    // BoardSaveReqDto boardSaveReqDto = new BoardSaveReqDto();
    // boardSaveReqDto.setTitle("테스트");
    // boardSaveReqDto.setContent("트랜잭션관리");
    // User user = User.builder().id(1L).username("very").build();
    // boardSaveReqDto.setSessionUser(new SessionUser(user));
    // boardService.save(boardSaveReqDto);

    // // given
    // Long id = 1L;

    // // when
    // // mvc.perform(MockMvcRequestBuilders.get("/board/"+id));
    // ResultActions resultActions = mvc.perform(get("/board/" +
    // id).accept(MediaType.APPLICATION_JSON_VALUE));

    // // then
    // // resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    // resultActions.andExpect(status().isOk()); // 200을 기대한다

    // }

    @Test
    public void findById_test() throws Exception {
        // given
        Long id = 1L;

        // when
        ResultActions resultActions = mvc
                .perform(MockMvcRequestBuilders.get("/board/" + id).accept("application/json; charset=utf-8"));

        // then
        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("디버그 : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value("스프링1강")); // 이거 문법 대문에... 제이슨 쿼리
                                                                                                // 여기 넣을 수 있는지 모름

        // json을 다시 파싱해서 확인할 수도 있음
        // om.readValue(mvcResult.getResponse().getContentAsString(), BoardReqDto); 이거
        // 뭐임... 모름...
    }

    // @Test
    // public void save_test() throws JsonProcessingException {
    // // given : 테스트 코드 매개변수에 들어올 데이터 생성
    // BoardSaveReqDto boardSaveReqDto = new BoardSaveReqDto();
    // boardSaveReqDto.setTitle("스프링 테스트");
    // boardSaveReqDto.setContent("테스트 내용");

    // // json화
    // String body = om.writeValueAsString(boardSaveReqDto);
    // System.out.println("디버그 확인" + body);

    // // 통합테스트 세션 값 어떻게? 검색
    // // 스프링부트 통합테스트 세션 로그인 [검색]

    // // when
    // HttpEntity<String> request = new HttpEntity<>(body, headers);

    // // mockMvc.perform(post("/board")
    // // .content(request)
    // // .contentType(MediaType.APPLICATION_JSON)
    // // .andExpect(status)
    // // );

    // ResponseEntity<String> response = rt.exchange("/board", HttpMethod.POST,
    // request, String.class);

    // // 세션이 잘 들어갔는지 검증
    // System.out.println("상태코드" + response.getStatusCode());

    // // then
    // }

    @Test
    public void save_test() throws Exception {
        // given
        BoardSaveReqDto boardSaveReqDto = new BoardSaveReqDto();
        boardSaveReqDto.setTitle("스프링1강");
        boardSaveReqDto.setContent("트랜잭션관리");

        String body = om.writeValueAsString(boardSaveReqDto);

        // when
        ResultActions resultActions = mvc
                .perform(MockMvcRequestBuilders.post("/board").content(body)
                        .contentType("application/json; charset=utf-8").accept("application/json; charset=utf-8")
                        .session(session));

        // then
        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("디버그 : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void findall_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc
                .perform(MockMvcRequestBuilders.get("/board").accept("application/json; charset=utf-8")
                        .session(session));

        // then
        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("디버그 : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1L));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.data.[0].title").value("스프링1강")); // 배열 찾는법

    }

    @Test
    public void update_test() throws Exception {
        // given
        Long id = 1l;
        BoardUpdateReqDto boardUpdateReqDto = new BoardUpdateReqDto();
        boardUpdateReqDto.setTitle("스프링2강");
        boardUpdateReqDto.setContent("트랜잭션관리");

        String body = om.writeValueAsString(boardUpdateReqDto);

        // when
        ResultActions resultActions = mvc
                .perform(MockMvcRequestBuilders.put("/board/" + id).content(body)
                        .contentType("application/json; charset=utf-8").accept("application/json; charset=utf-8")
                        .session(session));

        // then
        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("디버그 : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value("스프링2강"));

    }

    @Test
    public void deleteById_test() throws Exception {
        // given
        Long id = 1l;

        // when
        ResultActions resultActions = mvc
                .perform(MockMvcRequestBuilders.delete("/board/" + id)
                        .accept("application/json; charset=utf-8")
                        .session(session));

        // then
        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("디버그 : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());

    }

}
