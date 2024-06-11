package org.resrun.api.vo.base;

import org.resrun.api.validation.ValidationSorts;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Map;

/**
 * @Description: 接口统一请求参数父类
 * @Package: org.resrun.vo
 * @ClassName: APIRequest
 * @author: FengLai_Gong
 */
public abstract class APIRequest implements Serializable {

    private static final long serialVersionUID = -2266306977154288057L;

    //流水号
    @NotNull(message = "param_missing",groups = ValidationSorts.SortA1.class)
    @NotBlank(message = "param_blank",groups = ValidationSorts.SortA2.class)
    @Size(message = "param_format",max = 36,groups = ValidationSorts.SortA3.class)
    @Pattern(message = "param_format",regexp = "^[^ ]+$",groups = ValidationSorts.SortA4.class)
    private String uniqueCode ;


    public abstract Map<String, String> paramNameMap();

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }
}