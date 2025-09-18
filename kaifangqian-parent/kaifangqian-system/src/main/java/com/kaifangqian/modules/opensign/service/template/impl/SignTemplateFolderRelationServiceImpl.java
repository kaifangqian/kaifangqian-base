/**
 * @description 模板分类数据关联接口类
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
package com.kaifangqian.modules.opensign.service.template.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.common.base.entity.BaseEntity;
import com.kaifangqian.modules.opensign.entity.SignTemplateFolderRelation;
import com.kaifangqian.modules.opensign.mapper.SignTemplateFolderRelationMapper;
import com.kaifangqian.modules.opensign.service.template.SignTemplateFolderRelationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: SignTemplateFolderRelationServiceImpl
 * @Package: com.kaifangqian.modules.opensign.service.template.impl
 * @ClassName: SignTemplateFolderRelationServiceImpl
 * @author: FengLai_Gong
 */
@Service
public class SignTemplateFolderRelationServiceImpl extends ServiceImpl<SignTemplateFolderRelationMapper, SignTemplateFolderRelation> implements SignTemplateFolderRelationService {


    @Override
    public List<String> getTemplateIdList(String templateFolderId) {
        List<String> templateIdList = null ;
        QueryWrapper<SignTemplateFolderRelation> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        wrapper.lambda().eq(SignTemplateFolderRelation::getTemplateFolderId,templateFolderId);

        List<SignTemplateFolderRelation> signTemplateFolderRelations = this.baseMapper.selectList(wrapper);
        if(signTemplateFolderRelations != null && signTemplateFolderRelations.size() > 0){
            templateIdList  = signTemplateFolderRelations.stream().map(SignTemplateFolderRelation::getTemplateId).collect(Collectors.toList());
        }

        return templateIdList;
    }

    @Override
    public List<SignTemplateFolderRelation> getList(String folderId, List<String> templateIdList) {
        QueryWrapper<SignTemplateFolderRelation> queryWrapperRelation = new QueryWrapper<>();
        queryWrapperRelation.lambda().eq(SignTemplateFolderRelation::getTemplateFolderId,folderId);
        queryWrapperRelation.lambda().in(SignTemplateFolderRelation::getTemplateId,templateIdList);
        queryWrapperRelation.lambda().eq(BaseEntity::getDeleteFlag,false);

        return this.baseMapper.selectList(queryWrapperRelation);
    }

    @Override
    public Integer count(String folderId) {
        QueryWrapper<SignTemplateFolderRelation> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        wrapper.lambda().eq(SignTemplateFolderRelation::getTemplateFolderId,folderId);
        return this.baseMapper.selectCount(wrapper).intValue();
    }

    @Override
    public Integer count(List<String> folderIdList) {
        QueryWrapper<SignTemplateFolderRelation> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        wrapper.lambda().in(SignTemplateFolderRelation::getTemplateFolderId,folderIdList);
        return this.baseMapper.selectCount(wrapper).intValue();
    }

    @Override
    public Boolean delete(String folderId, List<String> templateIdList) {
        //更新原本的文件夹与文件的关系
        QueryWrapper<SignTemplateFolderRelation> sourceQueryWrapper = new QueryWrapper<>();
        sourceQueryWrapper.lambda().eq(SignTemplateFolderRelation::getTemplateFolderId,folderId);
        sourceQueryWrapper.lambda().in(SignTemplateFolderRelation::getTemplateId,templateIdList);
        sourceQueryWrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        SignTemplateFolderRelation sourceRelation = new SignTemplateFolderRelation();
        sourceRelation.setDeleteFlag(true);
        this.baseMapper.update(sourceRelation, sourceQueryWrapper);

        return true;
    }

    @Override
    public Boolean deleteRelation(String templateId) {

        QueryWrapper<SignTemplateFolderRelation> sourceQueryWrapper = new QueryWrapper<>();
        sourceQueryWrapper.lambda().eq(SignTemplateFolderRelation::getTemplateId,templateId);
        sourceQueryWrapper.lambda().eq(BaseEntity::getDeleteFlag,false);

        SignTemplateFolderRelation sourceRelation = new SignTemplateFolderRelation();
        sourceRelation.setDeleteFlag(true);
        this.baseMapper.update(sourceRelation, sourceQueryWrapper);


        return true;
    }

    @Override
    public Boolean deleteRelation(List<String> templateIdList) {

        QueryWrapper<SignTemplateFolderRelation> sourceQueryWrapper = new QueryWrapper<>();
        sourceQueryWrapper.lambda().in(SignTemplateFolderRelation::getTemplateId,templateIdList);
        sourceQueryWrapper.lambda().eq(BaseEntity::getDeleteFlag,false);

        SignTemplateFolderRelation sourceRelation = new SignTemplateFolderRelation();
        sourceRelation.setDeleteFlag(true);

        this.baseMapper.update(sourceRelation, sourceQueryWrapper);

        return true;
    }
}