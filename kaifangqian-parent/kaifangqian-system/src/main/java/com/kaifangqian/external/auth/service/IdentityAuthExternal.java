/**
 * @description 用户授权开通电子签服务
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
package com.kaifangqian.external.auth.service;

import com.kaifangqian.external.auth.request.AuthCallbackRequest;
import com.kaifangqian.external.auth.request.AuthOrderInfoRequest;
import com.kaifangqian.external.auth.response.IdentityAuthInfoForGetResponse;
import com.kaifangqian.external.auth.response.IdentityAuthInfoForQueryResponse;
import com.kaifangqian.external.auth.response.IdentityAuthResponse;
import com.kaifangqian.external.base.CommonRequest;
import com.kaifangqian.external.base.CommonResult;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.external.auth.request.*;

/**
 * @author : yxb
 * create at: 2025/6/6
 */
public interface IdentityAuthExternal {

    /**
     * 获取个人实名认证链接地址
     * @return
     */
    IdentityAuthResponse personalIdentityAuth(String callbackPage) throws Exception;

    /**
     * 查询实名认证信息
     * @return
     */
    Result<?> queryIdentityAuthInfo(String orderNo) throws Exception;

    /**
     * 查询实名认证信息
     * @return
     */
    Result<?> getIdentityAuthInfo(String unionId) throws Exception;

    /**
     * 获取个人实名认证变更链接地址
     * @return
     */
    IdentityAuthResponse personalIdentityAuthUpdate(String callbackPage) throws Exception;


    /**
     * 获取企业实名认证链接地址
     * @param callbackPage
     * @return
     */
    IdentityAuthResponse companyIdentityAuth(String callbackPage) throws Exception;

    /**
     * 获取企业实名认证变更链接地址
     * @param callbackPage
     * @return
     */
    IdentityAuthResponse companyIdentityAuthUpdate(String callbackPage) throws Exception;


    /**
     * 查询实名认证信息
     * @param request
     * @return
     */
    CommonResult<IdentityAuthInfoForQueryResponse> queryIdentityAuthInfo(CommonRequest<AuthOrderInfoRequest> request);

    /**
     * 获取实名认证信息
     * @param request
     * @return
     */
    CommonResult<IdentityAuthInfoForGetResponse> getIdentityAuthInfo(CommonRequest<AuthOrderInfoRequest>  request);


    /**
     * 更新个人实名认证
     * @param request
     * @return
     */
    Result<?> updateIdentityAuth(AuthCallbackRequest request) throws Exception;



}
