/**
 * @description 业务线配置管理，用于签署业务流程配置
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
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaifangqian.modules.opensign.entity.SignRe;
import com.kaifangqian.modules.opensign.entity.SignReAuth;
import com.kaifangqian.modules.opensign.entity.SignReFolder;
import com.kaifangqian.modules.opensign.entity.SignReFolderRelation;
import com.kaifangqian.modules.opensign.enums.*;
import com.kaifangqian.modules.opensign.vo.base.sign.*;
import com.kaifangqian.modules.opensign.vo.request.re.*;
import com.kaifangqian.modules.system.entity.SysTenantUser;
import com.kaifangqian.annotation.ResrunLogModule;
import com.kaifangqian.common.base.entity.BaseEntity;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.opensign.entity.*;
import com.kaifangqian.modules.opensign.enums.*;
import com.kaifangqian.modules.opensign.service.business.ReBusinessService;
import com.kaifangqian.modules.opensign.service.business.ReDataService;
import com.kaifangqian.modules.opensign.service.business.vo.ReSaveData;
import com.kaifangqian.modules.opensign.service.re.SignReAuthService;
import com.kaifangqian.modules.opensign.service.re.SignReFolderRelationService;
import com.kaifangqian.modules.opensign.service.re.SignReFolderService;
import com.kaifangqian.modules.opensign.service.re.SignReService;
import com.kaifangqian.modules.opensign.vo.base.FolderNode;
import com.kaifangqian.modules.opensign.vo.base.FolderVo;
import com.kaifangqian.modules.opensign.vo.base.sign.*;
import com.kaifangqian.modules.opensign.vo.request.re.*;
import com.kaifangqian.modules.opensign.vo.response.re.ListResponse;
import com.kaifangqian.modules.system.service.ISysTenantUserService;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: SignReController
 * @Package: com.kaifangqian.modules.opensign.controller
 * @ClassName: SignReController
 * @author: FengLai_Gong
 * @Date: 2023/12/12
 */
@Slf4j
@RestController
@RequestMapping("/sign/re")
@ResrunLogModule(name = "业务线配置")
// @Api(tags = "业务线配置")
public class SignReController {


    @Autowired
    private ReBusinessService reBusinessService ;

    @Autowired
    private SignReService reService ;
    
    @Autowired
    private SignReFolderService reFolderService ;
    
    @Autowired
    private SignReFolderRelationService reFolderRelationService ;

    @Autowired
    private SignReAuthService reAuthService ;
    @Autowired
    private ISysTenantUserService tenantUserService ;

    @Autowired
    private ReDataService reDataService ;

