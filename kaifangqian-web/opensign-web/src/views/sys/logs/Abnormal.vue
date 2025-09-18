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
    <BasicTable @register="registerTable"  @expand="handleRowChange">
      <template #expandedRowRender="{ record }">
        <div class="request-params">
          <input type="checkbox" name="toggle" id="toggle" style="display: none;" />
          <span>请求参数:</span>
           <a-popover title="json预览" >
              <template #content>
                <json-viewer :value="jsonDataRequestParam"  boxed :sort="true" :expand-depth="5" :expanded="true"/>
              </template>
            <p  v-if="showMore" class="request-text" ref="textRef"> {{ record.info?.requestParam }} </p>
            </a-popover>
            <p v-if="!showMore" class="request-text" ref="textRef"> {{ record.info?.requestParam}} </p>
        </div>
        <p>错误描述: {{ record.info?.errorMessage }} </p>
        <div class="request-params">
            <input type="checkbox" name="toggle" id="toggle2" style="display: none;" />
            <span>错误详情:</span>
            <a-popover title="json预览">
                <template #content>
                  <span class="popover-text">{{ record.info?.errorStackInfo}}</span>
                  <!-- <json-viewer :value="jsonDataErrorStack"  boxed :sort="true" :expand-depth="5" :expanded="true"/> -->
                </template>
              <p  v-if="showMore" class="request-text" ref="textRef"> {{ record.info?.errorStackInfo }} </p>
              </a-popover>
              <p v-if="!showMore" class="request-text" ref="textRef"> {{ record.info?.errorStackInfo}} </p>
          </div>
      </template>
       <template #logContent="{ record }">
          <span>{{ record.moduleName + '-' + record.methodName}}</span>
      </template>
    </BasicTable>
</template>
<script lang="ts">
  import { defineComponent, ref, nextTick, unref } from 'vue';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import { abnormalColumns,abnormalSearchFormSchema } from './log';
  import { getSysErrorlog, getSysErrorlogInfo} from '/@/api/sys/log';

  export default defineComponent({
    name:'Abnormal',
    components: { BasicTable, TableAction },
    setup() {
      const jsonDataRequestParam = ref();
      const jsonDataErrorStack = ref();
      const showMore = ref(false);
      const textRef = ref<HTMLDivElement | null>(null);
      const [registerTable] = useTable({
        api: getSysErrorlog,
        title: '',
        columns: abnormalColumns,
        fetchSetting:{
          listField:'records'
        },
        searchInfo:{
          logType:1
        },
        striped:false,
        rowKey:'id',
        canResize: false,
        useSearchForm: true,
        showIndexColumn: false,
        formConfig: {
          labelWidth: 100,
          schemas: abnormalSearchFormSchema,
        },
        expandRowByClick: true,
      });

      function handleDelete(record: Recordable) {
        console.log('点击了删除', record);
      }
      function handleOpen(record: Recordable) {
        console.log('点击了启用', record);
      }

      async function handleRowChange(isExpand,record){
          console.log(isExpand,record, '--ssss-')
          if(!isExpand && !record.info) return;
          let result = await getSysErrorlogInfo({id:record.id});
          if(result){
             record.info = result;
              nextTick(()=>{
                jsonDataRequestParam.value =  JSON.parse(result.requestParam);
                // jsonDataErrorStack.value =  JSON.parse(result.errorStackInfo);
                const textDom = unref(textRef);
                if(textDom&&textDom.clientHeight>=(22*3)){
                  showMore.value = true;
                }
            })
          }
      }

      return {
        registerTable,
        handleDelete,
        handleOpen,
        handleRowChange,
        jsonDataRequestParam,
        jsonDataErrorStack,
        showMore,
        textRef
      };
    },
  });
</script>
<style lang="less" scoped>
:deep(.ant-select-dropdown){
  top:-140px!important;
}
.request-params{
  margin-bottom:1em;
  // display: -webkit-box;
  // -webkit-line-clamp: 3;
  // -webkit-box-orient: vertical;
  // overflow: hidden;
    label {
      cursor: pointer;
      // color: @primary-color;
    }
    &:truncated {
      & + label {
      display: block;
      }
    }
    .request-text{
       display: -webkit-box;
      -webkit-line-clamp: 3;
      -webkit-box-orient: vertical;
      overflow: hidden;
      line-height: 22px;
      margin-bottom: 0;
      margin-top: -22px;
      margin-left: 60px;
      // display: inline-block;
    }
   
    input[name="toggle"]:checked {
        & + p {
          -webkit-line-clamp: unset;
        }
      }
    input[name="toggle2"]:checked {
        & + p {
          -webkit-line-clamp: unset;
        }
      }
}
</style>
