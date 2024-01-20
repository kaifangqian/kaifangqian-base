package org.resrun.api.service.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 证书生成所需的参数和证书生成后的相关属性
 * @Package: org.resrun.modules.sign.service.tool.pojo
 * @ClassName: CertGenerateProperty
 * @author: FengLai_Gong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertGenerateProperty implements Serializable {

    private static final long serialVersionUID = 4186449536291972943L;


    //certDn证书的标题如：CN=某某,OU=身份证号或企业证件号,O=哪家公司帮某某申请,L=省份,ST=企业城市,C=所在国家
    private String certDn ;
    //数字证书的别名默认mykey
    private String keyName = "mykey" ;
    //秘钥长度
    private Integer keySize = 2048;
    //生成证书的加密算法
    private String certAlgorithm = "SHA1WithRSA" ;
    //生成密钥对的加密算法
    private String KeyPairGeneratorAlgorithm  = "RSA";
    //证书有效期起始时间
    private Date startTime ;
    //证书有效期截止时间
    private Date endTime ;

    //根证书文件
    private byte[] rootCert ;
    //根证书密码
    private String rootPassword ;




    //p10文件
    private byte[] p10;
    //证书序列号
    private String serialNumber ;
    //keystore文件
    private byte[] keystoreFile ;
    //新生成原始证书密码
    private String password ;
    //新生成原始证书文件
    private byte[] cert ;
    //生成的p7b文件
    private byte[] p7bFile ;
    //生成的jks文件
    private byte[] jksFile ;
    //将原始证书认证后最终生成的pfx文件
    private byte[] pfxFile ;



}