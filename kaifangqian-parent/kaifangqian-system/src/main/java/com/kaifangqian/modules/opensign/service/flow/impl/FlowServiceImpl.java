/**
 * @description 流程驱动服务实现类
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
package com.kaifangqian.modules.opensign.service.flow.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.kaifangqian.modules.opensign.entity.*;
import com.kaifangqian.modules.opensign.service.ru.*;
import com.kaifangqian.modules.system.entity.SysAppInfo;
import com.kaifangqian.modules.system.entity.SysTenantInfo;
import com.kaifangqian.modules.system.entity.SysTenantUser;
import com.kaifangqian.modules.system.entity.SysUser;
import com.kaifangqian.common.constant.CommonConstant;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.config.FileProperties;
import com.kaifangqian.dto.EmailDto;
import com.kaifangqian.dto.MailDto;
import com.kaifangqian.dto.MessageDto;
import com.kaifangqian.entity.MessageShortMapping;
import com.kaifangqian.entity.SysConfig;
import com.kaifangqian.enums.SysConfigType;
import com.kaifangqian.eunms.MesAuthType;
import com.kaifangqian.eunms.SendType;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.external.sms.request.MsgRequest;
import com.kaifangqian.external.sms.service.SmsSendService;
import com.kaifangqian.inteface.CommonToolsAPI;
import com.kaifangqian.modules.opensign.config.OpensignFlowIntiConfig;
import com.kaifangqian.modules.opensign.dto.OpenSignTaskInfo;
import com.kaifangqian.modules.opensign.dto.SignTaskInfo;
import com.kaifangqian.modules.opensign.dto.SignTaskThreadlocalVO;
import com.kaifangqian.modules.opensign.dto.TaskCmdInfo;
import com.kaifangqian.modules.opensign.entity.*;
import com.kaifangqian.modules.opensign.enums.ReNoticeTypeEnum;
import com.kaifangqian.modules.opensign.enums.SignFileEnum;
import com.kaifangqian.modules.opensign.enums.TaskTypeEnum;
import com.kaifangqian.modules.opensign.interceptor.SignCommand;
import com.kaifangqian.modules.opensign.service.business.RuSignFlowService;
import com.kaifangqian.modules.opensign.service.business.RuSignReportService;
import com.kaifangqian.modules.opensign.service.business.vo.SignReportDocFileVo;
import com.kaifangqian.modules.opensign.service.business.vo.SignReportSignSubVO;
import com.kaifangqian.modules.opensign.service.flow.IFlowService;
import com.kaifangqian.modules.opensign.service.re.SignReNoticeService;
import com.kaifangqian.modules.opensign.service.ru.*;
import com.kaifangqian.modules.storage.service.IAnnexStorageService;
import com.kaifangqian.modules.system.service.ISysAppInfoService;
import com.kaifangqian.modules.system.service.ISysTenantInfoService;
import com.kaifangqian.modules.system.service.ISysTenantUserService;
import com.kaifangqian.modules.system.service.ISysUserService;
import com.kaifangqian.service.MessageShortMappingService;
import com.kaifangqian.utils.JacksonUtil;
import com.kaifangqian.utils.MyStringUtils;
import com.kaifangqian.utils.SysMessageUtil;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author : zhenghuihan
 * create at:  2023/11/8  15:11
 * @description: 流程驱动服务实现类
 */
@Service
public class FlowServiceImpl extends SignServiceImpl implements IFlowService {
    @Autowired
    private SignRuTaskService signRuTaskService;
    @Autowired
    private SignRuOperatorService signRuOperatorService;
    @Autowired
    private ISysTenantUserService sysTenantUserService;
    @Autowired
    private ISysUserService sysUserService;
    //流程实例-签署主表
    @Autowired
    private SignRuSignerService signRuSignerService;
    //流程实例-内部签署表
    @Autowired
    private SignRuSenderService signRuSenderService;
    @Autowired
    private SignRuCcerService signRuCcerService;
    @Autowired
    private SignRuRelationService signRuRelationService;
    @Autowired
    private RuSignFlowService ruSignFlowService;
    @Autowired
    private SysMessageUtil sysMessageUtil;
    @Autowired
    private SignRuService signRuService;
    @Autowired
    private ISysTenantInfoService sysTenantInfoService;
    @Autowired
    private ISysTenantUserService iSysTenantUserService;
    @Autowired
    private FileProperties fileProperties;
    @Autowired
    private RuSignReportService ruSignReportService;
    @Autowired
    private IAnnexStorageService annexStorageService;
    @Autowired
    private SignReNoticeService signReNoticeService;
    @Autowired(required = false)
    private SmsSendService smsSendService;
    @Autowired
    private CommonToolsAPI commonToolsAPI;

