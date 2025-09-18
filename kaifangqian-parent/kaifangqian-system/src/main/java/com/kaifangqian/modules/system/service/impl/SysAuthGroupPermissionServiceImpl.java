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
import com.kaifangqian.modules.system.entity.SysAuthGroupPermission;
import com.kaifangqian.modules.system.mapper.SysAuthGroupPermissionMapper;
import com.kaifangqian.modules.system.vo.AppPermissionVO;
import com.kaifangqian.modules.system.vo.PermissionRuleVO;
import com.kaifangqian.modules.system.vo.SysAuthGroupPermissionReq;
import com.kaifangqian.modules.system.service.ISysAuthGroupPermissionService;
import com.kaifangqian.utils.MyStringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhenghuihan
 * @description 权限组-权限表
 * @createTime 2022/9/2 18:16
 */
@Service
public class SysAuthGroupPermissionServiceImpl extends ServiceImpl<SysAuthGroupPermissionMapper, SysAuthGroupPermission> implements ISysAuthGroupPermissionService {

    @Override
    public List<SysAuthGroupPermission> listByEntity(SysAuthGroupPermission sysAuthGroupPermission) {
        LambdaQueryWrapper<SysAuthGroupPermission> query = new LambdaQueryWrapper<SysAuthGroupPermission>();
        query.eq(MyStringUtils.isNotBlank(sysAuthGroupPermission.getGroupId()), SysAuthGroupPermission::getGroupId, sysAuthGroupPermission.getGroupId())
                .eq(MyStringUtils.isNotBlank(sysAuthGroupPermission.getPermissionId()), SysAuthGroupPermission::getPermissionId, sysAuthGroupPermission.getPermissionId())
        ;

        return this.list(query);
    }

    @Override
    public void editExt(SysAuthGroupPermissionReq req) {
        if (MyStringUtils.isNotBlank(req.getGroupId())) {
            //删除历史数据
            LambdaQueryWrapper<SysAuthGroupPermission> deleteQuery = new LambdaQueryWrapper<SysAuthGroupPermission>();
            deleteQuery.eq(SysAuthGroupPermission::getGroupId, req.getGroupId());
            this.remove(deleteQuery);
            //新增数据
            addGroupPermission(req);
        }
    }

    @Override
    public void addExt(SysAuthGroupPermissionReq req) {
        if (MyStringUtils.isNotBlank(req.getGroupId())) {
            addGroupPermission(req);
        }
    }

    private void addGroupPermission(SysAuthGroupPermissionReq req) {
        //新增数据
        List<SysAuthGroupPermission> newData = new ArrayList<>();
        List<AppPermissionVO> appPermissionTem = req.getAppPermissionVOS();
        if (CollUtil.isNotEmpty(appPermissionTem)) {
            appPermissionTem.forEach(a -> {
                List<PermissionRuleVO> temDate = a.getRuleVOS();
                if (CollUtil.isNotEmpty(temDate)) {
                    temDate.forEach(n -> {
                        if (MyStringUtils.isNotBlank(n.getPermissionId())) {
                            if (CollUtil.isNotEmpty(n.getRuleIds())) {
                                n.getRuleIds().forEach(r -> {
                                    SysAuthGroupPermission tem = new SysAuthGroupPermission();
                                    tem.setGroupId(req.getGroupId());
                                    tem.setAppId(a.getAppId());
                                    tem.setPermissionId(n.getPermissionId());
                                    tem.setPermissionDataId(r);

                                    newData.add(tem);
                                });
                            } else {
                                SysAuthGroupPermission tem = new SysAuthGroupPermission();
                                tem.setGroupId(req.getGroupId());
                                tem.setAppId(a.getAppId());
                                tem.setPermissionId(n.getPermissionId());

                                newData.add(tem);
                            }
                        }
                    });
                }
            });
        }
        if (CollUtil.isNotEmpty(newData)) {
            this.saveBatch(newData);
        }
    }

    @Override
    public void deleteByPermissionId(String permissionId) {
        LambdaQueryWrapper<SysAuthGroupPermission> wrapper = new LambdaQueryWrapper<SysAuthGroupPermission>();
        wrapper.eq(SysAuthGroupPermission::getPermissionId, permissionId);

        this.remove(wrapper);
    }
}
