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
/**
 * 省市区
 */
export default class Area {
  /**
   * 构造器
   * @param express
   */
  constructor(pcaa) {
    if(!pcaa){
      pcaa = Vue.prototype.$Jpcaa;
    }
    let arr = []
    const province = pcaa['86']
    Object.keys(province).map(key=>{
      arr.push({id:key, text:province[key], pid:'86', index:1});
      const city = pcaa[key];
      Object.keys(city).map(key2=>{
        arr.push({id:key2, text:city[key2], pid:key, index:2});
        const qu = pcaa[key2];
        if(qu){
          Object.keys(qu).map(key3=>{
            arr.push({id:key3, text:qu[key3], pid:key2, index:3});
          })
        }
      })
    })
    this.all = arr;
  }

  get pca(){
    return this.all;
  }

  getCode(text){
    if(!text || text.length==0){
      return ''
    }
    for(let item of this.all){
      if(item.text === text){
        return item.id;
      }
    }
  }

  getText(code){
    if(!code || code.length==0){
      return ''
    }
    let arr = []
    this.getAreaBycode(code, arr, 3);
    return arr.join('/')
  }

  getRealCode(code){
    let arr = []
    this.getPcode(code, arr, 3)
    return arr;
  }

  getPcode(id, arr, index){
    for(let item of this.all){
      if(item.id === id && item.index == index){
        arr.unshift(id)
        if(item.pid != '86'){
          this.getPcode(item.pid, arr, --index)
        }
      }
    }
  }

  getAreaBycode(code, arr, index){
    for(let item of this.all){
      if(item.id === code && item.index == index){
        arr.unshift(item.text);
        if(item.pid != '86'){
          this.getAreaBycode(item.pid, arr, --index)
        }

      }
    }
  }

}