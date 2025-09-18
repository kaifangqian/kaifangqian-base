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
        <BasicForm @register="registerForm" class="modal-form"></BasicForm>
         <!-- <a-button block type="dashed" > 生成 </a-button> -->
        <Tabs>
          <TabPane key="1" tab="单号规则">
             <!-- <a-button  type="dashed" @click="handleAdd"> <template #icon><PlusOutlined /></template>新增 </a-button> -->
             <div class="splice-rule">
              <span>拼接规则：</span>
                  <a-radio-group v-model:value="state.link" name="radioGroup">
                    <a-radio value="">无符号拼接</a-radio>
                    <a-radio value="-">"-"拼接</a-radio>
                    <a-radio value="_">"_"拼接</a-radio>
                  </a-radio-group>
             </div>
             <div class="rule-list"> 
              <div>
                <Form :model="state">
                  <draggable :list="state.ruleList" :animation="300"   ghost-class="ghost" chosen-class="chosenClass">
                    <div v-for="(element,index) in state.ruleList" :key="index">
                        <Row class="enter-x" :gutter="6">
                          <Col :span="6">
                            <FormItem>
                              <div class="rule-type-select">
                                  <Icon icon="ant-design:menu-outlined" />
                                  <Select v-model:value="element.ruleType" :options="state.ruleTypes"  :allowClear="true" placeholder="请选择规则类型" @change="e=>handleRuleTypeChange(e,element)"/>
                              </div>
                            </FormItem>
                          </Col>
                          <Col :span="12" v-if="element.ruleType==='text'">
                            <FormItem :name="['ruleList', index, 'text']" :rules="{required:true, message:'内容不能空'}">
                              <Input   v-model:value="element.text"  placeholder="请输入"/>
                            </FormItem>
                          </Col>
                          <Col :span="12" v-if="element.ruleType==='datetime'">
                            <FormItem >
                              <Select v-model:value="element.datetime" placeholder="请选择日期" > 
                                  <SelectOption value="yyyy" >年，示例：2021</SelectOption>
                                  <SelectOption value="yyyyMM" >年月，示例：202101</SelectOption>
                                  <SelectOption value="yyyyMMdd" >年月日，示例：20210125</SelectOption>
                                  <SelectOption value="yyyyMMddHH" >年月日时，示例：2021012503</SelectOption>
                                  <SelectOption value="yyyyMMddHHmm" >年月日时分，示例：202101250313</SelectOption>
                                  <SelectOption value="yyyyMMddHHmmss" >年月日时分秒，示例：20210125031310</SelectOption>
                              </Select>
                            </FormItem>
                          </Col>
                          <Col :span="9" v-if="element.ruleType==='serialnum'">
                            <Select v-model:value="element.ruleLength" style="width:100%" placeholder="请选择序列号位数">
                                <SelectOption :value="item" :key="index" v-for="(item,index) in 10">{{item}}</SelectOption>
                            </Select>
                          </Col>
                          <Col :span="2" v-if="element.ruleType==='serialnum'">
                            <a-popover title="规则配置" trigger="click">
                                <template #content>
                                    <a-radio-group v-model:value="element.serialType" name="radioGroup" @change="handleSerialtype(element)">
                                        <!-- <a-radio value="initAuto">超出最大值后自动初始化</a-radio> -->
                                        <a-radio value="day">每天</a-radio>
                                        <a-radio value="week">每周</a-radio>
                                        <a-radio value="month">每月</a-radio>
                                        <!-- <a-radio value="quarter">每季度</a-radio> -->
                                        <a-radio value="year">每年</a-radio>
                                    </a-radio-group>
                                    <div class="date-input">
                                        <div class="date-input-item" v-if="(element.serialType !== 'day') && (element.serialType !== 'week') && (element.serialType !== 'month')">
                                          <Select v-model:value="element.month" style="width:100px">
                                            <SelectOption :value="item" :key="index" v-for="(item,index) in 12">{{item}}</SelectOption>
                                          </Select>
                                          <span>月</span>
                                        </div>
                                        <div class="date-input-item"  v-if="element.serialType !=='day' && element.serialType !== 'year'">
                                            <span>第</span> 
                                              <Input v-model:value="element.day" placeholder="" /> 
                                            <span>日</span>
                                        </div>
                                        <div class="date-input-item" v-if="element.serialType !=='day' && element.serialType !== 'week' && element.serialType !== 'month'">
                                            <Select v-model:value="element.day" style="width:100px">
                                              <SelectOption :value="item" :key="index" v-for="(item,index) in 31">{{item}}</SelectOption>
                                            </Select><span>日</span>
                                        </div>
                                        <div class="date-input-item">
                                            <Select v-model:value="element.hour" style="width:100px">
                                              <SelectOption :value="item" :key="index" v-for="(item,index) in 24">{{item}}</SelectOption>
                                            </Select><span>时</span>
                                        </div>
                                        <div class="date-input-item">
                                            <Select v-model:value="element.minute" style="width:100px">
                                              <SelectOption :value="item" :key="index" v-for="(item,index) in 60">{{item}}</SelectOption>
                                            </Select> <span>分</span>
                                        </div>
                                    </div>
                                  </template>
                                <p class="pop-text">初始化规则</p>
                              </a-popover>
                          </Col>
                          <Col :span="4" class="column-action">
                              <Icon icon="ant-design:plus-square-outlined" size="24" color="#8d8686"  @click="handleAdd" />
                              <Icon icon="ant-design:minus-square-outlined" size="24" color="#8d8686" v-if="index !==0" @click="handleDelete(index)"/>
                          </Col>
                        </Row>
                    </div>
                  </draggable>
                </Form>
              </div>
             </div>
          </TabPane>
        </Tabs>
      </BasicModal>
  </div>
