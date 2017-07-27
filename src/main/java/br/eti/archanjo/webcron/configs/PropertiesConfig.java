package br.eti.archanjo.webcron.configs;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "webcron")
public class PropertiesConfig {
    private HttpLimits httplimits;
    private S3 s3;
    private String applicationUrl;
    private Logging logging;

    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @Getter
    @Setter
    public static class HttpLimits {
        private int limit;
        private int page;
    }

    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @Getter
    @Setter
    public static class Logging {
        private String folder;
    }

    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @Getter
    @Setter
    public static class S3 {
        private String bucket;
        private String accessKey;
        private String secretKey;
        private String cacheControl = "max-age=2592000,public";
        private String url = "s3.amazonaws.com";
        private Integer maxImageWidth;
        private Integer maxImageHeight;

    }
}