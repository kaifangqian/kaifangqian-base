#### 1. 项目启动访问问题

* 使用npm/cnpm install 下载完依赖 启动正常 但访问时ant-design-vue 样式文件404

```js
GET http://localhost:8402/node_modules/_ant-design-vue@3.2.15D:/workspace/FormDesigner/resrun-paas-web/src/ant-design-vue/es/config-provider/style/index net::ERR_ABORTED 404 (Not Found)
GET http://localhost:8402/node_modules/_ant-design-vue@3.2.15D:/workspace/FormDesigner/resrun-paas-web/src/ant-design-vue/es/modal/style/index net::ERR_ABORTED 404 (Not Found)
GET http://localhost:8402/node_modules/_ant-design-vue@3.2.15D:/workspace/FormDesigner/resrun-paas-web/src/ant-design-vue/es/notification/style/index net::ERR_ABORTED 404 (Not Found)

```

  ```javascript
    localhost:
  ```
    解决： 删除 `node_modules` 通过 `pnpm install` 重新安装



* 正常启动后报错 Uncaught ReferenceError: __COLOR_PLUGIN_OUTPUT_FILE_NAME__ is not defined

  修改vite.config.ts文件，通过环境变量定义临时解决  [https://github.com/vbenjs/vite-plugin-theme/issues/27]

  ```js
  define: {
    ...
  //新增以下变量
    __COLOR_PLUGIN_OUTPUT_FILE_NAME__: undefined,
    __PROD__: true,
    __COLOR_PLUGIN_OPTIONS__: {},
  },

  ```