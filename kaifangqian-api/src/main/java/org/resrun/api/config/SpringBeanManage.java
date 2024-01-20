package org.resrun.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringBeanManage {

    @Bean
    @ConfigurationProperties(prefix = "kaifangqian")
    public ApiClientConfig clientConfig(){
        return new ApiClientConfig();
    }

}
