<!--
  @description 签署文档列表

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
    <div class="doc-page">
        <!-- <div class="doc-list-search flex-row">
            <div class="flex-row doc-search-left">
                <image src="" alt="公司logo" />
                <van-dropdown-menu>
                    <van-dropdown-item v-model="tenantId" :options="columns" placeholder="请选择" />
                </van-dropdown-menu>
            </div>
            <svg t="1706719411713" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
                p-id="5958" width="18" height="18">
                <path
                    d="M978.94584 85.542289H40.488129C16.93709 84.006352-0.982179 64.039166 0.041779 40.488127 1.065737 18.473026 18.473027 1.065736 40.488129 0.041777h938.457711c23.551039-1.023958 43.518224 16.895311 45.054162 40.44635s-16.895311 43.518224-40.44635 45.054162h-4.607812zM40.488129 426.520377h85.500512c23.551039 1.023958 41.470308 21.503123 40.446349 45.054162-1.023958 22.015102-18.431248 39.422392-40.446349 40.44635H40.488129c-23.551039-1.023958-41.470308-21.503123-40.44635-45.054162 1.023958-21.503123 18.431248-38.910412 40.44635-40.44635z m0 511.979111h255.989556c23.551039 1.023958 41.470308 21.503123 40.446349 45.054162-1.023958 22.015102-18.431248 39.422392-40.446349 40.44635h-255.989556c-23.551039-1.023958-41.470308-21.503123-40.44635-45.054162 1.023958-21.503123 18.431248-38.910412 40.44635-40.44635z m498.155675-753.121272c203.255707-55.293744 413.167143 65.021347 467.948908 268.277054 37.374475 136.698423-4.607812 283.124449-108.539572 379.376522l108.027593 108.027592c18.431248 18.431248 18.431248 48.126036 0 66.557284-18.431248 18.431248-48.126036 18.431248-66.557285 0l-118.267174-118.267174c-181.240605 103.93176-412.655164 40.958329-516.074944-140.282277-16.895311-29.694788-29.694788-61.437493-38.398434-94.204156C213.025089 450.583395 334.364139 240.67196 538.643804 185.378216z m-188.920292 447.981722c43.006245 159.225504 206.327582 252.917681 365.553086 210.423415s252.917681-206.327582 210.423414-365.553086c-42.494266-158.713524-205.815603-252.917681-364.529127-210.423414-158.713524 41.470308-253.42966 204.279665-211.959352 362.99319 0 0.511979 0.511979 1.535937 0.511979 2.559895z"
                    fill="#C0C4CC" p-id="5959"></path>
            </svg>
        </div> -->
		<SwitchIdentity>
				<svg t="1706719411713" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
				    p-id="5958" width="0.5rem" height="0.5rem">
				    <path
				        d="M978.94584 85.542289H40.488129C16.93709 84.006352-0.982179 64.039166 0.041779 40.488127 1.065737 18.473026 18.473027 1.065736 40.488129 0.041777h938.457711c23.551039-1.023958 43.518224 16.895311 45.054162 40.44635s-16.895311 43.518224-40.44635 45.054162h-4.607812zM40.488129 426.520377h85.500512c23.551039 1.023958 41.470308 21.503123 40.446349 45.054162-1.023958 22.015102-18.431248 39.422392-40.446349 40.44635H40.488129c-23.551039-1.023958-41.470308-21.503123-40.44635-45.054162 1.023958-21.503123 18.431248-38.910412 40.44635-40.44635z m0 511.979111h255.989556c23.551039 1.023958 41.470308 21.503123 40.446349 45.054162-1.023958 22.015102-18.431248 39.422392-40.446349 40.44635h-255.989556c-23.551039-1.023958-41.470308-21.503123-40.44635-45.054162 1.023958-21.503123 18.431248-38.910412 40.44635-40.44635z m498.155675-753.121272c203.255707-55.293744 413.167143 65.021347 467.948908 268.277054 37.374475 136.698423-4.607812 283.124449-108.539572 379.376522l108.027593 108.027592c18.431248 18.431248 18.431248 48.126036 0 66.557284-18.431248 18.431248-48.126036 18.431248-66.557285 0l-118.267174-118.267174c-181.240605 103.93176-412.655164 40.958329-516.074944-140.282277-16.895311-29.694788-29.694788-61.437493-38.398434-94.204156C213.025089 450.583395 334.364139 240.67196 538.643804 185.378216z m-188.920292 447.981722c43.006245 159.225504 206.327582 252.917681 365.553086 210.423415s252.917681-206.327582 210.423414-365.553086c-42.494266-158.713524-205.815603-252.917681-364.529127-210.423414-158.713524 41.470308-253.42966 204.279665-211.959352 362.99319 0 0.511979 0.511979 1.535937 0.511979 2.559895z"
				        fill="#C0C4CC" p-id="5959"></path>
				</svg>
		</SwitchIdentity>
        <div class="doc-group">
            <van-tabs v-model:active="activeTabKey" @change="activeTabKeyChange">
                <van-tab title="待我处理" :name="1"></van-tab>
                <van-tab title="全部文档" :name="2"></van-tab>
                <van-tab title="已发送" :name="3"></van-tab>
                <van-tab title="抄送给我" :name="4"></van-tab>
                <van-tab title="已完成" :name="5"></van-tab>
            </van-tabs>
        </div>
        <div class="doc-list">
            <template v-for="item in docList">
                <van-card>
                    <template #title>
                        <div class="doc-header flex-row">
                            <span class="doc-title">{{ item.subject }}</span>
                            <van-tag :color="loadSignColor(item.status)">{{ loadRuStatus(item.status) }}</van-tag>
                        </div>
                    </template>
                    <template #desc>
                        <div class="doc-body" @click="docDetails(item)">
                            <ul>
                                <li>
                                    <span class="doc-label">发送方：</span>
                                    <span class="doc-value">{{ item.fromTenantName }}</span>
                                </li>
                                <li>
                                    <span class="doc-label">发送时间：</span>
                                    <span class="doc-value">{{ item.createTime }}</span>
                                </li>
                                <li>
                                    <span class="doc-label">截止时间：</span>
                                    <span class="doc-value">{{ item.expireDate ? item.expireDate : "未设置" }}</span>
                                </li>
                            </ul>
                        </div>
                    </template>
                </van-card>
            </template>
        </div>
    </div>
