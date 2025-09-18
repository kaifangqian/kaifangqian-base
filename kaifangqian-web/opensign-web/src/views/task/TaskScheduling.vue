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
      <div :class="`${prefixCls}__title`">
          <a-button type="primary" :class="[prefixCls+'-action']" @click="handleAdd" v-if="hasPermission(['task:add'])">新增任务</a-button>
          <Icon icon="ant-design:undo-outlined" size="24" @click="fetch" style="margin:0 5px;cursor: pointer;"/>
      </div>
      <div :class="`${prefixCls}__content`">
        <a-spin :spinning="spinning" >
          <a-list :pagination="state.pagination">
            <template v-for="item in list" :key="item.id">
              <a-list-item class="list" @click="handleEdit(item)">
                <a-list-item-meta>
                  <!-- <template #avatar>
                    <Icon class="icon" v-if="item.icon" :icon="item.icon" :color="item.color" />
                  </template> -->
                  <template #avatar>
                    <img src="../../assets/images/taskAvatar.png" style="margin-left:10px;width:30px"/>
                  </template>

                  <template #title>
                    <div class="title-content">
                      <span>{{ item.jobName }}</span>
                      
                    </div>
                    <!-- <div class="extra">
                      
                    </div> -->
                    <div class="icon-list">
                      <a-tooltip title="运行记录">
                         <Icon  v-if="hasPermission(['task:history'])" icon="ant-design:file-done-outlined"  color="#939393" size="20" @click.stop="handleInstance(item)"/>
                      </a-tooltip>
                     
                      <!-- <span  @click.stop="handleStop">
                        <a-popconfirm
                            title="确定删除该任务?"
                            ok-text="是"
                            cancel-text="否"
                            disabled
                            @confirm.stop="handleInstance(item)"
                          >
                              <Icon icon="ant-design:delete-outlined"  color="#e11a2d" size="20"  />
                          </a-popconfirm>
                      </span> -->
                      <a-tooltip title="删除任务">
                        <Icon v-if="hasPermission(['task:delete'])" icon="ant-design:delete-outlined"  color="#e11a2d" size="20" @click.stop="handleDeleteInstance(item)" />
                      </a-tooltip>
                      <a-tooltip title="运行实例">
                        <Icon v-if="hasPermission(['task:run'])" icon="ant-design:forward-filled"  color="#55d187" size="20" @click.stop="handleStart(item)" />
                      </a-tooltip>
                      <a-tooltip :title="item.status===1?'禁止任务':'启用任务'">
                          <!-- <Switch v-model:checked="item.status" size="mini" @change.stop="handleChangeStatus(item)"></Switch> -->
                        <Icon v-if="hasPermission(['task:able'])" :icon="item.status===1?'ant-design:pause-circle-outlined':'ant-design:play-circle-outlined'"  :color="item.status==1?'#939393':'#127fd2'" size="20" @click.stop="handleChangeStatus(item)" />
                      </a-tooltip>
                      
                      
                    </div>
                  </template>
                  <template #description>
                    <div class="description">
                      <p>{{item.beanName}}</p>
                      <p>执行类型：{{loadExecuteType(item.executeType)}}</p>
                      <p>处理器类型：{{loadProcessorType(item.processorType)}}</p>
                    </div>
                    <div class="info">
                      <div>{{ item.gmtCreate }}</div>
                    </div>
                  </template>
                </a-list-item-meta>
              </a-list-item>
            </template>
          </a-list>
        </a-spin>
      </div>
      <!-- <a-pagination v-model:current="state.pagination.pageNo" show-quick-jumper :total="state.pagination.total" @change="onChange" /> -->
    <TaskDrawer  @register="registerDrawer" @success="handleSuccess"/>
  </div>
