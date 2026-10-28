package is.toxic.tbank.sdk.autoconfig;

import is.toxic.tbank.sdk.rest.TinkoffRestClientConfig;
import is.toxic.tbank.sdk.resilience.TinkoffResilience4jConfig;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

class TinkoffRestClientConfigTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    TinkoffRestClientConfig.class,
                    TinkoffResilience4jConfig.class
            ));

    @Test
    void createsRestClientInfrastructure() {
        contextRunner.run(context -> {
            assertThat(context).hasBean("tinkoffRestClient");
            assertThat(context).getBean("tinkoffRestClient").isInstanceOf(RestClient.class);
            assertThat(context).hasBean("tinkoffHttpClient");
            assertThat(context).getBean("tinkoffHttpClient").isInstanceOf(HttpClient.class);
            assertThat(context).hasBean("tinkoffClientHttpRequestFactory");
            assertThat(context).getBean("tinkoffClientHttpRequestFactory").isInstanceOf(HttpComponentsClientHttpRequestFactory.class);
            assertThat(context).hasBean("tinkoffRequestConfig");
            assertThat(context).getBean("tinkoffRequestConfig").isInstanceOf(RequestConfig.class);
            assertThat(context).hasBean("tinkoffPoolingHttpClientConnectionManager");
            assertThat(context).getBean("tinkoffPoolingHttpClientConnectionManager").isInstanceOf(PoolingHttpClientConnectionManager.class);
        });
    }
}

