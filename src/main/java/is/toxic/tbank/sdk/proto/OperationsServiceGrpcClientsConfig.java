package is.toxic.tbank.sdk.proto;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.grpc.client.ImportGrpcClients;
import ru.tinkoff.piapi.contract.v1.OperationsServiceGrpc;

@AutoConfiguration
@ImportGrpcClients(
    target = "t-bank",
    types = {
        OperationsServiceGrpc.OperationsServiceStub.class,
        OperationsServiceGrpc.OperationsServiceBlockingV2Stub.class,
        OperationsServiceGrpc.OperationsServiceBlockingStub.class,
        OperationsServiceGrpc.OperationsServiceFutureStub.class
    }
)
public class OperationsServiceGrpcClientsConfig {
}
