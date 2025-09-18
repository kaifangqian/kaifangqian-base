/*
 * @description 开放签
 *
 * Copyright (C) [2025] [版权所有者（北京资源律动科技有限公司）]. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * 注意：本代码基于 AGPLv3 协议发布。若通过网络提供服务（如 Web 应用），
 * 必须公开修改后的完整源码（包括衍生作品），详见协议全文。
 */

import { UploadApiResult } from './model/uploadModel';
import { defHttp } from '/@/utils/http/axios';
import { UploadFileParams } from '/#/axios';
import { useGlobSetting } from '/@/hooks/setting';


enum Api {
  ImgBase64 = '/file/downloadFileBase',
  FileStream = '/file/downloadFileStream',
  FileByte = '/file/downloadFileByte',
}


const { uploadUrl = '/file',uploadAvatarUrl } = useGlobSetting();
console.log(uploadUrl,uploadAvatarUrl,'路径上传-----')
/**
 * @description: Upload interface
 */
export function uploadAvatarApi(
  params: UploadFileParams,
  onUploadProgress: (progressEvent: ProgressEvent) => void,
) {
  return defHttp.uploadFile<UploadApiResult>(
    {
      url: uploadAvatarUrl,
      onUploadProgress,
    },
    params,
  );
}
/**
 * @description: Upload interface
 */
export function uploadApi(
  params: UploadFileParams,
  onUploadProgress: (progressEvent: ProgressEvent) => void,
) {
  return defHttp.uploadFile<UploadApiResult>(
    {
      url: uploadUrl,
      onUploadProgress,
    },
    params,
  );
}

export function getImgBase64(params) {
  return defHttp.get({ url: Api.ImgBase64 + '/'+ params.imgId , params });
}

export function getImgStream(params) {
  return defHttp.get({ url: Api.ImgStream + '/'+ params.imgId , params, headers: 
  {
    responseType: 'blob'
  }, });
}


export function getFileStrem(params) {
  return defHttp.get({ url: Api.FileStream + '/'+ params.fileId , params, headers: 
  {
    responseType: 'blob',
    // responseType: 'arraybuffer',
  }, 
});
}

export function getFileArrayBuffer(params) {
  return defHttp.get({ url: Api.FileByte + '/'+ params.fileId , params, headers: 
  {
    responseType: 'blob',
    // responseType: 'arraybuffer',
  }, 
});
}