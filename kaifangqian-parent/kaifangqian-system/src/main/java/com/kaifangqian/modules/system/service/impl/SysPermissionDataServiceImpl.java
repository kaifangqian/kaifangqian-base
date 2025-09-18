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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaifangqian.modules.system.entity.SysPermissionData;
import com.kaifangqian.modules.system.entity.SysPermissionDataRule;
import com.kaifangqian.modules.system.mapper.SysPermissionDataMapper;
import com.kaifangqian.modules.system.vo.SysPermissionDataRuleVO;
import com.kaifangqian.modules.system.vo.SysPermissionDataVO;
import com.kaifangqian.common.constant.enums.PermissionDataType;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.modules.system.service.ISysPermissionDataRuleService;
import com.kaifangqian.modules.system.service.ISysPermissionDataService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhenghuihan
 * @description 权限策略表
 * @createTime 2022/9/2 18:17
 */
@Service
public class SysPermissionDataServiceImpl extends ServiceImpl<SysPermissionDataMapper, SysPermissionData> implements ISysPermissionDataService {

    @Autowired
    private ISysPermissionDataRuleService iSysPermissionDataRuleService;


    @Override
    public IPage<SysPermissionData> pageExt(Page<SysPermissionData> page, String permissionId) {
        LambdaQueryWrapper<SysPermissionData> query = new LambdaQueryWrapper<SysPermissionData>();
        query.eq(SysPermissionData::getPermissionId, permissionId)
                .isNull(SysPermissionData::getTenantId);

        query.orderByAsc(SysPermissionData::getOrderNo);

        return this.page(page, query);
    }

    @Override
    public IPage<SysPermissionData> pageExt2(Page<SysPermissionData> page, String permissionId) {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        LambdaQueryWrapper<SysPermissionData> query = new LambdaQueryWrapper<SysPermissionData>();
        query.eq(SysPermissionData::getPermissionId, permissionId)
                .and(q -> q
                        .eq(SysPermissionData::getTenantId, loginUser.getTenantId())
                        .or()
                        .isNull(SysPermissionData::getTenantId));

        query.orderByAsc(SysPermissionData::getOrderNo);

        return this.page(page, query);
    }

    @Override
    public void addDefault(String permissionId) {
        //新增数据
        List<PermissionDataType> dataTypes = PermissionDataType.getAll();
        if (CollUtil.isNotEmpty(dataTypes)) {
            dataTypes.forEach(t -> {
                SysPermissionData permissionData = getSysPermissionData(permissionId, t);
                switch (t) {
                    case SELF:
                        this.save(permissionData);
                        addSysPermissionDataRule(permissionData.getId(), "sysUserId", "=", "#{sysUserId}");
                        break;
                    case DEPART:
                        this.save(permissionData);
                        addSysPermissionDataRule(permissionData.getId(), "sysOrgCode", "=", "#{sysOrgCode}");
                        break;
                    case DEPARTALL:
                        this.save(permissionData);
                        addSysPermissionDataRule(permissionData.getId(), "sysOrgCode", "RIGHT_LIKE", "#{sysOrgCode}");
                        break;
                    case ALL:
                        this.save(permissionData);
                        break;
                    default:
                        break;
                }
            });
        }
    }

    SysPermissionData getSysPermissionData(String permissionId, PermissionDataType dataType) {
        SysPermissionData data = new SysPermissionData();
        data.setPermissionId(permissionId);
        data.setDataType(dataType.getType());
        data.setDataName(dataType.getName());
        data.setDataDesc(dataType.getDesc());
        data.setOrderNo(dataType.getOrderNo());
        data.setDefaultFlag(1);

        return data;
    }

    void addSysPermissionDataRule(String dataId, String ruleColumn, String ruleCondition, String ruleValue) {
        SysPermissionDataRule dataRule = new SysPermissionDataRule();
        dataRule.setConditionGroup(1);
        dataRule.setDataId(dataId);
        dataRule.setRuleColumn(ruleColumn);
        dataRule.setRuleConditions(ruleCondition);
        dataRule.setRuleValue(ruleValue);

        iSysPermissionDataRuleService.save(dataRule);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addCustom(SysPermissionDataVO dataVO) {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        //新增主数据
        SysPermissionData data = new SysPermissionData();
        BeanUtils.copyProperties(dataVO, data);
        data.setDefaultFlag(2);
        data.setDataType(PermissionDataType.CUSTOM.getType());
        data.setTenantId(loginUser.getTenantId());

        this.save(data);

        //更新子数据
        dataVO.setId(data.getId());
        iSysPermissionDataRuleService.updateExt(dataVO);
    }

    @Override
    public void addAllCustom(SysPermissionDataVO dataVO) {
        //新增主数据
        SysPermissionData data = new SysPermissionData();
        BeanUtils.copyProperties(dataVO, data);
        data.setDefaultFlag(2);
        data.setDataType(PermissionDataType.CUSTOM.getType());

        this.save(data);

        //更新子数据
        dataVO.setId(data.getId());
        iSysPermissionDataRuleService.updateExt(dataVO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editCustom(SysPermissionDataVO dataVO) {
        //更新主数据
        SysPermissionData data = new SysPermissionData();
        data.setId(dataVO.getId());
        data.setDataName(dataVO.getDataName());
        data.setDataDesc(dataVO.getDataDesc());

        this.updateById(data);

        //更新子数据
        iSysPermissionDataRuleService.updateExt(dataVO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteExt(String id) {
        this.removeById(id);

        //删除子数据
        iSysPermissionDataRuleService.deleteByDataId(id);
    }

    @Override
    public SysPermissionDataVO getByIdExt(String id) {
        SysPermissionDataVO dataVO = new SysPermissionDataVO();
        SysPermissionData data = this.getById(id);
        if (data != null) {
            BeanUtils.copyProperties(data, dataVO);

            SysPermissionDataRule query = new SysPermissionDataRule();
            query.setDataId(id);
            List<SysPermissionDataRule> list = iSysPermissionDataRuleService.getByEntity(query);
            if (CollUtil.isNotEmpty(list)) {
                List<SysPermissionDataRuleVO> rules = new ArrayList<>();
                list.forEach(l -> {
                    SysPermissionDataRuleVO vo = new SysPermissionDataRuleVO();
                    BeanUtils.copyProperties(l, vo);

                    rules.add(vo);
                });

                dataVO.setRules(rules);
            }
        }

        return dataVO;
    }

    @Override
    public void deleteByPermissionId(String permissionId) {
        LambdaQueryWrapper<SysPermissionData> wrapper = new LambdaQueryWrapper<SysPermissionData>();
        wrapper.eq(SysPermissionData::getPermissionId, permissionId);
        this.remove(wrapper);
    }

    @Override
    public List<SysPermissionData> listByPermissionId(String permissionId) {
        LambdaQueryWrapper<SysPermissionData> wrapper = new LambdaQueryWrapper<SysPermissionData>();
        wrapper.eq(SysPermissionData::getPermissionId, permissionId);
        return this.list(wrapper);
    }

    @Override
    public List<SysPermissionData> listTenantRuleByPermissionIds(List<String> permissionIds) {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        LambdaQueryWrapper<SysPermissionData> query = new LambdaQueryWrapper<SysPermissionData>();
        query.in(SysPermissionData::getPermissionId, permissionIds)
                .and(q -> q
                        .eq(SysPermissionData::getTenantId, loginUser.getTenantId())
                        .or()
                        .isNull(SysPermissionData::getTenantId));

        query.orderByAsc(SysPermissionData::getOrderNo);

        return this.list(query);
    }
}
