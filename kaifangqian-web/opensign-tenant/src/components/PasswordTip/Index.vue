<!--
  @description 开放签

  Copyright (C) [2025] [版权所有者（北京资源律动科技有限公司）]. All rights reserved.

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU Affero General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Affero General Public License for more details.

  You should have received a copy of the GNU Affero General Public License
  along with this program.  If not, see <https://www.gnu.org/licenses/>.

  注意：本代码基于 AGPLv3 协议发布。若通过网络提供服务（如 Web 应用），
  必须公开修改后的完整源码（包括衍生作品），详见协议全文。
-->

<template>
  <div :style="wrapStyles">
      <a-tooltip
        :destroyTooltipOnHide="true"
        placement="bottom" 
        color="#fff" 
        v-model="state.passwordTipVisible" 
        trigger="click" 
        :overlayStyle="overlayStyle" 
        :getPopupContainer="setPoupContainer" 
        @visibleChange="handleChange">
          <template #title>
              <div class="rule-tip-list">
                <div v-for="(item,index) in ruleList" :key="index">
                  <Icon class="tip-icon" :icon='!item.status?"ant-design:close-circle-outlined" : "ant-design:check-circle-outlined"' :color="!item.status?'#ff5500' : '#87d068'" />
                  <span style="color:#333">{{item.tipText}}</span>
                </div>
              </div>
              <div class="strength-bar">
                <p class="strength-title">密码强度：{{state.passwordStrength}}</p>
                <a-progress :percent="state.percent"  :strokeColor="state.strokeColor"/>
              </div>  
          </template>
          <slot name="passwod"></slot>
      </a-tooltip>
  </div>
</template>
<script lang='ts'>
import { defineComponent,reactive,ref,watch, onMounted } from 'vue';
import { getSystemPasswordLength,getSystemPasswordConfig } from '/@/api/sys/safe';
import { PassRuleProps } from './rule';
import Icon from '/@/components/Icon';
import { CalculateScore ,RuleListEnum} from './util';

