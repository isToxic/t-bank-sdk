package is.toxic.tbank.sdk.rest;


import is.toxic.tbank.sdk.bot.api.*;
import is.toxic.tbank.sdk.config.TinkoffProperties;
import is.toxic.tbank.sdk.invoker.ApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@AutoConfiguration
@EnableConfigurationProperties(TinkoffProperties.class)
@RequiredArgsConstructor
public class TinkoffApiConfig {

    private final TinkoffProperties tinkoffProperties;
    private final RestClient tinkoffRestClient;

    @Bean
    public ApiClient tinkoffApiClient() {
        ApiClient apiClient = new ApiClient(tinkoffRestClient);
        apiClient.setBearerToken(tinkoffProperties.token());
        apiClient.setBasePath(tinkoffProperties.url());
        return apiClient;
    }

    @Bean
    public InstrumentsServiceApi instrumentsServiceApi(@Qualifier("tinkoffApiClient") ApiClient apiClient) {
        return new InstrumentsServiceApi(apiClient);
    }

    @Bean
    public MarketDataServiceApi marketDataServiceApi(@Qualifier("tinkoffApiClient") ApiClient apiClient) {
        return new MarketDataServiceApi(apiClient);
    }

    @Bean
    public StopOrdersServiceApi stopOrdersServiceApi(@Qualifier("tinkoffApiClient") ApiClient apiClient) {
        return new StopOrdersServiceApi(apiClient);
    }

    @Bean
    public OperationsServiceApi operationsServiceApi(@Qualifier("tinkoffApiClient") ApiClient apiClient) {
        return new OperationsServiceApi(apiClient);
    }

    @Bean
    public SignalServiceApi signalServiceApi(@Qualifier("tinkoffApiClient") ApiClient apiClient) {
        return new SignalServiceApi(apiClient);
    }

    @Bean
    public OrdersServiceApi ordersServiceApi(@Qualifier("tinkoffApiClient") ApiClient apiClient) {
        return new OrdersServiceApi(apiClient);
    }

    @Bean
    public SandboxServiceApi sandboxServiceApi(@Qualifier("tinkoffApiClient") ApiClient apiClient) {
        return new SandboxServiceApi(apiClient);
    }

    @Bean
    public UsersServiceApi usersServiceApi(@Qualifier("tinkoffApiClient") ApiClient apiClient) {
        return new UsersServiceApi(apiClient);
    }


}