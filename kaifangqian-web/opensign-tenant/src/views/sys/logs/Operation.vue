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
    <BasicTable @register="registerTable" @expand="handleRowChange">
      <template #expandedRowRender="{ record }">
        <p>请求方法: {{ record.info?.method }} </p>
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
        <p>请求时间: {{ record.info?.requestTime }} </p>
        <div class="request-params">
          <input type="checkbox" name="toggle" id="toggle1" style="display: none;" />
          <span>响应结果:</span>
           <a-popover title="json预览" >
              <template #content>
                <!-- <span class="popover-text">{{record.info?.responseParam }}</span> -->
                <json-viewer :value="jsonDataResponseParam"  boxed :sort="true" :expand-depth="5" :expanded="true" theme="light"/>
              </template>
            <p  v-if="showMore" class="request-text" ref="textRef"> {{ record.info?.responseParam }} </p>
            </a-popover>
            <p v-if="!showMore" class="request-text" ref="textRef"> {{ record.info?.responseParam}} </p>
          
        </div>
        <p>响应时间: {{ record.info?.responseTime }} </p>
        <p>请求耗时: {{ record.info?.costTime }} </p>
      </template>
      <template #operateType="{text}">
          <span>{{loadType(text)}}</span>
      </template>
      <template #logStatus="{record,text}">
        <div @click.stop="handleLogDrawer(record,text)" class="log-type">
          <!-- <a-tag :color="loadLogTypeColor(text)" style="margin:0 5px"> {{logStatusText(text)}} </a-tag> -->
          <span  :style="{color:loadLogTypeColor(text),margin:'0 5px',background:loadLogTypeBgcolor(text)}" class="a-customm-tag">{{logStatusText(text)}}</span>
          <a-tooltip title="点击查看详细内容">
              <Icon icon="ant-design:question-circle-outlined" color="#ad9c9c" v-if="text === -1"/>
          </a-tooltip>
        </div>
      </template>
      <template #logContent="{ record }">
          <span>{{ record.moduleName + '-' + record.methodName}}</span>
      </template>
    </BasicTable>
    <OperationDrawer @register="registerDrawer" @success="handleSuccess" />
</template>
<script lang="ts">
  import { defineComponent, ref, onMounted, nextTick, unref} from 'vue';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import { operationColumns,operationSearchFormSchema } from './log';
  import { useDrawer } from '/@/components/Drawer';
  import OperationDrawer from './drawer/OperationDrawer.vue';
  import Icon from '/@/components/Icon';
  import { Tooltip } from 'ant-design-vue';
  // import JsonViewer from "vue-json-viewer";
  import { getSyslog,getSyslogInfo} from '/@/api/sys/log';
  // import dayjs, { Dayjs } from 'dayjs';


  export default defineComponent({
    name:'Operation',
    components: { BasicTable, TableAction, OperationDrawer, Icon, Tooltip },
    setup() {
      
      const jsonDataRequestParam = ref();
      const jsonDataResponseParam = ref();
      const showMore = ref(false);
      const textRef = ref<HTMLDivElement | null>(null);
      const [registerDrawer, { openDrawer }] = useDrawer();
      const [registerTable] = useTable({
        api: getSyslog,
        title: '',
        columns: operationColumns,
        fetchSetting:{
          listField:'records'
        },
        searchInfo:{
          logType:2
        },
        striped:false,
        maxHeight:200,
        rowKey:'id',
        canResize: false,
        useSearchForm: true,
        showIndexColumn: false,
        formConfig: {
          labelWidth: 80,
          schemas: operationSearchFormSchema,
        },
        expandRowByClick: true,
        // expandedRowsChange:handleRowChange
      });
      async function handleRowChange(isExpand,record){
          console.log(isExpand,record, '--ssss-')
          if(!isExpand && !record.info) return;
          let result = await getSyslogInfo({id:record.id});
          if(result){
            record.info = result;
            nextTick(()=>{
              jsonDataRequestParam.value =  JSON.parse(result.requestParam);
              jsonDataResponseParam.value =  JSON.parse(result.responseParam);
              const textDom = unref(textRef);
              if(textDom&&textDom.clientHeight>=(22*3)){
                showMore.value = true;
              }
            })
             console.log( JSON.parse(result.requestParam),'--------json数据---')
          }
          
      }

      onMounted(()=>{
       
      })
      function handleDelete(record: Recordable) {
        console.log('点击了删除', record);
      }
      function handleOpen(record: Recordable) {
        console.log('点击了启用', record);
      }
      function loadType(status:number){
        switch(status){
          case 0:
            return '操作';
          case 1:
            return '查询';
          case 2:
            return '添加';
          case 3:
            return '修改';
          case 4:
            return '删除';
          case 5:
            return '导入';
          case 6:
            return '导出';
          case 7:
            return '登录';
          case 8:
            return '登出';
          default:
            return '';
        }
      }
      function loadLogTypeColor(status:number){
        switch(status){
          case -1:
            return '#E34D59';
          case 0:
            return '#ED7B2F';
          case 1:
            return '#00A870';
          default:
            return '#00A870';
        }
      }
      function loadLogTypeBgcolor(status:number){
        switch(status){
          case -1:
            return '#fdecee';
          case 0:
            return '#fef3e6';
          case 1:
            return '#e8f8f2';
          default:
            return '#e8f8f2';
        }
      }

      function handleLogDrawer(record,status){
        console.log(record, status,'-----')
        if(status === -1){
          openDrawer(true,{
            isUpdate:true,
            record:{
                errorLogId:record.errorLogId
            }
          })
        }
      }
      function logStatusText(status){

        switch(status){
          case -1:
            return '异常';
          case 0:
            return '告警';
          case 1:
            return '正常';
          default:
            return '正常';
        }

      }

      function handleSuccess(){

      }
       const dateFormat = 'YYYY/MM/DD';

      return {
        registerTable,
        handleDelete,
        handleOpen,
        loadType,
        handleRowChange,
        loadLogTypeColor,
        handleLogDrawer,
        registerDrawer,
        handleSuccess,
        logStatusText,
        loadLogTypeBgcolor,
        jsonDataRequestParam,
        jsonDataResponseParam,
        showMore,
        textRef,
      };
    },
  });
</script>
<style lang="less" scoped>
.log-type{
  cursor: pointer;
  text-align: left;
  width: 80px;
  // margin: 0 auto;
  // margin-left:20px;
  white-space: nowrap;
  .a-customm-tag{
    padding:2px 8px;
    border-radius: 2px;
  }
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
}
input[name="toggle2"]:checked {
    & + p {
      -webkit-line-clamp: unset;
    }
  }

:deep(.ant-popover-inner-content){
  min-width: 300px;
}
:deep(.ant-select-dropdown){
  top:-140px!important;
}
</style>
