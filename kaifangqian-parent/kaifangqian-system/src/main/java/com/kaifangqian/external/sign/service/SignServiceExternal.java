/**
 * @description 电子签业务逻辑接口
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

import com.kaifangqian.external.sign.request.AutoSignDocumentRequest;
import com.kaifangqian.external.sign.request.SignCallbackRequest;
import com.kaifangqian.external.sign.request.SignOrderRequest;
import com.kaifangqian.external.sign.request.VerifySignDocumentRequest;
import com.kaifangqian.external.sign.response.AuthSignDocumentResponse;
import com.kaifangqian.external.sign.response.AutoSignDocumentResponse;
import com.kaifangqian.external.sign.response.SignOrderServiceInfoResponse;
import com.kaifangqian.external.sign.response.SignOrderStatusInfoResponse;
import com.kaifangqian.common.vo.Result;

/**
 * @author : yxb
 * create at: 2025/6/6
 */
public interface SignServiceExternal {

    /**
     * 获取签署意愿校验链接
     * @return
     */
    SignOrderServiceInfoResponse generateSignOrderAndGetSignAuthUrl(SignOrderRequest signOrderRequest) throws Exception;

    /**
     * 提交文件hash值进行签署--意愿验证签署
     * @return
     */
    AuthSignDocumentResponse submitAuthHashSign(VerifySignDocumentRequest verifySignDocumentRequest) throws Exception;

    /**
     * 提交文件hash值进行签署--静默签署
     * @return
     */
    AutoSignDocumentResponse submitAutoHashSign(AutoSignDocumentRequest autoSignDocumentRequest) throws Exception;

    /**
     * 获取签署订单状态
     * @return
     */
    SignOrderStatusInfoResponse getSignOrderStatus(String orderNo) throws Exception;

    /**
     * 更新签署订单状态，完成签署
     * @param request
     * @throws Exception
     */
    Result<?> updateSignUserConfirmStatus(SignCallbackRequest request) throws Exception;
}
