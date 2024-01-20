package org.resrun.api.validation;

import org.resrun.api.enums.SignTypeEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Description: SignTypeValidator
 * @Package: org.resrun.validation
 * @ClassName: SignTypeValidator
 * @author: FengLai_Gong
 */
public class SignTypeValidator implements ConstraintValidator<SignTypeValid, Integer> {

    @Override
    public boolean isValid(Integer signType, ConstraintValidatorContext context) {
//        if(value == SignTypeEnum.KEYWORDS.getCode() || value == SignTypeEnum.POSITION.getCode()){
//            return true ;
//        }
        if(signType.equals(SignTypeEnum.KEYWORDS.getCode()) || signType.equals(SignTypeEnum.POSITION.getCode())){
            return true ;
        }
//        System.out.println(signType);
//        System.out.println(map.get("keywords"));
//        System.out.println(map.get("signLocationList"));
        return false;
    }

}