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
    <div class="sign-detail-container">
        <van-list>
            <van-cell title="编号：">
                <template #default>
                    <span class="detail-value">{{ detailInfo.baseVo.code || '无' }}</span>
                </template>
            </van-cell>
            <van-cell title="主题：">
                <template #default>
                    <span class="detail-value">{{ detailInfo.baseVo.subject }}</span>
                </template>
            </van-cell>
            <van-cell title="状态：">
                <template #default>
                    <span class="detail-value">{{ loadRuStatus(detailInfo.baseVo.status) }}</span>
                </template>
            </van-cell>
            <van-cell title="发起时间：">
                <template #default>
                    <span class="detail-value">{{ detailInfo.baseVo.startDateString }}</span>
                </template>
            </van-cell>
            <van-cell title="签署截止时间：">
                <template #default>
                    <span class="detail-value">{{ detailInfo.baseVo.expireDateString || '无' }}</span>
                </template>
            </van-cell>
            <van-cell title="附件：">
                <template #default>
                    <FileList v-if="detailInfo.otherFileList && detailInfo.otherFileList.length"
                        :fileList="detailInfo.otherFileList" />
                    <span v-else>无</span>
                </template>
            </van-cell>
            <van-cell title="抄送：" class="detail-cc-list">
                <template #default>
                    <van-tag color="#999" v-for="item in detailInfo.ccerList"
                        v-if="detailInfo.ccerList && detailInfo.ccerList.length">
                        {{
                            item.ccerType == 1 ?
                            item.internalCcerName : (item.externalCcerName + '（' + item.externalCcedValue + '）')
                        }}</van-tag>
                    <span v-else>无</span>
                </template>
            </van-cell>
        </van-list>
    </div>
</template>

<script lang="ts">
import { ref, defineComponent, onMounted, watch } from "vue"
import { loadRuStatus } from '../transform';
import Api from '@/api/contract/index';
import FileList from '@/components/FileList/index.vue'

interface CcerItem {
    ccerType: number;
    internalCcerName: string;
    externalCcedValue: string;
    externalCcerName: string;
}

export default defineComponent({
    name: "SignDetail",
    props: {
        signRuId: {
            type: String,
            default: ''
        }
    },
    components: {
        FileList
    },
    setup(props) {
        const detailInfo = ref({
            baseVo: {
                code: '',
                subject: '',
                status: 1,
                startDateString: '',
                expireDateString: ''

            },
            otherFileList: [],
            ccerList: <CcerItem[]>[],
            fileList: []
        })

        onMounted(() => {
        })
        watch(
            () => props.signRuId,
            (val) => {
                if (val) {
                    // getRuInfo()
                }
            }
        )

        async function getRuInfo() {
            let { result } = await Api.getDocInfoByRuId({ signRuId: props.signRuId })
            if (result) {
                detailInfo.value = {
                    ...result,
                }
            }
        }
        return {
            detailInfo,
            loadRuStatus,
            getRuInfo
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
</style>
