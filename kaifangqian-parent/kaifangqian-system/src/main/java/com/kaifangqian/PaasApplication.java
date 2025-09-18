package com.kaifangqian;

import com.kaifangqian.modules.storage.config.StorageProperties;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author : zhenghuihan
 * create at:  2022/5/26  14:59
 * @description: 启动类
 *
 *
 */
@Slf4j
@SpringBootApplication(exclude = {
        /**
         * MongoAutoConfiguration.class, MongoDataAutoConfiguration.class,防止未配置Mongo启动报错，如果配置了请删这两个类
         */
        MongoAutoConfiguration.class, MongoDataAutoConfiguration.class,
        SecurityAutoConfiguration.class
})
@EnableTransactionManagement
@MapperScan({"com.kaifangqian.modules.**.mapper", "com.kaifangqian.mapper"})
@EnableConfigurationProperties(StorageProperties.class)
public class PaasApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PaasApplication.class);
    }

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(PaasApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        log.info("\n----------------------------------------------------------\n\t" +
                "Application Restrun-Paas is running! Access URLs:\n\t" +
                "External: \thttp://" + ip + ":" + port + path + "/\n\t" +
                "----------------------------------------------------------");
    }

}