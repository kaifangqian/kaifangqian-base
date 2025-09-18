<!--
  @description 合同详情

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
		<div class="sign-detail-container">
			<div class="download-btn">
				<van-button type="primary" size="mini" style="width: 1.5rem;" @click="openActionSheet">下载</van-button>
			</div>
		    <div class="sign-info-card">
		        <img class="sign-detail-status-img" :src="getAssetsFile('sign-' + detailInfo.baseVo.status + '.png')" />
		        <van-list>
		            <van-cell title="编号：">
		                <template #default>
		                    <span class="detail-value">{{ detailInfo.baseVo.code || '无'}}</span>
		                </template>
		            </van-cell>
		            <van-cell title="主题：">
		                <template #default>
		                    <span class="detail-value">{{ detailInfo.baseVo.subject }}</span>
		                </template>
		            </van-cell>
		            <van-cell title="业务线：">
		                <template #default>
		                    <span class="detail-value">{{ detailInfo.baseVo.name }}</span>
		                </template>
		            </van-cell>
		            <van-cell title="状态：">
		                <template #default>
		                    <span class="detail-value">{{ loadRuStatus(detailInfo.baseVo.status) }}</span>
		                </template>
		            </van-cell>
		            <!-- <van-cell title="发起时间：">
		            <template #default>
		                <span class="detail-value">{{ detailInfo.baseVo.startDateString }}</span>
		            </template>
		        </van-cell> -->
		            <van-cell title="签署截止时间：">
		                <template #default>
		                    <span class="detail-value">{{ detailInfo.baseVo.expireDateString || '无' }}</span>
		                </template>
		            </van-cell>
		
		        </van-list>
		    </div>
		    <div class="sign-info-card">
		        <van-list>
		            <van-cell title="签约文件：">
		                <template #default>
		                    <ul class="contract-files-ul">
		                        <li v-for="(item, index) in detailInfo.fileList" :key="index" class="contract-files-li">
		                            <div class="file-icon" @click="loadDocInfo(item)">
		                                <SvgIcon :name="item.docType == 1 ? 'pdf' : 'tpl'" size="60" class="file-img" />
		                            </div>
		                            <div class="file-footer">
		                                <p class="file-name">{{ item.name || '签约文档' }}</p>
		                                <!-- <van-popover v-model:show="showPopover" :actions="actions"
		                                    @select="(e) => onSelect(e, item)">
		                                    <template #reference>
		                                        <SvgIcon name="more-options" size="20"></SvgIcon>
		                                    </template>
		                                </van-popover> -->
		                            </div>
		                        </li>
		                    </ul>
		                </template>
		            </van-cell>
		            <van-cell title="附件：">
		                <template #default>
		                    <FileList :fileList="detailInfo.otherFileList"
		                        v-if="detailInfo.otherFileList && detailInfo.otherFileList.length" />
		                    <span v-else>无</span>
		                </template>
		            </van-cell>
		        </van-list>
		
		    </div>
		    <div class="sign-info-card">
		        <van-list>
		            <van-cell title="签署顺序：">
		                <template #default>
		                    <span class="detail-value">{{ detailInfo.baseVo.signOrderType == 1 ? '顺序签署' : '无序签署' }}</span>
		                </template>
		            </van-cell>
		            <div class="signer-list cell-block-info">
		                <div v-for="(item, index) in operatorList">
		                    <div v-if="item.signerType == 1" class="signer-item">
		                        <van-tag class="signer-name sender-type" color="#1c84d4">
		                            发起方
		                        </van-tag>
		                        <div class="signer-area">
		                            <div class="sender-name">
		                                <p>{{ item.signerName }}</p>
		                                <van-tag v-if="item.writeStatus > -1" class="sign-status"
		                                    :color="loadSignColor(item.writeStatus)">{{
		                                        loadWriteStatus(item.writeStatus) }}</van-tag>
		                            </div>
		                            <div class="sender-list-area">
		                                <span class="sub-sign-title">签署流程：</span>
		                                <div class="sender-list-info">
		                                    <div v-for="(sendItem, sendIndex) in item.senderList" :key="sendIndex"
		                                        class="sender-item-info">
		                                        <div class="sender-status-item">
		                                            <van-badge status="default" color="#333" />
		                                            <span class="sign-flow-name">{{ sendItem.senderName + ' — ' + '[' +
		                                                (sendItem.senderSignType == 1 ? '自动盖章' : sendItem.senderUserName) +
		                                                ']' }}</span>
		                                        </div>
		                                        <van-tag class="sign-status" :color="loadSignColor(sendItem.signStatus)">{{
		                                            loadSignStatus(sendItem.signStatus) }}</van-tag>
		                                    </div>
		                                </div>
		                            </div>
		                        </div>
		                    </div>
		                    <div v-if="item.signerType == 2" class="signer-item">
		                        <van-tag class="signer-name sender-type" color="#1c84d4">
		                            接收方{{ hasSenderSigner ? (index == 0 ? index + 1 : index) : (index + 1) }}
		                        </van-tag>
		                        <div class="signer-info signer-area">
		                            <!-- <div class="sender-name">
		                                <p>个人</p>
		                            </div> -->
		                            <div class="sender-list-area"  style="margin-top: 20px;">
		                                <!-- <span class="sub-sign-title">个人姓名：</span> -->
		                                <div class="sender-list-info">
		                                    <div class="sender-item-info">
		                                        <div class="sender-status-item">
		                                            <div>
		                                                <span class="sign-flow-name">{{ item.signerName }}</span>
		                                                <span class="sign-flow-name" style="margin-left:5px;">{{
		                                                    item.signerExternalValue }}</span>
		                                            </div>
		                                            <van-tag v-if="item.writeStatus > -1" class="sign-status"
		                                                :color="loadSignColor(item.writeStatus)">{{
		                                                    loadWriteStatus(item.writeStatus)
		                                                }}</van-tag>
		                                            <van-tag class="sign-status" :color="loadSignColor(item.signStatus)">{{
		                                                loadSignStatus(item.signStatus) }}</van-tag>
		                                        </div>
		                                    </div>
		
		                                </div>
		                            </div>
		                        </div>
		                    </div>
		                    <div v-if="item.signerType == 3" class="signer-item">
		                        <van-tag class="signer-name sender-type" color="#1c84d4">
		                            接收方{{ hasSenderSigner ? (index == 0 ? index + 1 : index) : (index + 1) }}
		                        </van-tag>
		                        <div class="signer-area">
		                            <div class="sender-name">
		                                <p>{{ item.signerName }}</p>
		                                <van-tag v-if="item.writeStatus > -1" class="sign-status"
		                                    :color="loadSignColor(item.writeStatus)">{{
		                                        loadWriteStatus(item.writeStatus) }}</van-tag>
		                            </div>
		
		                            <div class="sender-list-area">
		                                <span class="sub-sign-title">签署流程：</span>
		                                <div class="sender-list-info">
		                                    <div v-for="(sendItem, sendIndex) in item.senderList" :key="sendIndex"
		                                        class="sender-item-info">
		                                        <div class="sender-status-item">
		                                            <van-badge status="default" color="#333" />
		                                            <span class="sign-flow-name">{{ (sendItem.senderType == 1 ? '经办人签字' :
		                                                '组织签章')
		                                                + ' — ' + '[' + sendItem.senderName + ']'
		                                            }}</span>
		
		                                        </div>
		                                        <van-tag class="sign-status" :color="loadSignColor(sendItem.signStatus)">{{
		                                            loadSignStatus(sendItem.signStatus) }}</van-tag>
		                                    </div>
		                                </div>
		                            </div>
		                        </div>
		                    </div>
		                </div>
		            </div>
		        </van-list>
		    </div>
		    <div class="sign-info-card">
		        <van-list>
		            <van-cell title="抄送：">
		                <template #default>
		                    <van-tag color="#999" size="medium" v-for="item in detailInfo.ccerList"
		                        v-if="detailInfo.ccerList && detailInfo.ccerList.length">{{
		                            item.ccerType == 1 ?
		                            item.internalCcerName : (item.externalCcerName + '（' + item.externalCcedValue + '）')
		                        }}</van-tag>
		                    <span v-else>无</span>
		                </template>
		            </van-cell>
		            <!-- <van-cell title="签署方式：">
		                <template #default>
		                    <span v-if="detailInfo.baseVo.caSignType == 1">CA数字证书（符合电子签名法）</span>
		                    <span v-if="detailInfo.baseVo.caSignType == 2">平台防篡改证书（保护文件）（无法律效力）</span>
		                    <span v-if="detailInfo.baseVo.caSignType == 3">合成签名和印章图片（不保护文件）（无法律效力）</span>
		                </template>
		            </van-cell> -->
		        </van-list>
		    </div>
		</div>
		<DownloadActionSheet :actionSheet="actionSheet" ref="actionSheetRef"/>
	</div>
    
