package is.toxic.tbank.sdk.proto;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.grpc.client.ImportGrpcClients;
import ru.tinkoff.piapi.contract.v1.StopOrdersServiceGrpc;

@AutoConfiguration
@ImportGrpcClients(
    target = "t-bank",
    types = {
        StopOrdersServiceGrpc.StopOrdersServiceStub.class,
        StopOrdersServiceGrpc.StopOrdersServiceBlockingV2Stub.class,
        StopOrdersServiceGrpc.StopOrdersServiceBlockingStub.class,
        StopOrdersServiceGrpc.StopOrdersServiceFutureStub.class
    }
)
public class StopOrdersServiceGrpcClientsConfig {
}
