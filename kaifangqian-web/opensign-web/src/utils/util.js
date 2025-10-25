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

import Vue from 'vue'

export function timeFix() {
  const time = new Date()
  const hour = time.getHours()
  return hour < 9 ? '早上好' : (hour <= 11 ? '上午好' : (hour <= 13 ? '中午好' : (hour < 20 ? '下午好' : '晚上好')))
}

export function welcome() {
  const arr = ['休息一会儿吧', '准备吃什么呢?', '要不要打一把 DOTA', '我猜你可能累了']
  let index = Math.floor((Math.random()*arr.length))
  return arr[index]
}

/**
 * 触发 window.resize
 */
export function triggerWindowResizeEvent() {
  let event = document.createEvent('HTMLEvents')
  event.initEvent('resize', true, true)
  event.eventType = 'message'
  window.dispatchEvent(event)
}

/**
 * 过滤对象中为空的属性
 * @param obj
 * @returns {*}
 */
export function filterObj(obj) {
  if (!(typeof obj == 'object')) {
    return;
  }

  for ( let key in obj) {
    if (obj.hasOwnProperty(key)
      && (obj[key] == null || obj[key] == undefined || obj[key] === '')) {
      delete obj[key];
    }
  }
  return obj;
}

/**
 * 时间格式化
 * @param value
 * @param fmt
 * @returns {*}
 */
export function formatDate(value, fmt) {
  let getDate: Date;
  
  // 如果是数字或数字字符串，转换为日期对象
  let regPos = /^\d+(\.\d+)?$/;
  if (regPos.test(value)) {
    getDate = new Date(Number(value));
  } else if (value instanceof Date) {
    getDate = value;
  } else {
    // 如果是日期字符串，尝试解析
    getDate = new Date(value);
  }
  
  // 检查日期是否有效
  if (!(getDate instanceof Date) || isNaN(getDate.getTime())) {
    console.warn('Invalid date value:', value);
    return '';
  }
  
  let o = {
    'M+': getDate.getMonth() + 1,
    'D+': getDate.getDate(),
    'd+': getDate.getDate(),
    'h+': getDate.getHours(),
    'm+': getDate.getMinutes(),
    's+': getDate.getSeconds(),
    'q+': Math.floor((getDate.getMonth() + 3) / 3),
    'S': getDate.getMilliseconds()
  };
  
  if (/(y+)/.test(fmt) || /(Y+)/.test(fmt)) {
    const year = getDate.getFullYear().toString();
    const match = RegExp.$1;
    if (match.length === 4) {
      // YYYY 格式
      fmt = fmt.replace(match, year);
    } else {
      // 其他年份格式（如 YY）
      fmt = fmt.replace(match, year.substr(year.length - match.length));
    }
  }
  
  for (let k in o) {
    if (new RegExp('(' + k + ')').test(fmt)) {
      const match = RegExp.$1;
      const val = o[k];
      if (k === 'M+' || k === 'd+' || k === 'h+' || k === 'm+' || k === 's+') {
        // 处理月份、日期、小时、分钟、秒的前导零
        fmt = fmt.replace(match, match.length === 1 ? val : ('00' + val).substr(('' + val).length));
      } else {
        // 其他格式保持原样
        fmt = fmt.replace(match, match.length === 1 ? val : ('00' + val).substr(('' + val).length));
      }
    }
  }
  
  return fmt;
}

// 生成首页路由
export function generateIndexRouter(data) {
let indexRouter = [{
          path: '/',
          name: 'dashboard',
          //component: () => import('@/components/layouts/BasicLayout'),
          component: resolve => require(['@/components/layouts/TabLayout'], resolve),
          meta: { title: '首页' },
          redirect: '/dashboard/analysis',
          children: [
            ...generateChildRouters(data)
          ]
        },{
          "path": "*", "redirect": "/404", "hidden": true
        }]
  return indexRouter;
}

/**
 * 深度克隆对象、数组
 * @param obj 被克隆的对象
 * @return 克隆后的对象
 */
export function cloneObject(obj) {
  return JSON.parse(JSON.stringify(obj))
}

