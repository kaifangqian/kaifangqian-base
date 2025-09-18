/*
 * @description 开放签
 *
 * Copyright (C) [2025] [版权所有者（北京资源律动科技有限公司）]. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * 注意：本代码基于 AGPLv3 协议发布。若通过网络提供服务（如 Web 应用），
 * 必须公开修改后的完整源码（包括衍生作品），详见协议全文。
 */

/*
 * @Author: ningw 
 * @Date: 2022-06-29 15:12:16 
 * @Last Modified by: mikey.zhaopeng
 * @Last Modified time: 2024-07-12 10:09:57
 */
import type { AxiosResponse,AxiosInstance } from 'axios';
import { clone } from 'lodash-es';
import type { RequestOptions, Result } from '/#/axios';
import type { AxiosTransform, CreateAxiosOptions } from './axiosTransform';
import { VAxios } from './Axios';
import { checkStatus } from './checkStatus';
import { useGlobSetting } from '/@/hooks/setting';
import { useMessage } from '/@/hooks/web/useMessage';
import { RequestEnum, ResultEnum, ContentTypeEnum } from '/@/enums/httpEnum';
import { isString } from '/@/utils/is';
import { getToken } from '/@/utils/auth';
import { setObjToUrlParams, deepMerge } from '/@/utils';
import { useErrorLogStoreWithOut } from '/@/store/modules/errorLog';
import { joinTimestamp, formatRequestDate } from './helper';
import { useUserStoreWithOut } from '/@/store/modules/user';
import { useAppStoreWithOut } from '/@/store/modules/app';
import { AxiosRetry } from '/@/utils/http/axios/axiosRetry';
import { useLoginState, LoginStateEnum } from '/@/views/sys/login/useLogin';
import jsonBig  from 'json-bigint';
import { formatToDate } from '/@/utils/dateUtil';
import axios from 'axios';



const globSetting = useGlobSetting();
const urlPrefix = globSetting.urlPrefix;
const { createMessage, createErrorModal,createWarningModal } = useMessage();
const { setLoginState } = useLoginState();
const appStore = useAppStoreWithOut();
const source = axios.CancelToken.source();



/**
 * @description: 数据处理，方便区分多种处理方式
 */