</template>

<script lang="ts">
import { ref, defineComponent, onMounted } from "vue"
import { loadRuStatus } from '../transform';
import { useRoute, useRouter } from 'vue-router';
import Api from '@/api/contract/index';
import { ImagePreview } from 'vant'
import { handleDownload, getHashQueryString } from '@/utils/util';
import { getAssetsFile } from '@/utils/pub-use';
import { loadSignColor, loadSignStatus, loadWriteStatus } from '@/pages/transform';
import FileList from '@/components/FileList/index.vue'
import DownloadActionSheet from "@/pages/components/DownloadActionSheet.vue"

export default defineComponent({
    name: "DocDetail",
    components: {
        FileList,DownloadActionSheet
    },
    props: {
        docId: {
            type: String,
            default: ''
        }
    },
    setup(props) {
        const detailInfo: any = ref({
            baseVo: {
                code: '',
                subject: '',
                status: 1,
            },
            ccerList: []
        })
        const showPopover = ref(false);
        const callbackPage = getHashQueryString('callbackPage'); 
        const actions = ref([
            { text: '下载' },
            { text: '查看' },
        ])
		const actionSheet = ref({
			show:false,
			fileList:[],
			signRuId:""
		});
		const actionSheetRef = ref();
        const imgPreviewList = ref([]);
        const hasSenderSigner = ref(false);
        const operatorList: any = ref([])
        const route = useRoute();
        const router = useRouter();
        const signRuId = route.query.signRuId;
        // const baseUrl = window.appInfo.mobile_app_info.url + '/resrun-paas';
        let baseUrl = import.meta.env.VITE_APP_API_BASE_URL;

        onMounted(() => {
            checkStatus()
        })
        async function checkStatus() {
            let { result } = await Api.getViewCheck({ signRuId: signRuId });
            console.log(result, 'cesfdsfds')
            if (!result) {
                router.replace({
                    path: 'noauth',
                    query: {
                        signRuId: signRuId,
                        taskType: 'copy',
                        callbackPage:callbackPage
                    }
                })
                return
            }

            setTimeout(() => {
                getRuInfo();
            })

        }

        async function getRuInfo() {
            let { result } = await Api.getDocInfoByRuId({ signRuId: signRuId })
            if (result) {
                detailInfo.value = {
                    ...result,
                }
                if (result.baseVo.downloadFileType != 1) {
                    actions.value = [
                        { text: '查看' },
                    ]
                }
				actionSheet.value.fileList = result.fileList;
                getSignerOperator()
            }
        }
        async function getSignerOperator() {
            let { result } = await Api.getOperators({ signRuId: signRuId })
            if (result) {
                operatorList.value = result.sort((a, b) => a.signerOrder - b.signerOrder);
                operatorList.value.map((item: any) => {
                    if (item.signerType == 1 || item.signerType == 3) {
                        item.senderList = item.senderList ? item.senderList.sort((a, b) => a.senderOrder - b.senderOrder) : []

                    }
                    if (item.signerType == 1) {
                        hasSenderSigner.value = true;
                    }

                    if (item.senderList) {
                        item.senderList.sort((a: any, b: any) => a.senderOrder - b.senderOrder);
                    }

                })
            }
        }
        function onSelect(e, item) {
            console.log(e.text, item, '文件下载---')
            if (e.text == '下载') {
                handleDownload({ id: item.annexId })
            } else {
                handlePreview(item)
            }
        }
        //查看文档
        function loadDocInfo(row) {
            router.push({
                path: '/doc',
                query: {
                    signRuId: signRuId,
                    docId: row.id,
                    callbackPage:callbackPage
                }
            })
        }
        //预览图片
        async function handlePreview(row) {
            let { result } = await Api.getDocImgsById({ annexId: row.annexId })
            if (result) {
                imgPreviewList.value = [];
                result.map((item: any) => {
                    imgPreviewList.value.push(baseUrl + '/file/downloadFileStream/' + item.annexId)
                })
                console.log(imgPreviewList.value, '图片地址')
                ImagePreview({
                    images: imgPreviewList.value,
                    startPosition: 0,
                });
            }
        }
		function openActionSheet(){
			actionSheetRef.value.initData();
			actionSheet.value.show = true;
			actionSheet.value.signRuId = detailInfo.value.baseVo.signRuId;
		}
        return {
            detailInfo,
            loadRuStatus,
            getAssetsFile,
            handleDownload,
            onSelect,
            showPopover,
            loadSignColor,
            loadSignStatus,
            loadWriteStatus,
            hasSenderSigner,
            operatorList,
            loadDocInfo,
            handlePreview,actionSheetRef,
            actions,actionSheet,openActionSheet
        }
    }
})
</script>

