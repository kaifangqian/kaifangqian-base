<template>
    <div class="sign-header-container">
        <!-- 当只有一个文件时，显示普通文本 -->
        <div v-if="options && options.length === 1" class="single-doc-display">
            {{ singleDocName }}
        </div>
        <!-- 当有多个文件时，显示下拉列表 -->
        <van-dropdown-menu v-else>
            <van-dropdown-item v-model="pickerId" :options="dropdownOptions" placeholder="请选择" @change="handleChangeDoc"></van-dropdown-item>
        </van-dropdown-menu>
        <div class="doc-info">
            <a href="javascript:;" @click="handleProcess"> 进度</a>
            <a href="javascript:;" @click="handleDetail"> 详情</a>
        </div>
    </div>
</template>

<script lang="ts">
import { ref, defineComponent, watch, computed } from "vue"

interface DocItem {
    name: string;
    id: string;
    [key: string]: string;
}

interface DropdownOption {
    text: string;
    value: string;
    [key: string]: string;
}

export default defineComponent({
    name: "SignHeader",
    props: {
        docs: {
            type: Array as () => DocItem[],
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
        const pickerId = ref<string>(props.docId);
        const options = ref<DocItem[]>([]);

        // 计算单个文档的名称
        const singleDocName = computed(() => {
            if (options.value && options.value.length === 1) {
                return options.value[0].name;
            }
            return '';
        });

        // 转换为下拉选项格式
        const dropdownOptions = computed<DropdownOption[]>(() => {
            return options.value.map(item => ({
                text: item.name,
                value: item.id
            }));
        });

        watch(
            () => pickerId.value,
            (val: string) => {
                // pickerId.value = val;
                // emit('docChange', val)
            }
        )
        watch(
            () => props.docId,
            (val: string) => {
                pickerId.value = val;
            }
        )
        watch(
            () => props.docs,
            (list: DocItem[]) => {
                if (list) {
                    console.log(list, '测试watch列表')
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
        function handleChangeDoc(val: string) {
            console.log(val, '测试----文档val')
            emit('docChange', val)
        }

        return {
            pickerId,
            handleProcess,
            handleDetail,
            options,
            dropdownOptions,
            singleDocName,
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
    
    .single-doc-display {
        display: flex;
        align-items: center;
        height: 100%;
        font-size: 28px;
        font-weight: 500;
        padding-left: 30px;
        color: #333;
        // 添加文本省略号样式
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        max-width: 60%;
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