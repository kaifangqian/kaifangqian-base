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

import type { RouteLocationNormalized, RouteRecordNormalized } from 'vue-router';
import type { App, Plugin } from 'vue';
import { unref } from 'vue';
import { isObject } from '/@/utils/is';
import { stubFalse } from 'lodash-es';
import { getToken } from '/@/utils/auth';
import { getAppEnvConfig } from '/@/utils/env';

export const noop = () => { };

/**
 * @description:  Set ui mount node
 */
export function getPopupContainer(node?: HTMLElement): HTMLElement {
    return (node?.parentNode as HTMLElement) ?? document.body;
}

/**
 * Add the object as a parameter to the URL
 * @param baseUrl url
 * @param obj
 * @returns {string}
 * eg:
 *  let obj = {a: '3', b: '4'}
 *  setObjToUrlParams('www.baidu.com', obj)
 *  ==>www.baidu.com?a=3&b=4
 */
export function setObjToUrlParams(baseUrl: string, obj: any): string {
    let parameters = '';
    for (const key in obj) {
        parameters += key + '=' + encodeURIComponent(obj[key]) + '&';
    }
    parameters = parameters.replace(/&$/, '');
    return /\?$/.test(baseUrl) ? baseUrl + parameters : baseUrl.replace(/\/?$/, '?') + parameters;
}

export function objectToQueryString(obj) {
    return Object.keys(obj)
        .map(key => encodeURIComponent(key) + '=' + encodeURIComponent(obj[key]))
        .join('&');
}

export function deepMerge<T = any>(src: any = {}, target: any = {}): T {
    let key: string;
    for (key in target) {
        src[key] = isObject(src[key]) ? deepMerge(src[key], target[key]) : (src[key] = target[key]);
    }
    return src;
}

export function openWindow(
    url: string,
    opt?: { target?: TargetContext | string; noopener?: boolean; noreferrer?: boolean },
) {
    const { target = '__blank', noopener = true, noreferrer = true } = opt || {};
    const feature: string[] = [];

    noopener && feature.push('noopener=yes');
    noreferrer && feature.push('noreferrer=yes');
    window.open(url, target, feature.join(','));
}


// dynamic use hook props
export function getDynamicProps<T, U>(props: T): Partial<U> {
    const ret: Recordable = {};

    Object.keys(props).map((key) => {
        ret[key] = unref((props as Recordable)[key]);
    });

    return ret as Partial<U>;
}

export function simpleDebounce(fn, delay = 100) {
    let timer = null
    return function () {
        let args = arguments
        if (timer) {
            clearTimeout(timer)
        }
        timer = setTimeout(() => {
            fn.apply(this, args)
        }, delay)
    }
}

export function getRawRoute(route: RouteLocationNormalized): RouteLocationNormalized {
    if (!route) return route;
    const { matched, ...opt } = route;
    return {
        ...opt,
        matched: (matched
            ? matched.map((item) => ({
                meta: item.meta,
                name: item.name,
                path: item.path,
            }))
            : undefined) as RouteRecordNormalized[],
    };
}

export const withInstall = <T>(component: T, alias?: string) => {
    const comp = component as any;
    comp.install = (app: App) => {
        console.log(comp.name, '组件注册名称');
        app.component(comp.name || comp.displayName, component);
        if (alias) {
            app.config.globalProperties[alias] = component;
        }
    };
    return component as T & Plugin;
};


/***
 * 判断一天的时间段
 * @param {*}
 */

export function getTimeState(): string {
    let timeNow = new Date();
    let hours = timeNow.getHours();
    let timeStateStr = '';
    if (hours >= 0 && hours <= 12) {
        timeStateStr = `上午好`;
    } else if (hours > 12 && hours <= 14) {
        timeStateStr = `中午好`;
    } else if (hours > 14 && hours <= 18) {
        timeStateStr = `下午好`;
    } else if (hours > 18 && hours <= 24) {
        timeStateStr = `晚上好`;
    }
    return timeStateStr;
}

/**
 * 解析url参数 (只能获取非hash路由即 # 前的?key1=value1&&key2=value2 形式的参数)
 * @param {*}
 */

export function getQueryString(name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return decodeURI(r[2]);
    }
    return null;
}

/**
 * 获取hash路由# 后的参数
 * @param {*}
 */
