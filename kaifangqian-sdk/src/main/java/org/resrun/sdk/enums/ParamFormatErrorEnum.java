package org.resrun.sdk.enums;

/**
 * @Description: 参数格式异常枚举
 * @Package: org.resrun.enums
 * @ClassName: ParamFormatErrorEnum
 * @author: FengLai_Gong
 */
public enum ParamFormatErrorEnum {


    LENGTH_ERROR(1,"长度不合法"),
    FILE_FORMAT_ERROR(2,"文件格式不支持"),
    FILE_SIZE_ERROR(3,"文件超出限制大小"),
    OUT_ENUM_RANG_ERROR(4,"超出枚举范围"),
    HAVE_BLANK_ERROR(5,"不能包含空格"),
    TIME_FORMAT_ERROR(6,"时间格式不正确"),
    NUMBER_RANG_ERROR(7,"数值范围不合法"),
    TIME_RANG_ERROR(8,"时间范围不合法"),



    ;

    private Integer code  ;

    private String name ;

    ParamFormatErrorEnum(Integer code, String name){
        this.code = code ;
        this.name = name;
    }


    public Integer getCode(){
        return this.code;
    }

    public String getName(){
        return this.name ;
    }

}