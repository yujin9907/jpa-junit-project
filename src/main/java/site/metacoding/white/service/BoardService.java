package site.metacoding.white.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.metacoding.white.domain.Board;
import site.metacoding.white.domain.BoardRepository;
import site.metacoding.white.dto.BoardRespDto.BoardDetailRespDto;
import site.metacoding.white.dto.BoardRespDto.BoardListRespDto;
import site.metacoding.white.dto.BoardRespDto.BoardSaveRespDto;
import site.metacoding.white.dto.BoardRespDto.BoardUpdateRespDto;
import site.metacoding.white.dto.boardReqDto.BoardSaveReqDto;
import site.metacoding.white.dto.boardReqDto.BoardUpdateReqDto;

// 트랜잭션 관리
// DTO 변환해서 컨트롤러에게 돌려줘야함

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public BoardSaveRespDto save(BoardSaveReqDto boardRSaveReqDto) {
        // 핵심 로직
        Board boardPS = boardRepository.save(boardRSaveReqDto.toEntity());

        // DTO 전환
        BoardSaveRespDto boardSaveRespDto = new BoardSaveRespDto(boardPS);

        return boardSaveRespDto;
    }

    @Transactional(readOnly = true) // 트랜잭션을 걸면 OSIV가 false여도 디비 커넥션이 유지됨.
    public BoardDetailRespDto findById(Long id) {
        Board boardPS = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 " + id + "로 상세보기를 할 수 없습니다"));

        // Optional<Board> boardOP = boardRepository.findById(id);
        // // .orElseThrow("아이디없음");
        // if (!boardOP.isPresent()) {
        // throw new RuntimeException(id + "해당 아이디가 없음");
        // }
        BoardDetailRespDto boardDetailRespDto = new BoardDetailRespDto(boardPS); // dto 내부적으로 유저 객체를 담아줄 때 자동 레이지
        return boardDetailRespDto;
    }

    @Transactional
    public BoardUpdateRespDto update(BoardUpdateReqDto boardUpdateReqDto) { // 완료된 body 결과를 리턴해야 됨
        Optional<Board> boardOP = boardRepository.findById(boardUpdateReqDto.getId()); // 영속화시킴 -> pc에서 더티채킹으로 자동 업데이트
        if (boardOP.isPresent()) {
            Board boardPS = boardOP.get();
            boardPS.update(boardUpdateReqDto.getTitle(), boardUpdateReqDto.getContent());
            return new BoardUpdateRespDto(boardPS);
        } else {
            throw new RuntimeException();
        }

    } // 트랜잭션 종료시 -> 더티체킹을 함

    public List<Board> findAll() {
        List<Board> boardList = boardRepository.findAll();

        List<BoardListRespDto> boardListRespDtos = new ArrayList<>();
        for (Board boardPS : boardList) {
            BoardListRespDto boardListRespDto = new BoardListRespDto(boardPS);
            boardListRespDtos.add(boardListRespDto);
        }

        return boardRepository.findAll();
    }

    @Transactional
    public void deleteById(Long id) {
        boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 " + id + "로 삭제를 할 수 없습니다"));
        boardRepository.deleteById(id);
    }

}
