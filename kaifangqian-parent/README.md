## 准备
- Git
- Nginx
- IDEA、eclipse
- MySQL: 5.7+
- Redis: 6.0+
- [PowerJob](https://github.com/PowerJob/PowerJob/blob/v4.0.1/README_zhCN.md): 4.0.1
- java: 1.8+ 

注意：部署期应先准备如上环境，目前java应用环境在仅在jdk1.8+下运行和测试，其他版本jdk请自行研究，有兴趣可以向开放签进行贡献代码

## 安装使用
### 1. 获取项目代码
```shell
git clone https://gitee.com/kaifangqian/kaifangqian-base.git
```
### 2. 安装依赖
```shell
# 初始化数据库脚本 或通过其他数据库工具进行导入
source opensign.sql;
# 到项目目录下进行构建
cd kaifangqian-base/kaifangqian-parent
mvn install
```
### 3. 其他配置
#### 3.1 调度中心（powerjob-server）
启动项目必须先启动powerjob-server:4.0.1，否则开放签项目会启动失败，具体请查看[PowerJob](https://github.com/PowerJob/PowerJob/blob/v4.0.1/README_zhCN.md)

opensign.sql中已包含powerjob的表结构和初始化数据，直接修改数据源配置即可

#### 3.2文件处理
- ##### windows环境
```shell
# 将kaifangqian-base/kaifangqian-parent/file复制C://kaifangqian/file
```
- ##### linux环境
```shell
# 将kaifangqian-base/kaifangqian-parent/file复制/home/kaifangqian/file
# 并将simsun.ttc、simkai.ttf、simhei.ttf、simfang.ttf进行安装，字体问题可自行解决，不安装字体印章图片为空白或模板填充的中文会填充不上
```
- ##### mac环境
```shell
# 将kaifangqian-base/kaifangqian-parent/file复制/Users/kaifangqian/file
```
备注：复制的目标路径可自定义，但请自行修改相关配置
```shell
# application-prod.yml
file:
  mac:
    path: /Users/kaifangqian/file/
    avatar: /Users/kaifangqian/file/
  linux:
    path: /home/kaifangqian/file/
    avatar: /home/kaifangqian/file/
  windows:
    path: C:\kaifangqian\file\
    avatar: C:\kaifangqian\file\
```
### 3. 启动项目
```shell
# 自行配置application-prod.yml中相关如Mysql、Redis、PowerJob必要的相关配置
cd kaifangqian-system
mvn spring-boot:run

# 验证
curl -v http://localhost:8899/resrun-paas/
# {"success":true,"message":"service started successfully","code":200,"result":null,"timestamp":1759057224865} 如果看到此输出代表服务启动成功
```