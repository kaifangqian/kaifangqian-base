/**
 * @description 流程节点类型初始化
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
package com.kaifangqian.modules.opensign.config;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kaifangqian.modules.api.entity.ApiCallback;
import com.kaifangqian.modules.api.service.IApiCallbackService;
import com.kaifangqian.modules.api.util.CallbackUtil;
import com.kaifangqian.modules.api.vo.request.ApiCallbackVO;
import com.kaifangqian.modules.opensign.constant.SignCommonConstant;
import com.kaifangqian.modules.opensign.entity.SignRuTask;
import com.kaifangqian.modules.opensign.interceptor.SignCommandInterceptor;
import com.kaifangqian.modules.opensign.interceptor.SignCommandInvoker;
import com.kaifangqian.modules.opensign.service.business.PdfConvertService;
import com.kaifangqian.modules.opensign.service.business.PdfEncryptionService;
import com.kaifangqian.modules.opensign.service.confirm.IUserConfirmService;
import com.kaifangqian.modules.opensign.service.flow.IFlowService;
import com.kaifangqian.modules.opensign.service.flow.SignCommandExecutor;
import com.kaifangqian.modules.opensign.service.flow.impl.SignCommandExecutorImpl;
import com.kaifangqian.modules.opensign.service.flow.impl.SignServiceImpl;
import com.kaifangqian.modules.opensign.service.ru.SignRuTaskService;
import com.kaifangqian.modules.system.entity.*;
import com.kaifangqian.modules.system.service.*;
import com.kaifangqian.common.constant.ApiConstants;
import com.kaifangqian.common.util.RsaUtils;
import com.kaifangqian.common.util.SpringContextHolder;
import com.kaifangqian.entity.SysConfig;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.opensign.dto.OpenSignTaskInfo;
import com.kaifangqian.modules.opensign.enums.TaskTypeEnum;
import com.kaifangqian.modules.system.entity.*;
import com.kaifangqian.modules.system.service.*;
import com.kaifangqian.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : zhenghuihan
 * create at:  2023/11/7  15:25
 * @description: 流程节点类型初始化
 */
@Slf4j
@Configuration
public class OpensignFlowIntiConfig implements ApplicationListener<ApplicationReadyEvent> {

    public static final Map<String, OpenSignTaskInfo> taskMap = new HashMap<>();

    @Autowired
    protected List<SignCommandInterceptor> signCommandInterceptors;
    @Autowired
    private IFlowService flowService;
    @Autowired
    private IUserConfirmService userConfirmService;
    protected SignCommandExecutor signCommandExecutor;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private IApiCallbackService apiCallbackService;
    @Value("${paas.privateKey}")
    private String privateKey;

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private ITenantInfoExtendService tenantInfoExtendService;

    @Autowired
    private ITenantAuthLogService tenantAuthLogService;

    @Autowired
    private CertificateInfoDao certificateInfoDao;

    @Autowired
    private PdfEncryptionService pdfEncryptionService ;
    @Autowired
    private PdfConvertService pdfConvertService ;

    @Autowired
    private SignRuTaskService signRuTaskService;


    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        intiTaskMap();

        SignCommandInterceptor first = initInterceptorChain(signCommandInterceptors);
        signCommandExecutor = SpringContextHolder.getBean(SignCommandExecutorImpl.class);
        ((SignCommandExecutorImpl) signCommandExecutor).setFirst(first);

        initServices();

        //getQCloudToken();

        //清洗数据
        cleanData();

//        Boolean encryption = pdfEncryptionService.isEncryption();
//        System.out.println(encryption);
//        if(encryption){
//            String pdfEncryptionPassword = pdfEncryptionService.getPdfEncryptionPassword();
//            System.out.println(pdfEncryptionPassword);
//        }
//        Boolean convert = pdfConvertService.isConvert();
//        System.out.println(convert);
//        if(convert){
//            pdfConvertService.test();
//        }

