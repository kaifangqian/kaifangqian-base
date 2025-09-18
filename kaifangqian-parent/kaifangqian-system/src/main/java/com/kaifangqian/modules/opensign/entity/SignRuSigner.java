/**
 * @description 业务线实例-签署人主表
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
 * @Description: SignRuSigner
 * @Package: com.kaifangqian.modules.opensign.entity
 * @ClassName: SignRuDocControl
 * @author: FengLai_Gong
 */
@Data
@TableName("sign_ru_signer")
// @ApiModel("业务线实例-签署人主表")
public class SignRuSigner extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -6541488767464512447L;

    // @ApiModelProperty("'主键'")
    private String id ;

    // @ApiModelProperty("'业务线实例主表id'")
    private String signRuId ;


    // @ApiModelProperty("'签署方类型，1发起方，2外部个人接收方 3外部企业接收方'")
    private Integer signerType ;

    // @ApiModelProperty("'签署方名称'")
    private String signerName ;

    // @ApiModelProperty("'签署方顺序'")
    private Integer signerOrder ;

    // @ApiModelProperty("'外部签署人类型，1手机号，2邮箱号'")
    private Integer signerExternalType ;

    // @ApiModelProperty("'外部签署人接受值'")
    private String signerExternalValue ;

    // @ApiModelProperty("签署人租户下用户id")
    private String signerUserId ;

    // @ApiModelProperty("是否需要签署")
    private Boolean signFlag ;

}