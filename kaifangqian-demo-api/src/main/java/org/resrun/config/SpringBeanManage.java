package org.resrun.config;

import org.resrun.sdk.config.SDKClientConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringBeanManage {
    @Bean
    @ConfigurationProperties(prefix = "kaifangqian")
    public SDKClientConfig clientConfig(){
        return new SDKClientConfig();
    }
}
