<!--
  @description 文档

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
    <Loading :loading="loading" text="文档加载中..." />
    <div class="contract-header">
        <div class="doc-list">
            <van-dropdown-menu>
                <van-dropdown-item v-model="docId" :options="docs" placeholder="请选择" @change="handleDocChange" />
            </van-dropdown-menu>
        </div>
        <div class="doc-operation">
            <!-- <van-space>
                <a href="javascript:;" @click="handleRuDownload({ ruDocIdList: docId, signRuId:signRuId})">下载</a>
            </van-space> -->
        </div>
    </div>
    <div class="doc-content" id="doc-content">
        <div class="document-list" :style="['position: relative;' +
                    // 'height: '+ (nowDoc.imageAllHeight) +'px;',
                    'width:'+ nowDoc.maxWidth + 'px;',
                    'transform:scale('+ zoom / 100 +')',
                    'transform-origin: 0% 0%'
            ]">
            <template v-for="item in nowDoc.images">
                <div class="doc-page" :style="[
                        'transform: translate('+ '-50%,'+(item.target.transformHeight + (item.page+1) * 16)+'px);',
                        'width:' + item.target.width +'px;',
                        'height:' + item.target.height +'px;',
                    ]">
                    <img  style="width:100%" v-lazy="baseUrl + '/file/downloadFileStream/' + item.annexId" />
                    <!-- 页面编号角标 -->
                    <div class="page-corner">
                    </div>
                    <div class="page-num">
                    {{ item.page + 1 }}
                    </div>
                </div>
            </template>
        </div>
    </div>
</div>
</template>

<script lang="ts">
import {
    ref,
    defineComponent,
    onMounted,
    watch
} from "vue";
import {
    handleRuDownload
} from '@/utils/util';
import {
    useRoute
} from 'vue-router';
import Api from '@/api/contract/index';

import {
    getCanvasZoom,
} from "@/utils/ControlData";

import {
    pageScaling
} from '@/pages/components/data/ControlerMoveRange'

export default defineComponent({
    name: "Document",
    setup() {
        const docs = ref([]);
        const annexId = ref('')
        const loading = ref(false)
        const nowDoc: any = ref({
            maxWidth:0,
        });
        const route = useRoute();
        const signRuId = route.query.signRuId;
        const docId = ref(route.query.docId);
        const clientWidth = ref(document.body.clientWidth);
        // const baseUrl = window.appInfo.mobile_app_info.url + '/resrun-paas';
        let baseUrl =
            import.meta.env.VITE_APP_API_BASE_URL;

        const CanvasZoomBase = ref(getCanvasZoom(clientWidth.value - 20, 100));

        watch(
            () => nowDoc && nowDoc.maxWidth,
            (val) => {
                if (val) {
                    zoom.value = Math.round((window.innerWidth - 20) / val * 100)
                }
            }, {
                deep: true
            }
        )
        const zoom = ref(100);

        onMounted(() => {
            init()
        })

        // 获取文档
        async function init() {
            loading.value = true;
            let {
                result
            } = await Api.getDocFiles({
                signRuId: signRuId
            })
            if (result && result.length) {
                docs.value = result;
                docs.value.map((v: any) => {
                    v.text = v.name;
                    v.value = v.id;
                })
                annexId.value = docs.value.filter((v: any) => v.id == docId.value)[0].annexId;
                getDocImg()
            }

        }

        //获取图片
        async function getDocImg() {
            let {
                result
            } = await Api.getDocImgsInSign({
                signFileId: docId.value
            })
            if (result) {
                let images = result.sort((a, b) => a.page - b.page)
                const {
                    targets,
                    maxWidth
                } = pageScaling(images);
                nowDoc.value.images = images;
                nowDoc.value.pageSize = result.length;
                nowDoc.value.targets = targets;
                nowDoc.value.maxWidth = maxWidth;
                zoom.value = Math.round((window.innerWidth - 20) / maxWidth * 100)
                nowDoc.value.imageAllHeight = targets[targets.length - 1].transformHeight + targets[targets.length - 1].height + (result.length * 16);
                loading.value = false;
            }
        }
        //切换文档
        async function handleDocChange(val: any) {
            docId.value = val;
            nowDoc.value.images = [];
            loading.value = true;
            annexId.value = docs.value.filter((v: any) => v.id == docId.value)[0].annexId;
            let {
                result
            } = await Api.getDocImgsInSign({
                signFileId: docId.value
            });
            if (result) {
                let images = result.sort((a, b) => a.page - b.page)
                const {
                    targets,
                    maxWidth
                } = pageScaling(images);
                nowDoc.value.images = images;
                nowDoc.value.pageSize = result.length;
                nowDoc.value.targets = targets;
                nowDoc.value.maxWidth = maxWidth;
                zoom.value = Math.round((window.innerWidth - 20) / maxWidth * 100)
                nowDoc.value.imageAllHeight = targets[targets.length - 1].transformHeight + targets[targets.length - 1].height + (result.length * 16);
                loading.value = false;
                setTimeout(() => {
                    loading.value = false;
                }, 500)
            }

        }

        return {
            docId,
            docs,
            handleDocChange,
            handleRuDownload,
            annexId,
            signRuId,
            nowDoc,
            baseUrl,
            CanvasZoomBase,
            loading,
            zoom,
        }
    }
})
</script>

<style lang="less" scoped>
.doc-preview-container{
    height:100%;
}
.contract-header {
    display: flex;
    border-bottom: 1px solid #efefef;
    padding: 10px;
    background-color: #fff;
    height: 72px;
    position: absolute;
    z-index: 10;
    top: 92px;
    left: 0;
    right: 0;

    :deep(.van-dropdown-menu__bar) {
        height: 100%;
    }

    .doc-list {
        flex: 1;
		display: flex;
		align-items: center;
		justify-content: center;
        // position: relative;
        :deep(.van-dropdown-menu) {
            width: 100%;
        }
    }

}

.doc-content {
    width: 100%;
    height:100%;
    // height: calc(100% - 2.66667rem);
    overflow: auto;
    overflow-x: hidden;
    padding-top: 25px;
    .doc-page{
        margin-top:32px;
        width: 100%;
        position: absolute;
        top: 0;
        left: 50%;
        border: 1px solid #ccc;
        box-sizing: border-box;
        box-shadow: 0px 2px 4px -2px rgba(0, 0, 0, 0.1),0px 4px 6px -1px rgba(0, 0, 0, 0.1);
    }
}

:deep(.van-button) {
    border: none;
}

.page-corner {
    position: absolute;
    right: 0px;
    bottom: 0px;
    width: 0;
    height: 0;
    border-color: #ccc transparent;
    border-width: 0 0 1.5rem 1.5rem;
    border-style: solid;
    }

  .page-num {
    position: absolute;
    bottom: 5px;
    right: 0.2rem;
    font-size:36px;
    color: #fff;
    // padding-right: 20px;
  }
</style>
