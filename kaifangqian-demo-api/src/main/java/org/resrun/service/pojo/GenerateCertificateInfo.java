package org.resrun.service.pojo;

import java.util.Date;


/**
 * @Description: 证书生成对象
 * @Package: org.resrun.service.pojo
 * @ClassName: GenerateCertificateInfo
 * @copyright 北京资源律动科技有限公司
 */

public class GenerateCertificateInfo {

    /**
     * 证书密码
     */
    private String password;

    /**
     * 证书类型  JKS  PFX
     */
    private String certFileType;

    /**
     * 证书库
     */
    private byte [] jks;


    /**
     * 签名证书
     */
    private byte [] pfx;

    /**
     * 证书序列号
     */
    private String serial;
    /**
     * 证书签名算法     SHA1withRSA  SHA256withRSA
     */
    private String algorithmSignature;

    /**
     *  证书算法类型： RSA、SM2
     */
    private String algorithm;

    /**
     * 证书有效期起始时间
     */
    private Date termOfValidityStartTime;

    /**
     * 证书有效期结束时间
     */
    private Date termOfValidityEndTime;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCertFileType() {
        return certFileType;
    }

    public void setCertFileType(String certFileType) {
        this.certFileType = certFileType;
    }

    public byte[] getJks() {
        return jks;
    }

    public void setJks(byte[] jks) {
        this.jks = jks;
    }

    public byte[] getPfx() {
        return pfx;
    }

    public void setPfx(byte[] pfx) {
        this.pfx = pfx;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getAlgorithmSignature() {
        return algorithmSignature;
    }

    public void setAlgorithmSignature(String algorithmSignature) {
        this.algorithmSignature = algorithmSignature;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public Date getTermOfValidityStartTime() {
        return termOfValidityStartTime;
    }

    public void setTermOfValidityStartTime(Date termOfValidityStartTime) {
        this.termOfValidityStartTime = termOfValidityStartTime;
    }

    public Date getTermOfValidityEndTime() {
        return termOfValidityEndTime;
    }

    public void setTermOfValidityEndTime(Date termOfValidityEndTime) {
        this.termOfValidityEndTime = termOfValidityEndTime;
    }
}
