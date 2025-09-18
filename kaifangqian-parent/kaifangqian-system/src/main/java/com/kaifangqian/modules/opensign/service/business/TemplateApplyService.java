/**
 * @description 模板申请业务逻辑
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
package com.kaifangqian.modules.opensign.service.business;

import com.kaifangqian.modules.opensign.service.template.SignTemplateLogApplyService;
import com.kaifangqian.modules.opensign.service.template.SignTemplateLogErrorService;
import com.kaifangqian.modules.opensign.service.template.SignTemplateService;
import com.kaifangqian.modules.opensign.entity.SignTemplate;
import com.kaifangqian.modules.opensign.entity.SignTemplateLogApply;
import com.kaifangqian.modules.opensign.entity.SignTemplateLogError;
import com.kaifangqian.modules.opensign.enums.ApplyErrorEnum;
import com.kaifangqian.modules.opensign.enums.TemplateApplyStatusEnum;
import com.kaifangqian.modules.opensign.enums.TemplateStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Description: TemplateApplyService
 * @Package: com.kaifangqian.modules.opensign.service.business
 * @ClassName: TemplateApplyService
 * @author: FengLai_Gong
 */
@Service
public class TemplateApplyService {


    @Autowired
    private SignTemplateService templateService ;
    @Autowired
    private SignTemplateLogApplyService templateLogApplyService;
    @Autowired
    private SignTemplateLogErrorService templateLogErrorService ;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor ;

    @Autowired
    private TemplateBusinessService templateBusinessService ;


    /**
     * @Description #模板变更-审批未通过
     *  *                  申请状态变更为审批未通过
     * @Param [templateLogApplyId]
     * @return void
     **/
    public void templateChangeFailed(String templateLogApplyId){
        updateStatus(templateLogApplyId,TemplateApplyStatusEnum.APPROVAL_FAILED);
    }

    /**
     * @Description #模板变更-审批通过
     *  *                  申请状态变更为审批通过；
     * @Param [templateLogApplyId]
     * @return void
     **/
    public void templateChangeSuccess(String templateLogApplyId){
        updateStatus(templateLogApplyId,TemplateApplyStatusEnum.APPROVAL_SUCCESS);
//        templateBusinessService.copyApplyToTemplate(templateLogApplyId);
    }

    /**
     * @Description #模板制作-审批未通过
     *  *                  实现两个操作：（1）申请状态变更为审批未通过；（2）模板状态变更为制作失败
     * @Param [templateLogApplyId]
     * @return void
     **/
    public void templateMakeFailed(String templateLogApplyId){
        updateStatus(templateLogApplyId,TemplateApplyStatusEnum.APPROVAL_FAILED,TemplateStatusEnum.MAKE_FAILED);
    }

    /**
     * @Description #模板制作/模板变更-审批人驳回申请
     *  *                  申请状态改为待重新提交
     * @Param [templateLogApplyId]
     * @return void
     **/
    public void templateMakeOrChangeReject(String templateLogApplyId){
        updateStatus(templateLogApplyId,TemplateApplyStatusEnum.TO_BE_RE_SUMMIT);
    }

    /**
     * @Description #模板制作-审批通过
     *  *                  实现两个操作：（1）申请状态变更为审批通过；（2）模板状态变更为已启用
     * @Param [templateLogApplyId]
     * @return void
     **/
    public void templateMakeSuccess(String templateLogApplyId){
        updateStatus(templateLogApplyId,TemplateApplyStatusEnum.APPROVAL_SUCCESS,TemplateStatusEnum.ENABLED);
    }


    /**
     * @Description #模板变更-申请人作废申请
     *  *                  申请状态变更为作废；
     * @Param [templateLogApplyId]
     * @return void
     **/
    public void templateChangeCancel(String templateLogApplyId){
        updateStatus(templateLogApplyId,TemplateApplyStatusEnum.CANCELED);
    }

    /**
     * @Description #模板制作-申请人作废申请
     *  *                  实现两个操作：（1）申请状态变更为作废；（2）模板状态变更为制作失败
     * @Param [templateLogApplyId]
     * @return void
     **/
    public void templateMakeCancel(String templateLogApplyId){
        updateStatus(templateLogApplyId,TemplateApplyStatusEnum.CANCELED,TemplateStatusEnum.MAKE_FAILED);
    }

