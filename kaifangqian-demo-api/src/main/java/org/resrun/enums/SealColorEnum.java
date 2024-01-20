package org.resrun.enums;

/**
 * @Description: 签章颜色枚举类
 * @Package: org.resrun.enums
 * @ClassName: SealColorEnum
 * @copyright 北京资源律动科技有限公司
 */
public enum SealColorEnum {

    RED(1,"红色"),
    BLUE(2,"蓝色"),
    BLACK(3,"黑色"),

    ;

    private Integer code ;
    private String name ;

    SealColorEnum(Integer code, String name){
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