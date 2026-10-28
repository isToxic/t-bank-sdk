package is.toxic.tbank.sdk.resilience;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class ResilienceClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    private final Map<String, CircuitBreaker> circuitBreakers;
    private final Map<String, RateLimiter> rateLimiters;
    private final Map<String, String> pathMapping;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte [] body, ClientHttpRequestExecution execution) throws IOException {

        String path = request.getURI().getPath();
        String serviceName = resolveServiceName(path);
        if (serviceName == null) {
            return execution.execute(request, body);
        }

        CircuitBreaker cb = circuitBreakers == null ? null : circuitBreakers.get(serviceName);
        RateLimiter rl = rateLimiters == null ? null : rateLimiters.get(serviceName);

        Callable<ClientHttpResponse> callable = () -> execution.execute(request, body);

        if (cb != null) {
            callable = cb.decorateCallable(callable);
        }
        if (rl != null) {
            callable = RateLimiter.decorateCallable(rl, callable);
        }

        try {
            return callable.call();
        } catch (IOException | RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    private String resolveServiceName(String path) {
        if (path == null || pathMapping == null || pathMapping.isEmpty()) {
            return null;
        }
        for (Map.Entry<String, String> entry : pathMapping.entrySet()) {
            String key = entry.getKey();
            if (key != null && !key.isEmpty() && path.startsWith(key)) {
                return entry.getValue();
            }
        }
        return null;
    }
}
