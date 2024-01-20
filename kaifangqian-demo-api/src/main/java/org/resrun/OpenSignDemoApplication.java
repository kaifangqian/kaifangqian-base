package org.resrun;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;

@Slf4j
@RestController
@SpringBootApplication
public class OpenSignDemoApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext application = SpringApplication.run(OpenSignDemoApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = "127.0.0.1";
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        log.info("\n----------------------------------------------------------\n\t" +
                "Application kaifangqian-demo is running! Access URLs:\n\t" +
                "External: \thttp://" + ip + ":" + port + path + "/\n" +
                "----------------------------------------------------------");

    }

    @GetMapping("/")
    @ApiOperation(value = "项目根目录")
    public String index(){
        return "kaifangqian-demo run success";
    }

}