package org.resrun.api.vo.response;

import java.io.Serializable;

/**
 * @Description: CertEventResponse
 * @Package: org.resrun.vo.response
 * @ClassName: CertEventResponse
 * @author: FengLai_Gong
 */
public class CertEventResponse implements Serializable {


    private static final long serialVersionUID = -2670652937955458956L;

    //证书主题 长度100
    private String certSubject ;

    //证书序列号 长度50
    private String certSN ;

    //签名证书文件
    private String pfx ;

    //证书密码
    private String certPassword ;

    //证书有效期起始时间 14 证书有效期起始时间（格式：yyyyMMddHHmmss）
    private String certValidityNotBefore ;

    //证书有效期截止时间 14 证书有效期截止时间（格式：yyyyMMddHHmmss）
    private String certValidityNotAfter ;

    //有效期天数
    private Integer certValidity ;

    public String getCertSubject() {
        return certSubject;
    }

    public void setCertSubject(String certSubject) {
        this.certSubject = certSubject;
    }

    public String getCertSN() {
        return certSN;
    }

    public void setCertSN(String certSN) {
        this.certSN = certSN;
    }

    public String getPfx() {
        return pfx;
    }

    public void setPfx(String pfx) {
        this.pfx = pfx;
    }

    public String getCertPassword() {
        return certPassword;
    }

    public void setCertPassword(String certPassword) {
        this.certPassword = certPassword;
    }

    public String getCertValidityNotBefore() {
        return certValidityNotBefore;
    }

    public void setCertValidityNotBefore(String certValidityNotBefore) {
        this.certValidityNotBefore = certValidityNotBefore;
    }

    public String getCertValidityNotAfter() {
        return certValidityNotAfter;
    }

    public void setCertValidityNotAfter(String certValidityNotAfter) {
        this.certValidityNotAfter = certValidityNotAfter;
    }

    public Integer getCertValidity() {
        return certValidity;
    }

    public void setCertValidity(Integer certValidity) {
        this.certValidity = certValidity;
    }
}