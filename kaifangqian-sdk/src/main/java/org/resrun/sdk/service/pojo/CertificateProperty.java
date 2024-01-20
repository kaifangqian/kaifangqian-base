package org.resrun.sdk.service.pojo;

import java.io.Serializable;

/**
 * @Description: 证书文件属性类
 * @Package: org.resrun.modules.sign.pojo
 * @ClassName: CertificateProperty
 * @author: FengLai_Gong
 */
public class CertificateProperty implements Serializable {

    private static final long serialVersionUID = -2073805779543816269L;


    private  byte[] certFile;
    /** 证书的类型 比如：PKCS12和jks*/
    private  String certType;
    /** 证书密码 */
    private  String password;


    public byte[] getCertFile() {
        return certFile;
    }

    public void setCertFile(byte[] certFile) {
        this.certFile = certFile;
    }

    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
}