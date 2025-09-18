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
  <div  :class="prefixCls">
      <BasicForm   @register="register"   @submit="handleSubmit" >
       <template #ipSwitch="{ model, field }">
         <Switch v-model:checked="model[field]" @change="handleIpSwitch"></Switch>
        </template>
        <template #radioButton="{ model, field }">
          <div>
            <RadioGroup v-model:value="model[field]" button-style="solid" @change="handleRadioChange">
              <RadioButton value="black">黑名单</RadioButton>
              <RadioButton value="white">白名单</RadioButton>
            </RadioGroup>
          </div>
        </template>
        <template #customSlot>
          <div>
            
          </div>
        </template>
      </BasicForm>
      <div class="ip-list" v-show="IpConfig">
         <Row class="enter-x">
            <Col :span="2">
              <p style="text-align:right">配置IP：</p>
            </Col>
            <Col :span="22">
              <BasicTable @register="registerTable">
                  <template #ipStart>
                      <!-- <template v-for="index in indexes" :key="index">
                          <Input  v-model:value="ipAddress[index]"
                                    @keyup="handleChange"
                                    oninput="value=value.replace(/[^\d]/g,'')&&value>255?255:value"/>
                          <span v-if="index!==3">.</span>
                      </template> -->
                  </template>
                  <template #toolbar>
                     <a-button  type="link" @click="handleAdd"> <Icon icon="ant-design:plus-circle-outlined" />新增 </a-button>
                  </template>
                  <template #action="{ record, column, index }">
                      <TableAction :actions="createActions(record, column, index)" />
                  </template>
                </BasicTable>
            </Col>
        </Row>
      </div> 
      <a-button  type="primary" @click="handleSubmit" class="page-save-btn">保存修改 </a-button>
  </div>
</template>
<script lang='ts'>
import { defineComponent,onMounted,watch,computed,ref } from 'vue';
import { useDesign } from '/@/hooks/web/useDesign';
import { BasicTable, useTable, EditRecordRow, BasicColumn, ActionItem, TableAction } from '/@/components/Table';
import { Input,Row, Col,Radio,Switch } from 'ant-design-vue';
import { BasicForm,useForm} from '/@/components/Form/index';
import { useMessage } from '/@/hooks/web/useMessage';
import { Icon } from '/@/components/Icon'
import { getIpList,addIpLimit } from '/@/api/sys/safe';
import { IPSchema,IPColumns } from './data';
const indexes=[0,1,2,3];
export default defineComponent({
  name: 'IPlimit',
  components: { BasicForm, BasicTable, TableAction,Input,Row, Col,Icon,RadioGroup:Radio.Group,RadioButton: Radio.Button,Switch},
  setup(){
    const { prefixCls } = useDesign('access');
    const { createMessage:msg } = useMessage();
    const IpConfig = ref(false)
    const ipAddress = ref([]);
     const [  register ,{ validate, setProps, getFieldsValue, setFieldsValue, updateSchema} ] = useForm({
        labelWidth: 150,
        schemas:IPSchema,
        showActionButtonGroup: false,
        showResetButton:false,
        baseColProps: { lg: 24, md: 24},
        wrapperCol:{
          span:24
        },
        actionColOptions: {
          span: 24,
        },
        // submitButtonOptions: {
        //   text: '保存修改',
        // },
        // submitFunc: customSubmitFunc,
       
      });
      

      const [registerTable,{getDataSource,setTableData}] = useTable({
        columns: IPColumns,
        api:getIpList,
        showIndexColumn: true,
        dataSource: [],
        striped:false,   
        immediate:false,
        size:'small',
        maxHeight:300,
        fetchSetting:{
          listField:'records'
        },
        actionColumn: {
          width: 160,
          title: '操作',
          dataIndex: 'action',
          slots: { customRender: 'action' },
        },
        pagination: false,
      });
      onMounted(() => {
        fetch();
      });

      async function fetch(){
        const values = await getFieldsValue();
        let result = await getIpList({type:values.type});
        if(result){
          let type = result?.type;
          let ipSwitch = result?.ipSwitch;
          if(ipSwitch=='true'){
            IpConfig.value= true;
            setFieldsValue({
              ipSwitch:true
            })
          }else{
            IpConfig.value= false;
            setFieldsValue({
              ipSwitch:false
            })

          }
          setFieldsValue({
            type:type
          });
          result.list.map(item=>{
            item.editable = true
          })
          setTableData(result.list)
        }
      }
      function handleIpSwitch(value){
        IpConfig.value = value;
      }
      async function handleRadioChange(e){
        console.log(e);
        if(e.target.value==='black'){
          updateSchema([
            {field:'type',  itemProps:{ extra:'设置黑名单限制后，不允许账号在黑名单限制 IP（段）内登录。'}},
          ])
        }else{
           updateSchema([
            {field:'type',  itemProps:{ extra:'设置白名单限制后，允许账号在白名单限制 IP（段）内登录。'}}
          ])
        }
        setFieldsValue({
            ipSwitch:true,
            type:e.target.value
          })
        let result = await getIpList({type:e.target.value});
        if(result){
          result.list.map(item=>{
            item.editable = true
          })
          setTableData(result.list)
        }

      }
      async function customSubmitFunc() {
          try {
            await validate();
            setProps({
              submitButtonOptions: {
                loading: true,
              },
            });
            setTimeout(() => {
              setProps({
                submitButtonOptions: {
                  loading: false,
                },
              });
              msg.success('提交成功！');
            }, 2000);
          } catch (error) {}
      }

      async function handleIpSet(){
        let values = await getFieldsValue();
        console.log(values)
       
      }

      function handleAdd() {
        const data = getDataSource();
        if(data.length===10){
          msg.success('最多添加10条记录');
          return; 
        }
        const addRow: EditRecordRow = {
          ipStart:'',
          ipEnd:'',
          ipDesc:'',
          editable:true,
        };
        data.push(addRow);
      }


      function handleDelete(record: EditRecordRow, index) {
        const data = getDataSource();
        data.splice(index,1)
      }

      function createActions(record: EditRecordRow, column: BasicColumn, index: number): ActionItem[] {
          return [
            {
              label: '删除',
              onClick: handleDelete.bind(null, record, index)
            },
          ];
      }

      async function handleSubmit(){
        const values = await getFieldsValue();
        const tableData = await getDataSource();
        let list = [];
        tableData.map((item:EditRecordRow) => list.push(item.editValueRefs as never))
        let params = {
          type:values.type,
          list:list,
          ipSwitch:values.ipSwitch?'true':'false'
        }
        let result = await addIpLimit(params);
        if(result){
          msg.success('保存成功');
          fetch();
        }
        
      }
      function handleSuccess(){

      }
      function handleChange(){

      }

    return {
      handleSubmit,
      handleAdd,
      prefixCls,
      createActions,
      handleSuccess,
      register,
      handleChange,
      handleIpSet,
      registerTable,
      handleRadioChange,
      ipAddress,
      indexes,
      IpConfig,
      handleIpSwitch
    }
  }
})
</script>
<style lang="less" scoped>
:deep(.ant-table-body){
  height:auto;
}
.page-save-btn{
  float:right;
}
</style>
