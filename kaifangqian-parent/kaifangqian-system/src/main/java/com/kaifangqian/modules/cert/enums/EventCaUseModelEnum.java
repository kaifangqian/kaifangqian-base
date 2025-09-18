package com.kaifangqian.modules.cert.enums;

/**
 * @Description: EventCaUseModelEnum
 * @Package: com.kaifangqian.modules.cert.enums
 * @ClassName: EventCaUseModelEnum
 * @author: FengLai_Gong
 */
public enum EventCaUseModelEnum {

    ONCE_USE(1,"仅使用一次"),

    EXPIRE_USE(2,"有效期内可重复使用"),

    ;

    private Integer code  ;

    private String name ;

    EventCaUseModelEnum(Integer code, String name){
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