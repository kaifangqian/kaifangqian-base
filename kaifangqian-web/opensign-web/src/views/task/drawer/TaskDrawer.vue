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
    width="70%"
    @ok="handleSubmit"
    >
      <Card title="任务定义" size="small">
        <BasicForm @register="registerForm" />
      </Card>

      <Card title="触发规则" size="small">
        <div class="rule-type-list">
          <a-radio-group v-model:value="state.cronType" name="radioGroup" @change="handleCronTypeChange">
            <a-radio value="day">每天</a-radio>
            <a-radio value="week">每周</a-radio>
            <a-radio value="month">每月</a-radio>
            <!-- <a-radio value="quarter">每季度</a-radio> -->
            <a-radio value="year">每年</a-radio>
            <a-radio value="cron">Cron表达式</a-radio>
            <a-radio :value="3">固定频率（毫秒）</a-radio>
            <a-radio :value="4">固定延迟（毫秒）</a-radio>
            <a-radio :value="1">API</a-radio>
          </a-radio-group>
        </div>
        <div class="rule-select">
          <a-form
              layout="inline"
              :model="state"
            >
              <a-form-item  v-if="state.cronType === 'quarter' || state.cronType === 'year'">
                <a-select v-model:value="state.month" placeholder="">
                  <a-select-option :value="item" :key="index" v-for="(item,index) in 12">{{item}} </a-select-option>
                </a-select>
                <span class="custom-label">月</span>
              </a-form-item>
              <a-form-item v-if="state.cronType !== 'day' && state.cronType !== 'cron' && state.cronType != 3 && state.cronType !== 4 && state.cronType !== 1">
                <span class="custom-label" v-if="state.cronType === 'week'|| state.cronType === 'month'">第</span>
                <a-select v-model:value="state.day" placeholder="" v-if="state.cronType !== 'week' && state.cronType !== 'month'">
                  <a-select-option :value="item" :key="index" v-for="(item,index) in 31">{{item}} </a-select-option>
                </a-select>
                <a-input v-model:value="state.day" v-if="state.cronType === 'week'|| state.cronType === 'month'"></a-input>
                <span class="custom-label" >日</span>
              </a-form-item>
              <a-form-item v-if="state.cronType !== 'cron' && state.cronType !== 3 && state.cronType !== 4 && state.cronType !== 1">
                <a-select v-model:value="state.hour" placeholder="">
                  <a-select-option :value="item" :key="index" v-for="(item,index) in 24">{{item}} </a-select-option>
                </a-select>
                <span class="custom-label" >时</span>
              </a-form-item>
              <a-form-item v-if="state.cronType !== 'cron' && state.cronType !== 3 && state.cronType !== 4 && state.cronType !== 1">
                <a-select v-model:value="state.minute" placeholder="">
                  <a-select-option :value="item" :key="index" v-for="(item,index) in 60">{{item}} </a-select-option>
                </a-select>
                <span class="custom-label" >分</span>
              </a-form-item>
              <a-form-item v-if="state.cronType === 'cron'">
                <a-input v-model:value="state.cronExpress" placeholder="请输入cron表达式，【示例：0 15 10 ? * 6#3】" style="width:500px"></a-input>
              </a-form-item>
              <a-form-item v-if="state.cronType === 3 || state.cronType == 4">
                <span class="custom-label" >执行间隔</span>
                <a-input-number  v-model:value="state.timeExpression" placeholder="请输入执行间隔" style="width:200px;"></a-input-number>
                <span  class="custom-label" > 毫秒</span>
              </a-form-item>
            </a-form>
            <div style="margin-top:10px;" v-if="state.cronType !==3 && state.cronType !=='cron' && state.cronType !== 4 && state.cronType !== 1">
                <span>Corn结果</span>
                <a-input disabled v-model:value="state.cronResult"></a-input>
            </div>
        </div>
        <!-- <Vue3CronCore i18n="cn" maxHeight="350px" @change="changeCron" v-model:value="state.cron" style="flex: 0.4" /> -->
      </Card>
      <Card title="状态" size="small">
        <Switch  v-model:checked="taskStatus" checkedChildren="启用" unCheckedChildren="禁用"/>
      </Card>
    </BasicDrawer>
