/**
 * @description 用户意愿校验接口类，短信验证码、密码、人脸识别等验证
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
package com.kaifangqian.modules.opensign.service.confirm;

import com.kaifangqian.modules.opensign.service.business.vo.SignReportSignSubVO;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.modules.opensign.vo.base.UserConfirmByCodeVO;
import com.kaifangqian.modules.opensign.vo.base.UserConfirmByPasswordVO;

import java.util.Map;

/**
 * @author : zhenghuihan
 * create at:  2024/4/2  15:14
 * @description:
 */
public interface IUserConfirmService {

    String sendMessage(String orderNo);

    String sendEmail(String orderNo);

    Result<?> confirmByPhoneEmail(UserConfirmByCodeVO vo);

    Result<?> confirmByPassword(UserConfirmByPasswordVO vo);

    String getFaceUrl(String orderNo, String redirectUrl);

    Boolean checkSign(String orderNo, String taskId, String operateType);

    String queryAndRecord(String httpType, String url, Map<String, String> headers, Map<String, String> params, String requestBody);

    void getQCloudToken();

    boolean updateFaceResult(String orderNo);

    void bindFaceFile();

    String getMyConfirmType();

    SignReportSignSubVO getConfirmInfoByTaskId(String taskId);

    String getFaceResult(String orderNo, String getFile);
}