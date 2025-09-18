package com.kaifangqian.modules.opensign.pdfbox.vo;

public class AssinaturaModel {

    /**
     * 证书对象
     */
    private CertificateInfo certInfo;

    /**
     * 待签名PDF文件
     */
    private byte [] pdf;

    /**
     * 签名图片
     */
    private byte [] signatureImage;


    /**
     * 签署位置信息
     */
    private AssinaturaPosition position;

    /**
     * 签名域key   控件的interface_name
     */
    private String signatureKey;

    /**
     * 签名时间戳
     */
    private String tsa;

    /**
     * 签名人
     */
    private String name;
    private String reason;
    private String location;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSignatureKey() {
        return signatureKey;
    }

    public void setSignatureKey(String signatureKey) {
        this.signatureKey = signatureKey;
    }

    public String getTsa() {
        return tsa;
    }

    public void setTsa(String tsa) {
        this.tsa = tsa;
    }

    public CertificateInfo getCertInfo() {
        return certInfo;
    }

    public void setCertInfo(CertificateInfo certInfo) {
        this.certInfo = certInfo;
    }

    public byte[] getPdf() {
        return pdf;
    }

    public void setPdf(byte[] pdf) {
        this.pdf = pdf;
    }

    public byte[] getSignatureImage() {
        return signatureImage;
    }

    public void setSignatureImage(byte[] signatureImage) {
        this.signatureImage = signatureImage;
    }

    public AssinaturaPosition getPosition() {
        return position;
    }

    public void setPosition(AssinaturaPosition position) {
        this.position = position;
    }
}
