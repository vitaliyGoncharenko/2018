package goncharenko.gvv.jwt.cache;

import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

public interface CachedPairKeyService {
    List<RSAPublicKey> getRSAPublicKey();
    PrivateKey getPrivateKey();
}