    /**
     * 驱动流程:instanceId:实例ID taskId:任务ID
     * operate：
     * 初始化流程：initiateFlow
     * 同意填写、签署：approve
     * 拒绝签署、填写：reject
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void complete(String signRuId, String operate) {
        if (MyStringUtils.isBlank(operate)) {
            throw new PaasException("驱动流程错误：操作不能为空");
        }
        SignTaskThreadlocalVO threadlocalVO = SignTaskInfo.THREAD_LOCAL.get();
        String taskType;
        if (MyStringUtils.isNotBlank(signRuId)) {
            threadlocalVO.setSignRuId(signRuId);
            taskType = OpensignFlowIntiConfig.taskMap.get(TaskTypeEnum.INITIATE_FLOW.getCode()).getType();
        } else if (MyStringUtils.isNotBlank(threadlocalVO.getTaskType())) {
            taskType = threadlocalVO.getTaskType();
        } else {
            throw new PaasException("驱动流程错误：节点未知");
        }
        TaskCmdInfo taskCmdInfo = new TaskCmdInfo();
        taskCmdInfo.setTaskType(taskType);
        taskCmdInfo.setOperate(operate);
        while (taskCmdInfo != null) {
            threadlocalVO.setTaskType(taskCmdInfo.getTaskType());
            taskCmdInfo = signCommandExecutor.execute(getCommand(taskCmdInfo));
        }
    }

    private SignCommand<TaskCmdInfo> getCommand(TaskCmdInfo taskCmdInfo) {
        if (MyStringUtils.isBlank(taskCmdInfo.getTaskType()) || MyStringUtils.isBlank(taskCmdInfo.getOperate())) {
            throw new PaasException("驱动流程错误：获取驱动命令参数缺失");
        }
        try {
            OpenSignTaskInfo openSignTaskInfo = OpensignFlowIntiConfig.taskMap.get(taskCmdInfo.getTaskType());
            if (openSignTaskInfo == null) {
                throw new PaasException("驱动流程错误：节点类型不支持");
            }
            String operateCmd = openSignTaskInfo.getOperateMap().get(taskCmdInfo.getOperate());
            if (MyStringUtils.isBlank(operateCmd)) {
                throw new PaasException("驱动流程错误：操作类型不支持");
            }
            Class command = Class.forName(operateCmd);
            return (SignCommand) command.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void computeFillInTask(List<SignRuOperator> list, Integer type, String signRuId) {
        List<SignRuTask> tasks = new ArrayList<>();
        SignRu signRu = signRuService.getById(signRuId);
        if (signRu == null) {
            return;
        }
        SysTenantUser sysTenantUser = sysTenantUserService.getById(signRu.getSysUserId());
        String sender = sysTenantUser != null ? sysTenantUser.getNickName() : "未知用户";
        list.forEach(l -> {
            //初始化填写节点任务
            SignRuTask task = new SignRuTask();
            task.setSignRuId(signRuId);
            task.setTaskType(TaskTypeEnum.WRITE_TASK.getCode());
            task.setUserType(l.getSignerType());
            task.setUserTaskId(l.getSignerId());
            if (MyStringUtils.isNotBlank(l.getOperateUserId())) {
                SysTenantUser tenantUser = sysTenantUserService.getById(l.getOperateUserId());
                if (tenantUser != null) {
                    task.setUserId(tenantUser.getUserId());
                    task.setTenantUserId(l.getOperateUserId());
                    task.setTenantId(tenantUser.getTenantId());
                    task.setTaskLinkType("system");
                    if (MyStringUtils.isNotBlank(tenantUser.getUserId())) {
                        SysUser sysUser = sysUserService.getById(tenantUser.getUserId());
                        if (sysUser != null) {
                            task.setPhone(sysUser.getPhone());
                            task.setEmail(sysUser.getEmail());
                        }
                    }
                    if (MyStringUtils.isNotBlank(tenantUser.getTenantId())) {
                        SysTenantInfo tenantInfo = sysTenantInfoService.getById(tenantUser.getTenantId());
                        if (tenantInfo != null) {
                            task.setTenantName(tenantInfo.getTenantName());
                        }
                    }
                } else {
                    task.setUserId("unknown");
                    task.setTenantUserId(l.getOperateUserId());
                    task.setTenantId("unknown");
                    task.setTaskLinkType("unknown");
                }
            } else if (l.getSignerType() == 2) {
                task.setTenantName("个人租户");
                if (l.getOperateExternalType() == 1) {
                    task.setPhone(l.getOperateExternalValue());
                    SysUser sysUser = sysUserService.getUserByPhone(l.getOperateExternalValue());
                    if (sysUser != null) {
                        task.setEmail(sysUser.getEmail());
                        task.setUserId(sysUser.getId());
                        SysTenantUser tenantUser = sysTenantUserService.getPersonalTenantUser(sysUser.getId());
                        if (tenantUser != null) {
                            task.setTenantUserId(tenantUser.getId());
                            task.setTenantId(tenantUser.getTenantId());
                            task.setTaskLinkType("system");
                            task.setSendMsgFlag(true);
                        }
                    }
                } else if (l.getOperateExternalType() == 2) {
                    task.setEmail(l.getOperateExternalValue());
                    SysUser sysUser = sysUserService.getUserByEmail(l.getOperateExternalValue());
                    if (sysUser != null) {
                        task.setPhone(sysUser.getPhone());
                        task.setUserId(sysUser.getId());
                        SysTenantUser tenantUser = sysTenantUserService.getPersonalTenantUser(sysUser.getId());
                        if (tenantUser != null) {
                            task.setTenantUserId(tenantUser.getId());
                            task.setTenantId(tenantUser.getTenantId());
                            task.setTaskLinkType("system");
                            task.setSendMsgFlag(true);
                        }
                    }
                } else {
                    task.setPhone("unknown");
                    task.setEmail("unknown");
                    task.setTaskLinkType("unknown");
                }
            } else if (l.getSignerType() == 3) {
                task.setTenantName(l.getTenantName());
                SysTenantInfo tenantInfo = sysTenantInfoService.getByName(l.getTenantName());
                task.setTenantId(tenantInfo != null ? tenantInfo.getId() : null);
                if (l.getOperateExternalType() == 1) {
                    task.setPhone(l.getOperateExternalValue());
                    SysUser sysUser = sysUserService.getUserByPhone(l.getOperateExternalValue());
                    if (sysUser != null) {
                        task.setEmail(sysUser.getEmail());
                        task.setUserId(sysUser.getId());
                        if (tenantInfo != null) {
                            SysTenantUser tenantUser = sysTenantUserService.getActiviTenantUser(tenantInfo.getId(), sysUser.getId());
                            if (tenantUser != null) {
                                task.setTenantUserId(tenantUser.getId());
                                task.setTaskLinkType("system");
                                task.setSendMsgFlag(true);
                            }
                        }
                    }
                } else if (l.getOperateExternalType() == 2) {
                    task.setEmail(l.getOperateExternalValue());
                    SysUser sysUser = sysUserService.getUserByEmail(l.getOperateExternalValue());
                    if (sysUser != null) {
                        sysUser.setPhone(sysUser.getPhone());
                        task.setUserId(sysUser.getId());
                        if (tenantInfo != null) {
                            SysTenantUser tenantUser = sysTenantUserService.getActiviTenantUser(tenantInfo.getId(), sysUser.getId());
                            if (tenantUser != null) {
                                task.setTenantUserId(tenantUser.getId());
                                task.setTaskLinkType("system");
                                task.setSendMsgFlag(true);
                            }
                        }
                    }
                } else {
                    task.setPhone("unknown");
                    task.setEmail("unknown");
                    task.setTaskLinkType("unknown");
                }
            }
            if (type == 1) {
                task.setCompleteUserId(task.getUserId());
                task.setCompleteTenantId(task.getTenantId());
                task.setCompleteTenantUserId(task.getTenantUserId());
                task.setTaskStatus(2);
                task.setCheckMenuType("approve");
                task.setCheckTime(new Date());
            } else {
                task.setTaskStatus(1);
            }
            tasks.add(task);
            //修改实例-用户操作表任务状态
            if (type == 1) {
                l.setOperateStatus(3);
            } else {
                l.setOperateStatus(2);
            }
            l.setOperateTime(new Date());
            signRuOperatorService.updateById(l);
        });
        signRuTaskService.saveBatch(tasks);

        //外部签署人发送填写短信通知
        //SysTenantInfo sysTenantInfo = sysTenantInfoService.getById(signRu.getSysTenantId());
        tasks.forEach(t -> {
            if (t.getUserType() == 2 || t.getUserType() == 3) {
                Map<String, String> para1 = new HashMap<>();
                Map<String, String> para2 = new HashMap<>();
                //para.put("company", sysTenantInfo != null ? sysTenantInfo.getTenantName() : "未知公司");
                para1.put("sender", sender);
                para1.put("contract", signRu.getSubject());
                para2.put("sender", sender);
                para2.put("contract", signRu.getSubject());
                String code1 = RandomUtil.randomString(CommonConstant.BASE_CHECK_CODES, 8);
                //防止code重复
                while (messageShortMappingService.getByCode(code1) != null) {
                    code1 = RandomUtil.randomString(CommonConstant.BASE_CHECK_CODES, 8);
                }
                para1.put("code", code1);
                String code2 = RandomUtil.randomString(CommonConstant.BASE_CHECK_CODES, 8);
                //防止code重复
                while (messageShortMappingService.getByCode(code2) != null) {
                    code2 = RandomUtil.randomString(CommonConstant.BASE_CHECK_CODES, 8);
                }
                para2.put("code", code2);

                //生成短连接
                Map<String, String> mappingPara1 = new HashMap<>();
                Map<String, String> mappingPara2 = new HashMap<>();
                mappingPara1.put("signRuId", signRuId);
                mappingPara1.put("taskId", t.getId());
                mappingPara1.put("taskType", "write");
                mappingPara2.put("signRuId", signRuId);
                mappingPara2.put("taskId", t.getId());
                mappingPara2.put("taskType", "write");
                MessageShortMapping mapping1 = new MessageShortMapping();
                MessageShortMapping mapping2 = new MessageShortMapping();
                mapping1.setMegCode(code1);
                mapping2.setMegCode(code2);
                MessageDto messageDto = new MessageDto();
                EmailDto emailDto = new EmailDto();
                messageDto.setSendType(SendType.IMMEDIATELY);
                emailDto.setSendType(SendType.IMMEDIATELY);
                List<String> receivers1 = new ArrayList<>();
                List<String> receivers2 = new ArrayList<>();
                if (MyStringUtils.isNotBlank(t.getPhone())) {
                    mappingPara1.put("phone", t.getPhone());

                    SysConfig isEcup = commonToolsAPI.getConfigByType("sms_channel");
                    SysConfig sendMessageConfig = commonToolsAPI.getConfigByType(SysConfigType.SENDMESSAGE.getType());
                    if(isEcup != null && "resrun".equals(isEcup.getValue()) && sendMessageConfig != null && "true".equals(sendMessageConfig.getValue())){
                        MsgRequest msgRequest = new MsgRequest();
                        msgRequest.setPhoneNumbers(t.getPhone());
                        msgRequest.setTemplateName("writeTask");
                        Map<String, String> para = new HashMap<>();
                        // todo 获取域名,补充信息
                        para.put("sender", sender);
                        para.put("contract", signRu.getSubject());
                        SysAppInfo sysAppInfo = sysAppInfoService.getById("490489ab-d8b4-414c-ad77-d856962c286f");
                        if (sysAppInfo != null && sysAppInfo.getAppAddress() != null) {
                            para.put("domain", sysAppInfo.getAppAddress());
                        }
                        para.put("code", code1);
                        msgRequest.setParams(para);
                        if (signReNoticeService.getByReIdAndType(signRu.getSignReId(), ReNoticeTypeEnum.WRITE_TASK_PHONE.getType())) {
                            smsSendService.sendMsg(msgRequest);
                            mapping1.setMsgPara(JacksonUtil.toJson(mappingPara1));
                            messageShortMappingService.save(mapping1);
                        }
                    }else{
                        receivers1.add(t.getPhone());
                        messageDto.setReceivers(receivers1);
                        messageDto.setTemplateCode("writeTask");
                        messageDto.setContentParaMap(para1);
                        if (signReNoticeService.getByReIdAndType(signRu.getSignReId(), ReNoticeTypeEnum.WRITE_TASK_PHONE.getType())) {
                            sysMessageUtil.asyncSendMessage(messageDto);
                            mapping1.setMsgPara(JacksonUtil.toJson(mappingPara1));
                            messageShortMappingService.save(mapping1);
                        }
                    }

                }
                if (MyStringUtils.isNotBlank(t.getEmail())) {
                    mappingPara2.put("email", t.getEmail());
                    receivers2.add(t.getEmail());
                    emailDto.setReceivers(receivers2);
                    emailDto.setTemplateCode("email_writeTask");
                    SysAppInfo sysAppInfo = sysAppInfoService.getById("490489ab-d8b4-414c-ad77-d856962c286f");
                    if (sysAppInfo != null && sysAppInfo.getAppAddress() != null) {
                        para2.put("domain", sysAppInfo.getAppAddress());
                    }
                    emailDto.setContentParaMap(para2);
                    if (signReNoticeService.getByReIdAndType(signRu.getSignReId(), ReNoticeTypeEnum.WRITE_TASK_EMAIL.getType())) {
                        sysMessageUtil.asyncSendEmail(emailDto);
                        mapping2.setMsgPara(JacksonUtil.toJson(mappingPara2));
                        messageShortMappingService.save(mapping2);
                    }
                }
                if (t.isSendMsgFlag()) {
                    sendFillInMail(t.getTenantUserId(), sender, signRu.getSubject(), signRu.getSignReId(), signRu.getId(), t.getId());
                }
            }
        });
    }

    void sendFillInMail(String tenantUserId, String sender, String contract, String signReId, String signRuId, String taskId) {
        MailDto mailDto = new MailDto();
        mailDto.setSendType(SendType.IMMEDIATELY);

        Map<MesAuthType, List<String>> userMap = new HashMap<>();
        List<String> userIds = Arrays.asList(tenantUserId);
        userMap.put(MesAuthType.USER, userIds);
        mailDto.setReceivers(userMap);

        mailDto.setTemplateCode("opensign_contract_wrirte");

        Map<String, String> titleParaMap = new HashMap<>();
        titleParaMap.put("sender", sender);
        titleParaMap.put("contract", contract);
        mailDto.setTitleParaMap(titleParaMap);

        Map<String, String> contentParaMap = new HashMap<>();
        contentParaMap.put("sender", sender);
        contentParaMap.put("contract", contract);
        mailDto.setContentParaMap(contentParaMap);

        Map<String, Map<String, String>> buttonParaMap = new HashMap<>();
        Map<String, String> para = new HashMap<>();
        para.put("__full__", "");
        para.put("from", "list");
        para.put("isDetail", "false");
        para.put("type", "receive");
        para.put("signReId", signReId);
        para.put("signRuId", signRuId);
        para.put("taskId", taskId);
        buttonParaMap.put("contract_wrirte", para);
        mailDto.setButtonParaMap(buttonParaMap);

        //发送通知
        sysMessageUtil.asyncSendMail(mailDto);
    }

    @Override
    public void addStartFlowTask(String signRuId) {
        SignRuTask task = new SignRuTask();
        task.setSignRuId(signRuId);
        task.setTaskType(TaskTypeEnum.START_FLOW.getCode());
        task.setUserType(1);
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        task.setUserId(loginUser.getId());
        task.setTenantUserId(loginUser.getTenantUserId());
        task.setTenantId(loginUser.getTenantId());
        task.setTaskLinkType("system");

        task.setCompleteUserId(loginUser.getId());
        task.setCompleteTenantId(loginUser.getTenantId());
        task.setCompleteTenantUserId(loginUser.getTenantUserId());

        task.setCheckTime(new Date());
        task.setCheckMenuType("approve");

        task.setTaskStatus(2);

        signRuTaskService.save(task);
    }

    @Override
    public void computePersonalOutSignTask(List<SignRuSigner> signers, String signRuId) {
        List<SignRuTask> tasks = new ArrayList<>();
        signers.forEach(s -> {
            //初始化签署节点任务
            SignRuTask task = new SignRuTask();
            task.setSignRuId(signRuId);
            task.setTaskType(TaskTypeEnum.SIGN_TASK.getCode());
            task.setUserType(2);
            task.setUserTaskId(s.getId());
            task.setTenantName("个人租户");
            if (s.getSignerExternalType() == 1) {
                task.setPhone(s.getSignerExternalValue());
                SysUser sysUser = sysUserService.getUserByPhone(s.getSignerExternalValue());
                if (sysUser != null) {
                    task.setUserId(sysUser.getId());
                    task.setEmail(sysUser.getEmail());
                    SysTenantUser tenantUser = sysTenantUserService.getPersonalTenantUser(sysUser.getId());
                    if (tenantUser != null) {
                        task.setTenantUserId(tenantUser.getId());
                        task.setTenantId(tenantUser.getTenantId());
                        task.setTaskLinkType("system");
                        task.setSendMsgFlag(true);
                    }
                }
            } else if (s.getSignerExternalType() == 2) {
                task.setEmail(s.getSignerExternalValue());
                SysUser sysUser = sysUserService.getUserByEmail(s.getSignerExternalValue());
                if (sysUser != null) {
                    task.setPhone(sysUser.getPhone());
                    task.setUserId(sysUser.getId());
                    SysTenantUser tenantUser = sysTenantUserService.getPersonalTenantUser(sysUser.getId());
                    if (tenantUser != null) {
                        task.setTenantUserId(tenantUser.getId());
                        task.setTenantId(tenantUser.getTenantId());
                        task.setTaskLinkType("system");
                        task.setSendMsgFlag(true);
                    }
                }
            } else {
                task.setPhone("unknown");
                task.setEmail("unknown");
                task.setTaskLinkType("unknown");
            }
            task.setTaskStatus(1);
            tasks.add(task);
            //修改实例-用户操作表任务状态
            SignRuOperator query = new SignRuOperator();
            query.setSignRuId(signRuId);
            query.setOperateType(2);
            query.setSignerType(2);
            query.setSignerId(s.getId());
            List<SignRuOperator> operators = signRuOperatorService.getByEntity(query);
            if (CollUtil.isNotEmpty(operators)) {
                SignRuOperator ruOperator = operators.get(0);
                ruOperator.setOperateStatus(2);
                ruOperator.setOperateTime(new Date());
                signRuOperatorService.updateById(ruOperator);
            }
        });
        signRuTaskService.saveBatch(tasks);
        //外部签署人发送签署短信通知
        SignRu signRu = signRuService.getById(signRuId);
        if (signRu != null) {
            //SysTenantInfo sysTenantInfo = sysTenantInfoService.getById(signRu.getSysTenantId());
            SysTenantUser sysTenantUser = sysTenantUserService.getById(signRu.getSysUserId());
            tasks.forEach(t -> {
                Map<String, String> para1 = new HashMap<>();
                Map<String, String> para2 = new HashMap<>();
                //para.put("company", sysTenantInfo != null ? sysTenantInfo.getTenantName() : "未知公司");
                para1.put("sender", sysTenantUser != null ? sysTenantUser.getNickName() : "未知用户");
                para1.put("contract", signRu.getSubject());
                para2.put("sender", sysTenantUser != null ? sysTenantUser.getNickName() : "未知用户");
                para2.put("contract", signRu.getSubject());
                String code1 = RandomUtil.randomString(CommonConstant.BASE_CHECK_CODES, 8);
                //防止code重复
                while (messageShortMappingService.getByCode(code1) != null) {
                    code1 = RandomUtil.randomString(CommonConstant.BASE_CHECK_CODES, 8);
                }
                para1.put("code", code1);
                String code2 = RandomUtil.randomString(CommonConstant.BASE_CHECK_CODES, 8);
                //防止code重复
                while (messageShortMappingService.getByCode(code2) != null) {
                    code2 = RandomUtil.randomString(CommonConstant.BASE_CHECK_CODES, 8);
                }
                para2.put("code", code2);

                //生成短连接
                Map<String, String> mappingPara1 = new HashMap<>();
                Map<String, String> mappingPara2 = new HashMap<>();
                mappingPara1.put("signRuId", signRuId);
                mappingPara1.put("taskId", t.getId());
                mappingPara1.put("taskType", "sign");
                mappingPara2.put("signRuId", signRuId);
                mappingPara2.put("taskId", t.getId());
                mappingPara2.put("taskType", "sign");
                MessageShortMapping mapping1 = new MessageShortMapping();
                MessageShortMapping mapping2 = new MessageShortMapping();
                mapping1.setMegCode(code1);
                mapping2.setMegCode(code2);
                MessageDto messageDto = new MessageDto();
                EmailDto emailDto = new EmailDto();
                messageDto.setSendType(SendType.IMMEDIATELY);
                emailDto.setSendType(SendType.IMMEDIATELY);
                List<String> receivers1 = new ArrayList<>();
                List<String> receivers2 = new ArrayList<>();
                if (MyStringUtils.isNotBlank(t.getPhone())) {
                    mappingPara1.put("phone", t.getPhone());
                    SysConfig isEcup = commonToolsAPI.getConfigByType("sms_channel");
                    SysConfig sendMessageConfig = commonToolsAPI.getConfigByType(SysConfigType.SENDMESSAGE.getType());
                    if(isEcup != null && "resrun".equals(isEcup.getValue()) && sendMessageConfig != null && "true".equals(sendMessageConfig.getValue())){
                        MsgRequest msgRequest = new MsgRequest();
                        msgRequest.setPhoneNumbers(t.getPhone());
                        msgRequest.setTemplateName("signTaskOut");
                        Map<String, String> para = new HashMap<>();
                        // todo 获取域名,补充信息
                        para.put("sender", sysTenantUser != null ? sysTenantUser.getNickName() : "未知用户");
                        para.put("contract", signRu.getSubject());
                        SysAppInfo sysAppInfo = sysAppInfoService.getById("490489ab-d8b4-414c-ad77-d856962c286f");
                        if (sysAppInfo != null && sysAppInfo.getAppAddress() != null) {
                            para.put("domain", sysAppInfo.getAppAddress());
                        }
                        para.put("code", code1);
                        msgRequest.setParams(para);
                        if (signReNoticeService.getByReIdAndType(signRu.getSignReId(), ReNoticeTypeEnum.SIGN_TASK_OUT_PHONE.getType())) {
                            smsSendService.sendMsg(msgRequest);
                            mapping1.setMsgPara(JacksonUtil.toJson(mappingPara1));
                            messageShortMappingService.save(mapping1);
                        }
                    }else{
                        receivers1.add(t.getPhone());
                        messageDto.setReceivers(receivers1);
                        messageDto.setTemplateCode("signTaskOut");
                        messageDto.setContentParaMap(para1);
                        if (signReNoticeService.getByReIdAndType(signRu.getSignReId(), ReNoticeTypeEnum.SIGN_TASK_OUT_PHONE.getType())) {
                            sysMessageUtil.asyncSendMessage(messageDto);
                            mapping1.setMsgPara(JacksonUtil.toJson(mappingPara1));
                            messageShortMappingService.save(mapping1);
                        }
                    }
                }
                if (MyStringUtils.isNotBlank(t.getEmail())) {
                    mappingPara2.put("email", t.getEmail());
                    receivers2.add(t.getEmail());
                    emailDto.setReceivers(receivers2);
                    emailDto.setTemplateCode("email_signTaskOut");
                    SysAppInfo sysAppInfo = sysAppInfoService.getById("490489ab-d8b4-414c-ad77-d856962c286f");
                    if (sysAppInfo != null && sysAppInfo.getAppAddress() != null) {
                        para2.put("domain", sysAppInfo.getAppAddress());
                    }
                    emailDto.setContentParaMap(para2);
                    if (signReNoticeService.getByReIdAndType(signRu.getSignReId(), ReNoticeTypeEnum.SIGN_TASK_OUT_EMAIL.getType())) {
                        sysMessageUtil.asyncSendEmail(emailDto);
                        mapping2.setMsgPara(JacksonUtil.toJson(mappingPara2));
                        messageShortMappingService.save(mapping2);
                    }
                }
                if (t.isSendMsgFlag()) {
                    sendSignMail(t.getTenantUserId(), signRuId, t.getId());
                }
            });
        }
    }

    @Autowired
    private ISysAppInfoService sysAppInfoService;

    void addCompanyOutRuTask(String signRuId, String signRuSenderId, String signerName, Integer senderExternalType, String senderExternalValue, Integer taskStatus) {
        //初始化签署节点任务
        SignRuTask task = new SignRuTask();
        task.setSignRuId(signRuId);
        task.setTaskType(TaskTypeEnum.SIGN_TASK.getCode());
        task.setUserType(3);
        task.setUserTaskId(signRuSenderId);
        task.setTenantName(signerName);
        String phone = null;
        String email = null;
        if (senderExternalType != null) {
            SysTenantInfo tenantInfo = sysTenantInfoService.getByName(signerName);
            task.setTenantId(tenantInfo != null ? tenantInfo.getId() : null);
            if (senderExternalType == 1) {
                phone = senderExternalValue;
                task.setPhone(senderExternalValue);
                SysUser sysUser = sysUserService.getUserByPhone(senderExternalValue);
                if (sysUser != null) {
                    email = sysUser.getEmail();
                    task.setEmail(sysUser.getEmail());
                    task.setUserId(sysUser.getId());
                    if (tenantInfo != null) {
                        SysTenantUser tenantUser = sysTenantUserService.getActiviTenantUser(tenantInfo.getId(), sysUser.getId());
                        if (tenantUser != null) {
                            task.setTenantUserId(tenantUser.getId());
                            task.setTaskLinkType("system");
                            task.setSendMsgFlag(true);
                        }
                    }
                }
            } else if (senderExternalType == 2) {
                task.setEmail(senderExternalValue);
                email = senderExternalValue;
                SysUser sysUser = sysUserService.getUserByEmail(senderExternalValue);
                if (sysUser != null) {
                    phone = sysUser.getPhone();
                    task.setPhone(sysUser.getPhone());
                    task.setUserId(sysUser.getId());
                    if (tenantInfo != null) {
                        SysTenantUser tenantUser = sysTenantUserService.getActiviTenantUser(tenantInfo.getId(), sysUser.getId());
                        if (tenantUser != null) {
                            task.setTenantUserId(tenantUser.getId());
                            task.setTaskLinkType("system");
                            task.setSendMsgFlag(true);
                        }
                    }
                }
            } else {
                task.setPhone("unknown");
                task.setEmail("unknown");
                task.setTaskLinkType("unknown");
            }
        } else {
            task.setPhone("system");
            task.setEmail("system");
            task.setTaskLinkType("system");
        }
        task.setTaskStatus(taskStatus);

        if (taskStatus == 2) {
            task.setCompleteUserId("system");
            task.setCompleteTenantId("system");
            task.setCompleteTenantUserId("system");
            task.setCheckTime(new Date());
            task.setCheckMenuType("approve");
        }
        signRuTaskService.save(task);

        //外部企业签署人非自动签署任务发送签署短信通知
        if (taskStatus == 1) {
            SignRu signRu = signRuService.getById(signRuId);
            if (signRu != null) {
                SysTenantUser sysTenantUser1 = sysTenantUserService.getById(signRu.getSysUserId());
                if (sysTenantUser1 != null) {
                    Map<String, String> para1 = new HashMap<>();
                    Map<String, String> para2 = new HashMap<>();
                    para1.put("sender", sysTenantUser1.getNickName());
                    para1.put("contract", signRu.getSubject());
                    para2.put("sender", sysTenantUser1.getNickName());
                    para2.put("contract", signRu.getSubject());
                    String code1 = RandomUtil.randomString(CommonConstant.BASE_CHECK_CODES, 8);
                    //防止code重复
                    while (messageShortMappingService.getByCode(code1) != null) {
                        code1 = RandomUtil.randomString(CommonConstant.BASE_CHECK_CODES, 8);
                    }
                    para1.put("code", code1);
                    String code2 = RandomUtil.randomString(CommonConstant.BASE_CHECK_CODES, 8);
                    //防止code重复
                    while (messageShortMappingService.getByCode(code2) != null) {
                        code2 = RandomUtil.randomString(CommonConstant.BASE_CHECK_CODES, 8);
                    }
                    para2.put("code", code2);
                    //生成短连接
                    Map<String, String> mappingPara1 = new HashMap<>();
                    Map<String, String> mappingPara2 = new HashMap<>();
                    mappingPara1.put("signRuId", signRuId);
                    mappingPara1.put("taskId", task.getId());
                    mappingPara1.put("taskType", "sign");
                    mappingPara2.put("signRuId", signRuId);
                    mappingPara2.put("taskId", task.getId());
                    mappingPara2.put("taskType", "sign");
                    MessageShortMapping mapping1 = new MessageShortMapping();
                    MessageShortMapping mapping2 = new MessageShortMapping();
                    mapping1.setMegCode(code1);
                    mapping2.setMegCode(code2);

                    MessageDto messageDto = new MessageDto();
                    EmailDto emailDto = new EmailDto();
                    messageDto.setSendType(SendType.IMMEDIATELY);
                    emailDto.setSendType(SendType.IMMEDIATELY);
                    List<String> receivers1 = new ArrayList<>();
                    List<String> receivers2 = new ArrayList<>();
                    if (MyStringUtils.isNotBlank(phone)) {
                        mappingPara1.put("phone", phone);

                        SysConfig isEcup = commonToolsAPI.getConfigByType("sms_channel");
                        SysConfig sendMessageConfig = commonToolsAPI.getConfigByType(SysConfigType.SENDMESSAGE.getType());
                        if(isEcup != null && "resrun".equals(isEcup.getValue()) && sendMessageConfig != null && "true".equals(sendMessageConfig.getValue())){
                            MsgRequest msgRequest = new MsgRequest();
                            msgRequest.setPhoneNumbers(phone);
                            msgRequest.setTemplateName("signTaskOut");
                            Map<String, String> para = new HashMap<>();
                            // todo 获取域名,补充信息
                            para.put("sender", sysTenantUser1.getNickName());
                            para.put("contract", signRu.getSubject());
                            SysAppInfo sysAppInfo = sysAppInfoService.getById("490489ab-d8b4-414c-ad77-d856962c286f");
                            if (sysAppInfo != null && sysAppInfo.getAppAddress() != null) {
                                para.put("domain", sysAppInfo.getAppAddress());
                            }
                            para.put("code", code1);
                            msgRequest.setParams(para);
                            if (signReNoticeService.getByReIdAndType(signRu.getSignReId(), ReNoticeTypeEnum.SIGN_TASK_OUT_PHONE.getType())) {
                                smsSendService.sendMsg(msgRequest);
                                mapping1.setMsgPara(JacksonUtil.toJson(mappingPara1));
                                messageShortMappingService.save(mapping1);
                            }
                        }else{
                            receivers1.add(phone);
                            messageDto.setReceivers(receivers1);
//                        messageDto.setTemplateCode("signTaskIn");
                            messageDto.setTemplateCode("signTaskOut");
                            messageDto.setContentParaMap(para1);
//                        if (signReNoticeService.getByReIdAndType(signRu.getSignReId(), ReNoticeTypeEnum.SIGN_TASK_IN_PHONE.getType())) {
                            if (signReNoticeService.getByReIdAndType(signRu.getSignReId(), ReNoticeTypeEnum.SIGN_TASK_OUT_PHONE.getType())) {
                                sysMessageUtil.asyncSendMessage(messageDto);
                                mapping1.setMsgPara(JacksonUtil.toJson(mappingPara1));
                                messageShortMappingService.save(mapping1);
                            }
                        }
                    }
                    if (MyStringUtils.isNotBlank(email)) {
                        mappingPara2.put("email", email);
                        receivers2.add(email);
                        emailDto.setReceivers(receivers2);
//                        emailDto.setTemplateCode("email_signTaskIn");
                        emailDto.setTemplateCode("email_signTaskOut");
                        SysAppInfo sysAppInfo = sysAppInfoService.getById("490489ab-d8b4-414c-ad77-d856962c286f");
                        if (sysAppInfo != null && sysAppInfo.getAppAddress() != null) {
                            para2.put("domain", sysAppInfo.getAppAddress());
                        }
                        emailDto.setContentParaMap(para2);
//                        if (signReNoticeService.getByReIdAndType(signRu.getSignReId(), ReNoticeTypeEnum.SIGN_TASK_IN_EMAIL.getType())) {
                        if (signReNoticeService.getByReIdAndType(signRu.getSignReId(), ReNoticeTypeEnum.SIGN_TASK_OUT_EMAIL.getType())) {
                            sysMessageUtil.asyncSendEmail(emailDto);
                            mapping2.setMsgPara(JacksonUtil.toJson(mappingPara2));
                            messageShortMappingService.save(mapping2);
                        }
                    }
                    if (MyStringUtils.isNotBlank(task.getTenantUserId())) {
                        MailDto mailDto = new MailDto();
                        mailDto.setSendType(SendType.IMMEDIATELY);

                        Map<MesAuthType, List<String>> userMap = new HashMap<>();
                        List<String> userIds = Arrays.asList(task.getTenantUserId());
                        userMap.put(MesAuthType.USER, userIds);
                        mailDto.setReceivers(userMap);

                        mailDto.setTemplateCode("opensign_sign");

                        Map<String, String> titleParaMap = new HashMap<>();
                        titleParaMap.put("contract", signRu.getSubject());
                        titleParaMap.put("sender", sysTenantUser1.getNickName());
                        mailDto.setTitleParaMap(titleParaMap);

                        Map<String, String> contentParaMap = new HashMap<>();
                        contentParaMap.put("contract", signRu.getSubject());
                        contentParaMap.put("sender", sysTenantUser1.getNickName());
                        mailDto.setContentParaMap(contentParaMap);

                        Map<String, Map<String, String>> buttonParaMap = new HashMap<>();
                        Map<String, String> para3 = new HashMap<>();
                        para3.put("__full__", "");
                        para3.put("isDetail", "false");
                        para3.put("signReId", signRu.getSignReId());
                        para3.put("signRuId", signRu.getId());
                        para3.put("type", "");
                        para3.put("from", "list");
                        para3.put("taskId", task.getId());

                        buttonParaMap.put("contract_sign", para3);
                        mailDto.setButtonParaMap(buttonParaMap);

                        //发送通知
                        sysMessageUtil.asyncSendMail(mailDto);
                    }
                }
            }
        }
    }

    @Override
    public void computeCompanyOutSignTask(List<SignRuSigner> signers, String taskId, String signRuId)  {
        //计算并创建下一个签署任务
        String ruSenderId = null;
        if (CollUtil.isEmpty(signers)) {
            if (MyStringUtils.isNotBlank(taskId)) {
                signers = new ArrayList<>();
                SignRuTask signRuTask = signRuTaskService.getById(taskId);
                if (signRuTask != null) {
                    ruSenderId = signRuTask.getUserTaskId();
                    SignRuSender signRuSender = signRuSenderService.getById(signRuTask.getUserTaskId());
                    if (signRuSender != null) {
                        SignRuSigner signRuSigner = signRuSignerService.getById(signRuSender.getSignerId());
                        if (signRuSigner != null) {
                            signers.add(signRuSigner);
                        }
                    }
                }
            }
        }
        boolean jump = true;
        if (MyStringUtils.isNotBlank(ruSenderId)) {
            jump = false;
        }
        if (CollUtil.isNotEmpty(signers)) {
            for (SignRuSigner s : signers) {
                SignRuSender senderQuery = new SignRuSender();
                senderQuery.setSignerId(s.getId());
                List<SignRuSender> senders = signRuSenderService.getByEntity(senderQuery);
                if (CollUtil.isNotEmpty(senders)) {
                    for (int i = 0; i < senders.size(); i++) {
                        SignRuSender sender = senders.get(i);
                        if (jump || ruSenderId.equals(sender.getId())) {
                            jump = true;
                            if (ruSenderId != null && ruSenderId.equals(sender.getId())) {
                                continue;
                            }
                            if (sender.getSenderSignType() == 1) {
                                //创建签署任务
                                addCompanyOutRuTask(signRuId, sender.getId(), s.getSignerName(), null, null, 2);
                                //调用自动签署接口
                                ruSignFlowService.autoSign(sender.getId(), 1);
                                //修改实例-操作表状态
                                updateRuOpetare(signRuId, sender.getId(), 3, 3);
                            } else if (sender.getSenderSignType() == 2) {
                                //创建签署任务
                                addCompanyOutRuTask(signRuId, sender.getId(), s.getSignerName(), sender.getSenderExternalType(), sender.getSenderExternalValue(), 1);
                                //修改实例-操作表状态
                                updateRuOpetare(signRuId, sender.getId(), 3, 2);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public TaskCmdInfo computeCompanyOutAndNextSignTask(String taskId, String signRuId) {
        computeCompanyOutSignTask(null, taskId, signRuId);
        return computentNextSignTask(taskId, signRuId);
    }

    TaskCmdInfo computentNextSignTask(String taskId, String signRuId)  {
        String ruSignerId = null;
        if (MyStringUtils.isNotBlank(taskId)) {
            SignRuTask signRuTask = signRuTaskService.getById(taskId);
            if (signRuTask != null) {
                SignRuSender signRuSender = signRuSenderService.getById(signRuTask.getUserTaskId());
                if (signRuSender != null) {
                    ruSignerId = signRuSender.getSignerId();
                }
            }
        }
        //计算当前是否有签署任务
        if (MyStringUtils.isNotBlank(ruSignerId)) {
            SignRuTask tastQuery = new SignRuTask();
            tastQuery.setSignRuId(signRuId);
            tastQuery.setTaskStatus(1);
            List<SignRuTask> tasks = signRuTaskService.getByEntity(tastQuery);
            if (CollUtil.isEmpty(tasks)) {
                //没有的话创建接下来的外部任务
                SignRuSigner signerQuery = new SignRuSigner();
                signerQuery.setSignRuId(signRuId);
                List<SignRuSigner> signers = signRuSignerService.getByEntity(signerQuery);
                if (CollUtil.isNotEmpty(signers)) {
                    int i = 0;
                    while (!signers.get(i).getId().equals(ruSignerId) && i < signers.size()) {
                        i++;
                    }
                    if (i < signers.size() - 1) {
                        SignRuSigner ruSigner = signers.get(i + 1);
                        if (ruSigner.getSignerType() == 1) {
                            //发起方:执行命令
                            TaskCmdInfo result = new TaskCmdInfo();
                            result.setTaskType(TaskTypeEnum.B_SIGN_TASK.getCode());
                            //todo 如果修改驱动逻辑需要修改该步骤
                            result.setOperate("signActivitiFlow");
                            return result;
                        } else if (ruSigner.getSignerType() == 2) {
                            //接收方
                            List<SignRuSigner> newList = new ArrayList<>();
                            newList.add(ruSigner);
                            computePersonalOutSignTask(newList, signRuId);
                        } else if (ruSigner.getSignerType() == 3) {
                            List<SignRuSigner> newList = new ArrayList<>();
                            newList.add(ruSigner);
                            computeCompanyOutSignTask(newList, null, signRuId);
                        }
                    }
                }
            }
        } else {
            SignRuTask tastQuery = new SignRuTask();
            tastQuery.setSignRuId(signRuId);
            tastQuery.setTaskStatus(1);
            List<SignRuTask> tasks = signRuTaskService.getByEntity(tastQuery);
            if (CollUtil.isEmpty(tasks)) {
                //没有的话创建接下来的外部任务
                SignRuSigner signerQuery = new SignRuSigner();
                signerQuery.setSignRuId(signRuId);
                List<SignRuSigner> signers = signRuSignerService.getByEntity(signerQuery);
                if (CollUtil.isNotEmpty(signers)) {
                    SignRuSigner ruSigner = signers.get(0);
                    for (SignRuSigner s : signers) {
                        if (s.getSignerType() != 1) {
                            ruSigner = s;
                            break;
                        }
                    }
                    if (ruSigner.getSignerType() == 1) {
                        //发起方:执行命令
                        TaskCmdInfo result = new TaskCmdInfo();
                        result.setTaskType(TaskTypeEnum.B_SIGN_TASK.getCode());
                        //todo 如果修改驱动逻辑需要修改该步骤
                        result.setOperate("signActivitiFlow");
                        return result;
                    } else if (ruSigner.getSignerType() == 2) {
                        //接收方
                        List<SignRuSigner> newList = new ArrayList<>();
                        newList.add(ruSigner);
                        computePersonalOutSignTask(newList, signRuId);
                    } else if (ruSigner.getSignerType() == 3) {
                        List<SignRuSigner> newList = new ArrayList<>();
                        newList.add(ruSigner);
                        computeCompanyOutSignTask(newList, null, signRuId);
                    }
                }
            }
        }
        return null;
    }


    void sendSignMail(String tenantUserId, String signRuId, String taskId) {
        SignRu signRu = signRuService.getById(signRuId);
        if (signRu == null) {
            return;
        }
        SysTenantUser sysTenantUser = sysTenantUserService.getById(signRu.getSysUserId());
        MailDto mailDto = new MailDto();
        mailDto.setSendType(SendType.IMMEDIATELY);

        Map<MesAuthType, List<String>> userMap = new HashMap<>();
        List<String> userIds = Arrays.asList(tenantUserId);
        userMap.put(MesAuthType.USER, userIds);
        mailDto.setReceivers(userMap);

        mailDto.setTemplateCode("opensign_sign");

        Map<String, String> titleParaMap = new HashMap<>();
        titleParaMap.put("contract", signRu.getSubject());
        titleParaMap.put("sender", sysTenantUser != null ? sysTenantUser.getNickName() : "未知用户");
        mailDto.setTitleParaMap(titleParaMap);

        Map<String, String> contentParaMap = new HashMap<>();
        contentParaMap.put("contract", signRu.getSubject());
        contentParaMap.put("sender", sysTenantUser != null ? sysTenantUser.getNickName() : "未知用户");
        mailDto.setContentParaMap(contentParaMap);

        Map<String, Map<String, String>> buttonParaMap = new HashMap<>();
        Map<String, String> para1 = new HashMap<>();
        para1.put("__full__", "");
        para1.put("isDetail", "false");
        para1.put("signReId", signRu.getSignReId());
        para1.put("signRuId", signRuId);
        para1.put("type", "");
        para1.put("from", "list");
        para1.put("taskId", taskId);

        buttonParaMap.put("contract_sign", para1);
        mailDto.setButtonParaMap(buttonParaMap);

        //发送通知
        sysMessageUtil.asyncSendMail(mailDto);

    }

    @Override
    public TaskCmdInfo computeInAndNextSignTask(String taskId, String signRuId) {
        //计算并创建下一个签署任务
        SignRuSigner query = new SignRuSigner();
        query.setSignRuId(signRuId);
        query.setSignerType(1);
        List<SignRuSigner> signers = signRuSignerService.getByEntity(query);
        if (CollUtil.isNotEmpty(signers)) {
            SignRuSender senderQuery = new SignRuSender();
            senderQuery.setSignerId(signers.get(0).getId());
            List<SignRuSender> senders = signRuSenderService.getByEntity(senderQuery);
            if (CollUtil.isNotEmpty(senders)) {
                String ruSenderId = null;
                if (MyStringUtils.isNotBlank(taskId)) {
                    SignRuTask currentTask = signRuTaskService.getById(taskId);
                    if (currentTask != null) {
                        ruSenderId = currentTask.getUserTaskId();
                    }
                }
                boolean jump = true;
                if (MyStringUtils.isNotBlank(ruSenderId)) {
                    jump = false;
                }
                for (int i = 0; i < senders.size(); i++) {
                    SignRuSender sender = senders.get(i);
                    if (jump || ruSenderId.equals(sender.getId())) {
                        jump = true;
                        if (ruSenderId != null && ruSenderId.equals(sender.getId())) {
                            continue;
                        }
                        if (sender.getSenderSignType() == 1) {
                            //创建签署任务
                            taskId = addRuTask(signRuId, sender.getId(), null, 2);
                            //调用自动签署接口
                            ruSignFlowService.autoSign(sender.getId(), 1);
                            //修改实例-操作表状态
                            updateRuOpetare(signRuId, sender.getId(), 1, 3);
                        } else if (sender.getSenderSignType() == 2) {
                            //创建签署任务
                            taskId = addRuTask(signRuId, sender.getId(), sender.getSenderUserId(), 1);
                            //修改实例-操作表状态
                            updateRuOpetare(signRuId, sender.getId(), 1, 2);
                            break;
                        }
                    }
                }
            }
        }
        return computentNextSignTask(taskId, signRuId);
    }

    @Override
    public void computeInSignTask(String taskId, String signRuId) {
        //计算并创建下一个签署任务
        SignRuSigner query = new SignRuSigner();
        query.setSignRuId(signRuId);
        query.setSignerType(1);
        List<SignRuSigner> signers = signRuSignerService.getByEntity(query);
        if (CollUtil.isNotEmpty(signers)) {
            SignRuSender senderQuery = new SignRuSender();
            senderQuery.setSignerId(signers.get(0).getId());
            List<SignRuSender> senders = signRuSenderService.getByEntity(senderQuery);
            if (CollUtil.isNotEmpty(senders)) {
                String ruSenderId = null;
                if (MyStringUtils.isNotBlank(taskId)) {
                    SignRuTask currentTask = signRuTaskService.getById(taskId);
                    if (currentTask != null) {
                        ruSenderId = currentTask.getUserTaskId();
                    }
                }
                boolean jump = true;
                if (MyStringUtils.isNotBlank(ruSenderId)) {
                    jump = false;
                }
                for (int i = 0; i < senders.size(); i++) {
                    SignRuSender sender = senders.get(i);
                    if (jump || ruSenderId.equals(sender.getId())) {
                        jump = true;
                        if (ruSenderId != null && ruSenderId.equals(sender.getId())) {
                            continue;
                        }
                        if (sender.getSenderSignType() == 1) {
                            //创建签署任务
                            addRuTask(signRuId, sender.getId(), null, 2);
                            //调用自动签署接口
                            ruSignFlowService.autoSign(sender.getId(), 1);
                            //修改实例-操作表状态
                            updateRuOpetare(signRuId, sender.getId(), 1, 3);
                        } else if (sender.getSenderSignType() == 2) {
                            //创建签署任务
                            addRuTask(signRuId, sender.getId(), sender.getSenderUserId(), 1);
                            //修改实例-操作表状态
                            updateRuOpetare(signRuId, sender.getId(), 1, 2);
                            break;
                        }
                    }
                }
            }
        }
    }


    @Autowired
    private MessageShortMappingService messageShortMappingService;

    @Override
    public void copyFlow(String signRuId, Integer type) {
        SignRuCcer query = new SignRuCcer();
        query.setSignRuId(signRuId);
        List<SignRuCcer> list = signRuCcerService.getByEntity(query);
        if (CollUtil.isNotEmpty(list)) {
            List<SignRuRelation> ruRelations = new ArrayList<>();
            list.forEach(l -> {
                SignRuRelation ruRelation = new SignRuRelation();
                ruRelation.setSignRuId(signRuId);
                ruRelation.setRelationType(2);
                ruRelation.setCcerType(l.getCcerType());
                if (MyStringUtils.isNotBlank(l.getTenantUserId())) {
                    ruRelation.setTenantUserId(l.getTenantUserId());
                } else {
                    ruRelation.setExternalCcedType(l.getExternalCcedType());
                    ruRelation.setExternalCcedValue(l.getExternalCcedValue());
                    if (l.getExternalCcedType() == 1) {
                        SysUser sysUser = sysUserService.getUserByPhone(l.getExternalCcedValue());
                        if (sysUser != null) {
                            SysTenantUser tenantUser = sysTenantUserService.getPersonalTenantUser(sysUser.getId());
                            if (tenantUser != null) {
                                ruRelation.setTenantUserId(tenantUser.getId());
                                ruRelation.setTaskLinkType("system");
                            }
                        }
                    } else if (l.getExternalCcedType() == 2) {
                        SysUser sysUser = sysUserService.getUserByEmail(l.getExternalCcedValue());
                        if (sysUser != null) {
                            SysTenantUser tenantUser = sysTenantUserService.getPersonalTenantUser(sysUser.getId());
                            if (tenantUser != null) {
                                ruRelation.setTenantUserId(tenantUser.getId());
                                ruRelation.setTaskLinkType("system");
                            }
                        }
                    } else {
                        ruRelation.setTenantUserId("unknown");
                        ruRelation.setTaskLinkType("unknown");
                    }
                }
                ruRelations.add(ruRelation);
            });
            signRuRelationService.saveBatch(ruRelations);
            //抄送短信通知
            SignRu signRu = signRuService.getById(signRuId);
            if (signRu != null) {
                SysTenantUser sysTenantUser = sysTenantUserService.getById(signRu.getSysUserId());
                ruRelations.forEach(r -> {
                    Map<String, String> para1 = new HashMap<>();
                    Map<String, String> para2 = new HashMap<>();
                    para1.put("sender", sysTenantUser != null ? sysTenantUser.getNickName() : "未知用户");
                    para1.put("contract", signRu.getSubject());
                    para2.put("sender", sysTenantUser != null ? sysTenantUser.getNickName() : "未知用户");
                    para2.put("contract", signRu.getSubject());
                    String code1 = RandomUtil.randomString(CommonConstant.BASE_CHECK_CODES, 8);
                    //防止code重复
                    while (messageShortMappingService.getByCode(code1) != null) {
                        code1 = RandomUtil.randomString(CommonConstant.BASE_CHECK_CODES, 8);
                    }
                    para1.put("code", code1);
                    String code2 = RandomUtil.randomString(CommonConstant.BASE_CHECK_CODES, 8);
                    //防止code重复
                    while (messageShortMappingService.getByCode(code2) != null) {
                        code2 = RandomUtil.randomString(CommonConstant.BASE_CHECK_CODES, 8);
                    }
                    para2.put("code", code2);

                    //生成短连接
                    Map<String, String> mappingPara1 = new HashMap<>();
                    Map<String, String> mappingPara2 = new HashMap<>();
                    mappingPara1.put("signRuId", signRuId);
                    mappingPara1.put("taskId", "");
                    mappingPara1.put("taskType", "copy");
                    mappingPara2.put("signRuId", signRuId);
                    mappingPara2.put("taskId", "");
                    mappingPara2.put("taskType", "copy");
                    MessageShortMapping mapping1 = new MessageShortMapping();
                    MessageShortMapping mapping2 = new MessageShortMapping();
                    mapping1.setMegCode(code1);
                    mapping2.setMegCode(code2);

                    MessageDto messageDto = new MessageDto();
                    MsgRequest msgRequest = new MsgRequest();
                    EmailDto emailDto = new EmailDto();
                    if (type == 1) {
                        //发起时
                        messageDto.setTemplateCode("copyBegin");
                        msgRequest.setTemplateName("copyBegin");
                        emailDto.setTemplateCode("email_copyBegin");
                    } else {
                        //签署完成时
                        messageDto.setTemplateCode("copySign");
                        msgRequest.setTemplateName("copySign");
                        emailDto.setTemplateCode("email_copySign");
                    }
                    messageDto.setSendType(SendType.IMMEDIATELY);
                    emailDto.setSendType(SendType.IMMEDIATELY);
                    List<String> receivers1 = new ArrayList<>();
                    List<String> receivers2 = new ArrayList<>();
                    if (MyStringUtils.isNotBlank(r.getTenantUserId())) {
                        SysTenantUser tenantUser = sysTenantUserService.getById(r.getTenantUserId());
                        if (tenantUser != null) {
                            SysUser sysUser = sysUserService.getById(tenantUser.getUserId());
                            if (sysUser != null) {
                                if (MyStringUtils.isNotBlank(sysUser.getPhone())) {
                                    receivers1.add(sysUser.getPhone());
                                }
                                if (MyStringUtils.isNotBlank(sysUser.getEmail())) {
                                    receivers2.add(sysUser.getEmail());
                                }
                            }
                        }
                    } else if (r.getExternalCcedType() == 1 && MyStringUtils.isNotBlank(r.getExternalCcedValue())) {
                        receivers1.add(r.getExternalCcedValue());
                    } else if (r.getExternalCcedType() == 2 && MyStringUtils.isNotBlank(r.getExternalCcedValue())) {
                        receivers2.add(r.getExternalCcedValue());
                    }
                    //发送短信
                    if (CollUtil.isNotEmpty(receivers1)) {


                        SysConfig isEcup = commonToolsAPI.getConfigByType("sms_channel");
                        SysConfig sendMessageConfig = commonToolsAPI.getConfigByType(SysConfigType.SENDMESSAGE.getType());
                        if(isEcup != null && "resrun".equals(isEcup.getValue()) && sendMessageConfig != null && "true".equals(sendMessageConfig.getValue())){
                            msgRequest.setPhoneNumbers(receivers1.get(0));
                            Map<String, String> para = new HashMap<>();
                            // todo 获取域名,补充信息
                            para.put("sender", sysTenantUser != null ? sysTenantUser.getNickName() : "未知用户");
                            para.put("contract", signRu.getSubject());
                            SysAppInfo sysAppInfo = sysAppInfoService.getById("490489ab-d8b4-414c-ad77-d856962c286f");
                            if (sysAppInfo != null && sysAppInfo.getAppAddress() != null) {
                                para.put("domain", sysAppInfo.getAppAddress());
                            }
                            para.put("code", code1);
                            msgRequest.setParams(para);
                            if (signReNoticeService.getByReIdAndType(signRu.getSignReId(), ReNoticeTypeEnum.COPY_SIGN_PHONE.getType())) {
                                smsSendService.sendMsg(msgRequest);
                                mapping1.setMsgPara(JacksonUtil.toJson(mappingPara1));
                                messageShortMappingService.save(mapping1);
                            }
                        }else{
                            messageDto.setReceivers(receivers1);
                            messageDto.setContentParaMap(para1);
                            if ((r.getCcerType().equals(1) && signReNoticeService.getByReIdAndType(signRu.getSignReId(), ReNoticeTypeEnum.COPY_BEGIN_PHONE.getType())) || (r.getCcerType().equals(2) && signReNoticeService.getByReIdAndType(signRu.getSignReId(), ReNoticeTypeEnum.COPY_SIGN_PHONE.getType()))) {
                                sysMessageUtil.asyncSendMessage(messageDto);
                                mappingPara1.put("phone", receivers1.get(0));

                                mapping1.setMsgPara(JacksonUtil.toJson(mappingPara1));
                                messageShortMappingService.save(mapping1);
                            }
                        }
                    }
                    //发送邮件
                    if (CollUtil.isNotEmpty(receivers2)) {
                        emailDto.setReceivers(receivers2);
                        SysAppInfo sysAppInfo = sysAppInfoService.getById("490489ab-d8b4-414c-ad77-d856962c286f");
                        if (sysAppInfo != null && sysAppInfo.getAppAddress() != null) {
                            para2.put("domain", sysAppInfo.getAppAddress());
                        }
                        emailDto.setContentParaMap(para2);
                        if ((r.getCcerType().equals(1) && signReNoticeService.getByReIdAndType(signRu.getSignReId(), ReNoticeTypeEnum.COPY_BEGIN_EMAIL.getType())) || (r.getCcerType().equals(2) && signReNoticeService.getByReIdAndType(signRu.getSignReId(), ReNoticeTypeEnum.COPY_SIGN_EMAIL.getType()))) {
                            sysMessageUtil.asyncSendEmail(emailDto);
                            mappingPara2.put("email", receivers2.get(0));

                            mapping2.setMsgPara(JacksonUtil.toJson(mappingPara2));
                            messageShortMappingService.save(mapping2);
                        }
                    }
                });
            }
        }
    }

    /**
     * 解析数据库已有数据做绑定表：sign_ru_task
     */
    @Override
    public void bindOutUserTask(String tenantName, String phone, String email, String fromTenantId, String fromUserId, String userId, String tenantId, String tenantUserId, String linkType) {
        if (MyStringUtils.isBlank(tenantName) && MyStringUtils.isBlank(phone) && MyStringUtils.isBlank(email) && MyStringUtils.isBlank(fromTenantId) && MyStringUtils.isBlank(fromUserId)) {
            return;
        }
        if (MyStringUtils.isNotBlank(userId) || MyStringUtils.isNotBlank(tenantId) || MyStringUtils.isNotBlank(tenantUserId)) {
            SignRuTask taskQuery = new SignRuTask();
            taskQuery.setTenantName(tenantName);
            taskQuery.setPhone(phone);
            taskQuery.setEmail(email);
            taskQuery.setTenantId(fromTenantId);
            taskQuery.setUserId(fromUserId);

            List<SignRuTask> ruTasks = signRuTaskService.getNoUserList(taskQuery);
            bindSignRuTask(ruTasks, userId, tenantId, tenantUserId, linkType);
        }
    }

