package org.resrun.sdk.enums;

/**
 * @Description: APIResultEnum
 * @Package: org.resrun.enums
 * @ClassName: APIResultEnum
 * @author: FengLai_Gong
 */
public enum APIResultEnum {


    SUCCESS(10000,"处理成功","处理成功"),

    VERIFY_FAIL(20000,"sign verify fail","sign verify fail"),

    PARAM_MISSING(21000,"{0}参数缺失","param_missing"),
    PARAM_BLANK(22000,"{0}参数不能为空","param_blank"),
    PARAM_FORMAT(23000,"{0}参数格式不正确,{1}","param_format"),


    BUSINESS_ERROR(31000,"业务处理失败,{0}","business_error"),


    TOKEN_INVALID(41000,"token无效","token_invalid"),
    TOKEN_NO_AUTH(42000,"token中未授权该接口调用权限","token_no_auth"),

    SERVICE_CONNECT_ERROR(43000,"调用证书签发服务超时","service_connect_error"),


    SQL_INJECTION_ERROR(51000,"请勿SQL攻击","sql_injection_error"),
    CROSS_SITE_ERROR(52000,"请勿跨站点攻击","cross_site_error"),
    UNKNOWN(91000, "系统未知错误","unknown"),

    ;

    private Integer code;

    private String template;

    private String message;

    APIResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    APIResultEnum(Integer code,String template, String message) {
        this.code = code;
        this.message = message;
        this.template = template;
    }

    public Integer getCode(){
        return code ;
    }

    public String getTemplate() {
        return template;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static APIResultEnum statOf(Integer code) {
        for (APIResultEnum state : values()) {
            if (state.code.equals(code)) {
                return state;
            }
        }
        return null;
    }


    public static APIResultEnum getByMessage(String message){
        if(message != null && message.length() > 0){
            for(APIResultEnum state : APIResultEnum.values()){
                if(state.message.equals(message)){
                    return state;
                }
            }
        }
        return null ;
    }

}