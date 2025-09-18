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
  <div class="my_ip_address_input">
    <template v-for="index in indexes" :key="index">
      <Input  v-model:value="ipAddress[index]"
                @keyup="handleChange"
                oninput="value=value.replace(/[^\d]/g,'')&&value>255?255:value"/>
    </template>
  </div>
</template>

<script>
import {defineComponent, ref} from "vue";
import {  Input } from 'ant-design-vue';
const indexes=[0,1,2,3]
export default defineComponent({
  name: "MyIpAddressInput",
  components:{Input},
  props: {
    value: {
      type: String,
      default: ""
    }
  },
  setup(props,ctx) {
    const ipAddress = ref([])
    ipAddress.value = props.value.split(".")
    const handleChange = () => {
      console.log(ipAddress.value.join("."))
      ctx.emit("update:value",ipAddress.value.join("."))
    }
    return {
      ipAddress,
      handleChange,
      indexes
    }
  }
})
</script>
<style lang="less" scoped>
.my_ip_address_input {
  border-radius: 4px;
  width:100%;
  display: inline-block;
  border: 1px solid #DCDFE6;

  .el-input {
    width: 24%;

  }

  .el-input__inner {
    border: none !important;
    text-align: center;
  }
}

.my_ip_address_input:hover {
  border-color: #f9f9f9;
}

.my_ip_address_input:focus-within {
  border-color: #f9f9f9;

}
</style>
