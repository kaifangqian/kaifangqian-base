package org.resrun.enums;


/**
 * @Description: 签名文件状态枚举
 * @Package: org.resrun.enums
 * @ClassName: SignStatus
 * @copyright 北京资源律动科技有限公司
 */

public enum SignStatus {
    SIGN_STATUS_NOSIGNATURE("PDF文件未发现任何数字签名信息",1),
    SIGN_STATUS_ERROR("PDF签名验证时发生错误，签名包含不正确的、无法识别的、已损坏的或可疑的数据",2),
    SIGN_STATUS_FIDDLE("PDF中存在数字证书，但文档被篡改，进入下一步，展示验证结果",3),
    SIGN_STATUS_RIGHT("PDF中存在数字证书，文档未被篡改，进入下一步，展示验证结果",4),

    ;

    private String msg;
    private Integer code;
    SignStatus(String msg,Integer code){
        this.msg = msg;
        this.code = code;
    }

    public Integer getCode(){
        return code;
    }
    public String getMsg(){
        return msg;
    }
}
