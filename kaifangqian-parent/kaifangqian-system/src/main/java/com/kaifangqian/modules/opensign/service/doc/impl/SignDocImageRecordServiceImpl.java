/**
 * @description 签署文档图片转换数据记录接口实现类
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
package com.kaifangqian.modules.opensign.service.doc.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.common.base.entity.BaseEntity;
import com.kaifangqian.modules.opensign.entity.SignDocImageRecord;
import com.kaifangqian.modules.opensign.enums.SignCurrentEnum;
import com.kaifangqian.modules.opensign.mapper.SignDocImageRecordMapper;
import com.kaifangqian.modules.opensign.service.doc.SignDocImageRecordService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: SignDocImageRecordServiceImpl
 * @Package: com.kaifangqian.modules.opensign.service.doc.impl
 * @ClassName: SignDocImageRecordServiceImpl
 * @author: FengLai_Gong
 */
@Service
public class SignDocImageRecordServiceImpl extends ServiceImpl<SignDocImageRecordMapper, SignDocImageRecord> implements SignDocImageRecordService {



    @Override
    public List<SignDocImageRecord> getCurrentList(String docId) {
        QueryWrapper<SignDocImageRecord> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        wrapper.lambda().eq(SignDocImageRecord::getDocId,docId);
        wrapper.lambda().eq(SignDocImageRecord::getIsCurrent, SignCurrentEnum.IS_CURRENT.getCode());
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public Integer countCurrentList(String docId) {
        QueryWrapper<SignDocImageRecord> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        wrapper.lambda().eq(SignDocImageRecord::getDocId,docId);
        wrapper.lambda().eq(SignDocImageRecord::getIsCurrent, SignCurrentEnum.IS_CURRENT.getCode());
        return this.baseMapper.selectCount(wrapper).intValue();
    }

    @Override
    public void updateNotCurrent(String docId) {

        QueryWrapper<SignDocImageRecord> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        wrapper.lambda().eq(SignDocImageRecord::getDocId,docId);
        wrapper.lambda().eq(SignDocImageRecord::getIsCurrent, SignCurrentEnum.IS_CURRENT.getCode());

        SignDocImageRecord record = new SignDocImageRecord();
        record.setIsCurrent(SignCurrentEnum.NOT_CURRENT.getCode());

        this.baseMapper.update(record,wrapper);

    }
}