/**
 * 随机生成数字
 *
 * 示例：生成长度为 12 的随机数：randomNumber(12)
 * 示例：生成 3~23 之间的随机数：randomNumber(3, 23)
 *
 * @param1 最小值 | 长度
 * @param2 最大值
 * @return int 生成后的数字
 */
export function randomNumber() {
  // 生成 最小值 到 最大值 区间的随机数
  const random = (min, max) => {
    return Math.floor(Math.random() * (max - min + 1) + min)
  }
  if (arguments.length === 1) {
    let [length] = arguments
  // 生成指定长度的随机数字，首位一定不是 0
    let nums = [...Array(length).keys()].map((i) => (i > 0 ? random(0, 9) : random(1, 9)))
    return parseInt(nums.join(''))
  } else if (arguments.length >= 2) {
    let [min, max] = arguments
    return random(min, max)
  } else {
    return Number.NaN
  }
}

/**
 * 随机生成字符串
 * @param length 字符串的长度
 * @param chats 可选字符串区间（只会生成传入的字符串中的字符）
 * @return string 生成的字符串
 */
export function randomString(length, chats) {
  if (!length) length = 1
  if (!chats) chats = '0123456789qwertyuioplkjhgfdsazxcvbnm'
  let str = ''
  for (let i = 0; i < length; i++) {
    let num = randomNumber(0, chats.length - 1)
    str += chats[num]
  }
  return str
}

/**
 * 随机生成uuid
 * @return string 生成的uuid
 */
export function randomUUID() {
  let chats = '0123456789abcdef'
  return randomString(32, chats)
}

/**
 * 下划线转驼峰
 * @param string
 * @returns {*}
 */
export function underLine2CamelCase(string){
  return string.replace( /_([a-z])/g, function( all, letter ) {
    return letter.toUpperCase();
  });
}

/**
 * 判断是否显示办理按钮
 * @param bpmStatus
 * @returns {*}
 */
export function showDealBtn(bpmStatus){
  if(bpmStatus!="1"&&bpmStatus!="3"&&bpmStatus!="4"){
    return true;
  }
  return false;
}

/**
 * 增强CSS，可以在页面上输出全局css
 * @param css 要增强的css
 * @param id style标签的id，可以用来清除旧样式
 */
export function cssExpand(css, id) {
  let style = document.createElement('style')
  style.type = "text/css"
  style.innerHTML = `@charset "UTF-8"; ${css}`
  // 清除旧样式
  if (id) {
    let $style = document.getElementById(id)
    if ($style != null) $style.outerHTML = ''
    style.id = id
  }
  // 应用新样式
  document.head.appendChild(style)
}


/** 用于js增强事件，运行JS代码，可以传参 */
// options 所需参数：
//    参数名         类型            说明
//    vm             VueComponent    vue实例
//    event          Object          event对象
//    jsCode         String          待执行的js代码
//    errorMessage   String          执行出错后的提示（控制台）
export function jsExpand(options = {}) {

  // 绑定到window上的keyName
  let windowKeyName = 'J_CLICK_EVENT_OPTIONS'
  if (typeof window[windowKeyName] != 'object') {
    window[windowKeyName] = {}
  }

  // 随机生成JS增强的执行id，防止冲突
  let id = randomString(16, 'qwertyuioplkjhgfdsazxcvbnm'.toUpperCase())
  // 封装按钮点击事件
  let code = `
    (function (o_${id}) {
      try {
        (function (globalEvent, vm) {
          ${options.jsCode}
        })(o_${id}.event, o_${id}.vm)
      } catch (e) {
        o_${id}.error(e)
      }
      o_${id}.done()
    })(window['${windowKeyName}']['EVENT_${id}'])
  `
  // 创建script标签
  const script = document.createElement('script')
  // 将需要传递的参数挂载到window对象上
  window[windowKeyName]['EVENT_' + id] = {
    vm: options.vm,
    event: options.event,
    // 当执行完成时，无论如何都会调用的回调事件
    done() {
      // 执行完后删除新增的 script 标签不会撤销执行结果（已产生的结果不会被撤销）
      script.outerHTML = ''
      delete window[windowKeyName]['EVENT_' + id]
    },
    // 当js运行出错的时候调用的事件
    error(e) {
      console.group(`${options.errorMessage || '用户自定义JS增强代码运行出错'}（${new Date()}）`)
      console.error(e)
      console.groupEnd()
    }
  }
  // 将事件挂载到document中
  script.innerHTML = code
  document.body.appendChild(script)
}






