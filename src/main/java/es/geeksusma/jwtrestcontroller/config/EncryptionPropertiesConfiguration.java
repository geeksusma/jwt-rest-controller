package es.geeksusma.jwtrestcontroller.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "encryption")
public class EncryptionPropertiesConfiguration {

    private String key;

}