</template>
<script lang="ts">
  import { defineComponent,ref, reactive, onMounted } from 'vue';
  import { Divider,Menu ,Dropdown, MenuItem,Tag,List, Avatar, Switch } from 'ant-design-vue';
  import { EditRecordRow } from '/@/components/Table';
  import { useGo } from '/@/hooks/web/usePage';
  import Icon from '/@/components/Icon/index';
  import { useDrawer } from '/@/components/Drawer';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useUserStore } from '/@/store/modules/user';
  import { usePermission } from '/@/hooks/web/usePermission';
  import { DownOutlined } from '@ant-design/icons-vue';
  import { getTaskList, disabledTask, runTask, deleteTask, addTaskList } from '/@/api/task';
  import TaskDrawer from './drawer/TaskDrawer.vue';



  interface ItemList {
    jobName:string;
    icon?:string;
    description:string;
    gmtCreate:string;
    color?:string;
    beanName?:string;
    status?:string|number;
    executeType?:string|number;
    processorType?:string|number;
  }
  export default defineComponent({
    name: '任务列表',
    components: { Tag, DownOutlined, Divider,Menu ,Dropdown, MenuItem,  Icon,
      [List.name]: List,TaskDrawer,
      [List.Item.name]: List.Item,
      AListItemMeta: List.Item.Meta,Avatar,Switch },
    setup() {

      const checkedKeys = ref<Array<string | number>>([]);
      const list = ref<ItemList[]>([]);
      const spinning = ref(false);
      const go = useGo();
      const { createMessage } = useMessage();
      const { hasPermission } = usePermission();
      const userStore = useUserStore();
      // const userInfo = computed(()=> userStore.getUserInfo);
      const appId =  userStore.getUserInfo.jobAppId;
      const state = reactive({pagination:{
        pageNo:1,
        pageSize:10,
        total:0,
        showQuickJumper:true,
        showSizeChanger:true,
        showTotal:(total)=>`共${total}条数据`,
        onChange:onChange,
      }})

      const [registerDrawer, { openDrawer}] = useDrawer();
      onMounted(() => {
        fetch();
      });
      async function fetch(){
        spinning.value = true;
        let result = await getTaskList({appId:appId,pageNo:state.pagination.pageNo,pageSize:state.pagination.pageSize});
        if(result){
          list.value = result.records;
          spinning.value = false;
          state.pagination.pageSize = result.size;
          state.pagination.pageNo = result.current;
          state.pagination.total = result.total;

        }
      }
      function onChange(number,pageSize){
        state.pagination.pageNo = number;
        state.pagination.pageSize = pageSize;
        fetch()
      }

    

      function onSelectChange(selectedRowKeys: (string | number)[]) {
        console.log(selectedRowKeys);
        checkedKeys.value = selectedRowKeys;
      }

      function handleDelete(record: Recordable) {
        console.log(record);
      }

      function handleSuccess() {
        fetch();
      }
      function handleView(){

      }
      function handleStop(e){
        e.stopPropagation();
      }

      function onFetchSuccess() {
        // 演示默认展开所有表项
      }
      function handleAdd(){
        openDrawer(true,{
          isUpdate:false
        })
      }
      function handleEdit(record?: EditRecordRow){
        openDrawer(true,{
            isUpdate:true,
            record:{
              ...record
            }
        })
      }
      function handleInstance(record){
        console.log(record)
        go(`${'/task/instance/'+ record.id}`)
      }
      async function handleDeleteInstance(record){
        let result = await deleteTask({jobId:record.id});
        if(result){
          createMessage.success('操作成功');
          fetch();
        }
      }
      async function handleStart(record){
        let  result = await runTask({jobId:record.id});
        if(result){
          createMessage.success('操作成功');
          // fetch();
        }
      }
      async function handleChangeStatus(record){
        let result;
        if(record.status===2){
          result = await addTaskList({jobId:record.id,...record,status:1});
        }else{
          result = await disabledTask({jobId:record.id});
        }
        if(result){
          createMessage.success('操作成功');
          fetch();
        }
      }

      function loadProcessorType(status){
        switch(status){
            case 1:
              return '内建处理器';
            case 2:
              return 'SHELL脚本';
            case 3:
              return 'Python脚本';
            case 4:
              return '外部处理器';
            default:
              return '';
        }
      }
      function loadExecuteType(status){
          switch(status){
            case 1:
              return '单机执行';
            case 2:
              return '广播执行';
            case 3:
              return 'MapReduce';
            case 4:
              return 'MAP';
            default:
              return '';
        }
      }

      return {
        prefixCls: 'list-basic',
        handleEdit,
        hasPermission,
        registerDrawer,
        handleDelete,
        handleSuccess,
        onFetchSuccess,
        onSelectChange,
        handleView,
        handleAdd,
        handleStop,
        handleStart,
        handleChangeStatus,
        loadExecuteType,
        loadProcessorType,
        handleInstance,
        handleDeleteInstance,
        list,
        spinning,
        fetch,
        onChange,
        state,
        pagination: {
          show: true,
          pageSize: 3,
        },
      };
    },
  });
</script>
<style lang="less" scoped>
  .list-basic {
    &__top {
      padding: 24px;
      text-align: center;
      background-color: @component-background;
      &-col {
        &:not(:last-child) {
          border-right: 1px dashed @border-color-base;
        }

        div {
          margin-bottom: 12px;
          font-size: 14px;
          line-height: 22px;
          color: @text-color;
        }

        p {
          margin: 0;
          font-size: 24px;
          line-height: 32px;
          color: @text-color;
        }
      }
    }
    &__title {
      padding: 20px 10px 0;
      // margin-bottom: 16px;
      background-color: #fff;
      border-radius: 2px;
      text-align: right;
    }

    &__content {
      padding: 24px 12px;
      // margin-top: 12px;
      background-color: @component-background;
      :deep(.ant-list-item-meta-title){
        display: flex;
        justify-content: space-between;
        padding-right:50px;
        .anticon{
          margin:0 5px;
        }
      }

      .list {
        cursor: pointer;
        position: relative;
        border: 1px solid #f4f4f4;
        margin: 2px;
        :deep(.ant-list-item-meta-description){
          display:flex;
          align-items: center;
          justify-content: space-between;
        }
        &:hover{
          box-shadow: 0px 3px 5px #f1f1f1;
        }
      }

      .icon {
        font-size: 40px !important;
      }
      .icon-list{
        display: flex;
        align-items: center;
      }

      .extra {
        position: absolute;
        top: 20px;
        right: 15px;
        font-weight: normal;
        color: @primary-color;
        cursor: pointer;
      }

      .description {
        display: inline-block;
        width: 40%;
      }

      .info {
        display: inline-block;
        width: 30%;
        text-align: right;

        div {
          display: inline-block;
          padding: 0 20px;

          span {
            display: block;
          }
        }
      }

      .progress {
        display: inline-block;
        width: 15%;
        vertical-align: top;
      }
    }
  }
</style>
