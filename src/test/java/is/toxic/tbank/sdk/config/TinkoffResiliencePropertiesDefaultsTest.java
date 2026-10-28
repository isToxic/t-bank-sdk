package is.toxic.tbank.sdk.config;

import is.toxic.tbank.sdk.resilience.ServiceResilienceProperties;
import is.toxic.tbank.sdk.resilience.TinkoffResilienceProperties;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TinkoffResiliencePropertiesDefaultsTest {

    @Test
    void loadsDefaultsWhenNull() {
        TinkoffResilienceProperties props = new TinkoffResilienceProperties(null, null);
        assertThat(props.services()).isNotEmpty();
        assertThat(props.services()).containsKeys("instruments", "users", "operations", "marketdata", "stoporders", "sandbox", "orders", "signal");
        assertThat(props.pathMapping()).isEqualTo(TinkoffResilienceProperties.DEFAULT_PATH_MAPPING);
        assertThat(props.services().get("instruments")).isEqualTo(TinkoffResilienceProperties.DEFAULT_SERVICES.get("instruments"));
    }

    @Test
    void defaultInstrumentsValues() {
        ServiceResilienceProperties p = TinkoffResilienceProperties.DEFAULT_SERVICES.get("instruments");
        assertThat(p.rateLimiterLimitForPeriod()).isEqualTo(100);
        assertThat(p.rateLimiterLimitRefreshPeriodMinutes()).isEqualTo(1);
        assertThat(p.rateLimiterTimeoutSeconds()).isEqualTo(5);
        assertThat(p.circuitBreakerFailureRateThreshold()).isEqualTo(50f);
        assertThat(p.circuitBreakerMinimumNumberOfCalls()).isEqualTo(10);
        assertThat(p.circuitBreakerWaitDurationInSeconds()).isEqualTo(30);
        assertThat(p.circuitBreakerSlidingWindowSize()).isEqualTo(20);
    }
}
