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
import com.kaifangqian.modules.storage.service.IAnnexStorageService;
import com.kaifangqian.modules.system.entity.SysAnnouncement;
import com.kaifangqian.modules.system.entity.SysAnnouncementSend;
import com.kaifangqian.modules.system.entity.SysUser;
import com.kaifangqian.modules.system.mapper.SysAnnouncementMapper;
import com.kaifangqian.modules.system.util.XSSUtils;
import com.kaifangqian.common.constant.CommonConstant;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.dto.MailDto;
import com.kaifangqian.eunms.MesAuthType;
import com.kaifangqian.eunms.SendType;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.system.service.ISysAnnouncementSendService;
import com.kaifangqian.modules.system.service.ISysAnnouncementService;
import com.kaifangqian.modules.system.service.ISysUserService;
import com.kaifangqian.utils.MyStringUtils;
import com.kaifangqian.utils.SysMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
/**
 * @author zhenghuihan
 * @description 系统公告服务
 * @createTime 2022/9/2 17:40
 */
@Service
public class SysAnnouncementServiceImpl extends ServiceImpl<SysAnnouncementMapper, SysAnnouncement> implements ISysAnnouncementService {

    @Resource
    private SysAnnouncementMapper mapper;
    @Resource
    private ISysAnnouncementSendService sysAnnouncementSendService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IAnnexStorageService iAnnexStorageService;
    @Autowired
    private SysMessageUtil sysMessageUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveAnnouncement(SysAnnouncement sysAnnouncement) {
        String title = XSSUtils.striptXSS(sysAnnouncement.getTitle());
        sysAnnouncement.setTitle(title);
        sysAnnouncement.setSendStatus(CommonConstant.UNPUBLISHED_STATUS_0);//未发布
        //1、新增数据
        this.save(sysAnnouncement);
        //2、关联附件
        iAnnexStorageService.updateMainDataFiles(sysAnnouncement.getId(), sysAnnouncement.getFiles());

        // 3、补充新的通知用户数据
        addAnnouncementSend(sysAnnouncement);
    }

