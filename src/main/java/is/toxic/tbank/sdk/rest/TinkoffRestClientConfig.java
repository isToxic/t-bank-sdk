package is.toxic.tbank.sdk.rest;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.config.TlsConfig;
import org.apache.hc.client5.http.impl.DefaultClientConnectionReuseStrategy;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.http.ssl.TLS;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import is.toxic.tbank.sdk.resilience.ResilienceClientHttpRequestInterceptor;

@AutoConfiguration
public class TinkoffRestClientConfig {

    @Bean
    public RestClient tinkoffRestClient(
            @Qualifier("tinkoffClientHttpRequestFactory") HttpComponentsClientHttpRequestFactory clientHttpRequestFactory,
            ResilienceClientHttpRequestInterceptor resilienceInterceptor) {
        return RestClient.builder()
                .requestFactory(clientHttpRequestFactory)
                .requestInterceptors(resilienceInterceptor)
                .build();
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory tinkoffClientHttpRequestFactory(@Qualifier("tinkoffHttpClient") HttpClient httpClient) {
        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }

    @Bean
    public HttpClient tinkoffHttpClient(@Qualifier("tinkoffRequestConfig") RequestConfig requestConfig,
                                         @Qualifier("tinkoffPoolingHttpClientConnectionManager") PoolingHttpClientConnectionManager httpClientConnectionManager) {
        return HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(httpClientConnectionManager)
                .setConnectionReuseStrategy(DefaultClientConnectionReuseStrategy.INSTANCE)
                .setRetryStrategy(new DefaultHttpRequestRetryStrategy(3, Timeout.ofMilliseconds(100)))
                .build();
    }

    @Bean
    public RequestConfig tinkoffRequestConfig() {
        return RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.ofSeconds(60))
                .build();
    }

    @Bean
    public PoolingHttpClientConnectionManager tinkoffPoolingHttpClientConnectionManager(
            @Qualifier("tinkoffConnectionConfig") ConnectionConfig connectionConfig,
            @Qualifier("tinkoffTlsConfig") TlsConfig tlsConfig) {
        return PoolingHttpClientConnectionManagerBuilder.create()
                .setDefaultConnectionConfig(connectionConfig)
                .setMaxConnPerRoute(50)
                .setMaxConnTotal(400)
                .setDefaultTlsConfig(tlsConfig)
                .setConnPoolPolicy(PoolReusePolicy.FIFO)
                .build();
    }

    @Bean
    public ConnectionConfig tinkoffConnectionConfig() {
        return ConnectionConfig.custom()
                .build();
    }

    @Bean
    public TlsConfig tinkoffTlsConfig() {
        return TlsConfig.custom()
                .setHandshakeTimeout(Timeout.ofSeconds(30))
                .setSupportedProtocols(TLS.V_1_2, TLS.V_1_3)
                .build();
    }

}
