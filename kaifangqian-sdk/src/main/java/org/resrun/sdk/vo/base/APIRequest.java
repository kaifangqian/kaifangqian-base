package org.resrun.sdk.vo.base;

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
    private String uniqueCode;

    public abstract Map<String, String> paramNameMap();

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }
}