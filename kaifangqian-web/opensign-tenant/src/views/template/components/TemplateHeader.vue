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
    <div class="template-header">
      <div class="template-header-title">
        <a-button type="text" size="large" @click="previousStep.callBack">
          <template #icon>
               <Icon :icon="previousStep.icon" style="font-size: 20px;"/>
               <!-- ant-design:close-outlined -->
          </template>
        </a-button>
        <div class="header-name" :title="previousStep.title">
          <span class="after" v-if="previousStep.title">
            {{previousStep.title}}
          </span>
        </div>
      </div>
      
      <div class="template-header-center"> 
        <slot></slot>
      </div>
      <div class="template-header-action">
        <template v-for="item in actions">
					<a-button v-if="!item.disabled && item.show" :type="item.type" @click="item.callBack" class="doc-action">
            {{item.text}}
          </a-button>
				</template>
      </div>
    </div>
</template>

<script lang="ts">
import {ref, unref, defineComponent,watch} from "vue"
import { Icon } from '/@/components/Icon';
import { useRouter } from 'vue-router';
import {backParent} from "/@/utils";
export default defineComponent({
  name:"DocHeader",
  components:{
    Icon
  },
  props:{
    previousStep:{
      type:Object,
    },
    actions:{
      type:Object,
    },
    doc:{
      type:Object,
    },
    showDoc:{
      type:Boolean,
      default:true
    }
  },
  setup(props,{emit}) {
      // const currentDocId = ref(props.docId)

      const router = useRouter();
      // const { currentRoute } = router;
      // const pageSource = route.query.from;
       
      function handleCancel(){
        //emit('cancel')
        backParent();
      }
    return {
          handleCancel,
          // pageSource,
    }
  }
})
</script>

<style lang="less" scoped>
  
.template-header{
  display: flex;
  height: 64px;
  position: fixed;
  top:0;
  border-bottom: 1px solid rgba(0, 0, 0, 0.08);
  width:100%;
}
.template-header-center{
  height: 64px;
  flex: 1;
  text-align: center;
  min-width:800px;
}
.template-header-title{
  width: 350px;
  line-height: 64px;
  padding-left: 20px;
  box-sizing: border-box;
  display: flex;
  align-items: center;
  .header-name{
  	font-size: 20px;
  	font-weight: 600;
  	position: relative;
  	padding-left: 30px;
  	/* width: 240px; */
    flex: 1;
  	overflow: hidden;
  	text-overflow: ellipsis;
  	white-space: nowrap;
  }
  .header-name .after::after{
  	content: "";
  	position: absolute;
  	width: 2px;
  	height: 18px;
  	background-color: #999;
  	left: 10px;
  	top:22px
  }
}

.template-header-action{
  width: 350px;
  text-align: right;
  line-height: 64px;
  padding-right: 20px;
  box-sizing: border-box;
}
.doc-action{
  margin:0 5px;
}

</style>
