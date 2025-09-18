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
public class YundunSignPositionArrayData implements Serializable {

    // @ApiModelProperty("文档id")
    private String docId;

    // @ApiModelProperty("签章位置")
    List<YundunSignPositionData> yundunSignPositionDataList;
}