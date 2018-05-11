package goncharenko.gvv.jwt.cache;

import goncharenko.gvv.jwt.KeyPairBuilder;
import goncharenko.gvv.setting.Settings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({Settings.class})
@Slf4j
public class CacheKeysContext {
    @Bean
    public KeyPairBuilder keyPairBuilder() {
        return new KeyPairBuilder();
    }

    @Bean(initMethod="init", destroyMethod = "destroy")
    public CachedPairKeyService cachedKeyService(Settings settings, KeyPairBuilder keyPairBuilder) {
        return new DefaultCachedPairKeysService(settings, keyPairBuilder);
    }
}