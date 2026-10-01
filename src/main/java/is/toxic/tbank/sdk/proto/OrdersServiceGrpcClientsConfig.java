package is.toxic.tbank.sdk.proto;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.grpc.client.ImportGrpcClients;
import ru.tinkoff.piapi.contract.v1.OrdersServiceGrpc;

@AutoConfiguration
@ImportGrpcClients(
    target = "t-bank",
    types = {
        OrdersServiceGrpc.OrdersServiceStub.class,
        OrdersServiceGrpc.OrdersServiceBlockingV2Stub.class,
        OrdersServiceGrpc.OrdersServiceBlockingStub.class,
        OrdersServiceGrpc.OrdersServiceFutureStub.class
    }
)
public class OrdersServiceGrpcClientsConfig {
}