const transform: AxiosTransform = {
  /**
   * @description: 处理请求数据。如果数据不是预期格式，可直接抛出错误
   */
  transformRequestHook: (res: AxiosResponse<Result>, options: RequestOptions) => {
    const { isTransformResponse, isReturnNativeResponse } = options;
    const userStore = useUserStoreWithOut();
    //清空安全设置相关的配置
    userStore.setSafeInfo({phone:'',username:''});
    // 是否返回原生响应头 比如：需要获取响应头时使用该属性
    if (isReturnNativeResponse) {
      return res;
    }
    // 不进行任何处理，直接返回
    // 用于页面代码可能需要直接获取code，data，message这些信息时开启
    if (!isTransformResponse) {
      return res.data;
    }
    // 错误的时候返回

    const { data } = res;
    if (!data) {
      // return '[HTTP] Request has no return value';
      throw new Error('请求出错，请稍候重试');
    }
    //  这里 code，result，message为 后台统一的字段，需要在 types.ts内修改为项目自己的接口返回格式
    const { code, result, message } = data;
    // 这里逻辑可以根据项目进行修改
    const hasSuccess = data && Reflect.has(data, 'code') && (code === ResultEnum.SUCCESS || code === 0);
    // 获取请求头信息，如果存在 请求成功后清除
    // const sensitiveInfo= appStore.getSensitiveHeader;
    if (hasSuccess) {
      // return result?result:data;
      return typeof(result) == 'boolean'?result: result?result:data;
    }
    // 异地和长时间未登录需要短信验证
    console.log(res,'请求信息')
    if(code === 530){
      let loginInfo = JSON.parse(res.config.data);
      console.log(loginInfo,'请求信息')
    // if(code===511 || code === 510){
      createWarningModal({
        title: '提示', 
        content: message,
        okText:'确定',
        iconType: 'warning',
        onOk() {
          userStore.setSafeInfo({phone:result?.phone,username:loginInfo.username});
          setLoginState(LoginStateEnum.LOGIN_VERIFY);
        },
      })
      return data;
    }
    if(code === 500 && message){
      createMessage.warning(message);
      return ''
    }
    if(code === 510){
      createMessage.warning(message);
      return ''
    }
    if(code === 404){
      createMessage.warning(message);
      return ''
    }
    if(code === 520){
      createMessage.warning(message);
      return ''
    }
    if(code==540){
      return '';
    }

    // 在此处根据自己项目的实际情况对不同的code执行不同的操作
    // 如果不希望中断当前请求，请return数据，否则直接抛出异常即可
    let timeoutMsg = '';
    switch (code) {
      case ResultEnum.TIMEOUT:
        timeoutMsg = '登录超时,请重新登录!';
        userStore.setToken(undefined);
        // userStore.logout(true);
        break;
      default:
        if (message) {
          timeoutMsg = message;
        }
    }

    // errorMessageMode=‘modal’的时候会显示modal错误弹窗，而不是消息提示，用于一些比较重要的错误
    // errorMessageMode='none' 一般是调用时明确表示不希望自动弹出错误提示
    if (options.errorMessageMode === 'modal') {
      createErrorModal({ title: '错误提示', content: timeoutMsg }); 
    } else if (options.errorMessageMode === 'message') {
      createMessage.error(timeoutMsg);
    }
    // 部分状态码下不抛出错误
    if(code && (code !== ResultEnum.LONG_TIME_NO_LOGIN) ){
        throw new Error(timeoutMsg || '请求出错，请稍候重试');
    }
  },

  //  /**
  //  * @description: 响应拦截器处理
  //  */
  //   transformResponseHook: (res: AxiosResponse<Result>, options: RequestOptions) => {
  //     try {
  //       return jsonBig.parse(res)
  //     } catch (err) {
  //       return res
  //     }
  //   },
  // 请求之前处理config
  beforeRequestHook: (config, options) => {
    const { apiUrl, joinPrefix, joinParamsToUrl, formatDate, joinTime = true, urlPrefix } = options;

    if (joinPrefix) {
      config.url = `${urlPrefix}${config.url}`;
    }

    if (apiUrl && isString(apiUrl)) {
      config.url = `${apiUrl}${config.url}`;
    }
    //如果是微应用加载 api路径写为绝对路径
    if(window.eventCenterForTenantVite){
      let microApiUrl = import.meta.env.VITE_MICRO_API;
      config.url = microApiUrl +`${config.url}`;
    }
    const params = config.params || {};
    const data = config.data || false;
    formatDate && data && !isString(data) && formatRequestDate(data);
    if ((config.method?.toUpperCase() === RequestEnum.GET) || (config.method?.toUpperCase() === RequestEnum.DELETE)) {
      if (!isString(params)) {
        // 给 get 请求加上时间戳参数，避免从缓存中拿数据。
        config.params = Object.assign(params || {}, joinTimestamp(joinTime, false));
      } else {
        // 兼容restful风格
        config.url = config.url + params + `${joinTimestamp(joinTime, true)}`;
        config.params = undefined;
      }
      if(config.params.beginTime){
        config.params.beginTime = formatToDate(config.params.beginTime,'YYYY-MM-DD')+ ' ' + '00:00:00';
      }
      if(config.params.endTime){
        config.params.endTime = formatToDate(config.params.endTime,'YYYY-MM-DD')+ ' ' + '23:59:59';
      }
    } else {
      if (!isString(params)) {
        formatDate && formatRequestDate(params);
        if (Reflect.has(config, 'data') && config.data && Object.keys(config.data).length > 0) {
          config.data = data;
          config.params = params;
        } else {
          // 非GET请求如果没有提供data，则将params视为data
          config.data = params;
          config.params = undefined;
        }
        if (joinParamsToUrl) {
          config.url = setObjToUrlParams(
            config.url as string,
            Object.assign({}, config.params, config.data),
          );
        }
      } else {
        // 兼容restful风格
        config.url = config.url + params;
        config.params = undefined;
      }
    }
    return config;
  },

  /**
   * @description: 请求拦截器处理
   */
  requestInterceptors: (config, options) => {
    // 请求之前处理config
    const token = getToken();
    const sensitiveInfo= appStore.getSensitiveHeader;
    config.cancelToken = source.token;
    if(sensitiveInfo){
      (config as Recordable).headers = Object.assign({
        'resrun-app-code':'app_00005'
      },(config as Recordable).headers,sensitiveInfo)
    }
    if (token && (config as Recordable)?.requestOptions?.withToken !== false) {
      // jwt token
      (config as Recordable).headers['X-Access-Token'] = options.authenticationScheme
        ? `${options.authenticationScheme} ${token}`
        : token;
       (config as Recordable).headers['resrun-app-code'] = 'app_00005';
    }
    if((config as Recordable)?.requestOptions?.withToken == false && (config as Recordable)?.requestOptions?.loginToken ){
      (config as Recordable).headers['X-Access-Token'] = (config as Recordable)?.requestOptions?.loginToken
    }
    return config;
  },

  /**
   * @description: 响应拦截器处理
   */
  responseInterceptors: (axiosInstance: AxiosInstance, res: AxiosResponse<any>) => { 
    const { data } = res;
    if (!data) {
      // return '[HTTP] Request has no return value';
      throw new Error('请求出错，请稍候重试');
    }
    //  这里 code，result，message为 后台统一的字段，需要在 types.ts内修改为项目自己的接口返回格式
    const { code, result } = data;
    if( code == 401 ){
      source.cancel()
    }
    if(code===540){
      try{
        appStore.setSensitiveConfig({sensitiveType:result,visible:true});
        appStore.setAxiosRetryConfig({
          instance:axiosInstance,
          config:res.config
        });
        // return axiosInstance.request(res.config);
      }catch(error){
        console.log(error)
      }
      return res;
    }
    return res
  },

  /**
   * @description: 响应错误处理
   */
  responseInterceptorsCatch: (axiosInstance: AxiosInstance, error: any) => {
    const errorLogStore = useErrorLogStoreWithOut();
    errorLogStore.addAjaxErrorInfo(error);
    const { response,  code, message, config } = error || {};
    const errorMessageMode = config?.requestOptions?.errorMessageMode || 'none';
    const msg: string = response?.data?.error?.message ?? '';
    const err: string = error?.toString?.() ?? '';
    let errMessage = '';
  
    try {
      if (code === 'ECONNABORTED' && message.indexOf('timeout') !== -1) {
        errMessage = '接口请求超时,请刷新页面重试!';
      }
      if (err?.includes('Network Error')) {
        errMessage = '网络异常，请检查您的网络连接是否正常!';
      }

      if (errMessage) {
        if (errorMessageMode === 'modal') {
          createErrorModal({ title: '错误提示', content: errMessage });
        } else if (errorMessageMode === 'message') {
          createMessage.error(errMessage);
        }
        return Promise.reject(error);
      }
    } catch (error) {
      throw new Error(error as unknown as string);
    }

    checkStatus(error?.response?.status, response.data.code, msg, errorMessageMode);
    // console.log(error,'错误')
    if(!error.config){
      return Promise.reject(error);
    }
    // 添加自动重试机制 保险起见 只针对GET请求
    const retryRequest = new AxiosRetry();
    const { isOpenRetry } = config?.requestOptions?.retryRequest;
    config.method?.toUpperCase() === RequestEnum.GET &&
      isOpenRetry &&
      // @ts-ignore
      retryRequest.retry(axiosInstance, error);
    return Promise.reject(error);
  },
};

