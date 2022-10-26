package site.metacoding.white.dto;

import javax.websocket.Session;

import lombok.Getter;
import lombok.Setter;
import site.metacoding.white.domain.Board;

public class boardReqDto {
    @Getter
    @Setter
    public static class BoardSaveReqDto {
        private String title;
        private String content;
        private SessionUser sessionUser;

        public Board toEntity() {
            Board board = new Board().builder()
                    .title(title)
                    .content(content)
                    .user(sessionUser.toEntity())
                    .build();
            return board;
        }

    }

    @Getter
    @Setter
    public static class BoardUpdateReqDto {
        private String title;
        private String content;
        private Long id;

        public Board toEntity() {
            Board board = new Board().builder()
                    .title(title)
                    .content(content)
                    .id(id)
                    .build();
            return board;
        }

    }
}
