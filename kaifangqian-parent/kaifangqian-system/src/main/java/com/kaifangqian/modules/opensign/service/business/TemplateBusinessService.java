/**
 * @description 模板管理业务逻辑
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

import com.kaifangqian.modules.opensign.entity.SignTemplate;
import com.kaifangqian.modules.opensign.entity.SignTemplateAuth;
import com.kaifangqian.modules.opensign.entity.SignTemplateControl;
import com.kaifangqian.modules.opensign.entity.SignTemplateRecord;
import com.kaifangqian.modules.opensign.enums.*;
import com.kaifangqian.modules.opensign.service.template.*;
import com.kaifangqian.modules.opensign.vo.base.TemplateAuthVo;
import com.kaifangqian.modules.opensign.vo.base.TemplateControlVo;
import com.kaifangqian.modules.opensign.vo.base.TemplateOperateVo;
import com.kaifangqian.modules.opensign.vo.base.TemplateVo;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.opensign.entity.*;
import com.kaifangqian.modules.opensign.enums.*;
import com.kaifangqian.modules.opensign.service.template.*;
import com.kaifangqian.modules.opensign.service.tool.SignFileService;
import com.kaifangqian.modules.opensign.vo.base.*;
import com.kaifangqian.modules.opensign.vo.base.sign.DocImageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: templateBusinssService
 * @Package: com.kaifangqian.modules.opensign.service.business
 * @ClassName: templateBusinssService
 * @author: FengLai_Gong
 */
@Service
public class TemplateBusinessService {

    @Autowired
    private SignTemplateService templateService ;
//    @Autowired
//    private SignTemplateLogApplyService templateLogApplyService ;
    @Autowired
    private SignTemplateControlService templateControlService;
    @Autowired
    private SignTemplateRecordService templateRecordService ;
    @Autowired
    private SignTemplateImageRecordService templateImageRecordService ;
    @Autowired
    private SignTemplateImageConvertService templateImageConvertService;
    @Autowired
    private SignTemplateFolderService templateFolderService ;
    @Autowired
    private SignTemplateFolderRelationService templateFolderRelationService ;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private SignFileService signFileService ;

    @Autowired
    private SignTemplateAuthService templateAuthService ;

    @Autowired
    private AuthBusinessService authBusinessService ;



//    public List<ImageConvertVo> convertImage(String templateId , String annexId){
//
//        List<ImageConvertVo> imageConvertVos = convertImageService.convertImage(annexId);
//        if(imageConvertVos != null && imageConvertVos.size() > 0){
//            List<SignTemplateImageConvert> convertList = new ArrayList<>();
//            for(ImageConvertVo vo : imageConvertVos){
//                SignTemplateImageConvert convert = new SignTemplateImageConvert();
//                convert.setTemplateId(templateId);
//                convert.setAnnexId(vo.getImageAnnexId());
//                convert.setDeleteFlag(false);
//                convert.setPage(vo.getPage());
//                convertList.add(convert);
//            }
//            threadPoolTaskExecutor.execute(new Runnable() {
//                @Override
//                public void run() {
//                    templateImageConvertService.saveBatch(convertList);
//                }
//            });
//        }
//
//        return imageConvertVos ;
//    }


    public void saveControlList(String templateId ,List<TemplateControlVo> controlVoList){


        List<SignTemplateControl> insertList = new ArrayList<>();
        if(controlVoList != null && controlVoList.size() > 0){
            //先删除
            templateControlService.delete(templateId);
            //再插入
            for(int i = 0 ; i < controlVoList.size() ; i++){
                TemplateControlVo vo = controlVoList.get(i);
                SignTemplateControl control = new SignTemplateControl();
                BeanUtils.copyProperties(vo,control);
                control.setDeleteFlag(false);
                control.setTemplateId(templateId);
                control.setControlOrder(i);
                insertList.add(control);
            }
        }
        if(insertList.size() > 0){
            templateControlService.saveBatch(insertList);
        }

    }


