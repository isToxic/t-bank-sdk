package is.toxic.tbank.sdk.proto;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.grpc.client.ImportGrpcClients;
import ru.tinkoff.piapi.contract.v1.OperationsStreamServiceGrpc;

@AutoConfiguration
@ImportGrpcClients(
    target = "t-bank",
    types = {
        OperationsStreamServiceGrpc.OperationsStreamServiceStub.class,
        OperationsStreamServiceGrpc.OperationsStreamServiceBlockingV2Stub.class,
        OperationsStreamServiceGrpc.OperationsStreamServiceBlockingStub.class,
        OperationsStreamServiceGrpc.OperationsStreamServiceFutureStub.class
    }
)
public class OperationsStreamServiceGrpcClientsConfig {
}
