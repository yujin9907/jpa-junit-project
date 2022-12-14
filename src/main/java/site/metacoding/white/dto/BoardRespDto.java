package site.metacoding.white.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.metacoding.white.domain.Board;
import site.metacoding.white.domain.Comment;
import site.metacoding.white.domain.User;

public class BoardRespDto {

    @Setter
    @Getter
    public static class BoardSaveRespDto {
        private Long id;
        private String title;
        private String content;
        private UserDto user;

        @Setter
        @Getter
        public static class UserDto {
            private Long id;
            private String username;

            public UserDto(User user) {
                this.id = user.getId();
                this.username = user.getUsername();
            }
        }

        public BoardSaveRespDto(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.user = new UserDto(board.getUser());
        }
    }

    @Setter
    @Getter
    public static class BoardDetailRespDto {
        private Long id;
        private String title;
        private String content;
        private BoardUserDto user;

        private List<CommentDto> comment = new ArrayList<>(); // 생성자를 안 만들어주면 null이 되니까 미리 new arry해줌: 아니면 다른 데서 new 해야
                                                              // 됨

        @NoArgsConstructor
        @Getter
        @Setter
        public static class CommentDto {
            private Long id;
            private String content;
            private CommentUserDto user;

            public CommentDto(Comment comment) {
                this.id = comment.getId();
                this.content = comment.getContent();
                this.user = new CommentUserDto(comment.getUser()); // lazy 로딩
            }

            @Setter
            @Getter
            public static class CommentUserDto {
                private Long id;
                private String username;

                public CommentUserDto(User user) {
                    this.id = user.getId();
                    this.username = user.getUsername();
                }
            }

        }

        @Setter
        @Getter
        public static class BoardUserDto {
            private Long id;
            private String username;

            public BoardUserDto(User user) {
                this.id = user.getId(); // 여기서 레이지 로딩, 여기서 불러옴.
                this.username = user.getUsername();
            }
        }

        public BoardDetailRespDto(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.user = new BoardUserDto(board.getUser());
            // list<commentdto> <------list<comment>를 옮기는 과정
            this.comment = board.getComment().stream()
                    .map((comment) -> new CommentDto(comment))
                    .collect(Collectors.toList());
        }

    }

    @Setter
    @Getter
    public static class BoardListRespDto {
        private Long id;
        private String title;
        private UserDto user;

        @Setter
        @Getter
        public static class UserDto {
            private Long id;
            private String username;

            public UserDto(User user) {
                this.id = user.getId();
                this.username = user.getUsername();
            }
        }

        public BoardListRespDto(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.user = new UserDto(board.getUser());
        }
    }

    @Setter
    @Getter
    public static class BoardUpdateRespDto {
        private Long id;
        private String title;
        private String content;
        private UserDto user;

        @Setter
        @Getter
        public static class UserDto {
            private Long id;

            public UserDto(User user) {
                this.id = user.getId();
            }
        }

        public BoardUpdateRespDto(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.user = new UserDto(board.getUser());
        }
    }
}
