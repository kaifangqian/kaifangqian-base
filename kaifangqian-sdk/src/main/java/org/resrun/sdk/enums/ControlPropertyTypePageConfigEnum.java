package org.resrun.sdk.enums;

/**
 * @Description: 控件属性类型-应用页面-枚举
 * @Package: com.resrun.modules.opensign.enums
 * @ClassName: ControlPropertyTypePageConfigEnum
 * @author: FengLai_Gong
 */
public enum ControlPropertyTypePageConfigEnum {


    ODD_NUMBER("odd_number","奇数页"),
    EVEN_NUMBER("even_number","偶数页"),
    ALL("all","全部页"),
    CUSTOM("custom","指定页"),

    ;

    private String  code  ;

    private String name ;

    ControlPropertyTypePageConfigEnum(String code, String name){
        this.code = code ;
        this.name = name;
    }


    public String getCode(){
        return this.code;
    }

    public String getName(){
        return this.name ;
    }

    public static ControlPropertyTypePageConfigEnum getValue(String code){
        ControlPropertyTypePageConfigEnum[] values = ControlPropertyTypePageConfigEnum.values();
        for(ControlPropertyTypePageConfigEnum pageConfigEnum : values){
            if(pageConfigEnum.getCode().equals(code)){
                return pageConfigEnum ;
            }
        }
        return null ;
    }

}