/**
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
package com.kaifangqian.modules.system.vo;

import com.kaifangqian.modules.system.entity.SysTenantUser;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author : zhenghuihan
 * create at:  2023/1/30  14:28
 * @description: 开通租户信息
 */
@Data
public class TenantCompanyInfoDTO {
    /**
     * 主键
     */
    private String id;
    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 姓名（个人、企业、工作站名称
     */
    // @ApiModelProperty(value = "姓名（个人、企业、工作站名称")
    private String name;
    /**
     * 单位证件号码
     */
    // @ApiModelProperty(value = "单位证件号码")
    private String organizationNo;
    /**
     * 注册时间
     */
    // @ApiModelProperty(value = "注册时间")
    private String createTime;
    /**
     * 认证通过时间
     */
    // @ApiModelProperty(value = "认证通过时间")
    private String verifyTime;

    /**
     * 企业管理员
     */
    // @ApiModelProperty(value = "企业管理员")
    private List<SysTenantUser> sysTenantUsers;

    // @ApiModelProperty(value = "企业管理员-用户信息")
    private List<SysUserForAddCompanyVO> sysUserVO;

    // @ApiModelProperty(value = "企业是否存在,0,不存在；1，存在，已实名")
    private Integer isExist;
}