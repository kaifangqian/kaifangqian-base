/**
 * @description 电子印章-印章管理
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
package com.kaifangqian.modules.opensign.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kaifangqian.modules.opensign.enums.*;
import com.kaifangqian.modules.opensign.vo.request.*;
import com.kaifangqian.modules.system.entity.SysTenantInfo;
import com.kaifangqian.modules.system.entity.SysTenantUser;
import com.kaifangqian.modules.system.vo.TenantInfoDTO;
import com.kaifangqian.annotation.ResrunLogModule;
import com.kaifangqian.common.base.entity.BaseEntity;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.modules.opensign.entity.SignBusinessAuth;
import com.kaifangqian.modules.opensign.entity.SignBusinessAuthPermission;
import com.kaifangqian.modules.opensign.entity.SignEntSeal;
import com.kaifangqian.modules.opensign.entity.SignEntSealLogApply;
import com.kaifangqian.modules.opensign.enums.*;
import com.kaifangqian.modules.opensign.service.business.ActivitiBusinessService;
import com.kaifangqian.modules.opensign.service.business.AuthBusinessService;
import com.kaifangqian.modules.opensign.service.business.EntSealBusinessService;
import com.kaifangqian.modules.opensign.service.seal.SignEntSealLogApplyService;
import com.kaifangqian.modules.opensign.service.seal.SignEntSealLogOperateService;
import com.kaifangqian.modules.opensign.service.seal.SignEntSealService;
import com.kaifangqian.modules.opensign.service.tool.EntSealGenerateService;
import com.kaifangqian.modules.opensign.service.tool.SignFileService;
import com.kaifangqian.modules.opensign.utils.Base64;
import com.kaifangqian.modules.opensign.vo.base.BusinessAuthOperateVo;
import com.kaifangqian.modules.opensign.vo.base.EntSealOperateVo;
import com.kaifangqian.modules.opensign.vo.request.*;
import com.kaifangqian.modules.opensign.vo.response.EntSealInfoResponse;
import com.kaifangqian.modules.opensign.vo.response.EntSealListResponse;
import com.kaifangqian.modules.opensign.vo.response.EntSealLogListResponse;
import com.kaifangqian.modules.storage.entity.AnnexStorage;
import com.kaifangqian.modules.system.service.ISysTenantInfoService;
import com.kaifangqian.modules.system.service.ISysTenantUserService;
import com.kaifangqian.modules.system.service.ITenantInfoExtendService;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: SignEntSealController
 * @Package: com.kaifangqian.modules.opensign.controller
 * @ClassName: SignEntSealController
 * @author: FengLai_Gong
 * @Date: 2022/11/07 15:05
 */
@Slf4j
@RestController
@RequestMapping("/sign/ent/seal")
@ResrunLogModule(name = "印章管理")
// @Api(tags = "电子印章-印章管理")
public class SignEntSealController {

    @Autowired
    private SignEntSealService signEntSealService ;
    @Autowired
    private SignEntSealLogApplyService sealLogApplyService ;
    @Autowired
    private SignEntSealLogOperateService sealLogOperateService ;
    @Autowired
    private EntSealGenerateService sealGenerateService ;

    @Autowired
    private ISysTenantInfoService tenantInfoService ;
    @Autowired
    private ITenantInfoExtendService tenantInfoExtendService ;
    @Autowired
    private SignFileService signFileService;

    @Autowired
    private EntSealBusinessService sealBusinessService ;

    @Autowired
    private ISysTenantUserService tenantUserService ;
    @Autowired
    private ActivitiBusinessService activitiBusinessService ;
    @Autowired
    private AuthBusinessService authBusinessService ;

