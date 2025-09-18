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

export const RuleListEnum = {
  
  'num':/^(?=.*[0-9])/,
  'lower':/^(?=.*[a-z])/,
  'upper':/^(?=.*[A-Z])/,
  'special': /^(?=.*[~!@$%^&*()\_\-\+={},\<\>./?\[\]\|])(?=\S*$)/, 
  'num_lower':/(?=.*[\d])?(?=.*[a-z])(?=.*[\d])/,
  'num_upper':/(?=.*[\d])?(?=.*[A-Z])(?=.*[\d])/,
  'num_special':/^(?=.*\d)(?=.*[~!@$%^&*()\_\-\+={},\<\>./?\[\]\|])(?=\S*$)[\d~!@#$%^&*]{2,}$/,
  'lower_upper':/^(?=.*[A-Z])(?=.*[a-z])/,
  'lower_special':/^(?=.*[~!@$%^&*()\_\-\+={},\<\>./?\[\]\|])(?=.*[a-z])/,
  'upper_special':/^(?=.*[~!@$%^&*()\_\-\+={},\<\>./?\[\]\|])(?=.*[A-Z])/,
  'num_lower_upper':/^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])/,
  'num_lower_special':/^(?=.*[0-9])(?=.*[a-z])(?=.*[~!@$%^&*()\_\-\+={},\<\>./?\[\]\|])/,
  'num_upper_special':/^(?=.*[0-9])(?=.*[A-Z])(?=.*[~!@$%^&*()\_\-\+={},\<\>./?\[\]\|])/,
  'lower_upper_special':/^(?=.*[a-z])(?=.*[A-Z])(?=.*[~!@$%^&*()\_\-\+={},\<\>./?\[\]\|])/,
  'num_lower_upper_special':/^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[~!@$%^&*()\_\-\+={},\<\>./?\[\]\|])(?=\S*$)/,
}

export function validatePass(rule,str){
  return rule.test(str)
}

export function CalculateScore(val){
  const levels = {STRONG:"强",AVERAGE:"一般",WEAK:"弱",};
  const pass = { num:0, lower:0, upper:0,other:0, len:function () {
      return this.num + this.lower + this.upper + this.other;
  },
  score:function () {
      var sum = 0;
      if(this.len() === 6 || this.len() === 7) {
          sum += 10;
      } else if(this.len() >= 8) {
          sum += 15;
      }
      if(this.lower > 0 && this.upper > 0) {
          sum += 20;
      } else if(this.lower > 0 || this.upper >0) {
          sum + 10;
      }
      if(this.num >1) {
          sum += 20;
      } else if( this.num > 0) {
          sum += 10;
      }
      if(this.other > 1) {
          sum += 25;
      } else if(this.other > 0) {
          sum += 10;
      }
      if(this.num >0 && this.lower > 0 && this.upper > 0 && this.other > 0) {
          sum += 5;
      } else if(this.num > 0  && this.other > 0 && (this.lower > 0 || this.upper >0)) {
          sum += 3;
      } else if(this.num > 0 && (this.lower > 0 || this.upper >0)) {
          sum += 2;
      }
      return sum;
  },
  level:function () {
      var s = this.score();
      if(s >= 70) {
        return "STRONG";
      } else if(s >= 50) {
        return "AVERAGE";
      } else {
          return "WEAK";
      }
  },
  barColor:function() {
    var s = this.score();
      if(s >= 70) {
        return "#87d068";
      } else if(s >= 50) {
        return "#f3b020";
      } else {
          return "#f50";
      }
  },
  clear:function () {
        this.num =0;
        this.lower = 0;
        this.upper = 0;
        this.other = 0;
    }};
    var initPass = function(val) {
        pass.clear();
        if(!val) return 0;
        for(var i=0; i<val.length; i++) {
            var c = val.charCodeAt(i);
            if(c >= 48 && c <= 57) {
                pass.num++;
            } else if(c >= 97 && c <= 122) {
                pass.lower++;
            } else if(c >= 65 && c <= 90) {
                pass.upper++;
            } else {
                pass.other++;
            }
        }
    }
    initPass(val);
    return {
      levelText:levels[pass.level()],
      level:pass.level(),
      score:pass.score(),
      color:pass.barColor()
    }
}