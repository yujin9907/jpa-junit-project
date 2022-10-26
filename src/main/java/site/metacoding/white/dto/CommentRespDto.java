package site.metacoding.white.dto;

import lombok.Getter;
import lombok.Setter;
import site.metacoding.white.domain.Board;
import site.metacoding.white.domain.Comment;
import site.metacoding.white.domain.User;

public class CommentRespDto {
    @Getter
    @Setter
    public static class CommentSaveRespDto {
        private Long id;
        private String content;

        private UserDto user;
        private BoardDto board;

        @Getter
        @Setter
        public static class UserDto {
            private Long id;
            private String username;

            public UserDto(User user) {
                this.id = user.getId();
                this.username = user.getUsername();
            }
        }

        @Getter
        @Setter
        public static class BoardDto {
            private Long id;

            public BoardDto(Board board) {
                this.id = board.getId();
            }
        }

        public CommentSaveRespDto(Comment comment) {
            this.id = comment.getId();
            this.content = comment.getContent();
            this.user = new UserDto(comment.getUser()); // 필드 노출을 막기 위해서
            this.board = new BoardDto(comment.getBoard()); // 내부적으로만 사용하기 때문에 userdto, boarddto를 안에 생성(일회용처럼)
        }
    }
}
