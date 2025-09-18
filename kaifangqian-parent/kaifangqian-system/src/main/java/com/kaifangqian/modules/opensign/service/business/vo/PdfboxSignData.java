package com.kaifangqian.modules.opensign.service.business.vo;

// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: YundunSignPositionData
 * @Package: com.kaifangqian.modules.opensign.service.business.vo
 * @ClassName: CompletedSignVo
 * @author: FengLai_Gong
 */
@Data
public class PdfboxSignData implements Serializable {

    // @ApiModelProperty("签署文件")
    private byte[] signedFile;

    // @ApiModelProperty("签名字段")
    private Integer offset;
}