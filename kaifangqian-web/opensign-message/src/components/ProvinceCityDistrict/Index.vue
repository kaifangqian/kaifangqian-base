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
  <a-cascader
    :fieldNames="{ label: 'name', value: 'code', children: 'children' }"
    :options="areaData"
    :placeholder="placeholder"
    v-model="selectedValues"
    @change="onChange"
  />
</template>
<script>
import areaData from "./areadata.json";
export default {
  name: "areaSelect",
  props: {
    placeholder: {
      type: String,
      default: "请选择省市区",
    },
    defaultValue: {
      type: Array,
      default() {
        return [];
      },
    },
  },
  data() {
    return {
      //地区数据
      areaData,
      //选择的数据
      selectedValues: [],
    };
  },
  created() {
    if (this.defaultValue.length) {
      this.selectedValues = [...this.defaultValue];
    }
  },
  watch: {
    defaultValue(newValue) {
      if (newValue.length) {
        this.selectedValues = newValue;
      } else {
        this.selectedValues = [];
      }
    },
  },
  methods: {
    //选择好之后的回调
    onChange(value) {
      this.$emit("change", value);
    },
  },
};
</script>
<style lang="scss" scoped>
</style>