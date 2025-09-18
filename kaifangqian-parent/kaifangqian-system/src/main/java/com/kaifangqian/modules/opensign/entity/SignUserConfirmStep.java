/**
 * @description 签署意愿校验子表
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
package com.kaifangqian.modules.opensign.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

@Data
@TableName("sign_user_confirm_step")
// @ApiModel("签署意愿校验子表")
public class SignUserConfirmStep implements Serializable {

    private static final long serialVersionUID = -1964598393002371754L;

    // @ApiModelProperty("主键")
    private String id;
    /**
     * 校验主表ID
     */
    private String confirmId;
    /**
     * 校验步骤
     */
    private Integer step;
    /**
     * 校验类型
     */
    private String confirmType;
    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 系统参数
     */
    private String systemPara;
    /**
     * 用户提交参数
     */
    private String userPara;

    /**
     * 校验结果
     */
    private Boolean confirmFlag;

    private Integer bindDataFlag;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;

    /**
     * 更新用户
     */
    private String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date updateTime;

    // @ApiModelProperty("删除标志：0未删除，1已删除")
    private Boolean deleteFlag;

}