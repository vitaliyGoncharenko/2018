package goncharenko.gvv.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JWTContext {

    @Bean
    public KeyPairBuilder keyPairBuilder() {
        return new KeyPairBuilder();
    }

    @Bean
    PublicKeysController publicKeysController() {
        return new PublicKeysController();
    }
}