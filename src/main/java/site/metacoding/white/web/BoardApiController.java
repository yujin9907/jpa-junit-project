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
import site.metacoding.white.dto.ResponseDto;
import site.metacoding.white.dto.SessionUser;
import site.metacoding.white.dto.BoardRespDto.BoardSaveRespDto;
import site.metacoding.white.dto.boardReqDto.BoardSaveReqDto;
import site.metacoding.white.dto.boardReqDto.BoardUpdateReqDto;
import site.metacoding.white.service.BoardService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@RestController
public class BoardAPIController {

    private final BoardService boardService;
    private final HttpSession session;

    @PostMapping("/board")
    public ResponseDto<?> save(@RequestBody BoardSaveReqDto boardSaveReqDto) {
        SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            throw new RuntimeException("인증 필요");
        }
        // 핵심
        boardSaveReqDto.setSessionUser(sessionUser);
        BoardSaveRespDto boardSaveRespDto = boardService.save(boardSaveReqDto); // 서비스에는 단 하나의 객체만 전달한다.
        return new ResponseDto<>(1, "성공", boardSaveRespDto);
    }

    // 게시물 상세보기 = 보드 + 유저 + 커멘트
    @GetMapping("/board/{id}")
    public ResponseDto<?> findById(@PathVariable Long id) {
        // 필요에 따라 인증 체크
        return new ResponseDto<>(1, "성공", boardService.findById(id)); // Entity -> JSON 변경 (MessageConverter)
    }

    @GetMapping("/board")
    public List<Board> findAll() {
        // 필요에 따라 인증 체크
        return boardService.findAll();
    }

    @PutMapping("/board/{id}")
    public ResponseDto<?> update(@PathVariable Long id, @RequestBody BoardUpdateReqDto boardUpdateReqDto) {
        SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            throw new RuntimeException("인증 필요");
        }
        // 핵심
        boardUpdateReqDto.setId(id);
        return new ResponseDto<>(1, "성공", boardService.update(boardUpdateReqDto));
    }

    @DeleteMapping("/board/{id}")
    public ResponseDto<?> deleteById(@PathVariable Long id) {
        SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            throw new RuntimeException("인증 필요");
        }
        // 핵심
        boardService.deleteById(id);
        return new ResponseDto<>(1, "성공", null); // Entity -> JSON 변경 (MessageConverter)
    }

    // @GetMapping("/v2/board/{id}")
    // public String findByIdV2(@PathVariable Long id) {
    // System.out.println("현재 open-in-view는 true 인가 false 인가 생각해보기!!");
    // Board boardPS = boardService.findById(id);
    // System.out.println("board.id : " + boardPS.getId());
    // System.out.println("board.title : " + boardPS.getTitle());
    // System.out.println("board.content : " + boardPS.getContent());
    // System.out.println("open-in-view가 false이면 Lazy 로딩 못함");

    // // 날라감)
    // return "ok";
    // }
}