/**
 * 如果值不存在就 push 进数组，反之不处理
 * @param array 要操作的数据
 * @param value 要添加的值
 * @param key 可空，如果比较的是对象，可能存在地址不一样但值实际上是一样的情况，可以传此字段判断对象中唯一的字段，例如 id。不传则直接比较实际值
 * @returns {boolean} 成功 push 返回 true，不处理返回 false
 */
export function pushIfNotExist(array, value, key) {
  for (let item of array) {
    if (key && (item[key] === value[key])) {
      return false
    } else if (item === value) {
      return false
    }
  }
  array.push(value)
  return true
}

/**
 * 可用于判断是否成功
 * @type {symbol}
 */
export const succeedSymbol = Symbol()
/**
 * 可用于判断是否失败
 * @type {symbol}
 */
export const failedSymbol = Symbol()

/**
 * 使 promise 无论如何都会 resolve，除非传入的参数不是一个Promise对象或返回Promise对象的方法
 * 一般用在 Promise.all 中
 *
 * @param promise 可传Promise对象或返回Promise对象的方法
 * @returns {Promise<any>}
 */
export function alwaysResolve(promise) {
  return new Promise((resolve, reject) => {
    let p = promise
    if (typeof promise === 'function') {
      p = promise()
    }
    if (p instanceof Promise) {
      p.then(data => {
        resolve({ type: succeedSymbol, data })
      }).catch(error => {
        resolve({ type: failedSymbol, error })
      })
    } else {
      reject('alwaysResolve: 传入的参数不是一个Promise对象或返回Promise对象的方法')
    }
  })
}

/**
 * 简单实现防抖方法
 *
 * 防抖(debounce)函数在第一次触发给定的函数时，不立即执行函数，而是给出一个期限值(delay)，比如100ms。
 * 如果100ms内再次执行函数，就重新开始计时，直到计时结束后再真正执行函数。
 * 这样做的好处是如果短时间内大量触发同一事件，只会执行一次函数。
 *
 * @param fn 要防抖的函数
 * @param delay 防抖的毫秒数
 * @returns {Function}
 */
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

/**
 * 不用正则的方式替换所有值
 * @param text 被替换的字符串
 * @param checker  替换前的内容
 * @param replacer 替换后的内容
 * @returns {String} 替换后的字符串
 */
export function replaceAll(text, checker, replacer) {
  let lastText = text
  text = text.replace(checker, replacer)
  if (lastText !== text) {
    return replaceAll(text, checker, replacer)
  }
  return text
}

/**
 * 获取事件冒泡路径，兼容 IE11，Edge，Chrome，Firefox，Safari
 * 目前使用的地方：JEditableTable Span模式
 */
export function getEventPath(event) {
  let target = event.target
  let path = (event.composedPath && event.composedPath()) || event.path

  if (path != null) {
    return (path.indexOf(window) < 0) ? path.concat(window) : path
  }

  if (target === window) {
    return [window]
  }

  let getParents = (node, memo) => {
    memo = memo || []
    const parentNode = node.parentNode

    if (!parentNode) {
      return memo
    } else {
      return getParents(parentNode, memo.concat(parentNode))
    }
  }
  return [target].concat(getParents(target), window)
}

/**
 * 根据组件名获取父级
 * @param vm
 * @param name
 * @returns {Vue | null|null|Vue}
 */
export function getVmParentByName(vm, name) {
  let parent = vm.$parent
  if (parent && parent.$options) {
    if (parent.$options.name === name) {
      return parent
    } else {
      let res = getVmParentByName(parent, name)
      if (res) {
        return res
      }
    }
  }
  return null
}

/**
 * 使一个值永远不会为（null | undefined）
 *
 * @param value 要处理的值
 * @param def 默认值，如果value为（null | undefined）则返回的默认值，可不传，默认为''
 */
export function neverNull(value, def) {
  return value == null ? (neverNull(def, '')) : value
}

