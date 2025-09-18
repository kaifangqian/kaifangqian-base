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
  <div class="container-file">
    <div v-for="(item ,index) in fileList" :key="index" class="file-item">
      <div class="file-title" v-if="item"> 
        <ThumbUrl :fileUrl="'/resrun-paas/file/downloadFileStream/'+ item.id " v-if="checkImgType({name:item.realName})"/>
        <SvgIcon :name="loadFileType(item.realName&&item.realName.split('.')[1])" size="38" v-else></SvgIcon>
        <div>
          <a-tooltip>
                <template #title> {{item.fileName?item.fileName:item.realName}}</template>
                <p class="file-name">{{item.fileName?item.fileName:item.realName}}</p>
          </a-tooltip>
        </div>
      </div>
      <div class="file-action"> 
        <a href="javascript:;" @click="handleDownload(item)" class="download-icon"><Icon icon="ant-design:download-outlined" size="20" /></a>
      </div>
    </div>
 </div>
</template>

<script lang="ts">
import {ref,defineComponent} from "vue";
import { getToken } from '/@/utils/auth';
import { Icon } from '/@/components/Icon';
import { checkImgType } from './helper';
import PreviewModal from './PreviewModal.vue';
import { useModal } from '/@/components/Modal';
import { SvgIcon } from '/@/components/Icon';
import { loadFileType } from './helper';
import ThumbUrl from './ThumbUrl.vue';
import {getImgBase64} from '/@/api/sys/upload';


interface fileItem {
  [x: string]: any;
  fileName?:string;
  realName?:string;
  id?:string;
  fileType?:string
}
export default defineComponent({
  name:"FileList",
  components:{
    Icon,
    PreviewModal,
    SvgIcon,
    ThumbUrl
  },
  props:{
    fileList:{
      type:Array<fileItem>,
      default: ()=>{
        return <fileItem[]>[]
      }
    }
  },
  setup() {
    const data = ref('');

    const [registerPreviewModal, { openModal: openPreviewModal }] = useModal();

    function handleDownload(record){
 
      const token:string = getToken() as string;
      const req = new XMLHttpRequest();
      req.open('GET', location.origin + import.meta.env.VITE_GLOB_API_URL + '/file/downloadFileStream/'+ record.id, true);
      req.responseType = 'blob';
      req.setRequestHeader('X-Access-Token',token);
      req.onload = function() {
        const data = req.response;
        const blob = new Blob([data], { type: 'application/octet-stream' });
        let dom = document.createElement('a')
        let url = window.URL.createObjectURL(blob)
        dom.href = url
        dom.download = decodeURI(record.realName)
        dom.style.display = 'none'
        document.body.appendChild(dom)
        dom.click()
        dom&&dom?.parentNode?.removeChild(dom)
        window.URL.revokeObjectURL(url)
      };
      req.send();
    } 
    function handlePreview(row){
      openPreviewModal(true,{
        isUpdate:false,
        imgId:row.id
      })
    }
    async function loadImgBase64(id){
      let result = await getImgBase64({imgId:id});
         return result.image
    }
    return {
         data,
         handleDownload,
         checkImgType,
         registerPreviewModal,
         handlePreview,
         loadFileType,
         loadImgBase64
    }
  }
})
</script>

<style lang="less" scoped>
.container-file{
    cursor: pointer;
    padding: 5px 5px 5px 15px;
    // border-bottom: 1px dashed #e4d4e4;
    border-radius: 4px;
    display: flex;
    flex-wrap: wrap;

    .file-item{
      display: flex;
      align-items: center;
      border: 1px solid #d9d9d9;
      padding: 6px 15px;
      width: 400px;
      justify-content: space-between;
      margin-right: 20px;
      border-radius: 4px;
      margin-bottom: 10px;

      .file-action{
        display: flex;
        align-items: center;
      }

      .thumb{
        margin-right: 15px;
      }

      .resrun-svg-icon{
        margin-right: 15px;
      }

      .download-icon{
        display: none;
      }

      .preview-icon{
        display: none;
      }

      &:hover .download-icon{
        display: block;
      }

      &:hover .preview-icon{
        display: block;
      }
    }
    
}

.file-title{
  display: flex;
  align-items: center;

  P{
    margin-bottom: 5px;
    line-height: 1;
  }

  .file-size{
    color: #a09c9c;
  }
}

.file-item{
  display: flex;
  align-items: center;
  justify-content: space-between;
  // border-bottom: 1px solid #e4e4e4;
  padding-bottom: 2px;
  // background: #e4e4e4;
  
}

.file-name{
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