function createAxios(opt?: Partial<CreateAxiosOptions>) {
  return new VAxios(
    deepMerge(
      {
        // See https://developer.mozilla.org/en-US/docs/Web/HTTP/Authentication#authentication_schemes
        // authentication schemes，e.g: Bearer
        // authenticationScheme: 'Bearer',
        authenticationScheme: '',
        timeout: 30 * 1000,
        // 基础接口地址
        // baseURL: globSetting.apiUrl,

        headers: { 'Content-Type': ContentTypeEnum.JSON },
        // 如果是form-data格式
        // headers: { 'Content-Type': ContentTypeEnum.FORM_URLENCODED },
        // 数据处理方式
        transform: clone(transform),
        transformResponse:(data)=>{
          try {
            // 如果转换成功则返回转换的数据结果
            const json = jsonBig({
              storeAsString: true
            })
            return json.parse(data)
          } catch (err) {
            // 如果转换失败，则包装为统一数据格式并返回
            return {
              data
            }
          }
        },
        // 配置项，下面的选项都可以在独立的接口请求中覆盖
        requestOptions: {
          // 默认将prefix 添加到url
          joinPrefix: true,
          // 是否返回原生响应头 比如：需要获取响应头时使用该属性
          isReturnNativeResponse: false,
          // 需要对返回数据进行处理
          isTransformResponse: true,
          // post请求的时候添加参数到url
          joinParamsToUrl: false,
          // 格式化提交参数时间
          formatDate: true,
          // 消息提示类型
          errorMessageMode: 'message',
          // 接口地址
          apiUrl: globSetting.apiUrl,
          // 接口拼接地址
          urlPrefix: urlPrefix,
          //  是否加入时间戳
          joinTime: true,
          // 忽略重复请求
          ignoreCancelToken: true,
          // 是否携带token
          withToken: true,
          retryRequest: {
            isOpenRetry: true,
            count: 5,
            waitTime: 100,
          },
        },
      },
      opt || {},
    ),
  );
}
export const defHttp = createAxios();

// other api url
// export const otherHttp = createAxios({
//   requestOptions: {
//     apiUrl: 'xxx',
//     urlPrefix: 'xxx',
//   },
// });
