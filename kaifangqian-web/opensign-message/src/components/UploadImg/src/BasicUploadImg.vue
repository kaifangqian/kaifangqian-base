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
  <div class="upload-container">
    <Upload 
        :accept="getStringAccept"
        :action="url"
        :data="{
         dataCategory:annexType,
         type:annexType
        }"
        :show-upload-list="false"
        :headers="headers.token"
        class="upload-modal-toolbar__btn"
        @change="handleFileChange"
    >
      <div  v-if="!base64Img"  class="upload-option-area" :style="{ 'background': defaultBackgroundImg?'url(' + getImgUrl(defaultBackgroundImg) + ') no-repeat center center':'#fff', 'background-size': '100% 100%',...uploadStyle}">
          <span v-if="uploadText">{{uploadText}}</span>
      </div>
      <div v-if="base64Img" class="upload-option-area" style="{......uploadStyle}">
        <img :src="base64Img" alt="" />
      </div>
    </Upload>
  
  </div>
</template>

<script lang="ts">
import {reactive,toRefs, unref, ref, watch} from "vue";
import { useUploadType } from './useUpload';
import { basicProps } from './props';
import { Upload } from 'ant-design-vue';
import { useMessage } from '/@/hooks/web/useMessage';
import { buildUUID } from '/@/utils/uuid';
import { checkImgType, getBase64WithFile } from './helper';
import { FileItem } from './typing';
import { getToken } from '/@/utils/auth';
import {getImgBase64} from '/@/api/sys/upload';

export default{
  name:"BasicUploadImg",
  components:{
    Upload
  },
  props: {
      ...basicProps,
      previewFileList: {
        type: Array as PropType<string[]>,
        default: () => [],
      },
      annexType:{
        type:String,
        default:''
      },
      defaultImgId:{
        type:String,
        default:''
      },
      url:{
        type:String,
        dfault:'resrun-paas/file'
      }
    },
   
  setup(props,{ emit}) {
    if(props.defaultImgId){
      fetchBase64( props.defaultImgId );
    }
    watch(
      ()=> props.defaultImgId,
      ()=> {
        console.log('文件详情1id', props.defaultImgId)
        fetchBase64( props.defaultImgId );
      }
    )
    const data = reactive({

    })
    const headers = reactive({token:{
        'X-Access-Token':getToken() as string,
        'resrun-app-code':'app_00005',
      }});
    const base64Img = ref('')
    const { createMessage } = useMessage();
    const { accept, helpText, maxNumber, maxSize, defaultBackgroundImg, uploadText } = toRefs(props);
    
    const fileListRef = ref<FileItem[]>([]);
    const refData = toRefs(data)
    const { getStringAccept, getHelpText } = useUploadType({
          acceptRef: accept,
          helpTextRef: helpText,
          maxNumberRef: maxNumber,
          maxSizeRef: maxSize,
        });
      // 引入图片
      function getImgUrl(url){
        return new URL(`../../../assets/images/${url}`,import.meta.url).href;
      }
      // 上传前校验
      function beforeUpload(file: File) {
        const { size, name } = file;
        const { maxSize } = props;
        // 设置最大值，则判断
        if (maxSize && file.size / 1024 / 1024 >= maxSize) {
          createMessage.error(`${'只能上传不超过'+ maxSize +'M大小的文件'}`);
          return false;
        }

        const commonItem = {
          uuid: buildUUID(),
          file,
          size,
          name,
          percent: 0,
          type: name.split('.').pop(),
        };
        // 生成图片缩略图
        if (checkImgType(file)) {
          // beforeUpload，如果异步会调用自带上传方法
          // file.thumbUrl = await getBase64(file);
          getBase64WithFile(file).then(({ result: thumbUrl }) => {
            fileListRef.value = [
              ...unref(fileListRef),
              {
                thumbUrl,
                ...commonItem,
              },
            ];
          });
        } else {
          fileListRef.value = [...unref(fileListRef), commonItem];
        }
        return false;
      }

   async  function handleFileChange({file}){
      if(file.response&&file.response.code==200){
        emit('success',file.response.result);
        // getBase64WithFile(file).then(({ result: thumbUrl }) =>{
        //   console.log(thumbUrl,'base64');
        //   base64Img.value = thumbUrl;
        // })
      }
    }
    async function fetchBase64(imgId){
      if(!imgId) return;
        let result = await getImgBase64({imgId})
        if(result){
          base64Img.value = result.image;
        }
    }

    return {
      refData,
      getStringAccept,
      getHelpText,
      defaultBackgroundImg,
      beforeUpload,
      getImgUrl,
      handleFileChange,
      uploadText,
      headers,
      base64Img
    }
  }
}
</script>

<style lang="less" scoped>
.upload-option-area{
  width:360px;
  height:204px;
  border: 1px dashed #e4e4e4;
  img{
    width:100%;
    height:100%;
  }
}
</style>
