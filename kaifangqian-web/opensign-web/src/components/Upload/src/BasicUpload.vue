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
    <Loading :loading="isUploadingRef" :absolute="false" />
    <Space>
      <Upload
        :accept="getStringAccept"
        :multiple="multiple"
        :before-upload="beforeUpload"
        :show-upload-list="false"
        class="upload-modal-toolbar__btn"
      >
        <div class="upload-file-btn">
          <Icon icon="ant-design:upload-outlined" />选择文件
        </div>
        <p class="upload-tip">{{ uploadTip }}</p>
      </Upload>
    </Space>

    <UploadPreviewModal
      :value="fileList"
      @register="registerPreviewModal"
      @list-change="handlePreviewChange"
      @delete="handlePreviewDelete"
    />
      <a-popover  placement="top" title="文件列表">
        <template #content>
            <FileList :dataSource="fileListRef" :columns="columns" :actionColumn="actionColumn" class="file_list_area" @remove="handleRemove"/>
        </template>
        <Icon icon="ant-design:file-done-outlined"  color="#18be21" v-if="popoverMode&&fileListRef.length"></Icon>
        <!-- <span  v-if="popoverMode&&fileListRef.length">已上传，点击查看</span> -->
      </a-popover >
      <!-- <span>{{ popoverMode&&fileListRef.length + '--' + popoverMode +'----'+ fileListRef.length}}</span> -->
      <FileList v-if="!popoverMode" :dataSource="fileListRef" :columns="columns" :actionColumn="actionColumn" class="file_list_area" @remove="handleRemove"/>
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref, watch, unref, computed, toRefs } from 'vue';
  import { Icon } from '/@/components/Icon';
  import { Tooltip, Space, Upload, Alert } from 'ant-design-vue';
  import { useModal } from '/@/components/Modal';
  import { FileItem, UploadResultStatus } from './typing';
  import { uploadContainerProps,basicProps } from './props';
  import { createTableColumns, createActionColumn } from './data';
  import { omit } from 'lodash-es';
  import { useUploadType } from './useUpload';
  import { isArray } from '/@/utils/is';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { checkImgType, getBase64WithFile } from './helper';
  import { buildUUID } from '/@/utils/uuid';
  import { isFunction } from '/@/utils/is';
  import { warn } from '/@/utils/log';
  import FileList from './FileList.vue';
  import UploadPreviewModal from './UploadPreviewModal.vue';
  import { useAppStoreWithOut } from '/@/store/modules/app';
  import { Loading } from '/@/components/Loading';
  export default defineComponent({
    name: 'BasicUpload',
    components: { Space, UploadPreviewModal, Icon, Tooltip, Upload, Alert, FileList, Loading },
    props: {
      ...uploadContainerProps,
      ...basicProps,
      previewFileList: {
        type: Array as PropType<string[]>,
        default: () => [],
      },
      uploadTip:{
        type:String,
        default:''
      },
      dataSource: {
        type: Array as PropType<string[]>,
        default: () => [],
      },
      popoverMode:{
        type:Boolean,
        default:false
      }
    },
    emits: ['change', 'delete', 'preview-delete', 'update:value','update'],

    setup(props, { emit, attrs }) {
      //预览modal
      const [registerPreviewModal, { openModal: openPreviewModal }] = useModal();

      const { accept, helpText, maxNumber, maxSize } = toRefs(props);

      const { getStringAccept, getHelpText } = useUploadType({
        acceptRef: accept,
        helpTextRef: helpText,
        maxNumberRef: maxNumber,
        maxSizeRef: maxSize,
      });

      
      const { createMessage } = useMessage();

      const appStore = useAppStoreWithOut();

      const fileListRef = ref<FileItem[]>([]);

      const isUploadingRef = ref(false);

      const fileList = ref<string[]>([]);

      const showPreview = computed(() => {
        const { emptyHidePreview } = props;
        if (!emptyHidePreview) return true;
        return emptyHidePreview ? fileList.value.length > 0 : true;
      });

 

      const bindValue = computed(() => {
        const value = { ...attrs, ...props };
        return omit(value, 'onChange');
      });

      watch(
        () => props.dataSource,
        (value = []) => {
          if(fileListRef.value.length){
            return
          }
          fileListRef.value = isArray(value) ? value : [];

        },
        { immediate: true },
      );
      watch(
        () => props.value,
        (value = []) => {
          fileList.value = isArray(value) ? value : [];
        },
        { immediate: true },
      );

      // // 上传modal保存操作
      function handleChange(urls: string[]) {
        fileList.value = [...unref(fileList), ...(urls || [])];
        emit('update:value', fileList.value);
        emit('change', fileList.value);
      }
      // 提交时获取文件列表
      function getFiles(){
        console.log(fileListRef.value,'文件列表')
        return fileListRef.value
      }

      // 预览modal保存操作
      function handlePreviewChange(urls: string[]) {
        fileList.value = [...(urls || [])];
        emit('update:value', fileList.value);
        emit('change', fileList.value);
      }

      function handleDelete(record: Recordable) {
        emit('update', fileListRef.value);
      }

      function handlePreviewDelete(url: string) {
        emit('preview-delete', url);
      }

      // 上传前校验
      function beforeUpload(file: File) {
        const { size, name } = file;
        const { maxNumber, accept } = props;
        const index = name.lastIndexOf("\.");
        const fileType = name.substring(index+1);
        const fileSize = file.size / 1024 / 1024;
        console.log(fileSize,fileType,accept,accept.includes('.'+ fileType),'上传文件信息 ')

        if(accept.length && !accept.includes('.'+ fileType)){
          createMessage.error(`${'上传文件格式不正确'}`);
          return false;
        }
        if(accept.length){
          if(fileType=='jpg' && fileSize>=10){
            createMessage.error(`${'jpg文件最大为10M'}`);
            return false;
          }
          if(fileType=='pdf' && fileSize>=30){
            createMessage.error(`${'pdf文件最大为30M'}`);
            return false;
          }
          if(fileType=='mp3' && fileSize>=100){
            createMessage.error(`${'mp3文件最大为100M'}`);
            return false;
          }
          if(fileType=='mp4' && fileSize>=200){
            createMessage.error(`${'mp4文件最大为200M'}`);
            return false;
          }
          if(fileType=='zip' && fileSize>=500){
            createMessage.error(`${'zip文件最大为500M'}`);
            return false;
          }
        }
        // // 设置最大值，则判断
        // if (maxSize && file.size / 1024 / 1024 >= maxSize) {
        //   createMessage.error(`${'只能上传'+ maxSize+'M大小的文件'}`);
        //   return false;
        // }

        if (fileListRef.value.length  >= maxNumber) {
          createMessage.error(`${'只能上传'+ maxNumber+'个文件'}`);
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
             handleStartUpload()
          });    
        //检验完成开始上传
        } else {
          fileListRef.value = [...unref(fileListRef), commonItem];
         //检验完成开始上传
          handleStartUpload()
        }
        return false;
      }
      

      async function handleStartUpload() {
        
        try {
          // appStore.setPageLoadingAction(true);
          isUploadingRef.value = true;
          // 只上传不是成功状态的
          const uploadFileList = fileListRef.value.filter((item) => item.status !== UploadResultStatus.SUCCESS) || [];
          const data = await Promise.all(
            uploadFileList.map((item) => {
              console.log(item,'文件状态')
              return uploadApiByItem(item);
            }),
          );
          isUploadingRef.value = false;
          emit('update',fileListRef.value);
          // appStore.setPageLoadingAction(false);
       
          // 生产环境:抛出错误
          const errorList = data.filter((item: any) => !item.success);
          if (errorList.length > 0) throw errorList;
        } catch (e) {
          isUploadingRef.value = false;
          throw e;
        }
      }

      async function uploadApiByItem(item: FileItem) {
        const { api } = props;
        if (!api || !isFunction(api)) {
          return warn('upload api must exist and be a function');
        }
        try {
          item.status = UploadResultStatus.UPLOADING;
          const { data } = await props.api?.(
            {
              data: {
                ...(props.uploadParams || {}),
              },
              file: item.file,
              name: props.name,
              filename: props.filename,
            },
            function onUploadProgress(progressEvent: ProgressEvent) {
              const complete = ((progressEvent.loaded / progressEvent.total) * 100) | 0;
              item.percent = complete;
            },
          );
          item.status = UploadResultStatus.SUCCESS;
          item.responseData = data;
          return {
            success: true,
            error: null,
            result:data
          };
        } catch (e) {
          console.log(e);
          item.status = UploadResultStatus.ERROR;
          return {
            success: false,
            error: e,
          };
        }
      }
      function handleRemove(record: FileItem) {
        const index = fileListRef.value.findIndex((item) => item.uuid === record.uuid);
        index !== -1 && fileListRef.value.splice(index, 1);
        handleDelete(record)
      }


      return {
        columns: createTableColumns() as any[],
        actionColumn: createActionColumn(handleRemove) as any,
        beforeUpload,
        getHelpText,
        getStringAccept,
        handleChange,
        handlePreviewChange,
        registerPreviewModal,
        openPreviewModal,
        fileList,
        showPreview,
        bindValue,
        fileListRef,
        handleDelete,
        handlePreviewDelete,
        getFiles,
        handleRemove,isUploadingRef
      };
    },
  });
</script>
<style lang="less" scoped>
.upload-file-btn{
  padding: 7px 5px;
  border: 1px dashed #d9d9d9;
  border-radius: 4px;
  text-align: center;
}
.upload-tip{
  font-size: 12px;
  color: #999;
  margin-top:5px;
}
</style>
