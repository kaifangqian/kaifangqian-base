package org.resrun.api.service.pojo;


import java.io.Serializable;

/**
 * @Description: 申请证书-返回对象
 * @Package: org.resrun.modules.cert.pojo
 * @ClassName: ApplyCert
 * @author: FengLai_Gong
 */
public class ApplyCert implements Serializable {

    private static final long serialVersionUID = -2734385624897679135L;

    private Integer code ;

    private String message ;

    private String uniqueCode ;

    private ApplyCertData data ;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public ApplyCertData getData() {
        return data;
    }

    public void setData(ApplyCertData data) {
        this.data = data;
    }
}