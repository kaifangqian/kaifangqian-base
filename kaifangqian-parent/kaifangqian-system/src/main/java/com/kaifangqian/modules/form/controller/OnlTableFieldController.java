package com.kaifangqian.modules.form.controller;

import java.util.Arrays;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kaifangqian.modules.system.entity.SysPermission;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.modules.form.entity.OnlTableField;
import com.kaifangqian.modules.form.entity.OnlTableForm;
import com.kaifangqian.modules.form.service.IOnlTableFieldService;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaifangqian.modules.form.service.IOnlTableFormService;
import com.kaifangqian.modules.system.service.ISysPermissionService;
import com.kaifangqian.utils.MyStringUtils;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;

// @Api(tags = "表字段")
@RestController
@RequestMapping("/form/onlTableField")
@Slf4j
public class OnlTableFieldController {
    @Autowired
    private IOnlTableFieldService onlTableFieldService;

    @Autowired
    private ISysPermissionService sysPermissionService;

    @Autowired
    private IOnlTableFormService onlTableFormService;

    /**
     * 分页列表查询
     *
     * @param permissionId
     * @param pageNo
     * @param pageSize
     * @return
     */
    // @ApiOperation(value = "表字段-分页列表查询", notes = "表字段-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(String permissionId, String headId, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        if (MyStringUtils.isNotBlank(permissionId)) {
            SysPermission permission = sysPermissionService.getById(permissionId);
            if (permission != null && permission.getFormTableId() != null) {
                LambdaQueryWrapper<OnlTableField> queryWrapper = new LambdaQueryWrapper();
                queryWrapper.eq(OnlTableField::getFormTableId, permission.getFormTableId());
                Page<OnlTableField> page = new Page<OnlTableField>(pageNo, pageSize);
                IPage<OnlTableField> pageList = onlTableFieldService.page(page, queryWrapper);
                return Result.OK(pageList);
            }
        } else if (MyStringUtils.isNotBlank(headId)) {
            OnlTableForm onlTableForm = onlTableFormService.getById(headId);
            if (onlTableForm != null) {
                LambdaQueryWrapper<OnlTableField> queryWrapper = new LambdaQueryWrapper();
                queryWrapper.eq(OnlTableField::getFormTableId, onlTableForm.getTableId());
                Page<OnlTableField> page = new Page<OnlTableField>(pageNo, pageSize);
                IPage<OnlTableField> pageList = onlTableFieldService.page(page, queryWrapper);
                return Result.OK(pageList);
            }
        }
        return Result.OK();
    }

    /**
     * 添加
     *
     * @param onlTableField
     * @return
     */
    // @ApiOperation(value = "表字段-添加", notes = "表字段-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody OnlTableField onlTableField) {
        onlTableFieldService.save(onlTableField);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param onlTableField
     * @return
     */
    // @ApiOperation(value = "表字段-编辑", notes = "表字段-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody OnlTableField onlTableField) {
        onlTableFieldService.updateById(onlTableField);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    // @ApiOperation(value = "表字段-通过id删除", notes = "表字段-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        onlTableFieldService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    // @ApiOperation(value = "表字段-批量删除", notes = "表字段-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.onlTableFieldService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    // @ApiOperation(value = "表字段-通过id查询", notes = "表字段-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        OnlTableField onlTableField = onlTableFieldService.getById(id);
        if (onlTableField == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(onlTableField);
    }
}
