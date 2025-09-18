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
       <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit" :destroyOnClose="true">
          <BasicForm @register="registerForm" >
            <template #userIds="{model,field}">
                <!-- <a-tree-select v-model:value="model[field]" :multiple="true" :load-data="onLoadData"  :tree-data="userTreeData" :fieldNames="{children:'children', label:'title', value: 'id'}"/> -->
                <a-select  v-model:value="model[field]" mode="multiple" disabled>
                  <a-select-option v-for="(item,index) in allUserList" :key="index" :value="item.userId">{{item.username}}</a-select-option>
                </a-select>
                <a-button @click="handleUser">选择</a-button>
            </template>
            <template #msgContent>
                <Tinymce v-model="tinymceValue" @change="handleTinymceChange" width="100%" :showImageUpload="false" />
            </template>
            <template #files>
                <!-- <BasicUpload :maxSize="20" :maxNumber="10" @change="handleChange" :api="uploadApi" /> -->
                <a-upload
                    v-model:file-list="fileList"
                    name="file"
                    :headers="headers.auth"
                    action="/resrun-paas/file"
                    @change="handleChange"
                  >
                   <template #itemRender="{ file, actions }">
                      <a-space>
                        <span :style="file.status === 'error' ? 'color: red' : ''">{{ file.name }}</span>
                        <a href="javascript:;" @click="actions.remove">删除</a>
                      </a-space>
                    </template>
                    <a-button>
                      上传
                    </a-button>
                </a-upload>
            </template>
          </BasicForm>
          
          <TabTreeModal @register="registerUserModal" @success="handleSuccess" />
      </BasicModal>
  </div>
