package is.toxic.tbank.sdk.rest;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import is.toxic.tbank.sdk.bot.api.InstrumentsServiceApi;
import is.toxic.tbank.sdk.resilience.TinkoffResilience4jConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.Assertions.assertThat;

class RestErrorHandlingTest {

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
    void contextNotBrokenOnHttpErrors() {
        stubFor(any(anyUrl())
                .willReturn(aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{}")));

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
            assertThat(context).hasNotFailed();
            assertThat(context.getBean(InstrumentsServiceApi.class)).isNotNull();
        });
    }
}
