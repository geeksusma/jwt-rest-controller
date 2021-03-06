package es.geeksusma.jwtrestcontroller.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "security.jwt")
public class JwtPropertiesConfiguration {

    private String secret;
    private Long expirationTime;
    private String tokenPrefix;
    private String headerPrefix;
    private String authorities;

}
