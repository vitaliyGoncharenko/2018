package goncharenko.gvv.jwt.cache

import goncharenko.gvv.jwt.KeyPairBuilder
import goncharenko.gvv.setting.Settings
import spock.lang.Specification

import java.security.KeyPair

class CachedKeyServiceTest extends Specification {
    Settings settings = new Settings(liveTimeSeconds: 1)
    KeyPairBuilder keyPairBuilder = Mock(KeyPairBuilder)
    DefaultCachedPairKeysService cachedPairKeyService

    def "should invoke resource with keys only one when cache isn't expired"() {
        given:
        def keyPair = GroovyMock(KeyPair)

        when:
        cachedPairKeyService = new DefaultCachedPairKeysService(settings, keyPairBuilder)
        cachedPairKeyService.startUpdater()
        cachedPairKeyService.getPrivateKey()
        cachedPairKeyService.getRSAPublicKey()
        cachedPairKeyService.getPrivateKey()
        cachedPairKeyService.getRSAPublicKey()

        then:
        1 * keyPairBuilder.generateKey() >> keyPair
        0 * _
    }

    def "should invoke resource with keys every time when cache is expired"() {
        given:
        def keyPair = GroovyMock(KeyPair)

        when:
        cachedPairKeyService = new DefaultCachedPairKeysService(settings, keyPairBuilder)
        cachedPairKeyService.startUpdater()
        sleep(1001)
        cachedPairKeyService.getPrivateKey()
        cachedPairKeyService.getRSAPublicKey()
        sleep(1001)
        cachedPairKeyService.getPrivateKey()
        cachedPairKeyService.getRSAPublicKey()

        then:
        3 * keyPairBuilder.generateKey() >> keyPair
        0 * _
    }

    def "should expired public keys"() {
        given:
        def keyPair = GroovyMock(KeyPair)

        when:
        cachedPairKeyService = new DefaultCachedPairKeysService(settings, keyPairBuilder)
        cachedPairKeyService.startUpdater()
        def res = cachedPairKeyService.getRSAPublicKey()
        sleep(2001)
        def res2 = cachedPairKeyService.getRSAPublicKey()

        then:
        3 * keyPairBuilder.generateKey() >> keyPair
        0 * _

        res.size() == 1
        res2.size() == 2
    }

    def cleanup() {
        cachedPairKeyService.stopUpdater()
    }
}