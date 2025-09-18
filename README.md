# 开放签电子合同系统

## 项目简介
开放签是一个电子合同签署平台，提供合同创建、签署、管理及验证等完整解决方案。系统支持个人与企业实名认证、电子印章管理、合同模板配置、签署流程控制等功能，适用于各类需要电子签约的业务场景。

## 功能特性
- **合同生命周期管理**：支持合同创建、签署、审批、归档及作废等全流程管理。
- **实名认证**：集成云盾认证服务，支持个人与企业实名认证。
- **电子印章管理**：提供个人签名与企业印章的生成、启用、禁用、删除等操作。
- **签署意愿确认**：支持短信、邮件、签约密码等多种意愿确认方式。
- **合同模板**：支持模板创建、控件配置、模板授权及模板使用。
- **签署流程控制**：支持多签署方顺序签署、填写控件配置、签署权限控制等。
- **文件处理**：支持PDF文件转换、加密、水印添加及验签功能。
- **数据权限控制**：基于RBAC模型的数据权限管理，支持细粒度的数据访问控制。
- **审计日志**：完整记录合同签署、印章使用、用户操作等日志，便于追踪与审计。

## 技术架构
- **后端框架**：Spring Boot + MyBatis Plus + Shiro
- **数据库**：MySQL
- **缓存**：Redis
- **消息队列**：Redis MQ
- **安全**：JWT鉴权、RSA/SM2加密、接口签名验证
- **部署**：Docker容器化部署，支持JRE 8环境

## 模块说明
- **API模块**：提供对外接口服务，支持合同创建、签署、查询等操作。
- **认证模块**：集成云盾实名认证服务，支持个人与企业认证。
- **签署服务模块**：处理合同签署流程、签署控件配置、签署意愿确认等。
- **印章管理模块**：支持个人签名与企业印章的生成、授权、使用及审计。
- **模板管理模块**：合同模板的创建、配置、授权及使用。
- **文件处理模块**：支持PDF文件转换、加密、验签及下载。
- **权限控制模块**：基于RBAC模型的数据权限控制，支持接口权限与数据权限分离。
- **日志与审计模块**：记录用户操作、合同签署、印章使用等关键操作日志。

## 使用说明
### 合同签署流程
1. **实名认证**：用户需完成个人或企业实名认证。
2. **创建合同**：通过API或前端界面创建合同，配置签署方、签署控件、签署顺序等。
3. **发起签署**：发起合同签署流程，系统生成签署任务。
4. **签署意愿确认**：签署方通过短信、邮件或签约密码确认签署意愿。
5. **签署操作**：签署方完成签署操作，系统记录签署信息。
6. **合同归档**：签署完成后合同自动归档，支持下载与验签。

### 接口调用示例
#### 创建合同
```java
@PostMapping("/contract/draft")
public ApiCommonRes<SignContract> contractDraft(@RequestBody ContractDraftRequest request)
```
请求参数：
```json
{
  "subject": "服务协议",
  "signerList": [
    {
      "signerType": "1",
      "signerName": "张三",
      "receiver": {
        "name": "张三",
        "contactType": "1",
        "contact": "13800000000"
      },
      "positionParamList": [
        {
          "controlType": "sign",
          "signPositionType": "keyword",
          "keyword": "签署处",
          "offsetX": "100",
          "offsetY": "200"
        }
      ]
    }
  ]
}
```
响应示例：
```json
{
  "code": 200,
  "message": "成功",
  "result": {
    "contractId": "123456"
  }
}
```

## 安全与权限
- **接口鉴权**：使用JWT Token进行接口鉴权，支持RSA/SM2签名验证。
- **数据权限**：基于RBAC模型的数据权限控制，支持字段级与行级权限。
- **签署密码**：签署操作需通过签约密码或意愿确认机制进行二次验证。
- **审计日志**：所有关键操作均记录日志，便于追踪与审计。

## 部署说明
### Docker部署
使用提供的Dockerfile进行容器化部署：
```dockerfile
FROM registry.cn-beijing.aliyuncs.com/kaifangqian/eclipse-temurin:8u462-b08-jre-noble
COPY file/simsun.ttc /usr/share/fonts/chinese
COPY file/simkai.ttf /usr/share/fonts/chinese
COPY file/simhei.ttf /usr/share/fonts/chinese
COPY file/simfang.ttf /usr/share/fonts/chinese
ENV TZ=Asia/Shanghai
COPY file /home/kaifangqian/file
ENTRYPOINT ["sh", "-c", "exec java -jar -Dspring.profiles.active=prod -Dproject.name=kaifangqian kaifangqian.jar"]
```

### 配置文件
- `application.yml`：主配置文件，包含数据库、Redis、接口签名等配置。
- `sign.properties`：签署相关配置，如签署模板路径、签署报告模板等。
- `shiro.properties`：权限控制配置，包括接口权限、数据权限规则等。

## 开发与扩展
- **自定义签署控件**：通过`SignReDocControl`与`SignRuDocControl`扩展签署控件类型与属性。
- **签署流程插件**：支持通过`SignCommandInterceptor`扩展签署流程拦截器。
- **模板引擎**：支持JasperReports模板引擎，用于生成签署报告。

## 许可证
本项目遵循Apache 2.0协议。详见[LICENSE](LICENSE)文件。