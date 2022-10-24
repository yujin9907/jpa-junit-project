package site.metacoding.white.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity // 테이블을 만들기 위해 필수
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // mariadb가 쓰는 전략으로, auto increment.
    private Long id; // 보통 primary key 는 long으로 생성
    @Column(unique = true) // , 하고 컨스 누르면 이것저것 많이 나옴 필요시 찾아쓰기 일단은 복습만
    private String username;
    private String password;
}
