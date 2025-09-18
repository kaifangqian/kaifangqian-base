/**
 * @description 签署文档操作管理接口实现类
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
import com.kaifangqian.modules.opensign.entity.SignRuDocOperate;
import com.kaifangqian.modules.opensign.enums.SignCurrentEnum;
import com.kaifangqian.modules.opensign.mapper.SignRuDocOperateMapper;
import com.kaifangqian.modules.opensign.service.ru.SignRuDocOperateService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: SignRuDocOperateServiceImpl
 * @Package: com.kaifangqian.modules.opensign.service.ru.impl
 * @ClassName: SignRuDocOperateServiceImpl
 * @author: FengLai_Gong
 */
@Service
public class SignRuDocOperateServiceImpl extends ServiceImpl<SignRuDocOperateMapper, SignRuDocOperate> implements SignRuDocOperateService {


    @Override
    public List<SignRuDocOperate> listByRuId(String ruId) {
        QueryWrapper<SignRuDocOperate> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignRuDocOperate::getSignRuId,ruId);
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public List<SignRuDocOperate> listByRuIdCurrent(String ruId) {
        QueryWrapper<SignRuDocOperate> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignRuDocOperate::getSignRuId,ruId);
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        wrapper.lambda().eq(SignRuDocOperate::getIsCurrent, SignCurrentEnum.IS_CURRENT.getCode());
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public List<SignRuDocOperate> listByParam(String ruId, String docId) {
        QueryWrapper<SignRuDocOperate> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignRuDocOperate::getSignRuId,ruId);
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        wrapper.lambda().eq(SignRuDocOperate::getDocId,docId);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public List<SignRuDocOperate> listByParamCurrent(String ruId, String docId) {
        QueryWrapper<SignRuDocOperate> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignRuDocOperate::getSignRuId,ruId);
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        wrapper.lambda().eq(SignRuDocOperate::getDocId,docId);
        wrapper.lambda().eq(SignRuDocOperate::getIsCurrent, SignCurrentEnum.IS_CURRENT.getCode());
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public List<SignRuDocOperate> listByParamCurrent(String ruId, List<String> docIdList) {
        QueryWrapper<SignRuDocOperate> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignRuDocOperate::getSignRuId,ruId);
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        wrapper.lambda().in(SignRuDocOperate::getDocId,docIdList);
        wrapper.lambda().eq(SignRuDocOperate::getIsCurrent, SignCurrentEnum.IS_CURRENT.getCode());
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public SignRuDocOperate getCurrentByDocId(String docId) {
        QueryWrapper<SignRuDocOperate> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        wrapper.lambda().eq(SignRuDocOperate::getDocId,docId);
        wrapper.lambda().eq(SignRuDocOperate::getIsCurrent, SignCurrentEnum.IS_CURRENT.getCode());

        List<SignRuDocOperate> signRuDocOperates = this.baseMapper.selectList(wrapper);
        if(signRuDocOperates != null && signRuDocOperates.size() > 0){
            return signRuDocOperates.get(0);
        }
        return null ;
    }

    @Override
    public void deleteByParam(List<String> docIdList ,String ruId) {
        QueryWrapper<SignRuDocOperate> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(SignRuDocOperate::getDocId,docIdList);
        wrapper.lambda().eq(SignRuDocOperate::getSignRuId,ruId);
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);

        SignRuDocOperate ruDocOperate = new SignRuDocOperate();
        ruDocOperate.setDeleteFlag(true);

        this.baseMapper.update(ruDocOperate,wrapper);
    }
}