        //数据不丢的接口回调
        apiCollBack();
    }

    public void apiCollBack() {
        while (true) {
            try {
//                Object message = redisTemplate.opsForList().rightPopAndLeftPush(ApiConstants.API_QUEUE_KEY, ApiConstants.API_PROCESSING_KEY); // 从队列中取出消息
                Object message = redisTemplate.opsForList().rightPop(ApiConstants.API_QUEUE_KEY); // 从队列中取出消息
                log.info("接收到回调消息:{}", message);
                if (message != null) {
                    ApiCallbackVO callbackVO = (ApiCallbackVO) message;
                    ApiCallback callback = apiCallbackService.getById(callbackVO.getId());
                    if(callback == null){
                        //确认消息-从处理队列中删除消息
//                        redisTemplate.opsForList().remove(ApiConstants.API_PROCESSING_KEY, 1, message);
//                        log.info("apiCollBack-opsForList-remove：不存在数据库中的数据，直接删除");
                        continue;
                    }

                    BeanUtils.copyProperties(callbackVO, callback);
                    //消息处理
                    boolean callbackFlag = CallbackUtil.callback(callbackVO.getId(), callbackVO.getStatus() == 0 ? 3 : 1, callbackVO.getCallbackUrl(), callbackVO.getReqPara(), privateKey);
                    log.info("apiCollBack-callbackFlag:" + callbackFlag);
                    if (callbackFlag) {
                        //发送成功，更新表数据
                        callback.setStatus(10);
                        apiCallbackService.updateById(callback);
                    } else {
                        //发送失败，更新表数据
                        callback.setStatus(callback.getStatus() + 1);
                        apiCallbackService.updateById(callback);
                    }
                    //确认消息-从处理队列中删除消息
//                    redisTemplate.opsForList().remove(ApiConstants.API_PROCESSING_KEY, 1, message);
//                    log.info("apiCollBack-opsForList-remove");
                } else {
                    //队列为空时等待一段时间
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
//                        e.printStackTrace();
                        log.info("线程等待异常");
                    }
                }
            } catch (Exception e) {
//                e.printStackTrace();
                //消息未被消费、重新把消息放回消息队列
                log.info("回调整体错误");
            }
        }
    }

    private void intiTaskMap() {
        Map<String, String> operateMap0 = new HashMap<>();
        operateMap0.put("initiateFlow", "com.kaifangqian.modules.opensign.service.cmd.InitiateFlowCmd");
        taskMap.put(TaskTypeEnum.INITIATE_FLOW.getCode(), intiTaskInfo(0, TaskTypeEnum.INITIATE_FLOW.getCode(), TaskTypeEnum.B_START_TASK.getCode(), operateMap0));
        Map<String, String> operateMap1 = new HashMap<>();
        operateMap1.put("startActivitiFlow", "com.kaifangqian.modules.opensign.service.cmd.StartActivitiFlowCmd");
        taskMap.put(TaskTypeEnum.B_START_TASK.getCode(), intiTaskInfo(1, TaskTypeEnum.B_START_TASK.getCode(), TaskTypeEnum.REAL_START_FLOW.getCode(), operateMap1));
        Map<String, String> operateMap2 = new HashMap<>();
        operateMap2.put("realStartFlow", "com.kaifangqian.modules.opensign.service.cmd.RealStartFlowCmd");
        taskMap.put(TaskTypeEnum.REAL_START_FLOW.getCode(), intiTaskInfo(2, TaskTypeEnum.REAL_START_FLOW.getCode(), TaskTypeEnum.INITIATE_WRITE_TASK.getCode(), operateMap2));
        Map<String, String> operateMap3 = new HashMap<>();
        operateMap3.put("initiateFillInTask", "com.kaifangqian.modules.opensign.service.cmd.InitiateFillInTaskCmd");
        taskMap.put(TaskTypeEnum.INITIATE_WRITE_TASK.getCode(), intiTaskInfo(3, TaskTypeEnum.INITIATE_WRITE_TASK.getCode(), TaskTypeEnum.INITIATE_SIGN_TASK.getCode(), operateMap3));
        Map<String, String> operateMap4 = new HashMap<>();
        operateMap4.put("approve", "com.kaifangqian.modules.opensign.service.cmd.ConfirmFillInCmd");
        operateMap4.put("reject", "com.kaifangqian.modules.opensign.service.cmd.RefuseFillInCmd");
        taskMap.put(TaskTypeEnum.WRITE_TASK.getCode(), intiTaskInfo(4, TaskTypeEnum.WRITE_TASK.getCode(), TaskTypeEnum.INITIATE_SIGN_TASK.getCode(), operateMap4));
        Map<String, String> operateMap5 = new HashMap<>();
        operateMap5.put("initiateSignTask", "com.kaifangqian.modules.opensign.service.cmd.InitiateSignTaskCmd");
        taskMap.put(TaskTypeEnum.INITIATE_SIGN_TASK.getCode(), intiTaskInfo(5, TaskTypeEnum.INITIATE_SIGN_TASK.getCode(), TaskTypeEnum.B_SIGN_TASK.getCode(), operateMap5));
        Map<String, String> operateMap6 = new HashMap<>();
        operateMap6.put("signActivitiFlow", "com.kaifangqian.modules.opensign.service.cmd.SignActivitiFlowCmd");
        taskMap.put(TaskTypeEnum.B_SIGN_TASK.getCode(), intiTaskInfo(6, TaskTypeEnum.B_SIGN_TASK.getCode(), TaskTypeEnum.FINISH_FLOW.getCode(), operateMap6));
        Map<String, String> operateMap7 = new HashMap<>();
        operateMap7.put("approve", "com.kaifangqian.modules.opensign.service.cmd.ConfirmSignCmd");
        operateMap7.put("reject", "com.kaifangqian.modules.opensign.service.cmd.RefuseSignCmd");
        taskMap.put(TaskTypeEnum.SIGN_TASK.getCode(), intiTaskInfo(7, TaskTypeEnum.SIGN_TASK.getCode(), TaskTypeEnum.FINISH_FLOW.getCode(), operateMap7));
        Map<String, String> operateMap8 = new HashMap<>();
        operateMap8.put("finishFlow", "com.kaifangqian.modules.opensign.service.cmd.FinishFlowCmd");
        taskMap.put(TaskTypeEnum.FINISH_FLOW.getCode(), intiTaskInfo(8, TaskTypeEnum.FINISH_FLOW.getCode(), null, operateMap8));
    }

    private OpenSignTaskInfo intiTaskInfo(Integer order, String type, String nextTaskType, Map<String, String> operateMap) {
        OpenSignTaskInfo taskInfo = new OpenSignTaskInfo();
        taskInfo.setOrder(order);
        taskInfo.setType(type);
        taskInfo.setNextTaskType(nextTaskType);
        taskInfo.setOperateMap(operateMap);
        return taskInfo;
    }

    public SignCommandInterceptor initInterceptorChain(List<SignCommandInterceptor> chain) {
        if (chain == null || chain.isEmpty()) {
            throw new PaasException("invalid command interceptor chain configuration: " + chain);
        }
        for (int i = 0; i < chain.size(); i++) {
            if (chain.get(i) instanceof SignCommandInvoker) {
                SignCommandInterceptor tem = chain.get(i);
                chain.remove(i);
                chain.add(tem);
            }
        }
        for (int i = 0; i < chain.size() - 1; i++) {
            chain.get(i).setNext(chain.get(i + 1));
        }
        return chain.get(0);
    }

    public void cleanData() {
        SysConfig sysConfig = sysConfigService.getByType("system_version");
        if (sysConfig == null) {
            return;
        }
        String value = sysConfig.getValue();
        if (value.compareTo("205") < 0) {
            //清洗TenantInfoExtend
            LambdaQueryWrapper<TenantInfoExtend> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(TenantInfoExtend::getDeleteFlag, 0);
            List<TenantInfoExtend> list1 = tenantInfoExtendService.list(queryWrapper1);
            if (CollUtil.isNotEmpty(list1)) {
                list1.forEach(l -> {
                    try {
                        l.setPhone(RsaUtils.encryptByPublicKey(SignCommonConstant.PUBLICKEYSTRING, l.getPhone()));
                        l.setCorporationNo(RsaUtils.encryptByPublicKey(SignCommonConstant.PUBLICKEYSTRING, l.getCorporationNo()));
                        l.setOrganizationNo(RsaUtils.encryptByPublicKey(SignCommonConstant.PUBLICKEYSTRING, l.getOrganizationNo()));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                tenantInfoExtendService.updateBatchById(list1);
            }

            //清洗TenantAuthLog
            LambdaQueryWrapper<TenantAuthLog> queryWrapper2 = new LambdaQueryWrapper<>();
            queryWrapper2.eq(TenantAuthLog::getDeleteFlag, 0);
            List<TenantAuthLog> list2 = tenantAuthLogService.list(queryWrapper2);
            if (CollUtil.isNotEmpty(list2)) {
                list2.forEach(l -> {
                    try {
                        l.setPhone(RsaUtils.encryptByPublicKey(SignCommonConstant.PUBLICKEYSTRING, l.getPhone()));
                        l.setCorporationNo(RsaUtils.encryptByPublicKey(SignCommonConstant.PUBLICKEYSTRING, l.getCorporationNo()));
                        l.setOrganizationNo(RsaUtils.encryptByPublicKey(SignCommonConstant.PUBLICKEYSTRING, l.getOrganizationNo()));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                tenantAuthLogService.updateBatchById(list2);
            }
            //清洗CertificateInfo
            LambdaQueryWrapper<CertificateInfo> queryWrapper3 = new LambdaQueryWrapper<>();
            queryWrapper3.eq(CertificateInfo::getDeleteFlag, 0);
            List<CertificateInfo> list3 = certificateInfoDao.list(queryWrapper3);
            if (CollUtil.isNotEmpty(list3)) {
                list3.forEach(l -> {
                    try {
                        l.setCertPassword(RsaUtils.encryptByPublicKey(SignCommonConstant.PUBLICKEYSTRING, l.getCertPassword()));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                certificateInfoDao.updateBatchById(list3);
            }

            sysConfig.setValue("205");
        }

        value = sysConfig.getValue();
        if (value.compareTo("207") < 0) {
            //清洗基础权限组-新增菜单（授权业务线）
            List<Integer> types = new ArrayList<>();
            types.add(1);
            types.add(2);
            List<SysAuthGroup> authGroups = sysAuthGroupService.getAllSystemTypeGroups(types);
            if (CollUtil.isNotEmpty(authGroups)) {
                List<SysAuthGroupPermission> list2 = new ArrayList<>();
                authGroups.forEach(g -> {
                    SysAuthGroupPermission permission = new SysAuthGroupPermission();
                    permission.setGroupId(g.getId());
                    permission.setAppId("490489ab-d8b4-414c-ad77-d856962c286f");
                    permission.setPermissionId("5600d9b6-3074-403c-b83b-883fb0394d22");

                    list2.add(permission);
                });


                sysAuthGroupPermissionService.saveBatch(list2);
            }


            sysConfig.setValue("207");
        }

        sysConfigService.updateById(sysConfig);

        //绑定并补充企业用户签署任务数据
        SignRuTask query = new SignRuTask();
        List<SignRuTask> signRuTaskList = signRuTaskService.getTenantNoBindListForLoading(query);
        signRuTaskList.forEach(s -> {
            //解析数据库已有数据做绑定表:sign_ru_task
            //绑定企业名：租户ID
            flowService.bindOutUserTask(s.getTenantName(), null, null, null, null, null, s.getTenantId(), null, "tenantCheck");
            //绑定租户ID：(查询用户ID)租户账号ID
            flowService.bindTenantUserTaskAll(s.getTenantId(), "tenantCheck");
        });

    }


    @Autowired
    private ISysAuthGroupService sysAuthGroupService;
    @Autowired
    private ISysAuthGroupPermissionService sysAuthGroupPermissionService;

    public void initServices() {
        initService(flowService);
    }

    public void initService(Object service) {
        if (service instanceof SignServiceImpl) {
            ((SignServiceImpl) service).setCommandExecutor(signCommandExecutor);
        }
    }

    void getQCloudToken() {
        try {
            userConfirmService.getQCloudToken();
        }catch (Exception e){
            log.error("getQCloudToken error:",e);
        }
    }

    public String getPrivateKey() {
        return privateKey;
    }
}