package org.resrun.sdk.vo.request;

import org.resrun.sdk.vo.base.APIRequest;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 签名证书（事件证书）签发-请求对象
 * @Package: org.resrun.vo
 * @ClassName: CertEventRequest
 * @author: FengLai_Gong
 */
public class CertEventRequest extends APIRequest implements Serializable {

    private static final long serialVersionUID = -5135668270081357110L;


    //证书主题 长度100
//    @NotNull(message = "param_missing",groups = ValidationSorts.SortB1.class)
//    @NotBlank(message = "param_blank",groups = ValidationSorts.SortB2.class)
//    @Size(message = "param_format",max = 100, groups = ValidationSorts.SortB3.class)
//    @Pattern(message = "param_format",regexp = "^[^ ]+$",groups = ValidationSorts.SortB4.class)
    private String certSubject ;

    //证书密码 长度32
//    @NotNull(message = "param_missing",groups = ValidationSorts.SortC1.class)
//    @NotBlank(message = "param_blank",groups = ValidationSorts.SortC2.class)
//    @Size(message = "param_format",max = 32,groups = ValidationSorts.SortC3.class)
//    @Pattern(message = "param_format",regexp = "^[^ ]+$",groups = ValidationSorts.SortC4.class)
    private String certPassword ;


    @Override
    public Map<String, String> paramNameMap() {
        Map<String,String> map = new HashMap<>();
        map.put("uniqueCode","uniqueCode");
        map.put("certSubject","certSubject");
        map.put("certPassword","certPassword");
        return map;
    }

    public String getCertSubject() {
        return certSubject;
    }

    public void setCertSubject(String certSubject) {
        this.certSubject = certSubject;
    }

    public String getCertPassword() {
        return certPassword;
    }

    public void setCertPassword(String certPassword) {
        this.certPassword = certPassword;
    }
}