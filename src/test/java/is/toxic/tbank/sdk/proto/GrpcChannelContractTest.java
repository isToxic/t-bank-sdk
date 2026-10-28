package is.toxic.tbank.sdk.proto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class GrpcChannelContractTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withPropertyValues(
                    "spring.grpc.client.channel.t-bank.target=static://invest-public-api.tinkoff.ru:443"
            );

    @Test
    void grpcChannelConfigured() {
        contextRunner.run(context -> assertThat(context).hasNotFailed());
    }
}
