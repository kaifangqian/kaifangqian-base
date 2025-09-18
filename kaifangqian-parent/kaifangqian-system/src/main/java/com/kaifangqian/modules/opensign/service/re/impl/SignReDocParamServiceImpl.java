/**
 * @description 业务线文档参数接口实现类
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
import com.kaifangqian.common.base.entity.BaseEntity;
import com.kaifangqian.modules.opensign.entity.SignReDocParam;
import com.kaifangqian.modules.opensign.mapper.SignReDocParamMapper;
import com.kaifangqian.modules.opensign.service.re.SignReDocParamService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: SignReDocParamServiceImpl
 * @Package: com.kaifangqian.modules.opensign.service.re.impl
 * @ClassName: SignReDocParamServiceImpl
 * @author: FengLai_Gong
 */
@Service
public class SignReDocParamServiceImpl extends ServiceImpl<SignReDocParamMapper, SignReDocParam> implements SignReDocParamService {

    @Override
    public List<SignReDocParam> listByReDocId(String reDocId) {
        QueryWrapper<SignReDocParam> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignReDocParam::getSignReDocId,reDocId);
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);

        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public List<SignReDocParam> listByReId(String reId) {
        QueryWrapper<SignReDocParam> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignReDocParam::getSignReId,reId);
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);

        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public List<SignReDocParam> listByReSignerId(String signerId) {
        QueryWrapper<SignReDocParam> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignReDocParam::getSignerId,signerId);
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);

        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public void deleteByRe(String reId) {
        QueryWrapper<SignReDocParam> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignReDocParam::getSignReId,reId);

        this.baseMapper.delete(wrapper);
    }

    @Override
    public void deleteByParam(List<String> reDocIdList, String reId) {
        QueryWrapper<SignReDocParam> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignReDocParam::getSignReId,reId);
        wrapper.lambda().in(SignReDocParam::getSignReDocId,reDocIdList);


        this.baseMapper.delete(wrapper);
    }
}