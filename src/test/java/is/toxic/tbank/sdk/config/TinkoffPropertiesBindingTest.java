package is.toxic.tbank.sdk.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

class TinkoffPropertiesBindingTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(TestConfig.class)
            .withPropertyValues(
                    "platforms.t-bank.url=https://invest-public-api.tinkoff.ru/rest",
                    "platforms.t-bank.token=test-token-123"
            );

    @Test
    void bindsProperties() {
        contextRunner.run(context -> {
            TinkoffProperties props = context.getBean(TinkoffProperties.class);
            assertThat(props.url()).isEqualTo("https://invest-public-api.tinkoff.ru/rest");
            assertThat(props.token()).isEqualTo("test-token-123");
        });
    }

    @Configuration
    @EnableConfigurationProperties(TinkoffProperties.class)
    static class TestConfig {
    }
}
