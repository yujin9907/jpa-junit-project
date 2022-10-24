package site.metacoding.white.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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

    // private Integer userId; 하이버네이트 안 쓰면 이렇게 의존함
    // @JoinColumn(name=username) 이런 식으로 조인의 기준을 정할 수도 있음.
    @ManyToOne(fetch = FetchType.EAGER) // 관계를 걸어줌 => 포린키 역할.
    private User user;

    // 좋아요의 경우
    // @OneToMany
    // private List<Love> Love;
}
