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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaifangqian.modules.system.entity.SysArticle;
import com.kaifangqian.modules.system.entity.SysArticleType;
import com.kaifangqian.modules.system.entity.SysDict;
import com.kaifangqian.annotation.ResrunLogMethod;
import com.kaifangqian.annotation.ResrunLogModule;
import com.kaifangqian.common.enums.OperateLogType;
import com.kaifangqian.common.system.query.QueryGenerator;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.modules.system.service.ISysArticleService;
import com.kaifangqian.modules.system.service.ISysArticleTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 文章
 */
@RestController
@RequestMapping("/sys/article")
@Slf4j
@ResrunLogModule(name = "文章管理模块")
public class SysArticleController {

    @Autowired
    private ISysArticleService sysArticleService;
    @Autowired
    private ISysArticleTypeService sysArticleTypeService;

    /**
     * @功能：新增分类
     */
    @ResrunLogMethod(name = "新增分类", operateType = OperateLogType.OPERATE_TYPE_2)
    @RequestMapping(value = "/type/add", method = RequestMethod.POST)
    public Result<?> addType(@RequestBody SysArticleType sysArticleType) {
        sysArticleTypeService.save(sysArticleType);
        return Result.OK();
    }

    @ResrunLogMethod(name = "编辑分类", operateType = OperateLogType.OPERATE_TYPE_3)
    @RequestMapping(value = "/type/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> editType(@RequestBody SysArticleType sysArticleType) {
        sysArticleTypeService.updateById(sysArticleType);
        return Result.OK();
    }

    @ResrunLogMethod(name = "删除分类", operateType = OperateLogType.OPERATE_TYPE_4)
    @RequestMapping(value = "/type/delete", method = RequestMethod.DELETE)
    public Result<SysDict> deleteType(@RequestParam(name = "id", required = true) String id) {
        sysArticleTypeService.removeById(id);
        return Result.OK();
    }

    @ResrunLogMethod(name = "分页查询分类列表", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/type/list", method = RequestMethod.GET)
    public Result<IPage<SysArticleType>> typePageList(SysArticleType sysArticleType, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<SysArticleType> queryWrapper = QueryGenerator.initQueryWrapper(sysArticleType, req.getParameterMap());
        Page<SysArticleType> page = new Page<SysArticleType>(pageNo, pageSize);
        queryWrapper.lambda().orderByDesc(SysArticleType::getCreateTime);
        IPage<SysArticleType> pageList = sysArticleTypeService.page(page, queryWrapper);
        return Result.OK(pageList);
    }



    @ResrunLogMethod(name = "新增文章", operateType = OperateLogType.OPERATE_TYPE_2)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<?> add(@RequestBody SysArticle sysArticle) {
        sysArticleService.saveExt(sysArticle);
        return Result.OK();
    }
    @ResrunLogMethod(name = "查询文章详情", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public Result<SysArticle> info(@RequestParam(name = "id", required = true) String id) {
        return Result.OK(sysArticleService.getById(id));
    }

    @ResrunLogMethod(name = "编辑文章", operateType = OperateLogType.OPERATE_TYPE_3)
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody SysArticle sysArticle) {
        sysArticleService.updateExt(sysArticle);
        return Result.OK();
    }

    @ResrunLogMethod(name = "删除文章", operateType = OperateLogType.OPERATE_TYPE_4)
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<SysDict> delete(@RequestParam(name = "id", required = true) String id) {
        sysArticleService.removeById(id);
        return Result.OK();
    }



    @ResrunLogMethod(name = "分页查询文章列表", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<IPage<SysArticle>> pageList(SysArticle sysArticle, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<SysArticle> page = new Page<SysArticle>(pageNo, pageSize);
        IPage<SysArticle> pageList = sysArticleService.pageExt(page, sysArticle);
        return Result.OK(pageList);
    }

    @ResrunLogMethod(name = "无权限分页查询文章列表", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/listNoAuth", method = RequestMethod.GET)
    public Result<IPage<SysArticle>> pageListNoAuth(SysArticle sysArticle, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<SysArticle> page = new Page<SysArticle>(pageNo, pageSize);
        sysArticle.setStatus(1);
        IPage<SysArticle> pageList = sysArticleService.pageExt(page, sysArticle);
        return Result.OK(pageList);
    }
}
