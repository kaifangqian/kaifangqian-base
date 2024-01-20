package org.resrun.api.service.pojo;

import java.security.KeyStore;

public class BaseCertificateInfo {

    /**
     * 根证书
     */
    private KeyStore cert;

    /**
     * 证书别名
     */
    private String alias;

    /**
     * 证书密码
     */
    private String password;

    public KeyStore getCert() {
        return cert;
    }

    public void setCert(KeyStore cert) {
        this.cert = cert;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
