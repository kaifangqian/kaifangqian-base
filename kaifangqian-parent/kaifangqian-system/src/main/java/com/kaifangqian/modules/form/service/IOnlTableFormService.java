package com.kaifangqian.modules.form.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kaifangqian.modules.form.entity.OnlTableForm;

/**
 * @author zhenghuihan
 * @description 表字段
 * @createTime 2022/9/2 18:05
 */
public interface IOnlTableFormService extends IService<OnlTableForm> {

    OnlTableForm getByPermissionId(String permissionId);

}
