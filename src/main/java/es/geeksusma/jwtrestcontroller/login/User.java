package es.geeksusma.jwtrestcontroller.login;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
class User {

    private Long userId;

    private String nickName;

    private String email;

    private String password;

}
