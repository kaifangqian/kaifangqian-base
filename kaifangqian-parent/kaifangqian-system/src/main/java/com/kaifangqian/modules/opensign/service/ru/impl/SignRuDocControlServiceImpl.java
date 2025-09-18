/**
 * @description 签署文档控件管理接口实现类
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
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.modules.opensign.service.business.vo.ControlQueryVo;
import com.kaifangqian.common.base.entity.BaseEntity;
import com.kaifangqian.modules.opensign.entity.SignRuDocControl;
import com.kaifangqian.modules.opensign.enums.ControlTypeEnum;
import com.kaifangqian.modules.opensign.mapper.SignRuDocControlMapper;
import com.kaifangqian.modules.opensign.service.ru.SignRuDocControlService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: SignRuDocControlServiceImpl
 * @Package: com.kaifangqian.modules.opensign.service.ru.impl
 * @ClassName: SignRuDocControlServiceImpl
 * @author: FengLai_Gong
 */
@Service
public class SignRuDocControlServiceImpl extends ServiceImpl<SignRuDocControlMapper, SignRuDocControl> implements SignRuDocControlService {


    @Override
    public List<SignRuDocControl> listByParam(ControlQueryVo vo) {
        QueryWrapper wrapper = buildQueryVo(vo);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public List<SignRuDocControl> listSignControlList(String ruId) {
        QueryWrapper<SignRuDocControl> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignRuDocControl::getSignRuId,ruId);
        wrapper.lambda().in(SignRuDocControl::getControlType,ControlTypeEnum.getSignList());
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public List<SignRuDocControl> listSignControlBySignerId(String signerId) {

        QueryWrapper<SignRuDocControl> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignRuDocControl::getSignerId,signerId);
        wrapper.lambda().in(SignRuDocControl::getControlType,ControlTypeEnum.getSignList());
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public List<SignRuDocControl> listWriteByDocList(List<String> docList) {
        QueryWrapper<SignRuDocControl> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(SignRuDocControl::getSignRuDocId,docList);
        wrapper.lambda().in(SignRuDocControl::getControlType,ControlTypeEnum.getWriteList());
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public Integer countByParam(ControlQueryVo vo) {
        QueryWrapper wrapper = buildQueryVo(vo);
        Long count = this.baseMapper.selectCount(wrapper);
        return count.intValue() ;
    }

    @Override
    public void deleteById(String controlId) {
        this.baseMapper.deleteById(controlId);
    }


    public QueryWrapper buildQueryVo(ControlQueryVo vo){
        QueryWrapper<SignRuDocControl> wrapper = new QueryWrapper<>();
        if(vo.getSignerId() != null && vo.getSignerId().length() > 0){
            wrapper.lambda().eq(SignRuDocControl::getSignerId,vo.getSignerId());
        }
        if(vo.getSignerIdList() != null && vo.getSignerIdList().size() > 0){
            wrapper.lambda().in(SignRuDocControl::getSignerId,vo.getSignerIdList());
        }
        if(vo.getControlType() != null && vo.getControlType().length() > 0){
            wrapper.lambda().eq(SignRuDocControl::getControlType,vo.getControlType());
        }
        if(vo.getControlTypeList() != null && vo.getControlTypeList().size() > 0){
            wrapper.lambda().in(SignRuDocControl::getControlType,vo.getControlTypeList());
        }
        if(vo.getSignerType() != null){
            wrapper.lambda().eq(SignRuDocControl::getSignerType,vo.getSignerType());
        }
        if(vo.getSignRuDocId() != null && vo.getSignRuDocId().length() > 0){
            wrapper.lambda().eq(SignRuDocControl::getSignRuDocId,vo.getSignRuDocId());
        }
        if(vo.getSignRuDocIdList() != null && vo.getSignRuDocIdList().size() > 0){
            wrapper.lambda().in(SignRuDocControl::getSignRuDocId,vo.getSignRuDocIdList());
        }
        if(vo.getSignRuDocId() != null && vo.getSignRuDocId().length() > 0){
            wrapper.lambda().eq(SignRuDocControl::getSignRuDocId,vo.getSignRuDocId());
        }
        if(vo.getErrorStatus() != null){
            wrapper.lambda().eq(SignRuDocControl::getErrorStatus,vo.getErrorStatus());
        }
        if(vo.getSignRuId() != null && vo.getSignRuId().length() > 0){
            wrapper.lambda().eq(SignRuDocControl::getSignRuId,vo.getSignRuId());
        }

        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);

        return wrapper;
    }

    @Override
    public void deleteByParam(List<String> docIdList, String ruId) {
        QueryWrapper<SignRuDocControl> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(SignRuDocControl::getSignRuDocId,docIdList);
        wrapper.lambda().eq(SignRuDocControl::getSignRuId,ruId);
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);

        SignRuDocControl control = new SignRuDocControl();
        control.setDeleteFlag(true);

        this.baseMapper.update(control,wrapper);
    }

    @Override
    public void deleteSignControlList(String ruId) {
        QueryWrapper<SignRuDocControl> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(SignRuDocControl::getControlType, ControlTypeEnum.getSignList());
        wrapper.lambda().eq(SignRuDocControl::getSignRuId,ruId);
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);

        SignRuDocControl control = new SignRuDocControl();
        control.setDeleteFlag(true);

        this.baseMapper.update(control,wrapper);
    }

    @Override
    public void deleteSignControlList(String signerId, String ruId) {
        QueryWrapper<SignRuDocControl> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(SignRuDocControl::getControlType, ControlTypeEnum.getSignList());
        wrapper.lambda().eq(SignRuDocControl::getSignerId,signerId);
//        wrapper.lambda().eq(SignRuDocControl::getSignerType,signerType);

        wrapper.lambda().eq(SignRuDocControl::getSignRuId,ruId);
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);

        SignRuDocControl control = new SignRuDocControl();
        control.setDeleteFlag(true);

        this.baseMapper.update(control,wrapper);
    }

    @Override
    public void resetWriteControlList(String ruId) {
        UpdateWrapper<SignRuDocControl> wrapper = new UpdateWrapper<>();
        wrapper.lambda().in(SignRuDocControl::getControlType, ControlTypeEnum.getWriteList());
        wrapper.lambda().eq(SignRuDocControl::getSignRuId,ruId);
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);

        wrapper.lambda().set(SignRuDocControl::getSignerId,null);
        wrapper.lambda().set(SignRuDocControl::getSignerType,null );

        this.update(wrapper);
    }

    @Override
    public void resetWriteControlList(String signerId, Integer signerType ,String ruId) {
        UpdateWrapper<SignRuDocControl> wrapper = new UpdateWrapper<>();
        wrapper.lambda().in(SignRuDocControl::getControlType, ControlTypeEnum.getWriteList());
        wrapper.lambda().eq(SignRuDocControl::getSignerId,signerId);
        wrapper.lambda().eq(SignRuDocControl::getSignerType,signerType);

        wrapper.lambda().eq(SignRuDocControl::getSignRuId,ruId);
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);


        wrapper.lambda().set(SignRuDocControl::getSignerId,null);
        wrapper.lambda().set(SignRuDocControl::getSignerType,null );


        this.update(wrapper);
    }

//    @Override
//    public void deleteById(String id) {
//        QueryWrapper<SignRuDocControl> wrapper = new QueryWrapper<>();
//        wrapper.lambda().eq(SignRuDocControl::getId,id);
//
//        SignRuDocControl control = new SignRuDocControl();
//        control.setDeleteFlag(false);
//
//        this.baseMapper.updateById()
//    }


}