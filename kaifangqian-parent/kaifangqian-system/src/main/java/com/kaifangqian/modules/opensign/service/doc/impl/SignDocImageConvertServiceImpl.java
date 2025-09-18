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
import com.kaifangqian.modules.opensign.entity.SignDocImageConvert;
import com.kaifangqian.modules.opensign.mapper.SignDocImageConvertMapper;
import com.kaifangqian.modules.opensign.service.doc.SignDocImageConvertService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Description: SignDocImageConvertServiceImpl
 * @Package: com.kaifangqian.modules.opensign.service.doc.impl
 * @ClassName: SignDocImageConvertServiceImpl
 * @author: FengLai_Gong
 */
@Service
public class SignDocImageConvertServiceImpl extends ServiceImpl<SignDocImageConvertMapper, SignDocImageConvert> implements SignDocImageConvertService {


    @Override
    public Integer count(String docId, String annexId) {
        QueryWrapper<SignDocImageConvert> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignDocImageConvert::getAnnexId,annexId);
        wrapper.lambda().eq(SignDocImageConvert::getDocId,docId);
        wrapper.lambda().eq(SignDocImageConvert::getDeleteFlag,false);

        return this.baseMapper.selectCount(wrapper).intValue();
    }

    @Override
    public List<SignDocImageConvert> getList(String docId, String annexId) {

        QueryWrapper<SignDocImageConvert> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignDocImageConvert::getAnnexId,annexId);
        wrapper.lambda().eq(SignDocImageConvert::getDocId,docId);
        wrapper.lambda().eq(SignDocImageConvert::getDeleteFlag,false);

        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public void delete(String docId, String annexId) {
        QueryWrapper<SignDocImageConvert> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignDocImageConvert::getAnnexId,annexId);
        wrapper.lambda().eq(SignDocImageConvert::getDocId,docId);

        SignDocImageConvert signDocImageConvert = new SignDocImageConvert();
        signDocImageConvert.setDeleteFlag(true);
        signDocImageConvert.setDeleteTime(new Date());

        this.baseMapper.update(signDocImageConvert,wrapper);
    }
}