/**
 * @description 印章使用申请服务
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

import com.kaifangqian.modules.opensign.entity.SignEntSeal;
import com.kaifangqian.modules.opensign.entity.SignEntSealLogApply;
import com.kaifangqian.modules.opensign.entity.SignEntSealLogError;
import com.kaifangqian.modules.opensign.enums.ApplyErrorEnum;
import com.kaifangqian.modules.opensign.enums.EntSealApplyStatusEnum;
import com.kaifangqian.modules.opensign.enums.EntSealOperateTypeEnum;
import com.kaifangqian.modules.opensign.enums.EntSealStatusEnum;
import com.kaifangqian.modules.opensign.service.seal.SignEntSealLogApplyService;
import com.kaifangqian.modules.opensign.service.seal.SignEntSealLogErrorService;
import com.kaifangqian.modules.opensign.service.seal.SignEntSealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Description: EntSealApplyService
 * @Package: com.kaifangqian.modules.opensign.service.business
 * @ClassName: EntSealApplyService
 * @author: FengLai_Gong
 */
@Service
public class EntSealApplyService {


    @Autowired
    private SignEntSealLogApplyService sealLogApplyService ;
    @Autowired
    private SignEntSealLogErrorService sealLogErrorService ;

    @Autowired
    private SignEntSealService sealService ;
    @Autowired
    private EntSealBusinessService sealBusinessService ;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor ;



    /**
     * @Description #印章制作/章面变更-申请人提交申请
     *               申请人提交后，申请处于待审批状态
     * @Param [sealLogApplyId]
     * @return void
     **/
    public void sealMakeOrChangeSubmit(String sealLogApplyId){
        updateStatus(sealLogApplyId,EntSealApplyStatusEnum.TO_BE_APPROVAL);
    }


    /**
     * @Description #印章制作/章面变更-审批人驳回申请
     *                 申请状态改为待重新提交
     * @Param [sealLogApplyId]
     * @return void
     **/
    public void sealMakeOrChangeReject(String sealLogApplyId){
        updateStatus(sealLogApplyId,EntSealApplyStatusEnum.TO_BE_RE_SUMMIT);

    }

    /**
     * @Description 印章制作-审批通过
     *  *              实现两个操作：（1）申请状态变更为审批通过；（2）印章状态变更为已启用
     * @Param [sealLogApplyId]
     * @return void
     **/
    public void sealMakeSuccessService(String sealLogApplyId){
        updateStatus(sealLogApplyId,EntSealApplyStatusEnum.APPROVAL_SUCCESS,EntSealStatusEnum.ENABLED);
    }

    /**
     * @Description #印章制作/章面变更-申请人撤销申请
     *                  申请状态变更为待重新提交；
     * @Param [sealLogApplyId]
     * @return void
     **/
    public void sealMakeOrChangeRevoke(String sealLogApplyId){
        updateStatus(sealLogApplyId,EntSealApplyStatusEnum.TO_BE_RE_SUMMIT);
    }

    /**
     * @Description #印章制作-申请人作废申请
     *  *            实现两个操作：（1）申请状态变更为作废；（2）印章状态变更为制作失败
     * @Param [sealLogApplyId]
     * @return void
     **/
    public void sealMakeCancel(String sealLogApplyId){
        updateStatus(sealLogApplyId,EntSealApplyStatusEnum.CANCELED,EntSealStatusEnum.MAKE_FAILED);
    }


    /**
     * @Description #印章制作-审批未通过
     *  *              实现两个操作：（1）申请状态变更为审批未通过；（2）印章状态变更为制作失败
     * @Param [sealLogApplyId]
     * @return void
     **/
    public void sealMakeFailed(String sealLogApplyId){
        updateStatus(sealLogApplyId,EntSealApplyStatusEnum.APPROVAL_FAILED,EntSealStatusEnum.MAKE_FAILED);
    }

    /**
     * @Description #章面变更-审批通过
     *              申请状态变更为审批通过；
     * @Param [sealLogApplyId]
     * @return void
     **/
    public void sealChangeSuccess(String sealLogApplyId){
        updateStatus(sealLogApplyId,EntSealApplyStatusEnum.APPROVAL_SUCCESS);
        //更新签章主数据
        sealBusinessService.copy(sealLogApplyId);
    }

    /**
     * @Description #章面变更-审批未通过     申请状态变更为审批未通过；
     * @Param [sealLogApplyId]
     * @return void
     **/
    public void sealChangeFailed(String sealLogApplyId){
        updateStatus(sealLogApplyId,EntSealApplyStatusEnum.APPROVAL_FAILED);

    }

    /**
     * @Description #章面变更-申请人作废申请
     *              申请状态变更为作废；
     * @Param [sealLogApplyId]
     * @return void
     **/
    public void sealChangeCancel(String sealLogApplyId){
        updateStatus(sealLogApplyId,EntSealApplyStatusEnum.CANCELED);
    }

    /**
     * @Description #印章状态变更-审批未通过   申请状态变更为审批未通过；
     * @Param [sealLogApplyId]
     * @return void
     **/
    public void sealStatusChangeFailed(String sealLogApplyId){
        updateStatus(sealLogApplyId,EntSealApplyStatusEnum.APPROVAL_FAILED);
    }


    /**
     * @Description #印章状态变更-申请人作废申请         申请状态变更为作废；
     * @Param [sealLogApplyId]
     * @return void
     **/
    public void sealStatusChangeCancel(String sealLogApplyId){
        updateStatus(sealLogApplyId,EntSealApplyStatusEnum.CANCELED);
    }

