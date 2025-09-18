/**
 * @description 业务线签署控件接口服务实现类
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
package com.kaifangqian.modules.opensign.service.re.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.modules.opensign.entity.SignReDocControlProperty;
import com.kaifangqian.modules.opensign.enums.ControlPropertyTypeEnum;
import com.kaifangqian.modules.opensign.mapper.SignReDocControlPropertyMapper;
import com.kaifangqian.modules.opensign.service.re.SignReDocControlPropertyService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: SignReDocControlPropertyServiceImpl
 * @Package: com.kaifangqian.modules.opensign.service.re.impl
 * @ClassName: SignReDocControlPropertyServiceImpl
 * @author: FengLai_Gong
 */
@Service
public class SignReDocControlPropertyServiceImpl extends ServiceImpl<SignReDocControlPropertyMapper, SignReDocControlProperty> implements SignReDocControlPropertyService {


    @Override
    public void deleteById(String id) {

        this.baseMapper.deleteById(id);
    }

    @Override
    public void deleteByReId(String reId) {
        QueryWrapper<SignReDocControlProperty> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignReDocControlProperty::getReId,reId);

        this.baseMapper.delete(wrapper);
    }

    @Override
    public void deleteByControlId(String controlId) {
        QueryWrapper<SignReDocControlProperty> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignReDocControlProperty::getControlId,controlId);

        this.baseMapper.delete(wrapper);
    }

    @Override
    public List<SignReDocControlProperty> listByControlId(String controlId) {
        QueryWrapper<SignReDocControlProperty> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignReDocControlProperty::getControlId,controlId);

        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public List<SignReDocControlProperty> listByControlIdList(List<String> controlIdList) {
        QueryWrapper<SignReDocControlProperty> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(SignReDocControlProperty::getControlId,controlIdList);

        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public List<SignReDocControlProperty> listByDocId(String docId) {
        QueryWrapper<SignReDocControlProperty> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignReDocControlProperty::getPropertyValue,docId);
        wrapper.lambda().eq(SignReDocControlProperty::getPropertyType, ControlPropertyTypeEnum.RELATION_DOC.getName());
        return this.baseMapper.selectList(wrapper);
    }


}