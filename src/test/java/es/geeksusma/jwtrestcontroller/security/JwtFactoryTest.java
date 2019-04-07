package es.geeksusma.jwtrestcontroller.security;

import es.geeksusma.jwtrestcontroller.arch.exception.InvalidTokenException;
import es.geeksusma.jwtrestcontroller.time.Clock;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Base64Utils;

import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class JwtFactoryTest {

    private static final String TOKEN_IS_NULL_OR_PREFIX_IS_WRONG = "Token is null or prefix is wrong";

    private static final long EXPIRATION_TIME = 1L;

    private static final String TOKEN_PREFIX = "bearer";

    private static final String SECRET = "secret";

    private static final String USERNAME = "username";

    private JwtFactory jwtFactory;

    private Clock clock;

    @Before
    public void setUp() {
        clock = mock(Clock.class);
        jwtFactory = new JwtFactory(clock);

    }

    @Test
    public void should_createClaims_when_createClaims() {
        // given
        final Claims expectedClaims = Jwts.claims();

        // when
        final Claims createdClaims = jwtFactory.createClaims();

        // then
        assertThat(createdClaims).isEqualTo(expectedClaims);
    }

    @Test
    public void should_createToken_when_createToken() {
        // given
        final Claims claims = mock(Claims.class);
        setUpDate();
        final String expected = Jwts.builder().setClaims(claims).setSubject(USERNAME)
                .signWith(SignatureAlgorithm.HS512, Base64Utils.encodeToString(SECRET.getBytes()).getBytes())
                .setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .compact();

        final Token token = Token.builder().userName(USERNAME).expirationTimeInMillis(EXPIRATION_TIME)
                .tokenClaims(claims).secret(SECRET).build();
        // when
        final String createdToken = jwtFactory.createToken(token);
        // then
        assertThat(createdToken).isEqualTo(expected);
    }

    @Test
    public void should_createClaims_when_createClaimsWithTokenSecretAndPrefix() throws InvalidTokenException {

        // given
        final Claims claims = Jwts.claims();
        final Token token = Token.builder().userName(USERNAME).expirationTimeInMillis(1000L).tokenClaims(claims)
                .secret(SECRET).build();
        final String validToken = jwtFactory.createToken(token);
        setUpDate();
        final Claims expected = Jwts.parser().setSigningKey(Base64Utils.encodeToString(SECRET.getBytes()).getBytes())
                .parseClaimsJws(validToken).getBody();
        // when
        final Claims createdClaims = jwtFactory.createClaim(TOKEN_PREFIX + " " + validToken, SECRET.getBytes(),
                TOKEN_PREFIX);
        // then
        assertThat(createdClaims).isEqualTo(expected);

    }

    @Test
    public void should_ThrowInvalidTokenException_when_TokenIsWrong() {
        // given
        final Claims claims = Jwts.claims();
        final Token token = Token.builder().userName(USERNAME).expirationTimeInMillis(1000L).tokenClaims(claims)
                .secret(SECRET).build();
        final String validToken = jwtFactory.createToken(token);
        final String WrongTokenPrefix = "Wrong ";
        setUpDate();

        // when
        final Throwable expectedException = catchThrowable(
                () -> jwtFactory.createClaim(WrongTokenPrefix + " " + validToken, SECRET.getBytes(), TOKEN_PREFIX));

        // then
        assertThat(expectedException).isInstanceOf(InvalidTokenException.class)
                .hasMessageContaining(TOKEN_IS_NULL_OR_PREFIX_IS_WRONG);
    }

    @Test
    public void should_ThrowInvalidTokenException_when_TokenPrefixIsWrong() {
        // given
        final Claims claims = Jwts.claims();
        final Token token = Token.builder().userName(USERNAME).expirationTimeInMillis(1000L).tokenClaims(claims)
                .secret(SECRET).build();
        final String validToken = jwtFactory.createToken(token);
        final String wrongPrefix = "Vearer ";
        setUpDate();

        // when
        final Throwable expectedException = catchThrowable(
                () -> jwtFactory.createClaim(TOKEN_PREFIX + " " + validToken, SECRET.getBytes(), wrongPrefix));

        // then
        assertThat(expectedException).isInstanceOf(InvalidTokenException.class)
                .hasMessageContaining(TOKEN_IS_NULL_OR_PREFIX_IS_WRONG);
    }

    @Test
    public void should_ThrowInvalidTokenException_when_createClaimsWithNullToken() {
        // given
        final String token = null;
        // when
        final Throwable expectedException = catchThrowable(
                () -> jwtFactory.createClaim(token, SECRET.getBytes(), TOKEN_PREFIX));

        // then
        assertThat(expectedException).isInstanceOf(InvalidTokenException.class)
                .hasMessageContaining(TOKEN_IS_NULL_OR_PREFIX_IS_WRONG);
    }

    private void setUpDate() {
        Date date = Date.from(Instant.now());
        given(clock.getCurrentDate()).willReturn(date);
        given(clock.getCurrentExpirationTime(date, JwtFactoryTest.EXPIRATION_TIME))
                .willReturn(Date.from(Instant.ofEpochMilli(date.getTime() + JwtFactoryTest.EXPIRATION_TIME)));
    }

}