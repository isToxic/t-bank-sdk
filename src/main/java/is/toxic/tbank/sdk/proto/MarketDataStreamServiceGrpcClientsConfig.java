package is.toxic.tbank.sdk.proto;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.grpc.client.ImportGrpcClients;
import ru.tinkoff.piapi.contract.v1.MarketDataStreamServiceGrpc;

@AutoConfiguration
@ImportGrpcClients(
    target = "t-bank",
    types = {
        MarketDataStreamServiceGrpc.MarketDataStreamServiceStub.class,
        MarketDataStreamServiceGrpc.MarketDataStreamServiceBlockingV2Stub.class,
        MarketDataStreamServiceGrpc.MarketDataStreamServiceBlockingStub.class,
        MarketDataStreamServiceGrpc.MarketDataStreamServiceFutureStub.class
    }
)
public class MarketDataStreamServiceGrpcClientsConfig {
}
