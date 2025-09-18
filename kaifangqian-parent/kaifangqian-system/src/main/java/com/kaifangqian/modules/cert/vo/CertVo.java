package com.kaifangqian.modules.cert.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: CertVo
 * @Package: com.kaifangqian.modules.cert.vo
 * @ClassName: CertVo
 * @author: FengLai_Gong
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
// @ApiModel("证书数据")
public class CertVo implements Serializable {

    private static final long serialVersionUID = -1934450663576120807L;

    // @ApiModelProperty("主键")
    private String id;

    // @ApiModelProperty("'证书颁发时间'")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date issueTime ;

    // @ApiModelProperty("证书序列号")
    private String certSuqeNo;

    // @ApiModelProperty("使用者名称")
    private String userName ;

    // @ApiModelProperty("证书所属类型（1、ca根证书；2、系统防篡改根证书；3、ca个人证书；4、ca企业证书；5、系统防篡改个人证书；6、系统防篡改企业证书）")
    private Integer certType ;

//    // @ApiModelProperty("证书所属类型（1、ca根证书；2、系统防篡改根证书；3、ca个人证书；4、ca企业证书；5、系统防篡改个人证书；6、系统防篡改企业证书）")
//    private String certTypeName ;

    // @ApiModelProperty("颁发机构")
    private String issueOrg ;

    // @ApiModelProperty("加密算法（1、RSA；2、SM2）")
    private String algorithmType ;

    // @ApiModelProperty("存储介质")
    private String storageMedium ;

    // @ApiModelProperty("证书文件地址")
    private String pfxPath ;

    // @ApiModelProperty("证书有效状态（1、有效；2、吊销;3、失效")
    private Integer certStatus ;

    // @ApiModelProperty("证书有效期起始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date termOfValidityStartTime ;

    // @ApiModelProperty("证书有效期结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date termOfValidityEndTime ;

    // @ApiModelProperty("备注")
    private String notes;
}