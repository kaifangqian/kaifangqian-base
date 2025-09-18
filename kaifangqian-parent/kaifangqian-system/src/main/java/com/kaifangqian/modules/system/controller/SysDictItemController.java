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
package com.kaifangqian.modules.system.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import com.kaifangqian.modules.system.entity.SysDictItem;
import com.kaifangqian.annotation.ResrunLogMethod;
import com.kaifangqian.annotation.ResrunLogModule;
import com.kaifangqian.common.enums.OperateLogType;
import com.kaifangqian.common.system.query.QueryGenerator;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.utils.MyStringUtils;
import com.kaifangqian.modules.system.service.ISysDictItemService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/sys/dictItem")
@Slf4j
@ResrunLogModule(name = "字典项管理模块")
public class SysDictItemController {

    @Autowired
    private ISysDictItemService sysDictItemService;

    /**
     * @param sysDictItem
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     * @功能：查询字典数据(分页、分层)
     */
    @ResrunLogMethod(name = "分页查询字典项列表", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<IPage<SysDictItem>> queryPageList(SysDictItem sysDictItem, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<SysDictItem>> result = new Result<IPage<SysDictItem>>();
        QueryWrapper<SysDictItem> queryWrapper = QueryGenerator.initQueryWrapper(sysDictItem, req.getParameterMap());
        queryWrapper.orderByAsc("sort_order");
        if (MyStringUtils.isBlank(sysDictItem.getParentId())) {
            queryWrapper.lambda().eq(SysDictItem::getParentId, "");
        }
        Page<SysDictItem> page = new Page<SysDictItem>(pageNo, pageSize);
        IPage<SysDictItem> pageList = sysDictItemService.page(page, queryWrapper);
        List<SysDictItem> list = pageList.getRecords();
        if (CollUtil.isNotEmpty(list)) {
            list.forEach(i -> {
                i.setChildCount(sysDictItemService.countByPId(i.getId()));
            });
        }
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }


    /**
     * @功能：字典树
     */
    @ResrunLogMethod(name = "查询字典项树", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/treeList", method = RequestMethod.GET)
    public Result<List<Tree<String>>> treeList(SysDictItem sysDictItem, HttpServletRequest req) {
        QueryWrapper<SysDictItem> queryWrapper = QueryGenerator.initQueryWrapper(sysDictItem, req.getParameterMap());
        List<SysDictItem> list = sysDictItemService.list(queryWrapper);
        List<Tree<String>> tree = sysDictItemService.getTreeList(list);

        return Result.OK(tree);
    }

    /**
     * @return
     * @功能：新增
     */
    @ResrunLogMethod(name = "新增字典项", operateType = OperateLogType.OPERATE_TYPE_2)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions({"dictdata:add"})
    public Result<?> add(@RequestBody SysDictItem sysDictItem) {
        sysDictItemService.saveExt(sysDictItem);
        return Result.OK();
    }

    /**
     * @param sysDictItem
     * @return
     * @功能：编辑
     */
    @ResrunLogMethod(name = "编辑字典项", operateType = OperateLogType.OPERATE_TYPE_3)
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    @RequiresPermissions({"dictdata:edit"})
    public Result<?> edit(@RequestBody SysDictItem sysDictItem) {
        sysDictItemService.updateExt(sysDictItem);
        return Result.OK();
    }

    /**
     * @param id
     * @return
     * @功能：删除字典数据
     */
    @ResrunLogMethod(name = "删除字典项", operateType = OperateLogType.OPERATE_TYPE_4)
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @RequiresPermissions({"dictdata:delete"})
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        sysDictItemService.deleteExt(id);
        return Result.OK();
    }
}