</template>
<script lang='ts'>
import { defineComponent, computed, ref ,unref,reactive} from 'vue';
import { useMessage } from '/@/hooks/web/useMessage';
import { Card,Switch } from 'ant-design-vue';
import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
import { BasicForm, useForm } from '/@/components/Form/index';
import Vue3CronCore from "/@/components/Cron/Index.vue";
import { taskFormSchema } from '../data';
import { addTaskList} from '/@/api/task';
import { useUserStore } from '/@/store/modules/user';

export default defineComponent({
  name: 'TaskDrawer',
  components:{
     BasicDrawer, BasicForm,Card,Vue3CronCore, Switch
  },
  setup(_, { emit }){



    const state = reactive({
        cron: "* * * * * ? *",
        cronType:'day',
        minute:'',
        hour:'',
        day:'',
        month:'',
        year:'',
        cronExpress:'',
        frequencyCount:'',
        timeExpression:null,
        cronResult: computed(() => {
          return `${ "*"} ${state.minute || "*"} ${state.hour || "*"} ${state.day || "*"} ${state.month || "*"} ${"?"} ${
            "*"
          }`;
      }),
    });
    const userStore = useUserStore();
    const appId = userStore.getUserInfo.jobAppId ;
    const createTime = ref('');
    const isUpdate = ref(true);
    const recordId = ref('');
    const taskStatus = ref(true);
    const { createMessage: msg } = useMessage();
    const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
      labelWidth: 120,
      schemas: taskFormSchema,
      showActionButtonGroup: false,
      // baseColProps: { lg: 12, md: 12 },
    });
    const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
        resetFields();
        setDrawerProps({ confirmLoading: false });
        isUpdate.value = !!data?.isUpdate;
        if (unref(isUpdate)) {
          recordId.value = data.record.id;
          setFieldsValue({
            ...data.record,
            // taskStatus:data.record.status===1?true:false
          });
          taskStatus.value = data.record.status===1?true:false;
          //2 cron表达式 非2 对应类型
          if(data.record.timeExpressionType != 2){
            state.cronType = data.record.timeExpressionType;
            state.timeExpression = data.record.timeExpression;
          }else{
            if(data.record.timeExpression){
              let expression = data.record.timeExpression.replace(/\s*/g,"");
              state.minute = expression.substr(1,1);
              state.hour = expression.substr(2,1);
              state.day = expression.substr(3,1);
              state.month = expression.substr(4,1);
            }
          }
        }else{
          recordId.value= '';
        }
    });
    const getTitle = computed(() => (!unref(isUpdate) ? '添加' : '编辑'));

    function changeCron (val) {
        if (typeof val !== "string") return false;
        state.cron = val;
    };
    async function handleSubmit() {
        try {
          const values = await validate();
          setDrawerProps({ confirmLoading: true });
          // TODO custom api
          let expressionType:string | number = '', expression:string="";
          if(state.cronType === 3 || state.cronType=== 4 || state.cronType=== 1 ){
            expressionType = state.cronType;
            expression = state.timeExpression;
          }else{
            expressionType = 2;
            expression = state.cronResult;
          }
          let params = {
            timeExpressionType:expressionType,
            timeExpression:expression,
            ...values,
            appId:appId,
            status:unref(taskStatus)?1:2,
            id:unref(recordId),
            dispatchStrategy:1,
          }

          let result = await addTaskList({ ...params});
          if(result){
              msg.success(unref(recordId)?'编辑成功':'新增成功');
              console.log(values);
              closeDrawer();
              emit('success');
          }
         
        } finally {
          setDrawerProps({ confirmLoading: false });
        }
    }

    function onChangeCron(){
      
    }
    function handleCronTypeChange(){
      state.timeExpression = '';
      state.minute = '';
      state.hour = '';
      state.day = '';
      state.month = '';
    }

    return {
      handleSubmit,
      registerDrawer,
      state,
      getTitle,
      changeCron,
      registerForm,
      onChangeCron,
      createTime,
      taskStatus,
      handleCronTypeChange

    }

  }
})
</script>
<style lang="less" scoped>
.rule-select{
  margin-top:10px;
  :deep(.ant-form-item){
    min-width: 150px;
    margin-right: 0;
    .ant-form-item-control-input-content{
      display: flex;
      align-items: center;
      .custom-label{
        margin:0 5px;
        white-space: nowrap;
      }
    }
  }
}
.ant-card{
  margin-bottom:10px;
}
</style>
