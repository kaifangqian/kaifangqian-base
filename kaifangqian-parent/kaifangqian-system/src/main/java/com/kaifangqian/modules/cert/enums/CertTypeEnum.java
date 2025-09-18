package com.kaifangqian.modules.cert.enums;

/**
 * @Description: CertType
 * @Package: com.kaifangqian.modules.opensign.enums
 * @ClassName: CertType
 * @author: FengLai_Gong
 */
public enum CertTypeEnum {

//    CA_ROOT(1,"ca根证书"),
//    SYSTEM_ROOT(2,"系统防篡改根证书"),

//    CA_PERSONAL(3,"ca个人证书"),
//    CA_ENTERPRISE(4,"ca企业证书"),

//    SYSTEM_PERSONAL(5,"系统防篡改个人证书"),
//    SYSTEM_ENTERPRISE(6,"系统防篡改企业证书"),

//    SHORT_TERM_EVENT_PERSONAL(7,"短期事件个人证书"),
//    SHORT_TERM_EVENT_ENTERPRISE(8,"短期事件企业证书"),

    SYSTEM(1,"平台防篡改证书"),
    CA_TEST(2,"测试数字证书"),
    CA_SHORT_TERM(3,"CA事件数字证书"),
    CA_LONG_TERM(4,"CA长效数字证书"),



//    LONG_TERM_EVENT(8,"长期事件证书"),



    ;

    private Integer code  ;

    private String name ;

    CertTypeEnum(Integer code, String name){
        this.code = code ;
        this.name = name;
    }


    public Integer getCode(){
        return this.code;
    }

    public String getName(){
        return this.name ;
    }


    public static CertTypeEnum getByCode(Integer code){
        CertTypeEnum[] values = CertTypeEnum.values();
        for(CertTypeEnum certTypeEnum : values){
            if(certTypeEnum.getCode() == code){
                return certTypeEnum ;
            }
        }
        return null ;
    }
}