<style lang="less" scoped>
.detail-label {
    width: 200px;
    display: inline-block;
    text-align: right;
}

.sign-detail-container {
    position: relative;

    .sign-info-card {
        position: relative;
        margin-bottom: 20px;
    }

    .sign-detail-status-img {
        position: absolute;
        right: 20px;
        top: 30px;
        width: 180px;
        z-index: 10;
    }

    :deep(.van-cell__title) {
        flex: 0 0 200px;
        color: #333;
        text-align: left;
    }

    :deep(.van-cell__value) {

        text-align: left;

        .van-tag {
            margin: 0 10px;
        }
    }
}

.contract-files-ul {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    // width: 800px;

    // border: 1px solid #e9e9e9;
    .contract-files-li {
        width: 170px;
        height: 220px;
        background: #fff;
        text-align: center;
        margin: 10px;
        position: relative;
        box-sizing: border-box;
        display: flex;
        padding: 0;
        flex-direction: column;
        border: 1px solid #d3d1d1;

        .file-icon {
            border-bottom: 1px solid #d3d1d1;
            position: relative;
            height: 80%;
            width: 100%;

            :deep(.app-iconify) {
                position: absolute;
                top: -5px;
                right: -5px;
                cursor: pointer;
                z-index: 800;
                background: #fff;
                border-radius: 50%;
                visibility: hidden;
                ;
            }

            &:hover {
                border-radius: 2px;

                .file-mask {
                    display: block;
                }

                :deep(.app-iconify) {
                    visibility: visible;
                    ;
                }
            }

            .file-tag {
                position: absolute;
                left: 2px;
                top: 0;
            }

            .file-img {
                position: absolute;
                left: 50%;
                top: 50%;
                transform: translate(-50%, -50%);
            }
        }

        .file-footer {
            display: flex;
            align-items: center;
            height: 20%;

            :deep(.van-popover__wrapper) {
                height: 100%;
            }
        }

        .file-mask {
            display: none;
            position: absolute;
            left: 0;
            top: 0;
            right: 0;
            bottom: 0;
            background-color: rgba(12, 12, 14, 0.7);

            :deep(.ant-btn-link) {
                color: #fff;
                height: 24px;
                padding: 0px 15px;
                border: 1px solid #fff;
                position: absolute;
                left: 50%;
                top: 50%;
                transform: translate(-50%, -50%);

                &:hover {
                    background: #fff;
                    color: #000;
                }
            }

        }

        .file-img {
            width: 100%;
            height: 100%;
        }

        .file-name {
            font-size: 14px;
            font-weight: 550;
            white-space: nowrap;
            width: 140px;
            margin: 0 auto;
            padding-left: 10px;
            overflow: hidden;
            text-overflow: ellipsis;
            margin-bottom: 0;
            color: #333;


        }
    }
}

