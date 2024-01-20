# 开放签--接口API

## 1、配置application-prod.yml
```
kaifangqian:
  #证书服务地址
  cert-apply-url: https://localhost/service/cert/event
  #授权token
  token: 123456
  # 默认 false 签发本地测试证书 true 签发 CA 证书
  prod: false
```
## 2、启动项目
```
#需要提前准备好jdk1.8+,maven3.0+的环境

#进入项目目录
cd kaifangqian-api

#打包
mvn clean package

#启动项目  默认端口为8891
java -jar ./target/kaifangqian-api.jar
```

## 3、api接口
请阅读[开放签API接口文档](../docs/kaifangqian-doc.pdf)


