/**
 * @description 获取和删除签署文档控件接口实现类
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
import com.kaifangqian.modules.opensign.entity.SignRuDocControlProperty;
import com.kaifangqian.modules.opensign.enums.ControlPropertyTypeEnum;
import com.kaifangqian.modules.opensign.mapper.SignRuDocControlPropertyMapper;
import com.kaifangqian.modules.opensign.service.ru.SignRuDocControlPropertyService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: SignRuDocControlPropertyServiceImpl
 * @Package: com.kaifangqian.modules.opensign.service.re.impl
 * @ClassName: SignRuDocControlPropertyServiceImpl
 * @author: FengLai_Gong
 */
@Service
public class SignRuDocControlPropertyServiceImpl extends ServiceImpl<SignRuDocControlPropertyMapper, SignRuDocControlProperty> implements SignRuDocControlPropertyService {

    @Override
    public void deleteById(String id) {
        this.baseMapper.deleteById(id);
    }

    @Override
    public void deleteByControlId(String controlId) {
        QueryWrapper<SignRuDocControlProperty> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignRuDocControlProperty::getControlId,controlId);

        this.baseMapper.delete(wrapper);

    }

    @Override
    public void deleteByRuId(String ruId) {
        QueryWrapper<SignRuDocControlProperty> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignRuDocControlProperty::getRuId,ruId);

        this.baseMapper.delete(wrapper);
    }

    @Override
    public List<SignRuDocControlProperty> listByControlId(String controlId) {
        QueryWrapper<SignRuDocControlProperty> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignRuDocControlProperty::getControlId,controlId);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public List<SignRuDocControlProperty> listByControlIdList(List<String> controlIdList) {
        QueryWrapper<SignRuDocControlProperty> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(SignRuDocControlProperty::getControlId,controlIdList);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public List<SignRuDocControlProperty> listByDocId(String docId) {
        QueryWrapper<SignRuDocControlProperty> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignRuDocControlProperty::getPropertyValue,docId);
        wrapper.lambda().eq(SignRuDocControlProperty::getPropertyType, ControlPropertyTypeEnum.RELATION_DOC.getCode());
        return this.baseMapper.selectList(wrapper);
    }

}