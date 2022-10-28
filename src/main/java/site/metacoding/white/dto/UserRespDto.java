package site.metacoding.white.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.metacoding.white.domain.User;

public class UserRespDto {

    @Setter
    @Getter
    public static class JoinRespDto {
        private Long id;
        private String username;

        // 응답의 DTO는 생성자로 처리한다.
        public JoinRespDto(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class UserUpdateRespDto {
        private Long id;
        private String username;

        public UserUpdateRespDto(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
        }
    }
}
