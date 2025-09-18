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
      <Tabs :class="prefixCls">
        <TabPane key="operation" tab="密码规则设置">
            <Password />
        </TabPane>
        <TabPane key="abnormal" tab="访问规则设置">
            <Access />
        </TabPane>
        <TabPane key="IP" tab="IP限制">
            <IPLimit />
        </TabPane>
        <TabPane key="security" tab="敏感操作保护设置">
          <Sensitive />
        </TabPane>
      </Tabs>
  </Card>
</template>
<script lang='ts'>
  import { defineComponent } from 'vue';
  import { Tabs,Card } from 'ant-design-vue';
  import { useDesign } from '/@/hooks/web/useDesign';

  import Password from './Password.vue';
  import Access from './Access.vue';
  import IPLimit from './IPLimit.vue';
  import Sensitive from './Sensitive.vue';
  export default defineComponent({
    name: '安全配置',
    components:{
      Tabs, TabPane: Tabs.TabPane,Password,Access,Sensitive,Card,IPLimit
    },
    setup(){
      const { prefixCls } = useDesign('sys-logs');
      return {
        prefixCls
      }
    }
  })
</script>
<style lang="less">
//   .resrun-basic-table-form-container .ant-form{  
//     border-bottom: 1px solid #f0f2ff;
//  }
 .ant-input-number{
    width:220px;
 }

</style>