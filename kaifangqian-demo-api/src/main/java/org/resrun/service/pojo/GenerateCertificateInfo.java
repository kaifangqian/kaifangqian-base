package org.resrun.service.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * @Description: 证书生成对象
 * @Package: org.resrun.service.pojo
 * @ClassName: GenerateCertificateInfo
 * @copyright 北京资源律动科技有限公司
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
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



}
