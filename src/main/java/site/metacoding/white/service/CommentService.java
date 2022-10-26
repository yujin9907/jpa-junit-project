package site.metacoding.white.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.metacoding.white.domain.Board;
import site.metacoding.white.domain.BoardRepository;
import site.metacoding.white.domain.Comment;
import site.metacoding.white.domain.CommentRepository;
import site.metacoding.white.dto.CommentReqDto.CommentSaveReqDto;
import site.metacoding.white.dto.CommentRespDto.CommentSaveRespDto;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public CommentSaveRespDto save(CommentSaveReqDto commentSaveReqDto) {
        // 먼저 처음에 어떻게 할지 모르겠으면, 셋이라도 박아서 해라, 이때만 잠깐 셋을 넘겨줌

        // 1. 보드가 있는지 채크 : 넘어오는 데이터을 받으면 영속화 후 사용(잘못된 데이터일 수 있음), 세션은 그냥 사용
        Optional<Board> boardOP = boardRepository.findById(commentSaveReqDto.getBoardId());
        boardOP.orElseThrow(() -> new RuntimeException("없음"));
        // 2. 있으면 로직 실행
        Comment comment = commentSaveReqDto.toEntity(commentSaveReqDto, boardOP.get());

        Comment commentPS = commentRepository.save(comment);
        CommentSaveRespDto commentSaveRespDto = new CommentSaveRespDto(commentPS);
        return commentSaveRespDto;
    }

    // @Transactional
    // public void deleteById(Long id) {
    // boardRepository.findById(id)
    // .orElseThrow(() -> new RuntimeException("해당 " + id + "로 삭제를 할 수 없습니다"));
    // boardRepository.deleteById(id);
    // }

    // @Transactional(readOnly = true) // 트랜잭션을 걸면 OSIV가 false여도 디비 커넥션이 유지됨.
    // public BoardDetailRespDto findById(Long id) {
    // Board boardPS = boardRepository.findById(id)
    // .orElseThrow(() -> new RuntimeException("해당 " + id + "로 상세보기를 할 수 없습니다"));

    // // Optional<Board> boardOP = boardRepository.findById(id);
    // // // .orElseThrow("아이디없음");
    // // if (!boardOP.isPresent()) {
    // // throw new RuntimeException(id + "해당 아이디가 없음");
    // // }
    // BoardDetailRespDto boardDetailRespDto = new BoardDetailRespDto(boardPS); //
    // dto 내부적으로 유저 객체를 담아줄 때 자동 레이지
    // return boardDetailRespDto;
    // }
}