export function getHashQueryString(val) {

    const w = location.hash.indexOf('?');
    const query = location.hash.substring(w + 1);

    const vars = query.split('&');
    for (let i = 0; i < vars.length; i++) {
        const pair = vars[i].split('=');
        if (pair[0] == val) { 
          const value = pair[1];
          if (value === "false") {
              return false;
          } else if(value === 'undefined'){
            return undefined
          }else if (value === "true") {
              return true;
          } else {
              return value;
          }
         }
    }

    return '';
}
/**
 * 获取url参数中除指定参数外的其他参数
 */

export function removeQueryParam(url, paramName) {
    var urlparts = url.split('?');
    if (urlparts.length >= 2) {

        var prefix = encodeURIComponent(paramName) + '=';
        var pars = urlparts[1].split(/[&;]/g);
        // reverse iteration as may be destructive
        for (var i = pars.length; i-- > 0;) {
            // idiom for string.startsWith
            if (pars[i].lastIndexOf(prefix, 0) !== -1) {
                pars.splice(i, 1);
            }
        }
        return urlparts[0] + (pars.length > 0 ? '?' + pars.join('&') : '');
    }
    return url;
}

/**
 * 判断终端
 * @param {*}
 */

export function isMobileAgent() {
    let flag = navigator.userAgent.match(
        /(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i
    );
    if (!flag) {
        return false;
    }
    return true
}
/**
 *tree扁平化
 */
export function treeToArray(tree) {
    return tree.reduce((res, item) => {
        const { children, ...i } = item
        return res.concat(i, children && children.length ? treeToArray(children) : [])
    }, [])
}


export const deepClone = (source: any) => {
    var sourceCopy: any = source instanceof Array ? [] : {}
    for (var item in source) {
        sourceCopy[item] = typeof source[item] === 'object' ? deepClone(source[item]) : source[item]
    }
    return sourceCopy
}

export const groupBy = (array: any[], name: string) => {
    const groups = <MenuGroup>{};
    array.forEach(item => {
        const group = item[name];
        groups[group] = groups[group] || [];
        groups[group].push(item);

    })
    return groups;
}

export const uuid = () => {
    return parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16);
}

export const backParent = (router?: any, parentPath?: string, query?: any) => {
    if (window.opener) {
        //如果父级页面还存在 则直接关闭当前页面  并返回父级页面
        // console.log("backParentName:",window.opener.name);
        window.close();
        window.open("", window.opener.name)
        // window.open("",parentName);
    } else {
        if (parentPath && router) {
            //不存在则直接根据path进行跳转
            router.push({
                path: parentPath,
                query: query
            })
            //console.log("backParentPath:",parentPath);
        }
    }
    window.close();
}


/**
 * 文件下载
 */

export function handleDownload(record) {
    const token = getToken() as string;
    const req = new XMLHttpRequest();
    req.open('GET', location.origin + import.meta.env.VITE_GLOB_API_URL + '/file/downloadFileStream/' + record.id, true);
    req.responseType = 'blob';
    req.setRequestHeader('X-Access-Token', token);
    req.onload = function () {
        const data = req.response;
        const blob = new Blob([data], { type: 'application/octet-stream' });
        //   let fileName = decodeURI(
        //     req?.getResponseHeader('Content-Disposition')?.split(';')[1].split('=')[1] || '',
        // );
        let fileName = decodeURI(
            req?.getResponseHeader('Filename') as string
        );
        let dom = document.createElement('a')
        let url = window.URL.createObjectURL(blob)
        dom.href = url
        dom.download = decodeURI(record.realName || fileName)
        dom.style.display = 'none'
        document.body.appendChild(dom)
        dom.click()
        dom && dom?.parentNode?.removeChild(dom)
        window.URL.revokeObjectURL(url)
    };
    req.send();
}

/**
 * 合同下载
 */

export function handleRuDownload(record, url?:string) {
  const token = getToken() as string;
  let {VITE_GLOB_APP_CODE} = getAppEnvConfig();
  const req = new XMLHttpRequest();
  const  apiUrl = url?url: '/sign/ru/doc/file/download?signRuId=' + record.signRuId + '&ruDocIdList='+ record.ruDocIdList
  req.open('GET', location.origin + import.meta.env.VITE_GLOB_API_URL + apiUrl , true);
  req.responseType = 'blob';
  req.setRequestHeader('X-Access-Token', token);
  req.setRequestHeader('Resrun-App-Code', VITE_GLOB_APP_CODE);
  req.onload = function () {
      const data = req.response;
      const blob = new Blob([data], { type: 'application/octet-stream' });
      //   let fileName = decodeURI(
      //     req?.getResponseHeader('Content-Disposition')?.split(';')[1].split('=')[1] || '',
      // );
      let fileName = decodeURIComponent(
          req?.getResponseHeader('Filename') as string
      );
      let dom = document.createElement('a')
      let url = window.URL.createObjectURL(blob)
      dom.href = url
      dom.download = (record.realName || fileName)
      dom.style.display = 'none'
      document.body.appendChild(dom)
      dom.click()
      dom && dom?.parentNode?.removeChild(dom)
      window.URL.revokeObjectURL(url)
  };
  req.send();
}



