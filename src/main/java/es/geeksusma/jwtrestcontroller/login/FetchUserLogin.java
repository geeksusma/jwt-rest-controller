package es.geeksusma.jwtrestcontroller.login;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
class FetchUserLogin {
    private String username;
    private String password;
}
