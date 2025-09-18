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

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author : zhenghuihan
 * create at:  2022/12/26  10:36
 * @description: 应用管理VO
 */
@Data
public class SysAppInfoVO {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    // @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 名称
     */
    // @ApiModelProperty(value = "名称")
    private String appName;
    /**
     * 图标
     */
    // @ApiModelProperty(value = "图标")
    private String appIcon;
    /**
     * 描述
     */
    // @ApiModelProperty(value = "描述")
    private String appDesc;
    /**
     * 是否已经拥有
     */
    // @ApiModelProperty(value = "是否已经拥有")
    private boolean containsFlag = false;

    private List<SysAppVersionVO> appVersionVOS;
}