    /**
     * @Description #印章状态变更-申请人撤销申请    申请状态变更为待重新提交；
     * @Param [sealLogApplyId]
     * @return void
     **/
    public void sealStatusChangeRevoke(String sealLogApplyId){
        updateStatus(sealLogApplyId,EntSealApplyStatusEnum.TO_BE_RE_SUMMIT);
    }


    /**
     * @Description #印章状态变更-申请人提交申请 申请状态改为待审批
     * @Param [sealLogApplyId]
     * @return void
     **/
    public void sealStatusChangeSubmit(String sealLogApplyId){
        updateStatus(sealLogApplyId,EntSealApplyStatusEnum.TO_BE_APPROVAL);
    }

    /**
     * @Description #印章状态变更（停用/启用/收缴/销毁）-审批通过
     *  *            实现两个操作：（1）申请状态变更为审批通过；（2）印章状态变更
     * @Param [sealLogApplyId]
     * @return void
     **/
    public void sealStatusChangeSuccess(String sealLogApplyId){
        SignEntSealLogApply logApply = updateStatus(sealLogApplyId, EntSealApplyStatusEnum.APPROVAL_SUCCESS);
        if(logApply == null || logApply.getOperateType() == null){
            return;
        }
        if(EntSealOperateTypeEnum.ENABLED.getCode().equals(logApply.getOperateType())){
            //启用
            updateStatus(logApply.getSealId(),EntSealStatusEnum.ENABLED);
        }else if(EntSealOperateTypeEnum.UN_ENABLED.getCode().equals(logApply.getOperateType())){
            //停用
            updateStatus(logApply.getSealId(),EntSealStatusEnum.UN_ENABLED);
        }else if(EntSealOperateTypeEnum.DIVESTED.getCode().equals(logApply.getOperateType())){
            //收缴
            updateStatus(logApply.getSealId(),EntSealStatusEnum.DIVESTED);
        }else if(EntSealOperateTypeEnum.DESTRUCTION.getCode().equals(logApply.getOperateType())){
            //销毁
            updateStatus(logApply.getSealId(),EntSealStatusEnum.DESTRUCTION);
        }

    }







    @Transactional(rollbackFor = Exception.class)
    public SignEntSealLogApply updateStatus(String sealLogApplyId,EntSealApplyStatusEnum entSealApplyStatusEnum){
        SignEntSealLogApply logApply = sealLogApplyService.getById(sealLogApplyId);
        if(logApply == null){
            error(null,logApply.getId(),null,null,ApplyErrorEnum.NO_BUSINESS_DATA);
        }
        logApply.setApplyStatus(entSealApplyStatusEnum.getCode());
        boolean b = sealLogApplyService.updateById(logApply);
        if(!b){
            error(null,logApply.getId(),null,null,ApplyErrorEnum.UPDATE_ERROR);
        }

        return logApply ;
    }

    @Transactional(rollbackFor = Exception.class)
    public SignEntSeal updateStatus(String sealId,EntSealStatusEnum entSealStatusEnum){
        SignEntSeal entSeal = sealService.getById(sealId);
        if(entSeal == null){
            error(entSeal.getId(),null,null,null,ApplyErrorEnum.NO_BUSINESS_DATA);
        }
        entSeal.setSealStatus(entSealStatusEnum.getCode());
        boolean updateSeal = sealService.updateById(entSeal);
        if(!updateSeal){
            error(entSeal.getId(),null,null,null,ApplyErrorEnum.UPDATE_ERROR);
        }
        return entSeal ;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(String sealLogApplyId, EntSealApplyStatusEnum entSealApplyStatusEnum, EntSealStatusEnum entSealStatusEnum){
        SignEntSealLogApply logApply = sealLogApplyService.getById(sealLogApplyId);
        if(logApply == null){
            error(null,logApply.getId(),null,null,ApplyErrorEnum.NO_BUSINESS_DATA);
        }
        logApply.setApplyStatus(entSealApplyStatusEnum.getCode());
        boolean updateLogApply = sealLogApplyService.updateById(logApply);
        if(!updateLogApply){
            error(null,logApply.getId(),null,null,ApplyErrorEnum.UPDATE_ERROR);
        }
        SignEntSeal entSeal = sealService.getById(logApply.getSealId());
        if(entSeal == null){
            error(entSeal.getId(),logApply.getId(),null,null,ApplyErrorEnum.NO_BUSINESS_DATA);
        }
        entSeal.setSealStatus(entSealStatusEnum.getCode());
        boolean updateSeal = sealService.updateById(entSeal);
        if(!updateSeal){
            error(entSeal.getId(),logApply.getId(),null,null,ApplyErrorEnum.UPDATE_ERROR);
        }
    }


//    public void updateError(String param){
//        throw new ApplyUpdateException(param);
//    }


//    public void noDataError(String param){
//        throw new ApplyNoDataException(param);
//
//    }


    public void error(String sealId, String sealApplyId, String processInstanceId, String taskDataId, ApplyErrorEnum applyErrorEnum){
        SignEntSealLogError error = new SignEntSealLogError();
        error.setSealId(sealId);
        error.setSealLogApplyId(sealApplyId);
        error.setProcessInstanceId(processInstanceId);
        error.setTaskDataId(taskDataId);
        error.setErrorType(applyErrorEnum.getCode());
        error.setErrorText(applyErrorEnum.getName());
        error.setCreateTime(new Date());
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                sealLogErrorService.save(error);
            }
        });
    }


}