    /**
     * @Description #模板保存
     * @Param [request]
     * @return com.kaifangqian.modules.opensign.vo.base.TemplateOperateVo
     **/
    public TemplateOperateVo save(TemplateVo request){
        TemplateOperateVo vo = new TemplateOperateVo();
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        SignTemplate signTemplate = new SignTemplate();
        //更新或者插入模板主数据
        BeanUtils.copyProperties(request,signTemplate);
//        if(request.getTemplateId() == null || request.getTemplateId().length() == 0){
//            signTemplate.setTemplateStatus(TemplateStatusEnum.MAKING.getCode());
//        }
        if(request.getSubmitFlag() == 1){
            signTemplate.setTemplateStatus(TemplateStatusEnum.ENABLED.getCode());
        }else {
            signTemplate.setTemplateStatus(TemplateStatusEnum.MAKING.getCode());
        }
        signTemplate.setDeleteFlag(false);
        signTemplate.setSysDeptId(currentUser.getDepartId());
        signTemplate.setId(request.getTemplateId());
        boolean updateTemplate = templateService.saveOrUpdate(signTemplate);
        if(!updateTemplate){
            return null;
        }
//        //更新或者插入模板申请记录主数据
//        SignTemplateLogApply templateLogApply = new SignTemplateLogApply();
//        BeanUtils.copyProperties(request,templateLogApply);
//        if(request.getTemplateApplyId() == null || request.getTemplateApplyId().length() == 0){
//            templateLogApply.setApplyStatus(TemplateApplyStatusEnum.TO_BE_SUMMIT.getCode());
//            templateLogApply.setOperateType(TemplateOperateTypeEnum.MAKE.getCode());
//            templateLogApply.setApplyTime(new Date());
//        }
//        templateLogApply.setDeleteFlag(false);
//        templateLogApply.setSysDeptId(currentUser.getDepartId());
//        templateLogApply.setTemplateId(signTemplate.getId());
//        templateLogApply.setId(request.getTemplateApplyId());
//        boolean updateTemplateApplyLog = templateLogApplyService.saveOrUpdate(templateLogApply);
//        if(!updateTemplateApplyLog){
//            return null;
//        }
        //权限数据
//        if(request.getAuthVoList() != null && request.getAuthVoList().size() > 0){
//            saveAuth(request.getAuthVoList(),signTemplate.getId(),currentUser.getTenantId());
//        }
        if(request.getManagerList() != null && request.getManagerList().size() > 0){
            authBusinessService.saveTemplateManageList(signTemplate.getId(),request.getManagerList(),"");
        }
        if(request.getUserList() != null && request.getUserList().size() > 0){
            authBusinessService.saveTemplateUserList(signTemplate.getId(),request.getUserList(),"");
        }


//        vo.setTemplateApplyId(templateLogApply.getId());
        vo.setTemplateId(signTemplate.getId());
        //如果没有上传文件则直接返回了
        if(request.getAnnexId() == null || request.getAnnexId().length() == 0){
            return vo;
        }
        //如果上传的文件和之前一致，无需更新，直接返回
        SignTemplateRecord templateRecordCurrent = templateRecordService.getCurrent(signTemplate.getId());
        if(templateRecordCurrent != null && templateRecordCurrent.getAnnexId() != null && templateRecordCurrent.getAnnexId().equals(request.getAnnexId())){
            return vo;
        }
        //模板操作记录数据
        String newTemplateRecordId = saveNewRecord(signTemplate.getId(), request.getAnnexId());
        //转换图片
        List<DocImageVo> voList = signFileService.saveAndConvertImage(request.getAnnexId(), null);
        if(voList != null && voList.size() > 0){
            SignTemplateRecord record = new SignTemplateRecord();
            record.setId(newTemplateRecordId);
            record.setDocPage(voList.size());
            templateRecordService.updateById(record);
        }
        //模板申请记录数据
        //复制文件数据
//        String newAnnexId = signFileService.copyAnnexStorage(request.getAnnexId());
//        String newTemplateApplyRecordId = saveNewRecord(templateLogApply.getId(), newAnnexId);
//        //文件转图片
//        List<ImageConvertVo> imageConvertVos = convertImageService.convertImage(request.getAnnexId());
//        if(imageConvertVos != null && imageConvertVos.size() > 0){
//            //更新之前的图片数据
//            templateImageRecordService.updateNotCurrent(signTemplate.getId());
////            templateImageRecordService.updateNotCurrent(templateLogApply.getId());
//            for(ImageConvertVo convert : imageConvertVos){
//                threadPoolTaskExecutor.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        //插入图片数据
//                        saveNewImageRecord(signTemplate.getId(),newTemplateRecordId,convert.getImageAnnexId(),convert.getPage());
//                    }
//                });
//                threadPoolTaskExecutor.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        //复制新图片数据
//                        String newAnnexId = signFileService.copyAnnexStorage(convert.getImageAnnexId());
//                        //插入图片数据
////                        saveNewImageRecord(templateLogApply.getId(),newTemplateApplyRecordId,newAnnexId,convert.getPage());
//                    }
//                });
//            }
//        }
//        threadPoolTaskExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
//                templateControlService.delete(signTemplate.getId());
//                templateControlService.delete(templateLogApply.getId());
//            }
//        });


        return vo ;
    }

