package site.metacoding.white.domain;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository // ioc 컨테이너에 등록
public class BoardRepository {
    // 여기 dao + xml 역할을 함 => 쿼리 적는 게 없는데 어떻게?

    private final EntityManager em;

    public void save(Board board) { // board를 인서트할 거임
        // 프리페어스테이트먼트를 어떻게 구현? => jpa를 라이브러리로 받았기 때문에, ioc 컨테이너에 있음, 사용할 때 di로 사용하면 됨
        // entity manager : 프리페어스테이트먼트 역할 => result set을 내리면, 커서를 내리면, 자바 오브젝트 매핑을 내가 해야
        // 됨
        // entity manager는 마이바티스처럼 자동 매핑을 해줌(위 과정을 생략해줌) 즉, 테이블 데이터 -> 자바 오브젝트로 바꾸는 걸 해줌

        // em.하면 트랜잭션 관리도 할 수도 있음 -> 근데 서비스에서 트랜잭션을 관리하므로 사용할 일이 없음
        em.persist(board); // 이러면 인서트 쿼리가 들어옴

        // ??
        // persist = 영속화

        // em.createQuery("inert into .........") 이렇게 적어도 됨

        // if (board.getId() == null) {
        // em.persist(board); // 인서트
        // } else {
        // em.merge(board); // 업데이트
        // }
    }

    public Board findById(Long id) {
        Board boardPS = em.createQuery("select b from Board b where b.id = :id", Board.class)
                .setParameter("id", id)
                .getSingleResult();
        return boardPS;
    }

    public List<Board> findAll() {
        List<Board> boardPS = em.createQuery("select b from Board b", Board.class)
                .getResultList();
        return boardPS;
    }

    public void deleteById(Board board) {
        em.remove(board);
    }
}
