<!--
  @description fileList

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
    <div class="container-file">
        <div v-for="(item, index) in  fileList " :key="index" class="file-item">
            <div class="file-title" v-if="item">
                <SvgIcon :name="loadFileType((item.name || item.realName).split('.')[1])" size="28"></SvgIcon>
                <p class="file-name">{{ item.name || item.realName }}</p>
            </div>
            <div class="file-action">
                <a href="javascript:;" @click="handleDownload({ ...item, id: item.annexId })" class="download-icon">
                    <SvgIcon name="download" size="20" color="#999"></SvgIcon>
                </a>
            </div>
        </div>

    </div>
</template>
  
<script lang="ts">
import { ref, defineComponent } from "vue";
import session from "@/utils/cache/session";
import { APP_TOKEN } from '@/utils/cache/constant';
import Api from '@/api/system';

import { checkImgType, loadFileType } from './helper';


interface FileItem {
    fileName?: string;
    realName?: string;
    id?: string;
    fileType?: string;
    [x: string]: any;

}
export default defineComponent({
    name: "FileList",
    components: {
    },
    props: {
        fileList: {
            type: Array<FileItem>,
            default: () => {
                return <FileItem[]>[]
            }
        }
    },
    setup(props) {
        const data = ref('');

        function handleDownload(record: any) {

            const token = session.getItem(APP_TOKEN)
            const req = new XMLHttpRequest();
            req.open('GET', window.appInfo.mobile_app_info.url + '/resrun-paas/file/downloadFileStream/' + record.id, true);
            req.responseType = 'blob';
            req.setRequestHeader('X-Access-Token', token);
            req.onload = function () {
                const data = req.response;
                const blob = new Blob([data], { type: 'application/octet-stream' });
                let fileName = '';
                if (req?.getResponseHeader('Filename')) {
                    fileName = decodeURI(req?.getResponseHeader('Filename') as string)
                } else {
                    fileName = decodeURI(req?.getResponseHeader('content-disposition')?.split(';')[1].split('=')[1])
                }
                let dom = document.createElement('a')
                let url = window.URL.createObjectURL(blob)
                dom.href = url
                dom.download = decodeURI(record.realName || fileName)
                dom.style.display = 'none'
                document.body.appendChild(dom)
                dom.click()
                dom && dom?.parentNode?.removeChild(dom)
                window.URL.revokeObjectURL(url)
            }
            req.send();
        }

        async function loadImgBase64(id: string) {
            let { result } = await Api.getImgBase64({ imgId: id });
            return result.image
        }
        return {
            data,
            handleDownload,
            checkImgType,
            loadFileType,
            loadImgBase64,
        }
    }
})
</script>
  
<style lang="less" scoped>
.container-file {
    cursor: pointer;
    padding: 5px 5px 5px 15px;
    border-radius: 4px;
    display: flex;
    justify-content: space-between;
    flex-wrap: wrap;

    .file-item {
        display: flex;
        align-items: center;
        border: 1px solid #d9d9d9;
        padding: 6px 15px;
        width: 400px;
        justify-content: space-between;
        margin-right: 20px;
        border-radius: 4px;
        margin-bottom: 10px;

        .file-action {
            display: flex;
            align-items: center;
        }

        .thumb {
            margin-right: 15px;
        }

        .resrun-svg-icon {
            margin-right: 15px;
        }

        .download-icon {}

        .preview-icon {
            display: none;
        }

        &:hover .download-icon {
            display: block;
        }

        &:hover .preview-icon {
            display: block;
        }
    }

}

.file-title {
    display: flex;
    align-items: center;

    // P {
    //     margin-bottom: 5px;
    //     line-height: 1;
    // }

    .file-size {
        color: #a09c9c;
    }
}

.file-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    // border-bottom: 1px solid #e4e4e4;
    padding-bottom: 2px;
    // background: #e4e4e4;

}

.file-name {
    max-width: 200px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    margin-left: 10px;
    font-size: 24px;
}
</style>
  