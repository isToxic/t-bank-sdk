package is.toxic.tbank.sdk.resilience;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import is.toxic.tbank.sdk.rest.TinkoffApiConfig;
import is.toxic.tbank.sdk.rest.TinkoffRestClientConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.util.Map;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.Assertions.assertThat;

class CircuitBreakerAppliedTest {

    private WireMockServer wireMockServer;

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();
    }

    @AfterEach
    void tearDown() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @Test
    void circuitBreakerConfigured() {
        ApplicationContextRunner contextRunner = new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(
                        TinkoffRestClientConfig.class,
                        TinkoffResilience4jConfig.class,
                        TinkoffApiConfig.class
                ))
                .withPropertyValues(
                        "platforms.t-bank.url=http://localhost:" + wireMockServer.port(),
                        "platforms.t-bank.token=test-token"
                );

        contextRunner.run(context -> {
            @SuppressWarnings("unchecked")
            Map<String, CircuitBreaker> cbs = context.getBean("tinkoffServiceCircuitBreakers", Map.class);
            CircuitBreaker cb = cbs.get("instruments");
            assertThat(cb).isNotNull();
            assertThat(cb.getCircuitBreakerConfig().getFailureRateThreshold()).isEqualTo(50f);
            assertThat(cb.getCircuitBreakerConfig().getMinimumNumberOfCalls()).isEqualTo(10);
            assertThat(cb.getCircuitBreakerConfig().getSlidingWindowSize()).isEqualTo(20);
        });
    }
}
