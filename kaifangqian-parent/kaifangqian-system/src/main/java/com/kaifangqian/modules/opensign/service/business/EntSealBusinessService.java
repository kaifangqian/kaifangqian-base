/**
 * @description 印章管理服务
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

import com.kaifangqian.modules.opensign.enums.*;
import com.kaifangqian.modules.system.entity.SysDepart;
import com.kaifangqian.modules.system.entity.SysTenantUser;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.modules.opensign.entity.SignEntSeal;
import com.kaifangqian.modules.opensign.entity.SignEntSealLogApply;
import com.kaifangqian.modules.opensign.enums.*;
import com.kaifangqian.modules.opensign.service.seal.SignEntSealLogApplyService;
import com.kaifangqian.modules.opensign.service.seal.SignEntSealService;
import com.kaifangqian.modules.opensign.service.tool.EntSealGenerateService;
import com.kaifangqian.modules.opensign.service.tool.SignFileService;
import com.kaifangqian.modules.opensign.utils.Base64;
import com.kaifangqian.modules.opensign.vo.base.BusinessAuthVo;
import com.kaifangqian.modules.opensign.vo.base.EntSealOperateVo;
import com.kaifangqian.modules.opensign.vo.request.EntSealChangeRequest;
import com.kaifangqian.modules.opensign.vo.request.EntSealEditRequest;
import com.kaifangqian.modules.opensign.vo.request.EntSealSaveRequest;
import com.kaifangqian.modules.storage.entity.AnnexStorage;
import com.kaifangqian.modules.system.service.ISysDepartService;
import com.kaifangqian.modules.system.service.ISysTenantInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: EntSealBusinessService
 * @Package: com.kaifangqian.modules.opensign.service.business
 * @ClassName: EntSealBusinessService
 * @author: FengLai_Gong
 */
@Service
public class EntSealBusinessService {

    @Autowired
    private SignEntSealService signEntSealService ;
    @Autowired
    private ISysTenantInfoService tenantInfoService ;
    @Autowired
    private SignFileService signFileService;
    @Autowired
    private SignEntSealLogApplyService sealLogApplyService ;
    @Autowired
    private AuthBusinessService authBusinessService ;

    @Autowired
    private ISysDepartService iSysDepartService;
    @Autowired
    private EntSealGenerateService sealGenerateService ;



    @Transactional(rollbackFor = Exception.class)
    public void generateEntDefaultSeal(String topText, SysTenantUser sysTenantUser, String departId){
        String sealBase64 = sealGenerateService.generateEntCircleSeal(topText, "公章", null,true);
        String replace = sealBase64.replace("data:image/png;base64,", "");
        byte[] decode = Base64.decode(replace);
        String annexId = signFileService.saveAnnexStorage(decode, SignFileEnum.SEAL_FILE_ENT, null);
        EntSealSaveRequest request = new EntSealSaveRequest();
        request.setColor(SealColorEnum.RED.getCode());
        request.setDescription("实名认证通过系统自动生成印章");
        request.setSealName(topText);
        request.setMiddleText("公章");
        request.setCreateType(1);
        request.setSealType(1);
        request.setAnnexId(annexId);

//        request
//        request
        BusinessAuthVo businessAuthVo = new BusinessAuthVo();

        businessAuthVo.setAuthType(1);
        businessAuthVo.setAuthRelationId(sysTenantUser.getId());
        businessAuthVo.setAuthRelationName(sysTenantUser.getNickName());
        List<BusinessAuthVo> authVos = new ArrayList<>(1);
        authVos.add(businessAuthVo);

        request.setUserList(authVos);
        request.setManagerList(authVos);
//        request.setAuditorList(authVos);
        this.sysGeneratesave(request,sysTenantUser.getTenantId(),sysTenantUser.getId(),departId);
    }




//    public List<SignEntSeal> getList(String tenantId) {
//
//        List<String> businessIdList = authBusinessService.getBusinessIdList(BusinessAuthType.SEAL);
//        if(businessIdList == null || businessIdList.size() == 0){
//            return null ;
//        }
//        QueryWrapper<SignEntSeal> wrapper = new QueryWrapper<>();
//        wrapper.lambda().eq(SignEntSeal::getSysTenantId,tenantId);
//        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
//        wrapper.lambda().orderByDesc(BaseEntity::getCreateTime);
//        wrapper.lambda().in(SignEntSeal::getId,businessIdList);
//        List<SignEntSeal> signEntSeals = signEntSealService.list(wrapper);
//
//        return signEntSeals;
//    }

