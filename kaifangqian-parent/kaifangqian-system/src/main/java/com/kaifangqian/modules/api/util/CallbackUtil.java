/**
 * @description 回调工具类
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
package com.kaifangqian.modules.api.util;

import com.kaifangqian.modules.api.entity.ApiCallbackRecord;
import com.kaifangqian.modules.api.service.IApiCallbackRecordService;
import com.kaifangqian.modules.system.util.HttpUtil;
import com.kaifangqian.common.constant.ApiConstants;
import com.kaifangqian.common.util.SpringContextUtils;
import com.kaifangqian.utils.MyStringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : zhenghuihan
 * create at:  2024/3/26  10:36
 * @description: 回调工具类
 */
@Slf4j
public class CallbackUtil {

    /**
     * @create by zhenghuihan
     * @createTime 2024/3/26 11:22
     * @description 处理消息
     */
    public static boolean callback(String callbackId, Integer callbackNum, String url, String body, String privateKey) {
        //发送次数可设置
        IApiCallbackRecordService apiCallbackRecordService = (IApiCallbackRecordService) SpringContextUtils.getBean("apiCallbackRecordServiceImpl");
        boolean flag = false;
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("sign", ApiSignature.sign(body, ApiConstants.CHARSET_UTF8, privateKey, ApiConstants.SIGN_TYPE_RSA2));
        for (int retry = 1; retry <= callbackNum; retry++) {
            ApiCallbackRecord record = new ApiCallbackRecord();
            try {
                log.info("第" + retry + "次回调");
                String result = HttpUtil.sendPost(url, headers, body);
                record.setCallbackId(callbackId);
                record.setReqPara(body);
                if (MyStringUtils.isNotBlank(result) && result.length() > 1024) {
                    record.setResPara(result.substring(0, 1024));
                } else {
                    record.setResPara(result);
                }

                apiCallbackRecordService.save(record);
                if (MyStringUtils.isNotBlank(result) && "success".equals(result.toLowerCase())) {
                    log.info("第" + retry + "次回调成功");
                    flag = true;
                    break;
                }
            } catch (Exception e) {
//                e.printStackTrace();
                log.info("CallbackUtil回调失败");
                if (MyStringUtils.isBlank(record.getResPara()) && MyStringUtils.isNotBlank(e.getMessage())) {
                    if(e.getMessage().length() > 1024){
                        record.setResPara(e.getMessage().substring(0, 1024));
                    }else {
                        record.setResPara(e.getMessage());
                    }
                    log.info("CallbackUtil回调失败详细信息：" + record.getResPara());
                }
                apiCallbackRecordService.save(record);
                log.info("回调失败. 重试...");
                if (retry == callbackNum || retry > callbackNum) {
                    log.info("回调达到最大重试次数. 回调失败.");
                }
            }
        }

        log.info("CallbackUtil返回了，flag：" + flag);
        return flag;
    }
}