    @Override
    public void bindTenantUserTaskAll(String tenantId, String linkType) {
        //绑定租户下未绑定的用户
        SignRuTask taskQuery = new SignRuTask();
        taskQuery.setTenantId(tenantId);
        List<SignRuTask> ruTasks = signRuTaskService.getTenanNoBindList(taskQuery);
        if (CollUtil.isNotEmpty(ruTasks)) {
            List<SignRuTask> tasks = new ArrayList<>();
            ruTasks.forEach(t -> {
                SysTenantUser tenantUser = sysTenantUserService.getActiviTenantUser(t.getTenantId(), t.getUserId());
                if (tenantUser != null) {
                    t.setTenantUserId(tenantUser.getId());
                    tasks.add(t);
                }
            });
            if (CollUtil.isNotEmpty(tasks)) {
                signRuTaskService.updateBatchById(tasks);
            }
        }
    }

    @Override
    public void bindOutUserTaskAll(String phone, String email, String linkType) {
        if (MyStringUtils.isBlank(phone) && MyStringUtils.isBlank(email)) {
            return;
        }
        SignRuTask taskQuery = new SignRuTask();
        taskQuery.setPhone(phone);
        taskQuery.setEmail(email);

        List<SignRuTask> ruTasks1 = signRuTaskService.getNoTenantUserList(taskQuery);
        if (CollUtil.isNotEmpty(ruTasks1)) {
            List<SignRuTask> tasks = new ArrayList<>();
            ruTasks1.forEach(t -> {
                SysTenantUser sysTenantUser = iSysTenantUserService.getActiviTenantUser(t.getTenantId(), t.getUserId());
                if (sysTenantUser != null) {
                    t.setTenantUserId(sysTenantUser.getId());
                    tasks.add(t);
                }
            });
            if (CollUtil.isNotEmpty(tasks)) {
                signRuTaskService.updateBatchById(tasks);
            }
        }

        //绑定个人租户
        List<SignRuTask> ruTasks2 = signRuTaskService.getPersonalTenanList(taskQuery);
        if (CollUtil.isNotEmpty(ruTasks2)) {
            List<SignRuTask> tasks = new ArrayList<>();
            ruTasks2.forEach(t -> {
                SysTenantUser tenantUser = sysTenantUserService.getPersonalTenantUser(t.getUserId());
                if (tenantUser != null) {
                    t.setTenantId(tenantUser.getTenantId());
                    t.setTenantUserId(tenantUser.getId());
                    tasks.add(t);
                }
            });
            if (CollUtil.isNotEmpty(tasks)) {
                signRuTaskService.updateBatchById(tasks);
            }
        }
    }