    /**
     * @Description #签章制作业务
     * @Param [request]
     * @return java.lang.String
     **/
    public EntSealOperateVo save(EntSealSaveRequest request,String departId){
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        //签章数据
        SignEntSeal signEntSeal = new SignEntSeal();
        BeanUtils.copyProperties(request,signEntSeal);
        signEntSeal.setSysDeptId(currentUser.getDepartId());
        signEntSeal.setDeleteFlag(false);
//        if(request.getSealId() == null || request.getSealId().length() == 0){
//            signEntSeal.setSealStatus(EntSealStatusEnum.MAKING.getCode());
//        }
        signEntSeal.setSealStatus(EntSealStatusEnum.ENABLED.getCode());
        signEntSeal.setId(request.getSealId());
        boolean saveOrUpdateSeal = signEntSealService.saveOrUpdate(signEntSeal);
        if(!saveOrUpdateSeal){
            return null ;
        }
        //维护签章图片文件数据
        //管理者
        authBusinessService.saveSealAuthManagerList(signEntSeal.getId(),request.getManagerList(),departId);
        //使用者
        authBusinessService.saveSealAuthUserList(signEntSeal.getId(),request.getUserList(),departId);
        //审计者
        authBusinessService.saveSealAuthAuditorList(signEntSeal.getId(),request.getAuditorList(),departId);
        //删除原有签章图片数据
        signFileService.deleteAnnexStorage(SignFileEnum.SEAL_FILE_ENT,signEntSeal.getId());
        //更新新签章图片数据
        String annexId = signFileService.updateAnnexStorage(SignFileEnum.SEAL_FILE_ENT, signEntSeal.getId(), request.getAnnexId());
        if(annexId == null){
            return null;
        }
        //签章申请记录数据
        SignEntSealLogApply logApply = new SignEntSealLogApply();
        BeanUtils.copyProperties(request,logApply);
        logApply.setSysDeptId(currentUser.getDepartId());
        logApply.setDeleteFlag(false);
        logApply.setOperateType(EntSealOperateTypeEnum.MAKE.getCode());
        if(request.getSealApplyId() == null || request.getSealApplyId().length() == 0){
            logApply.setCreateTime(null);
            logApply.setCreateBy(null);
            logApply.setApplyStatus(EntSealApplyStatusEnum.TO_BE_SUMMIT.getCode());
        }
        logApply.setId(request.getSealApplyId());
        logApply.setSealId(signEntSeal.getId());

        boolean saveOrUpdateSealLogApply = sealLogApplyService.saveOrUpdate(logApply);
        if(!saveOrUpdateSealLogApply){
            return null;
        }
        //维护申请记录签章图片文件数据
        //删除原有签章图片数据
        signFileService.deleteAnnexStorage(SignFileEnum.SEAL_FILE_ENT,logApply.getId());
        //复制新签章图片数据
        copyAnnexToApply(request.getAnnexId(),logApply.getId());
        //返回
        EntSealOperateVo vo = new EntSealOperateVo();
        vo.setSealApplyId(logApply.getId());
        vo.setSealId(signEntSeal.getId());
        return vo ;
    }

