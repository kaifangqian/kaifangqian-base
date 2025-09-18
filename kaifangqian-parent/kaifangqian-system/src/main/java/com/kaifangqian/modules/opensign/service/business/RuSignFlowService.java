/**
 * @description 业务线实例和驱动关联业务
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
package com.kaifangqian.modules.opensign.service.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 业务线实例和驱动关联业务
 * @Package: com.kaifangqian.modules.opensign.service.business
 * @ClassName: RuSignFlowService
 * @author: FengLai_Gong
 */
@Service
public class RuSignFlowService {


    @Autowired
    private RuBusinessService ruBusinessService ;


    /**
     * @Description #填写
     * @Param ruId 业务线实例id
     **/
    public void write(String ruId)  {
        ruBusinessService.write(ruId);
    }

    /**
     * @Description #自动签署
     * @Param signerType 1为发起方 2为接收方
     *        id 关联id，signerType为1时是senderId，为2时是signerId
     **/
    public void autoSign(String id , Integer signerType) {
        ruBusinessService.autoSign(id,signerType);
    }
}