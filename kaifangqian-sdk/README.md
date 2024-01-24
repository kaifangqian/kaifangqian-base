# 开放签电子签章系统-sdk

## 1、准备接入
```
#需要提前准备好jdk1.8+,maven3.0+的环境

#进入项目目录
cd kaifangqian-sdk

#打包
mvn clean package

#将jar导入本地maven仓库   可根据实际情况放入maven私服或者直接copy源码至项目中
mvn install:install-file -Dfile=target/kaifangqian-sdk.jar -DartifactId=kaifangqian-sdk -DgroupId=org.resrun -Dversion=1.1 -Dpackaging=jar
```


## 2、系统集成

### 2.1 引入kaifangqian-sdk.jar
```
<dependency>
    <groupId>org.resrun</groupId>
    <artifactId>kaifangqian-sdk</artifactId>
    <version>1.1</version>
</dependency>
```

### 2.2 Spring集成
```
第一步
<!-- applicationContext.xml中新增如下配置 -->
<bean id="clientConfig" class="org.resrun.sdk.config.SDKClientConfig">
 <!-- 默认 false 签发本地测试证书 true 签发 CA 证书 -->
 <property name="prod" value="false" />
 <!-- 授权token -->
 <property name="token" value="${token}" />
 <!-- 证书签发路径 -->
 <property name="certApplyUrl" value="${certApplyUrl}" />
</bean>

第二步 applicationContext.xml增加扫描org.resrun 这个包
<context:component-scan base-package="com.example,org.resrun"/>

```

### 2.3 SpringBoot集成
```
第一步
# application.yml 中新增如下配置， 在调用正式CA证书时， 请更新以下配置
kaifangqian:
  #证书服务地址
  cert-apply-url: https://localhost/service/cert/event
  #授权token
  token: 123456
  # 默认 false 签发本地测试证书 true 签发 CA 证书
  prod: false
  
第二步
// 初始化SDKClientConfig
@Configuration
public class SpringBeanManage {
    @Bean
    @ConfigurationProperties(prefix = "kaifangqian")
    public SDKClientConfig clientConfig(){
        return new SDKClientConfig();
    }
}
第三步
// 启动类上增加扫描org.resrun 这个包
@SpringBootApplication(scanBasePackages = {"org.resrun"})
public class Application {
    public static void main(String[] args) {
        ...
    }
}
```
### 2.3 SDK依赖
```
<dependency>
    <!-- 版本根据自身spring-boot而定 -->
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-autoconfigure</artifactId>
</dependency>
<!-- pdf签名 -->
<dependency>
    <groupId>org.apache.pdfbox</groupId>
    <artifactId>pdfbox-tools</artifactId>
    <version>3.0.1</version>
</dependency>
<!-- 加密相关 -->
<dependency>
    <groupId>org.bouncycastle</groupId>
    <artifactId>bcpkix-jdk15on</artifactId>
    <version>1.70</version>
</dependency>
<dependency>
    <groupId>org.bouncycastle</groupId>
    <artifactId>bcprov-ext-jdk15on</artifactId>
    <version>1.70</version>
</dependency>
<!-- http工具类 -->
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
    <version>4.5.13</version>
</dependency>
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpcore</artifactId>
    <version>4.4.13</version>
</dependency>
<!-- Json处理-->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.83</version>
</dependency>
```



## 3、调用

```
//注入sdkService
@Autowired
private SDKService sdkService;

//调用证书颁发参数与API请求参数一致，请参照对应的API请求报文
sdkService.certEvent(certEventRequest)

//调用证书颁发参数与API请求参数一致，请参照对应的API请求报文
sdkService.documentSign(DocumentSignRequest)
```

