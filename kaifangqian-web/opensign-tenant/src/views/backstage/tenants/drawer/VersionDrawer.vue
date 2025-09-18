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
  width="20%"
  @ok="handleSubmit"
  >
  <BasicForm @register="registerForm" >
    <template #limitKeys key="limitKeys">
      <BasicTree
            ref="basicTree"
            title=""
            :toolbar="false"
            :toggleSwitch="false"
            :checkable="true"
            :selectedKeys="selectedKeys"
            :checkedKeys="treeCheckedKeys"
            :search="false"
            :fieldNames="{ title: 'name', key: 'id' }"
            :treeData="treeData"
            :checkStrictly="true"
            :checkStrictlySwitch="true"
        />
    </template>   
  </BasicForm>
</BasicDrawer>
</template>

<script lang="ts">
import {reactive, ref,computed,unref, toRaw} from "vue";
import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
import { BasicForm, useForm } from '/@/components/Form/index';
import { versionFormSchema, } from '../data';
import { addApp, updateApp, getAppInfo } from '/@/api/backstage';
import { BasicTree,TreeItem,TreeActionType} from '/@/components/Tree/index';
import { eachTree } from '/@/utils/helper/treeHelper';
import { getAllMenuBtnList } from '/@/api/auth/group';


interface RuleItem {
    permissionId:string;
    ruleIds:Array<string|number>
}

export default{
name:"App",
components:{
  BasicDrawer,
  BasicForm,
  BasicTree,
},
setup(_,{emit}) {

    const checkedKeys = ref<Array<string | number>>([]);
    const recordId = ref();
    const isUpdate = ref(false);
    const treeData = ref<TreeItem[]>([]);
    const selectedKeys = ref<Array<string | number>>([]);
    const treeCheckedKeys = ref<Array<string | number>>([]);
    const ruleVOS = ref<RuleItem[]>([]);
    const getTitle = computed(() => (!unref(isUpdate) ? '添加版本' : '编辑版本'));
    const basicTree = ref<Nullable<TreeActionType>>(null);
    const currentPermissionId = ref('');


    const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
      labelWidth: 60,
      schemas: versionFormSchema,
      showActionButtonGroup: false,
      baseColProps: { lg: 24, md: 24 },
    });
    const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
      resetFields();
      recordId.value = data.record?.id;
      isUpdate.value = data.isUpdate;
      fetch();
      if(unref(recordId)){
        let result = await getAppInfo({id:unref(recordId)});
        if(result){
          setFieldsValue({
            ...result
          })
        }
      }
    })
    function getTree() {
        const tree = unref(basicTree);
        if (!tree) {
          throw new Error('tree is null!');
        }
        return tree;
    }
      
    async function fetch() {
        treeData.value =  (await getAllMenuBtnList({})) as unknown as TreeItem[];
        eachTree( treeData.value,(item, _parent)=>{
            if(item.menuType===2){
              item.icon = 'ant-design:safety-certificate-outlined';
              item.color = '#E34D59';
            }else{
              item.icon = '';
            }
        })
      }
  
    async function handleSubmit() {
      try {
        const values = await validate();
        setDrawerProps({ confirmLoading: true });
     

        const keys = toRaw(getTree().instance.getCheckedKeys());
        // 默认权限
        let defaultRuleVOS:RuleItem[] = unref(ruleVOS).filter((item)=>{
          return item.permissionId !== unref(currentPermissionId)
        });
        let filterKeys = keys.checked?keys.checked:keys; // 层级关联是
        let parentKeys = filterKeys.filter(key=>key !== unref(currentPermissionId));
        parentKeys.map(key=>{
          defaultRuleVOS.push({
            permissionId:key,
            ruleIds:[]
          })
        })
        defaultRuleVOS.push({
          permissionId:unref(currentPermissionId),
          ruleIds:unref(checkedKeys)
        })
        let params = {
          groupId:unref(recordId),
          ruleVOS:defaultRuleVOS
        }
        console.log(params,'------组装好的数据-----')



        let result = reactive({});
        if(unref(isUpdate)){
          result = await addApp({
            ...values,
            id:unref(recordId)
          })
        }else{
          result = await updateApp(values);
        }
        if(result){
          closeDrawer();
          emit('success');
        }
       
      } finally {
        setDrawerProps({ confirmLoading: false });
      }
    }


  return {
    registerForm,
    handleSubmit,
    checkedKeys,
    registerDrawer,
    getTitle,
    treeData,
    selectedKeys,
    treeCheckedKeys
  }
}
}
</script>

<style lang="less" scoped>
.resrun-tree.open{
  width:300px;
}
</style>
