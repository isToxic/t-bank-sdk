package is.toxic.tbank.sdk.autoconfig;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RateLimiter;
import is.toxic.tbank.sdk.resilience.ResilienceClientHttpRequestInterceptor;
import is.toxic.tbank.sdk.resilience.TinkoffResilience4jConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class TinkoffResilienceAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(TinkoffResilience4jConfig.class));

    @Test
    void createsResilienceBeans() {
        contextRunner.run(context -> {
            assertThat(context).hasNotFailed();
            assertThat(context).getBean("tinkoffServiceCircuitBreakers").isNotNull();
            assertThat(context).getBean("tinkoffServiceRateLimiters").isNotNull();
            assertThat(context).getBean(ResilienceClientHttpRequestInterceptor.class).isNotNull();
            @SuppressWarnings("unchecked")
            Map<String, CircuitBreaker> cbs = context.getBean("tinkoffServiceCircuitBreakers", Map.class);
            @SuppressWarnings("unchecked")
            Map<String, RateLimiter> rls = context.getBean("tinkoffServiceRateLimiters", Map.class);
            assertThat(cbs).containsKeys("instruments", "orders");
            assertThat(rls).containsKeys("instruments", "orders");
        });
    }
}