    // @ApiOperation("可用业务线列表查询")
    @RequestMapping(value = "/queryUseList",method = RequestMethod.GET)
    public Result<IPage<ListResponse>> queryUseList(QueryUseListRequest request){
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        IPage<ListResponse> responseIPage = new Page<>();
        List<ListResponse> responseList = new ArrayList<>();
        List<SignReAuth> auths = new ArrayList<>();
        //指定使用者列表
        QueryWrapper<SignReAuth> userTenantQueryWrapper = new QueryWrapper<>();
        userTenantQueryWrapper.lambda().eq(SignReAuth::getUserId,currentUser.getTenantUserId());
        userTenantQueryWrapper.lambda().eq(SignReAuth::getAuthType, SignReAuthTypeEnum.USER.getCode());
        userTenantQueryWrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        List<SignReAuth> userTenantList = reAuthService.list(userTenantQueryWrapper);
        if(userTenantList != null && userTenantList.size() > 0){
            auths.addAll(userTenantList);
        }
        //未指定使用者列表
        QueryWrapper<SignReAuth> tenantQueryWrapper = new QueryWrapper<>();
        tenantQueryWrapper.lambda().isNull(SignReAuth::getUserId);
        tenantQueryWrapper.lambda().eq(SignReAuth::getTenantId,currentUser.getTenantId());
        tenantQueryWrapper.lambda().eq(SignReAuth::getAuthType,SignReAuthTypeEnum.USER.getCode());
        tenantQueryWrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        List<SignReAuth> tenantList = reAuthService.list(tenantQueryWrapper);
        if(tenantList != null && tenantList.size() > 0){
            auths.addAll(tenantList);
        }
        if(auths.size() == 0){
            responseIPage.setRecords(responseList);
            return Result.OK(responseIPage) ;
        }
        List<String> reIdList = auths.stream().map(SignReAuth::getSignReId).collect(Collectors.toList());
        if(reIdList == null || reIdList.size() == 0){
            responseIPage.setRecords(responseList);
            return Result.OK(responseIPage) ;
        }
        QueryWrapper<SignRe> signReQueryWrapper = new QueryWrapper<>();
        signReQueryWrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        signReQueryWrapper.lambda().in(SignRe::getId,reIdList);
        if(request.getStatus() != null){
            signReQueryWrapper.lambda().eq(SignRe::getStatus, request.getStatus());
        }
        if(request.getName() != null && request.getName().length() > 0){
            signReQueryWrapper.lambda().like(SignRe::getName,"%" + request.getName() + "%");
        }
//        signReQueryWrapper.lambda().orderByDesc(BaseEntity::getCreateTime);
        signReQueryWrapper.lambda().orderByDesc(BaseEntity::getUpdateTime);
//        List<SignRe> signReList = reService.list(signReQueryWrapper);
//        if(signReList == null && signReList.size() == 0){
//            responseIPage.setRecords(responseList);
//            return Result.OK(responseIPage) ;
//        }
        if(request.getPageNo() == null || request.getPageNo() == 0){
            request.setPageNo(1);
        }
        if(request.getPageSize() == null || request.getPageSize() == 0){
            request.setPageSize(10);
        }
        Page<SignRe> page = reService.page(new Page<>(request.getPageNo(), request.getPageSize()), signReQueryWrapper);
        if(page != null && page.getRecords() != null && page.getRecords().size() > 0){

            responseIPage.setSize(page.getSize());
            responseIPage.setPages(page.getPages());
            responseIPage.setTotal(page.getTotal());
            responseIPage.setCurrent(page.getCurrent());
            List<SignRe> records = page.getRecords();

            List<String> recordIdList = records.stream().map(SignRe::getId).collect(Collectors.toList());

            List<SignReAuth> recordAuthList = reAuthService.listByReIdList(recordIdList,SignReAuthTypeEnum.MANAGER.getCode());
            Map<String, List<SignReAuth>> authMap = null;
            if(recordAuthList != null && recordAuthList.size() > 0){
                authMap = recordAuthList.stream().collect(Collectors.groupingBy(SignReAuth::getSignReId));
            }

            List<String> tenantUserIdList = recordAuthList.stream().map(SignReAuth::getUserId).collect(Collectors.toList());
            Map<String,String> tenantUserMap = null ;
            if(tenantUserIdList != null && tenantUserIdList.size() > 0){
                List<SysTenantUser> tenantUserList = tenantUserService.listByIds(tenantUserIdList);
                if(tenantUserList != null && tenantUserList.size() > 0){
                    tenantUserMap = tenantUserList.stream().collect(Collectors.toMap(SysTenantUser::getId,SysTenantUser::getNickName,(k1,k2) -> k1));
                }
            }

            for(SignRe re : records){
                ListResponse response = new ListResponse();
                response.setName(re.getName());
                response.setStatus(re.getStatus());
                response.setId(re.getId());
                response.setUpdateDate(re.getUpdateTime());
                response.setErrorStatus(re.getErrorStatus());
                if(authMap != null && authMap.containsKey(re.getId())){
                    List<SignReAuth> authList = authMap.get(re.getId());
                    if(authList != null && authList.size() > 0){
                        List<String> managerList = new ArrayList<>();
                        for(SignReAuth auth : authList){
                            if(tenantUserMap != null && tenantUserMap.containsKey(auth.getUserId())){
                                String nickName = tenantUserMap.get(auth.getUserId());
                                if(nickName != null){
                                    managerList.add(nickName);
                                }
                            }
                        }
                        response.setManagerList(managerList);

                    }
                }
                //文件夹名称获取
                String folderId = reFolderRelationService.getFolderId(re.getId());
                if(folderId != null){
                    SignReFolder reFolder = reFolderService.getById(folderId);
                    if(reFolder != null){
                        response.setFolderName(reFolder.getName());
                    }
                }
                responseList.add(response);
            }

        }
        responseIPage.setRecords(responseList);
        return Result.OK(responseIPage) ;
    }


