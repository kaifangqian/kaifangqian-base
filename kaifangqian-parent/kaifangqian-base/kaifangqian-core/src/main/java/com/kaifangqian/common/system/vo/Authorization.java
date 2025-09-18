/**
 * @description 产品授权版本
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
package com.kaifangqian.common.system.vo;
import com.fasterxml.jackson.annotation.JsonFormat;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author : zhenghuihan
 * create at: 2023/12/26
 */
@Data
public class Authorization {
    //-----------整体配置-------------//
    /**
     * 版本
     * 基础版本；专业版；旗舰版；定制版。不做判断，仅作为授权的版本记录
     */
    // @ApiModelProperty(value = "版本")
    private String version;
    /**
     * test-测试授权；prod-正式授权；
     */
    // @ApiModelProperty(value = "授权类型")
    private String environment;
    /**
     * 授权开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    // @ApiModelProperty(value = "授权开始时间")
    private Date licenseStart;
    /**
     * 授权到期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    // @ApiModelProperty(value = "授权到期时间")
    private Date licenseExpire;


    //-----------模版配置-------------//
    /**
     * 模版是否可用
     * true-可用；false-不可用
     */
    // @ApiModelProperty(value = "模版是否可用")
    private boolean templateUseFlag;
    /**
     * 模版可用的上限数量
     */
    // @ApiModelProperty(value = "模版可用的上限数量")
    private Integer templateCeiling;
    /**
     * 模版可用控件范围
     * base-基础控件；为空-不限制
     */
    // @ApiModelProperty(value = "模版可用控件范围")
    private String templateScope;


    //-----------业务线配置-------------//
    /**
     * 业务线是否可用
     * true-可用；false-不可用
     */
    // @ApiModelProperty(value = "业务线是否可用")
    private boolean businessLineUseFlag;
    /**
     * 业务线可用的上限数量
     */
    // @ApiModelProperty(value = "业务线可用的上限数量")
    private Integer businessLineCeiling;


    //-----------API接口授权配置-------------//
    /**
     * API接口授权是否可用
     * true-可用；false-不可用
     */
    // @ApiModelProperty(value = "API接口授权是否可用")
    private boolean apiAuthorizationUseFlag;
    /**
     * API接口授权可用的上限数量
     */
    // @ApiModelProperty(value = "API接口授权可用的上限数量")
    private Integer apiAuthorizationCeiling;


    //-----------大B数量配置-------------//
    /**
     * 大B数量是否可用
     * true-可用；false-不可用
     */
    // @ApiModelProperty(value = "大B数量是否可用")
    private boolean coreFirmUseFlag;
    /**
     * 大B数量可用的上限数量
     */
    // @ApiModelProperty(value = "大B数量可用的上限数量")
    private Integer coreFirmCeiling;


    //-----------人脸识别是否可用配置-------------//
    /**
     * 人脸识别是否可用
     * true-可用；false-不可用
     */
    // @ApiModelProperty(value = "人脸识别是否可用")
    private boolean faceRecognitionUseFlag;


    //-----------单次签署签约文件数量配置-------------//
    /**
     * 单次签署签约文件上限数量
     */
    // @ApiModelProperty(value = "单次签署签约文件上限数量")
    private Integer documentsCeiling;


    //-----------骑缝章签署配置-------------//
    /**
     * 骑缝章签署是否可用
     * true-可用；false-不可用
     */
    // @ApiModelProperty(value = "骑缝章签署是否可用")
    private boolean pagingSealUseFlag;

    //-----------网站信息自定义配置-------------//
    /**
     * 网站信息自定义是否可用
     * true-可用；false-不可用
     */
    // @ApiModelProperty(value = "网站信息自定义是否可用")
    private boolean websiteCustomizateUseFlag;


}