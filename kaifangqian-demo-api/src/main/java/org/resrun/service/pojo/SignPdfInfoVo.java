package org.resrun.service.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 数字签名信息
 * @Package: org.resrun.service.pojo
 * @ClassName: SignPdfInfoVo
 * @copyright 北京资源律动科技有限公司
 */

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

    public String getPdfName() {
        return pdfName;
    }

    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }

    public String getPdfSize() {
        return pdfSize;
    }

    public void setPdfSize(String pdfSize) {
        this.pdfSize = pdfSize;
    }

    public Integer getPdfSingResult() {
        return pdfSingResult;
    }

    public void setPdfSingResult(Integer pdfSingResult) {
        this.pdfSingResult = pdfSingResult;
    }

    public List<SignatureDetail> getSignatureDetails() {
        return signatureDetails;
    }

    public void setSignatureDetails(List<SignatureDetail> signatureDetails) {
        this.signatureDetails = signatureDetails;
    }
}
