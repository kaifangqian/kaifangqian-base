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
  <BasicModal
    v-bind="$attrs"
    @register="register"
    title="头像上传"
    width="800px"
    :canFullscreen="false"
    @ok="handleOk"
    okText="确定"
  >
    <div :class="prefixCls">
    <Upload :fileList="[]" accept="image/*" :beforeUpload="handleBeforeUpload">
      <!-- <Tooltip title="选择图片" placement="bottom">
        <a-button size="small" type="primary" ><Icon icon="ant-design:upload-outlined" /></a-button>
      </Tooltip> -->
      <div class="upload-block">
        <Icon icon="ant-design:upload-outlined" size="16"/>
        <span>上传</span>
        <p>支持 .jpg, .png 格式</p>
        <p>文件大小不超过3MB</p>
      </div>
    </Upload>
    

    <div :class="`${prefixCls}-preview`">
        <img :src="src || srcValue" v-if="src || srcValue" alt="预览" />
    </div>



      <!-- <div :class="`${prefixCls}-left`">
        <div :class="`${prefixCls}-cropper`">
          <CropperImage
            v-if="src"
            :src="src"
            height="300px"
            :circled="circled"
            @cropend="handleCropend"
            @ready="handleReady"
          />
        </div>

        <div :class="`${prefixCls}-toolbar`">
          <Upload :fileList="[]" accept="image/*" :beforeUpload="handleBeforeUpload">
            <Tooltip title="选择图片" placement="bottom">
              <a-button size="small" type="primary" ><Icon icon="ant-design:upload-outlined" /></a-button>
            </Tooltip>
          </Upload>
          <Space>
            <Tooltip title="重置" placement="bottom">
              <a-button
                type="primary"
                preIcon="ant-design:reload-outlined"
                size="small"
                :disabled="!src"
                @click="handlerToolbar('reset')"
              />
            </Tooltip>
            <Tooltip title="逆时针旋转" placement="bottom">
              <a-button
                type="primary"
                preIcon="ant-design:rotate-left-outlined"
                size="small"
                :disabled="!src"
                @click="handlerToolbar('rotate', -45)"
              />
            </Tooltip>
            <Tooltip :title="t('component.cropper.btn_rotate_right')" placement="bottom">
              <a-button
                type="primary"
                preIcon="ant-design:rotate-right-outlined"
                size="small"
                :disabled="!src"
                @click="handlerToolbar('rotate', 45)"
              />
            </Tooltip>
            <Tooltip :title="t('component.cropper.btn_scale_x')" placement="bottom">
              <a-button
                type="primary"
                preIcon="vaadin:arrows-long-h"
                size="small"
                :disabled="!src"
                @click="handlerToolbar('scaleX')"
              />
            </Tooltip>
            <Tooltip :title="t('component.cropper.btn_scale_y')" placement="bottom">
              <a-button
                type="primary"
                preIcon="vaadin:arrows-long-v"
                size="small"
                :disabled="!src"
                @click="handlerToolbar('scaleY')"
              />
            </Tooltip>
            <Tooltip :title="t('component.cropper.btn_zoom_in')" placement="bottom">
              <a-button
                type="primary"
                preIcon="ant-design:zoom-in-outlined"
                size="small"
                :disabled="!src"
                @click="handlerToolbar('zoom', 0.1)"
              />
            </Tooltip>
            <Tooltip :title="t('component.cropper.btn_zoom_out')" placement="bottom">
              <a-button
                type="primary"
                preIcon="ant-design:zoom-out-outlined"
                size="small"
                :disabled="!src"
                @click="handlerToolbar('zoom', -0.1)"
              />
            </Tooltip>
          </Space>
        </div>
      </div>
      <div :class="`${prefixCls}-right`">
        <div :class="`${prefixCls}-preview`">
          <img :src="previewSource" v-if="previewSource" alt="预览" />
        </div>
        <template v-if="previewSource">
          <div :class="`${prefixCls}-group`">
            <Avatar :src="previewSource" size="large" />
            <Avatar :src="previewSource" :size="48" />
            <Avatar :src="previewSource" :size="64" />
            <Avatar :src="previewSource" :size="80" />
          </div>
        </template>
      </div> -->
    </div>
  </BasicModal>
