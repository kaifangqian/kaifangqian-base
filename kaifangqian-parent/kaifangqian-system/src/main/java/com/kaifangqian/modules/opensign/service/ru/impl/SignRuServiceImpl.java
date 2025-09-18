/**
 * @description 签署文档主业务接口实现类
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
package com.kaifangqian.modules.opensign.service.ru.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.modules.opensign.entity.SignRe;
import com.kaifangqian.modules.opensign.entity.SignRu;
import com.kaifangqian.modules.opensign.entity.SignRuRelation;
import com.kaifangqian.modules.opensign.entity.SignRuTask;
import com.kaifangqian.modules.opensign.service.business.RuCallbackService;
import com.kaifangqian.common.base.entity.BaseEntity;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.dto.MailDto;
import com.kaifangqian.eunms.MesAuthType;
import com.kaifangqian.eunms.SendType;
import com.kaifangqian.modules.opensign.entity.*;
import com.kaifangqian.modules.opensign.enums.SignCallbackTypeEnum;
import com.kaifangqian.modules.opensign.enums.SignRuStatusEnum;
import com.kaifangqian.modules.opensign.mapper.SignRuMapper;
import com.kaifangqian.modules.opensign.service.re.SignReAuthService;
import com.kaifangqian.modules.opensign.service.re.SignReService;
import com.kaifangqian.modules.opensign.service.ru.SignRuRelationService;
import com.kaifangqian.modules.opensign.service.ru.SignRuService;
import com.kaifangqian.modules.opensign.service.ru.SignRuTaskService;
import com.kaifangqian.utils.MyStringUtils;
import com.kaifangqian.utils.SysMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @Description: SignRuServiceImpl
 * @Package: com.kaifangqian.modules.opensign.service.ru.impl
 * @ClassName: SignRuServiceImpl
 * @author: FengLai_Gong
 */
@Service
public class SignRuServiceImpl extends ServiceImpl<SignRuMapper, SignRu> implements SignRuService {

    @Autowired
    private SignReAuthService signReAuthService;
    @Autowired
    private SignRuTaskService signRuTaskService;
    @Autowired
    private SignRuRelationService signRuRelationService;
    @Autowired
    private SignReService signReService;
    @Autowired
    private SysMessageUtil sysMessageUtil;

    @Autowired
    private RuCallbackService ruCallbackService ;

    @Override
    public Integer countMyByStatus(Integer status, List<Integer> statusList) {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        LambdaQueryWrapper<SignRu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SignRu::getSysUserId, loginUser.getTenantUserId()).eq(SignRu::getDeleteFlag, 0).eq(status != null, SignRu::getStatus, status).in(CollUtil.isNotEmpty(statusList), SignRu::getStatus, statusList);

