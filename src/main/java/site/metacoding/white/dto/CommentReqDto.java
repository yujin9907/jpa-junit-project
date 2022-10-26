package site.metacoding.white.dto;

import lombok.Getter;
import lombok.Setter;
import site.metacoding.white.domain.Board;
import site.metacoding.white.domain.Comment;

public class CommentReqDto {

    @Getter
    @Setter
    public static class CommentSaveReqDto {
        private String content; // 클라이언트
        private SessionUser sessionUser; // 서비스 로직
        private Long boardId; // 클라이언트

        public Comment toEntity(CommentSaveReqDto commentSaveReqDto, Board board) {
            return Comment.builder()
                    .content(commentSaveReqDto.getContent())
                    .user(commentSaveReqDto.getSessionUser().toEntity())
                    .board(board)
                    .build();
        }
    }
}
