package site.metacoding.white.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// jpa리포지토리 상속하는 순간, ioc 컨테이너에 뜸
// 찝찝하면, 어노테이션 레포지토리 붙여도 되는데, 안붙여도 됨
public interface UserJpaRepository extends JpaRepository<User, Long> { // <엔티티, id타입> 규칙
    // login을 위한 findByName
    // 인터페이스니까 public, abstract 안 붙여도 자동으로 붙여줌. 자동으로 추상 클래스 취급

    // @Query(value = "select * form user where username=:username", ) 네이티브 쿼리 쓰는 것
    // [나중에 서치]
    @Query(value = "select u from User u where username=:username") // findby 문법이기 때문에 이거 주석처리해도 됨
    User findByUsername(@Param("username") String username);
    // findBy필드명(=where절) 이 문법임, jpa 네임드 쿼리라고 함
    // 좀 귀찮아도 적어주는 게 좋긴 함.
    // 이런 문법이 있다는 걸 알아두기

    // findbyid, findall, save, deletebyid, ... 몇가지 기본적으로 제공함 => 기본적으로 만들 crud는 구현이
    // 된 상태
}