    public EntSealOperateVo sysGeneratesave(EntSealSaveRequest request,String tenantId,String tenantIdUser, String departId){
//        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        List<SysDepart> tenantDept = iSysDepartService.lambdaQuery().eq(SysDepart::getTenantId,tenantId).list();
        //签章数据
        SignEntSeal signEntSeal = new SignEntSeal();
        BeanUtils.copyProperties(request,signEntSeal);
        signEntSeal.setSysDeptId(tenantDept.get(0).getId());
        signEntSeal.setSysOrgCode(tenantDept.get(0).getOrgCode());
        signEntSeal.setDeleteFlag(false);
        signEntSeal.setSysTenantId(tenantId);
//        if(request.getSealId() == null || request.getSealId().length() == 0){
//            signEntSeal.setSealStatus(EntSealStatusEnum.MAKING.getCode());
//        }
        signEntSeal.setSealStatus(EntSealStatusEnum.ENABLED.getCode());
        signEntSeal.setId(request.getSealId());
        boolean saveOrUpdateSeal = signEntSealService.saveOrUpdate(signEntSeal);
        if(!saveOrUpdateSeal){
            return null ;
        }
        //维护签章图片文件数据
        //管理者
        authBusinessService.saveSealAuthManagerList(signEntSeal.getId(),request.getManagerList(),departId);
        //使用者
        authBusinessService.saveSealAuthUserList(signEntSeal.getId(),request.getUserList(),departId);
        //审计者
//        authBusinessService.saveSealAuthAuditorList(signEntSeal.getId(),request.getAuditorList());
        //删除原有签章图片数据
        signFileService.deleteAnnexStorage(SignFileEnum.SEAL_FILE_ENT,signEntSeal.getId());
        //更新新签章图片数据
        String annexId = signFileService.updateAnnexStorage(SignFileEnum.SEAL_FILE_ENT, signEntSeal.getId(), request.getAnnexId());
        if(annexId == null){
            return null;
        }
        //签章申请记录数据
        SignEntSealLogApply logApply = new SignEntSealLogApply();
        BeanUtils.copyProperties(request,logApply);
        logApply.setSysDeptId(tenantDept.get(0).getId());
        logApply.setDeleteFlag(false);
        logApply.setOperateType(EntSealOperateTypeEnum.MAKE.getCode());
        if(request.getSealApplyId() == null || request.getSealApplyId().length() == 0){
            logApply.setCreateTime(null);
            logApply.setCreateBy(null);
            logApply.setApplyStatus(EntSealApplyStatusEnum.TO_BE_SUMMIT.getCode());
        }
        logApply.setSysUserId(tenantIdUser);
        logApply.setSysDeptId(tenantDept.get(0).getId());
        logApply.setSysOrgCode(tenantDept.get(0).getOrgCode());
        logApply.setId(request.getSealApplyId());
        logApply.setSealId(signEntSeal.getId());

        boolean saveOrUpdateSealLogApply = sealLogApplyService.saveOrUpdate(logApply);
        if(!saveOrUpdateSealLogApply){
            return null;
        }
        //维护申请记录签章图片文件数据
        //删除原有签章图片数据
        signFileService.deleteAnnexStorage(SignFileEnum.SEAL_FILE_ENT,logApply.getId());
        //复制新签章图片数据
        copyAnnexToApply(request.getAnnexId(),logApply.getId());
        //返回
        EntSealOperateVo vo = new EntSealOperateVo();
        vo.setSealApplyId(logApply.getId());
        vo.setSealId(signEntSeal.getId());
        return vo ;
    }


    /**
     * @Description #章面变更业务
     * @Param [request]
     * @return java.lang.String
     **/
    public EntSealOperateVo change(EntSealChangeRequest request){
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        //获取原签章数据
        SignEntSeal signEntSeal = signEntSealService.getById(request.getSealId());
        if(signEntSeal == null){
            return null;
        }
        signEntSeal.setCreateType(request.getCreateType());
        signEntSeal.setSealStyle(request.getSealStyle());
        signEntSeal.setColor(request.getColor());
        signEntSeal.setTopText(request.getTopText());
        signEntSeal.setMiddleText(request.getMiddleText());
        signEntSeal.setBottomText(request.getBottomText());
        //变成签章主数据
        boolean updateSeal = signEntSealService.updateById(signEntSeal);
        if(!updateSeal){
            return null ;
        }
        //删除之前的签章
        signFileService.deleteAnnexStorage(SignFileEnum.SEAL_FILE_ENT,signEntSeal.getId());
        //保存新签章
        signFileService.updateAnnexStorage(SignFileEnum.SEAL_FILE_ENT,signEntSeal.getId(),request.getAnnexId());


        //签章申请记录数据
        SignEntSealLogApply logApply = new SignEntSealLogApply();
        if(request.getSealApplyId() == null || request.getSealApplyId().length() == 0){
            BeanUtils.copyProperties(signEntSeal,logApply);
            logApply.setCreateTime(null);
            logApply.setCreateBy(null);
//            logApply.setSealName(signEntSeal.getSealName());
//            logApply.setSealStyle(signEntSeal.getSealStyle());
//            logApply.setSealType(signEntSeal.getSealType());
            logApply.setApplyStatus(EntSealApplyStatusEnum.TO_BE_SUMMIT.getCode());
            logApply.setSealId(signEntSeal.getId());
            logApply.setId(null);
        }else {
            BeanUtils.copyProperties(request,logApply);
            logApply.setId(request.getSealApplyId());
            logApply.setSealId(request.getSealId());
        }
        logApply.setDeleteFlag(false);
        logApply.setSysDeptId(currentUser.getDepartId());
        logApply.setOperateType(EntSealOperateTypeEnum.CHANGE.getCode());

        //更新章面变更申请记录数据
        boolean saveOrUpdateSealLogApply = sealLogApplyService.saveOrUpdate(logApply);
        if(!saveOrUpdateSealLogApply){
            return null;
        }
        if(request.getAnnexId() == null || request.getAnnexId().length() == 0){
            //如果没有图片数据，则复制原签章数据的图片数据
            copySealAnnexToApply(signEntSeal.getId(),logApply.getId());
        }else {
            //删除之前的签章
            signFileService.deleteAnnexStorage(SignFileEnum.SEAL_FILE_ENT,logApply.getId());
            //如果有图片数据
            AnnexStorage requestAnnex = signFileService.getAnnexById(request.getAnnexId());
            if(requestAnnex.getFatherId() == null || requestAnnex.getFatherId().length() == 0){
                //如果是无关联的图片数据，则和签章申请数据建立联系
                //关联新签章
                signFileService.updateAnnexStorage(SignFileEnum.SEAL_FILE_ENT,logApply.getId(),request.getAnnexId());
            }else if(!requestAnnex.getFatherId().equals(logApply.getId())) {
                //如果是已有关联的图片数据，则复制一份
                copyAnnexToApply(request.getAnnexId(),logApply.getId());
            }
        }
        if(request.getAnnexId() == null || request.getAnnexId().length() == 0){
            //如果没有图片数据，则复制原签章数据的图片数据
            copySealAnnexToApply(signEntSeal.getId(),logApply.getId());
        }else {
            //删除之前的签章
            signFileService.deleteAnnexStorage(SignFileEnum.SEAL_FILE_ENT,logApply.getId());
            //如果有图片数据
            AnnexStorage requestAnnex = signFileService.getAnnexById(request.getAnnexId());
            if(requestAnnex.getFatherId() == null || requestAnnex.getFatherId().length() == 0){
                //如果是无关联的图片数据，则和签章申请数据建立联系
                //关联新签章
                signFileService.updateAnnexStorage(SignFileEnum.SEAL_FILE_ENT,logApply.getId(),request.getAnnexId());
            }else if(!requestAnnex.getFatherId().equals(logApply.getId())) {
                //如果是已有关联的图片数据，则复制一份
                copyAnnexToApply(request.getAnnexId(),logApply.getId());
            }
        }
        EntSealOperateVo vo = new EntSealOperateVo();
        vo.setSealId(signEntSeal.getId());
        vo.setSealApplyId(logApply.getId());
        return vo ;
    }

