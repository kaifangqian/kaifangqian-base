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
  <Loading :loading="spinning" :absolute="false" />
  <Loading :loading="uploadLoading" :absolute="false" />
  <div class="page-content">
    <div class="custom-card">
      <div class="card-title">
        <span class="title">基本信息</span>
      </div>
      <div class="card-body">
        <a-form
          :model="sealFrom"
          name="basic"
          :labelCol="{ style: 'width: 100px' }"
          aut="off"
          ref="selaFromRef"
        >
          <a-row style="width: 100%; padding-top: 10px">
            <a-col :span="6">
              <a-form-item label="印章名称" name="sealName">
                <!-- <a-input v-model:value="sealFrom.sealName" :disabled="formColumn.sealName === FormAuthStatus.COLUMN_READ || formDisabled" size="middle" placeholder="请输入印章名" class="input-width" /> -->
                <span>{{ sealFrom.sealName }}</span>
              </a-form-item>
            </a-col>
            <a-col :span="6">
              <a-form-item label="印章类型" name="sealType">
                <!-- <a-select v-model:value="sealFrom.sealType" :disabled="formColumn.sealType === FormAuthStatus.COLUMN_READ || formDisabled" class="input-width"  size="middle" 
                    placeholder="请选择印章类型" @change="sealTypeChange" :options="sealType">
                  </a-select> -->
                <span>{{ getSealType(sealFrom.sealType).label }}</span>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row style="width: 100%; padding-top: 0px">
            <a-col :span="6">
              <a-form-item label="所属组织" name="sealOrg">
                <span>{{ sealFrom.entpName }}</span>
                <!-- <a-input v-model:value="sealFrom.entpName" disabled size="middle" class="input-width" /> -->
              </a-form-item>
            </a-col>
          </a-row>

          <!-- <a-row>
            <a-col :span="12" style="width: 100%;padding-top: 00px;">
              <a-form-item label="用途说明" name="description">
                <span>{{sealFrom.description}}</span>
              </a-form-item>
            </a-col>
          </a-row> -->
        </a-form>
        <div class="line"></div>
      </div>
    </div>

    <div class="custom-card">
      <div class="card-title">
        <span class="title">印章图案</span>
      </div>
      <div class="card-body">
        <div class="switch-button">
          <a-radio-group v-model:value="sealFrom.createType" @change="createTypeChange">
            <a-radio-button :value="1">模板制作</a-radio-button>
            <a-radio-button :value="2">印模生成</a-radio-button>
          </a-radio-group>
        </div>
        <div class="seal-make-layout">
          <div class="template-make" v-if="sealFrom.createType == 1 && !formDisabled">
            <div class="temlate-item temlate-style">
              <div class="title" style="margin-top: 10px">
                <span>选择印章样式</span>
              </div>
              <div class="seal-style">
                <ul>
                  <li :class="sealFrom.sealStyle == 1 ? 'active' : ''" @click="sealStyleChange(1)">
                    <div class="seal-img">
                      <img :src="templateGz" />
                    </div>
                    <div class="seal-name">圆章</div>
                  </li>
                  <li :class="sealFrom.sealStyle == 3 ? 'active' : ''" @click="sealStyleChange(3)">
                    <div class="seal-img">
                      <img :src="templateGzNone" />
                    </div>
                    <div class="seal-name">圆章(不带星)</div>
                  </li>
                  <li :class="sealFrom.sealStyle == 2 ? 'active' : ''" @click="sealStyleChange(2)">
                    <div class="seal-img">
                      <img :src="templateFinance" />
                    </div>
                    <div class="seal-name">椭圆章1</div>
                  </li>
                  <li :class="sealFrom.sealStyle == 4 ? 'active' : ''" @click="sealStyleChange(4)">
                    <div class="seal-img">
                      <img :src="templateFinanceNone" />
                    </div>
                    <div class="seal-name">椭圆章2</div>
                  </li>
                </ul>
              </div>
            </div>
            <div class="temlate-item">
              <div class="title" style="margin-top: 20px">
                <span>印章文字设置</span>
              </div>
              <div class="seal-font-seting" style="padding-top: 25px; max-width: 70%">
                <a-form
                  :model="sealFrom"
                  name="basic"
                  autocomplete="off"
                  layout="horizontal"
                  ref="sealStyleRef"
                >
                  <a-form-item label="环绕文字" :labelCol="{ span: 4, offset: 0 }">
                    <a-input
                      v-model:value="sealFrom.entpName"
                      :disabled="true"
                      class="input-width"
                    />
                  </a-form-item>
                  <a-form-item
                    name="middleText"
                    label="横排文字"
                    :labelCol="{ span: 4, offset: 0 }"
                    :rules="[{ validator: checkMiddleText, trigger: ['change', 'blur'] }]"
                  >
                    <a-input
                      v-model:value="sealFrom.middleText"
                      class="input-width"
                      placeholder="请输入横排文字"
                      @change="sealPreviewInfo"
                    />
                  </a-form-item>
                  <a-form-item
                    label="横排序列号"
                    :labelCol="{ span: 4, offset: 0 }"
                    v-if="sealFrom.sealStyle == 2"
                  >
                    <a-input
                      :value="tenantInfo.organizationNo"
                      :disabled="true"
                      class="input-width"
                    />
                  </a-form-item>
                  <a-form-item
                    name="bottomText"
                    label="下弦文"
                    :labelCol="{ span: 4, offset: 0 }"
                    :rules="[{ validator: checkBottomText, trigger: ['change', 'blur'] }]"
                  >
                    <a-input
                      v-model:value="sealFrom.bottomText"
                      class="input-width"
                      placeholder="请输入下弦文"
                      @change="sealPreviewInfo"
                    />
                  </a-form-item>
                </a-form>
              </div>
            </div>
          </div>
          <div class="seal-upload" v-if="sealFrom.createType == 2 && !formDisabled">
            <div class="upload-instructions" v-if="fileList.length == 0">
              <!-- <h3>上传章面</h3> -->
              <div class="upload-steps">
                <!-- 第一步 -->
                <div>
                  <p style="font-size: large"><strong>第 1 步：在空白白纸上盖上印章</strong></p>
                  <p>请盖印清晰完整的章面</p>
                  <div class="upload-step step-one">
                    <img src="/@/assets/images/stamp-on-paper.png" alt="Step 1" />
                  </div>
                </div>

                <!-- 第二步 -->
                <div>
                  <p style="font-size: large"
                    ><strong>第 2 步：拍摄或扫描刚才盖好的章，上传到这里</strong></p
                  >
                  <p>💡 温馨提示：上传完成后请及时销毁盖有印章的白纸，避免被不当使用</p>
                  <div class="upload-step step-two">
                    <div class="upload-wrapper">
                      <a-upload
                        v-model:file-list="fileList"
                        name="file"
                        :show-upload-list="false"
                        :headers="uploadHeaders"
                        accept="image/png,image/jpg,image/jpeg"
                        @change="handleChange"
                        :before-upload="beforeUpload"
                      >
                        <div class="upload-area">
                          <Icon
                            icon="ant-design:cloud-upload-outlined"
                            style="font-size: 60px; color: #1890ff"
                          />
                          <p>上传印章</p>
                          <p>支持 jpg/jpeg/png 文件，不超过2M</p>
                        </div>
                      </a-upload>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="upload-img" v-if="fileList.length > 0">
              <img
                :src="uploadSeal"
                ref="sealRef"
                @load="imgeLoad"
                style="height: 100%; display: block; margin: 0 auto"
              />
            </div>

            <div class="SealActions" v-if="fileList.length > 0">
              <a-row>
                <a-col :span="4" class="center">
                  <Icon icon="ant-design:undo-outlined" @click="sealActions(-1)" class="pointer" />
                </a-col>
                <a-col :span="4" class="center">
                  <Icon icon="ant-design:redo-outlined" @click="sealActions(1)" class="pointer" />
                </a-col>
                <a-col :span="4" class="center">
                  <Icon icon="ant-design:minus-outlined" @click="sealActions(-2)" class="pointer" />
                </a-col>
                <a-col :span="4" class="center">
                  <Icon icon="ant-design:plus-outlined" @click="sealActions(2)" class="pointer" />
                </a-col>
                <a-col :span="5">
                  <a-upload
                    v-model:file-list="fileList"
                    name="file"
                    actions="/"
                    :show-upload-list="false"
                    :headers="uploadHeaders"
                    :before-upload="beforeUpload"
                    accept="image/png,image/jpg,image/jpeg"
                    @change="handleChange2"
                  >
                    <a-button type="link">重新上传</a-button>
                  </a-upload>
                </a-col>
              </a-row>
              <a-row>
                <a-col :span="8">
                  <h4 style="line-height: 32px">旋转角度：</h4>
                </a-col>
                <a-col :span="16">
                  <a-slider
                    @afterChange="sealRotateChange"
                    v-model:value="sealOptions.sealRotate"
                    :min="-180"
                    :max="180"
                  />
                </a-col>
              </a-row>
              <a-row>
                <a-col :span="8">
                  <h4 style="line-height: 32px">背景透明度：</h4>
                </a-col>
                <a-col :span="16">
                  <a-slider
                    @afterChange="colorRangeChange"
                    v-model:value="sealOptions.sealBackground"
                    :min="0"
                    :max="100"
                  />
                </a-col>
              </a-row>
              <!-- <a-input-number v-model:value="sealOptions.sealRotate" :min="-360" :max="360" /> -->
            </div>
            <!-- <div class="upload-img">
             <img :src="zyld" ref="sealRef"/>
           </div> -->
          </div>
          <div class="seal-preview" v-if="sealPreview">
            <div class="title">签章预览</div>
            <div class="seal-preview-img">
              <img :src="sealPreview" style="width: 100%; height: 100%" />
            </div>
          </div>
        </div>
        <div class="line" style="padding-bottom: 20px"></div>
      </div>
      <div> </div>
    </div>
    <div style="padding: 10px 0">
      <a-space :size="10">
        <!-- <a-button type="primary" v-if="sealFrom.createType == 1" @click="sealPreviewInfo">预览</a-button> -->
        <a-button type="primary" @click="saveSeal">保存</a-button>
        <a-button @click="backPage">取消</a-button>
      </a-space>
    </div>
  </div>
