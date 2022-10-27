package site.metacoding.white.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.metacoding.white.dto.BoardRespDto.BoardDetailRespDto.CommentDto;

@NoArgsConstructor
@Entity
@Getter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @Builder
    public Comment(Long id, String content, User user, Board board) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.board = board;
    }

}
