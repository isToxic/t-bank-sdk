package is.toxic.tbank.sdk.resilience;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorator.Decorators;
import io.github.resilience4j.ratelimiter.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.Map;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class ResilienceClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    private final Map<String, CircuitBreaker> circuitBreakers;
    private final Map<String, RateLimiter> rateLimiters;
    private final Map<String, String> pathMapping;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        String serviceName = resolveServiceName(request.getURI().getPath());
        if (serviceName == null) {
            return execution.execute(request, body);
        }

        CircuitBreaker cb = circuitBreakers.get(serviceName);
        RateLimiter rl = rateLimiters.get(serviceName);

        Supplier<ClientHttpResponse> supplier = () -> {
            try {
                return execution.execute(request, body);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        };

        Supplier<ClientHttpResponse> decorated = Decorators.ofSupplier(supplier)
                .withCircuitBreaker(cb)
                .withRateLimiter(rl)
                .get();

        try {
            return decorated.get();
        } catch (UncheckedIOException e) {
            throw e.getCause();
        }
    }

    private String resolveServiceName(String path) {
        if (path == null) {
            return null;
        }
        for (var entry : pathMapping.entrySet()) {
            if (path.startsWith(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }
}