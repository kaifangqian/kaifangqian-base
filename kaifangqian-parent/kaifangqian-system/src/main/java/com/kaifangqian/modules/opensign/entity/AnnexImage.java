/**
 * @description 文件转换图片记录表
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
 * @Description: AnnexImage
 * @Package: com.kaifangqian.modules.opensign.entity
 * @ClassName: SignRuDocControl
 * @author: FengLai_Gong
 */
@Data
@TableName("annex_image")
// @ApiModel("文件转换图片记录表")
public class AnnexImage extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -1964598393002371754L;

    // @ApiModelProperty("'主键'")
    private String id ;

    // @ApiModelProperty("图片真实文件id")
    private String imageAnnexId ;

    // @ApiModelProperty("关联真实文件id")
    private String annexId ;

    // @ApiModelProperty("页码")
    private Integer page ;

    // @ApiModelProperty("图片宽")
    private String imageWidth ;

    // @ApiModelProperty("图片高")
    private String imageHeight ;

}