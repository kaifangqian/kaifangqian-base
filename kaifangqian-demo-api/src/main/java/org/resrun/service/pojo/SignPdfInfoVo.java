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

    public static class SignatureDetail {
        /**
         * 数字签名名称
         */
        @ApiModelProperty(value = "数字签名名称")
        public String signName;
        /**
         * 签名日期时间
         */
        @ApiModelProperty(value = "签名日期时间")
        public Date signTime;
        /**
         * 有效期开始时间
         */
        @ApiModelProperty(value = "有效期开始时间")
        public Date validStartTime;
        /**
         * 有效期结束时间
         */
        @ApiModelProperty(value = "有效期结束时间")
        public Date validEndTime;
        /**
         * 证书名称
         */
        @ApiModelProperty(value = "证书名称")
        public String certName;
        /**
         * 证书序列号
         */
        @ApiModelProperty(value = "证书序列号")
        public String serialNumber;
        /**
         * 证书公钥
         */
        @ApiModelProperty(value = "证书公钥")
        public String publicKey;
        /**
         * 证书格式
         */
        @ApiModelProperty(value = "证书格式")
        public String pubKeyFormat;
        /**
         * 证书签名算法
         */
        @ApiModelProperty(value = "证书签名算法")
        public String sigAlgName;
        /**
         * 证书颁发者
         */
        @ApiModelProperty(value = "证书颁发者")
        public String userDnName;
        /**
         * 是否被篡改
         */
        @ApiModelProperty(value = "是否被篡改")
        public boolean validate;

        @ApiModelProperty(value = "图片内容")
        public String imageContent;
        /**
         * 图片路径
         */
        @ApiModelProperty(value = "图片路径")
        public String imagePath;

        /**
         * 签名位置
         */
        @ApiModelProperty(value = "签名位置")
        public String reason;
        /**
         * 签名类型
         */
        @ApiModelProperty(value = "签名类型")
        public String location;

        /**
         * 签名页面
         */
        @ApiModelProperty(value = "签名页面")
        public int pageNum;

        /**
         * 图片的base64编码
         */
        @ApiModelProperty(value = "图片的base64编码")
        public String sealBase64;

        /**
         * 文件摘要
         */
        @ApiModelProperty(value = "文件摘要")
        public String fileDigest;

        public float llx, lly, urx, ury;
        /**
         * 文件路径
         */
        // public String filePath;
    }

}
