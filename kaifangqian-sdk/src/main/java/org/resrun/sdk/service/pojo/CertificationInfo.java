package org.resrun.sdk.service.pojo;

import java.io.Serializable;

/**
 * @Description: CertificateInfo
 * @Package: org.resrun.modules.cert.pojo
 * @ClassName: CertificateInfo
 * @author: FengLai_Gong
 */
public class CertificationInfo implements Serializable {


    private static final long serialVersionUID = -9222852413621841079L;

    //证书头信息
    private String certDn ;

    //证书keyName
    private String keyName ;

    //证书密码
    private String password ;

    //p10文件
    private byte[] p10;
    //生成的p7b文件
    private byte[] p7bFile ;
    //生成的jks文件
    private byte[] jksFile ;
    //p7b之后jks文件
    private byte[] certFile ;
    //将原始证书认证后最终生成的pfx文件
    private byte[] pfxFile ;

    public String getCertDn() {
        return certDn;
    }

    public void setCertDn(String certDn) {
        this.certDn = certDn;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getP10() {
        return p10;
    }

    public void setP10(byte[] p10) {
        this.p10 = p10;
    }

    public byte[] getP7bFile() {
        return p7bFile;
    }

    public void setP7bFile(byte[] p7bFile) {
        this.p7bFile = p7bFile;
    }

    public byte[] getJksFile() {
        return jksFile;
    }

    public void setJksFile(byte[] jksFile) {
        this.jksFile = jksFile;
    }

    public byte[] getCertFile() {
        return certFile;
    }

    public void setCertFile(byte[] certFile) {
        this.certFile = certFile;
    }

    public byte[] getPfxFile() {
        return pfxFile;
    }

    public void setPfxFile(byte[] pfxFile) {
        this.pfxFile = pfxFile;
    }
}