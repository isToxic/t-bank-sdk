package is.toxic.tbank.sdk.autoconfig;

import is.toxic.tbank.sdk.proto.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class TinkoffGrpcAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    UsersServiceGrpcClientsConfig.class,
                    OperationsStreamServiceGrpcClientsConfig.class,
                    InstrumentsServiceGrpcClientsConfig.class,
                    OrdersStreamServiceGrpcClientsConfig.class,
                    MarketDataServiceGrpcClientsConfig.class,
                    OperationsServiceGrpcClientsConfig.class,
                    MarketDataStreamServiceGrpcClientsConfig.class,
                    StopOrdersServiceGrpcClientsConfig.class,
                    SandboxServiceGrpcClientsConfig.class,
                    OrdersServiceGrpcClientsConfig.class
            ))
            .withPropertyValues(
                    "spring.grpc.client.channel.t-bank.target=static://invest-public-api.tinkoff.ru:443"
            );

    @Test
    void grpcConfigsLoad() {
        contextRunner.run(context -> assertThat(context).hasNotFailed());
    }
}

