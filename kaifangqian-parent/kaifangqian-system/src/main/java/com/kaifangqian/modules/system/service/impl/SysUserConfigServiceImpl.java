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
package com.kaifangqian.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.modules.opensign.constant.SignCommonConstant;
import com.kaifangqian.modules.opensign.enums.ConfirmTypeEnum;
import com.kaifangqian.modules.system.entity.SysUserConfig;
import com.kaifangqian.modules.system.enums.UpdatePasswordEnum;
import com.kaifangqian.modules.system.mapper.SysUserConfigMapper;
import com.kaifangqian.modules.system.vo.SysUserConfigVO;
import com.kaifangqian.common.redis.util.RedisUtil;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.system.service.ISysUserConfigService;
import com.kaifangqian.utils.MD5Util;
import com.kaifangqian.utils.MyStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * @author zhenghuihan
 * @description 用户系统配置表
 * @createTime 2022/9/2 17:32
 */
@Service
public class SysUserConfigServiceImpl extends ServiceImpl<SysUserConfigMapper, SysUserConfig> implements ISysUserConfigService {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public SysUserConfig getConfigByType(String userId, String type) {
//        if (MyStringUtils.isBlank(userId)) {
            userId = MySecurityUtils.getCurrentUser().getId();
//        }
        LambdaQueryWrapper<SysUserConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserConfig::getUserId, userId).eq(SysUserConfig::getType, type);

        List<SysUserConfig> list = list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            SysUserConfig tem = new SysUserConfig();
            tem.setUserId(userId);
            tem.setName(type);
            tem.setType(type);
            tem.setValue("");

            save(tem);
            return tem;
        }
        return list.get(0);
    }

    @Override
    public void updatePasswordExt(SysUserConfigVO configVO) {
        String key = null;
        LoginUser user = MySecurityUtils.getCurrentUser();
        if (UpdatePasswordEnum.PHONE.getType().equals(configVO.getType())) {
            key = user.getPhone();
        } else if (UpdatePasswordEnum.EMIAL.getType().equals(configVO.getType())) {
            key = user.getEmail();
        } else {
            throw new PaasException("类型不支持");
        }
        String realKey = MD5Util.MD5Encode(key + configVO.getCaptchaKey(), "utf-8");
        Object checkCode = redisUtil.get(realKey);
        if (checkCode == null || !checkCode.toString().equals(configVO.getCaptcha())) {
            throw new PaasException("验证码错误");
        }
        SysUserConfig config = getConfigByType(user.getId(), SignCommonConstant.USERSIGNPASSWORDKEY);
        config.setValue(configVO.getPassword());
        this.updateById(config);

        if (MyStringUtils.isNotBlank(configVO.getConfirmType())) {
            updateSignConfirmType(configVO.getConfirmType(), user.getId());
        }

        redisUtil.del(realKey);
    }

    @Override
    public void updateTypeExt(SysUserConfig userConfig) {
        LoginUser user = MySecurityUtils.getCurrentUser();
        if (MyStringUtils.isBlank(userConfig.getValue())) {
            throw new PaasException("校验方式不能为空");
        }
        if (!ConfirmTypeEnum.PHONE_EMAIL.getType().equals(userConfig.getValue())) {
            SysUserConfig config = getConfigByType(user.getId(), SignCommonConstant.USERSIGNPASSWORDKEY);
            if (MyStringUtils.isBlank(config.getValue())) {
                throw new PaasException("签约密码不能为空");
            }
        }
        updateSignConfirmType(userConfig.getValue(), user.getId());
    }

    void updateSignConfirmType(String value, String userId) {
        SysUserConfig config = getConfigByType(userId, SignCommonConstant.USERSIGNCONFIRMTYPEKEY);
        config.setValue(value);
        this.updateById(config);
    }

}
