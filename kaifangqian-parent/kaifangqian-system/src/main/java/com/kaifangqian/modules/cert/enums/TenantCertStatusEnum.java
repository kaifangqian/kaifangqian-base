package com.kaifangqian.modules.cert.enums;

/**
 * @Description: TenantCertStatusEnum
 * @Package: com.kaifangqian.modules.cert.enums
 * @ClassName: TenantCertStatusEnum
 * @author: FengLai_Gong
 */
public enum TenantCertStatusEnum {

    ENABLE(1,"正常"),
    REVOKE(2,"吊销"),
    UN_ENABLE(3,"失效"),


    ;

    private Integer code  ;

    private String name ;

    TenantCertStatusEnum(Integer code, String name){
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