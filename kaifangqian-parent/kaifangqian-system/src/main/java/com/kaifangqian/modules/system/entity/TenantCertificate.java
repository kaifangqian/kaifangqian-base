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
package com.kaifangqian.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 租户证书关联表
 * </p>
 *
 * @author Administrator
 * @since 2023-10-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tenant_certificate")
public class TenantCertificate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    // @ApiModelProperty(value = "主键")
    private String id;

    @TableField("tenant_id")
    private String tenantId;

    @TableField("cert_id")
    private String certId;

    /**
     * 证书状态: 正常、吊销、失效
     */
    @TableField("cert_status")
    private Integer certStatus;

    /**
     * 证书类型：CA证书、平台放篡改证书,事件证书
     */
    @TableField("cert_type")
    private Integer certType;

    /**
     * 不可用原因
     */
    @TableField("notes")
    private String notes;

    /**
     * 证书所属类型（1、个人；2、企业)
     */
    @TableField("holder_type")
    private Integer holderType ;


    public static final String ID = "id";

    public static final String TENANT_ID = "tenant_id";

    public static final String CERT_ID = "cert_id";

    public static final String CERT_STATUS = "cert_status";

    public static final String CERT_TYPE = "cert_type";

    public static final String NOTES = "notes";


}
