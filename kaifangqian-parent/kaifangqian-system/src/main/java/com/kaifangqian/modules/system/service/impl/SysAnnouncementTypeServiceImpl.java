/**
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
package com.kaifangqian.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.modules.system.entity.SysAnnouncementType;
import com.kaifangqian.modules.system.mapper.SysAnnouncementTypeMapper;
import com.kaifangqian.modules.system.service.ISysAnnouncementTypeService;
import com.kaifangqian.utils.MyStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhenghuihan
 * @description 公告类型 服务实现类
 * @createTime 2022/9/2 17:40
 */
@Service
public class SysAnnouncementTypeServiceImpl extends ServiceImpl<SysAnnouncementTypeMapper, SysAnnouncementType> implements ISysAnnouncementTypeService {

    @Override
    public void addExt(SysAnnouncementType sysAnnouncementType) {
        if (MyStringUtils.isBlank(sysAnnouncementType.getParentId())) {
            sysAnnouncementType.setParentId("");
        }
        this.save(sysAnnouncementType);
    }

    @Override
    public void editExt(SysAnnouncementType sysAnnouncementType) {
        if (MyStringUtils.isBlank(sysAnnouncementType.getParentId())) {
            sysAnnouncementType.setParentId("");
        }
        this.updateById(sysAnnouncementType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeExt(String id) {
        deleteTypeTree(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBatchExt(List<String> ids) {
        ids.forEach(i -> {
            deleteTypeTree(i);
        });
    }

    void deleteTypeTree(String parentId) {
        if (MyStringUtils.isNotBlank(parentId)) {
            this.removeById(parentId);
            LambdaQueryWrapper<SysAnnouncementType> wrapper = new LambdaQueryWrapper();
            wrapper.eq(SysAnnouncementType::getParentId, parentId);

            List<SysAnnouncementType> result = this.list(wrapper);
            if (CollUtil.isNotEmpty(result)) {
                result.forEach(d -> {
                    deleteTypeTree(d.getId());
                });
            }
        }
    }

    @Override
    public List<Tree<String>> getTreeList(List<SysAnnouncementType> list) {
        if (CollUtil.isNotEmpty(list)) {
            List<Tree<String>> treeList = TreeUtil.build(list, "", (object, tree) -> {
                tree.setId(object.getId());
                tree.setParentId(object.getParentId());
                tree.setName(object.getTypeName());

                tree.putExtra("description", object.getDescription());
            });

            return treeList;
        }
        return new ArrayList<>();
    }
}
