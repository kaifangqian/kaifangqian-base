/**
 * @description : axios封装
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
 * 必须公开修改后的完整源代码（包括衍生作品），详见协议全文。
 */
/* eslint-disable @typescript-eslint/no-unused-vars */
/* eslint-disable @typescript-eslint/no-explicit-any */
import service, { AxiosRequestConfig } from './axios';
export * from './types';

export const request = <T = any>(config: AxiosRequestConfig): Promise<T> => {
  return new Promise((resolve, reject) => {
    service
      .request(config)
      .then((res) => {
        // 一些业务处理
        resolve(res.data);
      })
      .catch((err) => {
        console.log('request fail:', err);
      });
  });
};

const http = {
  get<T = any>(url: string, params = {}, config?: AxiosRequestConfig): Promise<T> {
    return request({ url, params, ...config, method: 'GET' });
  },
  post<T = any>(url: string, data = {}, config?: AxiosRequestConfig): Promise<T> {
    return request({ url, data, ...config, method: 'POST' });
  },
  put<T = any>(url: string, data = {}, config?: AxiosRequestConfig): Promise<T> {
    return request({ url, data, ...config, method: 'PUT' });
  },
  delete<T = any>(url: string, data = {}, config?: AxiosRequestConfig): Promise<T> {
    return request({ url, data, ...config, method: 'DELETE' });
  },
  // 上传文件，指定 'Content-Type': 'multipart/form-data'
  upload<T = any>(url: string, data={}, config?: AxiosRequestConfig): Promise<T> {
	  
    return request({
		...config,
      url,
      data,
      method: 'POST',
      // headers: { 'Content-Type': 'multipart/form-data' },
    });
  },
};

export default http;
