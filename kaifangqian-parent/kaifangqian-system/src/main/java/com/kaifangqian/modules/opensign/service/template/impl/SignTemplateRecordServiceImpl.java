/**
 * @description 模板操作记录接口实现类
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
package com.kaifangqian.modules.opensign.service.template.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.common.base.entity.BaseEntity;
import com.kaifangqian.modules.opensign.entity.SignTemplateRecord;
import com.kaifangqian.modules.opensign.enums.SignCurrentEnum;
import com.kaifangqian.modules.opensign.mapper.SignTemplateRecordMapper;
import com.kaifangqian.modules.opensign.service.template.SignTemplateRecordService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Description: SignTemplateRecordServiceImpl
 * @Package: com.kaifangqian.modules.opensign.service.template.impl
 * @ClassName: SignTemplateRecordServiceImpl
 * @author: FengLai_Gong
 */
@Service
public class SignTemplateRecordServiceImpl extends ServiceImpl<SignTemplateRecordMapper, SignTemplateRecord> implements SignTemplateRecordService {


    @Override
    public SignTemplateRecord getCurrent(String templateId) {
        QueryWrapper<SignTemplateRecord> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        wrapper.lambda().eq(SignTemplateRecord::getTemplateId,templateId);
        wrapper.lambda().eq(SignTemplateRecord::getIsCurrent, SignCurrentEnum.IS_CURRENT.getCode());

        List<SignTemplateRecord> signTemplateRecords = this.baseMapper.selectList(wrapper);
        if(signTemplateRecords == null || signTemplateRecords.size() == 0){
            return null;
        }
        return signTemplateRecords.get(0);
    }

    @Override
    public Boolean setNotCurrent(String templateId) {

        QueryWrapper<SignTemplateRecord> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        wrapper.lambda().eq(SignTemplateRecord::getTemplateId,templateId);
        wrapper.lambda().eq(SignTemplateRecord::getIsCurrent, SignCurrentEnum.IS_CURRENT.getCode());

        SignTemplateRecord record = new SignTemplateRecord();
        record.setIsCurrent(SignCurrentEnum.NOT_CURRENT.getCode());
        record.setUpdateTime(new Date());

        this.baseMapper.update(record,wrapper);

        return true;
    }
}