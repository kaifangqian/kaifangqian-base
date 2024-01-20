package org.resrun.api;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@Slf4j
@RestController
public class OpenSignApiApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext application = SpringApplication.run(OpenSignApiApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = "127.0.0.1";
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        log.info("\n----------------------------------------------------------\n\t" +
                "Application kaifangqian-api is running! Access URLs:\n\t" +
                "External: \thttp://" + ip + ":" + port + path + "\n" +
                "----------------------------------------------------------");
    }

    @GetMapping("/")
    @ApiOperation(value = "项目根目录")
    public String index(){
        return "kaifangqian-api run success";
    }

}