        return (int)count(queryWrapper);
    }

    @Override
    public void updateStatus() {
        List<Integer> statusList = new ArrayList<>();
        statusList.add(SignRuStatusEnum.WRITING.getCode());
        statusList.add(SignRuStatusEnum.SIGNING.getCode());

        LambdaQueryWrapper<SignRu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SignRu::getDeleteFlag, 0).in(SignRu::getStatus, statusList).le(SignRu::getExpireDate, new Date());

        //失效设置以及提醒
        List<SignRu> result = list(queryWrapper);
        if (CollUtil.isNotEmpty(result)) {
            result.forEach(r -> {
                r.setStatus(SignRuStatusEnum.EXPIRE.getCode());
                sendExpireMail(r.getSysUserId(), r.getId(), r.getSubject());
                //回调合同过期
                ruCallbackService.callback(r.getId(),null, SignCallbackTypeEnum.EXPIRED);
            });
            this.updateBatchById(result);
        }

        //即将失效提醒
        LambdaQueryWrapper<SignRu> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(SignRu::getDeleteFlag, 0).in(SignRu::getStatus, statusList).le(SignRu::getExpireDate, LocalDateTime.now().plusDays(3));

        //失效设置以及提醒
        List<SignRu> result2 = list(queryWrapper2);

        if (CollUtil.isNotEmpty(result2)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            result2.forEach(r -> {
                sendNoticeMail(r.getSysUserId(), r.getSignReId(), r.getId(), r.getSubject(), dateFormat.format(r.getExpireDate()));
            });
            this.updateBatchById(result);
        }
    }

    void sendExpireMail(String tenantUserId, String signRuId, String subject) {
        MailDto mailDto = new MailDto();
        mailDto.setSendType(SendType.IMMEDIATELY);

        Map<MesAuthType, List<String>> userMap = new HashMap<>();
        List<String> userIds = Arrays.asList(tenantUserId);
        userMap.put(MesAuthType.USER, userIds);
        mailDto.setReceivers(userMap);

        mailDto.setTemplateCode("contract_sign_expired");

        Map<String, String> titleParaMap = new HashMap<>();
        titleParaMap.put("contract", subject);
        mailDto.setTitleParaMap(titleParaMap);

        Map<String, String> contentParaMap = new HashMap<>();
        contentParaMap.put("contract", subject);
        mailDto.setContentParaMap(contentParaMap);

        Map<String, Map<String, String>> buttonParaMap = new HashMap<>();
        Map<String, String> para = new HashMap<>();
        para.put("__full__", "");
        para.put("signRuId", signRuId);
        buttonParaMap.put("contract_expired", para);
        mailDto.setButtonParaMap(buttonParaMap);

        //发送通知
        sysMessageUtil.asyncSendMail(mailDto);
    }


    void sendNoticeMail(String tenantUserId, String signReId, String signRuId, String subject, String expireDate) {
        MailDto mailDto = new MailDto();
        mailDto.setSendType(SendType.IMMEDIATELY);

        Map<MesAuthType, List<String>> userMap = new HashMap<>();
        List<String> userIds = Arrays.asList(tenantUserId);
        userMap.put(MesAuthType.USER, userIds);
        mailDto.setReceivers(userMap);

        mailDto.setTemplateCode("sign_about_expired");

        Map<String, String> titleParaMap = new HashMap<>();
        titleParaMap.put("contract", subject);
        mailDto.setTitleParaMap(titleParaMap);

        Map<String, String> contentParaMap = new HashMap<>();
        contentParaMap.put("contract", subject);
        contentParaMap.put("expire_date", expireDate);
        mailDto.setContentParaMap(contentParaMap);

        Map<String, Map<String, String>> buttonParaMap = new HashMap<>();
        Map<String, String> para = new HashMap<>();
        para.put("__full__", "");
        para.put("type", "");
        para.put("from", "list");
        para.put("isDetail", "false");
        para.put("signReId", signReId);
        para.put("signRuId", signRuId);
        buttonParaMap.put("about_expired", para);
        mailDto.setButtonParaMap(buttonParaMap);

        //发送通知
        sysMessageUtil.asyncSendMail(mailDto);
    }

    @Override
    public Boolean updateExpireDate(Date expireDate, String id) {
        UpdateWrapper<SignRu> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(SignRu::getId, id);
        wrapper.lambda().eq(BaseEntity::getDeleteFlag, false);
        wrapper.lambda().set(SignRu::getExpireDate, expireDate);
        return this.update(wrapper);
    }

    @Override
    public Boolean checkDownloadAuth(String signRuId) {
        if (MyStringUtils.isBlank(signRuId)) {
            return false;
        }
        SignRu signRu = this.getById(signRuId);
        if (signRu == null) {
            return false;
        }
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        //下载权限类型
//        List<Integer> authTypeList = new ArrayList<>();
//        authTypeList.add(4);
//        List<SignReAuth> auths = signReAuthService.listByParam(signRu.getSignReId(), loginUser.getTenantId(), null, authTypeList);
//        if (CollUtil.isEmpty(auths)) {
//            return false;
//        }
//        SignReAuth auth = auths.get(0);
        SignRe signRe = signReService.getById(signRu.getSignReId());
        if (signRe == null) {
            return false;
        }
        if (signRe.getDownloaderType() == 1 || signRe.getDownloaderType() == 3) {
            //参与人
            SignRuTask query = new SignRuTask();
            query.setSignRuId(signRuId);
            query.setTenantUserId(loginUser.getTenantUserId());
            List<SignRuTask> tasks = signRuTaskService.getByEntity(query);
            if (CollUtil.isNotEmpty(tasks)) {
                return true;
            }
//            //抄送人
//            SignRuCcer query2 = new SignRuCcer();
//            query2.setSignRuId(signRuId);
//            query2.setTenantUserId(loginUser.getTenantUserId());
//            List<SignRuCcer> ruCcers = signRuCcerService.getByEntity(query2);
//            if (CollUtil.isNotEmpty(ruCcers)) {
//                return true;
//            }
        }
        if (signRe.getDownloaderType() == 2 || signRe.getDownloaderType() == 3) {
            //指定查看人
            List<Integer> auth3List = new ArrayList<>();
            auth3List.add(3);
            Integer count3 = signReAuthService.countByParam(signRu.getSignReId(), loginUser.getTenantId(), loginUser.getTenantUserId(), auth3List);
            if (count3 > 0) {
                return true;
            }
        }
        //指定下载人
        List<Integer> auth4List = new ArrayList<>();
        auth4List.add(4);
        Integer count4 = signReAuthService.countByParam(signRu.getSignReId(), loginUser.getTenantId(), loginUser.getTenantUserId(), auth4List);
        if (count4 > 0) {
            return true;
        }
        return false;
    }


    @Override
    public Boolean checkViewAuth(String signRuId) {
        if (MyStringUtils.isBlank(signRuId)) {
            return false;
        }
        SignRu signRu = this.getById(signRuId);
        if (signRu == null) {
            return false;
        }
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        //参与人
        SignRuTask query = new SignRuTask();
        query.setSignRuId(signRuId);
        query.setTenantUserId(loginUser.getTenantUserId());
        List<SignRuTask> tasks = signRuTaskService.getByEntity(query);
        if (CollUtil.isNotEmpty(tasks)) {
            return true;
        }
        //抄送人

        SignRuRelation query2 = new SignRuRelation();
        query2.setRelationType(2);
        query2.setSignRuId(signRuId);
        query2.setTenantUserId(loginUser.getTenantUserId());
        List<SignRuRelation> ruRelations = signRuRelationService.getByEntity(query2);
        if (CollUtil.isNotEmpty(ruRelations)) {
            return true;
        }
        //指定查看人
        List<Integer> auth3List = new ArrayList<>();
        auth3List.add(3);
        Integer count3 = signReAuthService.countByParam(signRu.getSignReId(), loginUser.getTenantId(), loginUser.getTenantUserId(), auth3List);
        if (count3 > 0) {
            return true;
        }
        return false;
    }

    @Override
    public SignRuTask getMyTask(String signRuId) {
        SignRuTask query = new SignRuTask();
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        query.setSignRuId(signRuId);
        query.setUserId(loginUser.getId());
        query.setTaskStatus(1);
        List<SignRuTask> tasks = signRuTaskService.getByEntity(query);
        if (CollUtil.isNotEmpty(tasks)) {
            return tasks.get(0);
        }
        return null;
    }

    @Override
    public Boolean allTaskComplete(String signRuId) {

        SignRuTask query = new SignRuTask();
        query.setSignRuId(signRuId);
        query.setTaskStatus(1);
        List<SignRuTask> tasks = signRuTaskService.getByEntity(query);
        if (tasks == null || tasks.size() == 0) {
            return true;
        }
        return false;
    }
}