</template>
<script lang="ts">
  import type { CropendResult, Cropper } from './typing';

  import { defineComponent, ref } from 'vue';
  import CropperImage from './Cropper.vue';
  import Icon from '/@/components/Icon';
  import { Space, Upload, Avatar, Tooltip } from 'ant-design-vue';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { dataURLtoBlob } from '/@/utils/file/base64Conver';
  import { isFunction } from '/@/utils/is';
  import { useI18n } from '/@/hooks/web/useI18n';

  type apiFunParams = { file: Blob; name: string; filename: string };

  const props = {
    circled: { type: Boolean, default: true },
    srcValue: { type: String, default: '' },
    uploadApi: {
      type: Function as PropType<(params: apiFunParams) => Promise<any>>,
    },
  };

  export default defineComponent({
    name: 'CropperModal',
    components: { BasicModal, Space, CropperImage, Upload, Avatar, Tooltip, Icon },
    props,
    emits: ['uploadSuccess', 'register'],
    setup(props, { emit }) {
      let filename = '';
      const src = ref('');
      console.log(props,'测试属性--')
      const previewSource = ref('');
      const cropper = ref<Cropper>();
      let scaleX = 1;
      let scaleY = 1;

      const { prefixCls } = useDesign('cropper-am');
      const [register, { closeModal, setModalProps }] = useModalInner();
      const { t } = useI18n();

      // Block upload
      function handleBeforeUpload(file: File) {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        src.value = '';
        previewSource.value = '';
        reader.onload = function (e) {
          src.value = (e.target?.result as string) ?? '';
          filename = file.name;
        };
        return false;
      }

      function handleCropend({ imgBase64 }: CropendResult) {
        previewSource.value = imgBase64;
      }

      function handleReady(cropperInstance: Cropper) {
        cropper.value = cropperInstance;
      }

      function handlerToolbar(event: string, arg?: number) {
        if (event === 'scaleX') {
          scaleX = arg = scaleX === -1 ? 1 : -1;
        }
        if (event === 'scaleY') {
          scaleY = arg = scaleY === -1 ? 1 : -1;
        }
        cropper?.value?.[event]?.(arg);
      }

      async function handleOk() {
        const uploadApi = props.uploadApi;
        if (uploadApi && isFunction(uploadApi)) {
          // const blob = dataURLtoBlob(previewSource.value);
          const blob = dataURLtoBlob(src.value);
          try {
            setModalProps({ confirmLoading: true });
            const result = await uploadApi({ name: 'file', file: blob, filename });
            // emit('uploadSuccess', { source: previewSource.value, data: result.data });
            emit('uploadSuccess', { source: src.value, data: result });
            closeModal();
          } finally {
            setModalProps({ confirmLoading: false });
          }
        }
      }

      return {
        t,
        prefixCls,
        src,
        register,
        previewSource,
        handleBeforeUpload,
        handleCropend,
        handleReady,
        handlerToolbar,
        handleOk,
      };
    },
  });
</script>

<style lang="less">
  @prefix-cls: ~'@{namespace}-cropper-am';

  .@{prefix-cls} {
    display: flex;
    align-items: center;

    &-left,
    &-right {
      height: 340px;
    }

    &-left {
      width: 55%;
    }

    &-right {
      width: 45%;
    }

    &-cropper {
      height: 300px;
      background: #eee;
      // background-image: linear-gradient(
      //     45deg,
      //     rgb(0 0 0 / 25%) 25%,
      //     transparent 0,
      //     transparent 75%,
      //     rgb(0 0 0 / 25%) 0
      //   ),
      //   linear-gradient(
      //     45deg,
      //     rgb(0 0 0 / 25%) 25%,
      //     transparent 0,
      //     transparent 75%,
      //     rgb(0 0 0 / 25%) 0
      //   );
      // background-position: 0 0, 12px 12px;
      // background-size: 24px 24px;
    }

    &-toolbar {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-top: 10px;
    }

    &-preview {
      width: 220px;
      height: 220px;
      margin: 0 auto;
      overflow: hidden;
      border: 1px solid @border-color-base;
      border-radius: 50%;

      img {
        width: 100%;
        height: 100%;
      }
    }

    &-group {
      display: flex;
      padding-top: 8px;
      margin-top: 8px;
      border-top: 1px solid @border-color-base;
      justify-content: space-around;
      align-items: center;
    }
    .upload-block{
        padding: 50px;
        border: 1px dashed #8b8989;
    }
  }
</style>