</template>
<script lang='ts'>
  import { defineComponent,ref,unref,computed,reactive, watch,  getCurrentInstance } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { Checkbox,Form, Input, Tabs, Row, Col,Select } from 'ant-design-vue';
  import { PlusOutlined } from '@ant-design/icons-vue';
  import Icon from '/@/components/Icon';
  import { BasicForm, useForm } from '/@/components/Form';
  import { serialModalFormSchema } from '../data';
  import { addOrderNumber, editOrderNumber, getOrderNumberRule } from '/@/api/dict';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { VueDraggableNext } from 'vue-draggable-next';
  import { cloneDeep } from 'lodash-es';

  export interface RuleItem {
    ruleType:string;
    text?:string;
    paramsName?:string;
    datetime?:string;
    timestamp?:string;
    dateStart?:string;
    dateEnd?:string;
    timeStart?:string;
    timeEnd?:string;
    serialType?:string;
    month?:string;
    day?:string;
    hour?:string;
    minute?:string;
    initType?:string;
    customCode?:string;
    class?:string;
    params?:string;
    id?: number;
    ruleLength?:string;
    editValueRefs?:string;
    ruleContent?:string;
    orderNum?:string | number;
  }
  export default defineComponent({
    name: 'DictModal',
    components:{
      Icon,
      PlusOutlined,
      BasicModal,
      BasicForm,
      Tabs,
      TabPane: Tabs.TabPane,
      Checkbox,
      Select,
      SelectOption:Select.Option,
      Form,
      FormItem: Form.Item,
      Input,
      Row,
      Col,
      draggable:VueDraggableNext
    },
    setup(_, { emit }){
      const instance = getCurrentInstance();
      const isUpdate = ref(true);
      const rowId = ref('');
      const tinymceValue = ref();
      const state = reactive({
        link:'1',
        initType:'1',
        ruleTypes:[
          { label: '文本',  value: 'text', },
          { label: '业务编号',  value: 'business', },
          { label: '日期',  value: 'datetime', },
          { label: '时间戳',  value: 'timestamp', },
          { label: '序列号', value: 'serialnum', },
        ],
        ruleList:[{serialType:'',ruleContent:'',ruleType:'',text:''}] as RuleItem[],
        classList:[
          {label:'测试类',value:1},
          {label:'测试类2',value:2},
        ],
        hour:'',
        minute:'',
        dateStart:'',
        dateEnd:'',
        paramsName:'',
        timeStart:'',
        timeEnd:'',
        timestrap:''

      })
      const { createMessage: msg } = useMessage();
      const [registerForm, { setFieldsValue, resetFields, validate }] = useForm({
        labelWidth: 100,
        schemas: serialModalFormSchema,
        showActionButtonGroup: false,
        actionColOptions: {
          span: 23,
        },
      });

      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        resetFields();
        setModalProps({ 
          confirmLoading: false,
          width:1200,
          cancelText:'关闭' 
        });
        isUpdate.value = !!data?.isUpdate;
        if (unref(isUpdate)) {
          rowId.value = data.record.id;
          setFieldsValue({
            ...data.record,
          });
          let ruleInfo = await getOrderNumberRule({id:unref(rowId)});
          if(ruleInfo){
            ruleInfo.globalidConfigInfoList.sort((a, b) => a.orderNum- b.orderNum);
            state.ruleList = [{serialType:'',ruleContent:''}] as RuleItem[]
            state.link = ruleInfo.link;
            if(ruleInfo.globalidConfigInfoList.length){
              state.ruleList = [] as RuleItem[];
              ruleInfo.globalidConfigInfoList.map(item=>{
                let ruleItem = {
                  ruleType:item.ruleType,
                  month:'',
                  day:'',
                  hour:'',
                  minute:'',
                  ruleLength:'',
                  serialType:'',
                  text:'',
                }
                if(item.ruleType==='text'){
                  ruleItem.text = item.ruleContent;
                }
                if(['business','datetime','timestamp'].includes(item.ruleType)){
                  ruleItem[item.ruleType] = item.ruleContent
                }
                if(item.ruleType==='serialnum'){
                  ruleItem.month = '';
                  ruleItem.day = '';
                  ruleItem.hour = '';
                  ruleItem.minute = '';
                  ruleItem.ruleLength = item.ruleLength;
                  ruleItem.serialType = item.serialType;
                  if(item.ruleContent){
                    let expression = item.ruleContent.replace(/\s*/g,"");
                    ruleItem.minute = expression.substr(1,1);
                    ruleItem.hour = expression.substr(2,1);
                    ruleItem.day = expression.substr(3,1);
                    ruleItem.month = expression.substr(4,1);
                    // 数据回显 (无法区分week/month )
                    if(ruleItem.month != '*'){
                        ruleItem.serialType = 'year';
                    }else if(ruleItem.day == '*'){
                      ruleItem.serialType = 'day';
                    }else{
                      ruleItem.serialType = 'month';
                    }
                  } else {
                    // 默认值
                    ruleItem.serialType = 'year';
                    ruleItem.month = '1';
                    ruleItem.day = '1';
                    ruleItem.hour = '0';
                    ruleItem.minute = '0';
                  }
                }
                state.ruleList.push(ruleItem);
              })
            }
          }
        }else{
          rowId.value = '';
          state.ruleList = [{serialType:'',ruleContent:''}] as RuleItem[]; 
        }
      });

      const getTitle = computed(() => (!unref(isUpdate) ? '新增' : '编辑'));
      // cloneDeep 解决新旧值一样的问题
      watch(
        () => [...cloneDeep(state.ruleList)],
        (newVal, oldVal) => {
           newVal.map(item=>{
            if(item.ruleType === 'serialnum' ){
              console.log(item);
              item.ruleContent =  `${ "*"} ${item.minute || "*"} ${item.hour || "*"} ${item.day || "*"} ${item.month || "*"} ${"?"} ${ "*" }`;
            }
          }) 
          console.log(oldVal)
        },
        { deep: true },
      );


      async function handleSubmit() {
        try {
          const values = await validate();
          let  infoList:[]= [];
          state.ruleList.map( (item,index) =>{
            let ruleContent:string|undefined = '',ruleLength:number = 0;
            if(['business','datetime','timestamp'].includes(item.ruleType)){
              ruleContent = item[item.ruleType]
            }
            if(item.ruleType==='serialnum'){
              if(item.minute || item.hour || item.day || item.month ){
                ruleContent =   `${ "*"} ${item.minute || "*"} ${item.hour || "*"} ${item.day || "*"} ${item.month || "*"} ${"?"} ${ "*" }`;
              }else{
                ruleContent = '';
              }
              ruleLength = Number(item.ruleLength);
            }
            if(item.ruleType==='text'){
              ruleContent = item.text;
            }
            infoList.push({
              orderNum:index+1,
              serialType:item.ruleType==='serialnum'?item.serialType:undefined,
              ruleContent:ruleContent,
              ruleLength:item.ruleType==='serialnum'?ruleLength:undefined,
              ruleType:item.ruleType
            })

          })
          // TODO custom api
          let result;
          let params = {
              ...values,
              id:unref(rowId),
              globalidConfigInfoList:infoList,
              link:state.link
              
          }
          setModalProps({ confirmLoading: true });
          if(!unref(isUpdate)){
              result = await addOrderNumber(params);
          }else{
              result = await editOrderNumber(params);
          }
          if(result){
            msg.success('保存成功');
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
      function handleSerialtype(record){
          record.year = '';
          record.month = '';
          record.day = '';
          record.hour = '';
          record.minute = '';
      }
      function handleEditChange(data: Recordable) {
        console.log(data);
      }
      function handleDelete(index) {
         state.ruleList.splice(index,1)
      }
      function handleAdd() {
        state.ruleList.push({ruleContent:''} as any);
      }
      function handleRuleTypeChange(e,record){
        if(e === 'serialnum'){
          record.serialType = 'year';
          record.ruleLength = 6;
          record.month = 1;
          record.day = 1;
          record.hour = 0;
          record.minute = 0;
        }
        if(e === 'datetime'){
          record.datetime = 'yyyyMMdd'
        }
        // instance.proxy?.$forceUpdate()
        if(e === 'serialnum' || e === 'datetime' || e === 'timestamp'){
          let matchItem = state.ruleList.filter((item)=>{
            console.log(item,e,'322222')
            return item.ruleType == e;
          })
          console.log(matchItem)
          if(matchItem.length == 2){
            record.ruleType =  '';
            msg.warning('已存在该类型，不可重复添加');
          } 
        }
      }

      return {state,handleSerialtype, handleDelete,registerModal,handleEditChange, registerForm, getTitle, handleSubmit,tinymceValue,handleTinymceChange,handleAdd,handleRuleTypeChange };
    }
  })
</script>
<style lang="less" scoped>
.rule-list{
  // padding: 15px;
  .rule-type-select{
    display: flex;
    align-items: center;
    .app-iconify{
      margin-right:10px;
      cursor: pointer;
    }
  }
  border: 1px solid #f0f0f0;
  border-radius: 2px;
   
  .enter-x{
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 15px;
    border-bottom: 1px solid #d9d9d9;
    :deep(.ant-form-item){
      margin-bottom: 0;
     
    }
  }
}
.column-action{
  .app-iconify {
    cursor: pointer;
  }
}
.splice-rule{
  margin: 25px 0 5px;
  padding-left: 25px;
}
.modal-form{
  :deep(.ant-row){
    justify-content: center;
  }
}
.pop-text{
  color: #127fd2;
  text-decoration: underline;
  margin-bottom: 0;
  cursor: pointer;

}
.date-input{
    margin-top:10px;
    display: flex;
      .date-input-item{
          input{
            width:100px;
          }
          span{
            margin:0 5px;
          }
      }
    }
 
</style>
