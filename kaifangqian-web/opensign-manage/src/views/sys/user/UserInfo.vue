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
    <Card title="基本信息" class="card-title">
      <Row>
        <Col :span="12">
          <div class="info-list left">
            <div class="info-item">
              <span class="title">用户账号：</span>
              <span class="info">{{userInfo.username}}</span>
              <!-- <p class="action"><Icon icon="ant-design:edit-twotone" /> </p> -->
            </div>
            <div class="info-item">
              <span class="title">用户姓名：</span>
              <span class="info">{{userInfo.realname}}</span>
              <p class="action" @click="handleChangeUserName(userInfo.realname)"><Icon icon="ant-design:edit-twotone" size="16"/> </p>
            </div>
            <div class="info-item">
              <span class="title">所属组织：</span>
              <span class="info">{{userInfo.departNames&&userInfo.departNames.join('/')}}</span>
            </div>
            <div class="info-item">
              <span class="title">所属角色：</span>
              <span class="info">{{userInfo.roleNames&&userInfo.roleNames.join('/')}}</span>
            </div>
            <div class="info-item">
              <span class="title">当前登录身份：</span>
              <span class="info">{{userInfo.loginDepartName}}</span>
            </div>
            <!-- <div class="info-item-auth">
              <span class="title">用户权限：</span>
              <div class="info">
                <BasicTable @register="registerTable">
                  <template #type="{text}">
                    <span>{{loadType(text)}}</span>
                  </template>
                  <template #name="{text,record}">
                    <span>{{record.departName?`${ text + '('+ record.departName +')'}`:text}}</span>
                  </template>
                </BasicTable>
              </div>
            </div> -->
          </div>
        </Col>
        <Col :span="12">
            <div class="info-list ">
              <div class="info-item">
                <span class="title">头像：</span>
                <span class="info">
                   <CropperAvatar
                    :uploadApi="uploadAvatarApi"
                    :value="userInfo.avatarImg"
                    btnText="更换头像"
                    :btnProps="{ preIcon: 'ant-design:cloud-upload-outlined' }"
                    @change="updateAvatar"
                    width="100"
                  />
                </span>
                <!-- <a-button shape="round" >
                  <template #icon>
                      <Icon icon="ant-design:upload-outlined" />
                  </template>

                </a-button> -->
              </div>
                <div class="info-item">
              <span class="title">创建时间：</span>
              <span class="info">{{userInfo.createTime}}</span>
            </div>
          </div>
        </Col>
      </Row>
    </Card>
    <Card title="登录信息" style="margin-top:20px;">
      <span class="card-tip">账号支持多种登录方式，默认使用账号密码登录，可以通过绑定手机号、邮箱进行登录</span>
      <div class="info-line-list">
        <div class="line-item">
            <Row>
              <Col :span="20">
                <div class="info-item">
                    <span class="title">密码</span>
                    <span class="info">**********  </span>
                    <span class="password-state-bar">
                      <Tag :color="tagBar.weakBarColor"></Tag>
                      <Tag :color="tagBar.averageBarColor"></Tag>
                      <Tag :color="tagBar.strongBarColor"></Tag>
                      <span class="password-state-text">{{tagBar.strongText}}</span>
                    </span>
                </div>
                <div class="info-tip">安全性高的密码可以使帐号更安全，建议您定期更换密码以保护帐号安全。 </div>
              </Col>
              <Col :span="4">
                 <a-button type="link" class="action-btn" @click="handleChangePassword">修改密码</a-button>
              </Col>
            </Row>
        </div>
        <div class="line-item">
            <Row>
              <Col :span="20">
                <div class="info-item">
                    <span class="title">手机号</span>
                    <span class="info">{{ userInfo.phone?('+86 '+ (userInfo.phone&&userInfo?.phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2'))):'暂无绑定手机号'}} </span>
                </div>
                <div class="info-tip">手机号可以用于登录、找回密码等，若该号码已丢失或停用，请立即更换以保护帐号安全。 </div>
              </Col>
              <Col :span="4">
                 <a-button type="link" class="action-btn" @click="handleChangePhone(userInfo.phone)">{{userInfo.phone?'解绑':'绑定'}}</a-button>
              </Col>
            </Row>
        </div>
        <div class="line-item">
            <Row>
              <Col :span="20">
                <div class="info-item">
                    <span class="title">邮箱</span>
                    <span class="info">{{userInfo.email || '暂未绑定邮箱'}}</span>
                </div>
                <div class="info-tip">邮件地址可以用于登录、找回密码等，若该邮件地址已丢失或停用，请立即更换以保护帐号安全。 </div>
              </Col>
              <Col :span="4" >
                 <a-button type="link" class="action-btn" @click="handleChangeEmail(userInfo.email)">{{userInfo.email?'解绑':'绑定'}}</a-button>
              </Col>
            </Row>
        </div>
      </div>
    </Card>
    <ResetPassword @register="registerModal" @success="handleSuccess" @back="handleBack"/>
    <ResetPhone @register="registerPhoneModal" @success="handleSuccess"/>
    <ResetEmail @register="registerEmail" @success="handleSuccess" />
    <ResetUserName @register="registerUserName" @success="handleSuccess" />
    <MobileOrEmail  @register="registerPhoneEmailModal" @success="handleMethodsSuccess"/>
  </div>
</template>
<script lang='ts'>
import type { UserInfo } from '/#/store';
import { defineComponent, onMounted, reactive, ref, unref, computed } from 'vue';
import { Card,Row,Col,Tag} from 'ant-design-vue';
import Icon from '/@/components/Icon';
import { useModal } from '/@/components/Modal';
import { useUserStore } from '/@/store/modules/user';
import ResetPassword from './modal/ResetPassword.vue';
import ResetPhone from './modal/ResetPhone.vue';
import ResetEmail from './modal/ResetEmail.vue';
import ResetUserName from './modal/ResetUserName.vue';
import { getUserInfo } from '/@/api/sys/user'; 
import MobileOrEmail from '/@/views/sys/login/MobileOrEmail.vue';
import { CropperAvatar } from '/@/components/Cropper';
import { useMessage } from '/@/hooks/web/useMessage';
import { useRouter } from 'vue-router';
import { uploadAvatarApi, getImgBase64 } from '/@/api/sys/upload';
import { BasicTable, useTable } from '/@/components/Table';
import { authColumns } from './data'

export default defineComponent({
  name: '用户中心',
  components:{
    Card,Row,Col,Icon,
    ResetPassword,
    ResetPhone,
    ResetEmail,
    ResetUserName,
    Tag,
    CropperAvatar,
    MobileOrEmail,
    BasicTable
  },
  setup(){
    let userInfo = ref<UserInfo>({
      phone:'',
      avatarImg:''
    });
    const route = useRouter(); 
  
    const tagBar = reactive({
      passwordLevel: 'WEAK',
      weakBarColor: "#cdc0c0",
      averageBarColor: '#f50',
      strongBarColor: '#cdc0c0',
      strongText:'弱'
    })
    const userStore = useUserStore();
    const [registerModal, { openModal,closeModal }] = useModal();
    const { createMessage } = useMessage();
    const [registerPhoneModal, { openModal:openPhoneModal }] = useModal();
    const [registerEmail, { openModal:openEmailModal }] = useModal();
    const [registerUserName, { openModal:openUserModal }] = useModal();
    const [registerPhoneEmailModal, { openModal:openMethodModal,closeModal:closePhoneEmailModal  }] = useModal();
    const storeUserInfo =  userStore.getUserInfo;
    tagBar.passwordLevel = storeUserInfo.passwordLevel;
    // userInfo.value = storeUserInfo.value;
    setPasswordBarColor(tagBar.passwordLevel);
    
    const [registerTable,{ setTableData}] = useTable({
        title: '',
        titleHelpMessage: [],
        immediate:false,
        columns: authColumns,
        rowKey:'id',
        useSearchForm: false,
        dataSource:[],
        showDragColumn:false,
        showIndexColumn: false,
        bordered: false,
        isTriggerSelect:false,
        showTableSetting: false,
        canResize: false,
        pagination:false,
        striped:false,
        tableSetting: { fullScreen: false ,redo:true,setting:false,size:false},
      });

    const fetch = async ()=> {
      userInfo.value = await getUserInfo();
      if(userInfo){
        userStore.setUserInfo(unref(userInfo));
        setTableData(userInfo.value.authGroups)
        if(userInfo.value.avatar){
          getAvatar(userInfo.value.avatar)
        }
      }
    }
    const avatar = computed(() => {
        const { avatar } = userStore.getUserInfo;
        return avatar;
    })
    async function getAvatar(id){
      let result1 = await getImgBase64({imgId:id});
      if(result1){
        userInfo.value.avatarImg = result1;
      }
      // let result = await getImgStream({imgId:id});
      // if(result){
      //   // let imgBase64 = 'data:image/png;base64,' + window.btoa(String.fromCharCode(...new Uint8Array(result.data)));

      //   let imgBase64 = 'data:image/png;base64,' + Buffer.from(result.data, 'binary').toString('base64');
      //   console.log(imgBase64,'---dfsdfds---')

        // let str =  Buffer.from(result.data, 'binary');
        // let data = "";
        // let len = str.byteLength;
        // for (let i = 0; i < len; i++) {
        //    data += String.fromCharCode(str[i]);
        // }
        // console.log(data,'-----ddddddd--')
        // let base64 =  'data:image/png;base64,' + window.btoa(data);
        // console.log(base64,'-----test---');


        // // console.log(imgBase64, window.btoa(String.fromCharCode(...new Uint8Array(result.data))),'--------fdsfs----');
        // console.log(window.btoa(  new Uint8Array(result.data).reduce( (data, byte) => data + String.fromCharCode(byte),  ""  )))
        // console.log(imgBase64, window.btoa(
        //      new Uint8Array(result.data).reduce( (data, byte) => data + String.fromCharCode(byte),  ""  )
        //    ));
        // let blob = new Blob([result.data]);
        // blobToBase64(blob).then(res=>{
        //   // console.log(res,'---结果--')
        // })
        // let url = 'data:image/png;base64,'+ window.URL.createObjectURL(blob);
        //   console.log(url,'333333')
      // }
    }

    function blobToBase64(blob) {
        return new Promise((resolve, reject) => {
          const fileReader = new FileReader();
          fileReader.onload = (e:any) => {
            resolve(e?.target.result);
          };
          // readAsDataURL
          fileReader.readAsDataURL(blob);
          fileReader.onerror = () => {
            reject(new Error('blobToBase64 error'));
          };
        });
    }
    function handleChange(list: string[]) {
      createMessage.info(`已上传文件${JSON.stringify(list)}`);
    }
   async  function updateAvatar(src: string, data) {
      // console.log(src, data, '上传成功')
      // console.log('fetch3----')
       fetch()
      // const userinfo = userStore.getUserInfo;
      // userinfo.avatar = src;
      // userStore.setUserInfo(userinfo);
    }
    function setPasswordBarColor(val){
      console.log(val)
      switch (val){
        case 'WEAK':
          tagBar.weakBarColor = '#f50';
          tagBar.averageBarColor = '#cdc0c0';
          tagBar.strongBarColor = '#cdc0c0';
          tagBar.strongText = '弱';
          break;
        case 'AVERAGE':
          tagBar.weakBarColor = '#c59631';
          tagBar.averageBarColor = '#c59631';
          tagBar.strongBarColor = '#cdc0c0';
          tagBar.strongText = '中';
          break;
        case 'STRONG':
          tagBar.weakBarColor = '#87d068';
          tagBar.averageBarColor = '#87d068';
          tagBar.strongBarColor = '#87d068';
          tagBar.strongText = '强';
          break;
        default:
          tagBar.weakBarColor = "#cdc0c0";
          tagBar.averageBarColor ='#cdc0c0';
          tagBar.strongBarColor = '#cdc0c0';
      }
    }
    function handleSuccess(){
      console.log('fetch2----')
      fetch();
    }


    onMounted(()=>{
      console.log(route.currentRoute,'----路由--');
      if(route.currentRoute.value?.params?.type?.includes('change')){
        openMethodModal(true,{
          isUpdate:true,
          record:{
            title:'请选择修改方式',
            showPasswordType:true
          }
        })
      }
      console.log('fetch1----')
      fetch()
    })
    

    function handleChangePassword(){
      openMethodModal(true,{
        isUpdate:true,
        record:{
          title:'请选择修改方式',
          showPasswordType:true
        }
      })
    }
    function handleChangePhone(val){
      openPhoneModal(true,{
        isUpdate:val?true:false,
        record:{
          phone:val?val:'',
          title:val?'解绑手机号':'绑定手机号'
        }
      })
    }
    function handleChangeEmail(val){
      openEmailModal(true,{
        isUpdate:val?true:false,
         record:{
          email:val?val:'',
          title:val?'解绑邮箱':'绑定邮箱'
        }
      })
    }
    function handleChangeUserName(val){
      openUserModal(true,{
        isUpdate:true,
         record:{
          username:val?val:'',
        }
      })
    }
    function handleMethodsSuccess(moduleName,methodName){
      console.log(moduleName,'修改类型')
      closePhoneEmailModal();
      openModal(true,{
        isUpdate:false,
        record:{
          type:methodName
        }
      })
    }
    function handleBack(){
      closeModal();
      openMethodModal();

    }
    function loadType(type){
      switch(type){
          case 'userOnly':
            return '用户';
          case 'userDepart':
            return '用户';
          case 'role':
            return '角色';
          case 'depart':
            return '部门';
          default:
            return ''
      }
    }
    return {
      registerTable,
      userInfo,
      registerModal,
      handleSuccess,
      handleChangePassword,
      handleChangePhone,
      registerPhoneModal,
      registerEmail,
      handleChangeEmail,
      registerUserName,
      handleChangeUserName,
      registerPhoneEmailModal,
      tagBar,
      loadType,
      handleMethodsSuccess,
      handleBack,
      handleChange,
      uploadAvatarApi: uploadAvatarApi as any,
      updateAvatar,
      avatar
    }
  }
})
</script>
<style lang="less" scoped>

.info-list.left{
  border-right:1px dashed #e4e4e4;
}
  :deep(.ant-card-head){
    min-height:40px;
    background: #f8f8f8;
  }
  :deep(.ant-card-head-title){
    font-size:14px!important;
    color:#333;
    padding:9px 0;
  }
  :deep(.ant-card-body){
    padding:20px;
  }
.info-item{
  display: flex;
  margin:20px 0;
  align-items: center;
  .title{
      width:110px;
      font-size:12px;
      font-weight: 600;
    }
}
.info-item-auth .title{
    width:90px;
    font-size:12px;
    font-weight: 600;
}
.info-list{
  padding:20px 25px;
  .info-item{
    .info{
      min-width:250px;
      font-size: 12px;
    }
    .action{
      cursor: pointer;
      margin-bottom:0;
      &:hover{
        color:#127fd2;
      }
    }
  };
}
.card-tip,.info-tip{
  font-size: 12px;
  color:#a0a1a3;
  margin:5px 0;
}
.info-line-list{
  margin-left:10px;
  .line-item{
    border-bottom:1px dashed #a0a1a3;
  }
  .action-btn{
    height: 100%;
    width: 100%;
  }
}
.password-state-bar{
  display: flex;
  margin-left:50px;
  :deep(.ant-tag){
    margin-right: 1px;
    border: none;
    border-radius: 0;
    padding: 2px 25px;
    height:22px;
  }
  .password-state-text{
    margin-left:5px;
  }
}
</style>
