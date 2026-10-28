package is.toxic.tbank.sdk.resilience;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PathMappingTest {

    @Test
    void mapsKnownPaths() throws Exception {
        Map<String, String> pathMapping = new LinkedHashMap<>();
        pathMapping.put("/instruments", "instruments");
        pathMapping.put("/marketdata", "marketdata");
        pathMapping.put("/operations", "operations");
        pathMapping.put("/orders", "orders");
        pathMapping.put("/sandbox", "sandbox");
        pathMapping.put("/users", "users");
        pathMapping.put("/stoporders", "stoporders");
        pathMapping.put("/signal", "signal");

        ResilienceClientHttpRequestInterceptor interceptor = new ResilienceClientHttpRequestInterceptor(
                Map.of(),
                Map.of(),
                pathMapping
        );

        Method m = ResilienceClientHttpRequestInterceptor.class.getDeclaredMethod("resolveServiceName", String.class);
        m.setAccessible(true);

        assertThat(m.invoke(interceptor, "/instruments")).isEqualTo("instruments");
        assertThat(m.invoke(interceptor, "/marketdata/candles")).isEqualTo("marketdata");
        assertThat(m.invoke(interceptor, "/orders/postOrder")).isEqualTo("orders");
        assertThat(m.invoke(interceptor, "/users/accounts")).isEqualTo("users");
        assertThat(m.invoke(interceptor, "/operations")).isEqualTo("operations");
        assertThat(m.invoke(interceptor, "/stoporders")).isEqualTo("stoporders");
        assertThat(m.invoke(interceptor, "/sandbox")).isEqualTo("sandbox");
        assertThat(m.invoke(interceptor, "/signal/strategies")).isEqualTo("signal");
        assertThat(m.invoke(interceptor, "/unknown/path")).isNull();
    }
}

