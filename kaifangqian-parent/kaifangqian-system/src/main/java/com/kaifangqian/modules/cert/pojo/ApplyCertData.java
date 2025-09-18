package com.kaifangqian.modules.cert.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: ApplyCertData
 * @Package: com.kaifangqian.modules.cert.pojo
 * @ClassName: ApplyCertData
 * @author: FengLai_Gong
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApplyCertData implements Serializable {

    private static final long serialVersionUID = 3447485591197821614L;

    private String p7b ;

    private String certSN ;

    private String certNO ;

    private String certSubject ;

    //时间格式 20231229102348
    private String certValidityNotBefore ;

    //时间格式 20231230102348
    private String certValidityNotAfter ;

    private Integer certValidity ;


}