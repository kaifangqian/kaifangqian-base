/**
 * @description 业务线权限控制表
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
import com.kaifangqian.common.base.entity.BaseEntity;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: SignReAuth
 * @Package: com.kaifangqian.modules.opensign.entity
 * @ClassName: SignReAuth
 * @author: FengLai_Gong
 */
@Data
@TableName("sign_re_auth")
// @ApiModel("业务线权限控制表")
public class SignReAuth extends BaseEntity implements Serializable {


    private static final long serialVersionUID = -2546725642070245357L;

    // @ApiModelProperty("'主键'")
    private String id ;

    // @ApiModelProperty("业务线主表id")
    private String signReId ;


    // @ApiModelProperty("'权限类型，1管理员，2使用范围，3查看权限，4下载权限'")
    private Integer authType ;

    // @ApiModelProperty("'用户类型'")
    private Integer userType ;

    // @ApiModelProperty("'用户id'")
    private String userId ;

    // @ApiModelProperty("'租户id'")
    private String tenantId ;


    // @ApiModelProperty("'下载权限类型，1参与人，2查看人，3全部'")
    private Integer downloaderType ;




}