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
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 프라이머리키 + autoinsert엿나 자동 생성되는 애
    private Long id; // 보통 primary key 는 long으로 생성
    private String title;
    @Column(length = 1000)
    private String content;
    private String author;
}
