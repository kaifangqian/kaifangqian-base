package com.kaifangqian.modules.cert.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: TenantCert
 * @Package: com.kaifangqian.modules.cert.pojo
 * @ClassName: TenantCert
 * @author: FengLai_Gong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenantCertInfo implements Serializable {

    private static final long serialVersionUID = -2254592709543630627L;

    //持证主体类型
    private Integer holderType ;
    //证书类型
    private Integer certType ;

    //租户id
    private String tenantId ;
    //个人名称
    private String name ;
    //个人身份证号
    private String idCardNumber ;
    //个人手机号
    private String phone ;

    //企业名称
    private String entName ;
    //企业统一信用代码
    private String uscc ;


}