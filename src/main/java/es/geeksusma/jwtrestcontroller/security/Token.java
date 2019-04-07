package es.geeksusma.jwtrestcontroller.security;

import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
class Token {

    private Claims tokenClaims;

    private String userName;

    private Long expirationTimeInMillis;

    private String secret;

}
