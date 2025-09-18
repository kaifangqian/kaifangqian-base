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
import com.kaifangqian.modules.system.entity.SysDict;
import com.kaifangqian.modules.system.enums.DictType;
import com.kaifangqian.modules.system.mapper.SysDictMapper;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.system.service.ISysDictItemService;
import com.kaifangqian.utils.MyStringUtils;
import lombok.extern.slf4j.Slf4j;
import com.kaifangqian.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {

    @Autowired
    private ISysDictItemService iSysDictItemService;

    @Override
    public void saveExt(SysDict sysDict) {
        if (MyStringUtils.isBlank(sysDict.getParentId())) {
            sysDict.setParentId("");
        } else {
            //校验数据是否合理
            checkEntity(sysDict);
        }

        this.save(sysDict);
    }

    @Override
    public void updateExt(SysDict sysDict) {
        if (MyStringUtils.isBlank(sysDict.getParentId())) {
            sysDict.setParentId("");
        } else {
            //校验数据是否合理
            checkEntity(sysDict);
        }

        this.updateById(sysDict);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteExt(String id) {
        SysDict sysDict = this.getById(id);
        if (sysDict != null) {
            if (DictType.DICT.getType().equals(sysDict.getType())) {
                iSysDictItemService.deleteByDictId(sysDict.getId());
            }

            deleteDictTree(id);
        }
    }

    void deleteDictTree(String parentId) {
        if (MyStringUtils.isNotBlank(parentId)) {
            this.removeById(parentId);
            LambdaQueryWrapper<SysDict> wrapper = new LambdaQueryWrapper();
            wrapper.eq(SysDict::getParentId, parentId);

            List<SysDict> result = this.list(wrapper);
            if (CollUtil.isNotEmpty(result)) {
                result.forEach(d -> {
                    if (DictType.DICT.getType().equals(d.getType())) {
                        iSysDictItemService.deleteByDictId(d.getId());
                    }
                    deleteDictTree(d.getId());
                });
            }
        }
    }

    void checkEntity(SysDict sysDict) {
        SysDict dict = this.getById(sysDict.getParentId());
        if (dict == null || !DictType.GROUP.getType().equals(dict.getType())) {
            throw new PaasException("所属上级不正确");
        }
    }

    @Override
    public List<Tree<String>> getTreeList(List<SysDict> list) {
        List<Tree<String>> treeList = TreeUtil.build(list, "", (object, tree) -> {
            tree.setId(object.getId());
            tree.setParentId(object.getParentId());
            tree.setName(object.getDictName());

            tree.putExtra("type", object.getType());
        });

        return treeList;
    }
}