    // @ApiOperation("用户中心-我的印章-印章列表")
    @RequestMapping(value = "/mine/list",method = RequestMethod.GET)
    public Result<List<EntSealListResponse>> mineList(){
        List<EntSealListResponse> responseList = new ArrayList<>();
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        //构建查询条件
        //必须包含是印章使用者
        List<Integer> businessTypeRoleList = new ArrayList<>();
        businessTypeRoleList.add(BusinessAuthTypeRole.SEAL_USER.getCode());
        List<String> businessIdList = authBusinessService.getBusinessIdList(BusinessAuthType.SEAL,businessTypeRoleList);
        if(businessIdList == null || businessIdList.size() == 0){
            return Result.OK(responseList);
        }
        //租户隔离
        QueryWrapper<SignEntSeal> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignEntSeal::getSysTenantId,currentUser.getTenantId());
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        wrapper.lambda().orderByDesc(BaseEntity::getCreateTime);
        wrapper.lambda().in(SignEntSeal::getId,businessIdList);
        List<SignEntSeal> list = signEntSealService.list(wrapper);

        if(list == null || list.size() == 0){
            return Result.OK(responseList);
        }
        for(SignEntSeal entSeal : list){
            EntSealListResponse response = new EntSealListResponse();
            response.setSealId(entSeal.getId());
            response.setSealName(entSeal.getSealName());
            response.setStatus(entSeal.getStatus());
            response.setSealStatus(entSeal.getSealStatus());
            AnnexStorage annexStorage = signFileService.findByFatherIdAndDataCategory(SignFileEnum.SEAL_FILE_ENT, entSeal.getId());
            if(annexStorage != null){
                response.setAnnexId(annexStorage.getId());
            }
            responseList.add(response);
        }
        return Result.OK(responseList);
    }




    // @ApiOperation("印章管理-印章业务权限列表")
    @GetMapping("/auth/list")
    @ResponseBody
    public Result<List<BusinessAuthOperateVo>> authList(@RequestParam("businessRelationId") String businessRelationId) {
        List<BusinessAuthOperateVo> returnList = new ArrayList<>();
        //获取签章业务权限
        List<SignBusinessAuthPermission> sealAuthList = authBusinessService.getPermissionList(BusinessAuthType.SEAL,businessRelationId);
        if(sealAuthList != null && sealAuthList.size() > 0){
            for(SignBusinessAuthPermission permission : sealAuthList){
                BusinessAuthOperateVo vo = new BusinessAuthOperateVo();
                vo.setOperateName(permission.getPermissionValue());
                vo.setOperateCode(permission.getPermissionCode());
                returnList.add(vo);
            }
        }
        return Result.OK(returnList);
    }



    // @ApiOperation("印章管理-印章列表")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public Result<List<EntSealListResponse>> list(){
        List<EntSealListResponse> responseList = new ArrayList<>();
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        //构建查询条件
        //必须包含是印章使用者
        List<Integer> businessTypeRoleList = new ArrayList<>();
        businessTypeRoleList.add(BusinessAuthTypeRole.SEAL_MANAGER.getCode());
        businessTypeRoleList.add(BusinessAuthTypeRole.SEAL_AUDITOR.getCode());
        List<String> businessIdList = authBusinessService.getBusinessIdList(BusinessAuthType.SEAL,businessTypeRoleList);
        if(businessIdList == null || businessIdList.size() == 0){
            return Result.OK(responseList);
        }
        //租户隔离
        QueryWrapper<SignEntSeal> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignEntSeal::getSysTenantId,currentUser.getTenantId());
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        wrapper.lambda().orderByDesc(BaseEntity::getCreateTime);
        wrapper.lambda().in(SignEntSeal::getId,businessIdList);

        List<SignEntSeal> list = signEntSealService.list(wrapper);
        if(list == null || list.size() == 0){
            return Result.OK(responseList);
        }
        for(SignEntSeal entSeal : list){
            EntSealListResponse response = new EntSealListResponse();
            response.setSealId(entSeal.getId());
            response.setSealName(entSeal.getSealName());
            response.setStatus(entSeal.getStatus());
            response.setSealStatus(entSeal.getSealStatus());
            AnnexStorage annexStorage = signFileService.findByFatherIdAndDataCategory(SignFileEnum.SEAL_FILE_ENT, entSeal.getId());
            if(annexStorage != null){
                response.setAnnexId(annexStorage.getId());
            }
            responseList.add(response);
        }
        return Result.OK(responseList);
    }


    // @ApiOperation("印章详情")
    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public Result<EntSealInfoResponse> info(@RequestParam(name="sealId") String sealId){

        if(sealId == null){
            return Result.error("印章不存在",null);
        }

//        Integer authIdentify = authBusinessService.authIdentify(sealId);
//        if(authIdentify == null || authIdentify == 0){
//            return Result.error("没有权限",null);
//        }

        SignEntSeal entSeal = signEntSealService.getById(sealId);
        if(entSeal == null || entSeal.getDeleteFlag()){
            return Result.error("印章不存在",null);
        }

        EntSealInfoResponse response = new EntSealInfoResponse();
        response.setCreateTime(entSeal.getCreateTime());
        response.setSealId(entSeal.getId());
        response.setSealName(entSeal.getSealName());
        response.setSealType(entSeal.getSealType());
        response.setSealStyle(entSeal.getSealStyle());
        response.setCreateType(entSeal.getCreateType());
        response.setSealStatus(entSeal.getSealStatus());
        response.setDescription(entSeal.getDescription());
        response.setMiddleText(entSeal.getMiddleText());
        response.setBottomText(entSeal.getBottomText());
        response.setAdminId(entSeal.getAdminId());
        if(entSeal.getAdminId() != null){
            SysTenantUser tenantUser = tenantUserService.getById(entSeal.getAdminId());
            if(tenantUser != null && tenantUser.getNickName() != null){
                response.setAdminName(tenantUser.getNickName());
            }
        }
        AnnexStorage annex = signFileService.findByFatherIdAndDataCategory(SignFileEnum.SEAL_FILE_ENT, entSeal.getId());
        if(annex != null){
            response.setAnnexId(annex.getId());
        }
        response.setStatus(entSeal.getStatus());

        List<SignBusinessAuth> businessAuthList = authBusinessService.getAuthListByBusinessId(entSeal.getId(), BusinessAuthType.SEAL);
        if(businessAuthList != null && businessAuthList.size() > 0){
            Map<Integer, List<SignBusinessAuth>> collect = businessAuthList.stream().collect(Collectors.groupingBy(SignBusinessAuth::getBusinessTypeRole));
            response.setManagerList(authBusinessService.convertVo(collect.get(BusinessAuthTypeRole.SEAL_MANAGER.getCode())));
            response.setUserList(authBusinessService.convertVo(collect.get(BusinessAuthTypeRole.SEAL_USER.getCode())));
            response.setAuditorList(authBusinessService.convertVo(collect.get(BusinessAuthTypeRole.SEAL_AUDITOR.getCode())));
        }

        return Result.OK(response) ;
    }

    // @ApiOperation("印章详情")
    @RequestMapping(value = "/info/apply",method = RequestMethod.GET)
    public Result<EntSealInfoResponse> infoApply(@RequestParam(name = "sealLogApplyId") String sealLogApplyId){



        SignEntSealLogApply sealLogApply = sealLogApplyService.getById(sealLogApplyId);
        if(sealLogApply == null || sealLogApply.getDeleteFlag()){
            return Result.error("印章不存在",null);
        }

        Integer authIdentify = authBusinessService.authIdentify(sealLogApply.getSealId());
        if(authIdentify == null || authIdentify == 0){
            return Result.error("没有权限",null);
        }

        EntSealInfoResponse response = new EntSealInfoResponse();
        response.setCreateTime(sealLogApply.getCreateTime());
        response.setSealApplyId(sealLogApply.getId());
        response.setSealId(sealLogApply.getSealId());
        response.setSealName(sealLogApply.getSealName());
        response.setSealType(sealLogApply.getSealType());
        response.setCreateType(sealLogApply.getCreateType());
        response.setDescription(sealLogApply.getDescription());
        response.setMiddleText(sealLogApply.getMiddleText());
        response.setBottomText(sealLogApply.getBottomText());
        response.setAdminId(sealLogApply.getAdminId());

        response.setApplyStatus(sealLogApply.getApplyStatus());
        response.setApplyDescription(sealLogApply.getDescription());
        response.setOperateType(sealLogApply.getOperateType());
        response.setSealStyle(sealLogApply.getSealStyle());
        //原因
        response.setReason(sealLogApply.getReason());
        //权限数据
        List<SignBusinessAuth> businessAuthList = authBusinessService.getAuthListByBusinessId(sealLogApply.getSealId(), BusinessAuthType.SEAL);
        if(businessAuthList != null && businessAuthList.size() > 0){
            Map<Integer, List<SignBusinessAuth>> collect = businessAuthList.stream().collect(Collectors.groupingBy(SignBusinessAuth::getBusinessTypeRole));
            response.setManagerList(authBusinessService.convertVo(collect.get(BusinessAuthTypeRole.SEAL_MANAGER.getCode())));
            response.setUserList(authBusinessService.convertVo(collect.get(BusinessAuthTypeRole.SEAL_USER.getCode())));
            response.setAuditorList(authBusinessService.convertVo(collect.get(BusinessAuthTypeRole.SEAL_AUDITOR.getCode())));
        }


        if(sealLogApply.getAdminId() != null){
            SysTenantUser tenantUser = tenantUserService.getById(sealLogApply.getAdminId());
            if(tenantUser != null && tenantUser.getNickName() != null){
                response.setAdminName(tenantUser.getNickName());
            }
        }


        AnnexStorage annex = signFileService.findByFatherIdAndDataCategory(SignFileEnum.SEAL_FILE_ENT, sealLogApply.getId());
        if(annex != null){
            response.setAnnexId(annex.getId());
        }
        return Result.OK(response) ;
    }



    // @ApiOperation("印章新增或保存")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Result<?> save(@RequestBody EntSealSaveRequest request){

        if(request == null || request.getSealBase64() == null ){
            return Result.error("参数缺失");
        }
        if(request.getSealId() != null && request.getSealId().length() > 0){
            //权限
            Integer authIdentify = authBusinessService.authIdentify(request.getSealId());
            if(authIdentify == null || authIdentify == 0 || authIdentify > BusinessAuthTypeRole.SEAL_MANAGER.getCode()){
                return Result.error("没有权限",null);
            }
        }
        byte[] decode = Base64.decode(request.getSealBase64());
        String annexId = signFileService.saveAnnexStorage(decode, SignFileEnum.SEAL_FILE_ENT, null);
        if(annexId == null || annexId.length() == 0){
            return Result.error("操作失败");
        }
        request.setAnnexId(annexId);


        EntSealOperateVo save = sealBusinessService.save(request,"");
        if(save == null){
            return Result.error("操作失败");
        }
        return Result.OK(save) ;

    }


    // @ApiOperation("印章编辑")
    @RequestMapping(value = "/edit",method = RequestMethod.PUT)
    public Result<?> edit(@RequestBody EntSealEditRequest request){

        if(request == null || request.getId() == null){
            return Result.error("参数缺失");
        }
        if(request.getId() != null && request.getId().length() > 0){
            //权限
            Integer authIdentify = authBusinessService.authIdentify(request.getId());
            if(authIdentify == null || authIdentify == 0 || authIdentify > BusinessAuthTypeRole.SEAL_MANAGER.getCode()){
                return Result.error("没有权限",null);
            }
        }

        String sealId = sealBusinessService.edit(request,"");
        if(sealId == null){
            return Result.error("操作失败");
        }
        EntSealOperateVo build = EntSealOperateVo.builder().sealId(sealId).build();
        return Result.OK(build) ;

    }


    // @ApiOperation("印章章面变更")
    @RequestMapping(value = "/change",method = RequestMethod.PUT)
    public Result<?> change(@RequestBody EntSealChangeRequest request){

        if(request == null || request.getSealId() == null || request.getSealId().length() == 0){
            return Result.error("参数缺失");
        }
        //权限
        Integer authIdentify = authBusinessService.authIdentify(request.getSealId());
        if(authIdentify == null || authIdentify == 0 || authIdentify > BusinessAuthTypeRole.SEAL_MANAGER.getCode()){
            return Result.error("没有权限",null);
        }
        byte[] decode = Base64.decode(request.getSealBase64());
        String annexId = signFileService.saveAnnexStorage(decode, SignFileEnum.SEAL_FILE_ENT, null);
        if(annexId == null || annexId.length() == 0){
            return Result.error("操作失败");
        }
        request.setAnnexId(annexId);

        EntSealOperateVo vo = sealBusinessService.change(request);

        if(vo == null){
            return Result.error("操作失败");
        }
        return Result.OK(vo) ;
    }


    // @ApiOperation("印章启用")
    @RequestMapping(value = "/enabled",method = RequestMethod.PUT)
    public Result<?> enable(@RequestBody EntSealEnabledRequest request){

        SignEntSeal signEntSeal = signEntSealService.getById(request.getSealId());
        if(signEntSeal == null){
            return Result.error("签章不存在");
        }
        //权限
        Integer authIdentify = authBusinessService.authIdentify(request.getSealId());
        if(authIdentify == null || authIdentify == 0 || authIdentify > BusinessAuthTypeRole.SEAL_MANAGER.getCode()){
            return Result.error("没有权限",null);
        }
        Boolean updateStatus = signEntSealService.updateStatus(EntSealStatusEnum.ENABLED, null, request.getSealId());
        if(!updateStatus){
            return Result.error("操作失败");
        }
        String sealLogApplyId = sealBusinessService.enabled(signEntSeal);
        if(sealLogApplyId == null){
            return Result.error("操作失败");
        }
        EntSealOperateVo build = EntSealOperateVo.builder().sealApplyId(sealLogApplyId).sealId(signEntSeal.getId()).build();
        return Result.OK(build) ;
//        signEntSeal.setSealStatus(EntSealStatusEnum.ENABLED.getCode());
//        boolean b = signEntSealService.updateById(signEntSeal);
//        if(!b){
//            return Result.error("操作失败");
//        }
//        EntSealOperateVo build = EntSealOperateVo.builder().sealId(signEntSeal.getId()).build();
//        return Result.OK(build) ;

    }

    // @ApiOperation("印章停用")
    @RequestMapping(value = "/unenabled",method = RequestMethod.PUT)
    public Result<?> unenabled(@RequestBody EntSealUnEnabledRequest request){

        SignEntSeal signEntSeal = signEntSealService.getById(request.getSealId());
        if(signEntSeal == null){
            return Result.error("签章不存在");
        }
        //权限
        Integer authIdentify = authBusinessService.authIdentify(request.getSealId());
        if(authIdentify == null || authIdentify == 0 || authIdentify > BusinessAuthTypeRole.SEAL_MANAGER.getCode()){
            return Result.error("没有权限",null);
        }
        Boolean updateStatus = signEntSealService.updateStatus(EntSealStatusEnum.UN_ENABLED, null, request.getSealId());
        if(!updateStatus){
            return Result.error("操作失败");
        }
        String sealLogApplyId = sealBusinessService.unenabled(signEntSeal);
        if(sealLogApplyId == null){
            return Result.error("操作失败");
        }
        EntSealOperateVo build = EntSealOperateVo.builder().sealApplyId(sealLogApplyId).sealId(signEntSeal.getId()).build();
        return Result.OK(build) ;
//        signEntSeal.setSealStatus(EntSealStatusEnum.UN_ENABLED.getCode());
//        boolean b = signEntSealService.updateById(signEntSeal);
//        if(!b){
//            return Result.error("操作失败");
//        }
//        EntSealOperateVo build = EntSealOperateVo.builder().sealId(signEntSeal.getId()).build();
//        return Result.OK(build) ;

    }

    // @ApiOperation("印章收缴")
    @RequestMapping(value = "/divested",method = RequestMethod.PUT)
    public Result<?> divested(@RequestBody EntSealDivestedRequest request){

        SignEntSeal signEntSeal = signEntSealService.getById(request.getSealId());
        if(signEntSeal == null){
            return Result.error("签章不存在");
        }
        //权限
        Integer authIdentify = authBusinessService.authIdentify(request.getSealId());
        if(authIdentify == null || authIdentify == 0 || authIdentify > BusinessAuthTypeRole.SEAL_MANAGER.getCode()){
            return Result.error("没有权限",null);
        }
        Boolean updateStatus = signEntSealService.updateStatus(EntSealStatusEnum.DIVESTED, null, request.getSealId());
        if(!updateStatus){
            return Result.error("操作失败");
        }
        String sealLogApplyId = sealBusinessService.divested(signEntSeal);
        if(sealLogApplyId == null){
            return Result.error("操作失败");
        }
        EntSealOperateVo build = EntSealOperateVo.builder().sealApplyId(sealLogApplyId).sealId(signEntSeal.getId()).build();
        return Result.OK(build) ;
//        signEntSeal.setSealStatus(EntSealStatusEnum.DIVESTED.getCode());
//        boolean b = signEntSealService.updateById(signEntSeal);
//        if(!b){
//            return Result.error("操作失败");
//        }
//        EntSealOperateVo build = EntSealOperateVo.builder().sealId(signEntSeal.getId()).build();
//        return Result.OK(build) ;
    }

    // @ApiOperation("印章销毁")
    @RequestMapping(value = "/destruction",method = RequestMethod.PUT)
    public Result<?> destruction(@RequestBody EntSealDestructionRequest request){

        SignEntSeal signEntSeal = signEntSealService.getById(request.getSealId());
        if(signEntSeal == null){
            return Result.error("签章不存在");
        }
        //权限
        Integer authIdentify = authBusinessService.authIdentify(request.getSealId());
        if(authIdentify == null || authIdentify == 0 || authIdentify > BusinessAuthTypeRole.SEAL_MANAGER.getCode()){
            return Result.error("没有权限",null);
        }
        Boolean updateStatus = signEntSealService.updateStatus(EntSealStatusEnum.DESTRUCTION, null, request.getSealId());
        if(!updateStatus){
            return Result.error("操作失败");
        }
        String sealLogApplyId = sealBusinessService.destruction(signEntSeal);
        if(sealLogApplyId == null){
            return Result.error("操作失败");
        }
        EntSealOperateVo build = EntSealOperateVo.builder().sealApplyId(sealLogApplyId).sealId(signEntSeal.getId()).build();
        return Result.OK(build) ;
//        signEntSeal.setSealStatus(EntSealStatusEnum.DESTRUCTION.getCode());
//        boolean b = signEntSealService.updateById(signEntSeal);
//        if(!b){
//            return Result.error("操作失败");
//        }
//        EntSealOperateVo build = EntSealOperateVo.builder().sealId(signEntSeal.getId()).build();
//        return Result.OK(build) ;
    }


    // @ApiOperation("印章状态修改原因")
    @RequestMapping(value = "/reason",method = RequestMethod.PUT)
    public Result<?> reason(@RequestBody EntSealReasonRequest request){

        if(request == null || request.getSealApplyId() == null || request.getDescription() == null){
            return Result.error("参数缺失");
        }

        SignEntSealLogApply logApply = sealLogApplyService.getById(request.getSealApplyId());
        if(logApply == null){
            return Result.error("申请记录不存在");
        }
        //权限
        Integer authIdentify = authBusinessService.authIdentify(logApply.getSealId());
        if(authIdentify == null || authIdentify == 0 || authIdentify > BusinessAuthTypeRole.SEAL_MANAGER.getCode()){
            return Result.error("没有权限",null);
        }
//        logApply.setDescription(request.getDescription());
        //停用原因
        logApply.setReason(request.getDescription());
        boolean b = sealLogApplyService.updateById(logApply);
        if(!b){
            return Result.error("操作失败");
        }
        return Result.OK() ;
    }


    // @ApiOperation("印章日志列表")
    @RequestMapping(value = "/log/list",method = RequestMethod.GET)
    public Result<List<EntSealLogListResponse>> logList(@RequestParam("sealId") String sealId, @RequestParam("operateType") List<Integer> operateTypeList){

        List<EntSealLogListResponse> responseList = new ArrayList<>();

        //权限
        Integer authIdentify = authBusinessService.authIdentify(sealId);
        if(authIdentify == null || authIdentify == 0 || authIdentify == BusinessAuthTypeRole.SEAL_USER.getCode()){
            return Result.OK(responseList) ;
        }

        QueryWrapper<SignEntSealLogApply> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        wrapper.lambda().eq(SignEntSealLogApply::getSealId,sealId);
        wrapper.lambda().in(SignEntSealLogApply::getOperateType,operateTypeList);
//        wrapper.lambda().ne(SignEntSealLogApply::getApplyStatus, EntSealApplyStatusEnum.INIT.getCode());

        wrapper.lambda().orderByDesc(BaseEntity::getCreateTime);

        List<SignEntSealLogApply> list = sealLogApplyService.list(wrapper);
        if(list == null || list.size() == 0){
            return Result.OK(responseList) ;
        }
        List<String> tenantUserIdList = list.stream().map(SignEntSealLogApply::getSysUserId).collect(Collectors.toList());
        List<SysTenantUser> sysTenantUsers = tenantUserService.listByIds(tenantUserIdList);
        Map<String, String> userMap = null ;
        if(sysTenantUsers != null && sysTenantUsers.size() > 0){
            userMap = sysTenantUsers.stream().collect(Collectors.toMap(SysTenantUser::getId, SysTenantUser::getNickName));
        }
        for(SignEntSealLogApply logApply : list){
            EntSealLogListResponse response = new EntSealLogListResponse();
            BeanUtils.copyProperties(logApply,response);
            if(userMap != null && userMap.containsKey(logApply.getSysUserId())){
                String name = userMap.get(logApply.getSysUserId());
                response.setApplierName(name);
            }
            if(logApply.getCreateTime() != null){
                response.setApplyTime(logApply.getCreateTime());
            }
            responseList.add(response);
        }
        return Result.OK(responseList) ;
    }


    // @ApiOperation("印章日志删除")
    @RequestMapping(value = "/log/delete",method = RequestMethod.DELETE)
    public Result<?> logDelete(@RequestParam("sealApplyId") String sealApplyId){

        SignEntSealLogApply logApply = sealLogApplyService.getById(sealApplyId);
        if(logApply == null || logApply.getDeleteFlag() || logApply.getSealId() == null || logApply.getSealId().length() == 0){
            return Result.error("申请记录不存在");
        }
        //权限
        Integer authIdentify = authBusinessService.authIdentify(logApply.getSealId());
        if(authIdentify == null || authIdentify == 0 || authIdentify > BusinessAuthTypeRole.SEAL_MANAGER.getCode()){
            return Result.error("没有权限",null);
        }

        if(logApply.getApplyStatus().equals(EntSealApplyStatusEnum.TO_BE_APPROVAL.getCode())){
            return Result.error("印章申请状态为" + EntSealApplyStatusEnum.TO_BE_APPROVAL.getName() + "，不能删除");
        }
        if(logApply.getApplyStatus().equals(EntSealApplyStatusEnum.APPROVAL_SUCCESS.getCode())){
            return Result.error("印章申请状态为" + EntSealApplyStatusEnum.APPROVAL_SUCCESS.getName() + "，不能删除");
        }
        logApply.setDeleteFlag(true);
        boolean deleteLogApply = sealLogApplyService.updateById(logApply);
        if(!deleteLogApply){
            return Result.error("操作失败");
        }
        SignEntSeal entSeal = signEntSealService.getById(logApply.getSealId());
        if(entSeal == null){
            return Result.error("印章数据不存在");
        }
        if(logApply.getOperateType().equals(EntSealOperateTypeEnum.MAKE.getCode())){
            entSeal.setDeleteFlag(true);
            boolean deleteSeal = signEntSealService.updateById(entSeal);
            if(!deleteSeal){
                return Result.error("操作失败");
            }
        }
        //删除流程
        activitiBusinessService.delete(logApply.getId());
        return Result.OK() ;
    }


    // @ApiOperation("印章生成-上传生成")
    @RequestMapping(value = "/generate/upload",method = RequestMethod.POST)
    public Result<?> generateUpload(@RequestBody EntSealGenerateUploadRequest request){

        if(request.getImage() == null || request.getImage().length() == 0){
            return Result.error("参数缺失");
        }
        byte[] decode = Base64.decode(request.getImage());
        if(decode == null || decode.length == 0){
            return Result.error("参数缺失");
        }

        byte[] bytes = sealGenerateService.clipEntSeal(decode, request.getColorRange());
        if(bytes == null){
            return Result.error("操作失败");
        }


//        String annexId = signFileService.saveAnnexStorage(bytes, SignFileEnum.SEAL_FILE_ENT, null);
//        if(annexId == null){
//            return Result.error("操作失败");
//        }
        String sealBase64 = Base64.encode(bytes);
        if(sealBase64 == null || sealBase64.length() == 0){
            return Result.error("操作失败");
        }
        String replace = sealBase64.replace("data:image/png;base64,", "");
        return Result.OK(null,replace) ;
    }

    // @ApiOperation("印章生成-参数生成")
    @RequestMapping(value = "/generate/param",method = RequestMethod.POST)
    public Result<?> generateParam(@RequestBody EntSealGenerateParamRequest request){

        if(request == null || request.getEntSealShapeType() == null){
            return Result.error("参数缺失");
        }

        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        String tenantId = currentUser.getTenantId();
        SysTenantInfo tenantInfo = tenantInfoService.getById(tenantId);
        if(tenantInfo == null){
            return Result.error("租户不存在");
        }
        String topText = tenantInfo.getTenantName();
        String middleText = request.getMiddleText();
        String bottomText = request.getBottomText();

        String sealBase64 = null ;
        if(request.getEntSealShapeType().equals(EntSealShapeTypeEnum.CIRCULAR_STAR.getCode())){
            sealBase64 = sealGenerateService.generateEntCircleSeal(topText, middleText, bottomText,true);
        }else if(request.getEntSealShapeType().equals(EntSealShapeTypeEnum.ELLIPSE_STAR.getCode())){
            TenantInfoDTO tenantInfoExt = tenantInfoExtendService.getTenantInfoExt(tenantId);
            if(tenantInfoExt == null || tenantInfoExt.getOrganizationNo() == null){
                return Result.error("企业没有实名认真信息");
            }
            String center = tenantInfoExt.getOrganizationNo();
            sealBase64 = sealGenerateService.generateEntEllipseSeal(topText, middleText, bottomText,center);
        }else if(request.getEntSealShapeType().equals(EntSealShapeTypeEnum.CIRCULAR.getCode())){
            sealBase64 = sealGenerateService.generateEntCircleSeal(topText, middleText, bottomText,false);
        }else if(request.getEntSealShapeType().equals(EntSealShapeTypeEnum.ELLIPSE.getCode())){
            sealBase64 = sealGenerateService.generateEntEllipseSeal(topText, middleText, bottomText,null);
        }
        if(sealBase64 == null || sealBase64.length() == 0){
            return Result.error("操作失败");
        }
        String replace = sealBase64.replace("data:image/png;base64,", "");
        return Result.OK(null,replace) ;
    }





}