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
  <Button v-bind="getBindValue" :class="getButtonClass" @click="handleClick">
    <template #default="data">
      <Icon :icon="preIcon" v-if="preIcon" :size="iconSize" />
      <slot v-bind="data || {}"></slot>
      <Icon :icon="postIcon" v-if="postIcon" :size="iconSize" />
    </template>
  </Button>
  <SensitiveModal :visible="visible" @cancelModal="handleCancel" @confirm="handleConfirm" :sensitiveVerifyType="sensitiveVerifyType"/>
</template>

<script lang="ts">
  import { defineComponent } from 'vue';
  export default defineComponent({
    name: 'SensitiveButton',
    inheritAttrs: false,
  });
</script>
<script lang="ts" setup>
  import { computed, unref,ref  } from 'vue';
  import { Button } from 'ant-design-vue';
  import Icon from '/@/components/Icon/src/Icon.vue';
  import { buttonProps } from './props';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  import SensitiveModal from './SensitiveModal.vue';
  import { usePermission } from '/@/hooks/web/usePermission';
  // import { getSensitiveTime } from '/@/utils/auth';


  const props = defineProps(buttonProps);
  const { isSensitive } = usePermission();
  const emit = defineEmits(["onClick"]);
  const visible = ref(false);
  const sensitiveVerifyType = ref('phone');
  // get component class
  const attrs = useAttrs({ excludeDefaultKeys: false });
  const getButtonClass = computed(() => {
  const { color, disabled,sensitiveType } = props;
  console.log(sensitiveType,'----敏感操作类型------')
    return [  
      {
        [`ant-btn-${color}`]: !!color,
        [`is-disabled`]: disabled,
      },
    ];
  });
  function handleCancel(){
    visible.value = false;
  }

  function handleClick(){
   if(isSensitive()){
      visible.value = true;
   }else{
    visible.value = false;
    emit('onClick');
   }
  }
  function handleConfirm(){
    visible.value = false;
    emit('onClick');
  }

  // get inherit binding value
  const getBindValue = computed(() => ({ ...unref(attrs), ...props }));
</script>

<style lang="less" scoped>
.sensitive-modal{
  .ant-modal{
    min-height: 220px;
    .ant-modal-body{
      padding:24px;
    }
  }
}

</style>

