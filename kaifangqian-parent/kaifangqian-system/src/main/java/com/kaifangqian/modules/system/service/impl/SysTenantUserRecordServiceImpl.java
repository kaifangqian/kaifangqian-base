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
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.modules.opensign.service.flow.IFlowService;
import com.kaifangqian.modules.system.entity.SysTenantInfo;
import com.kaifangqian.modules.system.entity.SysTenantUser;
import com.kaifangqian.modules.system.entity.SysTenantUserRecord;
import com.kaifangqian.modules.system.mapper.SysTenantUserRecordMapper;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.dto.MailDto;
import com.kaifangqian.eunms.MesAuthType;
import com.kaifangqian.eunms.SendType;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.system.service.ISysTenantInfoService;
import com.kaifangqian.modules.system.service.ISysTenantUserRecordService;
import com.kaifangqian.modules.system.service.ISysTenantUserService;
import com.kaifangqian.utils.MyStringUtils;
import com.kaifangqian.utils.SysMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
/**
 * @author zhenghuihan
 * @description 租户用户申请服务类
 * @createTime 2022/9/2 18:13
 */
@Service
public class SysTenantUserRecordServiceImpl extends ServiceImpl<SysTenantUserRecordMapper, SysTenantUserRecord> implements ISysTenantUserRecordService {

    @Autowired
    private ISysTenantUserService sysTenantUserService;
    @Autowired
    private SysMessageUtil sysMessageUtil;
    @Autowired
    private ISysTenantInfoService sysTenantInfoService;
    @Autowired
    private IFlowService flowService;

    @Override
    public SysTenantUserRecord getByEntity(SysTenantUserRecord query) {
        LambdaQueryWrapper<SysTenantUserRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MyStringUtils.isNotBlank(query.getTenantId()), SysTenantUserRecord::getTenantId, query.getTenantId()).eq(MyStringUtils.isNotBlank(query.getUserId()), SysTenantUserRecord::getUserId, query.getUserId()).eq(SysTenantUserRecord::getDeleteFlag, 0);

        List<SysTenantUserRecord> list = this.list(queryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public IPage<SysTenantUserRecord> pageExt(Page<SysTenantUserRecord> page, SysTenantUserRecord tenantUserRecord) {
        LambdaQueryWrapper<SysTenantUserRecord> queryWrapper = new LambdaQueryWrapper<>();
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        queryWrapper.eq(tenantUserRecord.getStatus() != null, SysTenantUserRecord::getStatus, tenantUserRecord.getStatus()).like(MyStringUtils.isNotBlank(tenantUserRecord.getNickName()), SysTenantUserRecord::getNickName, tenantUserRecord.getNickName()).eq(SysTenantUserRecord::getDeleteFlag, 0).eq(SysTenantUserRecord::getTenantId, loginUser.getTenantId());
        IPage<SysTenantUserRecord> result = this.page(page, queryWrapper);
        queryWrapper.orderByDesc(SysTenantUserRecord::getApplyTime);

        List<SysTenantUserRecord> list = result.getRecords();
        if (CollUtil.isNotEmpty(list)) {
            List<String> tenantUserIds = list.stream().map(SysTenantUserRecord::getCheckTenantUserId).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(tenantUserIds)) {
                List<SysTenantUser> tenantUsers = sysTenantUserService.getByIds(tenantUserIds);
                if (CollUtil.isNotEmpty(tenantUsers)) {
                    Map<String, String> tenantUserMap = tenantUsers.stream().collect(Collectors.toMap(i -> i.getId(), i -> Optional.ofNullable(i.getNickName()).orElse(""), (v1, v2) -> v1));
                    list.forEach(l -> {
                        l.setCheckTenantUserName(tenantUserMap.get(l.getCheckTenantUserId()));
                    });
                }
            }
        }
        return result;
    }

    @Override
    public void check(SysTenantUserRecord tenantUserRecord) {
        if (MyStringUtils.isBlank(tenantUserRecord.getId()) || tenantUserRecord.getStatus() == null) {
            throw new PaasException("字段为空");
        }
        SysTenantUserRecord entity = this.getById(tenantUserRecord.getId());
        if (entity == null) {
            throw new PaasException("数据不存在");
        }
        if (entity.getStatus() != 0) {
            throw new PaasException("操作不支持");
        }
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        MailDto mailDto = new MailDto();
        mailDto.setSendType(SendType.IMMEDIATELY);
        Map<String, String> contentParaMap = new HashMap<>();
        SysTenantInfo sysTenantInfo = sysTenantInfoService.getById(entity.getTenantId());
        if (sysTenantInfo != null) {
            contentParaMap.put("companyName", sysTenantInfo.getTenantName());
        }
        SysTenantUser sysTenantUser = sysTenantUserService.getById(entity.getTenantUserId());
        if (sysTenantUser == null) {
            throw new PaasException("用户不存在");
        }
        if (1 == tenantUserRecord.getStatus()) {
            sysTenantUser.setStatus(1);
            sysTenantUserService.updateById(sysTenantUser);
            //发送站内信
            mailDto.setTemplateCode("join_success");
            //解析数据库已有数据做绑定表:sign_ru_task
            //绑定租户ID+用户ID：租户账号ID
            flowService.bindOutUserTask(null, null, null, entity.getTenantId(), entity.getUserId(), entity.getUserId(), entity.getTenantId(), entity.getTenantUserId(), "check");
        } else if (2 == tenantUserRecord.getStatus()) {
            sysTenantUser.setDeleteBy(loginUser.getUsername());
            sysTenantUser.setDeleteTime(new Date());
            sysTenantUser.setDeleteFlag(true);
            sysTenantUserService.updateById(sysTenantUser);
            mailDto.setTemplateCode("join_faild");
        } else {
            throw new PaasException("操作不支持");
        }
        Map<MesAuthType, List<String>> userMap = new HashMap<>();
        List<String> userIds = Arrays.asList(entity.getTenantUserId());
        userMap.put(MesAuthType.USER, userIds);
        mailDto.setReceivers(userMap);
        mailDto.setTitleParaMap(contentParaMap);
        mailDto.setContentParaMap(contentParaMap);
        //发送通知
        sysMessageUtil.asyncSendMail(mailDto);

        entity.setCheckTime(new Date());
        entity.setCheckTenantUserId(loginUser.getTenantUserId());
        entity.setStatus(tenantUserRecord.getStatus());

        this.updateById(entity);
    }
}
