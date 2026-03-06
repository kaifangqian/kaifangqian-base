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

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @Description: DocInfo
 * @Package: com.kaifangqian.modules.opensign.vo.base
 * @ClassName: DocInfo
 * @author: FengLai_Gong
 */
@Data
// @ApiModel("文档详情数据对象")
public class PdfSignResult implements Serializable {

    private static final long serialVersionUID = -4829924328211608565L;

    /**
     * 新文档文件字节映射
     */
    private Map<String, byte[]> newDocFileByteMap;
    /**
     * 最终签署类型
     */
    private Integer finalSignType;

    /**
     * 个人签署实名要求
     */
    private String personalSignAuth;

    /**
     * 签署认证类型
     */
    private Integer authType;

    /**
     * 签署订单号
     */
    private String signOrderNo;

}