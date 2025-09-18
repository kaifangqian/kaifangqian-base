package com.kaifangqian.modules.cert.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: CertificateInfo
 * @Package: com.kaifangqian.modules.cert.pojo
 * @ClassName: CertificateInfo
 * @author: FengLai_Gong
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
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



}