package com.kaifangqian.modules.system.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "qcloud")
public class QCloudProperties {
    private String version;
    private management manage;

    @Data
    public static class management {
        private String accessTokenUrl;
        private String apiTicketUrl;
        private String getAdvFaceUrl;
        private String queryFaceRecordUrl;
        private String queryPhotoInfoUrl;
    }
}

