/**
 * @description 业务线分类接口实现类
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
import com.kaifangqian.modules.opensign.entity.SignReFolderRelation;
import com.kaifangqian.modules.opensign.mapper.SignReFolderRelationMapper;
import com.kaifangqian.modules.opensign.service.re.SignReFolderRelationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: SignReFolderRelationServiceImpl
 * @Package: com.kaifangqian.modules.opensign.service.re.impl
 * @ClassName: SignReFolderRelationServiceImpl
 * @author: FengLai_Gong
 */
@Service
public class SignReFolderRelationServiceImpl extends ServiceImpl<SignReFolderRelationMapper, SignReFolderRelation> implements SignReFolderRelationService {

    @Override
    public List<String> getReIdList(String reFolderId) {
        List<String> reIdList = null ;
        QueryWrapper<SignReFolderRelation> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        wrapper.lambda().eq(SignReFolderRelation::getSignReFolderId,reFolderId);

        List<SignReFolderRelation> signReFolderRelations = this.baseMapper.selectList(wrapper);
        if(signReFolderRelations != null && signReFolderRelations.size() > 0){
            reIdList  = signReFolderRelations.stream().map(SignReFolderRelation::getSignReId).collect(Collectors.toList());
        }

        return reIdList;
    }

    @Override
    public List<SignReFolderRelation> getList(String folderId, List<String> reIdList) {
        QueryWrapper<SignReFolderRelation> queryWrapperRelation = new QueryWrapper<>();
        queryWrapperRelation.lambda().eq(SignReFolderRelation::getSignReFolderId,folderId);
        queryWrapperRelation.lambda().in(SignReFolderRelation::getSignReId,reIdList);
        queryWrapperRelation.lambda().eq(BaseEntity::getDeleteFlag,false);

        return this.baseMapper.selectList(queryWrapperRelation);
    }

    @Override
    public String getFolderId(String reId) {
        QueryWrapper<SignReFolderRelation> queryWrapperRelation = new QueryWrapper<>();
        queryWrapperRelation.lambda().eq(SignReFolderRelation::getSignReId,reId);
        queryWrapperRelation.lambda().eq(BaseEntity::getDeleteFlag,false);

        List<SignReFolderRelation> signReFolderRelations = this.baseMapper.selectList(queryWrapperRelation);
        if(signReFolderRelations != null && signReFolderRelations.size() > 0){
            SignReFolderRelation relation = signReFolderRelations.get(0);
            return relation.getSignReFolderId();
        }
        return null ;
    }

    @Override
    public Integer count(String folderId) {
        QueryWrapper<SignReFolderRelation> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        wrapper.lambda().eq(SignReFolderRelation::getSignReFolderId,folderId);
        return this.baseMapper.selectCount(wrapper).intValue();
    }

    @Override
    public Integer count(List<String> folderIdList) {
        QueryWrapper<SignReFolderRelation> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        wrapper.lambda().in(SignReFolderRelation::getSignReFolderId,folderIdList);
        return this.baseMapper.selectCount(wrapper).intValue();
    }

    @Override
    public Boolean delete(String folderId, List<String> reIdList) {
//        //更新原本的文件夹与文件的关系
        QueryWrapper<SignReFolderRelation> sourceQueryWrapper = new QueryWrapper<>();
        sourceQueryWrapper.lambda().eq(SignReFolderRelation::getSignReFolderId,folderId);
        sourceQueryWrapper.lambda().in(SignReFolderRelation::getSignReId,reIdList);
        sourceQueryWrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        SignReFolderRelation sourceRelation = new SignReFolderRelation();
        sourceRelation.setDeleteFlag(true);
        this.baseMapper.update(sourceRelation, sourceQueryWrapper);
        return true ;
    }

    @Override
    public Boolean deleteRelation(String reId) {
        QueryWrapper<SignReFolderRelation> sourceQueryWrapper = new QueryWrapper<>();
        sourceQueryWrapper.lambda().eq(SignReFolderRelation::getSignReId,reId);
        sourceQueryWrapper.lambda().eq(BaseEntity::getDeleteFlag,false);

        SignReFolderRelation sourceRelation = new SignReFolderRelation();
        sourceRelation.setDeleteFlag(true);
        this.baseMapper.update(sourceRelation, sourceQueryWrapper);
        return true;

    }

    @Override
    public Boolean deleteRelation(List<String> reIdList) {
        QueryWrapper<SignReFolderRelation> sourceQueryWrapper = new QueryWrapper<>();
        sourceQueryWrapper.lambda().in(SignReFolderRelation::getSignReId,reIdList);
        sourceQueryWrapper.lambda().eq(BaseEntity::getDeleteFlag,false);

        SignReFolderRelation sourceRelation = new SignReFolderRelation();
        sourceRelation.setDeleteFlag(true);

        this.baseMapper.update(sourceRelation, sourceQueryWrapper);

        return true;
    }
}