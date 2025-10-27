/**
 * @description 文档详情数据对象
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
package com.kaifangqian.modules.opensign.vo.base.sign;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kaifangqian.modules.opensign.entity.SignRu;
import com.kaifangqian.modules.opensign.service.business.vo.YundunSignPositionArrayData;
import com.kaifangqian.modules.storage.entity.AnnexStorage;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: DocInfo
 * @Package: com.kaifangqian.modules.opensign.vo.base
 * @ClassName: DocInfo
 * @author: FengLai_Gong
 */
@Data
// @ApiModel("文档详情数据对象")
public class PdfSignVoInfo implements Serializable {

    private static final long serialVersionUID = -4829924328211608565L;

    /**
     * 合同ID
     */
    private String contractId;
    /**
     * 应用名称
     */
    private String appName;
    /**
     * 应用ID
     */
    private String appId;
    /**
     * 新文档文件字节映射
     */
    private Map<String, byte[]> newDocFileByteMap;
    /**
     * 企业印章字节数据
     */
    private byte[] entSealByte;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 证书持有者租户ID
     */
    private String certHolderTenantId;
    /**
     * 签署规则实体
     */
    private SignRu signRu;
    /**
     * 云盾签署位置数组数据列表
     */
    private List<YundunSignPositionArrayData> yundunSignPositionArrayDatas;
    /**
     * 签署类型
     */
    private String signType;
    /**
     * 个人签署认证类型
     */
    private String personalSignAuthType;
}