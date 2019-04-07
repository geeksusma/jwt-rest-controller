package es.geeksusma.jwtrestcontroller.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.geeksusma.jwtrestcontroller.config.JwtPropertiesConfiguration;
import es.geeksusma.jwtrestcontroller.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final String CONTENT_TYPE = "application/json";
    private static final String TOKEN_BODY_PARAM = "token";
    private static final String TOKEN_USER_PARAM = "user";

    private final JwtService jwtService;
    private final JwtPropertiesConfiguration jwtPropertiesConfiguration;

    public JwtAuthenticationFilter(final JwtService jwtService
            , final JwtPropertiesConfiguration jwtPropertiesConfiguration) {
        this.jwtPropertiesConfiguration = jwtPropertiesConfiguration;
        this.jwtService = jwtService;
    }

    @Override
    public void setAuthenticationManager(final AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response)
            throws AuthenticationException {
        final FetchUserLogin user = extractCredentialsFromRequest(request);
        final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getUsername(),
                user.getPassword());
        return super.getAuthenticationManager().authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain,
                                            final Authentication authResult) throws IOException {

        final User user = new User(authResult.getName(), "", authResult.getAuthorities());
        final String token = jwtPropertiesConfiguration.getTokenPrefix() + " " + jwtService.create(authResult);

        createSuccessfulResponse(response, createBodyForRightNotificationAuthentication(user, token), token);

    }

    private Map<String, Object> createBodyForRightNotificationAuthentication(final User user,
                                                                             final String token) {
        final Map<String, Object> body = new HashMap<>();
        body.put(TOKEN_BODY_PARAM, token);
        body.put(TOKEN_USER_PARAM, user);
        return body;
    }

    private void createSuccessfulResponse(final HttpServletResponse response, final Map<String, Object> body, final String token)
            throws IOException {

        response.addHeader(jwtPropertiesConfiguration.getHeaderPrefix(), token);
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(CONTENT_TYPE);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    private FetchUserLogin extractCredentialsFromRequest(final HttpServletRequest request) {
        try {
            return new ObjectMapper().readValue(request.getInputStream(), FetchUserLogin.class);
        } catch (IOException e) {
            log.error("Request Input Error {}", e.getMessage());
            throw new AuthenticationServiceException("Unable to get the credentials to check from the request");
        }
    }
}
