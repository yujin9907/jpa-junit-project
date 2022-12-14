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
import site.metacoding.white.dto.boardReqDto.BoardSaveDto;
import site.metacoding.white.dto.boardReqDto.BoardSaveDto.ServiceDto;
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

    @PostMapping("/v2/board")
    public String saveBoardV2(@RequestBody BoardSaveDto boardSaveDto) {
        // 공공데이터, 소켓으로 받아오는 거 했던 게 머임?
        User principal = (User) session.getAttribute("principal");
        boardSaveDto.newInstance();
        boardSaveDto.getServiceDto().setUser(principal);
        boardService.save(boardSaveDto);
        return "ok";
    }

    @GetMapping("/api/v1/board")
    public List<Board> findallV1() {
        return boardService.findAll();
    }

    @DeleteMapping("/board/{id}")
    public String deleteBoard(@PathVariable Long id) {
        boardService.deleteById(id);
        return "ok";
    }

    @GetMapping("/board")
    public List<Board> findall() {
        return boardService.findAll();
    }

    // @PostMapping("/board")
    // public String saveBoard(@RequestBody Board board) {
    // boardService.save(board);
    // return "ok";
    // }

    @GetMapping("/board/{id}")
    public Board findByIdBoard(@PathVariable long id) {
        return boardService.findById(id);
    }

    @PutMapping("/board/{id}")
    public String update(@PathVariable Long id, @RequestBody Board board) {
        // 이때 board에 id(기본키)가 들어오면 안 되기 때문에 엔티티를 받는 게 아니라 dto를 받아야 맞음
        // 화면에 뿌릴 때도 필요한 데이터만 던져야 됨
        boardService.update(id, board);
        return "ok";
    }
}
