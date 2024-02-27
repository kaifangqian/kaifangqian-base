package org.resrun.service.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 数字签名信息
 * @Package: org.resrun.service.pojo
 * @ClassName: SignPdfInfoVo
 * @copyright 北京资源律动科技有限公司
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("数字签名信息返回对象")
public class SignPdfInfoVo {
    /**
     * PDF名称
     */
    @ApiModelProperty(value = "PDF名称")
    private String pdfName;
    /**
     * PDF大小
     */
    @ApiModelProperty(value = "PDF大小")
    private String pdfSize;

    /**
     * 文件签名结果
     */
    @ApiModelProperty(value = "文件签名结果")
    private Integer pdfSingResult;

    @ApiModelProperty(value = "数字签名详细集合")
    private List<SignatureDetail> signatureDetails = new ArrayList<>();

}
