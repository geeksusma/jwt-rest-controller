package es.geeksusma.jwtrestcontroller.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.geeksusma.jwtrestcontroller.arch.exception.InvalidTokenException;
import es.geeksusma.jwtrestcontroller.config.JwtPropertiesConfiguration;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;

@Service
public class JwtService {

    private static final String USER_NAME_KEY = "name";
    private final JwtPropertiesConfiguration jwtPropertiesConfiguration;

    private final JwtFactory jwtFactory;

    private final ObjectMapper objectMapper;

    JwtService(JwtPropertiesConfiguration jwtPropertiesConfiguration, JwtFactory jwtFactory, ObjectMapper objectMapper) {
        this.jwtPropertiesConfiguration = jwtPropertiesConfiguration;
        this.jwtFactory = jwtFactory;
        this.objectMapper = objectMapper;
    }

    public String create(final Authentication auth) throws IOException {
        Collection<? extends GrantedAuthority> roles = auth.getAuthorities();
        final Claims claims = jwtFactory.createClaims();
        claims.put(jwtPropertiesConfiguration.getAuthorities(), objectMapper.writeValueAsString(roles));
        claims.put(USER_NAME_KEY, getAuthenticatedUser(auth).getUsername());
        return jwtFactory.createToken(Token.builder()
                .secret(jwtPropertiesConfiguration.getSecret())
                .userName(auth.getName()).tokenClaims(claims).expirationTimeInMillis(jwtPropertiesConfiguration.getExpirationTime()).build());

    }


    public Boolean validate(final String token) {
        try {
            final Claims claims = getClaims(token);
            return claims != null;
        } catch (JwtException | InvalidTokenException | IllegalArgumentException e) {
            return false;
        }
    }


    public String getUsername(String token) throws InvalidTokenException {
        return getClaims(token).getSubject();
    }


    private Claims getClaims(final String token) throws InvalidTokenException {
        return jwtFactory.createClaim(token, jwtPropertiesConfiguration.getSecret().getBytes(), jwtPropertiesConfiguration.getTokenPrefix());
    }

    private User getAuthenticatedUser(final Authentication authentication) {
        return (User) authentication.getPrincipal();
    }
}
