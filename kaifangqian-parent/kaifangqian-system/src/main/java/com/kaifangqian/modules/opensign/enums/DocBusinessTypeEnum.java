package com.kaifangqian.modules.opensign.enums;

/**
 * @Description: DocBusinessTypeEnum
 * @Package: com.kaifangqian.modules.opensign.enums
 * @ClassName: DocBusinessTypeEnum
 * @author: FengLai_Gong
 */
public enum DocBusinessTypeEnum {

    CREATE("1","业务开户材料"),
    DELETE("2","业务销户材料"),
    PLAN("3","资管计划合同"),
    STORE("4","同业定期存款协议"),

    ;

    private String code  ;

    private String name ;

    DocBusinessTypeEnum(String code, String name){
        this.code = code ;
        this.name = name;
    }


    public String getCode(){
        return this.code;
    }

    public String getName(){
        return this.name ;
    }


}