package org.resrun.sdk.config;

public class SDKClientConfig {

    /**
     * 是否生产环境 默认为否
     */
    private Boolean prod = false;

    /**
     * 授权token
     */
    private String token;

    /**
     * 证书申请路径
     */
    private String certApplyUrl;


    public Boolean getProd() {
        return prod;
    }

    public void setProd(Boolean prod) {
        this.prod = prod;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCertApplyUrl() {
        return certApplyUrl;
    }

    public void setCertApplyUrl(String certApplyUrl) {
        this.certApplyUrl = certApplyUrl;
    }
}
