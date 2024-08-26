# 开放签电子签章系统-demo
 

## 安装步骤
```
#需要提前准备node环境，最低版本 v14.20
#进入项目目录
cd kaifangqian-demo-vue

# 安装依赖
npm install -registry=https://registry.npmmirror.com/

# 启动前需要将后端服务启动,并修改vite.config.js中的代理地址,否则无法体验签署功能
# 启动后浏览器访问 http://128.0.0.1:8080
npm run dev

# 构建生产环境
npm run build
```



