package is.toxic.tbank.sdk.autoconfig;

import is.toxic.tbank.sdk.bot.api.*;
import is.toxic.tbank.sdk.invoker.ApiClient;
import is.toxic.tbank.sdk.rest.TinkoffApiConfig;
import is.toxic.tbank.sdk.rest.TinkoffRestClientConfig;
import is.toxic.tbank.sdk.resilience.TinkoffResilience4jConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class TinkoffRestAutoConfigurationTest {

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
    void createsAllApiBeans() {
        contextRunner.run(context -> {
            assertThat(context).hasNotFailed();
            assertThat(context).getBean(ApiClient.class).isNotNull();
            assertThat(context).getBean(InstrumentsServiceApi.class).isNotNull();
            assertThat(context).getBean(MarketDataServiceApi.class).isNotNull();
            assertThat(context).getBean(StopOrdersServiceApi.class).isNotNull();
            assertThat(context).getBean(OperationsServiceApi.class).isNotNull();
            assertThat(context).getBean(SignalServiceApi.class).isNotNull();
            assertThat(context).getBean(OrdersServiceApi.class).isNotNull();
            assertThat(context).getBean(SandboxServiceApi.class).isNotNull();
            assertThat(context).getBean(UsersServiceApi.class).isNotNull();
        });
    }
}


