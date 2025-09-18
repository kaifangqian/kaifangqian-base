# OpenSign Electronic Contract System

## Project Overview
OpenSign is an electronic contract signing platform that provides a complete solution for contract creation, signing, management, and verification. The system supports real-name authentication for individuals and enterprises, electronic seal management, contract template configuration, signing process control, and more, making it suitable for various business scenarios requiring electronic signatures.

## Key Features
- **Contract Lifecycle Management**: Supports full lifecycle management including contract creation, signing, approval, archiving, and invalidation.
- **Real-Name Authentication**: Integrated with Yundun authentication service, supporting real-name authentication for individuals and enterprises.
- **Electronic Seal Management**: Provides operations for generating, enabling, disabling, and deleting personal signatures and enterprise seals.
- **Signing Intent Confirmation**: Supports multiple intent confirmation methods such as SMS, email, and signing passwords.
- **Contract Templates**: Supports template creation, control configuration, template authorization, and template usage.
- **Signing Process Control**: Supports sequential signing by multiple parties, fillable control configuration, and signing permission control.
- **File Processing**: Supports PDF file conversion, encryption, watermark addition, and signature verification.
- **Data Permission Control**: Data permission management based on the RBAC model, supporting fine-grained data access control.
- **Audit Logs**: Fully records logs for contract signing, seal usage, and user operations, facilitating tracking and auditing.

## Technical Architecture
- **Backend Framework**: Spring Boot + MyBatis Plus + Shiro
- **Database**: MySQL
- **Caching**: Redis
- **Message Queue**: Redis MQ
- **Security**: JWT authentication, RSA/SM2 encryption, interface signature verification
- **Deployment**: Docker containerized deployment, supports JRE 8 environment

## Module Description
- **API Module**: Provides external interface services, supporting operations such as contract creation and signing.
- **Authentication Module**: Integrated with Yundun real-name authentication service, supports individual and enterprise authentication.
- **Signing Service Module**: Handles contract signing processes, signing control configuration, and intent confirmation.
- **Seal Management Module**: Supports generation, authorization, usage, and auditing of personal signatures and enterprise seals.
- **Template Management Module**: Creation, configuration, authorization, and usage of contract templates.
- **File Processing Module**: Supports PDF file conversion, encryption, signature verification, and download.
- **Permission Control Module**: Data permission control based on the RBAC model, supports separation of interface and data permissions.
- **Logging and Audit Module**: Records key operation logs such as user actions, contract signing, and seal usage.

## Usage Guide
### Contract Signing Process
1. **Real-Name Authentication**: Users must complete real-name authentication for individuals or enterprises.
2. **Create Contract**: Create a contract via API or frontend interface, configure signatories, signing controls, signing order, etc.
3. **Initiate Signing**: Start the contract signing process, the system generates a signing task.
4. **Signing Intent Confirmation**: Signatories confirm their intent via SMS, email, or signing password.
5. **Signing Operation**: Signatories complete the signing operation, the system records the signing information.
6. **Contract Archiving**: Contracts are automatically archived after signing, supporting download and signature verification.

### API Usage Example
#### Create Contract
```java
@PostMapping("/contract/draft")
public ApiCommonRes<SignContract> contractDraft(@RequestBody ContractDraftRequest request)
```
Request Parameters:
```json
{
  "subject": "Service Agreement",
  "signerList": [
    {
      "signerType": "1",
      "signerName": "Zhang San",
      "receiver": {
        "name": "Zhang San",
        "contactType": "1",
        "contact": "13800000000"
      },
      "positionParamList": [
        {
          "controlType": "sign",
          "signPositionType": "keyword",
          "keyword": "Signature Area",
          "offsetX": "100",
          "offsetY": "200"
        }
      ]
    }
  ]
}
```
Response Example:
```json
{
  "code": 200,
  "message": "Success",
  "result": {
    "contractId": "123456"
  }
}
```

## Security and Permissions
- **API Authentication**: Uses JWT Token for API authentication, supports RSA/SM2 signature verification.
- **Data Permissions**: Data permission control based on the RBAC model, supports field-level and row-level permissions.
- **Signing Password**: Signing operations require secondary verification via a signing password or intent confirmation mechanism.
- **Audit Logs**: Logs all key operations for tracking and auditing.

## Deployment Guide
### Docker Deployment
Use the provided Dockerfile for containerized deployment:
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

### Configuration Files
- `application.yml`: Main configuration file, includes configurations for database, Redis, interface signature, etc.
- `sign.properties`: Signing-related configurations, such as signing template paths and signing report templates.
- `shiro.properties`: Permission control configurations, including interface and data permission rules.

## Development and Extensibility
- **Custom Signing Controls**: Extend signing control types and attributes through `SignReDocControl` and `SignRuDocControl`.
- **Signing Process Plugins**: Supports extending signing process interceptors through `SignCommandInterceptor`.
- **Template Engine**: Supports the JasperReports template engine for generating signing reports.

## License
This project follows the Apache 2.0 license. See the [LICENSE](LICENSE) file for details.