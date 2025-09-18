/**
 * @description 文档签署图片记录
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
 * @Description: SignDocImageRecord
 * @Package: com.kaifangqian.modules.opensign.entity
 * @ClassName: SignDocImageRecord
 * @author: FengLai_Gong
 */
@Data
@TableName("sign_doc_image_record")
// @ApiModel("文档签署图片记录")
public class SignDocImageRecord extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 6526597812409385329L;

    // @ApiModelProperty("主键")
    private String id ;

    // @ApiModelProperty("文档id")
    private String docId ;

    // @ApiModelProperty("文档操作记录id")
    private String docRecordId ;

    // @ApiModelProperty("签署文档页数")
    private Integer docPage ;

    // @ApiModelProperty("是否为最新签署的，1为是，2为否")
    private Integer isCurrent ;

    // @ApiModelProperty("文件id")
    private String annexId ;



}