/**
 * @description 接口服务统一异常
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
package com.kaifangqian.modules.api.exception;

import com.kaifangqian.common.constant.ApiCode;
import com.kaifangqian.common.system.vo.ApiCommonRes;
import com.kaifangqian.modules.api.entity.ApiWarningReq;
import com.kaifangqian.modules.api.service.IApiWarningReqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
/**
 * @create by zhenghuihan
 * @createTime 2024/3/21 18:25
 */
@Slf4j
@RestControllerAdvice
public class ServiceExceptionHandler {

    @Autowired
    private IApiWarningReqService apiWarningReqService;

    /**
     * @param e
     * @return com.signature.common.response.BaseResponse
     * @Description 参数校验异常处理
     **/
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiCommonRes validParams(MethodArgumentNotValidException e) {
        //获取异常码
        String errorCode = e.getBindingResult().getFieldError().getDefaultMessage();
        //获取异常字段
        String field = e.getBindingResult().getFieldError().getField();
        //查找对应的异常码枚举
        ApiCode apiCode = ApiCode.statOf(Integer.parseInt(errorCode));
        //根据错误的字段名   格式化异常信息
        String message = MessageFormat.format(apiCode.getTemplate(), field);
        return ApiCommonRes.of(apiCode.getCode(), message);
    }

    @ExceptionHandler({RequestParamsException.class})
    public ApiCommonRes missingRequestParamsException(HttpServletRequest request, HttpServletResponse response, RequestParamsException ex) {
        printExceptionLog(request, ex, true);
        ApiWarningReq req = new ApiWarningReq();
        req.setCode(ex.getApiCode().getCode());
        req.setOperatorAccount(ex.getOperatorAccount());
        req.setToken(ex.getToken());
        req.setReqUrl(request.getRequestURI().toString());
        if(ex.getReqPara() != null){
            String para = cleanData(ex.getReqPara());
            req.setReqPara(para);
        }
        req.setUniqueCode(ex.getUniqueCode());

        apiWarningReqService.recordWarningReq(req);
        return ApiCommonRes.of(ex.getApiCode().getCode(), ex.getMessage());
    }

    public void printExceptionLog(HttpServletRequest request, Exception ex, boolean isPrintError) {
        // 请求Url
        String requestUrl = request.getRequestURI().toString();
        //请求方法名称
        String method = request.getMethod();
        // 异常信息：
        String exceptionMsg = ex.getCause() == null ? ex.getMessage() : ex.getCause().getMessage();
        log.info("统一异常处理：requestUrl = {}, method = {}, exceptionMsg= {}", requestUrl, method, exceptionMsg);
        if (isPrintError) {
            log.info("统一异常处理: {}", ex);
        }
    }

    private String cleanData(String data) {
        // 处理数据，删除或替换换行符和转义符
        String cleanedData = data.replace("\n", "").replace("\r", ""); // 删除换行符
        cleanedData = cleanedData.replace("\\n", "\n").replace("\\r", "\r"); // 还原转义符
        if (cleanedData.length() > 1024) {
            cleanedData = cleanedData.substring(0, 1024);
        }
        return cleanedData;
    }


}
