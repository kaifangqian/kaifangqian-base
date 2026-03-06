package com.kaifangqian.modules.account.enums;

/**
 * @Description: DocStatusEnum
 * @Package: com.resrun.modules.opensign.enums
 * @ClassName: DocStatusEnum
 * @author: FengLai_Gong
 */
public enum SignConsumeTypeEnum {


    VALID_SIGN(1,"VALID_SIGN", "意愿验证签署"),

    AUTO_SIGN(2,"AUTO_SIGN", "静默签署"),

    PASSWORD_SIGN(3,"PASSWORD", "密码验证签署"),

    FACE_SIGN(4,"FACE", "人脸识别验证签署"),

    CAPTCHA_SIGN(5,"CAPTCHA", "验证码验证签署"),

    DOUBLE_SIGN(6,"DOUBLE", "验证码、密码双因子验证签署"),

    WILLINGESS_FREE_SIGN(7,"WILLINGESS_FREE_SIGN", "免意愿验证签署"),

    WILLINGESS_FREE_DAY_SIGN(8,"WILLINGESS_FREE_DAY_SIGN","免意愿验证签署"),

    NO_AUTH_SIGN(9,"NO_AUTH_SIGN" ,"非实名认证签署"),

    SIGN_CONSUME_COPIES(1,"SIGN_CONSUME_COPIES", "签署份数消耗"),


    ;

    SignConsumeTypeEnum(Integer code , String name, String desc){
        this.code = code;
        this.name = name;
        this.desc = desc;
    }

    private Integer code ;
    private String name ;
    private String desc ;

    public String getName(){
        return this.name;
    }

    public Integer getCode(){
        return this.code;
    }

    public String getDesc(){
        return this.desc;
    }

    public static SignConsumeTypeEnum matchSignType(String typeName) {
        for (SignConsumeTypeEnum type : SignConsumeTypeEnum.values()) {
            if (type.name.equalsIgnoreCase(typeName)) {
                return type;
            }
        }
        return SignConsumeTypeEnum.AUTO_SIGN;
    }

    public static SignConsumeTypeEnum matchSignDesc(Integer code) {
        for (SignConsumeTypeEnum type : SignConsumeTypeEnum.values()) {
            if (type.code.equals(code)){
                return type;
            }
        }
        return SignConsumeTypeEnum.AUTO_SIGN;
    }



}

