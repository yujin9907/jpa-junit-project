package site.metacoding.white.dto;

import lombok.Getter;
import lombok.Setter;
import site.metacoding.white.domain.User;

public class boardReqDto {

    // static을 붙였으므로, boardReqDto를 new 하지 않아도 찾을 수 있음
    @Getter
    @Setter
    public static class BoardSaveDto {
        private String title;
        private String content;

        // 클라이언트한테 받는 게 아님
        @Setter
        @Getter
        public class ServiceDto {
            private User user;
        }

        private ServiceDto serviceDto;

        public void newInstance() {
            serviceDto = new ServiceDto();
        }

    }

    // 필요한 dto 여기다 추가로

}
