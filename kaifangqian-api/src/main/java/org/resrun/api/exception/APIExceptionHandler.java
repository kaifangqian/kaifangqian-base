package org.resrun.api.exception;

import com.alibaba.fastjson.JSONObject;
import org.resrun.api.vo.base.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: PaasExceptionHandler
 * @Package: org.resrun.exception
 * @ClassName: PaasExceptionHandler
 * @author: FengLai_Gong
 */
@RestControllerAdvice
@Slf4j
public class APIExceptionHandler {

    @ExceptionHandler(ResultException.class)
    public Result<?> handlePaasException(ResultException e, HttpServletRequest request,HttpServletResponse response) {
        Result result = Result.error(e.code,e.message,e.uniqueCode);
        printExceptionLog(request,response,result);
        return result;
    }

    @ExceptionHandler(BusinessException.class)
    public Result<?> handlePaasException(BusinessException e, HttpServletRequest request,HttpServletResponse response) {

        Result result = Result.error(e.resultEnum,e.businessErrorEnum);
        printExceptionLog(request,response,result);
        return result;

    }

    @ExceptionHandler(ParamFormatException.class)
    public Result<?> handlePaasException(ParamFormatException e, HttpServletRequest request,HttpServletResponse response) {
        Result result = Result.error(e.resultEnum,e.paramFormatErrorEnum);
        printExceptionLog(request,response,result);
        return result;
    }

    public void printExceptionLog(HttpServletRequest request, HttpServletResponse response, Result result) {
        // 请求Url
        String requestUrl = request.getRequestURI().toString();
        //请求方法名称
        String method = request.getMethod();
        // 异常信息：
        log.info("统一异常处理：requestUrl = {}, method = {}, result= {}", requestUrl, method, JSONObject.toJSON(result));
    }




}