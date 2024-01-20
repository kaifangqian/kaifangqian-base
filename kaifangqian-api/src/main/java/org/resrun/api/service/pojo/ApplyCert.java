package org.resrun.api.service.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: 申请证书-返回对象
 * @Package: org.resrun.modules.cert.pojo
 * @ClassName: ApplyCert
 * @author: FengLai_Gong
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApplyCert implements Serializable {

    private static final long serialVersionUID = -2734385624897679135L;

    private Integer code ;

    private String message ;

    private String uniqueCode ;

    private ApplyCertData data ;

}