package org.resrun.enums;

/**
 * @Description: 个人签章形状样式
 * @Package: com.resrun.modules.sign.enums
 * @ClassName: SealBorderEnum
 * @author: FengLai_Gong
 */
public enum SealShapeStyleEnum {

    RECTANGLE_NO_FRAME(1,"长方形无框"),
    RECTANGLE_FRAME(2,"长方形有框"),
    SQUARE_NO_FRAME(3,"正方形无框"),
    SQUARE_FRAME(4,"正方形有框"),

    ;

    private Integer code ;
    private String name ;

    SealShapeStyleEnum(Integer code, String name){
        this.code = code ;
        this.name = name ;
    }

    public String getName(){
        return this.name ;
    }

    public Integer getCode(){
        return this.code ;
    }
}