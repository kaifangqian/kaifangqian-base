<!--
  @description 人脸识别

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
<script lang="ts" setup>
  import { ref, onMounted, onUnmounted } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { Toast } from 'vant';
  import Api from '@/api/user';
  const route = useRoute();
  const router = useRouter();

  const data = {
    orderNo: route.query.bizOrderNo,
    userId: route.query.userId,
    taskId: route.query.taskId,
    signRuId: route.query.signRuId,
  };

  onMounted(async () => {
    console.log(data);
    // 后端检验是否实名通过
    const response: any = await Api.checkFace(data);
    console.log(response);
    setTimeout(() => {
      if (response && response.result) {
        Toast.success('实名认证成功');
        router.push({
          path: '/signContract',
          query: {
            signRuId: data.signRuId,
            taskId: data.taskId,
          },
        });
      } else {
        Toast.fail('实名认证失败');
        router.push({
          path: '/personal',
          query: {
            signRuId: data.signRuId,
            taskId: data.taskId,
            path: '/signContract',
          },
        });
      }
    }, 500);
  });
</script>

<template>
  <div class="face-loading">
    <van-overlay :show="true" z-index="0" />
    <van-loading color="#fff" type="spinner" :vertical="true">加载中...</van-loading>
  </div>
</template>
<style lang="less" scoped>
  .face-loading {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100vh;
  }
</style>