    // @ApiOperation("业务线列表")
    @RequestMapping(value = "/useList",method = RequestMethod.GET)
    public Result<List<ListResponse>> useList(){
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        List<ListResponse> responseList = new ArrayList<>();
        List<SignReAuth> auths = new ArrayList<>();
        //指定使用者列表
        QueryWrapper<SignReAuth> userTenantQueryWrapper = new QueryWrapper<>();
        userTenantQueryWrapper.lambda().eq(SignReAuth::getUserId,currentUser.getTenantUserId());
        userTenantQueryWrapper.lambda().eq(SignReAuth::getAuthType,SignReAuthTypeEnum.USER.getCode());
        userTenantQueryWrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        List<SignReAuth> userTenantList = reAuthService.list(userTenantQueryWrapper);
        if(userTenantList != null && userTenantList.size() > 0){
            auths.addAll(userTenantList);
        }
        //未指定使用者列表
        QueryWrapper<SignReAuth> tenantQueryWrapper = new QueryWrapper<>();
        tenantQueryWrapper.lambda().isNull(SignReAuth::getUserId);
        tenantQueryWrapper.lambda().eq(SignReAuth::getTenantId,currentUser.getTenantId());
        tenantQueryWrapper.lambda().eq(SignReAuth::getAuthType,SignReAuthTypeEnum.USER.getCode());
        tenantQueryWrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        List<SignReAuth> tenantList = reAuthService.list(tenantQueryWrapper);
        if(tenantList != null && tenantList.size() > 0){
            auths.addAll(tenantList);
        }
        if(auths.size() == 0){
            return Result.OK(responseList) ;
        }
        List<String> reIdList = auths.stream().map(SignReAuth::getSignReId).collect(Collectors.toList());
        if(reIdList == null || reIdList.size() == 0){
            return Result.OK(responseList) ;
        }
        QueryWrapper<SignRe> signReQueryWrapper = new QueryWrapper<>();
        signReQueryWrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        signReQueryWrapper.lambda().eq(SignRe::getStatus, SignReStatusEnum.ENABLE.getCode());
        signReQueryWrapper.lambda().in(SignRe::getId,reIdList);
//        signReQueryWrapper.lambda().orderByDesc(BaseEntity::getCreateTime);
        signReQueryWrapper.lambda().orderByDesc(BaseEntity::getUpdateTime);
        List<SignRe> signReList = reService.list(signReQueryWrapper);
        if(signReList == null && signReList.size() == 0){
            return Result.OK(responseList) ;
        }
        for(SignRe signRe : signReList){
            ListResponse response = new ListResponse();
            response.setId(signRe.getId());
            response.setName(signRe.getName());
            response.setErrorStatus(signRe.getErrorStatus());
            responseList.add(response);
        }
        return Result.OK(responseList) ;
    }


    // @ApiOperation("业务线列表")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public Result<IPage<ListResponse>> list(ReListRequest request){
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        IPage<ListResponse> responseIPage = new Page<>();
        List<ListResponse> responseList = new ArrayList<>();

        //校验权限
        List<Integer> authTypeList = new ArrayList<>();
        authTypeList.add(SignReAuthTypeEnum.MANAGER.getCode());
        List<SignReAuth> manageList = reAuthService.listByParam(currentUser.getTenantUserId(),authTypeList);
        if(manageList == null || manageList.size() == 0){
            responseIPage.setRecords(responseList);
            return Result.OK(responseIPage) ;
        }
        List<String> signReIdList = manageList.stream().map(SignReAuth::getSignReId).collect(Collectors.toList());

        //权限数据
        List<String> reIdList = new ArrayList<>() ;
        if(request.getFolderId() != null && request.getFolderId().length() > 0){
            //文件夹
            List<SignReFolderRelation> folderRelationList = reFolderRelationService.getList(request.getFolderId(), signReIdList);
            if(folderRelationList == null || folderRelationList.size() == 0){
                responseIPage.setRecords(responseList);
                return Result.OK(responseIPage) ;
            }
            List<String> collect = folderRelationList.stream().map(SignReFolderRelation::getSignReId).collect(Collectors.toList());
            if(collect != null || collect.size() > 0){
                reIdList.addAll(collect);
            }
        }else {
            reIdList.addAll(signReIdList);
        }
        if(reIdList.size() == 0){
            responseIPage.setRecords(responseList);
            return Result.OK(responseIPage) ;
        }