export default defineComponent({
  name: 'Index',
  components:{
    Icon
  },
  props:PassRuleProps,
  setup( props, { }){

    // /((?=.*\d)(?=.*\D)|(?=.*[a-zA-Z])(?=.*[^a-zA-Z]))(?!^.*[\u4E00-\u9FA5].*$)/
    const ruleList = ref([
      {
        status:false,
        tipText:'至少8-32 个字符',
        reg:/^.{8,32}$/
      },
      {
        status:false,
        tipText:'只能包含数字、字母以及特殊符号(非空格)',
        reg: /^[~!@$%^&*()\_\-\+={},\<\>./?\[\]\|A-Za-z0-9]+$/
      },
      // {
      //   status:false,
      //   tipText:'至少8-32 个字符',
      //   reg:''
      // },
    ])

  
    const overlayStyle = {
      "min-width":'350px'
    }

    const wrapStyles: Record<string, string> = {
      position: 'relative',
    };

    const state = reactive({
        passwordStrength:'弱',
        passwordTipVisible:false,
        strokeColor:'#ff4d4f',
        percent:5,
        passwordComposition:[],
        passwodRuleKey:''
    })

    onMounted( async () => {
        let passwordRule = await getSystemPasswordConfig({types:'password_composition'});
        let passwordLength = await getSystemPasswordLength();
        // console.log(passwordLength,'---密码长度---')
        if(passwordRule){
          state.passwordComposition = passwordRule.value.split(',');
          state.passwodRuleKey = passwordRule.value.replaceAll(',','_');
          console.log( state.passwodRuleKey ,'规则11')
        }
        if(passwordLength){
          let length = passwordLength.value;
          ruleList.value[0] = {
            status:false,
            tipText:`至少${length}-32 个字符`,
            reg: new RegExp("^\.{"+ length +",32}$")
          }
        }
        generatePasswprdRule();

    })

    
    watch(
        () => props.password,
        (val) => {
            validatePassword(val);
            validateStrong(val);
        },
        {
          immediate: true,
        },
      );
    function reset(){
        Object.assign(state, {
          passwordStrength:'弱',
          passwordTipVisible:false,
          strokeColor:'#ff4d4f',
          percent:5
        })
    }

    function generatePasswprdRule(){
      let systemPasswordArr:string[] = [];
      let systemPasswordObject = {
        'num':'数字',
        'lower':'小写字母',
        'upper':'大写字母',
        'special':'特殊符号（空格除外）',
      }
      if(state.passwordComposition.length){
        state.passwordComposition.map(key=>{
          systemPasswordArr.push(systemPasswordObject[key]);
        })
      }else{
        
      }
      // console.log(systemPasswordArr,'结果-------');
      let validateStr:string = '';
      // let array = ['数字', '小写字母','大写字母', '特殊符号（空格除外）'];
      let array = systemPasswordArr;
      let allCompositionArr:string[] = [];
        for(let i = 0, len1 = array.length; i < len1; i++) {
          let one = array[i];
          if(array.length === 1){
            // console.log(array[i]);
            validateStr = `需同时包含${array[i]}`;
          }
          allCompositionArr.push(one);
          let a2 = array.concat();
          a2.splice(0, i + 1);
          for(let j = 0, len2 = a2.length; j < len2; j++) { 
            // console.log(array[i] + ' ' +a2[j]);
            if(array.length === 2){
              validateStr = `需同时包含${array[i] + '和' +a2[j]}`;
            }
           
            allCompositionArr.push(array[i] + ' ' +a2[j]);
            let a3 = a2.concat();
            a3.splice(0, j + 1);
            for(let k = 0, len3 = a3.length; k < len3; k++) {
              // console.log(array[i] + ' ' +a2[j] + ' ' + a3[k]);
              if(array.length === 3){
                validateStr = `需同时包含${array[i] + '和' +a2[j] + ' ' + a3[k]}`;
              }
             
              allCompositionArr.push(array[i] + ' ' +a2[j] + ' ' + a3[k]);
              let a4 = a3.concat();
              a4.splice(0, k + 1);
              for(let l =  0,len4 = a4.length; l < len4; l++){
                // console.log(array[i] + ' ' +a2[j] + ' ' + a3[k] + ' '+ a4[l]);
                if(array.length === 4){
                    validateStr = `需同时包含${array[i] + ' ' +a2[j] + ' ' + a3[k] + ' '+ a4[l]}`;
                }
                 allCompositionArr.push(array[i] + ' ' +a2[j] + ' ' + a3[k] + ' '+ a4[l]);
              }
            }
          }
        }
        ruleList.value.push({
          status:false,
          tipText:validateStr,
          reg:RuleListEnum[state.passwodRuleKey]
        })
        console.log(validateStr,RuleListEnum[state.passwodRuleKey]);
    }
    function validatePassword(password){
      password = password || '';
      console.log(ruleList,'规则---')
      ruleList.value.map(item=>{
        if(!item.reg) return
        console.log(item.reg,password, item.reg.test(password))
        item.status = item.reg.test(password)
      })

    }
    function setPoupContainer(trigger){
      return document.body;
      // return  trigger.parentNode
    }
    function handleChange(){
      state.passwordTipVisible = !state.passwordTipVisible;
    }
    function validateStrong(password){
      let result = CalculateScore(password);
      // console.log(result,'----计算结果---');
      if(result){
        state.percent = result.score>70?100:result.score;
        state.passwordStrength = result.levelText;
        state.strokeColor = result.color;
      }
    }

    return {
      ruleList,
      overlayStyle,
      state,
      setPoupContainer,
      wrapStyles,
      handleChange
    }
  }
})
</script>
<style lang="less" scoped>
.tip-icon{
  margin-right:10px;
}
.rule-tip-list{
  padding:5px 3px;
}
.strength-title{
  color:#333;
  font-size:14px;
}
.strength-bar{
  padding: 4px 7px;
  border-top: 1px dashed #e4e4e4;
  p{
    margin-bottom:5px;
  }
  :deep(.ant-progress-text){
    display:none;
    // color:#333;
  }
}
// .ant-tooltip{
//   :deep(.ant-tooltip-inner){
//     background-color:#fff;
//   }
// }

.custom-password-tooltip{
  :deep(.ant-progress-outer){
    padding-right:0;
  }
  :deep(.ant-tooltip-inner){
    height:150px;
    // background-color:#fff;
    color:#333
  }
  // :deep(.ant-tooltip-arrow-content){
  //    background-color:#fff;
   
  // }
}
 
</style>