</template>

<script lang="ts">
  import { defineComponent, ref, unref, computed, reactive, onMounted } from 'vue';
  import { message, Upload } from 'ant-design-vue';
  import type { UserInfo } from '/#/store';
  import Icon from '/@/components/Icon';
  /* import templateGz from "/@/assets/images/template-gz.png"; */
  import { templateGz, templateFinance, templateGzNone, templateFinanceNone } from './image';
  import '/@/assets/style/custom-card-module.css';

  import { useUserStore } from '/@/store/modules/user';

  import { useRouter } from 'vue-router';
  import { getImgBase64 } from '/@/api/sys/upload';

  import 'cropperjs/dist/cropper.css';
  import Cropper from 'cropperjs';
  import {
    makeSeal,
    generateSeal,
    templateGenerateSeal,
    editSeal,
    getSealDetailes,
    sealChange,
  } from './api';
  import { sealType, getSealType, checkMiddleText, checkBottomText } from '../seal/data';
  // import {getFormColumn} from "/@/api/form"
  // import {FormAuthStatus} from "/@/utils/FormAuthStatus"
  import { Loading } from '/@/components/Loading';
  import type { UploadProps } from 'ant-design-vue';
  function getBase64(file: any) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => resolve(reader.result);
      reader.onerror = (error) => reject(error);
    });
  }

  export default defineComponent({
    name: 'SealChange',
    components: {
      Icon,
      Loading,
    },
    props: {
      formKey: {
        type: String,
      },
      formId: {
        type: String,
      },
    },
    setup(props) {
      let router = useRouter();
      const spinning = ref(false);
      const uploadLoading = ref(false);
      const sealStyleRef: any = ref({});
      const sealFrom = ref({
        sealType: null,
        sealName: '',
        createType: 1,
        sealStyle: 1,
        entpName: '',
        middleText: '',
        annexId: '',
        sealBackground: 0,
        description: '',
        sealId: '',
        adminId: '',
        adminName: '',
        sealBase64: '',
        bottomText: '',
      });
      const sealOptions = ref({
        sealRotate: 0,
        sealBackground: 50,
        imageWidth: 400,
        imageHeight: 400,
      });
      const uploadHeaders = reactive({
        'X-Access-Token': '',
        'resrun-app-code': '',
      });
      const editType = ref<number>(0);
      const userStore = useUserStore();
      const loginUser = userStore.getUserInfo as UserInfo;
      const tenantInfo: any = ref({ ...userStore.getTenantInfo });
      sealFrom.value.entpName = loginUser.loginTenantName;
      const firstUpload = ref(true);
      const sealRef = ref(null);
      const selaFromRef = ref({});
      const fileList = ref([]);
      const sealPreview = ref('');
      const sealCropper = ref<Cropper>();
      const uploadSeal = ref<string>('');
      const sealId = ref(null);
      const formDisabled = ref(false);
      const marks = ref<Record<number, any>>({
        '-180': '-180°',
        '-90': '-90°',
        0: '0°',
        90: '90°',
        180: '180°',
      });
      //120 -220
      /* const marks = ref<Record<number, any>>({
        0: '0°',
        90: '90°',
        255: '180°',
      }); */
      async function sealPreviewInfo() {
        spinning.value = true;
        if (sealFrom.value.createType == 1) {
          const response = await generateSeal({
            middleText: sealFrom.value.middleText,
            entSealShapeType: sealFrom.value.sealStyle,
            bottomText: sealFrom.value.bottomText,
          });
          sealPreview.value = 'data:image/png;base64,' + response;
          sealFrom.value.sealBase64 = response;
          // await annexEcho(response);
        }
        spinning.value = false;
      }
      async function annexEcho(annexId) {
        const result = await getImgBase64({ imgId: annexId });
        sealPreview.value = result.image;
        sealFrom.value.annexId = annexId;
      }
      function sealTypeChange(val) {
        if (val != 5) {
          sealFrom.value.middleText = getSealType(val).label;
          sealPreviewInfo();
        }
      }
      function sealPreviewCropper() {
        console.log(sealRef.value);
        sealCropper.value = new Cropper(sealRef.value, {
          viewMode: 1,
          dragMode: 'move',
          preview: '.before',
          initialAspectRatio: 1,
          aspectRatio: 1,
          background: true,
          autoCrop: true,
          autoCropArea: 0.7,
          zoomOnWheel: true,
          zoomOnTouch: true,
          cropBoxResizable: false,
          cropBoxMovable: false,
          wheelZoomRatio: 0.1,
          ready: sealCropend, //首次加载触发事件
          cropend: sealCropend, //拖动结束触发事件
          zoom: sealCropend, //缩放时触发事件
        });
      }
      function sealCropend(end) {
        buildCropperImage();
      }
      function colorRangeChange() {
        buildCropperImage();
      }
      async function buildCropperImage() {
        if (!sealCropper.value) {
          return;
        }
        spinning.value = true;
        // sealCropper.value.rotateTo(sealOptions.value.sealRotate);
        const seal = sealCropper.value
          .getCroppedCanvas({
            width: sealOptions.value.imageWidth,
            height: sealOptions.value.imageHeight,
            imageSmoothingQuality: 'high',
          })
          .toDataURL('image/jpeg');

        const data = {
          image: seal.split(',')[1],
          colorRange: sealOptions.value.sealBackground + 120,
        };

        // const result = await templateGenerateSeal(data);
        const response = await templateGenerateSeal(data);

        // await annexEcho(result);
        // sealFrom.value.annexId = result
        // const image = await getImgBase64({imgId:result});
        // sealPreview.value = result.message;
        sealPreview.value = 'data:image/png;base64,' + response;
        sealFrom.value.sealBase64 = response;
        spinning.value = false;
      }
      function createTypeChange(val) {
        sealPreview.value = '';
        sealFrom.value.annexId = '';
        // console.log("需要制作印章",val);
        if (sealFrom.value.createType == 1) {
          //console.log("需要制作印章");
          sealPreviewInfo();
        }
      }
      async function handleChange(info: any) {
        uploadLoading.value = true;
        uploadSeal.value = (await getBase64(info.file.originFileObj)) as string;
        uploadLoading.value = false;
        //sealPreviewCropper();
        //sealFrom.value.fileId = info.file.response.message;
      }
      async function handleChange2(info: any) {
        firstUpload.value = false;
        await handleChange(info);
        //sealPreviewCropper();
        //sealFrom.value.fileId = info.file.response.message;
      }
      function sealActions(type: number) {
        switch (type) {
          case -1:
            sealCropper.value.rotate(-90);
            break;
          case 1:
            sealCropper.value.rotate(90);
            break;
          case -2:
            sealCropper.value.zoom(-0.1);
            break;
          case 2:
            sealCropper.value.zoom(0.1);
            break;
        }
        setTimeout(function () {
          buildCropperImage();
        }, 100);
      }
      function sealRotateChange(to: number) {
        sealCropper.value.rotateTo(to);
        sealCropend(null);
      }
      function imgeLoad(img) {
        //firstUpload = true;
        console.log('load', img);
        if (!firstUpload.value) {
          sealCropper.value.destroy();
        }
        sealPreviewCropper();
      }
      async function saveSeal() {
        try {
          spinning.value = true;
          if (sealFrom.value.createType == 1) {
            // await sealPreviewInfo();
            const values = await sealStyleRef.value.validateFields();
          } else {
            await buildCropperImage();
          }
          if (!sealFrom.value.sealBase64) {
            message.warning('请先生成印章后再进行提交');
            spinning.value = false;
            return false;
          }

          const data = {
            //"annexId": sealFrom.value.annexId,
            sealId: sealFrom.value.sealId,
            createType: sealFrom.value.createType,
            sealBase64: sealFrom.value.sealBase64,
            middleText: sealFrom.value.middleText,
            bottomText: sealFrom.value.bottomText,
            sealStyle: sealFrom.value.sealStyle,
          };
          // if(editType.value == 0){
          const response = await sealChange(data);
          console.log('makeSeal response:', response);
          //const res = (await addSeal(sealFrom.value)) as any;
          if (response.code == 200) {
            // message.success("印章保存成功！");
            // sealFrom.value.sealId = response.result.sealId;
            // sealId.value = response.result.sealId;
            spinning.value = false;
            router.push('/seals/manage');
            // backPage();
          }
          // }
        } catch (errorInfo) {
          console.log('Failed:', errorInfo);
          message.warning('有必填参数未填');
          // throw new Error("seal params");
        }
        spinning.value = false;
      }

      // async function saveChangeInfo(){
      //   try {
      //     spinning.value = true;
      //     if(sealFrom.value.createType == 1){
      //       await sealPreviewInfo();
      //     }else{
      //       await buildCropperImage();
      //     }
      //     const data = {
      //       "annexId": sealFrom.value.annexId,
      //       "createType": sealFrom.value.createType,
      //       "id": sealFrom.value.sealId,
      //       "middleText": sealFrom.value.middleText,
      //     }
      //     if(editType.value == 2){
      //       const response = await sealChange(data);
      //        console.log("makeSeal response:",response);
      //       if(response.code == 200){
      //          message.success("印面变更成功！");
      //          spinning.value = false;
      //          router.push("/seals/manage")
      //        }
      //     }

      //   } catch (errorInfo) {
      //     console.log('Failed:', errorInfo);
      //     message.warning("有必填参数未填");
      //   }
      //   spinning.value = false;
      // }

      function backPage() {
        router.go(-1);
      }

      function sealUpdateHandle() {
        //editType.value = 2;
        if (!sealId.value) {
          message.warning('参数错误！！！');
        } else {
          getSealDetailes({ sealId: sealId.value }).then((res) => {
            sealFrom.value.sealName = res.sealName;
            sealFrom.value.description = res.description;
            sealFrom.value.sealType = res.sealType;
            sealFrom.value.sealStyle = res.sealStyle ? res.sealStyle : 1;
            sealFrom.value.sealId = res.sealId;
            sealFrom.value.annexId = res.annexId;
            sealFrom.value.createType = res.createType ? res.createType : 1;
            sealFrom.value.middleText = res.middleText;
            sealFrom.value.bottomText = res.bottomText;
            sealFrom.value.adminId = res.adminId;
            sealFrom.value.adminName = res.adminName;
            annexEcho(res.annexId);
          });
        }
      }
      const formColumn = ref<any>({});
      function buildFormColumn(data, taskId) {
        //console.log("build form data",data);
      }
      function sealStyleChange(type) {
        sealFrom.value.sealStyle = type;
        sealPreviewInfo();
      }
      onMounted(() => {
        const query = router.currentRoute.value.query as any;
        sealId.value = query.sealId;
        sealUpdateHandle();
      });
      const beforeUpload = (file: UploadProps['fileList'][number]) => {
        console.log('beforeUpload start');
        const isJpgOrPng =
          file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/jpg';
        if (!isJpgOrPng) {
          message.error('请上传jpg、jpeg、png格式的图片文件');
        }
        const isLt2M = file.size / 1024 / 1024 < 2;
        if (!isLt2M) {
          message.error('文件大小不超过2MB!');
        }
        console.log('beforeUpload:', file, isJpgOrPng, isLt2M);
        return (isJpgOrPng && isLt2M) || Upload.LIST_IGNORE;
      };
      return {
        sealFrom,
        templateGz,
        fileList,
        uploadHeaders,
        handleChange,
        sealPreview,
        sealCropper,
        sealRef,
        sealPreviewCropper,
        uploadSeal,
        buildCropperImage,
        sealOptions,
        imgeLoad,
        sealActions,
        marks,
        handleChange2,
        sealRotateChange,
        sealPreviewInfo,
        sealType,
        sealTypeChange,
        createTypeChange,
        colorRangeChange,
        saveSeal,
        selaFromRef,
        spinning,
        editType,
        backPage,
        uploadLoading,
        beforeUpload,
        formColumn,
        formDisabled,
        getSealType,
        templateGzNone,
        templateFinanceNone,
        sealStyleRef,
        checkMiddleText,
        checkBottomText,
        templateFinance,
        sealStyleChange,
        tenantInfo,
      };
    },
  });
