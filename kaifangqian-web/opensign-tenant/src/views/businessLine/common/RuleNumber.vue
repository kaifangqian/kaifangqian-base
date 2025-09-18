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
  <div class="container">
    <Form :model="state">
      <draggable :list="state.rules" :animation="300"   ghost-class="ghost" chosen-class="chosenClass">
        <div class="rule-item" v-for="(element,index) in state.rules" :key="index">
          <Row class="enter-x" :gutter="6">
                <Col :span="6">
                  <Icon icon="ant-design:holder-outlined"/>
                  <span>{{ element.ruleType }}</span>
                </Col>
                <Col :span="12" v-if="element.ruleType==='text'">
                  <FormItem :name="['ruleList', index, 'text']" :rules="{required:true, message:'内容不能空'}">
                    <Input   v-model:value="element.text"  placeholder="请输入" v-if="element.edit"/>
                    <span v-else>{{ element.text }}</span>
                  </FormItem>
                </Col>
                <Col :span="12" v-if="element.ruleType==='datetime'">
                  <FormItem >
                    <Select v-model:value="element.datetime" placeholder="请选择日期" > 
                        <SelectOption value="yyyy" >年，示例：2021</SelectOption>
                        <SelectOption value="yyyyMM" >年月，示例：202101</SelectOption>
                        <SelectOption value="yyyyMMdd" >年月日，示例：20210125</SelectOption>
                        <SelectOption value="yyyyMMddHH" >年月日时，示例：2021012503</SelectOption>
                        <SelectOption value="yyyyMMddHHmm" >年月日时分，示例：202101250313</SelectOption>
                        <SelectOption value="yyyyMMddHHmmss" >年月日时分秒，示例：20210125031310</SelectOption>
                    </Select>
                  </FormItem>
                </Col>
                <Col :span="9" v-if="element.ruleType==='serialnum'">
                  <Select v-model:value="element.ruleLength" style="width:100%" placeholder="请选择序列号位数">
                      <SelectOption :value="item" :key="index" v-for="(item,index) in 10">{{item}}</SelectOption>
                  </Select>
                </Col>
            </Row>
        </div>
      
      </draggable>
    </Form>
  </div>
</template>

<script lang="ts">

import {ref,defineComponent, computed} from "vue"
import { VueDraggableNext } from 'vue-draggable-next';
import { Icon } from '/@/components/Icon';
import { Checkbox,Form, Input, Tabs, Row, Col,Select } from 'ant-design-vue';


export interface RuleItem {
    ruleType:string;
    serialType:string;
    text?:string;
    edit?:boolean
}

export default defineComponent({
  name:"RuleNumber",
  components:{
    Checkbox,Form, Input, Tabs, Row, Col,Select ,
    Icon,
    draggable:VueDraggableNext
  },
  props:{
    ruleList:{
      type:Array,
      default:function(){
        return <RuleItem[]>[]
      }

    }
  },
  setup(props,{emit}) {

    const state:any = computed(()=>{
      return {
        rules:props.ruleList
      }
    });
    
    
    return {
      state  
    }
  }
})
</script>

<style lang="less" scoped>
</style>
