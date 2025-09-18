package com.kaifangqian.modules.form.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.kaifangqian.modules.form.entity.OnlFormTable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author zhenghuihan
 * @description 表
 * @createTime 2022/9/2 18:05
 */
public interface OnlFormTableMapper extends BaseMapper<OnlFormTable> {

    IPage<OnlFormTable> pageExt(Page page, @Param("formName") String formName);
}
