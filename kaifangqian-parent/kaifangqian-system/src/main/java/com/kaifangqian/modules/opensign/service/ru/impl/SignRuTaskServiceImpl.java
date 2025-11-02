/**
 * @description 签署业务签署任务接口实现类，获取各类签署任务
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
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.modules.system.entity.SysTenantInfo;
import com.kaifangqian.modules.system.entity.SysTenantUser;
import com.kaifangqian.modules.system.entity.SysUser;
import com.kaifangqian.modules.system.vo.SysTenantInfoExtendVO;
import com.kaifangqian.modules.system.vo.TenantUser3rd;
import com.kaifangqian.common.constant.CommonConstant;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.entity.MessageShortMapping;
import com.kaifangqian.modules.opensign.entity.SignRu;
import com.kaifangqian.modules.opensign.entity.SignRuRelation;
import com.kaifangqian.modules.opensign.entity.SignRuTask;
import com.kaifangqian.modules.opensign.enums.TaskTypeEnum;
import com.kaifangqian.modules.opensign.mapper.SignRuTaskMapper;
import com.kaifangqian.modules.opensign.service.re.SignReAuthService;
import com.kaifangqian.modules.opensign.service.ru.SignRuRelationService;
import com.kaifangqian.modules.opensign.service.ru.SignRuService;
import com.kaifangqian.modules.opensign.service.ru.SignRuTaskService;
import com.kaifangqian.modules.opensign.vo.base.sign.TaskInfoFor3rdVO;
import com.kaifangqian.modules.opensign.vo.base.sign.TaskSearchFor3rdVO;
import com.kaifangqian.modules.system.service.ISysTenantInfoService;
import com.kaifangqian.modules.system.service.ISysTenantUserService;
import com.kaifangqian.modules.system.service.ISysUserService;
import com.kaifangqian.service.MessageShortMappingService;
import com.kaifangqian.utils.JacksonUtil;
import com.kaifangqian.utils.MyStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SignRuTaskServiceImpl extends ServiceImpl<SignRuTaskMapper, SignRuTask> implements SignRuTaskService {

    @Resource
    private SignRuTaskMapper mapper;
    @Autowired
    private SignRuRelationService signRuRelationService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysTenantUserService iSysTenantUserService;
    @Autowired
    private ISysTenantInfoService sysTenantInfoService;
    @Autowired
    private SignReAuthService signReAuthService;
    @Autowired
    private SignRuService signRuService;

    @Autowired
    private MessageShortMappingService messageShortMappingService;

    @Override
    public List<SignRuTask> getByEntity(SignRuTask query) {
        LambdaQueryWrapper<SignRuTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(MyStringUtils.isNotBlank(query.getSignRuId()), SignRuTask::getSignRuId, query.getSignRuId())
                .eq(MyStringUtils.isNotBlank(query.getTaskType()), SignRuTask::getTaskType, query.getTaskType())
                .eq(query.getTaskStatus() != null, SignRuTask::getTaskStatus, query.getTaskStatus())
                .eq(MyStringUtils.isNotBlank(query.getTenantUserId()), SignRuTask::getTenantUserId, query.getTenantUserId())
                .eq(MyStringUtils.isNotBlank(query.getUserId()), SignRuTask::getUserId, query.getUserId())
                .eq(MyStringUtils.isNotBlank(query.getPhone()), SignRuTask::getPhone, query.getPhone())
                .eq(MyStringUtils.isNotBlank(query.getEmail()), SignRuTask::getEmail, query.getEmail())
                .eq(SignRuTask::getDeleteFlag, false);
        return this.list(queryWrapper);
    }

    @Override
    public List<SignRuTask> getNoUserList(SignRuTask query) {
        LambdaQueryWrapper<SignRuTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MyStringUtils.isNotBlank(query.getTenantName()), SignRuTask::getTenantName, query.getTenantName()).eq(MyStringUtils.isNotBlank(query.getPhone()), SignRuTask::getPhone, query.getPhone()).eq(MyStringUtils.isNotBlank(query.getEmail()), SignRuTask::getEmail, query.getEmail()).eq(MyStringUtils.isNotBlank(query.getTenantId()), SignRuTask::getTenantId, query.getTenantId()).eq(MyStringUtils.isNotBlank(query.getUserId()), SignRuTask::getUserId, query.getUserId()).eq(SignRuTask::getDeleteFlag, false).isNull(SignRuTask::getTenantUserId);

        return this.list(queryWrapper);
    }

    @Override
    public List<SignRuTask> getNoTenantUserList(SignRuTask query) {
        LambdaQueryWrapper<SignRuTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MyStringUtils.isNotBlank(query.getPhone()), SignRuTask::getPhone, query.getPhone()).eq(MyStringUtils.isNotBlank(query.getEmail()), SignRuTask::getEmail, query.getEmail()).eq(SignRuTask::getDeleteFlag, false).isNotNull(SignRuTask::getTenantId).isNull(SignRuTask::getTenantUserId);

        return this.list(queryWrapper);
    }

    @Override
    public List<SignRuTask> getTenantNoBindList(SignRuTask query) {
        LambdaQueryWrapper<SignRuTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MyStringUtils.isNotBlank(query.getTenantId()), SignRuTask::getTenantId, query.getTenantId())
                .eq(SignRuTask::getDeleteFlag, false)
                .isNotNull(SignRuTask::getUserId)
                .isNull(SignRuTask::getTenantUserId);

        return this.list(queryWrapper);
    }

    @Override
    public List<SignRuTask> getTenantNoBindListForApi(SignRuTask query) {
        LambdaQueryWrapper<SignRuTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MyStringUtils.isNotBlank(query.getTenantId()), SignRuTask::getTenantId, query.getTenantId())
                .eq(SignRuTask::getDeleteFlag, false)
                .isNull(SignRuTask::getTenantUserId);

        return this.list(queryWrapper);
    }

    @Override
    public List<SignRuTask> getTenantNoBindListForLoading(SignRuTask query) {
        LambdaQueryWrapper<SignRuTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SignRuTask::getDeleteFlag, false)
                .eq(SignRuTask::getTaskType, TaskTypeEnum.SIGN_TASK.getCode())
                .isNull(SignRuTask::getUserId)
                .isNull(SignRuTask::getTenantUserId);

        return this.list(queryWrapper);
    }

    @Override
    public List<SignRuTask> getPersonalTenanList(SignRuTask query) {
        LambdaQueryWrapper<SignRuTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MyStringUtils.isNotBlank(query.getPhone()), SignRuTask::getPhone, query.getPhone()).eq(MyStringUtils.isNotBlank(query.getEmail()), SignRuTask::getEmail, query.getEmail()).eq(SignRuTask::getDeleteFlag, false).eq(SignRuTask::getTenantName, "个人租户").isNull(SignRuTask::getTenantId).isNull(SignRuTask::getTenantUserId);

        return this.list(queryWrapper);
    }

    @Override
    public List<SysTenantInfoExtendVO> getMyTenantJobs() {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        return mapper.getMyTenantJobs(loginUser.getId());
    }

    @Override
    public TaskInfoFor3rdVO getTaskInfoFor3rd(TaskSearchFor3rdVO searchFor3rdVO) {
        TaskInfoFor3rdVO result = new TaskInfoFor3rdVO();
        BeanUtils.copyProperties(searchFor3rdVO, result);
        if (MyStringUtils.isBlank(searchFor3rdVO.getSignRuId()) || MyStringUtils.isBlank(searchFor3rdVO.getUsername())) {
            return null;
        }
        if (MyStringUtils.isBlank(searchFor3rdVO.getTenantName())) {
            //身份信息为空
            if (MyStringUtils.isBlank(searchFor3rdVO.getTaskId())) {
                //任务ID为空
                //1、查询任务表
                //1）查询代办任务
                SignRuTask query = new SignRuTask();
                query.setSignRuId(searchFor3rdVO.getSignRuId());
                query.setTaskStatus(1);
                if (searchFor3rdVO.getUsername().contains("@")) {
                    query.setEmail(searchFor3rdVO.getUsername());
                } else {
                    query.setPhone(searchFor3rdVO.getUsername());
                }
                List<SignRuTask> tasks = getByEntity(query);
                if (CollUtil.isNotEmpty(tasks)) {
                    result.setTaskType(tasks.get(0).getTaskType());
                    result.setTaskId(tasks.get(0).getId());
                    SysTenantInfo tenantInfo = sysTenantInfoService.getById(tasks.get(0).getTenantId());
                    if (tenantInfo != null) {
                        result.setTenantName(tenantInfo.getTenantName());
                    }
                    return result;
                } else {
                    //2）不存在代办任务时，查询已办任务
                    boolean taskFlag = false;

                    query.setTaskStatus(2);
                    tasks = getByEntity(query);
                    if (CollUtil.isNotEmpty(tasks)) {
                        taskFlag = true;
                        SignRuTask task = null;
                        for (SignRuTask t : tasks) {
                            if (MyStringUtils.isNotBlank(t.getTenantUserId())) {
                                task = t;
                                break;
                            }
                        }
                        if (task != null) {
                            result.setTaskType(task.getTaskType());
                            result.setTaskId(task.getId());
                            SysTenantInfo tenantInfo = sysTenantInfoService.getById(task.getTenantId());
                            if (tenantInfo != null) {
                                result.setTenantName(tenantInfo.getTenantName());
                            }
                            return result;
                        }
                    }
                    //2、查询抄送表
                    //查询用户名对应的个人租户ID
                    String personalTenantUserId = null;
                    SignRuRelation relationQuery = new SignRuRelation();
                    relationQuery.setSignRuId(searchFor3rdVO.getSignRuId());
                    SysUser sysUser = sysUserService.getUserByName(searchFor3rdVO.getUsername());
                    if (sysUser != null) {
                        SysTenantUser tenantUser = iSysTenantUserService.getPersonalTenantUser(sysUser.getId());
                        if (tenantUser != null) {
                            personalTenantUserId = tenantUser.getId();
                        }
                    }
                    if (MyStringUtils.isNotBlank(personalTenantUserId)) {
                        relationQuery.setTenantUserId(personalTenantUserId);
                    } else {
                        relationQuery.setExternalCcedValue(searchFor3rdVO.getUsername());
                    }
                    relationQuery.setRelationType(2);
                    List<SignRuRelation> relations = signRuRelationService.getByEntity(relationQuery);
                    if (CollUtil.isNotEmpty(relations)) {
                        result.setTaskType("copy");
                        if (MyStringUtils.isNotBlank(relations.get(0).getTenantUserId())) {
                            SysTenantInfo tenantInfo = sysTenantInfoService.getPersonalByTenantUserId(relations.get(0).getTenantUserId());
                            if (tenantInfo != null) {
                                result.setTenantName(tenantInfo.getTenantName());
                            }
                        }
                        return result;
                    } else if (taskFlag) {
                        result.setTaskType("copy");
                        if (MyStringUtils.isNotBlank(relations.get(0).getTenantUserId())) {
                            SysTenantInfo tenantInfo = sysTenantInfoService.getPersonalByTenantUserId(relations.get(0).getTenantUserId());
                            if (tenantInfo != null) {
                                result.setTenantName(tenantInfo.getTenantName());
                            }
                        }
                        return result;
                    } else {
                        return null;
                    }
                }
            } else {
                //任务ID不为空
                SignRuTask task = getById(searchFor3rdVO.getTaskId());
                if (task == null) {
                    return null;
                } else {
                    result.setTaskType(task.getTaskType());
                    SysTenantInfo tenantInfo = sysTenantInfoService.getById(task.getTenantId());
                    if (tenantInfo != null) {
                        result.setTenantName(tenantInfo.getTenantName());
                    }
                    return result;
                }
            }
        } else {
            //身份信息不为空
            String tenantUserId = null;
            Integer tenantType = null;
            List<TenantUser3rd> tenantInfos = sysTenantInfoService.getByUsernameAndTenantName(searchFor3rdVO.getUsername(), searchFor3rdVO.getTenantName());
            if (CollUtil.isNotEmpty(tenantInfos)) {
                tenantUserId = tenantInfos.get(0).getTenantUserId();
                tenantType = tenantInfos.get(0).getTenantType();
            }
            if (MyStringUtils.isBlank(tenantUserId)) {
                //租户用户不存在
                if (MyStringUtils.isBlank(searchFor3rdVO.getTaskId())) {
                    //任务ID为空
                    //查询任务表
                    //1）查询代办任务
                    SignRuTask query = new SignRuTask();
                    query.setSignRuId(searchFor3rdVO.getSignRuId());
                    query.setTaskStatus(1);
                    if (searchFor3rdVO.getUsername().contains("@")) {
                        query.setEmail(searchFor3rdVO.getUsername());
                    } else {
                        query.setPhone(searchFor3rdVO.getUsername());
                    }
                    List<SignRuTask> tasks = getByEntity(query);
                    if (CollUtil.isNotEmpty(tasks)) {
                        result.setTaskId(tasks.get(0).getId());
                        result.setTaskType(tasks.get(0).getTaskType());
                        return result;
                    } else {
                        //2）不存在代办任务时，查询已办任务
                        query.setTaskStatus(2);
                        tasks = getByEntity(query);
                        if (CollUtil.isNotEmpty(tasks)) {
                            SignRuTask task = null;
                            for (SignRuTask t : tasks) {
                                if (MyStringUtils.isNotBlank(t.getTenantUserId())) {
                                    task = t;
                                    break;
                                }
                            }
                            if (task != null) {
                                result.setTaskId(task.getId());
                                result.setTaskType(task.getTaskType());
                                return result;
                            } else {
                                result.setTaskId(tasks.get(0).getId());
                                result.setTaskType(tasks.get(0).getTaskType());
                                return result;
                            }
                        }
                        return null;
                    }
//                    //身份为个人时，查询ru_ralation表（如何判断个人身份）
//                    SignRuRelation relationQuery = new SignRuRelation();
//                    relationQuery.setSignRuId(searchFor3rdVO.getSignRuId());
//                    relationQuery.setExternalCcedValue(searchFor3rdVO.getUsername());
//                    relationQuery.setRelationType(2);
//                    List<SignRuRelation> relations = signRuRelationService.getByEntity(relationQuery);
//                    if (CollUtil.isNotEmpty(relations)) {
//                        result.setTaskType("copy");
//                        result.setTenantName("PERSONAL");
//                        return result;
//                    } else if (taskFlag) {
//                        result.setTaskType("copy");
//                        result.setTenantName("PERSONAL");
//                        return result;
//                    } else {
//                        return null;
//                    }
                } else {
                    //任务ID不为空
                    SignRuTask task = getById(searchFor3rdVO.getTaskId());
                    if (task == null) {
                        return null;
                    } else {
                        result.setTaskType(task.getTaskType());
                        return result;
                    }
                }
            } else {
                //租户用户存在
                if (MyStringUtils.isBlank(searchFor3rdVO.getTaskId())) {
                    //任务ID为空
                    //查询任务表
                    //1）查询代办任务
                    boolean taskFlag = false;

                    SignRuTask query = new SignRuTask();
                    query.setSignRuId(searchFor3rdVO.getSignRuId());
                    query.setTaskStatus(1);
                    query.setTenantUserId(tenantUserId);
                    List<SignRuTask> tasks = getByEntity(query);
                    if (CollUtil.isNotEmpty(tasks)) {
                        result.setTaskId(tasks.get(0).getId());
                        result.setTaskType(tasks.get(0).getTaskType());
                        return result;
                    } else {
                        //2）不存在代办任务时，查询已办任务
                        query.setTaskStatus(2);
                        tasks = getByEntity(query);
                        if (CollUtil.isNotEmpty(tasks)) {
                            taskFlag = true;
                            SignRuTask task = null;
                            for (SignRuTask t : tasks) {
                                if (MyStringUtils.isNotBlank(t.getTenantUserId())) {
                                    task = t;
                                    break;
                                }
                            }
                            if (task != null) {
                                result.setTaskId(task.getId());
                                result.setTaskType(task.getTaskType());
                                return result;
                            }
                        }
                    }
                    if (tenantType == 2) {
                        //个人查询抄送表
                        SignRuRelation relationQuery = new SignRuRelation();
                        relationQuery.setSignRuId(searchFor3rdVO.getSignRuId());
                        relationQuery.setTenantUserId(tenantUserId);
                        relationQuery.setRelationType(2);
                        List<SignRuRelation> relations = signRuRelationService.getByEntity(relationQuery);
                        if (CollUtil.isNotEmpty(relations)) {
                            result.setTaskType("copy");
                            return result;
                        }
                    } else {
                        //企业用户查询权限表
                        SignRu signRu = signRuService.getById(searchFor3rdVO.getSignRuId());
                        if (signRu == null) {
                            return null;
                        }
                        List<String> reIds = signReAuthService.geSignReByTenantUserId(tenantUserId);
                        if (CollUtil.isNotEmpty(reIds) && reIds.contains(signRu.getSignReId())) {
                            return result;
                        }
                    }
                    if (taskFlag) {
                        result.setTaskType("copy");
                        return result;
                    } else {
                        return null;
                    }
                } else {
                    //任务ID不为空
                    SignRuTask task = getById(searchFor3rdVO.getTaskId());
                    if (task == null) {
                        return null;
                    } else {
                        result.setTaskType(task.getTaskType());
                        return result;
                    }
                }
            }
        }

    }

    @Override
    public String getCodeFor3rd(TaskSearchFor3rdVO searchFor3rdVO) {
        TaskInfoFor3rdVO result = getTaskInfoFor3rd(searchFor3rdVO);
        if (result != null) {
            MessageShortMapping shortMapping = new MessageShortMapping();
            Map<String, String> mapping = new HashMap<>();
            mapping.put("signRuId", result.getSignRuId());
            if (result.getUsername().contains("@")) {
                mapping.put("email", result.getUsername());
            } else {
                mapping.put("phone", result.getUsername());
            }
            if (MyStringUtils.isNotBlank(result.getTaskId())) {
                mapping.put("taskId", result.getTaskId());
            }
            if (MyStringUtils.isNotBlank(result.getTaskType())) {
                if (result.getTaskType().equals(TaskTypeEnum.WRITE_TASK.getCode())) {
                    mapping.put("taskType", "write");
                } else if (result.getTaskType().equals(TaskTypeEnum.SIGN_TASK.getCode())) {
                    mapping.put("taskType", "sign");
                } else {
                    mapping.put("taskType", result.getTaskType());
                }
            }
            if (MyStringUtils.isNotBlank(result.getTenantName())) {
                mapping.put("tenantName", result.getTenantName());
            }
            String code = RandomUtil.randomString(CommonConstant.BASE_CHECK_CODES, 8);
            //防止code重复
            while (messageShortMappingService.getByCode(code) != null) {
                code = RandomUtil.randomString(CommonConstant.BASE_CHECK_CODES, 8);
            }
            shortMapping.setMegCode(code);
            shortMapping.setNoLogin(searchFor3rdVO.getNoLogin());
            shortMapping.setPageUrlExpireTime(searchFor3rdVO.getPageUrlExpireTime());
            shortMapping.setMsgPara(JacksonUtil.toJson(mapping));
            messageShortMappingService.save(shortMapping);

            return code;
        } else {
            return null;
        }
    }
}