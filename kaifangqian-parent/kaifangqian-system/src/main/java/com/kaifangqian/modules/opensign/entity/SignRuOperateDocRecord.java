/**
 * @description 业务线实例-签署操作文件记录表
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
 * @Description: SignRuOperateDocRecord
 * @Package: com.kaifangqian.modules.opensign.entity
 * @ClassName: SignRuOperateDocRecord
 * @author: FengLai_Gong
 */
@Data
@TableName("sign_ru_operate_doc_record")
// @ApiModel("业务线实例-签署操作文件记录表")
public class SignRuOperateDocRecord extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -504399301039175150L;

    // @ApiModelProperty("主键")
    private String id ;

    // @ApiModelProperty("签署操作记录表id")
    private String operateRecordId ;

    // @ApiModelProperty("文档ID")
    private String docId ;

    // @ApiModelProperty("上次签约文件记录表id")
    private String previousDocOperateId ;

    // @ApiModelProperty("上次签约文件id")
    private String previousDocOperateAnnexId ;

    // @ApiModelProperty("本次签约文件记录表id")
    private String currentDocOperateId ;

    // @ApiModelProperty("本次签约文件id")
    private String currentDocOperateAnnexId ;




}