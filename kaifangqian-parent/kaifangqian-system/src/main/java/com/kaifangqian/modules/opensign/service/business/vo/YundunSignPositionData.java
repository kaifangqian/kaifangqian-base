package com.kaifangqian.modules.opensign.service.business.vo;

import com.kaifangqian.modules.opensign.service.tool.pojo.RealPositionProperty;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: YundunSignPositionData
 * @Package: com.kaifangqian.modules.opensign.service.business.vo
 * @ClassName: CompletedSignVo
 * @author: FengLai_Gong
 */
@Data
public class YundunSignPositionData implements Serializable {

    // @ApiModelProperty("签章图片")
    private byte[] sealImgByte ;

    // @ApiModelProperty("签章位置")
    RealPositionProperty sealPosition ;

}