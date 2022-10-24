package site.metacoding.white.domain;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class UserRepository {

    private final EntityManager em;

    public void save(User user) {
        em.persist(user); // persistence context에 영속화 -> 트랜잭션 종료시 자동 flush
    }

    public User findByUsername(String username) {
        // jpal : 객체지향쿼리 특징 : 알아서 네이티브 쿼리로 바꿔서 실행해주며, from 첫글자가 대문자고, 무조건 별칭이 필요함
        // 하이버네이트가 만든 쿼리. 네이티브 쿼리와 좀 다름 => 자동으로 매핑해준다는 편리함
        User userPS = em.createQuery("select u from User u where u.username = :username", User.class)
                .setParameter("username", username)
                .getSingleResult();
        return userPS;
    }

}
