package org.resrun.api.pdfbox.vo;

public class CertificateInfo {


    /**
     * 证书密码
     */
    private String password;

    /**
     * 证书文集   PFX格式
     */
    private byte [] cert;


    /**
     * 证书类型  PFX格式为PKCS12
     */
    private CertTypeEnum certType;

    public CertTypeEnum getCertType() {
        return certType;
    }

    public void setCertType(CertTypeEnum certType) {
        this.certType = certType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getCert() {
        return cert;
    }

    public void setCert(byte[] cert) {
        this.cert = cert;
    }
    public enum CertTypeEnum {

        PKCS12,JKS

    }

}

