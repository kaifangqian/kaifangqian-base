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
  <div class="doc-params-container">
    <div class="full-parameter-content">
      <div class="doc-params-set">
        <a-tabs>
          <a-tab-pane key="1" tab="参数填写">
            <a-radio-group v-model:value="isRequired" name="radioGroup" @change="handleFilterParams" :disabled="isDetail">
                <a-radio :value="0">全部</a-radio>
                <a-radio :value="1">仅看必填</a-radio>
                <a-radio :value="2">仅看待我填写</a-radio>
              </a-radio-group>
          </a-tab-pane>
         
        </a-tabs>
      </div>
      <div class="doc-item" v-for="(docItem,docIndex) in documentList" :key="docIndex">
          <div class="doc-header">
            <Icon icon="ant-design:container-outlined" size="18"></Icon>
            <span>{{ docItem.documentName }}</span>
          </div>
          <ul>
              <li v-for="(item,index) in docItem.activeControl" :key="index">
              <div v-if="(item.controlType == 'line-text' || item.controlType =='multiline-text' || item.controlType == 'date' || item.controlType =='number') && loadShow(item)">
                  <div class="params-header">
                    <a-tooltip>
                      <template #title>点击后，定位到该参数所在文档位置</template>
                      <SvgIcon name="location" :size="24" class="control-pos" @click="handleControlPos(docItem,item)"></SvgIcon>
                    </a-tooltip>
                    <span class="params-name">  <SvgIcon v-if="item.isRequired == 1" name="required" :size="16" class="control-pos"></SvgIcon>{{ item.name }}</span>
                  </div>
                  <div class="params-body"> 
                    <div class="params-select">
                      <a-input v-if="item.controlType == ControlType.LineText" v-model:vaue="item.value" @change="(e)=>handleWriteChange(e, item)" :placeholder="item.value" :disabled="item.isMineFlag!=1 || isDetail"></a-input>
                      <a-input v-if="item.controlType == ControlType.Number"  type="number" v-model:vaue="item.value" @change="(e)=>handleWriteChange(e, item)" :placeholder="item.value" :disabled="item.isMineFlag!=1 || isDetail"></a-input>
                      <a-textarea v-if="item.controlType == ControlType.MultilineText"  :auto-size="{ minRows: 2, maxRows: 5 }" v-model:vaue="item.value" @change="(e)=>handleWriteChange(e, item)" :placeholder="item.value" :disabled="item.isMineFlag!=1 || isDetail"></a-textarea>
                      <a-date-picker style="width:100%" v-if="item.controlType == ControlType.Date"  :auto-size="{ minRows: 2, maxRows: 5 }" v-model:vaue="item.value" @change="(e)=>handleWriteDateChange(e, item)" :placeholder="item.value" :disabled="item.isMineFlag!=1 || isDetail"></a-date-picker>
                    </div>
                  </div>
                </div>
              </li>
        </ul>
      </div>
     
    </div>
  </div>
</template>

<script lang="ts">
import {ref,defineComponent, onMounted} from "vue";
import { Icon,SvgIcon} from "/@/components/Icon";
// import { controllerListData } from '../mock/json';
import {ControlType} from "./data/ControlData";
import dayjs from 'dayjs';

export default defineComponent({
  name:"FullParameter",
  components:{
    Icon,
    SvgIcon,
  },
  props:{
    controlList:{
      type:Object,
    },
    signerList:{
      type:Object,
    },
    documentList:{
      type:Object,
    },
    signRuId:{
      type:String,
    },
    isDetail:{
      type:Boolean,
    },
  },
  setup(props,{emit}) {
    const docControl:any = ref([]);
    const options:any = ref([])
    const isRequired = ref(0);
    

    onMounted(()=>{

    })
    function handleWriteChange(e,item){
      // console.log(e,item,'变更值')
      item.value = e.target.value;
    }
    function handleWriteDateChange(e,item){
      // console.log(e,item,'变更时间')
      item.value = dayjs(e).format('YYYY-MM-DD');
    }
    //参数过滤
    function handleFilterParams(val){
      console.log(val)
      if(val==2){

      }
    }
    function handleControlPos(doc,control){
      emit('scroll',doc,control)
    }
    function loadShow(row){
      if(isRequired.value==0){
        return true
      }
      if(isRequired.value==1){
        return row.isRequired ==1?true:false
      }
      if(isRequired.value==2){
        return row.isMineFlag ==1?true:false
      }
    }

    return {
      docControl,
      options,
      handleWriteChange,
      isRequired,
      handleFilterParams,
      ControlType,
      handleWriteDateChange,
      loadShow,
      handleControlPos,
    }
  }
})
</script>

<style lang="less" scoped>
.full-parameter-content{
  .doc-params-set{
    box-shadow: 5px 5px 20px #0000001a;
    padding:10px 15px 20px;
    margin: 10px 5px 0px;
  }
  .doc-item{
    box-shadow: 5px 5px 20px #0000001a;
    margin: 10px 5px 40px;
    padding: 10px 15px;
    .doc-header{
      display: flex;
      align-items: center;
      margin-bottom:10px;
      font-size: 14px;
      font-weight: 550;
      span{
        margin-right:10px;
      }

    }
  }
  li{
    margin-bottom: 10px;
  }
  .params-header{
    display: flex;
    align-items: center;
      .params-name{
      font-size: 12px;
      font-weight: 550;
    }
  }
  .params-body{
    // background: #f9f9f9;
    padding:8px 0px;
    .params-select{
      display: flex;
      align-items: center;
      span{
        white-space: nowrap;
      }
      :deep(.ant-select){
        width:100%;
        flex:1;
      }
    }
  }
}
</style>
