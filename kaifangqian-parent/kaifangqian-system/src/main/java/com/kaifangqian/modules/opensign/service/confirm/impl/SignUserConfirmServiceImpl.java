/**
 * @description 用户意愿校验订单接口实现类
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
package com.kaifangqian.modules.opensign.service.confirm.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.kaifangqian.modules.opensign.constant.SignCommonConstant;
import com.kaifangqian.modules.system.entity.SysTenantUser;
import com.kaifangqian.modules.system.entity.SysUserConfig;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.opensign.entity.SignUserConfirm;
import com.kaifangqian.modules.opensign.enums.ConfirmTypeEnum;
import com.kaifangqian.modules.opensign.mapper.SignUserConfirmMapper;
import com.kaifangqian.modules.opensign.service.confirm.ISignUserConfirmService;
import com.kaifangqian.modules.opensign.vo.base.ConfirmPara;
import com.kaifangqian.modules.opensign.vo.request.ConfirmParaRequest;
import com.kaifangqian.modules.system.service.ISysTenantUserService;
import com.kaifangqian.modules.system.service.ISysUserConfigService;
import com.kaifangqian.modules.system.service.ITenantInfoExtendService;
import com.kaifangqian.utils.MyStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SignUserConfirmServiceImpl extends ServiceImpl<SignUserConfirmMapper, SignUserConfirm> implements ISignUserConfirmService {

    @Autowired
    private ISysTenantUserService iSysTenantUserService;
    @Autowired
    private ITenantInfoExtendService tenantInfoExtendService;
    @Autowired
    private ISysUserConfigService sysUserConfigService;


    @Override
    public String saveExt(String taskId) {
        SignUserConfirm confirm = getByMainId(taskId);
        if (confirm != null) {
            if (confirm.getFinalConfirmFlag() != null && confirm.getFinalConfirmFlag()) {
                return confirm.getId();
            }
            this.removeById(confirm);
        }
        confirm = new SignUserConfirm();
        confirm.setId(UUID.randomUUID().toString().replace("-", ""));
        confirm.setMainId(taskId);

        this.save(confirm);
        return confirm.getId();
    }

    @Override
    public void confirmPara(ConfirmParaRequest confirmPara) {
        if (MyStringUtils.isBlank(confirmPara.getOrderNo())) {
            throw new PaasException("orderNo不能为空");
        }
        if (MyStringUtils.isBlank(confirmPara.getConfirmType())) {
            throw new PaasException("confirmType不能为空");
        }
        ConfirmTypeEnum typeEnum = ConfirmTypeEnum.getByType(confirmPara.getConfirmType());
        if (typeEnum == null) {
            throw new PaasException("confirmType类型不支持");
        }
        SignUserConfirm confirm = this.getById(confirmPara.getOrderNo());
        if (confirm == null) {
            throw new PaasException("orderNo传值错误");
        }
        // 更新确认参数
        confirm.setConfirmType(confirmPara.getConfirmType());
        confirm.setConfirmPara("");
        ConfirmPara para = new ConfirmPara();
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        switch (typeEnum) {
            case NONE:
                //如果不需要校验的话，直接设置校通过flag为true
                confirm.setFinalConfirmFlag(true);
                break;
            case FACE:
                SysTenantUser tenantUser = iSysTenantUserService.getPersonalTenantUser(loginUser.getId());
                if (tenantUser == null) {
                    throw new PaasException("个人身份不存在");
                }
                para = tenantInfoExtendService.getNameAndNo(tenantUser.getTenantId());
                if (para == null || MyStringUtils.isBlank(para.getName()) || MyStringUtils.isBlank(para.getIdCard())) {
                    throw new PaasException("个人信息不存在");
                }
                confirm.setConfirmPara(new Gson().toJson(para));
                break;
            case PAASWORD:
                SysUserConfig userConfig = sysUserConfigService.getConfigByType(loginUser.getId(), SignCommonConstant.USERSIGNPASSWORDKEY);
                if (userConfig == null || MyStringUtils.isBlank(userConfig.getValue())) {
                    throw new PaasException("签署密码未设置");
                }
                para.setPassword(userConfig.getValue());
                confirm.setConfirmPara(new Gson().toJson(para));
                break;
            case PHONE_EMAIL:
                para.setPhone(loginUser.getPhone());
                para.setEmail(loginUser.getEmail());
                confirm.setConfirmPara(new Gson().toJson(para));
                break;
            case DOUBLE:
                SysUserConfig userConfig1 = sysUserConfigService.getConfigByType(loginUser.getId(), SignCommonConstant.USERSIGNPASSWORDKEY);
                if (userConfig1 == null || MyStringUtils.isBlank(userConfig1.getValue())) {
                    throw new PaasException("签署密码未设置");
                }
                para.setPassword(userConfig1.getValue());
                para.setPhone(loginUser.getPhone());
                para.setEmail(loginUser.getEmail());
                confirm.setConfirmPara(new Gson().toJson(para));
                break;
            default:
                break;
        }

        this.updateById(confirm);
    }

    @Override
    public void setFlag(String orderNo, Boolean flag) {
        SignUserConfirm confirm = this.getById(orderNo);
        if (confirm == null) {
            throw new PaasException("orderNo传值错误");
        }
        confirm.setFinalConfirmFlag(flag);
        this.updateById(confirm);
    }

    @Override
    public SignUserConfirm getByMainId(String mainId) {
        LambdaQueryWrapper<SignUserConfirm> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(SignUserConfirm::getMainId, mainId);

        List<SignUserConfirm> list = this.list(queryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        }

        return null;
    }
}