/**
 * @description API接口参数验证
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
package com.kaifangqian.modules.api.validation;

import com.kaifangqian.common.constant.ApiCode;
import com.kaifangqian.modules.api.base.ReqBaseVO;
import com.kaifangqian.modules.api.exception.RequestParamsException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.text.MessageFormat;
import java.util.Set;

/**
 * @Description: ParamValidatation
 * @Package: com.kaifangqian.modules.api.validation
 * @ClassName: ParamValidatation
 * @author: FengLai_Gong
 * @Date: 2018/5/23 14:05
 */
public class ParamValidation {


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
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        //验证公共参数
        Set<ConstraintViolation<ReqBaseVO>> publicParamResult = validator.validate(req, ValidationSorts.Sort.class);
        if (publicParamResult.size() > 0) {
            //获取第一个异常参数并抛出异常
            ConstraintViolation<ReqBaseVO> publicParamError = publicParamResult.stream().findFirst().get();
            throwError(req.getAppAuthToken(), req.getOperatorAccount(), req.getUniqueCode(), req.toString(), publicParamError.getMessage(), publicParamError.getPropertyPath().toString());
        }
    }



}