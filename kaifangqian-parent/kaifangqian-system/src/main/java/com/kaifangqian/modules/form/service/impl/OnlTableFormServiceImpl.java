package com.kaifangqian.modules.form.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.modules.form.entity.OnlTableForm;
import com.kaifangqian.modules.form.mapper.OnlTableFormMapper;
import com.kaifangqian.modules.form.service.IOnlTableFormService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhenghuihan
 * @description 表字段
 * @createTime 2022/9/2 18:05
 */
@Service
public class OnlTableFormServiceImpl extends ServiceImpl<OnlTableFormMapper, OnlTableForm> implements IOnlTableFormService {

    @Override
    public OnlTableForm getByPermissionId(String permissionId) {
        LambdaQueryWrapper<OnlTableForm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OnlTableForm::getSysPermissionId, permissionId);
        List<OnlTableForm> list = this.list(queryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }
}
