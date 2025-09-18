package com.kaifangqian.modules.form.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaifangqian.modules.form.entity.OnlFormTable;
import com.kaifangqian.modules.form.mapper.OnlFormTableMapper;
import com.kaifangqian.modules.form.service.IOnlFormTableService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;

/**
 * @author zhenghuihan
 * @description 表
 * @createTime 2022/9/2 18:05
 */
@Service
public class OnlFormTableServiceImpl extends ServiceImpl<OnlFormTableMapper, OnlFormTable> implements IOnlFormTableService {

    @Resource
    private OnlFormTableMapper mapper;

    @Override
    public OnlFormTable getByTableName(String tableName) {
        LambdaQueryWrapper<OnlFormTable> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OnlFormTable::getTableName, tableName);

        return this.getOne(queryWrapper);
    }

    @Override
    public IPage<OnlFormTable> pageExt(Page<OnlFormTable> page, String formName) {
        return mapper.pageExt(page, formName);
    }
}