    public void saveAuth(List<TemplateAuthVo> authVoList, String templateId, String tenantId){
        if(authVoList != null && authVoList.size() > 0){
            //删除之前的权限数据
            templateAuthService.deleteByTemplateId(templateId);
            //新增权限数据
            Boolean noManagerFlag = true ;
            List<SignTemplateAuth> authList = new ArrayList<>();
            for(TemplateAuthVo authVo : authVoList){
                SignTemplateAuth auth = new SignTemplateAuth();
                auth.setAuthType(authVo.getAuthType());
                auth.setTemplateId(templateId);
                auth.setTenantId(tenantId);
                auth.setTenantUserId(authVo.getTenantUserId());
                auth.setDeleteFlag(false);
                authList.add(auth);
                if(authVo.getAuthType().equals(SignTemplateAuthTypeEnum.MANAGER.getCode())){
                    noManagerFlag = false ;
                }
            }
            if(noManagerFlag){
                SignTemplateAuth auth = new SignTemplateAuth();
                auth.setAuthType(SignTemplateAuthTypeEnum.MANAGER.getCode());
                auth.setTemplateId(templateId);
                auth.setTenantId(tenantId);
                auth.setTenantUserId(MySecurityUtils.getCurrentUser().getTenantUserId());
                auth.setDeleteFlag(false);
                authList.add(auth);
            }
            boolean b = templateAuthService.saveBatch(authList);
            if(!b){
                throw new PaasException("保存权限失败");
            }

        }

    }


    public String saveNewRecord(String templateId,String annexId){
        //更新旧的模板记录数据
        templateRecordService.setNotCurrent(templateId);
        //创建模板记录数据
        SignTemplateRecord record = new SignTemplateRecord();
        record.setAnnexId(annexId);
        record.setTemplateId(templateId);
        record.setSysDeptId(MySecurityUtils.getCurrentUser().getDepartId());
        record.setIsCurrent(SignCurrentEnum.IS_CURRENT.getCode());
        record.setOperateStatus(SignTemplateOperateRecordEnum.INIT.getCode());
        record.setOperateTime(new Date());
        record.setDeleteFlag(false);
        boolean saverRecord = templateRecordService.save(record);
        if(!saverRecord){
            return null;
        }
        //维护模板主文件和文档记录的关系
        signFileService.updateAnnexStorage(SignFileEnum.SIGN_FILE_MAIN, record.getId(), annexId);
        return record.getId() ;
    }

//    public String saveNewImageRecord(String templateId,String recordId ,String annexId,Integer page){
//        SignTemplateImageRecord imageRecord = new SignTemplateImageRecord();
//        imageRecord.setTemplateRecordId(recordId);
//        imageRecord.setTemplateId(templateId);
//        imageRecord.setAnnexId(annexId);
//        imageRecord.setTemplatePage(page);
//        imageRecord.setIsCurrent(SignCurrentEnum.IS_CURRENT.getCode());
//        imageRecord.setDeleteFlag(false);
//        boolean save = templateImageRecordService.save(imageRecord);
//        if(!save){
//            return null ;
//        }
//        return imageRecord.getId();
//    }


