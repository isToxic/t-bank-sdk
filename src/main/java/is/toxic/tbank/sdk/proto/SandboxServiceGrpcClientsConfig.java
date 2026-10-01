package is.toxic.tbank.sdk.proto;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.grpc.client.ImportGrpcClients;
import ru.tinkoff.piapi.contract.v1.SandboxServiceGrpc;

@AutoConfiguration
@ImportGrpcClients(
    target = "t-bank",
    types = {
        SandboxServiceGrpc.SandboxServiceStub.class,
        SandboxServiceGrpc.SandboxServiceBlockingV2Stub.class,
        SandboxServiceGrpc.SandboxServiceBlockingStub.class,
        SandboxServiceGrpc.SandboxServiceFutureStub.class
    }
)
public class SandboxServiceGrpcClientsConfig {
}
