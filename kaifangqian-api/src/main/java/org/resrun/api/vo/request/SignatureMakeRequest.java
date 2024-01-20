package org.resrun.api.vo.request;

import lombok.Data;
import org.resrun.api.validation.ValidationSorts;
import org.resrun.api.vo.base.APIRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class SignatureMakeRequest extends APIRequest implements Serializable {

    //环绕文字 6～20长度，建议传递真实章的环绕文字
    @NotNull(message = "param_missing",groups = ValidationSorts.SortC1.class)
    @NotBlank(message = "param_blank",groups = ValidationSorts.SortC2.class)
    @Size(message = "param_format",max = 20, min = 6,groups = ValidationSorts.SortC3.class)


    private String topText;
    //横排文字  2～10长度，如：合同专用章
    @Size(message = "param_format",max = 10, min = 2,groups = ValidationSorts.SortD1.class)
    private String middleText;

    @Override
    public Map<String, String> paramNameMap() {
        Map<String,String> map = new HashMap<>();
        map.put("uniqueCode","uniqueCode");
        map.put("middleText","middleText");
        map.put("topText","topText");
        return map;
    }
}
