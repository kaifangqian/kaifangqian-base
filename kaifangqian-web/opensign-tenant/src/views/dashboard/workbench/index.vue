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
  <div :loading="loading" :class="prefixCls">

    <div :class="`${prefixCls}-header`">
      <div class="header-title">
        <div class="title">   {{ `${period}`+'，欢迎回来！' }}  </div>
        <div class="title-desc">你可以从这里访问以下应用</div>
      </div>
      <div :class="`${prefixCls}-content`">
        <div class="application-list">
          <div class="application-item" v-for="(item,index) in apps" :key="index" @click="handleAppJump">
            <img  class="app-icon" src="../../../assets/svg/illustration.svg" alt="">
            <span class="app-name">{{ item.name }}</span>
          </div>

        </div>
        
      </div>
      <!-- <img class="xl:h-50 h-30 mx-auto" src="../../../assets/svg/illustration.svg" /> -->
    </div>
  </div>
</template>
<script lang="ts" setup>
  import { ref , onUnmounted} from 'vue';  
  import { useAppStore } from '/@/store/modules/app';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { getTimeState } from '/@/utils/';
  import { useRedo } from '/@/hooks/web/usePage';
  import { useGo } from '/@/hooks/web/usePage';
  import { PageEnum } from '/@/enums/pageEnum';

  const { prefixCls } = useDesign('workbench');

  const appStore = useAppStore();
  const redo = useRedo();
  const go = useGo();
  appStore.setProjectConfig({
    menuSetting: {
      show:false
    },
  })
  const period = ref('');
  period.value = getTimeState();
  const apps = ref([
    {name:'签到', appUrl:''},
    {name:'考勤打卡'},
    {name:'日志'},
    {name:'公告'},
    {name:'OA审批'},
    {name:'邮箱'},
  ])

  onUnmounted(() => {
    appStore.setProjectConfig({
      menuSetting: {
        show:true
      },
    })
  })
  function handleAppJump(item){
    console.log(item,'-----')
    setTimeout(() => {
      // window.location.reload();
      go(PageEnum.DEFAULT_HOME);
    })


  }
  const loading = ref(true);

  setTimeout(() => {
    loading.value = false;
  }, 1500);
</script>

<style lang="less" scoped>
 @prefix-cls: ~'@{namespace}-workbench';
.@{prefix-cls} {
  &-header{
    height:90px;
    width:100%;
    background-color:#f3f4ff;
    // background-image: url('../../../assets/svg/illustration.svg');
    .header-title{
      width: 1200px;
      height: 100%;
      margin:0 auto;
      display: flex;
      align-items: center;
    }
    .title{
      font-size: 14px;
      font-weight: 600;
    }
    .title-desc{
      font-size: 12px;
      line-height: 14px;
    }
  }
  &-content{
    width:1200px;
    margin:30px auto;
    .application-list {
      display: flex;
      justify-content: flex-start;
      flex-wrap: wrap;
      .application-item{
        cursor: pointer;
        width:200px;
        border-radius: 4px;
        border:1px solid #dedede;
        display: flex;
        align-items: center;
        padding:15px 10px;
        box-sizing: border-box;
        justify-content: flex-start;
        margin:10px;
        img{
          width:50px;
          margin-right: 10px;
        }
        &:not(:nth-child(5n)) {
          margin-right: calc(8% / 3);
      }
      }
    }
  }
}
</style>
