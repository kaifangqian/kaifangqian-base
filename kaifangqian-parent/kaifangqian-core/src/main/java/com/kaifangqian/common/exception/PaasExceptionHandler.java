/**
 * @description 异常处理器
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
package com.kaifangqian.common.exception;

import com.kaifangqian.common.api.CommonAPI;
import com.kaifangqian.common.constant.ApiConstants;
import com.kaifangqian.common.constant.PaasCommonConstans;
import com.kaifangqian.common.enums.OperateLogStatus;
import com.kaifangqian.common.enums.WarningLogLevel;
import com.kaifangqian.common.enums.WarningLogType;
import com.kaifangqian.common.system.vo.ApiCommonRes;
import com.kaifangqian.common.vo.ErrorLogInfo;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.enums.MonitorType;
import com.kaifangqian.exception.ControlException;
import com.kaifangqian.exception.OverStepDataException;
import com.kaifangqian.exception.OverStepOPException;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.processors.MonitorProcessor;
import com.kaifangqian.utils.MyStringUtils;
import com.kaifangqian.utils.ThrowableUtil;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.connection.PoolException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : zhenghuihan
 * create at: 2022/6/6
 */
@RestControllerAdvice
@Slf4j
public class PaasExceptionHandler {

    @Autowired
    private MonitorProcessor monitorProcessor;
    @Autowired
    private CommonAPI commonAPI;


    @ExceptionHandler(PaasException.class)
    public Object handlePaasException(PaasException e, HttpServletRequest request) {
        log.error(ThrowableUtil.getStackTrace(e));
        Object attr = request.getAttribute(ApiConstants.FROM_TYPE);
        if (attr != null && attr.equals(ApiConstants.FROM_API)) {
            ApiCommonRes res = ApiCommonRes.of(e.getCode(), e.getMessage());
            commonAPI.recordNormalRes(res.toString());
            return res;
        } else {
            Result result = Result.error(e.getMessage());
            addLogToRequest(request, result, e);
            return result;
        }
    }

