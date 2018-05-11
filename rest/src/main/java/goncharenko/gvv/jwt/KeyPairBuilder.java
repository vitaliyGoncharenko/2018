package goncharenko.gvv.jwt;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class KeyPairBuilder {
    public KeyPair generateKey() {
        KeyPairGenerator keyGenerator = getKeyPairGenerator();
        keyGenerator.initialize(1024);
        return keyGenerator.genKeyPair();
    }

    private static KeyPairGenerator getKeyPairGenerator() {
        try {
            return KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
}