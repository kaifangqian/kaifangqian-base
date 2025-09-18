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
          <template #templateContent>
              <Tinymce v-model:value="templateContent" @change="handleTinymceChange" width="100%" :showImageUpload="false"/>
          </template>
        </BasicForm>
        <a-button  type="dashed" @click="handleButtonConfig">按钮配置 </a-button>
        <BasicTable @register="registerTable" v-if="buttonConfigShow">
          <template #buttonParas="{record}">
            <a-popover title="参数配置" trigger="click"   placement="top">
              <template #content  class="custom-popover">
                <template v-for="(item,index) in record.buttonParas" >
                  <div class="form-list">
                    <Form :key="index" >
                      <!-- <Row class="enter-x" >
                        <Col :span="10"> -->
                          <FormItem label="参数名称">
                            <Input v-model:value="item.paraName"   placeholder="请选择规则类型"/>
                          </FormItem>
                        <!-- </Col>
                        <Col :span="10"> -->
                          <FormItem label="传参方式">
                            <Select v-model:value="item.sendType" :options="sendTypeList"  placeholder="请选择规则类型"/>
                          </FormItem>
                        <!-- </Col>
                        <Col :span="4" class="column-action"> -->
                            <Icon icon="ant-design:plus-square-outlined" size="24" color="#8d8686"  @click.stop="handleParamsAdd(record)" />
                            <Icon icon="ant-design:minus-square-outlined" size="24" color="#8d8686" v-if="index !==0" @click.stop="handleParamsDelete(record,index)"/>
                        <!-- </Col>
                      </Row> -->
                    </Form>
                  </div> 
                </template>
              </template>
              <a-button type="link">参数</a-button>
            </a-popover>
          </template>
          <template #action="{ index }">
            <Icon icon="ant-design:plus-square-outlined" size="24" color="#8d8686"  @click="handleAdd" />
            <Icon icon="ant-design:minus-square-outlined" size="24" color="#8d8686" v-if="index !==0" @click="handleDelete(index)"/>
              <!-- <TableAction :actions="createActions(record, column)" /> -->
          </template>
        </BasicTable>
      </BasicModal>
  </div>
