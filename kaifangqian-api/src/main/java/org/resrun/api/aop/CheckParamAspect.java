package org.resrun.api.aop;

import org.resrun.api.enums.APIResultEnum;
import org.resrun.api.enums.ParamFormatErrorEnum;
import org.resrun.api.exception.ResultException;
import org.resrun.api.validation.SignTypeValid;
import org.resrun.api.validation.ValidationSorts;
import org.resrun.api.vo.base.APIRequest;
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Set;
/**
 * create by wangwenting at 2021/9/29 4:56 PM
 *
 * @param
 * @description 根据controller，记录详细日志到日志文件
 * @return
 * @throws
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
    @Pointcut("execution(public * org.resrun.api.controller.*Controller.*(..))")
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
            if (param instanceof APIRequest) {
                APIRequest apiRequest = (APIRequest) param;
                checkParams(apiRequest);
            }
        }
    }

//    /**
//     * @param errorMessage 错误信息
//     * @param args         错误的字段名称和其他附加参数
//     * @return void
//     * @Description 格式化统一返回信息   并抛出异常
//     **/
//    public static void throwErrorBusiness(String errorMessage, String... args) {
//        //根据错误异常编码获取异常枚举
//        APIResultEnum apiResultEnum = APIResultEnum.getByMessage(errorMessage);
//        //根据错误的字段名   格式化异常信息
//        String message = MessageFormat.format(apiResultEnum.getTemplate(), args);
//        throw new BusinessException(apiResultEnum,null);
//    }

//    public static void throwErrorParamFormat(String errorMessage, String field) {
//        //根据错误异常编码获取异常枚举
//        APIResultEnum apiResultEnum = APIResultEnum.getByMessage(errorMessage);
//        //根据错误的字段名   格式化异常信息
//        String message = MessageFormat.format(apiResultEnum.getTemplate(), field);
//        throw new ParamFormatException(apiResultEnum,ParamFormatErrorEnum.LENGTH_ERROR);
//    }


    public static void throwError(String errorMessage,String field,String uniqueCode) {
        //根据错误异常编码获取异常枚举
        APIResultEnum apiResultEnum = APIResultEnum.getByMessage(errorMessage);
        //根据错误的字段名   格式化异常信息
        String message = MessageFormat.format(apiResultEnum.getTemplate(), field);
        throw new ResultException(apiResultEnum.getCode(),message,uniqueCode);
    }
    public static void throwError(String errorMessage,String field,String uniqueCode,ParamFormatErrorEnum paramFormatErrorEnum) {
        //根据错误异常编码获取异常枚举
        APIResultEnum apiResultEnum = APIResultEnum.getByMessage(errorMessage);
        //根据错误的字段名   格式化异常信息
        String message = MessageFormat.format(apiResultEnum.getTemplate(), field,paramFormatErrorEnum.getName());
        throw new ResultException(apiResultEnum.getCode(),message,uniqueCode);
    }


    /**
     * @param req
     * @return void
     * @Description POST请求参数校验
     **/
    public void checkParams(APIRequest req) {

        //验证参数合法性 是否存在SQL注入
        //CheckSqlKeyWords.checkSqlKeyWords(req.toMap());

        Validator validator = validatorFactory.getValidator();
        //验证公共参数
        Set<ConstraintViolation<APIRequest>> publicParamResult = validator.validate(req, ValidationSorts.Sort.class);
        if (publicParamResult.size() > 0) {
            //获取第一个异常参数并抛出异常
            ConstraintViolation<APIRequest> publicParamError = publicParamResult.stream().findFirst().get();
            //获取当前异常参数的注解
            Object errorAnnotation = publicParamError.getConstraintDescriptor().getAnnotation();
            Map<String, String> map = req.paramNameMap();
            String param = publicParamError.getPropertyPath().toString();
            String paramName = map.get(param);
            if(errorAnnotation instanceof NotNull){
                throwError(publicParamError.getMessage(), paramName,req.getUniqueCode());
            }else if(errorAnnotation instanceof NotBlank){
                throwError(publicParamError.getMessage(), paramName,req.getUniqueCode());
            }else if(errorAnnotation instanceof Size){
                throwError(publicParamError.getMessage(), paramName,req.getUniqueCode(),ParamFormatErrorEnum.LENGTH_ERROR);
            }else if(errorAnnotation instanceof Pattern){
                throwError(publicParamError.getMessage(), paramName,req.getUniqueCode(),ParamFormatErrorEnum.HAVE_BLANK_ERROR);
            }else if(errorAnnotation instanceof SignTypeValid){
                throwError(publicParamError.getMessage(), paramName,req.getUniqueCode(),ParamFormatErrorEnum.OUT_ENUM_RANG_ERROR);
            }

        }
    }
}
