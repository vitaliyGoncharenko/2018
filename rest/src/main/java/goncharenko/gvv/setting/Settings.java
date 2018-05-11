package goncharenko.gvv.setting;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
@Data
public class Settings {
    public int liveTimeSeconds;
}