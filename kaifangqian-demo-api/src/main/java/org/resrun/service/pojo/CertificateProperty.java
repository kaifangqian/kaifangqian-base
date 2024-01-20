package org.resrun.service.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: 证书文件属性类
 * @Package: org.resrun.service.pojo
 * @ClassName: CertificateProperty
 * @copyright 北京资源律动科技有限公司
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CertificateProperty implements Serializable {

    private static final long serialVersionUID = -2073805779543816269L;


    private  byte[] certFile;
    /** 证书的类型 比如：PKCS12和jks*/
    private  String certType;
    /** 证书密码 */
    private  String password;


}