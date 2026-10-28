package is.toxic.tbank.sdk.config;

import is.toxic.tbank.sdk.resilience.ServiceResilienceProperties;
import is.toxic.tbank.sdk.resilience.TinkoffResilienceProperties;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class TinkoffResiliencePropertiesMergeTest {

    @Test
    void partialOverrideKeepsOthers() {
        Map<String, ServiceResilienceProperties> services = new LinkedHashMap<>();
        services.put("instruments", new ServiceResilienceProperties(200, 1, 5, 50f, 10, 30, 20));
        TinkoffResilienceProperties props = new TinkoffResilienceProperties(services, null);
        assertThat(props.services().get("instruments").rateLimiterLimitForPeriod()).isEqualTo(200);
        assertThat(props.pathMapping()).isNotEmpty();
    }
}
