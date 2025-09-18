/**
 * @description utils
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
 * 必须公开修改后的完整源代码（包括衍生作品），详见协议全文。
 */
import session from '@/utils/cache/session';
import { APP_TOKEN } from '@/utils/cache/constant';

export const toPhoneCall = (mobile: number | string) => {
  const url = `tel:${mobile}`;
  window.location.href = url;
};

/**
 * 动态设置css全局变量实现旋转
 * transform: rotate(var(--image-rotate))
 * @param deg 旋转角度
 * @param prop css变量, 默认'--image-rotate'
 */
export const setRotate = (deg: string, prop = '--image-rotate') => {
  let rotate = document.documentElement.style.getPropertyValue(prop) || '0deg';
  if (typeof deg === 'string') {
    rotate = deg;
  } else {
    rotate = parseInt(rotate) + 90 + 'deg';
  }
  document.documentElement.style.setProperty(prop, rotate);
};
/**
 * 设备判断
 */
export function isMobileDevice() {
  return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
}
/**
 * 获取近一周、月、三月、半年、一年时间
 * @params
 */

export function getRecentTimeRanges() {
  const today = new Date();
  today.setHours(0, 0, 0, 0); // 设置为当天的最后一毫秒

  // 当天
  const currentDate = new Date(today);
  currentDate.setHours(23, 59, 59, 999);

  // 近一周
  const oneWeekAgo = new Date(today);
  oneWeekAgo.setDate(today.getDate() - 7);

  // 近一月
  const oneMonthAgo = new Date(today);
  oneMonthAgo.setMonth(today.getMonth() - 1);

  // 近三月
  const threeMonthsAgo = new Date(today);
  threeMonthsAgo.setMonth(today.getMonth() - 3);

  // 近六月
  const sixMonthsAgo = new Date(today);
  sixMonthsAgo.setMonth(today.getMonth() - 6);

  // 近一年
  const oneYearAgo = new Date(today);
  oneYearAgo.setFullYear(today.getFullYear() - 1);

  // 近三年
  const threeYearAgo = new Date(today);
  threeYearAgo.setFullYear(today.getFullYear() - 3);

  return {
    currentDate: formatDate(currentDate),
    oneWeekAgo: formatDate(oneWeekAgo),
    oneMonthAgo: formatDate(oneMonthAgo),
    threeMonthsAgo: formatDate(threeMonthsAgo),
    sixMonthsAgo: formatDate(sixMonthsAgo),
    oneYearAgo: formatDate(oneYearAgo),
    threeYearAgo: formatDate(threeYearAgo),
  };
}

// 格式化日期为 "YYYY-MM-DD HH:mm:ss"
function formatDate(date: any) {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');
  // return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
  return `${year}-${month}-${day}`;
}

export const timeRanges = getRecentTimeRanges();

/**
 * 文件下载 根据文件id
 */
export function handleDownload(record: any) {
  const token = session.getItem(APP_TOKEN);
  const req = new XMLHttpRequest();
  const baseUrl = import.meta.env.VITE_APP_API_BASE_URL;
  req.open('GET', baseUrl + '/file/downloadFileStream/' + record.id, true);
  req.responseType = 'blob';
  req.setRequestHeader('X-Access-Token', token);
  req.onload = function () {
    const data = req.response;
    const blob = new Blob([data], { type: 'application/octet-stream' });
    // let fileName = decodeURI(
    //     req?.getResponseHeader('content-disposition')?.split(';')[1].split('=')[1] || '',
    // );
    const fileName = decodeURIComponent(req?.getResponseHeader('Filename') as string);
    const dom = document.createElement('a');
    const url = window.URL.createObjectURL(blob);
    dom.href = url;
    dom.download = record.realName || fileName;
    dom.style.display = 'none';
    document.body.appendChild(dom);
    dom.click();
    dom && dom?.parentNode?.removeChild(dom);
    window.URL.revokeObjectURL(url);
  };
  req.send();
}

/**
 * 合同下载
 */
