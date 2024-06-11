package org.resrun.service.pojo;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

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


        public String getSignName() {
                return signName;
        }

        public void setSignName(String signName) {
                this.signName = signName;
        }

        public Date getSignTime() {
                return signTime;
        }

        public void setSignTime(Date signTime) {
                this.signTime = signTime;
        }

        public Date getValidStartTime() {
                return validStartTime;
        }

        public void setValidStartTime(Date validStartTime) {
                this.validStartTime = validStartTime;
        }

        public Date getValidEndTime() {
                return validEndTime;
        }

        public void setValidEndTime(Date validEndTime) {
                this.validEndTime = validEndTime;
        }

        public String getCertName() {
                return certName;
        }

        public void setCertName(String certName) {
                this.certName = certName;
        }

        public String getSerialNumber() {
                return serialNumber;
        }

        public void setSerialNumber(String serialNumber) {
                this.serialNumber = serialNumber;
        }

        public String getPublicKey() {
                return publicKey;
        }

        public void setPublicKey(String publicKey) {
                this.publicKey = publicKey;
        }

        public String getPubKeyFormat() {
                return pubKeyFormat;
        }

        public void setPubKeyFormat(String pubKeyFormat) {
                this.pubKeyFormat = pubKeyFormat;
        }

        public String getSigAlgName() {
                return sigAlgName;
        }

        public void setSigAlgName(String sigAlgName) {
                this.sigAlgName = sigAlgName;
        }

        public String getUserDnName() {
                return userDnName;
        }

        public void setUserDnName(String userDnName) {
                this.userDnName = userDnName;
        }

        public boolean isValidate() {
                return validate;
        }

        public void setValidate(boolean validate) {
                this.validate = validate;
        }

        public String getImageContent() {
                return imageContent;
        }

        public void setImageContent(String imageContent) {
                this.imageContent = imageContent;
        }

        public String getImagePath() {
                return imagePath;
        }

        public void setImagePath(String imagePath) {
                this.imagePath = imagePath;
        }

        public String getReason() {
                return reason;
        }

        public void setReason(String reason) {
                this.reason = reason;
        }

        public String getLocation() {
                return location;
        }

        public void setLocation(String location) {
                this.location = location;
        }

        public int getPageNum() {
                return pageNum;
        }

        public void setPageNum(int pageNum) {
                this.pageNum = pageNum;
        }

        public String getSealBase64() {
                return sealBase64;
        }

        public void setSealBase64(String sealBase64) {
                this.sealBase64 = sealBase64;
        }

        public String getFileDigest() {
                return fileDigest;
        }

        public void setFileDigest(String fileDigest) {
                this.fileDigest = fileDigest;
        }

        public float getLlx() {
                return llx;
        }

        public void setLlx(float llx) {
                this.llx = llx;
        }

        public float getLly() {
                return lly;
        }

        public void setLly(float lly) {
                this.lly = lly;
        }

        public float getUrx() {
                return urx;
        }

        public void setUrx(float urx) {
                this.urx = urx;
        }

        public float getUry() {
                return ury;
        }

        public void setUry(float ury) {
                this.ury = ury;
        }
}