/**
 * 页码范围拆分
 */

export function parseInputPages(val) {
    const result: number[] = [];

    // 分割字符串
    const parts = val.split(',');

    // 遍历每个部分
    for (let i = 0; i < parts.length; i++) {
        const part = parts[i].trim();

        // 检查是否为数字
        if (/^\d+$/.test(part)) {
            // 如果是数字，直接添加到结果数组
            result.push(parseInt(part));
        } else if (/^\d+-\d+$/.test(part)) {
            // 如果是数字范围，拆分范围并添加到结果数组
            const rangeParts = part.split('-');
            const start = parseInt(rangeParts[0]);
            const end = parseInt(rangeParts[1]);

            // 添加范围内的数字到结果数组
            for (let j = start; j <= end; j++) {
                result.push(j);
            }
        } else {
            // 非法输入
            console.error(`Invalid input: ${part}`);
        }
    }

    // 对结果数组进行排序
    // 对结果数组进行排序和去重
    const uniqueSortedResult = Array.from(new Set(result)).sort((a, b) => a - b);

    return uniqueSortedResult;

}

export  function getBase64Size(base64) {
  //确认处理的是png格式的数据
  if (base64.substring(0,22) === 'data:image/png;base64,') {
      // base64 是用四个字符来表示3个字节
      // 我们只需要截取base64前32个字符(不计开头那22个字符)便可（24 / 3 * 4）
      // 这里的data包含12个字符，9个字节，除去第1个字节，后面8个字节就是我们想要的宽度和高度
      const data = base64.substring(22 + 20, 22 + 32); 
      const base64Characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/';
      const nums:number[] = [];
      for (const c of data) {
          nums.push(base64Characters.indexOf(c));
      }
      const bytes:number[]  = [];
      for(let i = 0; i < nums.length; i += 4) {
          bytes.push((nums[i] << 2) + (nums[i+1] >> 4));
          bytes.push(((nums[i+1] & 15) << 4) + (nums[i+2] >> 2));
          bytes.push(((nums[i+2] & 3) << 6) + nums[i+3]);
      }
      const width = (bytes[1] << 24) + (bytes[2] << 16) + (bytes[3] << 8) + bytes[4];
      const height = (bytes[5] << 24) + (bytes[6] << 16) + (bytes[7] << 8) + bytes[8];
      return {
          width,
          height,
      };
  }
  throw Error('unsupported image type');
}



/**
 * 冒泡排序
 * @param {any} arr 
 * @return 
 */ 
export function bubbleSort(arr) {
  let len = arr.length;
  for (let i = 0; i < len - 1; i++) {
      for (let j = 0; j < len - 1 - i; j++) {
          if (arr[j] > arr[j+1]) {        // 相邻元素两两对比
              let temp = arr[j+1];        // 元素交换
              arr[j+1] = arr[j];
              arr[j] = temp;
          }
      }
  }
  return arr;
}


export function retainDecimals(num:any,len?:number){
  var newNum = num.toFixed(len?len:2);
  return parseInt(newNum);
}

//获取指定页码之前的图片高度累加值
export function getAccumulatedHeight(array, targetPage) {
  return array.reduce((accumulator, current) => {
      if (current.page <= targetPage) {
          return accumulator + current.height;
      }
      return accumulator;
  }, 0);
}


/**
 * 函数节流
 */
export function throttle(fn,delay){
  let preTime = Date.now();
  return function(){
    let context = this,
      args = arguments,
      nowTime = Date.now();
      if(nowTime - preTime > delay){
        fn.apply(context,args);
        preTime = Date.now();
      }

  }
}
// 函数防抖的实现
export function debounce(fn, wait) {
    let timer = null;
    return function() {
        if (timer) {
            clearTimeout(timer)
        }
        timer = setTimeout(() => {
            fn.apply(this, arguments)
            timer = null
        }, wait)
    }
}

export function decodeURIs(url:any) {
  if(url && url.length > 0 && url.includes('http')){
    var urlparts = decodeURIComponent(url);
    if(urlparts.startsWith("http://") || urlparts.startsWith("https://")){
      return urlparts;
    }else{
      return decodeURIs(urlparts);
    }
  }else{
    return url;
  }
}
