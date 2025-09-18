package com.kaifangqian.modules.form.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaifangqian.modules.form.entity.OnlFormTable;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author zhenghuihan
 * @description 表
 * @createTime 2022/9/2 18:05
 */
public interface IOnlFormTableService extends IService<OnlFormTable> {

    OnlFormTable getByTableName(String tableName);

    IPage<OnlFormTable> pageExt(Page<OnlFormTable> page, String formName);

}