</script>

<style lang="less" scoped>
  .page-content {
    padding: 5px 20px;
  }
  .input-width {
    // width:300px !important;
  }
  .template-make {
    width: 800px;
    border: 1px solid #ededed;
    margin-top: 10px;
    padding: 5px 20px;
    .temlate-item .title {
      font-weight: 600;
    }
    .seal-style {
      //height:100px;
      padding-top: 15px;
      ul {
        display: flex;
        padding: 0;
        margin: 0;
      }
      ul li {
        user-select: none;
        cursor: pointer;
        .seal-img {
          padding: 6px;
          width: 160px;
          height: 160px;
          border: 1px solid #ededed;
          border-radius: 4px;
          display: flex;
          align-items: center;
        }
        .signature-style {
          display: flex;
          width: 116px;
          height: 116px;
          padding: 8px 6px;
          justify-items: center;
          align-items: center;
          border: 1px solid;
          border-color: inherit;
          border-radius: 4px;
        }
        .signature-style img {
          user-select: none;
        }
        .seal-name {
          text-align: center;
          line-height: 30px;
          color: #9e9e9e;
        }
      }
      ul li.active {
        .seal-img {
          border-color: #1890ff;
        }
        .seal-name {
          color: #1890ff;
        }
      }
      ul li:nth-child(n + 2) {
        margin-left: 10px;
      }
    }
  }
  .seal-make-layout {
    display: flex;
    .template-make {
    }
    .seal-preview {
      padding-left: 40px;
      .title {
        font-size: 14px;
        font-weight: 600;
        height: 30px;
      }
    }

    .seal-upload {
      padding: 20px 0;
    }
    .seal-preview-img {
      width: 160px;
      padding: 10px;
      border: 1px solid #ededed;
      display: flex;
      align-items: center;
    }
  }
  .local-upload {
    width: 160px;
    height: 160px;
    padding: 10px;
    border: 1px dashed rgba(0, 0, 0, 0.3);
    display: flex;
    align-items: center;
    justify-items: center;
    text-align: center;
    flex-direction: column;
    padding-top: 40px;
  }
  .upload-img {
    width: 300px;
    height: 300px;
  }
  .SealActions {
    width: 300px;
    padding: 8px 15px;
    background-color: rgba(0, 0, 0, 0.07);
  }
  .center {
    text-align: center;
    line-height: 32px;
  }
  .pointer {
    cursor: pointer;
  }

  .upload-instructions {
    margin-bottom: 20px;
    h3 {
      font-size: 16px;
      margin-bottom: 10px;
    }
    div {
      // margin-bottom: 20px;
      p {
        margin: 5px 0;
      }
      img {
        display: block;
        max-width: 100%;
        // margin: 10px 0;
      }
    }
  }

  .upload-area {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    width: 500px;
    height: 300px;
    border: 1px dashed #d9d9d9;
    border-radius: 4px;
    background-color: #b3d9fd16;
    transition: all 0.3s;
    text-align: center;

    &:hover {
      border-color: #1890ff;
    }

    p {
      margin: 10px 0;
      color: #666;
    }
  }

  .upload-steps {
    display: flex;
    justify-content: space-between;
    align-items: stretch;
    gap: 20px;

    @media (max-width: 768px) {
      flex-direction: column;
    }

    .upload-step {
      flex: 1;
      // padding: 15px;
      background-color: #f3f6fd;
      border: 1px solid #eee;
      border-radius: 4px;
      box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
      margin-top: 20px;
      width: 500px;
      height: 300px;

      p {
        margin: 5px 0;
      }

      img {
        max-width: 100%;
        // margin-top: 10px;
      }
    }

    .step-one {
      order: 1;
    }

    .step-two {
      order: 2;
      display: flex;
      justify-content: center; // 水平居中
      align-items: center; // 垂直居中
      height: 300px; // 父容器高度需明确
    }

    .upload-wrapper {
      width: 100%;
      height: 100%;
      display: flex;
      justify-content: center;
      align-items: center;
    }
  }
</style>