    public void copySealAnnexToApply(String sealId, String sealApplyId){
        //获取印章文件id
        AnnexStorage sealAnnex = signFileService.findByFatherIdAndDataCategory(SignFileEnum.SEAL_FILE_ENT, sealId);
        if(sealAnnex == null){
            return ;
        }
        //复制签章文件数据
        AnnexStorage newAnnex = new AnnexStorage();
        BeanUtils.copyProperties(sealAnnex,newAnnex);
        newAnnex.setDeleteFlag(false);
        newAnnex.setFatherId(sealApplyId);
        newAnnex.setId(null);
        //新生成一条文件数据，关联到印章申请记录
        signFileService.saveAnnexStorage(newAnnex);
    }


    public void copyAnnexToApply(String annexId, String sealApplyId){
        //获取印章文件id
        AnnexStorage sealAnnex = signFileService.getAnnexById(annexId);
        if(sealAnnex == null){
            return ;
        }
        //复制签章文件数据
        AnnexStorage newAnnex = new AnnexStorage();
        BeanUtils.copyProperties(sealAnnex,newAnnex);
        newAnnex.setDeleteFlag(false);
        newAnnex.setFatherId(sealApplyId);
        newAnnex.setId(null);
        //新生成一条文件数据，关联到印章申请记录
        signFileService.saveAnnexStorage(newAnnex);
    }


    public String edit(EntSealEditRequest request,String departId){
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        SignEntSeal entSeal = signEntSealService.getById(request.getId());
        if(entSeal == null){
            return null ;
        }
        entSeal.setAdminId(request.getAdminId());
        entSeal.setSealName(request.getSealName());
        entSeal.setSealType(request.getSealType());
        if(request.getDescription() != null && request.getDescription().length() > 0){
            entSeal.setDescription(request.getDescription());
        }
        boolean b = signEntSealService.updateById(entSeal);
        if(!b){
            return null ;
        }
        //管理者
        authBusinessService.saveSealAuthManagerList(entSeal.getId(),request.getManagerList(),departId);
        //使用者
        authBusinessService.saveSealAuthUserList(entSeal.getId(),request.getUserList(),departId);
        //审计者
        authBusinessService.saveSealAuthAuditorList(entSeal.getId(),request.getAuditorList(),departId);
        SignEntSealLogApply sealLogApply = new SignEntSealLogApply();
        BeanUtils.copyProperties(entSeal,sealLogApply);

        sealLogApply.setSealId(entSeal.getId());
        sealLogApply.setDeleteFlag(false);
        sealLogApply.setSysDeptId(currentUser.getDepartId());
        sealLogApply.setOperateType(EntSealOperateTypeEnum.EDIT.getCode());
        sealLogApply.setApplyStatus(EntSealApplyStatusEnum.APPROVAL_SUCCESS.getCode());
        sealLogApply.setId(null);
        sealLogApply.setCreateTime(null);
        sealLogApply.setCreateBy(null);
        boolean save = sealLogApplyService.save(sealLogApply);
        if(!save){
            return null ;
        }
        return entSeal.getId() ;
    }

