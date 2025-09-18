/**
 * @description 签署文档管理接口实现类
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
import com.kaifangqian.modules.opensign.entity.SignDocControl;
import com.kaifangqian.modules.opensign.mapper.SignDocControlMapper;
import com.kaifangqian.modules.opensign.service.doc.SignDocControlService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Description: SignDocControlServiceImpl
 * @Package: com.kaifangqian.modules.opensign.service.doc.impl
 * @ClassName: SignDocControlServiceImpl
 * @author: FengLai_Gong
 */
@Service
public class SignDocControlServiceImpl extends ServiceImpl<SignDocControlMapper, SignDocControl> implements SignDocControlService {


    @Override
    public Integer count(String docId) {
        QueryWrapper<SignDocControl> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignDocControl::getDocId,docId);
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);

        return this.baseMapper.selectCount(wrapper).intValue();
    }

    @Override
    public List<SignDocControl> getList(String docId) {
        QueryWrapper<SignDocControl> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignDocControl::getDocId,docId);
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);

        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public void delete(String docId) {
        //删除之前的控件
        QueryWrapper<SignDocControl> updateWrapper = new QueryWrapper<>();
        updateWrapper.lambda().eq(SignDocControl::getDocId,docId);
        updateWrapper.lambda().eq(BaseEntity::getDeleteFlag,false);

        SignDocControl updateEntity = new SignDocControl();
        updateEntity.setDeleteFlag(true);
        updateEntity.setDeleteTime(new Date());
        this.baseMapper.update(updateEntity, updateWrapper);
    }
}