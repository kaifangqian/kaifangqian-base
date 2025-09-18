/**
 * @description 模板数据
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
package com.kaifangqian.modules.opensign.vo.base;

import com.kaifangqian.modules.storage.entity.AnnexStorage;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: TemplateVo
 * @Package: com.kaifangqian.modules.opensign.vo.base
 * @ClassName: TemplateVo
 * @author: FengLai_Gong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
// @ApiModel("模板数据")
public class TemplateInfo implements Serializable {

    private static final long serialVersionUID = -2494082873157926125L;

    // @ApiModelProperty("模板id")
    private String templateId;

    // @ApiModelProperty("模板申请id")
    private String templateApplyId ;

    // @ApiModelProperty("模板编号")
    private String templateCode ;

    // @ApiModelProperty("模板名称")
    private String templateName ;

    // @ApiModelProperty("业务类型字典id")
    private String businessType ;

    // @ApiModelProperty("模板类型（1、有参数模板；2、无参数模板；")
    private Integer templateType ;

    // @ApiModelProperty("签章id")
    private String sealId ;

    // @ApiModelProperty("备注")
    private String note ;

    // @ApiModelProperty("模板状态（制作中、制作失败、已停用、已启用）")
    private Integer templateStatus ;

    // @ApiModelProperty("申请状态（待提交、待重新提交、待审批、审批未通过、审批通过、作废）")
    private Integer applyStatus ;

    // @ApiModelProperty("文件id")
    private AnnexStorage annex ;



}