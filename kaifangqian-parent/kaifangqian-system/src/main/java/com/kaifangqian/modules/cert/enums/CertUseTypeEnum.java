package com.kaifangqian.modules.cert.enums;

/**
 * @Description: CertUseTypeEnum
 * @Package: com.kaifangqian.modules.cert.enums
 * @ClassName: CertUseTypeEnum
 * @author: FengLai_Gong
 */
public enum CertUseTypeEnum {


    ALL_SHORT_TERM(1,"均使用事件证书"),
    ALL_LONG_TERM(2,"均使用长效证书"),
    PERSON_SHORT_ENT_LONG(1,"企业使用长效证书，个人使用事件证书"),


    ;

    private Integer code  ;

    private String name ;

    CertUseTypeEnum(Integer code, String name){
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