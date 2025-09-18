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
  <BasicDrawer
    v-bind="$attrs"
    @register="registerDrawer"
    showFooter
    :title="getTitle"
    width="50%"
    @ok="handleSubmit"
    >
    <BasicForm @register="registerForm" />
    <div class="rule-block">
      <div class="header-context">
        <a-button type="primary" @click="handleAddCondition">新建组 </a-button>
        <a-button type="danger" @click="handleDeleteCondition">删除组 </a-button>
      </div>
      <div class="rule-list">
          <div class="rule-item" v-for="(item,index) in ployData.domains" :key="index">
              <div class="rule-content-body">
                  <span>{{index+1}}</span>
                   <div class="rule-title-left">
                        <Checkbox v-model:checked="item.checked"/>
                        <div class="title-line">
                          <span class="line-top"></span>
                          <span>AND</span>
                          <span class="line-bottom"></span>
                        </div>
                    </div>
                    <div class="rule-content-right">
                      <div v-for="(childItem,childIndex) in item.rules" :key="childIndex">
                        <span>{{childItem.key}}</span>
                        <Form>
                          <Row class="enter-x" :gutter="6">
                            <Col :span="6">
                              <FormItem>
                                <Select v-model:value="childItem.ruleColumnId" :options="rulesOptions.options" :fieldNames="{label: 'dbFieldTxt', value: 'id',}" placeholder="请选择"/>
                              </FormItem>
                            </Col>
                            <Col :span="6">
                              <FormItem >
                              <Select   v-model:value="childItem.ruleConditions" :options="conditions" placeholder="请选择" />
                              </FormItem>
                            </Col>
                            <Col :span="8">
                              <FormItem >
                                <a-input-group compact style="display:flex;">
                                    <a-input v-model:value="childItem.ruleValue"  placeholder="请填写" @blur="clearRuleSelect"/>
                                    <a-popover title="勾选下面选项值自动填充">
                                      <template #content>
                                        <a-radio-group v-model:value="ruleSelectValue" @change="handleSelectRuleValue(ruleSelectValue,childItem)">
                                            <a-radio  value="#{sysUserId}">当前用户</a-radio>
                                            <a-radio  value=" #{sysOrgCode}">当前部门</a-radio>
                                        </a-radio-group>
                                      </template>
                                      <a-button type="primary">选择值</a-button>
                                    </a-popover>
                                </a-input-group>
                              </FormItem>
                            </Col>
                            <Col :span="4">
                                <Icon icon="ant-design:plus-square-outlined" size="24" color="#8d8686" @click="handleAddRules(item,item.conditionGroupIndex)"/>
                                <Icon icon="ant-design:minus-square-outlined" size="24" color="#8d8686" v-if="item.rules&&item.rules.length>1 " @click="handleDeleteRules(item,childIndex)"/>
                            </Col>
                          </Row>
                        </Form>
                    </div>
                  </div>
              </div>
              <div class="rule-splice-line" v-if="(index+1)!== ployData.domains.length"></div> 
          </div>
      </div>
    </div> 

  </BasicDrawer>
