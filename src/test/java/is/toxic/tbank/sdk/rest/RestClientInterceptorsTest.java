package is.toxic.tbank.sdk.rest;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import is.toxic.tbank.sdk.bot.api.InstrumentsServiceApi;
import is.toxic.tbank.sdk.invoker.ApiClient;
import is.toxic.tbank.sdk.resilience.ResilienceClientHttpRequestInterceptor;
import is.toxic.tbank.sdk.resilience.TinkoffResilience4jConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.web.client.RestClient;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.Assertions.assertThat;

class RestClientInterceptorsTest {

    private WireMockServer wireMockServer;

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
    }

    @AfterEach
    void tearDown() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @Test
    void restClientHasResilienceInterceptor() {
        ApplicationContextRunner contextRunner = new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(
                        TinkoffRestClientConfig.class,
                        TinkoffResilience4jConfig.class,
                        TinkoffApiConfig.class
                ))
                .withPropertyValues(
                        "platforms.t-bank.url=http://localhost:" + wireMockServer.port(),
                        "platforms.t-bank.token=test-token"
                );

        contextRunner.run(context -> {
            RestClient restClient = context.getBean("tinkoffRestClient", RestClient.class);
            assertThat(restClient).isNotNull();
            assertThat(context.getBean(ResilienceClientHttpRequestInterceptor.class)).isNotNull();
            assertThat(context.getBean(InstrumentsServiceApi.class)).isNotNull();
            assertThat(context.getBean(ApiClient.class)).isNotNull();
        });
    }
}
