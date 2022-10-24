package site.metacoding.white.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import site.metacoding.white.domain.Board;
import site.metacoding.white.domain.BoardRepository;

@RequiredArgsConstructor
@Service // 스프링 ioc 메모리에 무조건 등록해야 됨
// 이 어노테이션을 통해 메모리에 띄우려(=컴포넌트 스캔이라고 함)면, 디폴트 컨스트럭터(빈 생성자)를 호출함
public class BoardService {
    // 서비스의 역할, 트랜잭션 관리
    // 일관성을 위해 트랜잭션 관리 필요 없어도 생성

    private final BoardRepository boardRepository;

    @Transactional
    public void save(Board board) {
        boardRepository.save(board);
    }

    @Transactional // 셀렉트엔 트랜잭션 필요없음 상식적으로
    public Board findById(long id) {
        return boardRepository.findById(id);
    }

    @Transactional
    public void update(Long id, Board board) {
        // 즉, save{if} 부분의 머지를 직접 짜는 거임
        Board boardPS = boardRepository.findById(id); // 영속화
        // 사실 이게 없으면 null 익셉션 터트려줘야 됨.
        // if 데이터가 null이면
        // throw new RuntimeException("없는 데이터다");
        boardPS.setTitle(board.getTitle());
        boardPS.setContent(board.getContent());
        boardPS.setAuthor(board.getAuthor()); // 영속화된 데이터를 들어온 데이터로 수정함 => PC 에 있는 boardPS가 수정되고
                                              // 트랜잭션이 종료되면 자동으로 flush되기 때문에, 업데이트 처리가 됨
    } // == 트랜잭션 종료시 더티체킹 함, 모아놨다 한 번에 처리함 = 하이버네이트 기술

    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    @Transactional
    public void deleteById(Long id) {
        Board boardPS = boardRepository.findById(id);
        boardRepository.deleteById(boardPS);
    }
}