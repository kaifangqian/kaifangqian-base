/**
 * @description 数字签名信息
 *
 * Copyright (C) [2025] [版权所有者（北京资源律动科技有限公司）]. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * 注意：本代码基于 AGPLv3 协议发布。若通过网络提供服务（如 Web 应用），
 * 必须公开修改后的完整源代码（包括衍生作品），详见协议全文。
 */
package com.kaifangqian.modules.opensign.vo.base;

// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import com.kaifangqian.modules.opensign.enums.SignStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description 数字签名信息
 * @create Fusion
 * @date 2023/8/21 20:23
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
// @ApiModel("数字签名信息返回对象")
public class SignPdfInfoVo {
    /**
     * PDF名称
     */
    // @ApiModelProperty(value = "PDF名称")
    private String pdfName;
    /**
     * PDF大小
     */
    // @ApiModelProperty(value = "PDF大小")
    private String pdfSize;

    /**
     * 文件签名结果
     */
    // @ApiModelProperty(value = "文件签名结果")
    private Integer pdfSingResult = 0;


    public void setPdfSingResult(Integer pdfSingResult){
        if(this.pdfSingResult.intValue() != SignStatus.SIGN_STATUS_FIDDLE.getCode() ||this.pdfSingResult.intValue() != SignStatus.SIGN_STATUS_ERROR.getCode()){
            this.pdfSingResult = pdfSingResult;
        }
    }

    // @ApiModelProperty(value = "数字签名详细集合")
    private List<SignatureDetail> signatureDetails = new ArrayList<>();

    @Data
    public static class SignatureDetail {
        /**
         * 数字签名名称
         */
        // @ApiModelProperty(value = "数字签名名称")
        public String signName;
        /**
         * 签名日期时间
         */
        // @ApiModelProperty(value = "签名日期时间")
        public Date signTime;
        /**
         * 有效期开始时间
         */
        // @ApiModelProperty(value = "有效期开始时间")
        public Date validStartTime;
        /**
         * 有效期结束时间
         */
        // @ApiModelProperty(value = "有效期结束时间")
        public Date validEndTime;
        /**
         * 证书名称
         */
        // @ApiModelProperty(value = "证书名称")
        public String certName;
        /**
         * 证书序列号
         */
        // @ApiModelProperty(value = "证书序列号")
        public String serialNumber;
        /**
         * 证书公钥
         */
        // @ApiModelProperty(value = "证书公钥")
        public String publicKey;
        /**
         * 证书格式
         */
        // @ApiModelProperty(value = "证书格式")
        public String pubKeyFormat;
        /**
         * 证书签名算法
         */
        // @ApiModelProperty(value = "证书签名算法")
        public String sigAlgName;
        /**
         * 证书颁发者
         */
        // @ApiModelProperty(value = "证书颁发者")
        public String userDnName;
        /**
         * 是否被篡改
         */
        // @ApiModelProperty(value = "是否被篡改")
        public boolean validate;

        // @ApiModelProperty(value = "图片内容")
        public String imageContent;
        /**
         * 图片路径
         */
        // @ApiModelProperty(value = "图片路径")
        public String imagePath;

        /**
         * 签名位置
         */
        // @ApiModelProperty(value = "签名位置")
        public String reason;
        /**
         * 签名类型
         */
        // @ApiModelProperty(value = "签名类型")
        public String location;

        /**
         * 签名页面
         */
        // @ApiModelProperty(value = "签名页面")
        public int pageNum;

        /**
         * 图片的base64编码
         */
        // @ApiModelProperty(value = "图片的base64编码")
        public String sealBase64;

        /**
         * 文件摘要
         */
        // @ApiModelProperty(value = "文件摘要")
        public String fileDigest;

        public float llx, lly, urx, ury;

        private Integer pdfSingResult;
        /**
         * 文件路径
         */
        // public String filePath;
    }

}
