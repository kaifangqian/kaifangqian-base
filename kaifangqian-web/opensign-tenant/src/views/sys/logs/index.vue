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
  <Card>
      <Tabs :class="prefixCls" v-model:value="activeTab" @change="handleTabChange">
        <TabPane key="operation" tab="操作日志">
            <Operation  v-if="activeTab === 'operation'"/>
        </TabPane>
        <TabPane key="abnormal" tab="异常日志">
            <Abnormal  v-if="activeTab === 'abnormal'"/>
        </TabPane>
        <TabPane key="security" tab="安全预警日志">
          <Security  v-if="activeTab === 'security'" />
        </TabPane>
      </Tabs>
  </Card>
</template>
<script lang='ts'>
  import { defineComponent, ref } from 'vue';
  import { Tabs,Card } from 'ant-design-vue';
  import { useDesign } from '/@/hooks/web/useDesign';

  import Operation from './Operation.vue';
  import Abnormal from './Abnormal.vue';
  import Security from './Security.vue';
  export default defineComponent({
    name: 'index',
    components:{
      Tabs, TabPane: Tabs.TabPane,Operation,Abnormal,Security,Card
    },
    setup(){
      const { prefixCls } = useDesign('sys-logs');
      const activeTab = ref('operation');
      function handleTabChange(val){
        activeTab.value = val;
      }
      return {
        prefixCls,
        activeTab,
        handleTabChange
      }
    }
  })
</script>
<style lang="less">
//   .resrun-basic-table-form-container .ant-form{  
//     border-bottom: 1px solid #f0f2ff;
//  }

</style>