package es.geeksusma.jwtrestcontroller.security;

import es.geeksusma.jwtrestcontroller.arch.exception.InvalidTokenException;
import es.geeksusma.jwtrestcontroller.time.Clock;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

@Component
class JwtFactory {
    private static final String INVALID_TOKEN_ERROR = "Token is null or prefix is wrong";
    private final Clock clock;

    JwtFactory(final Clock clock) {
        this.clock = clock;
    }

    Claims createClaims() {
        return Jwts.claims();
    }

    String createToken(final Token token) {

        return Jwts.builder().setClaims(token.getTokenClaims()).setSubject(token.getUserName())
                .signWith(SignatureAlgorithm.HS512, Base64Utils.encodeToString(token.getSecret().getBytes()).getBytes())
                .setIssuedAt(clock.getCurrentDate())
                .setExpiration(clock.getCurrentExpirationTime(clock.getCurrentDate(), token.getExpirationTimeInMillis())).compact();
    }

    Claims createClaim(final String token, final byte[] encodedSecret, final String prefix)
            throws InvalidTokenException {
        String secret = Base64Utils.encodeToString(encodedSecret);
        return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(resolve(token, prefix)).getBody();
    }

    private String resolve(final String token, final String prefix) throws InvalidTokenException {
        if (token != null && token.startsWith(prefix)) {
            return token.replace(prefix, "");
        }
        throw new InvalidTokenException(INVALID_TOKEN_ERROR);

    }
}
