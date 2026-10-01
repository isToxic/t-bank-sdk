package is.toxic.tbank.sdk.resilience;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

@AutoConfiguration
@EnableConfigurationProperties(TinkoffResilienceProperties.class)
@RequiredArgsConstructor
public class TinkoffResilience4jConfig {

    private final TinkoffResilienceProperties props;

    @Bean
    public Map<String, CircuitBreaker> tinkoffServiceCircuitBreakers() {
        Map<String, CircuitBreaker> map = new LinkedHashMap<>();
        for (var entry : props.getServices().entrySet()) {
            ServiceResilienceProperties p = entry.getValue();
            CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                    .failureRateThreshold(p.circuitBreakerFailureRateThreshold())
                    .minimumNumberOfCalls(p.circuitBreakerMinimumNumberOfCalls())
                    .waitDurationInOpenState(Duration.ofSeconds(p.circuitBreakerWaitDurationInSeconds()))
                    .slidingWindowSize(p.circuitBreakerSlidingWindowSize())
                    .build();
            map.put(entry.getKey(), CircuitBreaker.of("tinkoff-" + entry.getKey(), config));
        }
        return map;
    }

    @Bean
    public Map<String, RateLimiter> tinkoffServiceRateLimiters() {
        Map<String, RateLimiter> map = new LinkedHashMap<>();
        for (var entry : props.getServices().entrySet()) {
            ServiceResilienceProperties p = entry.getValue();
            RateLimiterConfig config = RateLimiterConfig.custom()
                    .limitRefreshPeriod(Duration.ofMinutes(p.rateLimiterLimitRefreshPeriodMinutes()))
                    .limitForPeriod(p.rateLimiterLimitForPeriod())
                    .timeout(Duration.ofSeconds(p.rateLimiterTimeoutSeconds()))
                    .build();
            map.put(entry.getKey(), RateLimiter.of("tinkoff-" + entry.getKey(), config));
        }
        return map;
    }

    @Bean
    public ResilienceClientHttpRequestInterceptor tinkoffResilienceInterceptor(
            Map<String, CircuitBreaker> circuitBreakers,
            Map<String, RateLimiter> rateLimiters) {
        return new ResilienceClientHttpRequestInterceptor(circuitBreakers, rateLimiters, props.getPathMapping());
    }
}