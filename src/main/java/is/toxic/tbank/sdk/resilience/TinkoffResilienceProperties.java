package is.toxic.tbank.sdk.resilience;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "platforms.t-bank.resilience")
public record TinkoffResilienceProperties(
        Map<String, ServiceResilienceProperties> services,
        Map<String, String> pathMapping
) {

    public static final Map<String, ServiceResilienceProperties> DEFAULT_SERVICES = createDefaults();

    public static final Map<String, String> DEFAULT_PATH_MAPPING = createPathMapping();

    private static Map<String, ServiceResilienceProperties> createDefaults() {
        Map<String, ServiceResilienceProperties> map = new LinkedHashMap<>();
        map.put("instruments", new ServiceResilienceProperties(100, 1, 5, 50f, 10, 30, 20));
        map.put("users", new ServiceResilienceProperties(50, 1, 5, 50f, 10, 30, 20));
        map.put("operations", new ServiceResilienceProperties(100, 1, 5, 50f, 10, 30, 20));
        map.put("marketdata", new ServiceResilienceProperties(150, 1, 5, 50f, 10, 30, 20));
        map.put("stoporders", new ServiceResilienceProperties(25, 1, 5, 50f, 10, 30, 20));
        map.put("sandbox", new ServiceResilienceProperties(100, 1, 5, 50f, 10, 30, 20));
        map.put("orders", new ServiceResilienceProperties(50, 1, 5, 50f, 10, 30, 20));
        map.put("signal", new ServiceResilienceProperties(50, 1, 5, 50f, 10, 30, 20));
        return map;
    }

    private static Map<String, String> createPathMapping() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("/instruments", "instruments");
        map.put("/marketdata", "marketdata");
        map.put("/operations", "operations");
        map.put("/orders", "orders");
        map.put("/sandbox", "sandbox");
        map.put("/users", "users");
        map.put("/stoporders", "stoporders");
        map.put("/signal", "signal");
        return map;
    }

    public TinkoffResilienceProperties {
        if (services == null) {
            services = new LinkedHashMap<>(DEFAULT_SERVICES);
        }
        if (pathMapping == null) {
            pathMapping = new LinkedHashMap<>(DEFAULT_PATH_MAPPING);
        }
    }

    public Map<String, ServiceResilienceProperties> getServices() {
        return services;
    }

    public Map<String, String> getPathMapping() {
        return pathMapping;
    }
}