    @ExceptionHandler(Paas401Exception.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<?> handlePaas401Exception(Paas401Exception e, HttpServletRequest request) {
        log.error(ThrowableUtil.getStackTrace(e));
        Result result = new Result(401, e.getMessage());
        addLogToRequest(request, result, e);
        return result;
    }

    @ExceptionHandler(PaasNoAuthException.class)
    public Result<?> paasNoAuthException(PaasNoAuthException e, HttpServletRequest request) {
        log.error(ThrowableUtil.getStackTrace(e));
        Result result = new Result(550, e.getMessage());
        addLogToRequest(request, result, e);
        return result;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public Result<?> handlerNoFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        log.error(ThrowableUtil.getStackTrace(e));
        Result result = Result.error(404, "路径不存在，请检查路径是否正确");
        addLogToRequest(request, result, e);
        return result;
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Result<?> handleDuplicateKeyException(DuplicateKeyException e, HttpServletRequest request) {
        log.error(ThrowableUtil.getStackTrace(e));
        Result result = Result.error("数据库中已存在该记录");
        addLogToRequest(request, result, e);
        return result;
    }

    @ExceptionHandler({UnauthorizedException.class, AuthorizationException.class})
    public Result<?> handleAuthorizationException(AuthorizationException e, HttpServletRequest request) {
        log.error(ThrowableUtil.getStackTrace(e));
        Result result = Result.noauth("没有权限，请联系管理员授权");
        addLogToRequest(request, result, e);
        return result;
    }

    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e, HttpServletRequest request) {
        log.error(ThrowableUtil.getStackTrace(e));
        Result result = Result.error("操作失败，" + e.getMessage());
        addLogToRequest(request, result, e);
        return result;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<?> HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        StringBuffer sb = new StringBuffer();
        sb.append("不支持");
        sb.append(e.getMethod());
        sb.append("请求方法，");
        sb.append("支持以下");
        String[] methods = e.getSupportedMethods();
        if (methods != null) {
            for (String str : methods) {
                sb.append(str);
                sb.append("、");
            }
        }
        log.error(ThrowableUtil.getStackTrace(e));
        Result result = Result.error(405, sb.toString());
        addLogToRequest(request, result, e);
        return Result.error(405, sb.toString());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e, HttpServletRequest request) {
        log.error(ThrowableUtil.getStackTrace(e));
        Result result = Result.error("文件大小超出10MB限制, 请压缩或降低文件质量! ");
        addLogToRequest(request, result, e);
        return result;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public Result<?> handleDataIntegrityViolationException(DataIntegrityViolationException e, HttpServletRequest request) {
        log.error(ThrowableUtil.getStackTrace(e));
        Result result = Result.error("字段太长,超出数据库字段的长度");
        addLogToRequest(request, result, e);
        return result;
    }

    @ExceptionHandler(PoolException.class)
    public Result<?> handlePoolException(PoolException e, HttpServletRequest request) {
        log.error(ThrowableUtil.getStackTrace(e));
        Result result = Result.error("Redis 连接异常!");
        addLogToRequest(request, result, e);
        return result;
    }

    /**
     * 接口控制异常处理
     */
    @ExceptionHandler(ControlException.class)
    public Result<?> controlException(ControlException e, HttpServletRequest request) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        Result result = Result.error(e.getStatus(), e.getControlType().getDesc());
        if (MyStringUtils.isNotBlank(e.getResult())) {
            result.setResult(e.getResult());
        }
        addLogToRequest(request, result, e.getAPIName() + e.getControlType().getDesc(), WarningLogType.WARNING_LOG_TYPE_3, WarningLogLevel.WARNING_LOG_LEVEL_2);
        return result;
    }

    /**
     * 数据越权异常处理
     */
    @ExceptionHandler(OverStepDataException.class)
    public Result<?> overStepDataException(OverStepDataException e, HttpServletRequest request) {
        monitorProcessor.addNum(request.getRequestURI(), request.getMethod(), MonitorType.overstepAPINum, null);
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        Result result = Result.error(e.getStatus(), e.getMessage());
        addLogToRequest(request, result, e.getMessage(), WarningLogType.WARNING_LOG_TYPE_1, WarningLogLevel.WARNING_LOG_LEVEL_2);
        return result;
    }

    /**
     * 功能越权异常处理
     */
    @ExceptionHandler(OverStepOPException.class)
    public Result<?> overStepOPException(OverStepOPException e, HttpServletRequest request) {
        monitorProcessor.addNum(request.getRequestURI(), request.getMethod(), MonitorType.overstepDataNum, null);
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        Result result = Result.error(e.getStatus(), e.getMessage());
        addLogToRequest(request, result, e.getMessage(), WarningLogType.WARNING_LOG_TYPE_1, WarningLogLevel.WARNING_LOG_LEVEL_2);
        return result;
    }

    /**
     * 统一日志处理(异常)
     */
    private void addLogToRequest(HttpServletRequest request, Result result, String warningName, WarningLogType warningType, WarningLogLevel warningLevel) {
        ErrorLogInfo errorLogInfo = new ErrorLogInfo();
        errorLogInfo.setLogStatus(OperateLogStatus.OPERATE_STATUS_WARNING);
        errorLogInfo.setResult(result);
        errorLogInfo.setWarningName(warningName);
        errorLogInfo.setWarningType(warningType);
        errorLogInfo.setWarningLevel(warningLevel);
        request.setAttribute(PaasCommonConstans.ERROR_LOG_KEY, errorLogInfo);
    }

    /**
     * 统一日志处理(错误)
     */
    private void addLogToRequest(HttpServletRequest request, Result result, Exception e) {
        ErrorLogInfo errorLogInfo = new ErrorLogInfo();
        errorLogInfo.setLogStatus(OperateLogStatus.OPERATE_STATUS_ERROR);
        errorLogInfo.setResult(result);
        errorLogInfo.setErrorMessage(e.getMessage());
        errorLogInfo.setErrorStackInfo(ThrowableUtil.getStackTrace(e));
        request.setAttribute(PaasCommonConstans.ERROR_LOG_KEY, errorLogInfo);
    }

}
