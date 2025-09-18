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
  <div class="doc-preview-container">
    <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit" :destroyOnClose="true">
    <Loading :loading="loading" :absolute="false" tip="文件加载中..." />
      <div style="height: 600px;background-color:#fafafa;">
        <Scrollbar width="100%" height="100%" :native="true" :noresize="true" >
          <!-- <div class="document-list" :style="[
            'height: '+( CanvasZoom.height * docImages.length + docImages.length * 16)+'px;']" > -->
            <div class="document-list">
            <template v-for="item in docImages">
              <div class="document-page group"  v-if="item" :style="'width:'+item.imageWidth+'px;'">
                <img style="width: 100%;" v-lazy="baseUrl + '/file/downloadFileStream/' + item.annexId" />
              </div>
            </template>
          </div>
        </Scrollbar>
      </div>
    </BasicModal>
  </div>
</template>

<script lang="ts">
import { ref, defineComponent, computed, unref, toRefs, reactive,h  } from "vue";
import { BasicModal, useModalInner } from '/@/components/Modal';
import { getDocImgs, getTemplateDocImgs, getAnnexImgs } from '/@/api/contract/index';
import { Scrollbar } from '/@/components/Scrollbar';
import { getAppEnvConfig } from '/@/utils/env';
import { CanvasZoom } from '/@/views/businessLine/components/data/ControlData';
import { Loading } from '/@/components/Loading';
import { useMessage } from '/@/hooks/web/useMessage';
import { Button } from 'ant-design-vue';
import { Icon } from '/@/components/Icon';

export default defineComponent({
  name:"DocPreviewModal",
  components:{
    BasicModal,
    Scrollbar,
    Loading
  },
  setup() {
    const docId= ref('');
    const docName= ref('');
    const docImages:any = ref([]);
    const docType:any = ref('');

    const compState = reactive({
        absolute: false,
        loading: false,
        tip:''
      })

    const {VITE_GLOB_API_URL} = getAppEnvConfig();
    const baseUrl = ref(VITE_GLOB_API_URL);
    const { createMessage: msg, } = useMessage();


    const getTitle = computed(() => (unref(docName)));
    const [registerModal, { setModalProps }] = useModalInner(async (data) => {
        setModalProps({ 
          confirmLoading: false,
          width:1200,
          height:600,
          centered:false,
          cancelText:'关闭',
          showOkBtn:false,
          canFullscreen:false
        });
        docId.value = data.record?.docId;
        docName.value = data.record?.docName;
        docType.value = data.record?.docType;
        getDocImages()
        
      });
      function handleClose(){
        msg.destroy()
      }
 
      async function getDocImages(){
        try{
          compState.loading = true;
          docImages.value = [];
          let result  = await  getAnnexImgs({annexId:docId.value})
          if(result.length){
            //item.imageWidth
            result.forEach(item=>{
              var newWidth = parseInt(item.imageWidth) * 1.74;
              item.imageWidth = newWidth>1140?1140:newWidth;
            })
            docImages.value = result.sort((a, b) => a.page - b.page);
            compState.loading = false;
            handleClose();
          }else{
            compState.loading = false;
            reloadImg()
          }
        }catch (error){
          compState.loading = false;
          reloadImg()
        }
    }
    function reloadImg(){
        const content = h('span', [
          h('span', {default: ()=> '文件加载失败？'}),
          h(Button, { type: 'link', onClick: getDocImages }, {default: ()=> '重新加载'}),
          h(Icon,   { icon: 'ant-design:close-outlined',color:'#666',style:{cursor: 'pointer'}, onClick: handleClose }),
        ]);
        msg.warning({
          content,
          duration: 20,
        })
    }

    function handleSubmit(){

    }

    return {
      registerModal,
      docImages,
      getTitle,
      handleSubmit,
      CanvasZoom,
      baseUrl,
      ...toRefs(compState), 
    }
  }
})
</script>

<style lang="less" scoped>
.document-list{
  position: relative;
  // padding: 20px 0;
}
.document-page{
    margin:0 auto;
    padding: 8px 0;
    img{
      width:100%;
      height:100%;
    }
  }
</style>
