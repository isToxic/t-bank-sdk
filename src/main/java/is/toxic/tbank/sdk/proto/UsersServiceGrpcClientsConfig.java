package is.toxic.tbank.sdk.proto;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.grpc.client.ImportGrpcClients;
import ru.tinkoff.piapi.contract.v1.UsersServiceGrpc;

@AutoConfiguration
@ImportGrpcClients(
    target = "t-bank",
    types = {
        UsersServiceGrpc.UsersServiceStub.class,
        UsersServiceGrpc.UsersServiceBlockingV2Stub.class,
        UsersServiceGrpc.UsersServiceBlockingStub.class,
        UsersServiceGrpc.UsersServiceFutureStub.class
    }
)
public class UsersServiceGrpcClientsConfig {
}
