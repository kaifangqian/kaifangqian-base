package com.kaifangqian.modules.cert.enums;

/**
 * @Description: CertHolderTypeEnum
 * @Package: com.kaifangqian.modules.opensign.enums
 * @ClassName: CertHolderTypeEnum
 * @author: FengLai_Gong
 */
public enum CertHolderTypeEnum {



    PERSONAL(1,"个人证书"),
    ENTERPRISE(2,"企业证书"),


    ;

    private Integer code  ;

    private String name ;

    CertHolderTypeEnum(Integer code, String name){
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