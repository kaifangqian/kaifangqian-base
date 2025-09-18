package com.kaifangqian.modules.cert.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: ShortTermVo
 * @Package: com.kaifangqian.modules.cert.vo
 * @ClassName: ShortTermVo
 * @author: FengLai_Gong
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShortTermVo implements Serializable {

    private static final long serialVersionUID = -886864817447786296L;

    private Integer code ;

    private String mes ;

    private String uniqueCode ;

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