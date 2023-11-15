package revolut.live.coding;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class LoadBalancerTest {

    @Test
    void shouldRegisterInstance() {
        // given
        LoadBalancer sut = new LoadBalancer();
        URI instanceUri = URI.create("http://www.revolut.com");

        // when
        sut.registerInstance(instanceUri);

        // then
        assertTrue(sut.isRegistered(instanceUri));
    }

    @Test
    void shouldNotRegisterDuplicateInstances() {
        // given
        LoadBalancer sut = new LoadBalancer();
        URI instanceUri = URI.create("http://www.revolut.com");
        sut.registerInstance(instanceUri);

        // when
        URI instanceUri2 = URI.create("http://www.revolut.com");
        sut.registerInstance(instanceUri2);

        // then
        assertEquals(1, sut.registeredInstanceCount());
    }

    @Test
    void shouldRegisterTenInstances() {
        // given
        LoadBalancer sut = new LoadBalancer();

        // when
        IntStream.range(1, 11).forEach(i -> {
            URI instanceUri = URI.create("http://www.revolut.com" + i);
            sut.registerInstance(instanceUri);
        });

        // then
        assertEquals(10, sut.registeredInstanceCount());
    }

    @Test
    void shouldThrowExceptionWhenRegisteringElevenInstances() {
        // given
        LoadBalancer sut = new LoadBalancer();
        IntStream.range(1, 11).forEach(i -> {
            URI instanceUri = URI.create("http://www.revolut.com" + i);
            sut.registerInstance(instanceUri);
        });

        // when
        URI instanceUri = URI.create("http://www.11revolut.com");
        var exception = assertThrows(LoadBalancerException.class, () -> sut.registerInstance(instanceUri));

        // then
        assertEquals("You can only register 10 backend instances in the Load Balancer", exception.getMessage());
    }
}
