# 开放签电子签章系统-开源工具版

## 一、项目介绍

开放签电子签章系统开源工具版旨在将电子签章、电子合同系统开发中的前后端核心技术开源开放，适合有技术能力的个人/团队学习或自建电子签章\电子合同功能或应用，避免研发同仁在工作过程中重复造轮子，降低电子签章技术研发要求，让电子签章相关的技术可以更低门槛的应用在各个业务系统中。

开放签电子签章系统开源工具版前端采用VUE架构，后端采用spring boot架构，开发语言采用java.

开源工具版分为前端、后端两个仓库，前端主要实现在线签署、手写签名、电子印章生成、拖动位置签署等可视化的操作体验。后端主要实现数字证书生成、前后端签署位置计算、电子印章生成、通过印模生成印章、数字签名等电子签章核心技术。
如您系统中需要使用电子签章/电子合同相关应用场景，开放签开源工具版将满足您自建电子签章/电子合同功能模块，您只需要自己采购或用我们采购的数字证书替换即可。
特别提醒：开放签开源版生成的数字证书只能应用于技术测试环境，且应用测试数字证书签署的任何电子文件不具备法律效力，如需要签发正规CA机构数字证书，请与我们联系。

|       |   后端源码  |   前端源码  |
|-------|--- | --- |
| github |  https://github.com/kaifangqian/open-sign   |  https://github.com/kaifangqian/open-sign-vue  |
| 码云    |  https://gitee.com/kaifangqian/open-sign   |  https://gitee.com/kaifangqian/open-sign-vue  |

## 二、功能介绍
### 1、整体功能页面展示
![整体功能页面展示](./src/assets/images/product.png)

### 2、手写签名面板：提供个人手写签名面板的前端页面，生成手写签名图片。
![手写面板](./src/assets/images/signature.png)

### 3、电子印章制作：为企业生成电子印章，生成印章的方式有两种。
#### （1）系统生成：根据印章环绕文字、横排文字生成电子印章图片。
![系统生成电子印章](./src/assets/images/seal-template.png)

#### （2）印模生成：1.在白纸上加盖印章；2.扫描上传进行自动透明化抠图；3.生成透明印章。
![印模图片生成电子印章](./src/assets/images/seal-ym.png)

### 4、数字证书生成：用于电子签章的数字证书（测试证书，正式环境下或真实场景使用数字证书，需自己采购或用我们采购的数字证书替换），该数字证书使用公钥加密技术进行生成，主要用于帮助开发者跑通电子签章流程。【非CA机构签发，不具备法律效力！】
![数字证书生成](./src/assets/images/pdf-cert.png)
### 5、PDF文件转图片：提供PDF文件转图片的工具类。
电子签章过程中存在着在网页上对签署文件进行预览、指定签署位置、文件签署等操作，由于图片在浏览器上的兼容性和友好性优于PDF文件，所以一般在网页上进行电子签章时，会先将PDF文件转换成图片，展示给用户。用户在页面上确定好签署位置，并进行签署时，后端服务会通过对电子印章/手写签名位置、大小以及PDF文件的大小进行计算，在PDF文件的准确位置上完成文件签署。

### 6、电子文件签署：提供两种电子签署的方式。
（1）关键字签署：指定PDF文件中需要进行签章的关键字。签署时，在文档中查找对应关键字的位置，并加盖电子印章/手写签名，完成电子签章；

（2）指定位置签署：直接在PDF文件中拖动需要加盖电子印章/手写签名的位置。签署时，直接在指定位置上完成电子签章。


### 7、签署文件查验：验证电子文件是否被篡改和查看文件中对应的电子印章/手写签名的数字证书信息。
![签署文件查验](./src/assets/images/verify.png)

## 三、后端代码结构
推荐 node 版本：v16.x
```
- public          favicon.ico
- src          
	- assets        静态资源文件
	- components    组件 
	- layouts       布局
	- router        路由配置
	- utils         工具包
	- views         页面文件
		- main             demo主页面
		- SealModal        印章制作弹窗
		- SignatureModal   手写签名弹窗
	- App.vue      VUE根组件
	- main.js      入口文件
```


## 四、前端Build Setup

```
#进入项目目录
cd open-sign-vue

# 安装依赖
npm install -registry=https://registry.npm.taobao.org

# 启动前需要将后端服务启动,并修改vite.config.js中的代理地址,否则无法体验签署功能
# 启动服务 localhost:8080
npm run dev

# 构建生产环境
npm run build
```


## 五、项目地址

### 官方网站

开放签官网网站：https://www.kaifangqian.com

### 开源工具版
（1）开放签电子签章系统-开源工具版体验地址：https://demo.kaifangqian.com

（2）开放签电子签章系统-开源工具版gitee源码：https://gitee.com/kaifangqian

（3）开放签电子签章系统-开源工具版github源码：https://github.com/kaifangqian

### 企业版
（1）开放签电子签章系统-企业版体验地址：https://home.kaifangqian.com/#/login

（2）开放签电子签章系统-企业版使用手册：https://www.yuque.com/huxin-ch41t/kaifangqian



## 六、反馈交流
QQ交流群：482074553

联系电话：150-1099-3257



