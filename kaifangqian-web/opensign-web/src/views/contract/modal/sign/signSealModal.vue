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
  <div>
       <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit" :destroyOnClose="true">
        <ul class="seal_list">
          <template v-if="sealList&&sealList.length">
          <li v-for="(item,index) in sealList"  :key="index">
            <div class="seal-item">
              <div class="seal-img-box" @click="handleUse(item)">
                <img class="seal-img" :src="item.signature"/>
                <a-button class="use-btn" type="primary" @click="handleUse(item)">使用</a-button>
              </div>
              <a-tooltip>
                <template #title> {{item.sealName}} </template>
                <div class="seal-name">
                  {{item.sealName}}
                </div>
              </a-tooltip>
            </div>
          </li>
        </template>
          <div v-else>
            <p class="no-data">
                <!-- <img src="~@/assets/images/no-data.png" alt=""> -->
                您尚未拥有可以签署的企业印章，请联系企业印章管理员为您授权相应印章的使用权限。
            </p>
          </div>
        </ul>

      </BasicModal>
  </div>
</template>
<script lang='ts'>
  import { defineComponent,ref,unref,computed  } from 'vue'
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { getImgBase64} from '/@/api/sys/upload'; 
  import { getSignSealIds} from '/@/api/contract'; 
  
  export default defineComponent({
    name: 'SignSealModal',
    components:{
      BasicModal,
    },
    setup(_, { emit }){

      const isUpdate = ref(true);
      const sealList:any = ref([])

      const checkedKeys = ref<Array<string | number>>([]);
     
      const { createMessage: msg } = useMessage();
     

      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        
        await getSealIds();
        
        setModalProps({
          confirmLoading: false,
          width: sealList.value&&sealList.value.length>0?800:600,
          height:510,
          cancelText:'关闭',
          footer:null ,
          canFullscreen: false,
        });
        
        
      });
      const getTitle = computed(() => (!unref(isUpdate) ? '使用印章' : '使用印章'));

      async function getSealIds(){
        let result = await getSignSealIds({});
        if(result){
          sealList.value = result;
          loadSealBase64()
        }
      }
      function loadSealBase64(){
      	sealList.value.forEach((item:any)=>{
      		getImgBase64({imgId:item.annexId}).then((img:any)=>{
      			item.signature = img.image;
      		})
      	})
      }

      async function handleSubmit() {
        try {
            msg.success('保存成功');
            closeModal();
            emit('success', { });
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }

      function handleUse(row){
        // msg.success('操作成功');
        emit('success', row);
      }
      return { 
        registerModal, 
        getTitle, 
        handleSubmit,
        checkedKeys,
        sealList,
        handleUse
      };
    }
  })
</script>
<style lang="less" scoped>
 .seal_list{
    display: flex;
    flex-wrap:wrap;
    margin-top:25px;
    justify-content: center;
  }
  .no_seal_list{
    justify-content:center;
  }
  .seal-item{
    .seal-img-box{
      width: 166px;
      height: 166px;
      padding: 20px;
      border: 1px solid #eee;
      border-radius: 5px;
      margin:0 auto;
      position: relative;
      &:hover {
        border-color: #1890ff;
        background-color: #eeeeee6a;
        box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.1);
      }
      .seal-mask{
        position: absolute;
        left:0;
        top:0;
        right:0;
        bottom:0;
        background-color: rgba(187, 187, 187, 0.8);
        display: flex;
        align-items: center;
        justify-content: center;
        .seal-status-text{
          font-size: 16px;
        }
      }
      .seal-img {
        transition: opacity 0.3s ease;
      }
      .seal-img-box:hover .seal-img {
        opacity: 0.7;
      }
    }
    
    .use-btn {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      opacity: 0;
      transition: opacity 0.3s ease;
      z-index: 10;
    }
    
    .seal-img-box:hover .use-btn {
      opacity: 1;
    }
    
    .seal-name{
      line-height: 40px;
      text-align: center;
      font-size: 14px;
      width:200px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }
  .no-data{
    font-size:14px;
    color:#999
  }
  
</style>