    /**
     * 解析数据库已有数据做绑定表：sign_ru_relation
     */
    @Override
    public void bindOutUserRelation(String phone, String email, String tenantUserId, String linkType) {
        if (MyStringUtils.isNotBlank(tenantUserId)) {
            if (MyStringUtils.isNotBlank(phone)) {
                SignRuRelation relationQuery = new SignRuRelation();
                relationQuery.setExternalCcedType(1);
                relationQuery.setExternalCcedValue(phone);
                List<SignRuRelation> ruRelations = signRuRelationService.getNoUserList(relationQuery);
                bindSignRuRelation(ruRelations, tenantUserId, linkType);
            }

            if (MyStringUtils.isNotBlank(email)) {
                SignRuRelation relationQuery = new SignRuRelation();
                relationQuery.setExternalCcedType(2);
                relationQuery.setExternalCcedValue(email);
                List<SignRuRelation> ruRelations = signRuRelationService.getNoUserList(relationQuery);
                bindSignRuRelation(ruRelations, tenantUserId, linkType);
            }
        }
    }

    @Override
    public void signReportAndSave(String ruId) {
        Long time1 = System.currentTimeMillis();
        SignRu signRu = signRuService.getById(ruId);
        if (signRu == null) {
            throw new PaasException("实例不存在");
        }
        String SUBREPORT_DIR = fileProperties.getPath().getPath();

        String jasperPath1 = SUBREPORT_DIR + "sign_1.jasper";
        String jasperPath2 = SUBREPORT_DIR + "sign_2.jasper";
        String jasperPath3 = SUBREPORT_DIR + "sign_3.jasper";
        String jasperPath4 = SUBREPORT_DIR + "sign_4.jasper";

//        try {
//            JasperDesign subReport = JRXmlLoader.load(SUBREPORT_DIR + "sign_2_sub.jrxml");
//            JasperCompileManager.compileReportToFile(subReport, SUBREPORT_DIR + "sign_2_sub.jasper");
//        } catch (JRException e) {
//            throw new RuntimeException(e);
//        }


        String backgroundImagePath = fileProperties.getPath().getPath() + "reportBack.png";

        Map<String, Object> parameters = new HashMap<String, Object>(2);
        parameters.put("SUBREPORT_DIR", SUBREPORT_DIR);

        List<SignReportSignSubVO> docFileVoList1 = deleteEmptyValue(ruSignReportService.baseInfo(ruId));
        JRBeanCollectionDataSource dataSource1 = new JRBeanCollectionDataSource(docFileVoList1);

        List<SignReportSignSubVO> docFileVoList2 = deleteEmptyValue(ruSignReportService.flowInfo(ruId));
        JRBeanCollectionDataSource dataSource2 = new JRBeanCollectionDataSource(docFileVoList2);

        Set<String> tenantIds = ruSignReportService.getAllTenantIds(ruId);

        List<SignReportSignSubVO> docFileVoList3 = deleteEmptyValue(sysTenantInfoService.queryTenantAuthLog(tenantIds));
        JRBeanCollectionDataSource dataSource3 = new JRBeanCollectionDataSource(docFileVoList3);

        List<SignReportSignSubVO> docFileVoList4 = new ArrayList<>();
        docFileVoList4.add(new SignReportSignSubVO());
        JRBeanCollectionDataSource dataSource4 = new JRBeanCollectionDataSource(docFileVoList4);

        ByteArrayOutputStream pdfStream = null;
        ByteArrayOutputStream outputStream = null;
        try {
            JasperPrint jasperPrint1 = JasperFillManager.fillReport(jasperPath1, parameters, dataSource1);
            JasperPrint jasperPrint2 = JasperFillManager.fillReport(jasperPath2, parameters, dataSource2);
            JasperPrint jasperPrint3 = JasperFillManager.fillReport(jasperPath3, parameters, dataSource3);
            JasperPrint jasperPrint4 = JasperFillManager.fillReport(jasperPath4, parameters, dataSource4);

            // 将两个 JasperPrint 对象添加到列表中
            List<JasperPrint> jasperPrintList = new ArrayList<>();
            jasperPrintList.add(jasperPrint1);
            jasperPrintList.add(jasperPrint2);
            jasperPrintList.add(jasperPrint3);
            jasperPrintList.add(jasperPrint4);

            // 4. 导出报表为 PDF 文件
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
            pdfStream = new ByteArrayOutputStream();
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfStream));

