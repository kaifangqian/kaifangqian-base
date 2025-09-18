<!--
  @description 合同详情页面header

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
    <div class="sign-header-container">
        <van-dropdown-menu>
            <van-dropdown-item v-model="pickerId" :options="options ? options : []" placeholder="请选择" @change="handleChangeDoc"></van-dropdown-item>
        </van-dropdown-menu>
        <div class="doc-info">
            <a href="javascript:;" @click="handleProcess"> 进度</a>
            <a href="javascript:;" @click="handleDetail"> 详情</a>
        </div>
    </div>
</template>

<script lang="ts">
import { ref, defineComponent, watch } from "vue"

interface DocIem {
    name: string;
    text: string;
    [key: string]: string;
}
export default defineComponent({
    name: "SignHeader",
    props: {
        docs: {
            type: Array,
            default: function () {
                return []
            }
        },
        docId: {
            type: String,
            default: ''
        }
    },
    setup(props, { emit }) {
        const pickerId = ref(props.docId);
        const options: any = ref(props.docs);


        watch(
            () => pickerId.value,
            (val) => {
                // pickerId.value = val;
                // emit('docChange', val)
            }
        )
        watch(
            () => props.docId,
            (val) => {
                pickerId.value = val;
            }
        )
        watch(
            () => props.docs,
            (list) => {

                if (list) {
                    list.map((v) => {
                        v.text = v.name;
                        v.value = v.id;
                    });
                    console.log(list, '测试wacth列表')
                    options.value = list;
                }

            },
            {
                deep: true

            }
        )

        function handleProcess() {
            emit('process')
        }
        function handleDetail() {
            emit('detail')
        }
        function handleChangeDoc(val) {
            console.log(val, '测试----文档val')
            emit('docChange', val)
        }

        return {
            pickerId,
            handleProcess,
            handleDetail,
            options,
            handleChangeDoc
        }
    }
})
</script>

<style lang="less" scoped>
.sign-header-container {
    height: 92px;
    position: fixed;
    left: 0;
    background: #fff;
    width: 100%;
    width: 100%;
    top: @header-height;
    right: 0;
    padding-right: 20px;
    box-shadow: 0 0px 4px 4px rgba(64, 106, 103, 0.13);
    display: flex;
    z-index: 999;
    justify-content: space-between;

    :deep(.van-dropdown-menu) {
        width: 400px;
        border: none;
    }

    :deep(.van-dropdown-menu__bar) {
        box-shadow: none;
        height: 100%;

    }

    .doc-info {
        display: flex;
        align-items: center;
        height: 100%;
        position: absolute;
        right: 40px;

        a {
            color: @blue;
            cursor: pointer;
            font-size: 24px;
            margin: 0 20px;

        }
    }


}
</style>
