package is.toxic.tbank.sdk.proto;

import is.toxic.tbank.sdk.rest.TinkoffApiConfig;
import is.toxic.tbank.sdk.rest.TinkoffRestClientConfig;
import is.toxic.tbank.sdk.resilience.TinkoffResilience4jConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class GrpcAutoConfigIsolationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    TinkoffRestClientConfig.class,
                    TinkoffResilience4jConfig.class,
                    TinkoffApiConfig.class
            ))
            .withPropertyValues(
                    "platforms.t-bank.url=https://invest-public-api.tinkoff.ru/rest",
                    "platforms.t-bank.token=test-token"
            );

    @Test
    void restWorksWithoutGrpcConfigs() {
        contextRunner.run(context -> {
            assertThat(context).hasNotFailed();
            assertThat(context).hasBean("tinkoffApiClient");
        });
    }
}
