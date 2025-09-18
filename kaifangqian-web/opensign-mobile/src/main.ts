import App from './App.vue';
import router from '@/router';
import axios from 'axios';
import 'virtual:svg-icons-register';
import { setupRouterGuard } from '@/router/permission';
import SvgIcon from '@/components/SvgIcon/index.vue';
import Loading from '@/components/Loading/index.vue';
import VueLazyload from 'vue-lazyload';
import store from '@/store';
import 'amfe-flexible';
import 'vant/lib/index.css';
import '@/styles/index.less';

const app = createApp(App);
setupRouterGuard(router);
app.use(VueLazyload);
app.use(Loading);
app.use(router);
app.use(store);
app.component('SvgIcon', SvgIcon);

function transformAppInfoData(rawData) {
  const transformedData: any = {};

  rawData.forEach((item: any) => {
    // 根据appId将数据分类
    switch (item.appId) {
      case '490489ab-d8b4-414c-ad77-d856962c286f':
        transformedData.sign_app_info = {
          url: item.url,
          appCode: item.appCode,
          appId: item.appId,
        };
        break;
      case '13131313':
        transformedData.tenant_app_info = {
          url: item.url,
          appCode: item.appCode,
          appId: item.appId,
        };
        break;
      case '70588803-52e4-433d-a61f-0a68e1febd72':
        transformedData.manage_app_info = {
          url: item.url,
          appCode: item.appCode,
          appId: item.appId,
        };
        break;
      case '313123131':
        transformedData.backstage_app_info = {
          url: item.url,
          appCode: item.appCode,
          appId: item.appId,
        };
        break;
      case '70588803-52e4-ss3d-a61f-0a68e1febd72':
        transformedData.auth_app_info = {
          url: item.url,
          appCode: item.appCode,
          appId: item.appId,
        };
        break;
      case '70588803-52e4-ss3d-a61f-0a68e1febder':
        transformedData.message_app_info = {
          url: item.url,
          appCode: item.appCode,
          appId: item.appId,
        };
        break;
      case '70588803-52e4-ss3d-a61f-0a68e1febxbn':
        transformedData.scan_code_service = {
          url: item.url,
          appCode: item.appCode,
          appId: item.appId,
        };
        break;
      case '70588803-52e4-ss3d-a61f-0a68e1fe12d':
        transformedData.mobile_app_info = {
          url: item.url,
          appCode: item.appCode,
          appId: item.appId,
        };
      default:
        break;
    }
  });

  return transformedData;
}

axios.get('/resrun-paas/sys/listApp?t=' + new Date().getTime(), { async: false }).then((res) => {
  if (res.data.result) {
    window.appInfo = transformAppInfoData(res.data.result);
  }
});

app.mount('#app');
