package es.geeksusma.jwtrestcontroller.login;

import es.geeksusma.jwtrestcontroller.arch.exception.InvalidTokenException;
import es.geeksusma.jwtrestcontroller.config.JwtPropertiesConfiguration;
import es.geeksusma.jwtrestcontroller.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final JwtService jwtService;
    private final JwtPropertiesConfiguration jwtPropertiesConfiguration;

    public JwtAuthorizationFilter(final AuthenticationManager authenticationManager, final JwtService jwtService, final JwtPropertiesConfiguration jwtPropertiesConfiguration) {
        super(authenticationManager);
        this.jwtService = jwtService;
        this.jwtPropertiesConfiguration = jwtPropertiesConfiguration;
    }


    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
            throws IOException, ServletException {

        final String token = request.getHeader(jwtPropertiesConfiguration.getHeaderPrefix());

        if (jwtService.validate(token)) {
            UsernamePasswordAuthenticationToken authentication;
            try {
                authentication = new UsernamePasswordAuthenticationToken(jwtService.getUsername(token), null,
                        null);
            } catch (InvalidTokenException e) {
                throw new IOException(e.getMessage());
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

}
