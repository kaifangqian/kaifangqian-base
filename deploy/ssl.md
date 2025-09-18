# 注意：修改域名是请将[]一同删除，请勿保留

# 1、将SSL证书放入config/ssl目录中
	a、下载Nginx类型的证书配置
	b、将证书重命名为[您的域名].crt 和 [您的域名].key
	c、将证书 [您的域名].crt、[您的域名].key文件放在config/ssl目录下
	
# 2、修改config/opensign-https.conf中的配置
把[您的域名]改成实际的域名即可
```
server_name [您的域名];
#请填写证书文件的相对路径或绝对路径
ssl_certificate /etc/nginx/ssl/[您的域名].crt;
#请填写私钥文件的相对路径或绝对路径
ssl_certificate_key /etc/nginx/ssl/[您的域名].key;
```
# 3、修改容器的配置
修改opensign.yaml配置文件中的web的配置 将这两行代码解除注释（删掉#即解除注释）
services -> web -> volumes
```
#- /home/data/deploy/config/ssl:/etc/nginx/ssl
#- /home/data/deploy/config/opensign-https.conf:/etc/nginx/conf.d/opensign-https.conf
```

# 4、重新部署服务

	#4.1 删除开放签服务
	./drop-opensign.sh
	#4.2 重新部署开放签服务
	./deploy.sh opensign.yaml