export function handleRuDownload(record: any) {
  const token = session.getItem(APP_TOKEN);
  const appCode = import.meta.env.VITE_GLOB_APP_CODE;
  const req = new XMLHttpRequest();
  const baseUrl = import.meta.env.VITE_APP_API_BASE_URL;
  const apiUrl = `/sign/ru/doc/file/download?signRuId=${record.signRuId}${buildParams(
    record.ruDocIdList
  )}`;
  req.open('GET', baseUrl + apiUrl, true);
  req.responseType = 'blob';
  req.setRequestHeader('X-Access-Token', token);
  req.setRequestHeader('Resrun-App-Code', appCode);
  req.onload = function () {
    const data = req.response;
    const blob = new Blob([data], { type: 'application/octet-stream' });
    // let fileName = decodeURI(
    //     req?.getResponseHeader('content-disposition')?.split(';')[1].split('=')[1] || '',
    // );
    const fileName = decodeURI(req?.getResponseHeader('Filename') as string);
    const dom = document.createElement('a');
    const url = window.URL.createObjectURL(blob);
    dom.href = url;
    dom.download = decodeURI(fileName);
    dom.style.display = 'none';
    document.body.appendChild(dom);
    dom.click();
    dom && dom?.parentNode?.removeChild(dom);
    window.URL.revokeObjectURL(url);
  };
  req.send();
}

function buildParams(arr: any) {
  let params = '&';
  arr.forEach((item: any) => {
    params += 'ruDocIdList=' + item + '&';
  });
  return params;
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

export const uuid = () => {
  return parseInt(new Date().getMilliseconds() + '' + Math.ceil(Math.random() * 100000)).toString(
    16
  );
};

export function buildUUID(): string {
  const hexList: string[] = [];
  for (let i = 0; i <= 15; i++) {
    hexList[i] = i.toString(16);
  }

  let uuid = '';
  for (let i = 1; i <= 36; i++) {
    if (i === 9 || i === 14 || i === 19 || i === 24) {
      uuid += '-';
    } else if (i === 15) {
      uuid += 4;
    } else if (i === 20) {
      uuid += hexList[(Math.random() * 4) | 8];
    } else {
      uuid += hexList[(Math.random() * 16) | 0];
    }
  }
  return uuid.replace(/-/g, '');
}

let unique = 0;
export function buildShortUUID(prefix = ''): string {
  const time = Date.now();
  const random = Math.floor(Math.random() * 1000000000);
  unique++;
  return prefix + '_' + random + unique + String(time);
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
      if (value === 'false') {
        return false;
      } else if (value === 'true') {
        return true;
      } else if (value == 'undefined') {
        return undefined;
      } else {
        return value;
      }
    }
  }

  return '';
}

export function getBase64Size(base64) {
  //确认处理的是png格式的数据
  if (base64.substring(0, 22) === 'data:image/png;base64,') {
    // base64 是用四个字符来表示3个字节
    // 我们只需要截取base64前32个字符(不计开头那22个字符)便可（24 / 3 * 4）
    // 这里的data包含12个字符，9个字节，除去第1个字节，后面8个字节就是我们想要的宽度和高度
    const data = base64.substring(22 + 20, 22 + 32);
    const base64Characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/';
    const nums: number[] = [];
    for (const c of data) {
      nums.push(base64Characters.indexOf(c));
    }
    const bytes: number[] = [];
    for (let i = 0; i < nums.length; i += 4) {
      bytes.push((nums[i] << 2) + (nums[i + 1] >> 4));
      bytes.push(((nums[i + 1] & 15) << 4) + (nums[i + 2] >> 2));
      bytes.push(((nums[i + 2] & 3) << 6) + nums[i + 3]);
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
  const len = arr.length;
  for (let i = 0; i < len - 1; i++) {
    for (let j = 0; j < len - 1 - i; j++) {
      if (arr[j] > arr[j + 1]) {
        // 相邻元素两两对比
        const temp = arr[j + 1]; // 元素交换
        arr[j + 1] = arr[j];
        arr[j] = temp;
      }
    }
  }
  return arr;
}

export function retainDecimals(num: any, len?: number) {
  const newNum = num.toFixed(len ? len : 2);
  return parseInt(newNum);
}

export function arrayToGetParams(array: any) {
  return array
    .map(function (item: any) {
      return item.key + '=' + encodeURIComponent(item.value);
    })
    .join('&');
}

export function decodeURIs(url: any) {
  if (url && url.length > 0 && url.includes('http')) {
    const urlparts = decodeURIComponent(url);
    if (urlparts.startsWith('http://') || urlparts.startsWith('https://')) {
      return urlparts;
    } else {
      return decodeURIs(urlparts);
    }
  } else {
    return url;
  }
}
