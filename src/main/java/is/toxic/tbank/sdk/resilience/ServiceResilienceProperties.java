package is.toxic.tbank.sdk.resilience;

public record ServiceResilienceProperties(
        int rateLimiterLimitForPeriod,
        int rateLimiterLimitRefreshPeriodMinutes,
        int rateLimiterTimeoutSeconds,
        float circuitBreakerFailureRateThreshold,
        int circuitBreakerMinimumNumberOfCalls,
        int circuitBreakerWaitDurationInSeconds,
        int circuitBreakerSlidingWindowSize
) {

    public ServiceResilienceProperties {
        if (rateLimiterLimitForPeriod <= 0) {
            throw new IllegalArgumentException("rateLimiterLimitForPeriod must be positive");
        }
        if (rateLimiterLimitRefreshPeriodMinutes <= 0) {
            throw new IllegalArgumentException("rateLimiterLimitRefreshPeriodMinutes must be positive");
        }
        if (rateLimiterTimeoutSeconds <= 0) {
            throw new IllegalArgumentException("rateLimiterTimeoutSeconds must be positive");
        }
        if (circuitBreakerFailureRateThreshold <= 0 || circuitBreakerFailureRateThreshold > 100) {
            throw new IllegalArgumentException("circuitBreakerFailureRateThreshold must be in (0, 100]");
        }
        if (circuitBreakerMinimumNumberOfCalls <= 0) {
            throw new IllegalArgumentException("circuitBreakerMinimumNumberOfCalls must be positive");
        }
        if (circuitBreakerWaitDurationInSeconds <= 0) {
            throw new IllegalArgumentException("circuitBreakerWaitDurationInSeconds must be positive");
        }
        if (circuitBreakerSlidingWindowSize <= 0) {
            throw new IllegalArgumentException("circuitBreakerSlidingWindowSize must be positive");
        }
    }
}