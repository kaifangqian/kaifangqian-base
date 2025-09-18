/**
 * @description 签署业务流程服务
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

//import com.kaifangqian.modules.activiti.enums.OperateType;
//import com.kaifangqian.modules.activiti.service.IProcessService;
//import com.kaifangqian.modules.activiti.service.TaskBusinessService;
//import com.kaifangqian.modules.activiti.vo.ProcessOpVO;
import org.springframework.stereotype.Service;

/**
 * @Description: ActivitBusinessService
 * @Package: com.kaifangqian.modules.opensign.service.business
 * @ClassName: ActivitBusinessService
 * @author: FengLai_Gong
 */
@Service
public class ActivitiBusinessService {


    public static final String MESSAGE_STOP = "业务调用流程终止";
    public static final String MESSAGE_REVOKE = "业务调用流程撤销";
    public static final String MESSAGE_DELETE = "业务调用流程删除";

//
//    @Autowired
//    private TaskBusinessService taskBusinessService;
//    @Autowired
//    private IProcessService iProcessService;

    /**
     * @Description #流程删除
     * @Param [businessId]
     * @return void
     **/
    public void delete(String businessId){
//        String proRecId = taskBusinessService.getProRecIdByMainDataId(businessId);
//        if(proRecId == null || proRecId.length() == 0){
//            throw new ActivitiNoDataException("流程实例数据不存在");
//        }
//        ProcessOpVO vo = new ProcessOpVO();
//        vo.setProcessRecordId(proRecId);
//        vo.setMessage(MESSAGE_DELETE);
//        vo.setOperateType(OperateType.PROCESS_DELETE.getValue());
//        iProcessService.delete(vo);
    }

    /**
     * @Description #流程终止
     * @Param [businessId]
     * @return void
     **/
    public void stop(String businessId){
//        String proRecId = taskBusinessService.getProRecIdByMainDataId(businessId);
//        if(proRecId == null || proRecId.length() == 0){
//            throw new ActivitiNoDataException("流程实例数据不存在");
//        }
//        ProcessOpVO vo = new ProcessOpVO();
//        vo.setProcessRecordId(proRecId);
//        vo.setMessage(MESSAGE_STOP);
//        vo.setOperateType(OperateType.PROCESS_STOP.getValue());
//        iProcessService.stop(vo);
    }


    /**
     * @Description #流程撤销
     * @Param [businessId]
     * @return void
     **/
//    public void revoke(String businessId){
//        String proRecId = taskBusinessService.getProRecIdByMainDataId(businessId);
//        if(proRecId == null || proRecId.length() == 0){
//            throw new ActivitiNoDataException("流程实例数据不存在");
//        }
//        ProcessOpVO vo = new ProcessOpVO();
//        vo.setProcessRecordId(proRecId);
//        vo.setMessage(MESSAGE_STOP);
//        vo.setOperateType(OperateType.PROCESS_REVOKE.getValue());
//        iProcessService.stop(vo);
//    }



}