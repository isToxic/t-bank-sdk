package is.toxic.tbank.sdk.config;

import is.toxic.tbank.sdk.resilience.ServiceResilienceProperties;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ServiceResiliencePropertiesValidationTest {

    @Test
    void rateLimiterLimitForPeriodMustBePositive() {
        assertThatThrownBy(() -> new ServiceResilienceProperties(0, 1, 5, 50f, 10, 30, 20))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rateLimiterTimeoutSecondsMustBePositive() {
        assertThatThrownBy(() -> new ServiceResilienceProperties(100, 1, 0, 50f, 10, 30, 20))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void circuitBreakerThresholdInRange() {
        assertThatThrownBy(() -> new ServiceResilienceProperties(100, 1, 5, 0f, 10, 30, 20))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new ServiceResilienceProperties(100, 1, 5, 101f, 10, 30, 20))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void circuitBreakerParamsMustBePositive() {
        assertThatThrownBy(() -> new ServiceResilienceProperties(100, 1, 5, 50f, 0, 30, 20))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new ServiceResilienceProperties(100, 1, 5, 50f, 10, 0, 20))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new ServiceResilienceProperties(100, 1, 5, 50f, 10, 30, 0))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
