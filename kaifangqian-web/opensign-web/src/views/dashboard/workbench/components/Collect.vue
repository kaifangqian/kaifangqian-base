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
  <div class="main-collect">
    <Card size="small" title="收藏">
      <template #title>
        <div>
          <span>我的收藏</span>
          <Icon icon="ant-design:plus-outlined" />
          <span>添加</span>
        </div>
      </template>
      <ul class="collect-list">
        <li v-for="item in collectData">
          <span>{{ item.name }}</span>
        </li>
      </ul>
    </Card>
  </div>
</template>

<script lang="ts">
import {ref,defineComponent} from "vue";
import { Card } from 'ant-design-vue';
import Icon from '/@/components/Icon';
export default defineComponent({
  name:"Collect",
  components:{
    Card,
    Icon
  },
  setup() {
    const data = ref('');
    const collectData = ref([
      {name:'订单管理',icon:''},
      {name:'采购申请',icon:''},
      {name:'员工档案',icon:''},
    ]) 
    return {
      collectData,
      data  
    }
  }
})
</script>
<style lang="less" scoped>
.ant-card{
  margin:20px 0;
}
.collect-list{
  display: flex;
  justify-content: flex-start;
  li{
    width:160px;
    padding:10px 20px;
    box-sizing: border-box;
    margin-right: 20px;
    &:hover{
      background:#f5f5f5;
    }
  }

}

</style>
