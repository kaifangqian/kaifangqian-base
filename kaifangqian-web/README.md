## 项目结构
```
├── handwriting/            # 扫码手写签名
├── nginx-config/           # nginx 配置文件
├── opensign-manage/        # 管理后台
├── opensign-message/       # 消息服务
├── opensign-mobile/        # 移动端应用
├── opensign-tenant/        # 租户管理
└── opensign-web/           # 签署主应用
```

## 项目构建
说明：前端所有项目都使用如下的方式进行启动或构建
### 环境依赖
* node: v16+

### 项目启动
* 安装依赖
```bash
npm install (或：pnpm install)

```
* 启动
```bash
npm run dev
```
* 构建项目
```bash
npm run build
```

## 生产环境部署
```shell
1.除开handwriting、nginx-config不需要构建，其他几个项目均需要构建。
2.将handwriting、opensign-manage/dist/manage、opensign-message/dist/message、
  opensign-mobile/dist/mobile、opensign-tenant/dist/tenant
  复制移动到opensign-web/dist/opensign-web目录下
3.最终将opensign-web部署至nginx即可
```

