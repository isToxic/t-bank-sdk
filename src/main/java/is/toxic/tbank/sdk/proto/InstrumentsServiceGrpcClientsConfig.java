package is.toxic.tbank.sdk.proto;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.grpc.client.ImportGrpcClients;
import ru.tinkoff.piapi.contract.v1.InstrumentsServiceGrpc;

@AutoConfiguration
@ImportGrpcClients(
    target = "t-bank",
    types = {
        InstrumentsServiceGrpc.InstrumentsServiceStub.class,
        InstrumentsServiceGrpc.InstrumentsServiceBlockingV2Stub.class,
        InstrumentsServiceGrpc.InstrumentsServiceBlockingStub.class,
        InstrumentsServiceGrpc.InstrumentsServiceFutureStub.class
    }
)
public class InstrumentsServiceGrpcClientsConfig {
}
