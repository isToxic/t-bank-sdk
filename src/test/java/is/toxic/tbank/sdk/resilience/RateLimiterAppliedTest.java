package is.toxic.tbank.sdk.resilience;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.github.resilience4j.ratelimiter.RateLimiter;
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

class RateLimiterAppliedTest {

    private WireMockServer wireMockServer;

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
    }

    @AfterEach
    void tearDown() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @Test
    void rateLimiterConfiguredPerService() {
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
            Map<String, RateLimiter> rateLimiters = context.getBean("tinkoffServiceRateLimiters", Map.class);
            RateLimiter instrumentsRl = rateLimiters.get("instruments");
            assertThat(instrumentsRl).isNotNull();
            assertThat(instrumentsRl.getRateLimiterConfig().getLimitForPeriod()).isEqualTo(100);
        });
    }
}
