/**
 * @description 签署业务权限对象类
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
package com.kaifangqian.modules.opensign.service.auth.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: BusinessAuthQueryVo
 * @Package: com.kaifangqian.modules.opensign.service.auth.vo
 * @ClassName: BusinessAuthQueryVo
 * @author: FengLai_Gong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessAuthQueryVo implements Serializable {

    private static final long serialVersionUID = 2243719949765310751L;


    //租户下用户id
    private String tenantUserId ;
    //组织架构id
    private String departId ;
    //角色id列表
    private List<String> roleIds ;
    //业务关联id
    private String businessRelationId ;
    //业务类型，1为签章，2为模板，3为文档，4为业务线
    private Integer businessType ;

    //业务类型角色，具体数值参照枚举类,1印章管理员,2印章审计者,3印章使用者
    private List<Integer> businessTypeRoleList ;



}