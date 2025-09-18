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
  <div class="platform-config-content">
    <div class="content-tabs">
      <a-tabs
        v-model:activeKey="activeKey"
        tab-position="left"
        @tabClick="handleCancel"
        :destroy-inactive-tab-pane="true"
      >
        <!-- <a-tab-pane key="1" tab="网站配置">
          <tabWeb />
        </a-tab-pane> -->
        <a-tab-pane key="2" tab="短信配置">
          <tabMessage />
        </a-tab-pane>
        <a-tab-pane key="3" tab="邮箱配置">
          <tabEmail />
        </a-tab-pane>
        <!-- <a-tab-pane key="4" tab="证书配置">
          <tabCertificate />
        </a-tab-pane> -->
        <!-- <a-tab-pane key="5" tab="实名认证配置">
          <tabRealame />
        </a-tab-pane> -->
        <a-tab-pane key="6" tab="人脸识别配置">
          <tabSign />
        </a-tab-pane>
        <a-tab-pane key="7" tab="账号类型配置">
          <account />
        </a-tab-pane>
        <!-- <a-tab-pane key="7" tab="文件存储配置">Content of Tab Pane 3</a-tab-pane> -->
      </a-tabs>
    </div>
    <!-- <div class="content-body">
      
    </div> -->
  </div>
</template>

<script lang="ts">
  import { ref, defineComponent, onMounted } from 'vue';
  import { BasicTable, useTable } from '/@/components/Table';
  import Icon from '/@/components/Icon';
  import { message } from 'ant-design-vue';
  import { developerManageList, developerManageEdit } from '/@/api/interface';
  import { useModal } from '/@/components/Modal';
  import { cloneDeep } from 'lodash-es';
  import { CopyOutlined } from '@ant-design/icons-vue';

  import tabWeb from './tabs/web.vue';
  import tabMessage from './tabs/message.vue';
  import tabEmail from './tabs/email.vue';
  import tabCertificate from './tabs/certificate.vue';
  import tabRealame from './tabs/realame.vue';
  import tabSign from './tabs/sign.vue';
  import account from './tabs/account.vue';

  export default defineComponent({
    name: 'PlatformConfig',
    components: {
      tabWeb,
      tabMessage,
      tabEmail,
      tabCertificate,
      tabRealame,
      tabSign,
      account,
    },
    setup() {
      const activeKey = ref('2');
      const webConfigRef = ref();

      function handleCancel(key) {
        console.log('handleCancel:', key);
        if (key == '1') {
          // webConfigRef.value.initData();
        }
      }
      return {
        activeKey,
        handleCancel,
        webConfigRef,
      };
    },
  });
</script>

<style lang="less" scoped>
  .platform-config-content {
    .content-tabs {
      /* width:150px; */
    }

    .content-content {
      flex: 1;
    }
  }
</style>
