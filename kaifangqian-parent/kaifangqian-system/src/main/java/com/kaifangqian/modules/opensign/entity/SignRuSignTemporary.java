/**
 * @description 业务线实例-提交签署临时数据
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

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kaifangqian.common.base.entity.BaseEntity;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: SignRuSignTemporary
 * @Package: com.kaifangqian.modules.opensign.entity
 * @ClassName: SignRuSignTemporary
 * @author: FengLai_Gong
 */
@Data
@TableName("sign_ru_sign_temporary")
// @ApiModel("业务线实例-提交签署临时数据")
public class SignRuSignTemporary extends BaseEntity implements Serializable {


    private static final long serialVersionUID = -5051322056200600601L;

    @TableId(type = IdType.ASSIGN_ID)
    // @ApiModelProperty("主键")
    private String id;

    // @ApiModelProperty("签署意愿校验订单号")
    private String signConfirmOrderNo;

    // @ApiModelProperty("业务线实例id")
    private String signRuId ;

    // @ApiModelProperty("任务id")
    private String taskId ;

    // @ApiModelProperty("参数")
    private String params ;

    // @ApiModelProperty("状态,0是进行中，1是已完成")
    private Integer status ;


}