</template>
<script lang="ts">
  import { defineComponent, ref, computed, unref, onMounted,reactive, toRaw} from 'vue';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { Icon} from '/@/components/Icon';
  import { Checkbox,Form, Input, Row, Col,Select } from 'ant-design-vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { addAuthData,editAuthData,getAuthDataRules,getAuthTableField} from '/@/api/auth/group'; 
  import { ployFormSchema } from '../data';
  
  interface ConditionItem {
    ruleColumnId?:string;
    ruleConditions?:string;
    ruleValue?:string;
    conditionGroup?:string|number;
    key?:string | number
  }
  interface ConditionGroupItem extends ConditionItem {
    conditionGroupIndex:number;
    rules:ConditionItem[],
    checked?:boolean,
    key?:string | number
  }
  export default defineComponent({
    name: 'PloyDrawer',
    components: {
      BasicDrawer,
      BasicForm,
      Checkbox,
      Select,
      Form,
      FormItem: Form.Item,
      Input,
      Row,
      Col,
      Icon
    },
    emits: ['success', 'register'],
    setup(_, { emit }) {
      const isUpdate = ref(true);
      const recordId = ref('');
      const permissionId = ref('');
      const ruleSelectValue = ref('');
      const conditions = reactive([
        { label:'等于',  value:'=' },
        { label:'大于',  value:'>' },
        { label:'大于等于',  value:'>=' },
        { label:'小于',  value:'<' },
        { label:'小于等于',  value:'<=' },
        { label:'不等于',  value:'!=' },
        { label:'被包含',  value:'IN' },
        { label:'全模糊',  value:'LIKE' },
        { label:'左模糊',  value:'LEFT_LIKE' },
        { label:'右模糊',  value:'RIGHT_LIKE' },
      ]);
      const rulesOptions =  reactive({options:[]});
      
      const ployData = reactive<{ domains: ConditionGroupItem[] }>({
              domains: [ {
                conditionGroupIndex:1,
                rules:[{
                  ruleColumnId:'',
                  ruleConditions:'',
                  ruleValue:'',
                  conditionGroup:1,
                  key:'default'
                }],
                checked:false,
              }],
            });

      const { createMessage: msg } = useMessage();

      async function fetch(permissionId) {
        let result = await getAuthTableField({permissionId})
        if(result){
          rulesOptions.options = result.records
        }
       
      }
      onMounted(() => {
        // fetch();
      })
      const [ registerForm, { resetFields, setFieldsValue, validate }] = useForm({
        labelWidth: 100,
        schemas: ployFormSchema,
        showActionButtonGroup: false,
        baseColProps: { lg: 24, md: 24 },
      });
      const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
        fetch( data.record?.permissionId )
        resetFields();
        resetRules();
        setDrawerProps({ confirmLoading: false });
        isUpdate.value = !!data?.isUpdate;
        permissionId.value = data.record?.permissionId;
        console.log(permissionId,'3333')
        if (unref(isUpdate)) {
          recordId.value = data.record.id;
          setFieldsValue({
            ...data.record,
          });
          handleGetRulesData(unref(recordId));
        }else{
            recordId.value = '';
        }
      });
      const getTitle = computed(() => (!unref(isUpdate) ? '添加策略' : '编辑策略'));

      function resetRules(){
          ployData.domains = [
            {
              conditionGroupIndex:1,
              rules:[{
                ruleColumnId:'',
                ruleConditions:'',
                ruleValue:'',
                conditionGroup:1
              }],
              checked:false
            }
          ];
      }
      async function handleGetRulesData(id){
        if(!id) return ;
        let result = await getAuthDataRules({id});
        if(result){
            if( result.rules ){
              ployData.domains[0].rules = []; 
              // ployData.domains[0].rules.length = 0;
              result.rules.map( item => {
              if(!item.conditionGroup) return;
                let matchGroup =  ployData.domains.filter( v => v.conditionGroupIndex === item.conditionGroup)[0];
                //匹配到放到对应组下的规则
                if(matchGroup){
                  ployData.domains[item.conditionGroup-1].rules = ployData.domains[item.conditionGroup-1].rules || []; //首次设置默认为空数组
                  ployData.domains[item.conditionGroup-1].rules.push({ ...item });
                }else{
                  ployData.domains.push({
                    conditionGroupIndex:item.conditionGroup,
                    rules:[{ ...item }],
                    checked:false,
                  })
                }
              })
            }
        }
      }
      async function handleSubmit() {
        try {
          const values = await validate();
          setDrawerProps({ confirmLoading: true });
          let result = reactive({});
          let rules  = reactive<{ list: ConditionItem[]}>({ list: []});
            ployData.domains.map( item => {
              [...item.rules].map( v =>{
                  rules.list.push(v);
              })
            })
          let params = {
            id:unref(recordId),
            permissionId:unref(permissionId),
            ...values,
            rules:toRaw(rules.list),
          }
          if(!unref(isUpdate)){
              result = await addAuthData(params)
            }else{
              result = await editAuthData(params)
          }
          if(result){
            msg.success('操作成功')
            closeDrawer();
            emit('success');
          }
        } finally {
          setDrawerProps({ confirmLoading: false });
        }
      }
      function handleAddRules(record,index){
          record.rules.push({
            ruleColumnId:'',
            ruleConditions:'',
            ruleValue:'',
            conditionGroup:index
          })
          ruleSelectValue.value = '';
      }
      function handleDeleteRules(record,index){
        record.rules.splice(index,1);
        ruleSelectValue.value = '';
      }
      function handleAddCondition(){
         ployData.domains.push(
            {
              conditionGroupIndex: ployData.domains.length+1,
              rules:[{
                ruleColumnId:'',
                ruleConditions:'',
                ruleValue:'',
                conditionGroup:ployData.domains.length+1
              }],
              checked:false
          })
          console.log(ployData)
      }
      function handleDeleteCondition(){
        let matchGroup = ployData.domains.filter(item=>item.checked === true);
        matchGroup.map(item=>{
          ployData.domains.splice(item.conditionGroupIndex-1,1);
        })
      }
      function handleSelectRuleValue(val,item){
        item.ruleValue = val;
      }
      function clearRuleSelect(){
        ruleSelectValue.value = '';
      }
      return {
        clearRuleSelect,
        handleAddCondition,
        handleDeleteCondition, 
        registerForm,
        getTitle, 
        registerDrawer,
        handleSubmit, 
        isUpdate,
        ployData,
        conditions,
        rulesOptions,
        handleAddRules,
        handleDeleteRules,
        ruleSelectValue,
        handleSelectRuleValue
      }
    }
  });
</script>
<style lang="less" scoped>
.rule-block{
  border: 1px solid #e4e4e4;
  border-radius: 2px;
  .header-context{
    padding: 10px 12px;
    border-bottom: 1px solid #e4e4e4;
    :deep(.ant-btn){
      margin:0 5px;
    }
  }
}
  .rule-list{
    margin-top:5px;
    max-height: 500px;
    overflow: auto;
    .rule-splice-line{
      width: 100%;
      height: 2px;
      background: #f5f4f4;
      padding:0 10px;
      margin: 20px 0;
    }
    .rule-content-body{
      display:flex;
      justify-content: space-between;
      padding: 20px 40px;
      .rule-title-left{
        flex:0 0 50px;
        position:relative;
        :deep(.ant-checkbox-wrapper){
            position: absolute;
            left: -20px;
            top: 50%;
            transform: translate(-50%, -50%)
        }
      }
      .line-top{
        width: 28px;
        height: 100px;
        border: 10px solid #efefef;
        display: block;
        border-right: none;
        border-bottom: none;
        margin-left: 10px;
      }
      .line-bottom{
        width: 28px;
        height: 100px;
        border: 10px solid #efefef;
        display: block;
        border-top: none;
        border-right: none;
        margin-left: 10px;
      }
      .rule-content-right{
          background: #eeee;
          flex: 1;
          border-radius: 2px;
          padding:15px 10px;
          cursor: pointer;
          .enter-x{
            display: flex;
            align-items: center;
            margin-bottom: 8px;
            :deep(.ant-form-item){
              margin-bottom:0;
            }
          }
      }
    }
  }
</style>