        QueryWrapper<SignRe> wrapper = new QueryWrapper<SignRe>();
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        //数据权限
        wrapper.lambda().in(SignRe::getId,reIdList);
        if(request.getStatus() != null){
            wrapper.lambda().eq(SignRe::getStatus,request.getStatus());
        }
        if(request.getName() != null && request.getName().length() > 0){
            wrapper.lambda().like(SignRe::getName,"%" + request.getName() + "%");
        }
//        wrapper.lambda().orderByDesc(BaseEntity::getCreateTime);
        wrapper.lambda().orderByDesc(BaseEntity::getUpdateTime);
        Page<SignRe> page = reService.page(new Page<>(request.getPageNo(), request.getPageSize()), wrapper);
        if(page != null && page.getRecords() != null && page.getRecords().size() > 0){

            responseIPage.setSize(page.getSize());
            responseIPage.setPages(page.getPages());
            responseIPage.setTotal(page.getTotal());
            responseIPage.setCurrent(page.getCurrent());
            List<SignRe> records = page.getRecords();

            List<String> recordIdList = records.stream().map(SignRe::getId).collect(Collectors.toList());

            List<SignReAuth> recordAuthList = reAuthService.listByReIdList(recordIdList,SignReAuthTypeEnum.MANAGER.getCode());
            Map<String, List<SignReAuth>> authMap = null;
            if(recordAuthList != null && recordAuthList.size() > 0){
                authMap = recordAuthList.stream().collect(Collectors.groupingBy(SignReAuth::getSignReId));
            }

            List<String> tenantUserIdList = recordAuthList.stream().map(SignReAuth::getUserId).collect(Collectors.toList());
            Map<String,String> tenantUserMap = null ;
            if(tenantUserIdList != null && tenantUserIdList.size() > 0){
                List<SysTenantUser> tenantUserList = tenantUserService.listByIds(tenantUserIdList);
                if(tenantUserList != null && tenantUserList.size() > 0){
                    tenantUserMap = tenantUserList.stream().collect(Collectors.toMap(SysTenantUser::getId,SysTenantUser::getNickName,(k1,k2) -> k1));
                }
            }

            for(SignRe re : records){
                ListResponse response = new ListResponse();
                response.setName(re.getName());
                response.setStatus(re.getStatus());
                response.setId(re.getId());
                response.setUpdateDate(re.getUpdateTime());
                response.setErrorStatus(re.getErrorStatus());
                if(authMap != null && authMap.containsKey(re.getId())){
                    List<SignReAuth> authList = authMap.get(re.getId());
                    if(authList != null && authList.size() > 0){
                        List<String> managerList = new ArrayList<>();
                        for(SignReAuth auth : authList){
                            if(tenantUserMap != null && tenantUserMap.containsKey(auth.getUserId())){
                                String nickName = tenantUserMap.get(auth.getUserId());
                                if(nickName != null){
                                    managerList.add(nickName);
                                }
                            }
                        }
                        response.setManagerList(managerList);

                    }
                }
                responseList.add(response);
            }

        }
        responseIPage.setRecords(responseList);
        return Result.OK(responseIPage) ;
    }

    // @ApiOperation("业务线配置全部详情数据")
    @RequestMapping(value = "/info/all",method = RequestMethod.GET)
    public Result<BaseVo> infoAll(@RequestParam("signReId") String signReId){
        BaseVo info = reBusinessService.info(signReId);
        if(info != null){
            return Result.OK(info);
        }
        return Result.error("数据不存在" ,null) ;
    }

    // @ApiOperation("业务线配置全部详情数据")
    @RequestMapping(value = "/info/all/ru",method = RequestMethod.GET)
    public Result<BaseVo> infoAllRu(@RequestParam("signReId") String signReId){
        BaseVo info = reBusinessService.info2Ru(signReId);
        if(info != null){
            return Result.OK(info);
        }
        return Result.error("数据不存在" ,null) ;
    }



    // @ApiOperation("业务线配置详情")
    @RequestMapping(value = "/info/base",method = RequestMethod.GET)
    public Result<DocBaseVo> infoBase(@RequestParam("signReId") String signReId){
        DocBaseVo docBase = reBusinessService.getDocBase(signReId);
        return Result.OK(docBase) ;
    }

    // @ApiOperation("业务线权限设置")
    @RequestMapping(value = "/list/auth",method = RequestMethod.GET)
    public Result<ReAuthVo> listAuth(@RequestParam("signReId") String signReId){
        ReAuthVo vo = reBusinessService.getAuth(signReId);
        return Result.OK(vo) ;
    }

    // @ApiOperation("业务线签署人列表")
    @RequestMapping(value = "/list/signer",method = RequestMethod.GET)
    public Result<List<DocSignerVo>> listSigner(@RequestParam("signReId") String signReId){
        List<DocSignerVo> signerList = reBusinessService.getSignerList(signReId);
        return Result.OK(signerList) ;
    }

    // @ApiOperation("业务线文件列表")
    @RequestMapping(value = "/list/file",method = RequestMethod.GET)
    public Result<List<DocFileVo>> listFile(@RequestParam("signReId") String signReId){
        List<DocFileVo> docList = reBusinessService.getDocList(signReId);
        return Result.OK(docList) ;
    }

    // @ApiOperation("业务线位置及参数")
    @RequestMapping(value = "/list/control",method = RequestMethod.GET)
    public Result<ControlDataVo> listControl(DocControlListVo request){
        if(request.getSignReId() == null || request.getSignReId().length() == 0){
            return Result.error("参数缺失",null);
        }
        ControlDataVo vo = new ControlDataVo();
        SignRe signRe = reService.getById(request.getSignReId());
        if(signRe == null){
            return Result.error("业务线数据不存在",null);
        }

        if(signRe.getControlChangeFlag() == null){
            vo.setControlChangeFlag(ControlChangeFlagEnum.NECESSARY_AND_ADD.getType());
        }else {
            vo.setControlChangeFlag(signRe.getControlChangeFlag());
        }
        List<DocControlVo> docControlList = reBusinessService.getDocControlList(request);
        if(docControlList != null && docControlList.size() > 0){
            for(DocControlVo controlVo : docControlList){
                if(controlVo.getControlOrder() == null){
                    controlVo.setControlOrder(0);
                }
            }
            docControlList = docControlList.stream().sorted(Comparator.comparing(DocControlVo::getControlOrder)).collect(Collectors.toList());
        }
        vo.setControlList(docControlList);

        return Result.OK(vo) ;
    }

    // @ApiOperation("业务线抄送人数据")
    @RequestMapping(value = "/list/ccer",method = RequestMethod.GET)
    public Result<List<DocCcerVo>> listCcer(@RequestParam("signReId") String signReId){
        List<DocCcerVo> ccerList = reBusinessService.getCcerList(signReId);
        return Result.OK(ccerList) ;
    }

    // @ApiOperation("业务线审批设置数据")
    @RequestMapping(value = "/list/approve",method = RequestMethod.POST)
    public Result<List<DocApproveVo>> listApprove(@RequestParam("signReId") String signReId){
        List<DocApproveVo> approveList = reBusinessService.getApproveList(signReId);
        return Result.OK(approveList) ;
    }


    // @ApiOperation("业务线配置创建")
    @RequestMapping(value = "/create",method =RequestMethod.POST)
    public Result<?> create(@RequestBody CreateRequest request){
        if(request == null || request.getFolderId() == null || request.getFolderId().length() == 0
                || request.getName() == null || request.getName().length() == 0){
            return Result.error("参数缺失",null);
        }
        String reId = reBusinessService.create(request.getName(), request.getFolderId());
        return Result.OK("",reId) ;
    }


    // @ApiOperation("业务线配置保存")
    @RequestMapping(value = "/save/base",method =RequestMethod.POST)
    public Result<?> saveBase(@RequestBody BaseVo request){
        if(request == null || request.getBaseVo() == null ||
                request.getBaseVo().getSignReId() == null || request.getBaseVo().getSignReId().length() == 0){
            return Result.error("参数缺失",null);
        }

        String reId = reBusinessService.save(request);
        //校验整体参数
        reBusinessService.checkReErrorStatus(reId);
        return Result.OK("",reId) ;
    }

    // @ApiOperation("业务线权限设置保存")
    @RequestMapping(value = "/save/auth",method = RequestMethod.POST)
    public Result<?> saveAuth(@RequestBody ReAuthVo request){
        if(request == null || request.getSignReId() == null || request.getSignReId().length() == 0){
            return Result.error("参数缺失",null);
        }
        if(request.getAuthList() == null || request.getAuthList().size() == 0){
            return Result.error("参数缺失",null);
        }else {
            List<DocAuthVo> authList = request.getAuthList();
            Boolean noMangerFlag = true ;
            for(DocAuthVo authVo : authList){
                if(authVo.getAuthType().equals(SignReAuthTypeEnum.MANAGER.getCode())){
                    noMangerFlag = false ;
                }
            }
            if(noMangerFlag){
                return Result.error("必须指定业务线管理员",null);
            }
        }

        String reId = reBusinessService.saveAuth(request);
        return Result.OK("",reId) ;
    }

    // @ApiOperation("业务线签署人保存")
    @RequestMapping(value = "/save/signer",method = RequestMethod.POST)
    public Result<?> saveSigner(@RequestBody SaveSignerRequest request){
        if(request == null ||
                request.getSignerList() == null || request.getSignerList().size() == 0 ||
                request.getSignReId() == null || request.getSignReId().length() == 0){
            return Result.error("参数缺失",null);
        }
        SignRe re = reService.getById(request.getSignReId());
        if(re == null){
            throw new PaasException("业务线不存在");
        }
        //创建整体更新数据
        ReSaveData saveData = new ReSaveData();
        saveData.setReId(re.getId());
        String reId = reBusinessService.saveSignerList(request.getSignerList(), request.getSignReId(),saveData);

        reDataService.saveData(saveData);

        //校验整体参数
        reBusinessService.checkReErrorStatus(reId);
        return Result.OK("",request.getSignReId()) ;
    }