    /**
     * @Description #模板制作/模板变更-申请人提交申请
     *  *                  申请人提交后，申请处于待审批状态
     * @Param [templateLogApplyId]
     * @return void
     **/
    public void templateMakeOrChangeSubmit(String templateLogApplyId){
        updateStatus(templateLogApplyId,TemplateApplyStatusEnum.TO_BE_APPROVAL);
    }

    /**
     * @Description #模板制作/模板变更-申请人撤销申请
     *  *                  申请状态变更为待重新提交；
     * @Param [templateLogApplyId]
     * @return void
     **/
    public void templateMakeRevoke(String templateLogApplyId){
        updateStatus(templateLogApplyId,TemplateApplyStatusEnum.TO_BE_RE_SUMMIT);
    }


    public void templateInit(String templateLogApplyId){
        updateStatus(templateLogApplyId,TemplateApplyStatusEnum.TO_BE_SUMMIT);
    }



    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(String templateLogApplyId , TemplateApplyStatusEnum templateApplyStatusEnum){
        SignTemplateLogApply templateLogApply = templateLogApplyService.getById(templateLogApplyId);
        if(templateLogApply == null){
            errorLog(null,templateLogApplyId,null,null,ApplyErrorEnum.NO_BUSINESS_DATA);
        }
        templateLogApply.setApplyStatus(templateApplyStatusEnum.getCode());
        boolean b = templateLogApplyService.updateById(templateLogApply);
        if(!b){
            errorLog(null,templateLogApplyId,null,null,ApplyErrorEnum.UPDATE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(String templateId , TemplateStatusEnum templateStatusEnum){
        SignTemplate signTemplate = templateService.getById(templateId);
        if(signTemplate == null){
            errorLog(templateId,null,null,null,ApplyErrorEnum.NO_BUSINESS_DATA);
        }
        signTemplate.setTemplateStatus(templateStatusEnum.getCode());
        boolean b = templateService.updateById(signTemplate);
        if(!b){
            errorLog(templateId,null,null,null,ApplyErrorEnum.UPDATE_ERROR);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(String templateLogApplyId , TemplateApplyStatusEnum templateApplyStatusEnum ,TemplateStatusEnum templateStatusEnum){
        SignTemplateLogApply templateLogApply = templateLogApplyService.getById(templateLogApplyId);
        if(templateLogApply == null){
            errorLog(null,templateLogApplyId,null,null,ApplyErrorEnum.NO_BUSINESS_DATA);
        }
        templateLogApply.setApplyStatus(templateApplyStatusEnum.getCode());
        boolean updateTemplate = templateLogApplyService.updateById(templateLogApply);
        if(!updateTemplate){
            errorLog(null,templateLogApplyId,null,null,ApplyErrorEnum.UPDATE_ERROR);
        }
        SignTemplate signTemplate = templateService.getById(templateLogApply.getTemplateId());
        if(signTemplate == null){
            errorLog(templateLogApply.getTemplateId(),null,null,null,ApplyErrorEnum.NO_BUSINESS_DATA);
        }
        signTemplate.setTemplateStatus(templateStatusEnum.getCode());
        boolean updateTemplateApply = templateService.updateById(signTemplate);
        if(!updateTemplateApply){
            errorLog(templateLogApply.getTemplateId(),null,null,null,ApplyErrorEnum.UPDATE_ERROR);
        }
    }


    public void errorLog(String templateId, String templateApplyId, String processInstanceId, String taskDataId, ApplyErrorEnum applyErrorEnum){

        SignTemplateLogError error = new SignTemplateLogError();
        error.setTemplateId(templateId);
        error.setTemplateLogApplyId(templateApplyId);
        error.setProcessInstanceId(processInstanceId);
        error.setTaskDataId(taskDataId);
        error.setErrorType(applyErrorEnum.getCode());
        error.setErrorText(applyErrorEnum.getName());
        error.setCreateTime(new Date());
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                templateLogErrorService.save(error);
            }
        });
    }




}