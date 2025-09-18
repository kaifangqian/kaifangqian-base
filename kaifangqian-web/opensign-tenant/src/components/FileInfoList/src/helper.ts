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

export function checkFileType(file: File, accepts: string[]) {
  const newTypes = accepts.join('|');
  // const reg = /\.(jpg|jpeg|png|gif|txt|doc|docx|xls|xlsx|xml)$/i;
  const reg = new RegExp('\\.(' + newTypes + ')$', 'i');

  return reg.test(file.name);
}

export function checkImgType(file: File) {
  return isImgTypeByName(file.name);
}

export function isImgTypeByName(name: string) {
  return /\.(jpg|jpeg|png|gif)$/i.test(name);
}

export function getBase64WithFile(file: File) {
  return new Promise<{
    result: string;
    file: File;
  }>((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve({ result: reader.result as string, file });
    reader.onerror = (error) => reject(error);
  });
}

export function loadFileType(type){
  let fileIcon:string = 'otherfile';
  if(['png','jpg','png','gif','bmp','psd','tif','jfif','webp'].includes(type)){
    fileIcon = 'image'
  }
  if(['mp3','flac','ape','wma','wav','aac','m4a','au','ram','mmf','aif','alac','wavpack','ogg','vorbis','opus'].includes(type)){
    fileIcon = 'mp'
  }
  if(['mkv','mp4','avi','swf','wmv','rmvb','mov','mpg','flv','f4v'].includes(type)){
    fileIcon = 'video'
  }
  if(['doc','docx','ppt','pptx','wps'].includes(type)){
    fileIcon = 'word'
  }
  if(['xls','xlsx'].includes(type)){
    fileIcon = 'excel'
  }
  if(['pdf'].includes(type)){
    fileIcon = 'pdf'
  }
  if(['rar','zip'].includes(type)){
    fileIcon = 'zip'
  }
  return fileIcon
}