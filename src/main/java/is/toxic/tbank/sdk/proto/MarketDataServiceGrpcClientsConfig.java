package is.toxic.tbank.sdk.proto;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.grpc.client.ImportGrpcClients;
import ru.tinkoff.piapi.contract.v1.MarketDataServiceGrpc;

@AutoConfiguration
@ImportGrpcClients(
    target = "t-bank",
    types = {
        MarketDataServiceGrpc.MarketDataServiceStub.class,
        MarketDataServiceGrpc.MarketDataServiceBlockingV2Stub.class,
        MarketDataServiceGrpc.MarketDataServiceBlockingStub.class,
        MarketDataServiceGrpc.MarketDataServiceFutureStub.class
    }
)
public class MarketDataServiceGrpcClientsConfig {
}
