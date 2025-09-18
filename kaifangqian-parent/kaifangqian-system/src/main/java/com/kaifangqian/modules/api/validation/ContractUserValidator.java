/**
 * @description API签署用户验证
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

import com.kaifangqian.modules.api.anno.ContractUserValid;
import com.kaifangqian.modules.api.vo.base.ContractUser;

import javax.validation.*;
import java.util.Set;

/**
 * @Description: ContractUserValidator
 * @Package: com.kaifangqian.modules.api.validation
 * @ClassName: ContractUserValidator
 * @author: FengLai_Gong
 * @Date: 2025/5/23
 */
public class ContractUserValidator implements ConstraintValidator<ContractUserValid, ContractUser> {


    @Override
    public void initialize(ContractUserValid constraintAnnotation) {

    }

    @Override
    public boolean isValid(ContractUser contractUser, ConstraintValidatorContext context) {
        if(contractUser == null){
            return true ;
        }
        String s = LocalData.THREAD_LOCAL.get();
        System.out.println(s);

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        //验证公共参数
        Set<ConstraintViolation<ContractUser>> publicParamErrorSet = validator.validate(contractUser, ValidationSorts.Sort.class);
        if(publicParamErrorSet.size() > 0){
            ConstraintViolation<ContractUser> publicParamError = publicParamErrorSet.stream().findFirst().get();
            //获取当前异常参数的注解

            String path = publicParamError.getPropertyPath().toString();
            System.out.println(path);

            Object errorAnnotation = publicParamError.getConstraintDescriptor().getAnnotation();
            System.out.println(errorAnnotation);

            String message = publicParamError.getMessage();
            System.out.println(message);
        }
        LocalData.THREAD_LOCAL.remove();
        return false;
    }
}