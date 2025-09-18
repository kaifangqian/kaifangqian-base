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
package com.kaifangqian.modules.system.service.impl;

import com.kaifangqian.modules.system.entity.ThirdServiceRecord;
import com.kaifangqian.modules.system.mapper.ThirdServiceRecordMapper;
import com.kaifangqian.modules.system.service.IThirdServiceRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.utils.MyStringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 第三方接口调用记录表 服务实现类
 * </p>
 *
 * @author Administrator
 * @since 2023-10-10
 */
@Service
public class ThirdServiceRecordServiceImpl extends ServiceImpl<ThirdServiceRecordMapper, ThirdServiceRecord> implements IThirdServiceRecordService {

    @Override
    public ThirdServiceRecord saveThirdRecord(String serviceType, String serviceUrl, String reqPara,
                                              boolean successFlag, String resPara, String orderNo) {
        ThirdServiceRecord record = new ThirdServiceRecord();
        record.setServiceType(serviceType);
        record.setServiceUrl(serviceUrl);
        record.setReqPara(reqPara);
        if (MyStringUtils.isNotBlank(resPara) && resPara.length() > 1024) {
            record.setResPara(resPara.substring(0, 1024));
        } else {
            record.setResPara(resPara);
        }
        record.setSuccessFlag(successFlag);
        record.setOrderNo(orderNo);
        save(record);
        return record;
    }
}
