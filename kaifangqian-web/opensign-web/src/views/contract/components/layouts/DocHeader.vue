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
    <div class="contract-header">
      <div class="contract-header-back" @click="handleCancel">
        <!-- <Icon icon="ant-design:left-outlined"/>
        <span>返回</span> -->
        <a-button type="text">{{pageSource=='list'?'< 返回':'上一步'}}</a-button>
      </div>
      <div class="contract-header-title" v-if="showDoc"> 
        <slot></slot>
        <a-select  v-if="docs&&docs.length>1"  v-model:value="currentDocId"  @change="handleChangeDoc" class="doc-select">
          <a-select-option v-for="(item,index) in docs" :key="index"  :value="item.id">{{ item.name }}</a-select-option>
        </a-select>
        <a-badge :count="docs&&docs.length" v-else-if="docs&&docs.length>1"/>
        <p v-else style="margin-top:10px">{{docs&&docs.length&&docs[0].name }} </p>
        <span v-if="docs&&docs.length>1" class="doc-tio-change" @click="handleChangeDoc">
          {{docs&&docs.length + '个签约文件，点击切换'}} 
          <SvgIcon name="doc-file"></SvgIcon>
        </span>
      </div>
      <div class="contract-header-action">
        <slot></slot>
        <template v-for="(item,index) in actions">
					<a-button v-if="!item.disabled && item.show" :type="item.type" @click="item.callBack" :class="['doc-action',item.class]" :key="index" :ref="item.ref">
            <SvgIcon :name="item.svgIcon" v-if="item.svgIcon" />
            <span style="margin-left:5px;"> {{item.text}}</span>
          </a-button>
				</template>
      </div>
    </div>
</template>

<script lang="ts">
import {ref, unref, defineComponent,watch} from "vue"
import { Icon,SvgIcon } from '/@/components/Icon';
import { useRouter } from 'vue-router';
export default defineComponent({
  name:"DocHeader",
  components:{
    Icon,
    SvgIcon
  },
  props:{
    actions:{
      type:Object,
    },
    docs:{
      type:Array,
    },
    docId:{
      type:String,
    },
    showDoc:{
      type:Boolean,
      default:true
    }
  },
  setup(props,{emit}) {
      const currentDocId = ref(props.docId)
      const currentIndex = ref(1)

      const router = useRouter();
      const { currentRoute } = router;
      const route = unref(currentRoute);
      const pageSource = route.query.from;
      console.log(pageSource,'路由来源')
      watch(
        ()=>props.docId,
        (val)=>{
          currentDocId.value = val
        }
      )
      function handleChangeDoc(val){
        if(typeof val != 'string'){
          let val = props.docs[currentIndex.value]?.id;
          currentIndex.value++;
          if(currentIndex.value == props.docs.length){
            currentIndex.value = 0;
          }
          emit('change',val)
        }else{
         emit('change',val)
        }

      } 
      function handleCancel(){
        emit('cancel')
      }
    return {
          handleChangeDoc,
          currentDocId,
          handleCancel,
          pageSource,
    }
  }
})
</script>

<style lang="less" scoped>
.contract-header-title{
  :deep(.ant-select-selector){
    border:none;
    min-width: 220px;
    text-align: right;
  }
  :deep(.ant-select-selection-item){
    font-size:18px;
    font-weight:600;
  }
  :deep(.ant-select-arrow){
    color:#000;
    font-size:18px;
    margin-top:-10px;
  }
}
.doc-action{
  margin:0 5px;
}
.doc-tio-change{
  font-size:14px;
  color:#999;
  margin-left:10px;
  cursor: pointer;
}
</style>