.signer-list {
    .signer-item {
        position: relative;

        .signer-name {
            position: absolute;
        }

        .signer-area {
            padding: 10px 20px;
            background: #eaf1f7;
            margin-bottom: 10px;
            margin-top: 20px;
            border-radius: 8px;

            .sender-name {
                display: flex;
                align-items: center;
                // margin-bottom: 30px;
                margin-top: 10px;


                p {
                    // margin-bottom: 0;
                    min-width: 400px;
                    font-size: 26px;
                    font-weight: 550;
                }

                :deep(.van-tag) {
                    margin-left: 20px;
                }
            }

            .sender-list-area {
                display: flex;

                .sub-sign-title {
                    font-size: 24px;
                    font-weight: 550;
                    flex: 0 0 auto;
                }

                .sign-flow-name {
                    min-width: 290px;
                }

                // .sender-status-item {
                //     .sign-flow-name {
                //         margin-right: 10px;

                //     }
                // }

                .sender-list-info {
                    display: flex;
                    flex-direction: column;

                    .sender-item-info {
                        display: flex;
                        align-items: center;
                        margin-bottom: 20px;
                        font-size: 24px;

                        :deep(.van-tag) {
                            margin-left: 10px;
                        }
                    }

                    .sender-status-item {
                        display: flex;
                    }
                }

            }


        }
    }


}
.download-btn{
	position: fixed;
	top:0;
	right: 0;
	z-index:2;
	height: var(--van-nav-bar-height);
	width: 2.5rem;
	display: flex;
	align-items: center;
	justify-content: center;
}
</style>