</template>
<script lang='ts'>
  import { defineComponent,ref,unref,computed, nextTick } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form';
  import { tplFormSchema,tplBtnCloumns } from '../data';
  import { BasicTable, useTable, EditRecordRow, ActionItem, TableAction, BasicColumn } from '/@/components/Table';
  import { Tinymce } from '/@/components/Tinymce';
  import { addMessageList, editMessage ,getTemplateInfo} from '/@/api/message'; 
  import { useMessage } from '/@/hooks/web/useMessage';
  import Icon from '/@/components/Icon';
  import { Form, Input, Row, Col,Select } from 'ant-design-vue';


  export default defineComponent({
    name: 'TemplateModal',
    components:{
      BasicModal,
      BasicForm,
      Tinymce,
      BasicTable,
      TableAction,
      Icon,
      Form, Input, Row, Col,Select,
      FormItem: Form.Item,

    },
    setup(_, { emit }){
      const isUpdate = ref(true);
      const rowId = ref('');
      const templateContent = ref();
      const { createMessage: msg } = useMessage();
      const sendTypeList = ref([]);
      const buttonConfigShow = ref(false);

      const [registerForm, { setFieldsValue, resetFields, validate }] = useForm({
        labelWidth: 100,
        schemas: tplFormSchema,
        showActionButtonGroup: false,
        actionColOptions: {
          span: 23,
        },
      });
      const [registerTable, { getDataSource,setTableData }] = useTable({
        columns: tplBtnCloumns,
        showIndexColumn: false,
        showDragColumn:false,
        bordered: false,
        isTriggerSelect:false,
        dataSource: [{
          editable: true,
          buttonName:'',
          buttonCode:'',
          buttonDesc:'',
          buttonRoute:'',
          buttonStyle:'',
          routeMethod:'',
          buttonOrder:'',
          buttonParas:[{paraName:'',sendType:''}]
        }],
        // actionColumn: {
        //   width: 160,
        //   title: '操作',
        //   dataIndex: 'action',
        //   slots: { customRender: 'action' },
        // },
        pagination: false,
      });

      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        resetFields();
        setModalProps({ confirmLoading: false,width:1200 });
        isUpdate.value = !!data?.isUpdate;
        if (unref(isUpdate)) {
          rowId.value = data.record.id;
          let info = await getTemplateInfo({id:data.record.id});
          if(!info) return;
          setFieldsValue({
            ...info,
          });
          templateContent.value = info.templateContent;
          if(info.buttons&&info.buttons.length){
            buttonConfigShow.value = true;
            nextTick(()=>{
              info.buttons.map(item=>{
                item.editable = true;
                item.buttonParas = item.buttonParas&&item.buttonParas.length?item.buttonParas:[{paraName:'',sendType:''}]
              })
              setTableData(info.buttons);
            })
          }
        }else{
          buttonConfigShow.value = false;
          rowId.value = '';
        }
      });

      const getTitle = computed(() => (!unref(isUpdate) ? '新增' : '编辑'));


      function handleButtonConfig(){
        buttonConfigShow.value = !buttonConfigShow.value;
      }

      async function handleSubmit() {
        try {
          const values = await validate();
          let buttonData = [];
          if(buttonConfigShow.value){
            let buttonDataRefs = await getDataSource();
            buttonDataRefs.map(item=>{
              buttonData.push({...item.editValueRefs,buttonParas:item.buttonParas})
            });
            
          }
          setModalProps({ confirmLoading: true });
          // TODO custom api
          let result;
          if(!unref(isUpdate)){
              result =  await addMessageList({
                ...values,
                buttons:buttonData,
                templateContent:unref(templateContent)
              });
          }else{
              result = await editMessage({
                ...values,
                buttons:buttonData,
                id:unref(rowId),
                templateContent:unref(templateContent)
              });
          }
          if(result){
            msg.success(result.message);
            closeModal();
            emit('success', { isUpdate: unref(isUpdate), values: { ...values, id: rowId.value } });
          }
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }
      function createActions(record: EditRecordRow, column: BasicColumn): ActionItem[] {
        if (!record.editable) {
          return [
            {
              label: '编辑',
              onClick: handleEdit.bind(null, record),
            },
            {
              label: '删除',
              onClick:handleDelete.bind(null, record)
            },
          ];
        }
        return [
          {
            label: '保存',
            onClick: handleSave.bind(null, record, column),
          },
          {
            label: '取消',
            popConfirm: {
              title: '是否取消编辑',
              confirm: handleCancel.bind(null, record, column),
            },
          },
        ];
      }
      function handleParamsAdd(record) {
        record.buttonParas.push({paraName:'',sendType:''})
      }
      function handleParamsDelete(record,index) {
        console.log(record,index)
        record.buttonParas.splice(index,1)
      }

      async function handleAdd(){
        const data = await getDataSource();
        const addRow: EditRecordRow = {
          editable: true,
          buttonName:'',
          buttonCode:'',
          buttonDesc:'',
          buttonRoute:'',
          buttonStyle:'',
          routeMethod:'',
          buttonOrder:'',
          buttonParas:[{paraName:'',sendType:''}],
          key: `${Date.now()}`,
        };
        data.push(addRow);

      }

      function handleEdit(record: EditRecordRow) {
        // bthEdit.value = true;
        record.onEdit?.(true);
      }

      async function handleCancel(record: EditRecordRow) {
        record.onEdit?.(false);
        
        if (record.isNew) {
          const data = await getDataSource();
          const index = data.findIndex((item) => item.key === record.key);
          data.splice(index, 1);
        }
      }
      async function handleDelete(record: EditRecordRow){
        const data = await getDataSource();
        const index = data.findIndex((item) => item.key === record.key);
        data.splice(index, 1);
      }


      async function handleSave(record: EditRecordRow) {
       // 校验
        msg.loading({ content: '正在保存...', duration: 0, key: 'saving' });
        const valid = await record.onValid?.();
        if (valid) {
          try {
            
          
           
          } catch (error) {
            msg.error({ content: '保存失败', key: 'saving' });
          }
        } else {
          msg.error({ content: '请填写正确的数据', key: 'saving' });
        }
      }
      function handleTinymceChange(value: string) {
        console.log(value);
        templateContent.value = value;
      }

      return {handleButtonConfig, buttonConfigShow, handleParamsAdd,handleParamsDelete,sendTypeList,handleDelete, registerModal,handleAdd, registerTable,createActions,registerForm, getTitle, handleSubmit,templateContent,handleTinymceChange };
    }
  })
</script>
<style lang="less" >
  .custom-popover{
    :deep(.ant-popover-inner-content){
      min-width: 550px;
      min-height:150px;
      padding:0;
    }
  }
  .form-list{
    margin:20px;
    .ant-form{
      display: flex;
      align-items: center;
    }
    .ant-form-item{
      margin-bottom:0;
      margin:0 2px;
    }
    .ant-form-item-control{
      width:150px;
    }
  }
 
</style>