    /**
     * @功能：编辑消息信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAnnouncement(SysAnnouncement sysAnnouncement) {
        SysAnnouncement sysAnnouncementEntity = getById(sysAnnouncement.getId());
        if (sysAnnouncementEntity == null) {
            throw new PaasException("未找到对应实体");
        }
        if (sysAnnouncementEntity.getSendStatus().equals(CommonConstant.PUBLISHING_STATUS_1)) {
            throw new PaasException("发布中的不可以编辑");
        }
        if (sysAnnouncementEntity.getSendStatus().equals(CommonConstant.PUBLISHED_STATUS_2)) {
            throw new PaasException("已发布的不可以编辑");
        }
        String title = XSSUtils.striptXSS(sysAnnouncement.getTitle());
        sysAnnouncement.setTitle(title);
        // 1.更新系统信息表数据
        this.updateById(sysAnnouncement);
        //2、关联附件
        iAnnexStorageService.updateMainDataFiles(sysAnnouncement.getId(), sysAnnouncement.getFiles());

        // 3、 删除多余通知用户数据
        sysAnnouncementSendService.deleteByAnntId(sysAnnouncement.getId());
        // 4、补充新的通知用户数据
        addAnnouncementSend(sysAnnouncement);
    }

    void addAnnouncementSend(SysAnnouncement sysAnnouncement) {
        String anntId = sysAnnouncement.getId();
        if (sysAnnouncement.getMsgType().equals(CommonConstant.MSG_TYPE_UESR) && MyStringUtils.isNotBlank(sysAnnouncement.getUserIds())) {

            // 插入用户通告阅读标记表记录
            String[] userIds = sysAnnouncement.getUserIds().split(",");
            for (int i = 0; i < userIds.length; i++) {
                SysAnnouncementSend announcementSend = new SysAnnouncementSend();
                announcementSend.setAnntId(anntId);
                announcementSend.setUserId(userIds[i]);
                sysAnnouncementSendService.saveExt(announcementSend);
            }
        } else if (sysAnnouncement.getMsgType().equals(CommonConstant.MSG_TYPE_ALL)) {
            List<SysUser> sysUsers = sysUserService.listAll();
            if (CollUtil.isNotEmpty(sysUsers)) {
                sysUsers.forEach(s -> {
                    SysAnnouncementSend announcementSend = new SysAnnouncementSend();
                    announcementSend.setAnntId(anntId);
                    announcementSend.setUserId(s.getId());
                    sysAnnouncementSendService.saveExt(announcementSend);
                });
            }
        }
    }

    @Override
    public IPage<SysAnnouncement> pageExt(Page<SysAnnouncement> page, SysAnnouncement sysAnnouncement) {
        return mapper.pageExt(page, sysAnnouncement);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatchByIdsExt(List<String> ids) {
        if (CollUtil.isNotEmpty(ids)) {
            List<SysAnnouncement> announcements = new ArrayList<>();
            LoginUser user = MySecurityUtils.getCurrentUser();
            ids.forEach(i -> {
                SysAnnouncement temp = new SysAnnouncement();
                temp.setId(i);
                temp.setDeleteFlag(true);
                temp.setDeleteTime(new Date());
                temp.setDeleteBy(user.getUsername());

                announcements.add(temp);
            });
            this.updateBatchById(announcements);
        }
    }

    @Override
    public SysAnnouncement getByIdExt(String id) {
        SysAnnouncement announcement = this.getById(id);
        announcement.setFiles(iAnnexStorageService.getByFatherId(id));

        return announcement;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doReleaseData(String id) {
        SysAnnouncement sysAnnouncement = getById(id);
        if (sysAnnouncement == null) {
            throw new PaasException("未找到对应实体");
        } else {
            if (sysAnnouncement.getSendStatus().equals(CommonConstant.PUBLISHING_STATUS_1) || sysAnnouncement.getSendStatus().equals(CommonConstant.PUBLISHED_STATUS_2)) {
                throw new PaasException("发布中或已发布的公告不可以再次发布");
            } else {
                if (new Date().before(sysAnnouncement.getStartTime())) {
                    sysAnnouncement.setSendStatus(CommonConstant.PUBLISHING_STATUS_1);//发布中
                } else if (new Date().after(sysAnnouncement.getEndTime())) {
                    throw new PaasException("有效时间段不合理,不可以发布");
                } else {
                    sysAnnouncement.setSendStatus(CommonConstant.PUBLISHED_STATUS_2);//已发布
                    //消息通知
                    List<SysAnnouncementSend> sendModels = sysAnnouncementSendService.getByAnntId(id);
                    if (CollUtil.isNotEmpty(sendModels)) {
                        MailDto mailDto = new MailDto();
                        mailDto.setSendType(SendType.IMMEDIATELY);

                        Map<MesAuthType, List<String>> userMap = new HashMap<>();
                        List<String> userIds = sendModels.stream().map(SysAnnouncementSend::getUserId).collect(Collectors.toList());
                        userMap.put(MesAuthType.USER, userIds);
                        mailDto.setReceivers(userMap);

                        mailDto.setTemplateCode("announcement_mail");

                        Map<String, String> titleParaMap = new HashMap<>();
                        titleParaMap.put("title", sysAnnouncement.getMsgAbstract());
                        mailDto.setTitleParaMap(titleParaMap);

                        Map<String, Map<String, String>> buttonParaMap = new HashMap<>();
                        Map<String, String> para = new HashMap<>();
                        para.put("infoId", sysAnnouncement.getId());
                        buttonParaMap.put("info", para);
                        mailDto.setButtonParaMap(buttonParaMap);

                        //发送通知
                        sysMessageUtil.asyncSendMail(mailDto);
                    }
                }
                sysAnnouncement.setSendTime(new Date());
                LoginUser user = MySecurityUtils.getCurrentUser();
                sysAnnouncement.setSender(user.getUsername());
                updateById(sysAnnouncement);
            }
        }
    }

    @Override
    public void doReovkeData(String id) {
        SysAnnouncement sysAnnouncement = getById(id);
        if (sysAnnouncement == null) {
            throw new PaasException("未找到对应实体");
        } else {
            sysAnnouncement.setSendStatus(CommonConstant.REVOKE_STATUS_3);//撤销发布
            sysAnnouncement.setCancelTime(new Date());
            updateById(sysAnnouncement);
        }
    }

    @Override
    public List<SysAnnouncement> getJobPublishedList() {
        LambdaQueryWrapper<SysAnnouncement> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysAnnouncement::getSendStatus, CommonConstant.PUBLISHED_STATUS_2)
                .lt(SysAnnouncement::getEndTime, new Date());

        return this.list(queryWrapper);
    }

    @Override
    public List<SysAnnouncement> getJobPublishingList() {
        LambdaQueryWrapper<SysAnnouncement> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysAnnouncement::getSendStatus, CommonConstant.PUBLISHING_STATUS_1)
                .lt(SysAnnouncement::getStartTime, new Date())
                .gt(SysAnnouncement::getEndTime, new Date());

        return this.list(queryWrapper);
    }
}
