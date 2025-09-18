/**
 * @description 业务线实例-实例关联人表
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
 * @Description: SignRurelation
 * @Package: com.kaifangqian.modules.opensign.entity
 * @ClassName: SignRurelation
 * @author: FengLai_Gong
 */
@Data
@TableName("sign_ru_relation")
// @ApiModel("业务线实例-实例关联人表")
public class SignRuRelation extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 873174720695070012L;

    // @ApiModelProperty("主键")
    private String id;

    // @ApiModelProperty("'业务线实例主表id'")
    private String signRuId ;

    // @ApiModelProperty("关联类型,1发起人，2抄送人")
    private Integer relationType;

    // @ApiModelProperty("租户下用户id，发起人id，抄送人id")
    private String tenantUserId;

    // @ApiModelProperty("'节点关联类型:system,bind,register'")
    private String taskLinkType;

    // @ApiModelProperty("'外部抄送人抄送类型，1手机号，2邮箱号'")
    private Integer externalCcedType;

    // @ApiModelProperty("'外部抄送人抄送值'")
    private String externalCcedValue;

    // @ApiModelProperty("'抄送人类型，1内部，2外部'")
    private transient Integer ccerType ;
}