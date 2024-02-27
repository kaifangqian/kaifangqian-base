package org.resrun.service.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class SignatureDetail {

        /**
         * 数字签名名称
         */
        @ApiModelProperty(value = "数字签名名称")
        private String signName;
        /**
         * 签名日期时间
         */
        @ApiModelProperty(value = "签名日期时间")
        private Date signTime;
        /**
         * 有效期开始时间
         */
        @ApiModelProperty(value = "有效期开始时间")
        private Date validStartTime;
        /**
         * 有效期结束时间
         */
        @ApiModelProperty(value = "有效期结束时间")
        private Date validEndTime;
        /**
         * 证书名称
         */
        @ApiModelProperty(value = "证书名称")
        private String certName;
        /**
         * 证书序列号
         */
        @ApiModelProperty(value = "证书序列号")
        private String serialNumber;
        /**
         * 证书公钥
         */
        @ApiModelProperty(value = "证书公钥")
        private String publicKey;
        /**
         * 证书格式
         */
        @ApiModelProperty(value = "证书格式")
        private String pubKeyFormat;
        /**
         * 证书签名算法
         */
        @ApiModelProperty(value = "证书签名算法")
        private String sigAlgName;
        /**
         * 证书颁发者
         */
        @ApiModelProperty(value = "证书颁发者")
        private String userDnName;
        /**
         * 是否被篡改
         */
        @ApiModelProperty(value = "是否被篡改")
        private boolean validate;

        @ApiModelProperty(value = "图片内容")
        private String imageContent;
        /**
         * 图片路径
         */
        @ApiModelProperty(value = "图片路径")
        private String imagePath;

        /**
         * 签名位置
         */
        @ApiModelProperty(value = "签名位置")
        private String reason;
        /**
         * 签名类型
         */
        @ApiModelProperty(value = "签名类型")
        private String location;

        /**
         * 签名页面
         */
        @ApiModelProperty(value = "签名页面")
        private int pageNum;

        /**
         * 图片的base64编码
         */
        @ApiModelProperty(value = "图片的base64编码")
        private String sealBase64;

        /**
         * 文件摘要
         */
        @ApiModelProperty(value = "文件摘要")
        private String fileDigest;

        private float llx, lly, urx, ury;
        /**
         * 文件路径
         */
        // public String filePath;
}