            exporter.exportReport();

            Long time2 = System.currentTimeMillis();

            System.out.println("导出PDF耗时：" + (time2 - time1) + "ms");

            // Step 2: 使用 PDFBox 添加背景图片
            try (PDDocument document = Loader.loadPDF(pdfStream.toByteArray())) {
                PDImageXObject backgroundImage = PDImageXObject.createFromFile(backgroundImagePath, document);
                for (PDPage page : document.getPages()) {
                    try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.PREPEND, true, true)) {
                        contentStream.drawImage(backgroundImage, 0, 0, page.getMediaBox().getWidth(), page.getMediaBox().getHeight());
                    }
                }
                outputStream = new ByteArrayOutputStream();
                document.save(outputStream);
                byte[] reportFinal = ruSignReportService.signReport(outputStream.toByteArray());
                annexStorageService.create(reportFinal, SignFileEnum.SIGN_FILE_REPORT.getDataCategory(), signRu.getSubject() + "电子文件签署记录报告.pdf", reportFinal.length, ruId);

//                String pdfFile2 = "/Users/zhenghuihan/Desktop/report——new.pdf";
//                document.save(pdfFile2);
//                IOUtils.write(reportFinal,new FileOutputStream(new File("/Users/gongfenglai/Desktop/test/opensign/电子文件签署记录报告" + System.currentTimeMillis() + ".pdf")));
                Long time3 = System.currentTimeMillis();
                System.out.println("设置背景图耗时：" + (time3 - time2) + "ms");
            }
        } catch (JRException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (pdfStream != null) {
                try {
                    pdfStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    List<SignReportSignSubVO> deleteEmptyValue(List<SignReportSignSubVO> list) {
        if (CollUtil.isNotEmpty(list)) {
            list.forEach(l -> {
                if (CollUtil.isNotEmpty(l.getSubReport())) {
                    Iterator<SignReportDocFileVo> iterator = l.getSubReport().iterator();
                    while (iterator.hasNext()) {
                        SignReportDocFileVo fileVo = iterator.next();
                        if (MyStringUtils.isBlank(fileVo.getValueSecond())) iterator.remove();
                    }
                }
            });
        }
        return list;
    }


    void bindSignRuTask(List<SignRuTask> ruTasks, String userId, String tenantId, String tenantUserId, String linkType) {
        if (CollUtil.isNotEmpty(ruTasks)) {
            ruTasks.forEach(r -> {
                r.setUserId(userId);
                r.setTenantId(tenantId);
                r.setTenantUserId(tenantUserId);
                r.setTaskLinkType(linkType);

            });
            signRuTaskService.updateBatchById(ruTasks);
        }
    }

    void bindSignRuRelation(List<SignRuRelation> ruRelations, String tenantUserId, String linkType) {
        if (CollUtil.isNotEmpty(ruRelations)) {
            ruRelations.forEach(r -> {
                r.setTenantUserId(tenantUserId);
                r.setTaskLinkType(linkType);
            });
            signRuRelationService.updateBatchById(ruRelations);
        }

    }

    void updateRuOpetare(String signRuId, String signRuSenderId, Integer userType, Integer operateStatus) {
        //修改实例-用户操作表任务状态
        SignRuOperator query = new SignRuOperator();
        query.setSignRuId(signRuId);
        query.setOperateType(2);
        query.setSignerType(userType);
        query.setSignerId(signRuSenderId);
        List<SignRuOperator> operators = signRuOperatorService.getByEntity(query);
        if (CollUtil.isNotEmpty(operators)) {
            operators.forEach(o -> {
                o.setOperateStatus(operateStatus);
                o.setOperateTime(new Date());
            });

            signRuOperatorService.updateBatchById(operators);
        }
    }

    String addRuTask(String signRuId, String signRuSenderId, String tenantUserId, Integer taskStatus) {
        SignRuTask task = new SignRuTask();
        task.setSignRuId(signRuId);
        task.setTaskType(TaskTypeEnum.SIGN_TASK.getCode());
        task.setUserType(1);
        task.setUserTaskId(signRuSenderId);
        if (MyStringUtils.isNotBlank(tenantUserId)) {
            SysTenantUser tenantUser = sysTenantUserService.getById(tenantUserId);
            if (tenantUser != null) {
                task.setUserId(tenantUser.getUserId());
                task.setTenantUserId(tenantUserId);
                task.setTenantId(tenantUser.getTenantId());
                task.setTaskLinkType("system");
                if (MyStringUtils.isNotBlank(tenantUser.getUserId())) {
                    SysUser sysUser = sysUserService.getById(tenantUser.getUserId());
                    if (sysUser != null) {
                        task.setPhone(sysUser.getUsername());
                    }
                }
                if (MyStringUtils.isNotBlank(tenantUser.getTenantId())) {
                    SysTenantInfo tenantInfo = sysTenantInfoService.getById(tenantUser.getTenantId());
                    if (tenantInfo != null) {
                        task.setTenantName(tenantInfo.getTenantName());
                    }
                }
            } else {
                task.setUserId("unknown");
                task.setTenantUserId(tenantUserId);
                task.setTenantId("unknown");
                task.setTaskLinkType("unknown");
            }
        } else {
            task.setUserId("system");
            task.setTenantUserId("system");
            task.setTenantId("system");
            task.setTaskLinkType("system");
        }

        task.setTaskStatus(taskStatus);
        if (taskStatus == 2) {
            task.setCompleteUserId("system");
            task.setCompleteTenantId("system");
            task.setCompleteTenantUserId("system");
            task.setCheckTime(new Date());
            task.setCheckMenuType("approve");
        }
        signRuTaskService.save(task);

        //内部签署人非自动签署任务发送签署短信通知
        if (taskStatus == 1) {
            SignRu signRu = signRuService.getById(signRuId);
            if (signRu != null) {
                SysTenantUser sysTenantUser1 = sysTenantUserService.getById(signRu.getSysUserId());
                SysTenantUser sysTenantUser2 = sysTenantUserService.getById(task.getTenantUserId());
                if (sysTenantUser1 != null) {
                    Map<String, String> para1 = new HashMap<>();
                    Map<String, String> para2 = new HashMap<>();
                    para1.put("sender", sysTenantUser1.getNickName());
                    para1.put("contract", signRu.getSubject());
                    para2.put("sender", sysTenantUser1.getNickName());
                    para2.put("contract", signRu.getSubject());
                    String code1 = RandomUtil.randomString(CommonConstant.BASE_CHECK_CODES, 8);
                    //防止code重复
                    while (messageShortMappingService.getByCode(code1) != null) {
                        code1 = RandomUtil.randomString(CommonConstant.BASE_CHECK_CODES, 8);
                    }
                    para1.put("code", code1);
                    String code2 = RandomUtil.randomString(CommonConstant.BASE_CHECK_CODES, 8);
                    //防止code重复
                    while (messageShortMappingService.getByCode(code2) != null) {
                        code2 = RandomUtil.randomString(CommonConstant.BASE_CHECK_CODES, 8);
                    }
                    para2.put("code", code2);

                    SysUser sysUser = sysUserService.getById(sysTenantUser2.getUserId());
                    //生成短连接
                    Map<String, String> mappingPara1 = new HashMap<>();
                    Map<String, String> mappingPara2 = new HashMap<>();
                    mappingPara1.put("signRuId", signRuId);
                    mappingPara1.put("taskId", task.getId());
                    mappingPara1.put("taskType", "sign");
                    mappingPara2.put("signRuId", signRuId);
                    mappingPara2.put("taskId", task.getId());
                    mappingPara2.put("taskType", "sign");
                    MessageShortMapping mapping1 = new MessageShortMapping();
                    mapping1.setMegCode(code1);
                    MessageShortMapping mapping2 = new MessageShortMapping();
                    mapping2.setMegCode(code2);

                    MessageDto messageDto = new MessageDto();
                    EmailDto emailDto = new EmailDto();
                    messageDto.setSendType(SendType.IMMEDIATELY);
                    emailDto.setSendType(SendType.IMMEDIATELY);
                    List<String> receivers1 = new ArrayList<>();
                    List<String> receivers2 = new ArrayList<>();
                    if (sysUser != null && MyStringUtils.isNotBlank(sysUser.getPhone())) {
                        mappingPara1.put("phone", sysUser.getPhone());

                        SysConfig isEcup = commonToolsAPI.getConfigByType("sms_channel");
                        SysConfig sendMessageConfig = commonToolsAPI.getConfigByType(SysConfigType.SENDMESSAGE.getType());
                        if(isEcup != null && "resrun".equals(isEcup.getValue()) && sendMessageConfig != null && "true".equals(sendMessageConfig.getValue())){
                            MsgRequest msgRequest = new MsgRequest();
                            msgRequest.setPhoneNumbers(sysUser.getPhone());
                            msgRequest.setTemplateName("signTaskIn");
                            Map<String, String> para = new HashMap<>();
                            // todo 获取域名,补充信息
                            para.put("sender", sysTenantUser1.getNickName());
                            para.put("contract", signRu.getSubject());
                            SysAppInfo sysAppInfo = sysAppInfoService.getById("490489ab-d8b4-414c-ad77-d856962c286f");
                            if (sysAppInfo != null && sysAppInfo.getAppAddress() != null) {
                                para.put("domain", sysAppInfo.getAppAddress());
                            }
                            para.put("code", code1);
                            msgRequest.setParams(para);
                            if (signReNoticeService.getByReIdAndType(signRu.getSignReId(), ReNoticeTypeEnum.SIGN_TASK_IN_PHONE.getType())) {
                                smsSendService.sendMsg(msgRequest);
                                mapping1.setMsgPara(JacksonUtil.toJson(mappingPara1));
                                messageShortMappingService.save(mapping1);
                            }
                        }else{
                            receivers1.add(sysUser.getPhone());
                            messageDto.setReceivers(receivers1);
                            messageDto.setTemplateCode("signTaskIn");
                            messageDto.setContentParaMap(para1);
                            if (signReNoticeService.getByReIdAndType(signRu.getSignReId(), ReNoticeTypeEnum.SIGN_TASK_IN_PHONE.getType())) {
                                sysMessageUtil.asyncSendMessage(messageDto);
                                mapping1.setMsgPara(JacksonUtil.toJson(mappingPara1));
                                messageShortMappingService.save(mapping1);
                            }
                        }
                    }
                    if (sysUser != null && MyStringUtils.isNotBlank(sysUser.getEmail())) {
                        mappingPara2.put("email", sysUser.getEmail());
                        receivers2.add(sysUser.getEmail());
                        emailDto.setReceivers(receivers2);
                        emailDto.setTemplateCode("email_signTaskIn");
                        SysAppInfo sysAppInfo = sysAppInfoService.getById("490489ab-d8b4-414c-ad77-d856962c286f");
                        if (sysAppInfo != null && sysAppInfo.getAppAddress() != null) {
                            para2.put("domain", sysAppInfo.getAppAddress());
                        }
                        emailDto.setContentParaMap(para2);
                        if (signReNoticeService.getByReIdAndType(signRu.getSignReId(), ReNoticeTypeEnum.SIGN_TASK_IN_EMAIL.getType())) {
                            sysMessageUtil.asyncSendEmail(emailDto);

                            mapping2.setMsgPara(JacksonUtil.toJson(mappingPara2));
                            messageShortMappingService.save(mapping2);
                        }
                    }
                    MailDto mailDto = new MailDto();
                    mailDto.setSendType(SendType.IMMEDIATELY);

                    Map<MesAuthType, List<String>> userMap = new HashMap<>();
                    List<String> userIds = Arrays.asList(task.getTenantUserId());
                    userMap.put(MesAuthType.USER, userIds);
                    mailDto.setReceivers(userMap);

                    mailDto.setTemplateCode("opensign_sign");

                    Map<String, String> titleParaMap = new HashMap<>();
                    titleParaMap.put("contract", signRu.getSubject());
                    titleParaMap.put("sender", sysTenantUser1.getNickName());
                    mailDto.setTitleParaMap(titleParaMap);

                    Map<String, String> contentParaMap = new HashMap<>();
                    contentParaMap.put("contract", signRu.getSubject());
                    contentParaMap.put("sender", sysTenantUser1.getNickName());
                    mailDto.setContentParaMap(contentParaMap);

                    Map<String, Map<String, String>> buttonParaMap = new HashMap<>();
                    Map<String, String> para3 = new HashMap<>();
                    para3.put("__full__", "");
                    para3.put("isDetail", "false");
                    para3.put("signReId", signRu.getSignReId());
                    para3.put("signRuId", signRu.getId());
                    para3.put("type", "");
                    para3.put("from", "list");
                    para3.put("taskId", task.getId());

                    buttonParaMap.put("contract_sign", para3);
                    mailDto.setButtonParaMap(buttonParaMap);

                    //发送通知
                    sysMessageUtil.asyncSendMail(mailDto);
                }
            }
        }
        return task.getId();
    }
}