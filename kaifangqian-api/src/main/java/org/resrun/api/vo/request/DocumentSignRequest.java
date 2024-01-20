package org.resrun.api.vo.request;

import org.resrun.api.validation.SignTypeValid;
import org.resrun.api.validation.ValidationSorts;
import org.resrun.api.vo.base.APIRequest;
import org.resrun.api.vo.base.SignLocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 文件签署-请求对象
 * @Package: org.resrun.vo
 * @ClassName: DocumentSignRequest
 * @author: FengLai_Gong
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DocumentSignRequest extends APIRequest implements Serializable {


    private static final long serialVersionUID = -8454045874267119629L;

    //待签署文件
    private String documentFile ;

    //待签署文件网络地址
    private String documentNetworkURL ;

    //签署类型 1:关键字签署；2:指定位置签署
    @NotNull(message = "param_missing",groups = ValidationSorts.SortB1.class)
    @SignTypeValid(message = "param_format",groups = ValidationSorts.SortB2.class)
    private Integer signType ;

//    //方法名称必须是get开头，才会走到注解中
//    @SignTypeValid(message = "param_format",groups = ValidationSorts.SortB2.class)
//    public Map<String,Object> getSignTypeValid(){
//        Map<String,Object> map = new HashMap<>();
//        map.put("signType",this.signType);
//        map.put("keywords",this.keywords);
//        map.put("signLocationList",this.signLocationList);
//        return map ;
//    }

    //关键字 当signType为1时，必传
    private String keywords ;

    //签署位置集合
    private List<SignLocation> signLocationList ;

    //签章图片
    private String signatureFile ;

    //签章图片网络地址
    private String signatureNetworkURL ;

    //签名证书
    private String pfx ;

    //签名证书网络地址
    private String pfxNetworkURL ;

    //证书密码 长度32
    @NotNull(message = "param_missing",groups = ValidationSorts.SortC1.class)
    @NotBlank(message = "param_blank",groups = ValidationSorts.SortC2.class)
    @Size(message = "param_format",max = 32,groups = ValidationSorts.SortC3.class)
    @Pattern(message = "param_format",regexp = "^[^ ]+$",groups = ValidationSorts.SortC4.class)
    private String certPassword ;

    @Override
    public Map<String, String> paramNameMap() {
        Map<String,String> map = new HashMap<>();
        map.put("uniqueCode","uniqueCode");
        map.put("documentFile","documentFile");
        map.put("documentNetworkURL","documentNetworkURL");
        map.put("signType","signType");
        map.put("keywords","keywords");
        map.put("signLocationList","signLocationList");
        map.put("signatureFile","signatureFile");
        map.put("signatureNetworkURL","signatureNetworkURL");
        map.put("pfx","pfx");
        map.put("pfxNetworkURL","pfxNetworkURL");
        map.put("certPassword","certPassword");
        return map;
    }


}