/**
 * 根据元素值移除数组中的一个元素
 * @param array 数组
 * @param prod 属性名
 * @param value 属性值
 * @returns {string}
 */
export function removeArrayElement(array, prod, value) {
  let index = -1
  for(let i = 0;i<array.length;i++){
    if(array[i][prod] == value){
      index = i;
      break;
    }
  }
  if(index>=0){
    array.splice(index, 1);
  }
}

/** 判断是否是OAuth2APP环境 */
export function isOAuth2AppEnv() {
  return /wxwork|dingtalk/i.test(navigator.userAgent)
}



/**
 * JS实现AOP切面
 *
 * @param obj 包含函数的对象
 * @param funcName 要切面的函数名
 * @param callback 执行方法前的回调，用于切面，callback的返回值就是funcName最终的返回值
 */
export function aspectAroundFunction(obj, funcName, callback) {
  if (typeof callback !== 'function' || !obj) {
    console.warn('【aspectAroundFunction】obj或callback格式不正确')
    return
  }
  // 保存原来的函数
  let func = obj[funcName]
  if (typeof func !== 'function') {
    console.warn('【aspectAroundFunction】' + funcName + '不是一个方法')
    return
  }
  // 赋值新方法
  // 实现当外部调用 funcName 时，首先调用我定义的新方法
  // 然后调用传入的callback方法，以决定是否执行 funcName，以及更改参数、返回值
  obj[funcName] = function (...args) {
    return callback({
      args,
      // 只有执行 proceed 才会真正执行给定的 funcName 方法
      proceed() {
        try {
          return func.apply(obj, args)
        } catch (e) {
          console.error(e)
        }
      },
    })
  }
}


/**
 * @param {Function} func
 * @param {number} wait
 * @param {boolean} immediate
 * @return {*}
 */
 export function debounce(func, wait, immediate) {
    let timeout, args, context, timestamp, result
  
    const later = function() {
      // 据上一次触发时间间隔
      const last = +new Date() - timestamp
  
      // 上次被包装函数被调用时间间隔 last 小于设定时间间隔 wait
      if (last < wait && last > 0) {
        timeout = setTimeout(later, wait - last)
      } else {
        timeout = null
        // 如果设定为immediate===true，因为开始边界已经调用过了此处无需调用
        if (!immediate) {
          result = func.apply(context, args)
          if (!timeout) context = args = null
        }
      }
    }
  
    return function(...args) {
      context = this
      timestamp = +new Date()
      const callNow = immediate && !timeout
      // 如果延时不存在，重新设定延时
      if (!timeout) timeout = setTimeout(later, wait)
      if (callNow) {
        result = func.apply(context, args)
        context = args = null
      }
  
      return result
    }
}

/**
 *tree扁平化
 */
 export function  treeToArray(tree) {
    return tree.reduce((res, item) => {
        const { children, ...i } = item
        return res.concat(i, children && children.length ? treeToArray(children) : [])
    }, [])
}
/**
 * 数组对象去重
 * @param {*} arr 
 * @returns 
 */
 export function deweight(arr,type) {
   let key = type || 'id'
  let map = new Map()
  for (let i of arr) {
      if (!map.has(i[key])) {
          map.set(i[key], i)
      }
  }
  arr = [...map.values()]
  return arr
}

/**
 * 数组更改项目属性
 * @param {*} arr
 * @param {*} arr
 * @param {} condition 触发条件
 */

export function setArrayItemProperty(list,obj) {
  list.map(item=>{
    Object.keys(obj).forEach(key=>{
      item[key] = obj[key]
    })
    if(item.children){
      setArrayItemProperty(item.children)
    }
  })
  return list;
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
    if (pair[0] == val) { return pair[1]; }
  }

  return (false);
}

/**
 * 根据指定字符截取字符串最后一个字符
 */
export function getLastSegmentAfterSlash(String,character) {
  // 找到最后一个 '/' 的索引位置
  const lastIndex = String.lastIndexOf(character);

  // 如果找到了 '/'，则截取最后一个 '/' 后面的字符
  if (lastIndex !== -1) {
    return String.substring(lastIndex + 1);
  } else {
    // 如果没有找到 '/'，则返回原始字符串
    return String;
  }
}


