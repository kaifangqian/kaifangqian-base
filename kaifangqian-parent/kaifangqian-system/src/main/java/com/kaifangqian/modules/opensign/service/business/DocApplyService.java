/**
 * @description 签署操作申请服务
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

import com.kaifangqian.common.util.SpringContextHolder;
import com.kaifangqian.modules.opensign.entity.SignDoc;
import com.kaifangqian.modules.opensign.entity.SignDocLogError;
import com.kaifangqian.modules.opensign.enums.ApplyErrorEnum;
import com.kaifangqian.modules.opensign.enums.DocStatusEnum;
import com.kaifangqian.modules.opensign.service.doc.SignDocLogApplyService;
import com.kaifangqian.modules.opensign.service.doc.SignDocLogErrorService;
import com.kaifangqian.modules.opensign.service.doc.SignDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Description: DocApplyService
 * @Package: com.kaifangqian.modules.opensign.service.business
 * @ClassName: DocApplyService
 * @author: FengLai_Gong
 */
@Service
public class DocApplyService {


    @Autowired
    private SignDocService docService ;
    @Autowired
    private DocBusinessService docBusinessService ;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor ;
    @Autowired
    private SignDocLogApplyService docLogApplyService ;
    @Autowired
    private SignDocLogErrorService docLogErrorService ;


    /**
     * @Description #用印申请-审批未通过
     *  *                  文档状态变更为审批未通过；
     * @Param [docId]
     * @return void
     **/
    public void docApprovalFailed(String docId){

        SignDoc doc = docService.getById(docId);
        if(doc == null){
            errorLog(docId,null,ApplyErrorEnum.NO_BUSINESS_DATA);
        }
        doc.setDocStatus(DocStatusEnum.APPROVAL_FAILED.getCode());
        boolean b = docService.updateById(doc);
        if(!b){
            errorLog(docId,null,ApplyErrorEnum.UPDATE_ERROR);
        }
    }

    /**
     * @Description #用印申请-审批人驳回申请
     *  *                  文档状态变更为待重新发起；
     * @Param [docId]
     * @return void
     **/
    public void docReject(String docId){
        SignDoc doc = docService.getById(docId);
        if(doc == null){
            errorLog(docId,null,ApplyErrorEnum.NO_BUSINESS_DATA);
        }
        doc.setDocStatus(DocStatusEnum.TO_BE_RE_SUMMIT.getCode());
        boolean b = docService.updateById(doc);
        if(!b){
            errorLog(docId,null,ApplyErrorEnum.UPDATE_ERROR);
        }
    }

    /**
     * @Description #用印申请-待签署
     *  *                  文档状态变更为待签署状态；
     * @Param []
     * @return void
     **/
    public void docToBeSign(String docId){
        SignDocService docService = SpringContextHolder.getBean(SignDocService.class);
        SignDoc doc = docService.getById(docId);
        if(doc == null){
            errorLog(docId,null,ApplyErrorEnum.NO_BUSINESS_DATA);
        }
        doc.setDocStatus(DocStatusEnum.TO_BE_SIGN.getCode());
        boolean b = docService.updateById(doc);
        if(!b){
            errorLog(docId,null,ApplyErrorEnum.UPDATE_ERROR);
        }
    }


    public void docAutoSign(String docId){
//        String signDocMainId = docBusinessService.autoSign(docId);
        String signDocMainId = null;
        SignDoc doc = new SignDoc();
        doc.setId(docId);
        if(signDocMainId == null){
            doc.setDocStatus(DocStatusEnum.SIGN_FAILED.getCode());
            boolean b = docService.updateById(doc);
            if(!b){
                errorLog(docId,"签署失败");
            }
        }else {
            doc.setDocStatus(DocStatusEnum.FINISHED.getCode());
            boolean b = docService.updateById(doc);
            if(!b){
                errorLog(docId,null,ApplyErrorEnum.UPDATE_ERROR);
            }
        }
    }


    public void errorLog(String docId,String errorText){
        SignDocLogError error = new SignDocLogError();
        error.setDocId(docId);
        error.setErrorText(errorText);
        error.setCreateTime(new Date());
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                docLogErrorService.save(error);
            }
        });

    }

    public void errorLog(String docId, String processInstanceId, ApplyErrorEnum errorEnum){
//        SignDocLogApply log = new SignDocLogApply();
//        log.setDocId(docId);
//        Date now = new Date();
//        log.setApplyTime(now);
//        log.setApplyStatus(DocStatusEnum.TO_BE_SUMMIT.getCode());
//
//        threadPoolTaskExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
//                docLogApplyService.save(log);
//            }
//        });
        SignDocLogError error = new SignDocLogError();
        error.setDocId(docId);
        error.setProcessInstanceId(processInstanceId);
        error.setErrorType(errorEnum.getCode());
        error.setErrorText(errorEnum.getName());
        error.setCreateTime(new Date());
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                docLogErrorService.save(error);
            }
        });


    }
}