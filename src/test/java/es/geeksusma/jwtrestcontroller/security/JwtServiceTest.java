package es.geeksusma.jwtrestcontroller.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.geeksusma.jwtrestcontroller.arch.exception.InvalidTokenException;
import es.geeksusma.jwtrestcontroller.config.JwtPropertiesConfiguration;
import io.jsonwebtoken.Claims;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import java.io.IOException;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class JwtServiceTest {

    private static final String AUTHORITIES = "authorities";
    private static final long EXPIRATION_TIME = 1L;
    private static final String SECRET = "MY DIRTY SECRET";
    private static final String LOGGED_USER = "LOGGED_USER";
    private static final String TOKEN_PREFIX = "TOKEN PREFIX";
    private static final String AUTHENTICATED_USER_NAME = "Buddy";


    @Mock
    private JwtPropertiesConfiguration jwtPropertiesConfiguration;

    @Mock
    private JwtFactory jwtFactory;

    //I had to use Spy because the readValue method to check the claims was impossible to mock due to the usage of internalClasses plus Generics
    @Spy
    private ObjectMapper objectMapper;

    private JwtService jwtService;

    @Before
    public void setUp() {
        jwtService = new JwtService(jwtPropertiesConfiguration, jwtFactory, objectMapper);

        given(jwtPropertiesConfiguration.getAuthorities()).willReturn(AUTHORITIES);
        given(jwtPropertiesConfiguration.getExpirationTime()).willReturn(EXPIRATION_TIME);
        given(jwtPropertiesConfiguration.getSecret()).willReturn(SECRET);
        given(jwtPropertiesConfiguration.getTokenPrefix()).willReturn(TOKEN_PREFIX);
    }

    @Test
    public void should_getClaims_when_createToken() throws IOException {
        // given
        final Authentication authentication = setUpAuthentication();
        setUpClaims();

        // when
        jwtService.create(authentication);

        // then
        then(jwtFactory).should().createClaims();
    }

    @Test
    public void should_mapRoles_when_create() throws IOException {
        // given
        final Authentication authentication = setUpAuthentication();
        setUpClaims();

        // when
        jwtService.create(authentication);

        // then
        then(objectMapper).should().writeValueAsString(authentication.getAuthorities());
    }

    @Test
    public void should_putAuthoritiesAsRoles_when_create() throws IOException {
        // given
        final Authentication authentication = setUpAuthentication();
        final Claims claims = setUpClaims();

        // when
        jwtService.create(authentication);

        // this test is checking only the case of no having roles

        // then
        then(claims).should().put(AUTHORITIES, "[]");

    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void should_requestSecret_when_create() throws IOException {
        // given
        final Authentication authentication = setUpAuthentication();
        setUpClaims();

        // when
        jwtService.create(authentication);

        // then
        then(jwtPropertiesConfiguration).should().getSecret();
    }

    @Test
    public void should_createTokenFromFactory_when_createToken() throws IOException {
        // given
        final Authentication authentication = setUpAuthentication();
        final Token token = setUpDomainToken(setUpClaims());

        // when
        jwtService.create(authentication);

        // then
        then(jwtFactory).should().createToken(token);
    }

    @Test
    public void should_returnExpectedToken_when_createToken() throws IOException {
        // given
        final Authentication authentication = setUpAuthentication();
        final Token token = setUpDomainToken(setUpClaims());
        final String expected = "expected";

        given(jwtFactory.createToken(token)).willReturn(expected);

        // when
        final String result = jwtService.create(authentication);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void should_returnTrue_when_validate() throws InvalidTokenException {
        // given
        final String token = "Bearer token";
        Claims claims = mock(Claims.class);
        given(jwtFactory.createClaim(token, SECRET.getBytes(), TOKEN_PREFIX)).willReturn(claims);
        final boolean expected = true;
        // when
        final boolean validate = jwtService.validate(token);
        // then
        assertThat(validate).isEqualTo(expected);
    }

    @Test
    public void should_returnFalse_when_validateClaimsIsNull() throws InvalidTokenException {
        // given
        final String token = "Bearer token";
        given(jwtFactory.createClaim(token, SECRET.getBytes(), TOKEN_PREFIX)).willReturn(null);
        final boolean expected = false;
        // when
        final boolean validate = jwtService.validate(token);
        // then
        assertThat(validate).isEqualTo(expected);
    }

    @Test
    public void should_returnFalse_when_validateClaimsThrowInvalidTokenException() throws InvalidTokenException {
        // given
        final String wrongToken = "Vearer Wrong token";
        given(jwtFactory.createClaim(wrongToken, SECRET.getBytes(), TOKEN_PREFIX))
                .willThrow(new InvalidTokenException("Exception Message"));
        final boolean expected = false;
        // when
        final boolean validate = jwtService.validate(wrongToken);
        // then
        assertThat(validate).isEqualTo(expected);
    }

    @Test
    public void should_returnUsername_when_tokenGiven() throws InvalidTokenException {
        // given
        final Claims claims = mock(Claims.class);
        final String token = "token";
        given(jwtFactory.createClaim(token, SECRET.getBytes(), TOKEN_PREFIX)).willReturn(claims);
        final String expect = "username";
        given(claims.getSubject()).willReturn(expect);
        // when
        String username = jwtService.getUsername(token);
        // then
        assertThat(username).isEqualTo(expect);
    }

    @Test
    public void should_returnExpected_when_GetUsername() throws InvalidTokenException {
        // given
        final String token = "my-token";
        final Claims claims = mock(Claims.class);
        final String expected = "expected user name";

        given(jwtFactory.createClaim(token, SECRET.getBytes(), TOKEN_PREFIX)).willReturn(claims);
        given(claims.getSubject()).willReturn(expected);

        // when
        final String username = jwtService.getUsername(token);

        // then
        assertThat(username).isEqualTo(expected);
    }

    private Authentication setUpAuthentication() {
        final Authentication authentication = mock(Authentication.class);
        final User authenticatedUser = mock(User.class);

        given(authentication.getAuthorities()).willReturn(emptyList());
        given(authentication.getName()).willReturn(LOGGED_USER);
        given(authentication.getPrincipal()).willReturn(authenticatedUser);
        given(authenticatedUser.getUsername()).willReturn(AUTHENTICATED_USER_NAME);
        return authentication;
    }

    private Claims setUpClaims() {
        final Claims claims = mock(Claims.class);
        given(jwtFactory.createClaims()).willReturn(claims);
        return claims;
    }

    private Token setUpDomainToken(Claims claims) {
        return Token.builder().expirationTimeInMillis(EXPIRATION_TIME).tokenClaims(claims).secret(SECRET)
                .userName(LOGGED_USER).build();
    }
}