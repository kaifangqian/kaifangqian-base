# 开放签电子签平台docker安装手册

## 1、将deploy.zip上传至/home/data/目录下 进行解压
```
cd /home/data/ && unzip deploy.zip && cd deploy
mkdir -p /home/data/mysql
mkdir -p /home/data/storage
```

## 2、赋予sh脚本可执行权限
```
chmod 777 deploy.sh install-docker.sh drop-mysql-redis.sh drop-opensign.sh
```
## 3、执行docker安装命令
### 3.1 安装docker
```
./install-docker.sh --mirror Aliyun
```

### 3.2 设置开机自启，并启动docker
```
systemctl enable docker
systemctl start docker
docker swarm init
```

## 4、部署mysql redis服务
### 执行前请先设置mysql/redis的密码，在mysql-redis.yaml中有提示
```
./deploy.sh mysql-redis.yaml
```


## 5、初始化数据
### 5.1查询mysql容器id
```
docker ps -f "name=mysql"
```

### 5.2向容器中copy 数据库初始化文件
```
docker cp /home/data/deploy/config/opensign.sql [容器id]:/home
```
### 5.3进入容器
```
docker exec -it [容器id] /bin/bash
```
### 5.4进入数据库 密码在mysql-redis.yaml中查找
```
mysql -uroot -p
```
### 5.5执行sql脚本
```
source /home/opensign.sql;
```
### 5.6修改数据库应用访问地址   需要将host 和 pord替换例如 通过 https://www.kaifangqian.com访问
```
#update sys_app_info s set s.app_address = REPLACE(app_address, 'http://localhost', 'https://www.kaifangqian.com');
#update sys_app_info s set s.app_address = REPLACE(app_address, 'http://localhost', 'http://192.168.0.1');
update sys_app_info s set s.app_address = REPLACE(app_address, 'http://localhost', 'http://host:pord');
```
### 5.7退出数据库和容器 （若果在数据库中需要exit两次才能回到宿主机上）
```
exit
```
### 5.8 数据库备份（在数据库容器中执行）
```
mysqldump -u root -p opensign > /back-opensign.sql
```
### 5.9 copy备份数据库至宿主机/home路径（在宿主机上执行）
```
docker cp [容器id]:/back-opensign.sql /home/
```
## 6、如果有阿里云OSS
需要将opensign.yaml中的这三项进行修改 STORAGE_ACTIVE=aliyun/OSS_ACCESSKEYID=xxx/OSS_ACCESSKEYSECRET=xxx
	
## 7、部署 job、api、web服务
```
#部署前需要将opensign.yaml中的mysql/redis/oss/token配置进行修改
./deploy.sh opensign.yaml
```
## 8、重新部署
以下操作当需要升级或者更改配置时会用到。
### 8.1 删除mysql/redis服务
```
./drop-mysql-redis.sh
```
### 8.2 重新部署mysql/redis
```
./deploy.sh mysql-redis.yaml
```
### 8.3 删除开放签服务
```
./drop-opensign.sh
```
### 8.4 重新部署开放签服务
```
./deploy.sh opensign.yaml
```

## 9、v3.0.0升级v3.0.2
### 9.1 执行v3.0.1.sql，v3.0.2.sql; 执行步骤参看5.1 - 5.5
### 9.2 修改opensign.yaml中的api和nginx镜像版本为v3.0.2
### 9.3 执行8.3和8.4删除重新部署服务