//    // @ApiOperation("业务线签约文件保存")
//    @RequestMapping(value = "/save/file",method = RequestMethod.POST)
//    public Result<?> saveFile(@RequestBody SaveFileRequest request){
//        return null ;
//    }
//
//    // @ApiOperation("业务线签约文件删除")
//    @RequestMapping(value = "/delete/file",method = RequestMethod.DELETE)
//    public Result<?> deleteFile(@RequestBody DeleteFileRequest request){
//        return null ;
//    }

    // @ApiOperation("业务线位置及参数保存")
    @RequestMapping(value = "/save/control",method = RequestMethod.POST)
    public Result<?> saveControl(@RequestBody SaveControlRequest request){

        SignRe re = reService.getById(request.getSignReId());
        if(re == null){
            throw new PaasException("业务线不存在");
        }
        if(request.getControlList() != null && request.getControlList().size() > 0){
            String message = reBusinessService.verifyControl(request.getControlList());
            if(message != null ){
                return Result.error(message,null);
            }
        }
        for(DocControlVo controlVo : request.getControlList()){
            if(controlVo.getSignerId() == null || controlVo.getSignerId().length() == 0){
                if(ControlTypeEnum.getWriteList().contains(controlVo.getControlType())){
                    throw new PaasException("填写控件未全部指定签署人");
                }else if(ControlTypeEnum.getSignList().contains(controlVo.getControlType())){
                    throw new PaasException("签署控件未全部指定签署人");
                }
            }
            if(controlVo.getControlType() == null || controlVo.getControlType().length() == 0){
                throw new PaasException("控件类型缺失");
            }
            if(controlVo.getControlType().equals(ControlTypeEnum.LINE_TEXT.getName()) || controlVo.getControlType().equals(ControlTypeEnum.MULTILINE_TEXT.getName())
                    || controlVo.getControlType().equals(ControlTypeEnum.DATE.getName()) || controlVo.getControlType().equals(ControlTypeEnum.NUMBER.getName())
                    ||controlVo.getControlType().equals(ControlTypeEnum.DROPDOWN_LIST.getName())){
                if(controlVo.getFontFamily() == null || controlVo.getFontFamily().length() == 0){
                    return Result.error("控件字体缺失",null);
                }
                if(controlVo.getFontSize() == null){
                    return Result.error("控件字体大小缺失",null);
                }
            }
        }

        if(request.getControlChangeFlag() != null){
            re.setControlChangeFlag(request.getControlChangeFlag());
            boolean updateRe = reService.updateById(re);
            if(!updateRe){
                throw new PaasException("业务线更新控件变更状态失败");
            }
        }else {
            if(re.getControlChangeFlag() == null){
                re.setControlChangeFlag(ControlChangeFlagEnum.NECESSARY_AND_ADD.getType());
                boolean updateRe = reService.updateById(re);
                if(!updateRe){
                    throw new PaasException("业务线更新控件变更状态失败");
                }
            }
        }

        reBusinessService.saveControlList(request.getControlList(), request.getDeleteIdList(), request.getSignReId());
        //校验整体参数
        reBusinessService.checkReErrorStatus(request.getSignReId());


        DocControlListVo docControlListVo = new DocControlListVo();
        docControlListVo.setSignReId(re.getId());
        List<DocControlVo> docControlList = reBusinessService.getDocControlList(docControlListVo);

        ControlDataVo vo = new ControlDataVo();
        vo.setControlChangeFlag(request.getControlChangeFlag());
        vo.setControlList(docControlList);
        return Result.OK(vo) ;
    }

    // @ApiOperation("业务线抄送人数据保存")
    @RequestMapping(value = "/save/ccer",method = RequestMethod.POST)
    public Result<?> saveCcer(@RequestBody SaveCcerRequest request){
        if(request == null || request.getCcerList() == null || request.getCcerList().size() == 0){
            return Result.error("参数缺失",null);
        }
        ReSaveData saveData = new ReSaveData();
        saveData.setReId(request.getSignReId());
        reBusinessService.saveCcerList(request.getCcerList(),request.getSignReId(),saveData);

        reDataService.saveData(saveData);

        return Result.OK("",request.getSignReId()) ;
    }


    // @ApiOperation("业务线审批设置保存")
    @RequestMapping(value = "/save/approve",method = RequestMethod.POST)
    public Result<?> saveApprove(@RequestBody SaveApproveRequest request){

        return null ;
    }

    // @ApiOperation("业务线-停用、启用")
    @RequestMapping(value = "/operate/status",method = RequestMethod.POST)
    public Result<?> operateStatus(@RequestBody OperationRequest request){

        if(request.getSignReId() == null || request.getSignReId().length() == 0 || request.getStatus() == null){
            return Result.error("参数缺失",null);
        }
        SignRe signRe = reService.getById(request.getSignReId());
        if(signRe == null){
            return Result.error("业务线不存在",null);
        }
        if(request.getStatus() == 1){
            //启用时再进行校验
            if(signRe.getErrorStatus() != SignReErrorStatusEnum.NO.getCode()){
                return Result.error("业务线存在异常",null);
            }
            reService.checkTemplatePara(signRe);
        }

        signRe.setStatus(request.getStatus());
        boolean b = reService.updateById(signRe);
        if(!b){
            return Result.error("操作失败",null);
        }

        return Result.OK() ;
    }

    // @ApiOperation("业务线-复制")
    @RequestMapping(value = "/operate/copy",method = RequestMethod.POST)
    public Result<?> operateCopy(@RequestBody OperationRequest request){

        return Result.OK() ;
    }

    // @ApiOperation("业务线-迁移")
    @RequestMapping(value = "/operate/move",method = RequestMethod.POST)
    public Result<?> operateMove(@RequestBody OperationRequest request){
        if(request == null || request.getFolderId() == null
                || request.getSignReId() == null){
            return Result.error("参数缺失");
        }
        //查询
        SignReFolder targetReFolder = reFolderService.getById(request.getFolderId());
        if(targetReFolder == null){
            return Result.error("目标文件夹不存在");
        }
        //更新原本的文件夹与文件的关系
        Boolean update = reFolderRelationService.deleteRelation(request.getSignReId());
        if(!update){
            return Result.error("操作失败");
        }
        SignReFolderRelation relation = new SignReFolderRelation();
        relation.setSignReFolderId(request.getFolderId());
        relation.setSignReId(request.getSignReId());
        relation.setDeleteFlag(false);
        reFolderRelationService.save(relation);
        return Result.OK() ;
    }




    // @ApiOperation("业务线文件夹列表")
    @RequestMapping(value = "/folder/tree",method = RequestMethod.GET)
    public Result<List<FolderNode>> folderTree(){
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        QueryWrapper<SignReFolder> wrapper = new QueryWrapper<>();
        //租户隔离
        wrapper.lambda().eq(SignReFolder::getSysTenantId,currentUser.getTenantId());
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);

        List<SignReFolder> list = reFolderService.list(wrapper);
        if(list == null || list.size() == 0){
            return Result.OK(new ArrayList<>());
        }
        List<FolderNode> voList = buildTree(list);
        return Result.OK(voList) ;
    }


    public List<FolderNode> buildTree(List<SignReFolder> list){
        List<FolderNode> parentVoList = new ArrayList<>();
        for(SignReFolder reFolder : list){
            if(reFolder.getParentReFolderId() == null || reFolder.getParentReFolderId().length() == 0){
                FolderNode vo = new FolderNode();
                vo.setParentFolderId(reFolder.getParentReFolderId());
                vo.setKey(reFolder.getId());
                vo.setId(reFolder.getId());
                vo.setLabel(reFolder.getName());
                vo.setName(reFolder.getName());
                List<FolderNode> children = findChildren(reFolder.getId(), list);
                vo.setChildren(children);
                parentVoList.add(vo);
            }
        }
        return parentVoList;
    }

    public List<FolderNode> findChildren(String parentId, List<SignReFolder> list){
        List<FolderNode> children = new ArrayList<>();
        for(SignReFolder reFolder : list){
            if(reFolder.getParentReFolderId() != null && reFolder.getParentReFolderId().length() > 0
                    && reFolder.getParentReFolderId().equals(parentId)){
                FolderNode vo = new FolderNode();
                vo.setParentFolderId(reFolder.getParentReFolderId());
                vo.setKey(reFolder.getId());
                vo.setId(reFolder.getId());
                vo.setLabel(reFolder.getName());
                vo.setName(reFolder.getName());
                List<FolderNode> nextChildren = findChildren(reFolder.getId(), list);
                vo.setChildren(nextChildren);
                children.add(vo);
            }
        }
        return children ;
    }


    // @ApiOperation("业务线文件夹列表")
    @RequestMapping(value = "/folder/list",method = RequestMethod.GET)
    public Result<List<FolderVo>> folderList(@RequestParam("parentreFolderId") String parentreFolderId){
        List<FolderVo> voList = new ArrayList<>();

        QueryWrapper<SignReFolder> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        if(parentreFolderId != null){
            wrapper.lambda().eq(SignReFolder::getParentReFolderId,parentreFolderId);
        }

        List<SignReFolder> list = reFolderService.list(wrapper);
        if(list != null && list.size() > 0){
            for(SignReFolder reFolder : list){
                FolderVo vo = new FolderVo();
                vo.setId(reFolder.getId());
                vo.setName(reFolder.getName());
                vo.setParentFolderId(reFolder.getParentReFolderId());
                voList.add(vo);
            }
        }

        return Result.OK(voList) ;
    }

    // @ApiOperation("业务线文件夹添加或编辑")
    @RequestMapping(value = "/folder/save",method = RequestMethod.POST)
    public Result<?> folderSave(@RequestBody FolderVo request){

        SignReFolder folder = null;
        Boolean result = null ;
        if(request.getId() == null || request.getId().length() == 0){
            folder = new SignReFolder();
            folder.setName(request.getName());
            folder.setParentReFolderId(request.getParentFolderId());
            folder.setDeleteFlag(false);
            result = reFolderService.save(folder);

        }else {
            folder = reFolderService.getById(request.getId());
            if(folder == null){
                return Result.error("文件夹不存在");
            }
            //更新
            LambdaUpdateWrapper<SignReFolder> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(SignReFolder::getId,request.getId());
            if(request.getName() != null){
                wrapper.set(SignReFolder::getName,request.getName());
            }
            if(request.getParentFolderId() != null && request.getParentFolderId().length() > 0){
                wrapper.set(SignReFolder::getParentReFolderId,request.getParentFolderId());
            }else {
                wrapper.set(SignReFolder::getParentReFolderId,null);
            }
            result = reFolderService.update(wrapper);
        }
        if(!result){
            return Result.error("操作失败");
        }

        return Result.OK() ;
    }

    // @ApiOperation("业务线文件夹删除")
    @RequestMapping(value = "/folder/delete",method = RequestMethod.DELETE)
    public Result<?> folderDelete(ReFolderDeleteRequest request){
        if(request == null || request.getFolderIdList() == null || request.getFolderIdList().size() == 0){
            return Result.error("参数缺失");
        }

        //校验是否有子文件夹
        Integer countChildren = reFolderService.countChildren(request.getFolderIdList());
        if(countChildren != null && countChildren > 0){
            return Result.error("存在子文件夹，不能删除");
        }

        //校验是否有关联文件
        Integer countRelation = reFolderRelationService.count(request.getFolderIdList());
        if(countRelation != null && countRelation > 0){
            return Result.error("存在关联业务线，不能删除");
        }
        reFolderService.delete(request.getFolderIdList());

        return Result.OK() ;
    }

    // @ApiOperation("业务线文件夹添加业务线")
    @RequestMapping(value = "/folder/join",method = RequestMethod.POST)
    public Result<?> folderJoin(@RequestBody ReFolderJoinRequest request){
        if(request == null || request.getFolderId() == null || request.getIds() == null || request.getIds().size() == 0){
            return Result.error("参数缺失");
        }
        //查询
        SignReFolder reFolder = reFolderService.getById(request.getFolderId());
        if(reFolder == null){
            return Result.error("文件夹不存在");
        }
        //查询是否有已经存在文件夹中的文档
        List<SignReFolderRelation> list = reFolderRelationService.getList(request.getFolderId(), request.getIds());
        //如果存在已经有关联的文档id
        List<String> existsReIdList = null ;
        if(list != null && list.size() > 0){
            existsReIdList = list.stream().map(SignReFolderRelation::getSignReId).collect(Collectors.toList());
        }
        //循环插入
        for(String reId : request.getIds()){
            //排除掉已经存在的文档id
            if(existsReIdList == null || !existsReIdList.contains(reId)){
                SignReFolderRelation relation = new SignReFolderRelation();
                relation.setSignReFolderId(request.getFolderId());
                relation.setSignReId(reId);
                relation.setDeleteFlag(false);
                reFolderRelationService.save(relation);
            }
        }
        return Result.OK() ;

    }

    // @ApiOperation("业务线文件夹移动业务线")
    @RequestMapping(value = "/folder/move",method = RequestMethod.POST)
    public Result<?> folderMove(@RequestBody ReFolderMoveRequest request){
        if(request == null || request.getTargetFolderId() == null
                || request.getIds() == null || request.getIds().size() == 0){
            return Result.error("参数缺失");
        }
        //查询
//        LoginUser sysUser = MySecurityUtils.getCurrentUser();
//        SignReFolder sourcereFolder = reFolderService.getById(request.getSourceFolderId());
//        if(sourcereFolder == null){
//            return Result.error("源文件夹不存在");
//        }
        SignReFolder targetReFolder = reFolderService.getById(request.getTargetFolderId());
        if(targetReFolder == null){
            return Result.error("目标文件夹不存在");
        }
        //更新原本的文件夹与文件的关系
        Boolean update = reFolderRelationService.deleteRelation(request.getIds());
        if(!update){
            return Result.error("操作失败");
        }
        //查询是否有已经存在文件夹中的文档
        List<SignReFolderRelation> list = reFolderRelationService.getList(request.getTargetFolderId(), request.getIds());
        //如果存在已经有关联的文档id
        List<String> existsReIdList = null ;
        if(list != null && list.size() > 0){
            existsReIdList = list.stream().map(SignReFolderRelation::getSignReId).collect(Collectors.toList());
        }
        //循环插入
        for(String reId : request.getIds()){
            //排除掉已经存在的文档id
            if(existsReIdList == null || !existsReIdList.contains(reId)){
                SignReFolderRelation relation = new SignReFolderRelation();
                relation.setSignReFolderId(request.getTargetFolderId());
                relation.setSignReId(reId);
                relation.setDeleteFlag(false);
                reFolderRelationService.save(relation);
            }
        }
        return Result.OK() ;

    }

    // @ApiOperation("业务线文件夹移除业务线")
    @RequestMapping(value = "/folder/remove",method = RequestMethod.POST)
    public Result<?> folderRemove(@RequestBody ReFolderRemoveRequest request){


        if(request == null || request.getFolderId() == null || request.getIds() == null || request.getIds().size() == 0){
            return Result.error("参数缺失");
        }
        //查询
        SignReFolder reFolder = reFolderService.getById(request.getFolderId());
        if(reFolder == null){
            return Result.error("文件夹不存在");
        }
        boolean update = reFolderRelationService.delete(request.getFolderId(),request.getIds());
        if(!update){
            return Result.error("操作失败");
        }
        return Result.OK() ;
    }





}