</template>

<script  lang="ts">
import { ref, defineComponent, onMounted } from "vue";

import Api from "@/api/contract";
import { loadRuStatus, loadSignColor } from './transform';

import SwitchIdentity from '../components/SwitchIdentity.vue'

export default defineComponent({
    name: 'contractList',
	components:{
		SwitchIdentity
	},
    setup() {
        const router = useRouter();
        const tenantId = ref(0)
        const activeTabKey = ref(1)
        const docList = ref<any>([])
        const columns = ref([
            { text: '北京资源律动', value: 0 },
            { text: '测试公司1', value: 1 },
            { text: '测试公司2', value: 2 },
        ])
        function onConfirm() {

        }
        function onCancel() {

        }
        function onChange() {

        }

        function docDetails(row: any) {
            router.push('/signContract');
        }


        async function initPage() {
            const params = {
                pageNo: 1,
                pageSize: 10
            }
            if (activeTabKey.value == 1) {
                const result = await Api.listMyJob(params);
                docList.value = result.result.records;
            } else if (activeTabKey.value == 2) {
                const result = await Api.listAll(params);
                docList.value = result.result.records;
            } else {
                docList.value = []
            }
        }
        function activeTabKeyChange() {
            initPage();
        }
        onMounted(() => {
            initPage();
        })


        return {
            columns,
            onConfirm,
            onCancel,
            onChange,
            tenantId,
            activeTabKey, activeTabKeyChange,
            docList, loadRuStatus,
            docDetails, loadSignColor
        }
    }
})
</script>

<style lang="less" scoped>
.index {
    :deep(.van-cell) {
        background-color: #ddd;
        margin-top: 16px;
    }
}

.doc-list-search {
    display: flex;
    justify-content: space-between;
    align-items: center;

    svg,
    img {
        display: block;
    }

    .doc-search-left {
        :deep(.van-dropdown-menu) {
            width: 7rem;
            height: 58px;
        }

        :deep(.van-dropdown-menu__item) {
            justify-content: left;
        }


        :deep(.van-dropdown-menu__bar) {
            height: 100%;
        }


    }
}

:deep(.van-tabs__line) {
    background: @blue;
}

:deep(.van-card__bottom) {
    border-top: 1px solid #e4e4e4;
}

// :deep(.van-button) {
//     margin: 0 10px;
//     height: 40px;
// }

.doc-list {
    :deep(.van-card) {
        background: #fff;
        box-shadow: 0 2px 9px 5px rgba(64, 106, 103, 0.13);
        margin: 30px 0;
        padding: 0;

        .doc-header {
            padding: 20px 25px;
            border-bottom: 1px solid #e4e4e4;
            color: #666;

            .doc-title {
                font-size: 28px;
                font-weight: 600;
            }
        }

        .doc-body {
            padding: 20px 25px;

            li {
                margin: 20px 10px;
            }

            .doc-label {
                // font-weight: 600;
                width: 130px;
                display: inline-block;
                white-space: nowrap;
            }

        }





        .doc-footer {
            padding: 25px;
            width: 100%;
            text-align: right;


        }


    }
}
</style>