    /**
     * @Description #模板变更
     * @Param [request]
     * @return com.kaifangqian.modules.opensign.vo.base.TemplateOperateVo
     **/
    public TemplateOperateVo change(TemplateVo request){
        TemplateOperateVo vo = new TemplateOperateVo();
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        SignTemplate signTemplate = new SignTemplate();
        BeanUtils.copyProperties(request,signTemplate);
        signTemplate.setId(request.getTemplateId());
        boolean b = templateService.updateById(signTemplate);
        if(!b){
            throw new PaasException("操作失败");
        }
        vo.setTemplateId(signTemplate.getId());
        //更新或者插入模板申请记录主数据
//        SignTemplateLogApply templateLogApply = new SignTemplateLogApply();
//        BeanUtils.copyProperties(request,templateLogApply);
//        if(request.getTemplateApplyId() == null || request.getTemplateApplyId().length() == 0){
//            SignTemplate signTemplate = templateService.getById(request.getTemplateId());
//            if(signTemplate == null){
//                return null ;
//            }
//            BeanUtils.copyProperties(signTemplate,templateLogApply);
//            templateLogApply.setApplyStatus(TemplateApplyStatusEnum.INIT.getCode());
//            templateLogApply.setOperateType(TemplateOperateTypeEnum.CHANGE.getCode());
//            templateLogApply.setApplyTime(new Date());
//        }
//        templateLogApply.setDeleteFlag(false);
//        templateLogApply.setSysDeptId(currentUser.getDepartId());
//        templateLogApply.setTemplateId(request.getTemplateId());
//        templateLogApply.setId(request.getTemplateApplyId());
//        boolean updateTemplateApplyLog = templateLogApplyService.saveOrUpdate(templateLogApply);
//        if(!updateTemplateApplyLog){
//            return null;
//        }
//        //如果是第一次变更,复制控件数据
//        if(request.getTemplateApplyId() == null || request.getTemplateApplyId().length() == 0){
//            threadPoolTaskExecutor.execute(new Runnable() {
//                @Override
//                public void run() {
//                    List<SignTemplateControl> list = templateControlService.getList(request.getTemplateId());
//                    if(list != null && list.size() > 0){
//                        List<SignTemplateControl> controlsApplyList = new ArrayList<>();
//                        for(SignTemplateControl control : list){
//                            SignTemplateControl controlApply = new SignTemplateControl();
//                            BeanUtils.copyProperties(control,controlApply);
//                            controlApply.setTemplateId(templateLogApply.getId());
//                            controlApply.setId(null);
//                            controlApply.setDeleteFlag(false);
//
//                            controlsApplyList.add(controlApply);
//                        }
//                        templateControlService.saveBatch(controlsApplyList);
//                    }
//                }
//            });
//
//        }
//        vo.setTemplateApplyId(templateLogApply.getId());
//        //如果上传的文件和之前一致，无需更新，直接返回
        if(request.getAnnexId() == null || request.getAnnexId().length() == 0){
            return vo;
        }
        SignTemplateRecord templateRecordCurrent = templateRecordService.getCurrent(signTemplate.getId());
        if(templateRecordCurrent != null && templateRecordCurrent.getAnnexId() != null && templateRecordCurrent.getAnnexId().equals(request.getAnnexId())){
            return vo;
        }
//        //如果没有上传文件则直接返回了
//        String newAnnexId = null ;
//        if(request.getAnnexId() == null || request.getAnnexId().length() == 0){
//            SignTemplateRecord current = templateRecordService.getCurrent(request.getTemplateId());
//            if(current == null || current.getAnnexId() == null){
//                return null ;
//            }
//            newAnnexId = signFileService.copyAnnexStorage(current.getAnnexId());
//        }else {
//            AnnexStorage annex = signFileService.getAnnexById(request.getAnnexId());
//            if(annex != null && annex.getFatherId() != null && annex.getFatherId().length() > 0){
//                newAnnexId = signFileService.copyAnnexStorage(request.getAnnexId());
//            }else {
//                newAnnexId = request.getAnnexId();
//            }
//        }
        //模板申请记录数据
        String newTemplateRecordId = saveNewRecord(templateRecordCurrent.getId(), request.getAnnexId());
        //文件转图片
        //转换图片
        List<DocImageVo> voList = signFileService.saveAndConvertImage(request.getAnnexId(), null);
        if(voList != null && voList.size() > 0){
            SignTemplateRecord record = new SignTemplateRecord();
            record.setId(newTemplateRecordId);
            record.setDocPage(voList.size());
            templateRecordService.updateById(record);
        }
//        List<ImageConvertVo> imageConvertVos = convertImageService.convertImage(newAnnexId);
//        if(imageConvertVos != null && imageConvertVos.size() > 0){
//            //更新之前的图片数据
//            templateImageRecordService.updateNotCurrent(templateLogApply.getId());
//            for(ImageConvertVo convert : imageConvertVos){
//                threadPoolTaskExecutor.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        //插入图片数据
//                        saveNewImageRecord(templateLogApply.getId(),newTemplateApplyRecordId,convert.getImageAnnexId(),convert.getPage());
//                    }
//                });
//            }
//        }
//        threadPoolTaskExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
//                templateControlService.delete(templateLogApply.getId());
//            }
//        });


        return vo ;
    }


//    public String enabled(SignTemplate signTemplate){
//        return createTemplateApplyLog(signTemplate,TemplateOperateTypeEnum.ENABLED,TemplateApplyStatusEnum.TO_BE_SUMMIT) ;
//
//    }


//    public String unenabled(SignTemplate signTemplate){
//        return createTemplateApplyLog(signTemplate,TemplateOperateTypeEnum.UN_ENABLED,TemplateApplyStatusEnum.TO_BE_SUMMIT) ;
//    }

//    public String createTemplateApplyLog(SignTemplate signTemplate,TemplateOperateTypeEnum operateTypeEnum,TemplateApplyStatusEnum applyStatusEnum){
//        LoginUser currentUser = MySecurityUtils.getCurrentUser();
//        SignTemplateLogApply templateLogApply = new SignTemplateLogApply();
//        BeanUtils.copyProperties(signTemplate,templateLogApply);
//        templateLogApply.setApplyTime(new Date());
//        templateLogApply.setApplyStatus(applyStatusEnum.getCode());
//        templateLogApply.setOperateType(operateTypeEnum.getCode());
//        templateLogApply.setDeleteFlag(false);
//        templateLogApply.setSysDeptId(currentUser.getDepartId());
//        templateLogApply.setId(null);
//        templateLogApply.setTemplateId(signTemplate.getId());
//        boolean save = templateLogApplyService.save(templateLogApply);
//        if(!save){
//            return null ;
//        }
//        //复制关联性数据
//        copyRelationData(signTemplate.getId(),templateLogApply.getId());
//        return templateLogApply.getId() ;
//    }
//
//    public void copyRelationData(String templateId,String templateApplyId){
//        //文件记录数据
//        SignTemplateRecord recordCurrent = templateRecordService.getCurrent(templateId);
//        if(recordCurrent == null || recordCurrent.getAnnexId() == null){
//            return;
//        }
//        String newAnnexId = signFileService.copyAnnexStorage(recordCurrent.getAnnexId());
//        SignTemplateRecord applyRecordCurrent = new SignTemplateRecord();
//        applyRecordCurrent.setIsCurrent(SignCurrentEnum.IS_CURRENT.getCode());
//        applyRecordCurrent.setDeleteFlag(false);
//        applyRecordCurrent.setTemplateId(templateApplyId);
//        applyRecordCurrent.setSysDeptId(MySecurityUtils.getCurrentUser().getDepartId());
//        applyRecordCurrent.setAnnexId(newAnnexId);
//        boolean save = templateRecordService.save(applyRecordCurrent);
//        if(!save){
//            return;
//        }
//        //图片数据
//        threadPoolTaskExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
//                List<SignTemplateImageRecord> currentList = templateImageRecordService.getCurrentList(templateId);
//                if(currentList != null && currentList.size() > 0){
//                    List<SignTemplateImageRecord> applyImageRecordList = new ArrayList<>();
//                    for(SignTemplateImageRecord imageRecord : currentList){
//                        String newAnnexId = signFileService.copyAnnexStorage(imageRecord.getAnnexId());
//                        SignTemplateImageRecord applyImageRecord = new SignTemplateImageRecord();
//                        applyImageRecord.setDeleteFlag(false);
//                        applyImageRecord.setTemplateId(templateApplyId);
//                        applyImageRecord.setTemplateRecordId(applyRecordCurrent.getId());
//                        applyImageRecord.setIsCurrent(SignCurrentEnum.IS_CURRENT.getCode());
//                        applyImageRecord.setAnnexId(newAnnexId);
//                        applyImageRecord.setTemplatePage(imageRecord.getTemplatePage());
//                        applyImageRecordList.add(applyImageRecord);
//                    }
//                    templateImageRecordService.saveBatch(applyImageRecordList);
//                }
//
//            }
//        });
//
//        //控件数据
//        threadPoolTaskExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
//                List<SignTemplateControl> list = templateControlService.getList(templateId);
//                if(list != null && list.size() > 0){
//                    List<SignTemplateControl> applyList = new ArrayList<>();
//                    for(SignTemplateControl control : list){
//                        SignTemplateControl applyControl = new SignTemplateControl();
//                        BeanUtils.copyProperties(control,applyControl);
//                        applyControl.setTemplateId(templateApplyId);
//                        applyControl.setId(null);
//                        applyList.add(applyControl);
//                    }
//                    templateControlService.saveBatch(applyList);
//                }
//            }
//        });
//
//    }
//
//    public void copyApplyToTemplate(String templateApplyId){
//
//        SignTemplateLogApply templateLogApply = templateLogApplyService.getById(templateApplyId);
//        if(templateLogApply == null){
//            return;
//        }
//        SignTemplate signTemplate = templateService.getById(templateLogApply.getTemplateId());
//        if(signTemplate == null){
//            return;
//        }
//        signTemplate.setTemplateName(templateLogApply.getTemplateName());
//        signTemplate.setTemplateCode(templateLogApply.getTemplateCode());
//        signTemplate.setTemplateType(templateLogApply.getTemplateType());
//        signTemplate.setBusinessType(templateLogApply.getBusinessType());
//        signTemplate.setSealId(templateLogApply.getSealId());
//        signTemplate.setNote(templateLogApply.getNote());
//        //更新模板主数据
//        boolean updateTemplate = templateService.updateById(signTemplate);
//        if(!updateTemplate){
//            return;
//        }
//
//        //文件记录数据
//        SignTemplateRecord applyRecordCurrent = templateRecordService.getCurrent(templateApplyId);
//        if(applyRecordCurrent == null || applyRecordCurrent.getAnnexId() == null){
//            return;
//        }
//
//        Boolean setNotCurrent = templateRecordService.setNotCurrent(signTemplate.getId());
//        if(!setNotCurrent){
//            return;
//        }
//        String newAnnexId = signFileService.copyAnnexStorage(applyRecordCurrent.getAnnexId());
//        SignTemplateRecord recordCurrent = new SignTemplateRecord();
//        recordCurrent.setIsCurrent(SignCurrentEnum.IS_CURRENT.getCode());
//        recordCurrent.setDeleteFlag(false);
//        recordCurrent.setTemplateId(signTemplate.getId());
//        recordCurrent.setSysDeptId(MySecurityUtils.getCurrentUser().getDepartId());
//        recordCurrent.setAnnexId(newAnnexId);
//        boolean save = templateRecordService.save(recordCurrent);
//        if(!save){
//            return;
//        }
//        //图片数据
//        threadPoolTaskExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
//                List<SignTemplateImageRecord> currentList = templateImageRecordService.getCurrentList(templateApplyId);
//                if(currentList != null && currentList.size() > 0){
//                    List<SignTemplateImageRecord> imageRecordList = new ArrayList<>();
//                    for(SignTemplateImageRecord applyImageRecord : currentList){
//                        String newAnnexId = signFileService.copyAnnexStorage(applyImageRecord.getAnnexId());
//                        SignTemplateImageRecord imageRecord = new SignTemplateImageRecord();
//                        imageRecord.setDeleteFlag(false);
//                        imageRecord.setTemplateId(signTemplate.getId());
//                        imageRecord.setTemplateRecordId(recordCurrent.getId());
//                        imageRecord.setIsCurrent(SignCurrentEnum.IS_CURRENT.getCode());
//                        imageRecord.setAnnexId(newAnnexId);
//                        imageRecord.setTemplatePage(imageRecord.getTemplatePage());
//                        imageRecordList.add(imageRecord);
//                    }
//                    templateImageRecordService.saveBatch(imageRecordList);
//                }
//
//            }
//        });
//
//        //控件数据
//        threadPoolTaskExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
//                List<SignTemplateControl> list = templateControlService.getList(templateApplyId);
//                if(list != null && list.size() > 0){
//                    List<SignTemplateControl> controlList = new ArrayList<>();
//                    for(SignTemplateControl applyControl : list){
//                        SignTemplateControl control = new SignTemplateControl();
//                        BeanUtils.copyProperties(applyControl,control);
//                        control.setTemplateId(signTemplate.getId());
//                        control.setId(null);
//                        controlList.add(control);
//                    }
//                    templateControlService.saveBatch(controlList);
//                }
//            }
//        });
//
//    }


//    public String createTemplate(TemplateVo request){
//        LoginUser currentUser = MySecurityUtils.getCurrentUser();
//        SignTemplate signTemplate = new SignTemplate();
//        //更新模板主数据
//        BeanUtils.copyProperties(request,signTemplate);
//        signTemplate.setTemplateStatus(1);
//        signTemplate.setDeleteFlag(false);
//        signTemplate.setSysDeptId(currentUser.getDepartId());
//        boolean updateTemplate = templateService.save(signTemplate);
//        if(!updateTemplate){
//            return null;
//        }
//        if(request.getAnnexId() == null || request.getAnnexId().length() == 0){
//            return signTemplate.getId();
//        }
//        //创建模板记录数据
//        SignTemplateRecord record = new SignTemplateRecord();
//        record.setAnnexId(request.getAnnexId());
//        record.setTemplateId(signTemplate.getId());
//        record.setSysDeptId(currentUser.getDepartId());
//        record.setIsCurrent(SignCurrentEnum.IS_CURRENT.getCode());
//        record.setOperateStatus(SignTemplateOperateRecordEnum.INIT.getCode());
//        record.setOperateTime(new Date());
//        record.setDeleteFlag(false);
//        boolean saverRecord = templateRecordService.save(record);
//        if(!saverRecord){
//            return null;
//        }
//        //维护模板主文件和文档记录的关系
//        signFileService.updateAnnexStorage(SignFileEnum.SIGN_FILE_MAIN, record.getId(), request.getAnnexId());
//        //维护文档记录关联的图片数据
//        List<ImageConvertVo> imageConvertVos = convertImageService.convertImage(request.getAnnexId());
//        if(imageConvertVos != null && imageConvertVos.size() > 0){
//            List<SignTemplateImageRecord> imageRecordList = new ArrayList<>();
//            for(ImageConvertVo convert : imageConvertVos){
//                SignTemplateImageRecord imageRecord = new SignTemplateImageRecord();
//                imageRecord.setTemplateRecordId(record.getId());
//                imageRecord.setTemplateId(signTemplate.getId());
//                imageRecord.setAnnexId(convert.getImageAnnexId());
//                imageRecord.setTemplatePage(convert.getPage());
//                imageRecord.setIsCurrent(SignCurrentEnum.IS_CURRENT.getCode());
//                imageRecord.setDeleteFlag(false);
//                imageRecordList.add(imageRecord);
//            }
//            boolean saveImageRecordList = templateImageRecordService.saveBatch(imageRecordList);
//            if(!saveImageRecordList){
//                return null ;
//            }
//        }
//        return signTemplate.getId() ;
//    }
//
//
//    public String updateTemplate(TemplateVo request ,SignTemplate signTemplate){
//        LoginUser currentUser = MySecurityUtils.getCurrentUser();
//        //更新模板主数据
//        BeanUtils.copyProperties(request,signTemplate);
//        boolean updateTemplate = templateService.updateById(signTemplate);
//        if(!updateTemplate){
//            return null;
//        }
//        if(request.getAnnexId() == null || request.getAnnexId().length() == 0){
//            return signTemplate.getId();
//        }
//        //获取最新模板记录
//        SignTemplateRecord currentRecord = templateRecordService.getCurrent(signTemplate.getId());
//        if(currentRecord != null && currentRecord.getAnnexId() != null && currentRecord.getAnnexId().equals(request.getAnnexId())){
//            //如果文件没有变更，则直接返回
//            return signTemplate.getId();
//        }
//        //更新旧的记录数据
//        templateRecordService.setNotCurrent(signTemplate.getId());
//        //创建模板记录数据
//        SignTemplateRecord record = new SignTemplateRecord();
//        record.setAnnexId(request.getAnnexId());
//        record.setTemplateId(signTemplate.getId());
//        record.setSysDeptId(currentUser.getDepartId());
//        record.setIsCurrent(SignCurrentEnum.IS_CURRENT.getCode());
//        record.setOperateStatus(SignTemplateOperateRecordEnum.INIT.getCode());
//        record.setOperateTime(new Date());
//        record.setDeleteFlag(false);
//        boolean saverRecord = templateRecordService.save(record);
//        if(!saverRecord){
//            return null;
//        }
//        //维护模板主文件和文档记录的关系
//        signFileService.updateAnnexStorage(SignFileEnum.SIGN_FILE_MAIN, record.getId(), request.getAnnexId());
//        //维护文档记录关联的图片数据
//        //获取临时文件转换图片关联数据
//        List<ImageConvertVo> imageConvertVos = convertImageService.convertImage(request.getAnnexId());
//        if(imageConvertVos != null && imageConvertVos.size() > 0){
//            List<SignTemplateImageRecord> imageRecordList = new ArrayList<>();
//            for(ImageConvertVo convert : imageConvertVos){
//                SignTemplateImageRecord imageRecord = new SignTemplateImageRecord();
//                imageRecord.setTemplateRecordId(record.getId());
//                imageRecord.setTemplateId(signTemplate.getId());
//                imageRecord.setAnnexId(convert.getImageAnnexId());
//                imageRecord.setTemplatePage(convert.getPage());
//                imageRecord.setIsCurrent(SignCurrentEnum.IS_CURRENT.getCode());
//                imageRecord.setDeleteFlag(false);
//                imageRecordList.add(imageRecord);
//            }
//            //更新之前的图片数据
//            templateImageRecordService.updateNotCurrent(signTemplate.getId());
//            //插入新的图片数据
//            boolean saveImageRecordList = templateImageRecordService.saveBatch(imageRecordList);
//            if(!saveImageRecordList){
//                return null ;
//            }
//        }
//        threadPoolTaskExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
//                templateControlService.delete(signTemplate.getId());
//            }
//        });
//
//
//        return signTemplate.getId() ;
//    }



}