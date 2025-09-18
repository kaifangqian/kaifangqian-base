package com.kaifangqian.modules.form.controller;

import java.util.Arrays;

import com.kaifangqian.common.vo.Result;
import com.kaifangqian.modules.form.entity.OnlFormTable;
import com.kaifangqian.modules.form.service.IOnlFormTableService;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;

// @Api(tags = "表")
@RestController
@RequestMapping("/form/onlFormTable")
@Slf4j
public class OnlFormTableController {
    @Autowired
    private IOnlFormTableService onlFormTableService;

    /**
     * 分页列表查询
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    // @ApiOperation(value = "表-分页列表查询", notes = "表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(String formName,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<OnlFormTable> page = new Page<OnlFormTable>(pageNo, pageSize);
        IPage<OnlFormTable> pageList = onlFormTableService.pageExt(page, formName);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param onlFormTable
     * @return
     */
    // @ApiOperation(value = "表-添加", notes = "表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody OnlFormTable onlFormTable) {
        onlFormTableService.save(onlFormTable);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param onlFormTable
     * @return
     */
    // @ApiOperation(value = "表-编辑", notes = "表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody OnlFormTable onlFormTable) {
        onlFormTableService.updateById(onlFormTable);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    // @ApiOperation(value = "表-通过id删除", notes = "表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        onlFormTableService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    // @ApiOperation(value = "表-批量删除", notes = "表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.onlFormTableService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    // @ApiOperation(value = "表-通过id查询", notes = "表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        OnlFormTable onlFormTable = onlFormTableService.getById(id);
        if (onlFormTable == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(onlFormTable);
    }
}
