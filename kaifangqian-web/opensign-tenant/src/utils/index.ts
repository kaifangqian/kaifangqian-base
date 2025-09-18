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
import { getToken } from '/@/utils/auth';

import { unref } from 'vue';
import { isObject } from '/@/utils/is';

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
          } else if (value === "true") {
              return true;
          } else {
              return value;
          }
        }
    }

    return ('');
}

export function handleDownload(record) {
    const token = getToken() as string;
    const req = new XMLHttpRequest();
    req.open('GET', import.meta.env.VITE_PROXY_ADDRESS + '/file/downloadFileStream/' + record.id, true);
    req.responseType = 'blob';
    req.setRequestHeader('X-Access-Token', token);
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

export const deepClone = (source: any) => {
    var sourceCopy: any = source instanceof Array ? [] : {}
    for (var item in source) {
        sourceCopy[item] = typeof source[item] === 'object' ? deepClone(source[item]) : source[item]
    }
    return sourceCopy
}

export const backParent = (parentPath: string, router: any, query?: any) => {
    if (window.opener) {
        //如果父级页面还存在 则直接关闭当前页面  并返回父级页面
        window.close();
        window.open("", window.opener.name)
        // window.open("",parentName);
    } else {
        if (parentPath) {
            //不存在则直接根据path进行跳转
            router.push({
                path: parentPath,
                query: query
            })
        }

    }
}


export const uuid = () => {
    return parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16);
}
export function objectToQueryString(obj) {
    return Object.keys(obj)
        .map(key => encodeURIComponent(key) + '=' + encodeURIComponent(obj[key]))
        .join('&');
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
 * 计算窗体 的可用宽度
 * @param {number} useWidth 
 * @param {number} minWidth 
 * @return 
 */ 
export function getWindowAvailable(useWidth:number,minWidth?:number){
  const available = window.innerWidth - useWidth;
  if(minWidth && available < minWidth){
    return minWidth;
  }
  return available;
}

/**
 * 查找数组项相同项
 */

export function findCommonItemsByKey(array1, array2, key) {
  // 使用 Set 存储 array1 中的 key
  const keySet = new Set(array1.map(item => item[key]));

  // 过滤 array2 中的 items，找出 key 存在于 keySet 中的项
  const commonItems = array2.filter(item => keySet.has(item[key]));

  return commonItems;
}



export function copyClipboard(text:string){
  const range = document.createRange();
  const selection:any = window.getSelection();
  const element = document.createElement('span');
  element.textContent = text;
  document.body.appendChild(element);
  range.selectNodeContents(element);
  selection.removeAllRanges();
  selection.addRange(range);
  document.execCommand('copy');
  document.body.removeChild(element);
}


// 函数节流的实现;
export function throttle(fn, delay) {
  let timer;
  return function(){
    if(!timer) {
      fn.apply(this, arguments)
      timer = setTimeout(()=>{
        clearTimeout(timer)
        timer = null
      },delay)
    }
  }
}
