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
package com.kaifangqian.modules.opensign.service.ru.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.common.base.entity.BaseEntity;
import com.kaifangqian.modules.opensign.entity.SignRuDoc;
import com.kaifangqian.modules.opensign.mapper.SignRuDocMapper;
import com.kaifangqian.modules.opensign.service.ru.SignRuDocService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: SignRuDocServiceImpl
 * @Package: com.kaifangqian.modules.opensign.service.ru.impl
 * @ClassName: SignRuDocServiceImpl
 * @author: FengLai_Gong
 */
@Service
public class SignRuDocServiceImpl extends ServiceImpl<SignRuDocMapper, SignRuDoc> implements SignRuDocService {

    @Override
    public List<SignRuDoc> listByRuId(String ruId) {
        QueryWrapper<SignRuDoc> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignRuDoc::getSignRuId,ruId);
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public void deleteByRuId(String ruId) {
        QueryWrapper<SignRuDoc> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignRuDoc::getSignRuId,ruId);
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        SignRuDoc doc = new SignRuDoc();
        doc.setDeleteFlag(true);
        this.baseMapper.update(doc,wrapper);
    }

    @Override
    public void deleteByParam(List<String> idList,String ruId) {
        QueryWrapper<SignRuDoc> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(SignRuDoc::getId,idList);
        wrapper.lambda().eq(SignRuDoc::getSignRuId,ruId);
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);

        SignRuDoc doc = new SignRuDoc();
        doc.setDeleteFlag(true);

        this.baseMapper.update(doc,wrapper);
    }

}