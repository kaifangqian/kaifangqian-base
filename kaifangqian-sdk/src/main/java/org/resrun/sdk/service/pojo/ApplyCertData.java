package org.resrun.sdk.service.pojo;

import java.io.Serializable;

/**
 * @Description: ApplyCertData
 * @Package: org.resrun.modules.cert.pojo
 * @ClassName: ApplyCertData
 * @author: FengLai_Gong
 */
public class ApplyCertData implements Serializable {

    private static final long serialVersionUID = 3447485591197821614L;

    private String p7b ;

    private String certSN ;

    private String certNO ;

    private String certSubject ;

    //时间格式 20231229102348
    private String certValidityNotBefore ;

    //时间格式 20231230102348
    private String certValidityNotAfter ;

    private Integer certValidity ;

    public String getP7b() {
        return p7b;
    }

    public void setP7b(String p7b) {
        this.p7b = p7b;
    }

    public String getCertSN() {
        return certSN;
    }

    public void setCertSN(String certSN) {
        this.certSN = certSN;
    }

    public String getCertNO() {
        return certNO;
    }

    public void setCertNO(String certNO) {
        this.certNO = certNO;
    }

    public String getCertSubject() {
        return certSubject;
    }

    public void setCertSubject(String certSubject) {
        this.certSubject = certSubject;
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

    public ApplyCertData(String p7b, String certSN, String certNO, String certSubject, String certValidityNotBefore, String certValidityNotAfter, Integer certValidity) {
        this.p7b = p7b;
        this.certSN = certSN;
        this.certNO = certNO;
        this.certSubject = certSubject;
        this.certValidityNotBefore = certValidityNotBefore;
        this.certValidityNotAfter = certValidityNotAfter;
        this.certValidity = certValidity;
    }

    public ApplyCertData() {
        super();
    }
}