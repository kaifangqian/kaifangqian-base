package org.resrun.api.vo.base;

import org.resrun.api.enums.APIResultEnum;
import org.resrun.api.enums.BusinessErrorEnum;
import org.resrun.api.enums.ParamFormatErrorEnum;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 * @Description: 接口统一返回参数
 * @Package: org.resrun.vo
 * @ClassName: Result
 * @author: FengLai_Gong
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -3953054203092646590L;

    //返回处理消息
    private String message ;
    //返回代码
    private Integer code ;
    //流水号
    private String uniqueCode ;
    //返回数据对象
    private T data;

    public Result() {
    }

    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static <T> Result<T> OK(T data,String uniqueCode) {
        Result<T> r = new Result();
        r.setCode(APIResultEnum.SUCCESS.getCode());
        r.setMessage(APIResultEnum.SUCCESS.getTemplate());
        r.setData(data);
        r.setUniqueCode(uniqueCode);
        return r;
    }

    public static <T> Result<T> error(Integer code,String msg,String uniqueCode){
        Result<T> r = new Result();
        r.setCode(code);
        r.setMessage(msg);
        r.setUniqueCode(uniqueCode);
        r.setData(null);
        return r;
    }

    public static <T> Result<T> error(APIResultEnum resultEnum,String uniqueCode,String ... args){
        Result<T> r = new Result();
        r.setCode(resultEnum.getCode());
        String template = resultEnum.getTemplate();
        String message = MessageFormat.format(template,args);
        r.setMessage(message);
        r.setUniqueCode(uniqueCode);
        r.setData(null);
        return r;
    }

    public static <T> Result<T> error(APIResultEnum resultEnum , ParamFormatErrorEnum paramFormatErrorEnum){
        Result<T> r = new Result();
        r.setCode(resultEnum.getCode());
        String template = resultEnum.getTemplate();
        String message = MessageFormat.format(template,paramFormatErrorEnum.getName());
        r.setMessage(message);
        r.setData(null);
        return r;
    }

    public static <T> Result<T> error(APIResultEnum resultEnum , BusinessErrorEnum businessErrorEnum){
        Result<T> r = new Result();
        r.setCode(resultEnum.getCode());
        String template = resultEnum.getTemplate();
        String message = MessageFormat.format(template,businessErrorEnum.getName());
        r.setMessage(message);
        r.setData(null);
        return r;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}