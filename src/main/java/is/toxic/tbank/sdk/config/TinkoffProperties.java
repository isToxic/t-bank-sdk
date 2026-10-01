package is.toxic.tbank.sdk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "platforms.t-bank")
public record TinkoffProperties(String url, String token) {}