    /**
     * @Description #章面变更业务完成后复制更新印章主数据
     * @Param [sealLogApplyId]
     * @return java.lang.String
     **/
    public String copy(String sealLogApplyId){
        //获取签章申请记录数据
        SignEntSealLogApply sealLogApply = sealLogApplyService.getById(sealLogApplyId);
        if(sealLogApply == null || sealLogApply.getSealId() == null){
            return null ;
        }
        //获取签章主数据
        SignEntSeal entSeal = signEntSealService.getById(sealLogApply.getSealId());
        if(entSeal == null){
            return null ;
        }
        entSeal.setCreateType(sealLogApply.getCreateType());
        entSeal.setColor(sealLogApply.getColor());
        entSeal.setTopText(sealLogApply.getTopText());
        entSeal.setMiddleText(sealLogApply.getMiddleText());
        entSeal.setBottomText(sealLogApply.getBottomText());
        //变成签章主数据
        boolean updateSeal = signEntSealService.updateById(entSeal);
        if(!updateSeal){
            return null ;
        }
        AnnexStorage source = signFileService.findByFatherIdAndDataCategory(SignFileEnum.SEAL_FILE_ENT, sealLogApply.getId());
        if(source == null){
            return null ;
        }
        AnnexStorage target = new AnnexStorage();
        BeanUtils.copyProperties(source,target);
        target.setDeleteFlag(false);
        target.setFatherId(entSeal.getId());
        target.setId(null);

        //删除之前的签章
        signFileService.deleteAnnexStorage(SignFileEnum.SEAL_FILE_ENT,entSeal.getId());
        //保存新签章
        signFileService.saveAnnexStorage(target);

        return entSeal.getId() ;
    }




    public String createLogApply(SignEntSeal signEntSeal, EntSealApplyStatusEnum applyStatusEnum, EntSealOperateTypeEnum operateTypeEnum){
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        SignEntSealLogApply sealLogApply = new SignEntSealLogApply();
        BeanUtils.copyProperties(signEntSeal,sealLogApply);

        sealLogApply.setSealId(signEntSeal.getId());
        sealLogApply.setDeleteFlag(false);
        sealLogApply.setSysDeptId(currentUser.getDepartId());
        sealLogApply.setApplyStatus(applyStatusEnum.getCode());
        sealLogApply.setOperateType(operateTypeEnum.getCode());
        sealLogApply.setId(null);
        sealLogApply.setDescription(null);
        sealLogApply.setCreateTime(null);
        sealLogApply.setCreateBy(null);
        boolean save = sealLogApplyService.save(sealLogApply);
        if(!save){
            return null ;
        }
        //复制签章数据
        copySealAnnexToApply(signEntSeal.getId(),sealLogApply.getId());
        return sealLogApply.getId() ;
    }

    /**
     * @Description #启用
     * @Param [signEntSeal]
     * @return java.lang.String
     **/
    public String enabled(SignEntSeal signEntSeal){

        return createLogApply(signEntSeal,EntSealApplyStatusEnum.INIT,EntSealOperateTypeEnum.ENABLED);
    }


    /**
     * @Description #停用
     * @Param [signEntSeal]
     * @return java.lang.String
     **/
    public String unenabled(SignEntSeal signEntSeal){
        return createLogApply(signEntSeal,EntSealApplyStatusEnum.INIT,EntSealOperateTypeEnum.UN_ENABLED);
    }

    /**
     * @Description #收缴
     * @Param [signEntSeal]
     * @return java.lang.String
     **/
    public String divested(SignEntSeal signEntSeal){
        return createLogApply(signEntSeal,EntSealApplyStatusEnum.INIT,EntSealOperateTypeEnum.DIVESTED);
    }

    /**
     * @Description #销毁
     * @Param [signEntSeal]
     * @return java.lang.String
     **/
    public String destruction(SignEntSeal signEntSeal){
        return createLogApply(signEntSeal,EntSealApplyStatusEnum.INIT,EntSealOperateTypeEnum.DESTRUCTION);
    }





}