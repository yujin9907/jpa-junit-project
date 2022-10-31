package site.metacoding.white.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.metacoding.white.domain.Board;
import site.metacoding.white.domain.BoardRepository;
import site.metacoding.white.dto.BoardReqDto.BoardSaveReqDto;
import site.metacoding.white.dto.BoardReqDto.BoardUpdateReqDto;
import site.metacoding.white.dto.BoardRespDto.BoardAllRespDto;
import site.metacoding.white.dto.BoardRespDto.BoardDetailRespDto;
import site.metacoding.white.dto.BoardRespDto.BoardSaveRespDto;
import site.metacoding.white.dto.BoardRespDto.BoardUpdateRespDto;

// 트랜잭션 관리
// DTO 변환해서 컨트롤러에게 돌려줘야함

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public BoardSaveRespDto save(BoardSaveReqDto boardSaveReqDto) {
        // 핵심 로직
        Board boardPS = boardRepository.save(boardSaveReqDto.toEntity());

        // DTO 전환
        BoardSaveRespDto boardSaveRespDto = new BoardSaveRespDto(boardPS);

        return boardSaveRespDto;
    } // DB커넥션을 종료

    @Transactional(readOnly = true) // 트랜잭션을 걸면 OSIV가 false여도 디비 커넥션이 유지됨.
    public BoardDetailRespDto findById(Long id) {

        Optional<Board> boardOP = boardRepository.findById(id);

        if (boardOP.isPresent()) {
            BoardDetailRespDto boardDetailRespDto = new BoardDetailRespDto(boardOP.get());
            return boardDetailRespDto;
        } else {
            throw new RuntimeException("해당 " + id + "로 상세보기를 할 수 없습니다.");
        }
    }

    @Transactional
    public BoardUpdateRespDto update(BoardUpdateReqDto boardUpdateReqDto) {
        Long id = boardUpdateReqDto.getId();
        Optional<Board> boardOP = boardRepository.findById(id);
        if (boardOP.isPresent()) {
            Board boardPS = boardOP.get();
            boardPS.update(boardUpdateReqDto.getTitle(), boardUpdateReqDto.getContent());
            return new BoardUpdateRespDto(boardPS);
        } else {
            throw new RuntimeException("해당 " + id + "로 수정을 할 수 없습니다.");
        }

    } // 트랜잭션 종료시 -> 더티체킹을 함

    @Transactional(readOnly = true)
    public List<BoardAllRespDto> findAll() {
        List<Board> boardList = boardRepository.findAll();

        List<BoardAllRespDto> boardAllRespDtoList = new ArrayList<>();
        for (Board board : boardList) {
            boardAllRespDtoList.add(new BoardAllRespDto(board));
        }

        return boardAllRespDtoList;
    }

    // return boardRepository.findAll()
    // .stream().map((board) -> new
    // BoardAllRespDto(board)).collect(Collectors.toList());

    // delete는 리턴 안함.
    @Transactional
    public void deleteById(Long id, Long sessionUserId) {
        log.debug("디버그 : " + id);
        Optional<Board> boardOP = boardRepository.findById(id);
        log.debug("디버그 : " + boardOP);
        if (boardOP.isPresent()) {
            Board boardPS = boardOP.get();
            if (boardPS.getUser().getId() != sessionUserId) {
                throw new RuntimeException("해당 아이디를 삭제할 권한이 없습니다"); // jwt 필터 적용시, 세션은 rep->res까지 통신 과정까지 잠깐 담아두는 주머니.
                                                                    // statefull과 관련 없음
            }
            boardRepository.deleteById(id);
        } else {
            throw new RuntimeException("해당 " + id + "로 삭제를 할 수 없습니다.");
        }

    }

}