</template>
<script lang='ts'>

  import TabTreeModal from '/@/views/organize/modal/TabTreeModal.vue';
  import { BasicUpload } from '/@/components/Upload';
  import { getToken } from '/@/utils/auth';
  import { defineComponent,ref,unref,computed,reactive } from 'vue'
  import { BasicModal, useModalInner, useModal } from '/@/components/Modal';
  import { BasicTree } from '/@/components/Tree/index';
  import { BasicForm, useForm } from '/@/components/Form';
  import { Tinymce } from '/@/components/Tinymce';
  import {  getAllDeptTree, getDeptNoCountTree} from '/@/api/sys/dept';
  import { getUserByDeptId, getAllUser } from '/@/api/sys/user';
  import { getUserList } from '/@/api/demo/system'; 
  import { getRoleTreeList,getUserByRoleId,  } from '/@/api/sys/role'; 
  import { announceAdd,announceEdit, getAnnounceFile } from '/@/api/announce';
  import { announceFormSchema } from '../data';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { uploadApi } from '/@/api/sys/upload';

 interface FileItem {
  id:string;
  realName:string;
 }

  export default defineComponent({
    name: 'AnnounceFormModal',
    components:{
      BasicModal,  
      BasicForm, 
      Tinymce,
      BasicTree,
      TabTreeModal,
      BasicUpload
    },
    setup(_, { emit }){
      const isUpdate = ref(true);
      const rowId = ref('');
      const allUserList = ref('');
      const tinymceValue = ref();
      const userTreeData = ref([]);
      const fileList = ref([]);
      const headers = reactive({auth:{
        'X-Access-Token':getToken()
      }})
      const { createMessage: msg } = useMessage();
      const [registerUserModal, { openModal, closeModal:closeUserModal}] = useModal();
      const [registerForm, { setFieldsValue, resetFields, validate }] = useForm({
        labelWidth: 100,
        schemas: announceFormSchema,
        showActionButtonGroup: false,
        actionColOptions: {
          span: 23,
        },
      });
      console.log(uploadApi,'----d')

      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        resetFields();
        setModalProps({ confirmLoading: false,width:1200 });
        isUpdate.value = !!data?.isUpdate;
        if (unref(isUpdate)) {
          rowId.value = data.record.id;
          getFiles(data.record.id)
          let userIds =  data.record.userIds?data.record.userIds.split(','):[];
          tinymceValue.value = data.record.msgContent;

          setFieldsValue({
            ...data.record,
            userIds,
          });
        }else{
          rowId.value = '';
        }
        let deptData = await getAllDeptTree();
        setArrayItemProperty(deptData);
        userTreeData.value = deptData;
        allUserList.value = await getAllUser();
      });
      function setArrayItemProperty(list) {
        list.map(item=>{
            item.disabled = true;
            item.children = item.children || [];
          if(item.children){
            setArrayItemProperty(item.children)
          }
        })
      }
      function getFiles(id){
        getAnnounceFile({fatherId:id}).then(res=>{
          if(res){
            fileList.value = res;
          }
        })
      }

      function handleChange(list){
        console.log(list,'结果文件')
      }
      function handleUser(){
        // treeType.value = 'user';
        openModal(true,{
          isupDate:false,
          title:'指定用户',
          tabs:[
              { 
                title:'根据组织选人',
                type:'deptUser',
                api:getDeptNoCountTree,
                asyncTree:true,
                asyncApi:getUserList,
                asyncFieldNames:{
                  title:'realname',
                  key:'id'
                },
                fieldNames:{
                  children:'children',
                  title:'departName',
                  key:'id'
                }
              },
              { 
                title:'根据角色选人',
                type:'roleUser',
                api:getRoleTreeList,
                asyncTree:true,
                asyncApi:getUserByRoleId,
                asyncFieldNames:{
                  title:'realname',
                  key:'userId'
                },
                fieldNames:{
                  children:'children',
                  title:'name',
                  key:'id'
                }
              },
            ]
        })
      }
       async function handleSuccess(list) {
        let userIds:string[] = [];
        console.log(list,'人员')
        if(list){ // 人员选择
          list.map( item => {
            userIds.push(item.id)
          })
          setFieldsValue({userIds})
        }
        closeUserModal()
      }
     
      const getTitle = computed(() => (!unref(isUpdate) ? '新增' : '编辑'));
      
      async function handleSubmit() {
        try {
          const values = await validate();
          setModalProps({ confirmLoading: true });
          let files = <FileItem[]>[];
          fileList.value.map( (item:any )=>{
            console.log(item,'--文件id--')
            files.push({
              id: item?.id || item?.response.result,
              realName:item.name
            })
          })
          console.log(files,'-------')
          // TODO custom api
          let result = null;
          let params = {
            ...values,
            userIds:values.userIds&&values.userIds.join(','),
            id:unref(rowId),
            msgContent:unref(tinymceValue),
            files
            
          }
          if(unref(isUpdate)){
            result = await announceEdit(params);
          }else{
            result = await announceAdd(params);
          }
          if(result){
            msg.success('操作成功');
            closeModal();
            emit('success', { isUpdate: unref(isUpdate), values: { ...values, id: rowId.value } });
          }
       
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }
      function handleTinymceChange(value: string) {
        console.log(value);
      }
      async function onLoadData(treeNode) {
        return new Promise((resolve: (value?: unknown) => void) => {
          setTimeout(async() => {
            let parentId = treeNode.id;
            if(!parentId || (treeNode.type==='user')){
                resolve();
                return;
            }
            let result = await getUserByDeptId({id:treeNode.id});
            result.records&&result.records.map(item=>{
                item.title= item.realname;
            })
            treeNode.dataRef.children = treeNode.dataRef.children.concat(result.records);
            userTreeData.value = [...userTreeData.value];
            resolve();
            return;
          }, 1000);
        })

      }

      return { handleChange,fileList, headers,uploadApi,handleUser,allUserList, handleSuccess, onLoadData, userTreeData, registerModal, registerForm, getTitle, handleSubmit,tinymceValue,handleTinymceChange, registerUserModal };
    }
  })
</script>
<style lang="less" scoped>
  :deep(.ant-picker){
    width:100%;
  }
 
</style>
