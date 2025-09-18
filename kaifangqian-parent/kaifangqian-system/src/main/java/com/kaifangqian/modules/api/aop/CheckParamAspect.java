/**
 * @description 校验请求参数格式
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
package com.kaifangqian.modules.api.aop;

import com.kaifangqian.modules.api.exception.RequestParamsException;
import com.kaifangqian.modules.api.validation.ValidationSorts;
import com.kaifangqian.common.constant.ApiCode;
import com.kaifangqian.modules.api.base.ReqBaseVO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.text.MessageFormat;
import java.util.Set;
/**
 * create by zhenghuihan at 2022/3/16
 * @description 处理签署线程本地变量
 */
@Slf4j
@Aspect
@Order(value = 1)
@Component
public class CheckParamAspect {

    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    /**
     * 切入点
     * 根据包名和Controller类目进行切入
     */
    @Pointcut("execution(public * com.kaifangqian.modules.api.controller.*Controller.*(..))")
    public void check() {
    }

    /**
     * @param point
     * @return void
     * @Description 方法执行前切入   对请求参数进行验证
     **/
    @Before("check()")
    public void beforeCheck(JoinPoint point) {

        //获取Servlet  request对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        //获取请求参数
        Object[] args = point.getArgs();
        for (Object param : args) {
            if (param instanceof ReqBaseVO) {
                ReqBaseVO apiRequest = (ReqBaseVO) param;
                checkPublicParams(apiRequest);
            }
        }
    }

    /**
     * @param errorMessage 错误信息
     * @param args         错误的字段名称和其他附加参数
     * @return void
     * @Description 格式化统一返回信息   并抛出异常
     **/
    public static void throwError(String token, String operatorAccount, String uniqueCode, String reqPara, String errorMessage, String... args) {
        //根据错误异常编码获取异常枚举
        Integer code = Integer.valueOf(errorMessage);
        ApiCode apiCode = ApiCode.statOf(code);
        //根据错误的字段名   格式化异常信息
        String message = MessageFormat.format(apiCode.getTemplate(), args);
        throw new RequestParamsException(token, operatorAccount, uniqueCode, reqPara, apiCode, message);
    }


    /**
     * @param req
     * @return void
     * @Description POST请求参数校验
     **/
    public void checkPublicParams(ReqBaseVO req) {
        Validator validator = validatorFactory.getValidator();
        //验证公共参数
        Set<ConstraintViolation<ReqBaseVO>> publicParamResult = validator.validate(req, ValidationSorts.Sort.class);
        if (publicParamResult.size() > 0) {
            //获取第一个异常参数并抛出异常
            ConstraintViolation<ReqBaseVO> publicParamError = publicParamResult.stream().findFirst().get();
            throwError(req.getAppAuthToken(), req.getOperatorAccount(), req.getUniqueCode(), req.toString(), publicParamError.getMessage(), publicParamError.getPropertyPath().toString());
        }
    }
}
