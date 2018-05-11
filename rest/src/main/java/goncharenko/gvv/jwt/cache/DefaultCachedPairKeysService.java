package goncharenko.gvv.jwt.cache;

import goncharenko.gvv.jwt.KeyExpiration;
import goncharenko.gvv.jwt.KeyPairBuilder;
import goncharenko.gvv.setting.Settings;
import lombok.extern.slf4j.Slf4j;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.concurrent.DelayQueue;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

@Slf4j
public class DefaultCachedPairKeysService implements CachedPairKeyService {
    private DelayQueue<KeyExpiration> publicKeysQueue;
    private DelayQueue<KeyExpiration> privateKeysQueue;
    private List<RSAPublicKey> publicKeys;
    private PrivateKey privateKey;
    private Thread updaterKeys;
    private Thread updaterPublicKeys;
    private boolean running = true;

    private Settings settings;
    private KeyPairBuilder keyPairBuilder;

    public DefaultCachedPairKeysService(Settings settings, KeyPairBuilder keyPairBuilder) {
        this.settings = settings;
        this.keyPairBuilder = keyPairBuilder;
        this.publicKeysQueue = new DelayQueue<>();
        this.privateKeysQueue = new DelayQueue<>();
        initializePublicKeys();
    }

    private void init() {
        startUpdater();
    }

    private void destroy() {
        stopUpdater();
    }

    private void initializePublicKeys() {
        updateKeys();
    }

    private void putToQueue(KeyPair keyPair) {
        publicKeysQueue.put(new KeyExpiration(settings.liveTimeSeconds * 2, keyPair.getPublic()));
        updaterPublicKeys();
        privateKeysQueue.put(new KeyExpiration(settings.liveTimeSeconds, keyPair.getPrivate()));
    }

    private void updaterPublicKeys() {
        publicKeys = stream(publicKeysQueue.spliterator(), false)
                .map(k -> (RSAPublicKey) k.key)
                .collect(toList());
    }

    @Override
    public List<RSAPublicKey> getRSAPublicKey() {
        return publicKeys;
    }

    @Override
    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    private void startUpdater() {
        log.debug("Start public keys updater");
        updaterKeys = new Thread(this::updatingKeys);
        updaterPublicKeys = new Thread(this::expirePublicKeys);
        updaterKeys.start();
        updaterPublicKeys.start();
    }

    private void stopUpdater() {
        log.debug("Stop keys updater");
        running = false;
        updaterKeys.interrupt();
        updaterPublicKeys.interrupt();
    }

    private void updatingKeys() {
        while (running) {
            try {
                privateKeysQueue.take();
            } catch (InterruptedException e) {
                log.debug("Updating keys is interrupted");
            }
            if (running) {
                updateKeys();
            }
        }
    }

    private void expirePublicKeys() {
        while (running) {
            try {
                publicKeysQueue.take();
            } catch (InterruptedException e) {
                log.debug("Expire public keys is interrupted");
            }
        }
    }

    private void updateKeys() {
        KeyPair keyPair = keyPairBuilder.generateKey();
        putToQueue(keyPair);

        privateKey = keyPair.getPrivate();
    }
}
