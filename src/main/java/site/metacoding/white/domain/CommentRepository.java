package site.metacoding.white.domain;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Repository
public class CommentRepository {
    // 등록, 삭제, 한건보기, (전체보기는 board에)

    private final EntityManager em;

    public Comment save(Comment comment) {
        em.persist(comment);
        return comment;
    }

    public void deleteById(Long id) {
        em.createQuery("delete from Comment b where b.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    public Optional<Comment> findById(Long id) {
        Optional<Comment> commentOP = Optional.of(em
                .createQuery("select c from Comment c where c.id = :id",
                        Comment.class)
                .setParameter("id", id)
                .getSingleResult());

        return commentOP;
    }

    // public void deleteById(Long id) {
    // em.createQuery("delete from Comment b where b.id = :id")
    // .setParameter("id", id)
    // .executeUpdate();
    // }

    // public Optional<Comment> findById(Long id) {
    // Optional<Comment> boardOP = Optional.of(em
    // .createQuery("select b from Comment b where b.id = :id",
    // Comment.class)
    // .setParameter("id", id)
    // .getSingleResult());

    // return boardOP;
    // }
}
