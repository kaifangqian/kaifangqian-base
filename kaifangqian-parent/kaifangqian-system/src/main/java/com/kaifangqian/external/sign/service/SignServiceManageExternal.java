/**
 * @description 电子签服务（静默签管理、快捷签管理）业务逻辑接口
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
package com.kaifangqian.external.sign.service;

import com.kaifangqian.external.sign.response.*;
import com.kaifangqian.external.sign.response.*;

/**
 * @author : yxb
 * create at: 2025/6/6
 */
public interface SignServiceManageExternal {

    /**
     * 查询静默签署服务开通情况
     * @return
     */
    SignServiceOpenInfoResponse querySilentInfo() throws Exception;


    /**
     * 查询静默签署服务开通记录
     * @return
     */
    SilentSignServiceInfosResponse querySilentRecord() throws Exception;


    /**
     * 开通静默签署服务
     * @param callbackPage
     * @return
     */
    SilentSignOpenServiceInfoResponse openSilentSignService(String callbackPage) throws Exception;

    /**
     * 关闭静默签署服务
     * @return
     */
    SilentSignOpenServiceInfoResponse closeSilentSignService() throws Exception;

    /**
     * 查询免意愿快捷签署服务开通情况
     * @return
     */
    SignServiceOpenInfoResponse queryFastSignInfo() throws Exception;

    /**
     * 查询免意愿快捷签署服务开通情况
     * @return
     */
    SignServiceOpenInfoResponse queryFastSignInfo(String tenantId) throws Exception;

    /**
     * 查询免意愿快捷签署服务开通记录
     * @return
     */
    FastSignServiceInfosResponse queryFastSignRecord() throws Exception;

    /**
     * 开通免意愿快捷签署服务
     * @param callbackPage
     * @return
     */
    FastSignOpenServiceInfoResponse openFastSignService(String callbackPage) throws Exception;

    /**
     * 关闭免意愿快捷签署服务
     * @return
     */
    FastSignOpenServiceInfoResponse closeFastSignService() throws Exception;

    /**
     * 查询签署应用信息
     * @return
     */
    SignAppInfoResponse querySignAppInfo() throws Exception;
}
