package is.toxic.tbank.sdk.rest;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import is.toxic.tbank.sdk.bot.api.InstrumentsServiceApi;
import is.toxic.tbank.sdk.config.TinkoffProperties;
import is.toxic.tbank.sdk.invoker.ApiClient;
import is.toxic.tbank.sdk.resilience.TinkoffResilience4jConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.Assertions.assertThat;

class ApiClientConfigurationTest {

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
    void apiClientUsesBasePathAndBearerToken() {
        ApplicationContextRunner contextRunner = new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(
                        TinkoffRestClientConfig.class,
                        TinkoffResilience4jConfig.class,
                        TinkoffApiConfig.class
                ))
                .withPropertyValues(
                        "platforms.t-bank.url=http://localhost:" + wireMockServer.port(),
                        "platforms.t-bank.token=test-bearer-token"
                );

        stubFor(get(urlPathMatching("/tinkoff.public.invest.api.contract.v1.InstrumentsService/.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{}")));

        contextRunner.run(context -> {
            ApiClient apiClient = context.getBean(ApiClient.class);
            assertThat(apiClient.getBasePath()).isEqualTo("http://localhost:" + wireMockServer.port());
            TinkoffProperties props = context.getBean(TinkoffProperties.class);
            assertThat(props.token()).isEqualTo("test-bearer-token");
            assertThat(context.getBean(InstrumentsServiceApi.class)).isNotNull();
        });
    }
}
