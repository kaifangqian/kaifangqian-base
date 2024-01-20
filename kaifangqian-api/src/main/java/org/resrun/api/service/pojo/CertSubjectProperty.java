package org.resrun.api.service.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: CertSubjectProperty
 * @Package: org.resrun.modules.opensign.service.tool.pojo
 * @ClassName: CertSubjectProperty
 * @author: FengLai_Gong
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CertSubjectProperty implements Serializable {



    //   C  Country Name (国家代号),eg: CN
    private String c ;

    //    ST State or Province Name (洲或者省份),eg: Beijing
    private String st ;
    //  CN Common Name (服务器ip或者域名),eg: 192.168.30.71 or www.baidu.com
    //1）个人：【姓名】@【身份证号码】@【手机号】
    //2）企业：【企业名称】@【统一社会信用代码】
    private String cn ;

    //   L  Locality Name (城市名),eg: Beijing
    private String l ;

    //   O  Organization Name (可以是公司名称),eg: 北京资源律动科技有限公司
    private String o ;
    //   OU Organizational Unit Name (可以是单位部门名称)
    private String ou ;

    //    STREET  streetAddress
    private String street ;
    //    DC      domainComponent
    private String dc ;
    //    UID     userid
    private String uid ;


    // 1为个人，2为企业
    private Integer certType ;









}