package com.kaifangqian.modules.cert.vo;

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
 * @Description: CertPageRequest
 * @Package: com.kaifangqian.modules.cert.vo
 * @ClassName: CertPageRequest
 * @author: FengLai_Gong
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
// @ApiModel("证书列表查询请求对象")
public class CertPageRequest implements Serializable {

    private static final long serialVersionUID = 5176879781451290724L;

    // @ApiModelProperty("使用者名称")
    private String userName ;

    // @ApiModelProperty("证书所属类型（1、ca根证书；2、系统防篡改根证书；3、ca个人证书；4、ca企业证书；5、系统防篡改个人证书；6、系统防篡改企业证书）")
    private Integer certType ;

    // @ApiModelProperty("证书有效状态（1、有效；2、吊销;3、失效")
    private Integer certStatus ;

    // @ApiModelProperty("证书有效期-开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date termStartTime ;

    // @ApiModelProperty("证书有效期-结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date termEndTime ;

    // @ApiModelProperty("证书颁发时间-开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date issueTimeStartTime ;

    // @ApiModelProperty("证书颁发时间-结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date issueTimeEndTime ;

    // @ApiModelProperty("页码")
    Integer pageNo = 1;

    // @ApiModelProperty("页码大小")
    Integer pageSize = 10 ;




}