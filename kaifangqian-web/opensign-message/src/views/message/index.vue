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
    <div>
      <BasicTable 
        @register="registerTable" 
        @fetch-success="onFetchSuccess" 
        @row-click="handleDetail"
        style="cursor: pointer;"
      :rowSelection="{ type: 'checkbox', selectedRowKeys: checkedKeys, onChange: onSelectChange }" :pagination="pagination"
     >
        <template #headerTop  v-if="checkedKeys.length > 0">
              <a-alert type="info" show-icon>
                <template #message>
                  <template v-if="checkedKeys.length > 0">
                    <span>已选中{{ checkedKeys.length }}条记录</span>
                    <a-button type="link" @click="checkedKeys = []" size="small">清空</a-button>
                  </template>
                  <template v-else>
                    <span>未选中任何项目</span>
                  </template>
                </template>
              </a-alert>
        </template>
        <template #toolbar>
            <a-button type="default" :disabled="checkedKeys.length === 0" @click="handleDelete">批量删除 </a-button>
            <a-button type="primary" :disabled="checkedKeys.length === 0" @click="handelRead">标记已读 </a-button>
            <a-button type="primary" @click="handelAllRead">全部标记已读 </a-button>
        </template>

        <template #mesTitle="{ record, text }">
          <span
            :style="{ color: record.isRead ? '#bfbfbf' : '' }"
          >
            {{ text }}
          </span>
        </template>

      </BasicTable>
      <MessageDetailModal @register="registerModal" @success="handleSuccess" />
    </div>
  </template>
  <script lang="ts">
    import { defineComponent, ref, onMounted, unref  } from 'vue';
    import { BasicTable, useTable, TableAction } from '/@/components/Table';
    import { Tag } from 'ant-design-vue';
    import { getMessageRetry,readMyMessage,readAllMyMessage,deleteMessageBatch , getMyMessage} from '/@/api/message';
    import { messageHistoryColumns, messageHistorySearchFormSchema } from './data';
    import { useMessage } from '/@/hooks/web/useMessage';
    import { getMyMessageInfo} from '/@/api/message';
    import { objectToQueryString } from '/@/utils'
    import MessageDetailModal from './modal/MessageDetailModal.vue';
    import { useModal } from '/@/components/Modal';
    import { useUserStore } from '/@/store/modules/user';
    import { getToken } from '/@/utils/auth';
    import { Badge } from 'ant-design-vue';
    import {  getAppTokenByAuthToken } from '/@/api/sys/user';
  
    export default defineComponent({
      name: 'MyMessage',
      components: { BasicTable, TableAction,Tag, MessageDetailModal, Badge },
      setup() {
        const checkedKeys = ref<Array<string | number>>([]);
        const userStore = useUserStore();
        const userInfo =  userStore.getUserInfo;
        const { createMessage: msg } = useMessage();
        const [registerModal, { openModal }] = useModal();
        const pagination = ref({
            total: 50,
            current: 1,
            pageSize: 20,
            showSizeChanger: true,
            showQuickJumper: true,
            pageSizeOptions: ['10', '20', '30', '40', '50'],
            showTotal: (total) => `共 ${total} 条记录`,
            locale: {
                items_per_page: '条/页',
                jump_to: '跳至',
                jump_to_confirm: '确定',
                page: '页',
            },
        })

        const [registerTable, { reload, setTableData, clearSelectedRowKeys}] = useTable({
          title: '',
          api: getMyMessage,
          columns:messageHistoryColumns,
          formConfig: {
            labelWidth: 80,
            schemas: messageHistorySearchFormSchema,
          },
          immediate:true,
          useSearchForm: true,
          fetchSetting:{
            listField:'records'
          },
          tableSetting: { fullScreen: false,align:'right' },
          showIndexColumn: false,
          rowKey: 'id',
          striped: false,
          bordered: false,
          canResize: false,
          isTriggerSelect:false,
        //   actionColumn: {
        //     width: 80,
        //     title: '操作',
        //     dataIndex: 'action',
        //     slots: { customRender: 'action' },
        //     fixed: undefined,
        //   },
        })

        onMounted(()=>{
          let list = [{msgTitle:"测试", msgType:'ddssd',msgDate:'2022-09-22'}];
          setTableData(list);
        })
  
        function onSelectChange(selectedRowKeys: (string | number)[]) {
          console.log(selectedRowKeys);
          checkedKeys.value = selectedRowKeys;
        }
  
        function handleSuccess() {
          reload();
        }
        function handleReSend(record){
          console.log(record)
          getMessageRetry({id:record.id}).then(res=>{
            if(res.code){
              msg.success('发送成功')
              reload()
            }
          })
        }
    async function handelRead(){
        if(!unref(checkedKeys).length){
          msg.warning('请选择数据');
          return 
        }
        let result = await readMyMessage({ids:unref(checkedKeys).join(',')});
        if(result){
          msg.success('标记成功');
          reload();
          checkedKeys.value = [];
          clearSelectedRowKeys()
          //查询未读消息数量
        }
      }
      async function handelAllRead(){
        let result = await readAllMyMessage({});
        if(result){
          msg.success('标记成功');
          reload();
        }
      }
      async function handleDelete(){
        if(!unref(checkedKeys).length){
          msg.warning('请选择数据');
          return 
        }
        let result = await deleteMessageBatch({ids:unref(checkedKeys).join(',')});
        if(result){
          checkedKeys.value = [];
          msg.success('删除成功');
          reload();
        }
      }
  
        function loadChannelType(status){
          switch(status){
            case 0:
              return '站内信';
            case 1:
              return '邮件';
            case 2:
              return '短信';
            default:
              return ''
          }
        }
        function loadSendStatus(status){
           switch(status){
            case 0:
              return '未发送';
            case 1:
              return '发送成功';
            case 2:
              return '发送失败';
            default:
              return ''
          }
        }
  
        function onFetchSuccess() {
          // 演示默认展开所有表项
        }
       async  function handleDetail(row){
            let result = await getMyMessageInfo({id:row.id});
            if(result){
                if(result.buttons && result.buttons.length==1){
                    let appAuthInfo = window.appInfo.sign_app_info;
                    let routerParams = JSON.parse(result.buttons[0].buttonPara)
                    // let params = {
                    //     tenantId:routerParams.tenantId,
                    //     departId:routerParams.departId
                    // }
                    // let newToken = await getAppTokenByAuthToken(params);
                    let token = getToken();
                    let appInfo = {
                        token:token,
                        appCode:result.appCode,
                        appId:result.appId,
                        departId:result.departId,
                        redirect:result.appAddress + '/#' + result.buttons[0].buttonRoute
                    }
                    let paramsString = new URLSearchParams(appInfo).toString();
                    let urlParams = objectToQueryString(routerParams);
                    window.open(appAuthInfo.url+'/#/auth/login?directly=true&appId='+ result.appId + '&appCode='+ result.appCode + paramsString +'&' + urlParams)
                   
                    checkedKeys.value = [row.id]
                    handelRead()
                }else{
                    openModal(true,{
                        isUpdate:false,
                            record:{
                            id:row.id
                        }
                    })
                }
            }
        }
  
        return {
          checkedKeys,
          registerTable,
          handleDelete,
          handleSuccess,
          onFetchSuccess,
          onSelectChange,
          handleReSend,
          loadChannelType,
          loadSendStatus,
          handelRead,
          handelAllRead,
          handleDetail,
          registerModal,
          pagination
        };
      },
    });
  </script>
<style lang="less" scoped>
:deep(.ant-pagination){
    width:100%;
}
 </style>
  