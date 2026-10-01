package is.toxic.tbank.sdk.proto;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.grpc.client.ImportGrpcClients;
import ru.tinkoff.piapi.contract.v1.OrdersStreamServiceGrpc;

@AutoConfiguration
@ImportGrpcClients(
    target = "t-bank",
    types = {
        OrdersStreamServiceGrpc.OrdersStreamServiceStub.class,
        OrdersStreamServiceGrpc.OrdersStreamServiceBlockingV2Stub.class,
        OrdersStreamServiceGrpc.OrdersStreamServiceBlockingStub.class,
        OrdersStreamServiceGrpc.OrdersStreamServiceFutureStub.class
    }
)
public class OrdersStreamServiceGrpcClientsConfig {
}
