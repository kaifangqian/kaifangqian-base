/**
 * @description 电子印章-模板管理
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

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaifangqian.modules.opensign.entity.*;
import com.kaifangqian.modules.opensign.enums.*;
import com.kaifangqian.modules.opensign.service.template.*;
import com.kaifangqian.modules.opensign.vo.base.*;
import com.kaifangqian.modules.opensign.vo.request.*;
import com.kaifangqian.modules.opensign.vo.response.OperateListResponse;
import com.kaifangqian.modules.opensign.vo.response.TemplateInfoResponse;
import com.kaifangqian.modules.opensign.vo.response.TemplateListResponse;
import com.kaifangqian.modules.system.entity.SysTenantUser;
import com.kaifangqian.annotation.ResrunLogModule;
import com.kaifangqian.common.base.entity.BaseEntity;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.modules.opensign.entity.*;
import com.kaifangqian.modules.opensign.enums.*;
import com.kaifangqian.modules.opensign.service.business.ActivitiBusinessService;
import com.kaifangqian.modules.opensign.service.business.AuthBusinessService;
import com.kaifangqian.modules.opensign.service.business.TemplateBusinessService;
import com.kaifangqian.modules.opensign.service.template.*;
import com.kaifangqian.modules.opensign.service.tool.SignFileService;
import com.kaifangqian.modules.opensign.service.tool.pojo.ControlWidgetProperty;
import com.kaifangqian.modules.opensign.vo.base.*;
import com.kaifangqian.modules.opensign.vo.base.sign.DocImageVo;
import com.kaifangqian.modules.opensign.vo.request.*;
import com.kaifangqian.modules.opensign.vo.response.*;
import com.kaifangqian.modules.storage.entity.AnnexStorage;
import com.kaifangqian.modules.system.service.ISysTenantUserService;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: SignTemplateController
 * @Package: com.kaifangqian.modules.opensign.controller
 * @ClassName: SignTemplateController
 * @author: FengLai_Gong
 * @Date: 2022/1/19 10:05
 */
@Slf4j
@RestController
@RequestMapping("/sign/template")
@ResrunLogModule(name = "模板管理")
// @Api(tags = "电子印章-模板管理")
public class SignTemplateController {

    @Autowired
    private SignTemplateService templateService ;
    @Autowired
    private SignTemplateAuthService templateAuthService ;
    @Autowired
    private SignTemplateControlService templateControlService;
    @Autowired
    private SignTemplateRecordService templateRecordService ;
    @Autowired
    private SignTemplateImageRecordService templateImageRecordService ;
//    @Autowired
//    private SignTemplateImageConvertService templateImageConvertService;

//    @Autowired
//    private SignTemplateLogApplyService templateLogApplyService ;
    @Autowired
    private SignTemplateLogOperateService templateLogOperateService ;
    @Autowired
    private SignTemplateLogReferenceService templateLogReferenceService ;


    @Autowired
    private SignTemplateFolderService templateFolderService ;
    @Autowired
    private SignTemplateFolderRelationService templateFolderRelationService ;

    @Autowired
    private TemplateBusinessService templateBusinessService ;
    @Autowired
    private ActivitiBusinessService activitiBusinessService ;

    @Autowired
    private SignFileService signFileService ;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor ;


    @Autowired
    private ISysTenantUserService tenantUserService ;
    @Autowired
    private AuthBusinessService authBusinessService ;



    // @ApiOperation("模版列表")
    @RequestMapping(value = "/useList",method = RequestMethod.GET)
    public Result<List<TemplateListResponse>> useList() {
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        List<TemplateListResponse> list = new ArrayList<>();
        //必须包含是模板使用者
        List<Integer> businessTypeRoleList = new ArrayList<>();
        businessTypeRoleList.add(BusinessAuthTypeRole.TEMPLATE_USER.getCode());
        List<String> businessIdList = authBusinessService.getBusinessIdList(BusinessAuthType.TEMPLATE,businessTypeRoleList);
        if(businessIdList == null || businessIdList.size() == 0){
            return Result.OK(list);
        }

        QueryWrapper<SignTemplate> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignTemplate::getTemplateStatus, TemplateStatusEnum.ENABLED.getCode());
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        wrapper.lambda().in(SignTemplate::getId,businessIdList);
        wrapper.lambda().orderByDesc(BaseEntity::getCreateTime);
        List<SignTemplate> signTemplates = templateService.list(wrapper);
        if(signTemplates == null || signTemplates.size() == 0){
            return Result.OK(list) ;
        }
        for(SignTemplate signTemplate : signTemplates){
            TemplateListResponse response = new TemplateListResponse();
            response.setId(signTemplate.getId());
            response.setTemplateName(signTemplate.getTemplateName());
            response.setTemplateType(signTemplate.getTemplateType());
            response.setTemplateCode(signTemplate.getTemplateCode());
            SignTemplateRecord current = templateRecordService.getCurrent(signTemplate.getId());
            if(current != null){
                response.setAnnexId(current.getAnnexId());
            }
            list.add(response);
        }
        return Result.OK(list) ;
    }


    // @ApiOperation("模版列表")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public Result<IPage<TemplateListResponse>> list(TemplateListRequest request){

        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        IPage<TemplateListResponse> responseIPage = new Page<>(request.getPageNo(),request.getPageSize());
        List<String> templateIdList = new ArrayList<>();
        //必须包含是模板使用者
        List<Integer> businessTypeRoleList = new ArrayList<>();
        businessTypeRoleList.add(BusinessAuthTypeRole.TEMPLATE_MANAGE.getCode());
        List<String> businessIdList = authBusinessService.getBusinessIdList(BusinessAuthType.TEMPLATE,businessTypeRoleList);
        if(businessIdList == null || businessIdList.size() == 0){
            return Result.OK(responseIPage);
        }

        if(request.getTemplateFolderId() != null && request.getTemplateFolderId().length() > 0){
            //如果根据文件夹查询
            List<SignTemplateFolderRelation> list = templateFolderRelationService.getList(request.getTemplateFolderId(), businessIdList);
            //文件查询不到，直接返回了
            if(list == null || list.size() == 0){
                return Result.OK(responseIPage);
            }
            templateIdList.addAll(list.stream().map(SignTemplateFolderRelation::getTemplateId).collect(Collectors.toList()));
        }else {
            templateIdList.addAll(businessIdList);
        }
        if(templateIdList.size() == 0){
            return Result.OK(responseIPage);
        }
        //条件
        QueryWrapper<SignTemplate> wrapper = new QueryWrapper<>();
        if(request.getTemplateCode() != null && request.getTemplateCode().length() > 0){
            wrapper.lambda().like(SignTemplate::getTemplateCode,"%" + request.getTemplateCode() + "%");
        }
        if(request.getTemplateName() != null && request.getTemplateName().length() > 0){
            wrapper.lambda().like(SignTemplate::getTemplateName,"%" + request.getTemplateName() + "%");
        }
        if(request.getBusinessType() != null && request.getBusinessType().length() > 0){
            wrapper.lambda().eq(SignTemplate::getBusinessType,request.getBusinessType());
        }
        if(request.getTemplateType() != null && request.getTemplateType() > 0){
            wrapper.lambda().eq(SignTemplate::getTemplateType,request.getTemplateType());
        }
        if(request.getTemplateStatus() != null){
            wrapper.lambda().eq(SignTemplate::getTemplateStatus,request.getTemplateStatus());
        }
        wrapper.lambda().in(SignTemplate::getId,templateIdList);
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        //租户隔离
//        wrapper.lambda().eq(SignTemplate::getSysTenantId,currentUser.getTenantId());
        wrapper.lambda().orderByDesc(BaseEntity::getCreateTime);
        //查询
        Page<SignTemplate> page = templateService.page(new Page<>(request.getPageNo(), request.getPageSize()), wrapper);
        if(page != null && page.getRecords() != null && page.getRecords().size() > 0){
            responseIPage.setPages(page.getPages());
            responseIPage.setCurrent(page.getCurrent());
            responseIPage.setTotal(page.getTotal());
            responseIPage.setSize(page.getSize());
            List<TemplateListResponse> responseList = new ArrayList<>();
            for (SignTemplate template : page.getRecords()){
                TemplateListResponse response = new TemplateListResponse();
                BeanUtils.copyProperties(template,response);
                responseList.add(response);
            }
            responseIPage.setRecords(responseList);
        }

        return Result.OK(responseIPage);
    }

    // @ApiOperation("模版详情")
    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public Result<TemplateInfoResponse> info(@RequestParam(name = "templateId") String templateId){

        SignTemplate template = templateService.getById(templateId);

        if(template == null){
            return Result.error("模板不存在",null);
        }
        TemplateInfoResponse response = new TemplateInfoResponse();
        //模板主数据
        TemplateInfo templateVo = new TemplateInfo();
        BeanUtils.copyProperties(template,templateVo);
        templateVo.setTemplateId(template.getId());
        //附件数据
        SignTemplateRecord current = templateRecordService.getCurrent(templateId);
        if(current != null && current.getAnnexId() != null){
            AnnexStorage annex = signFileService.getAnnexById(current.getAnnexId());
            if(annex != null){
                templateVo.setAnnex(annex);
                //模板图片数据
                List<ImageVo> imageVoList = new ArrayList<>() ;
                List<DocImageVo> voList = signFileService.saveAndConvertImage(annex.getId(), null);
//                List<SignTemplateImageRecord> imageRecordList = templateImageRecordService.getCurrentList(templateId);
                if(voList != null && voList.size() > 0){
                    for(DocImageVo docImageVo : voList){
                        ImageVo imageVo = new ImageVo();
                        imageVo.setAnnexId(docImageVo.getAnnexId());
                        imageVo.setPage(docImageVo.getPage());
                        imageVo.setId(docImageVo.getId());

                        imageVo.setImageWidth(docImageVo.getImageWidth());
                        imageVo.setImageHeight(docImageVo.getImageHeight());

                        //设置默认值
                        if(imageVo.getImageWidth() == null){
                            imageVo.setImageWidth("595.3");
                        }
                        if(imageVo.getImageHeight() == null){
                            imageVo.setImageHeight("841.9");
                        }
                        imageVoList.add(imageVo);
                    }
                }
                response.setImageVoList(imageVoList);
            }

        }
        response.setTemplateVo(templateVo);

        //模板控件数据
        List<TemplateControlVo> templateControlVoList = new ArrayList<>();
        List<SignTemplateControl> controlList = templateControlService.getList(templateId);
        if(controlList != null && controlList.size() > 0){
            for(SignTemplateControl control : controlList){
                TemplateControlVo controlVo = new TemplateControlVo();
                BeanUtils.copyProperties(control,controlVo);

                templateControlVoList.add(controlVo);
            }
        }
        if(templateControlVoList.size() > 0){
            for(TemplateControlVo vo : templateControlVoList){
                if(vo.getControlOrder() == null){
                    vo.setControlOrder(0);
                }
            }
            templateControlVoList = templateControlVoList.stream().sorted(Comparator.comparing(TemplateControlVo::getControlOrder)).collect(Collectors.toList());
        }
        response.setTemplateControlVoList(templateControlVoList);

        //权限列表
        List<SignBusinessAuth> businessAuthList = authBusinessService.getAuthListByBusinessId(template.getId(), BusinessAuthType.TEMPLATE);
        if(businessAuthList != null && businessAuthList.size() > 0){
            Map<Integer, List<SignBusinessAuth>> collect = businessAuthList.stream().collect(Collectors.groupingBy(SignBusinessAuth::getBusinessTypeRole));
            response.setManagerList(authBusinessService.convertVo(collect.get(BusinessAuthTypeRole.TEMPLATE_MANAGE.getCode())));
            response.setUserList(authBusinessService.convertVo(collect.get(BusinessAuthTypeRole.TEMPLATE_USER.getCode())));
        }
        return Result.OK(response) ;
    }


//    // @ApiOperation("模版详情")
//    @RequestMapping(value = "/info/apply",method = RequestMethod.GET)
//    public Result<TemplateInfoResponse> infoApply(@RequestParam(name = "templateApplyId") String templateApplyId){
//
//        SignTemplateLogApply templateLogApply = templateLogApplyService.getById(templateApplyId);
//
//        if(templateLogApply == null){
//            return Result.error("模板不存在",null);
//        }
//        TemplateInfoResponse response = new TemplateInfoResponse();
//        //模板主数据
//        TemplateInfo templateVo = new TemplateInfo();
//        BeanUtils.copyProperties(templateLogApply,templateVo);
//        templateVo.setTemplateId(templateLogApply.getTemplateId());
//        templateVo.setTemplateApplyId(templateLogApply.getId());
//
//
//        //附件数据
//        SignTemplateRecord current = templateRecordService.getCurrent(templateLogApply.getId());
//        if(current != null && current.getAnnexId() != null){
//            AnnexStorage annex = signFileService.getAnnexById(current.getAnnexId());
//            templateVo.setAnnex(annex);
//        }
//        response.setTemplateVo(templateVo);
//        //模板控件数据
//        List<TemplateControlVo> templateControlVoList = new ArrayList<>();
//        List<SignTemplateControl> controlList = templateControlService.getList(templateLogApply.getId());
//        if(controlList != null && controlList.size() > 0){
//            for(SignTemplateControl control : controlList){
//                TemplateControlVo controlVo = new TemplateControlVo();
//                BeanUtils.copyProperties(control,controlVo);
//                templateControlVoList.add(controlVo);
//            }
//        }
//        response.setTemplateControlVoList(templateControlVoList);
//        //模板图片数据
//        List<ImageVo> imageVoList = new ArrayList<>() ;
//        List<SignTemplateImageRecord> imageRecordList = templateImageRecordService.getCurrentList(templateLogApply.getId());
//        if(imageRecordList != null && imageRecordList.size() > 0){
//            for(SignTemplateImageRecord record : imageRecordList){
//                ImageVo imageVo = new ImageVo();
//                imageVo.setAnnexId(record.getAnnexId());
//                imageVo.setPage(record.getTemplatePage());
//                imageVo.setId(record.getId());
//                imageVoList.add(imageVo);
//            }
//        }
//        response.setImageVoList(imageVoList);
//        return Result.OK(response) ;
//    }

    // @ApiOperation("模版创建")
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public Result<?> create(){
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        SignTemplate signTemplate = new SignTemplate();
        signTemplate.setDeleteFlag(false);
        signTemplate.setSysDeptId(currentUser.getDepartId());

        boolean save = templateService.save(signTemplate);
        if(!save){
            return Result.error("操作失败");
        }

        return Result.OK("",signTemplate.getId()) ;
    }


    // @ApiOperation("模版制作保存")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Result<?> save(@RequestBody TemplateVo request){
//        if(request == null || request.getUserList() == null || request.getUserList().size() == 0){
//            return Result.error("参数缺失");
//        }
//        if(request.getId() == null || request.getId().length() == 0){
//            templateId = templateBusinessService.createTemplate(request);
//        }else {
//            SignTemplate signTemplate = templateService.getById(request.getId());
//            if(signTemplate == null){
//                return Result.error("模板不存在，无法更新");
//            }
//            templateId = templateBusinessService.updateTemplate(request,signTemplate);
//        }
        TemplateOperateVo vo = templateBusinessService.save(request);
        if(vo == null){
            return Result.error("操作失败");
        }

        operateLog(request.getTemplateId(), TemplateOperateTypeEnum.MAKE);
        return Result.OK(vo) ;
    }


    // @ApiOperation("模版变更保存")
    @RequestMapping(value = "/change",method = RequestMethod.POST)
    public Result<?> change(@RequestBody TemplateVo request){
        if(request == null){
            return Result.error("参数缺失");
        }
        TemplateOperateVo vo = templateBusinessService.change(request);
        if(vo == null){
            return Result.error("操作失败");
        }
        operateLog(request.getTemplateId(),TemplateOperateTypeEnum.CHANGE);
        return Result.OK(vo) ;
    }

//    // @ApiOperation("模版发布")
//    @RequestMapping(value = "/submit",method = RequestMethod.POST)
//    public Result<?> submit(@RequestBody TemplateSubmitRequest request){
//        if(request == null || request.getId() == null || request.getId().length() == 0){
//            return Result.error("参数缺失");
//        }
//        //校验模板数据是否存在
//        SignTemplate signTemplate = templateService.getById(request.getId());
//        if(signTemplate == null){
//            return Result.error("模板不存在");
//        }
//        //校验模板控件数据是否存在
//        Integer controlCount = templateControlService.count(request.getId());
//        if(controlCount == null || controlCount == 0){
//            return Result.error("模板没有控件数据");
//        }
//        //校验模板图片数据是否存在
//        Integer imageCount = templateImageRecordService.countCurrentList(request.getId());
//        if(imageCount == null || imageCount == 0){
//            return Result.error("模板没有图片数据");
//        }
//        //
//        signTemplate.setTemplateStatus(2);
//
//        boolean b = templateService.updateById(signTemplate);
//        if(!b){
//            return Result.error("操作失败");
//        }
//        return Result.OK("") ;
//    }

    // @ApiOperation("模版保存控件参数")
    @RequestMapping(value = "/save/control",method = RequestMethod.POST)
    public Result<?> saveControl(@RequestBody TemplateSaveControlListRequest request){
        if(request == null || request.getControlVoList() == null || request.getControlVoList().size() == 0){
            return Result.error("参数缺失",null);
        }
//        SignTemplateLogApply signTemplateLogApply = templateLogApplyService.getById(request.getTemplateApplyId());
//        if(signTemplateLogApply == null){
//            return Result.error("模板申请记录不存在",null);
//        }
//        if(TemplateOperateTypeEnum.MAKE.getCode().equals(signTemplateLogApply.getOperateType())){
//            SignTemplate signTemplate = templateService.getById(request.getTemplateId());
//            if(signTemplate == null){
//                return Result.error("模板不存在",null);
//            }
//            templateBusinessService.saveControlList(signTemplateLogApply.getTemplateId(),request.getControlVoList());
//        }
        for(TemplateControlVo templateControlVo : request.getControlVoList()){
            if(templateControlVo.getInterfaceParamName() == null || templateControlVo.getInterfaceParamName().length() == 0){
                return Result.error("参数名称缺失",null);
            }
            if(templateControlVo.getType() == null || templateControlVo.getType().length() == 0){
                return Result.error("控件类型缺失",null);
            }
            if(!ControlTypeEnum.getWriteList().contains(templateControlVo.getType())){
                return Result.error("控件类型不存在",null);
            }
            if(templateControlVo.getType().equals(ControlTypeEnum.LINE_TEXT.getName()) || templateControlVo.getType().equals(ControlTypeEnum.MULTILINE_TEXT.getName())
                    ||templateControlVo.getType().equals(ControlTypeEnum.NUMBER.getName()) || templateControlVo.getType().equals(ControlTypeEnum.DATE.getName()) || templateControlVo.getType().equals(ControlTypeEnum.DROPDOWN_LIST.getName())){
                if(templateControlVo.getFontFamily() == null || templateControlVo.getFontFamily().length() == 0){
                    return Result.error("控件字体缺失",null);
                }
                if(templateControlVo.getFontSize() == null){
                    return Result.error("控件字体大小缺失",null);
                }
            }
            if(templateControlVo.getType().equals(ControlTypeEnum.RADIO.getName()) || templateControlVo.getType().equals(ControlTypeEnum.CHECKBOX.getName())
            ||templateControlVo.getType().equals(ControlTypeEnum.DROPDOWN_LIST.getName())){
                try {
                    List<ControlWidgetProperty> widgetPropertyList = JSON.parseArray(templateControlVo.getProperties(), ControlWidgetProperty.class);
                    if(widgetPropertyList == null || widgetPropertyList.size() == 0){
                        return Result.error("填写控件数据异常",null);
                    }
                    List<String> uidList = widgetPropertyList.stream().map(ControlWidgetProperty::getUid).collect(Collectors.toList());
                    if(uidList == null || uidList.size() == 0){
                        return Result.error("填写控件数据异常",null);
                    }

                }catch (Exception e){
                    return Result.error("填写控件数据异常",null);
                }
            }

        }
        templateBusinessService.saveControlList(request.getTemplateId(),request.getControlVoList());
        return Result.OK() ;
    }

    // @ApiOperation("模版控件校验")
    @RequestMapping(value = "/verify/control",method = RequestMethod.GET)
    public Result<?> verifyControl(@RequestParam(value = "templateId") String templateId){
//    public Result<?> verifyControl(@RequestParam(value = "templateApplyId") String templateApplyId){

//        SignTemplateLogApply templateLogApply = templateLogApplyService.getById(templateApplyId);
//        if(templateLogApply == null){
//            return Result.error("模板申请记录不存在", null);
//        }

        List<SignTemplateControl> list = templateControlService.getList(templateId);
        if(list == null || list.size() == 0){
            return Result.OK("模板控件不存在",false);
        }
        return Result.OK(true) ;
    }


    // @ApiOperation("模版下载控件参数")
    @RequestMapping(value = "/download/control",method = RequestMethod.POST)
    public Result<?> downloadControl(@RequestBody TemplateDownloadControlRequest request){
        if(request == null || request.getTemplateId() == null){
            return Result.error("参数缺失",null);
        }
        QueryWrapper<SignTemplateControl> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        wrapper.lambda().eq(SignTemplateControl::getTemplateId,request.getTemplateId());

//        wrapper.lambda().in(SignTemplateControl::getType, ControlTypeEnum.DATE.getName(),ControlTypeEnum.LINE_TEXT.getName(),ControlTypeEnum.MULTILINE_TEXT.getName(),ControlTypeEnum.NUMBER.getName());

        List<SignTemplateControl> list = templateControlService.list(wrapper);
        Map<String,Object> map = new LinkedHashMap<>();
        if(list != null && list.size() > 0){
            list = list.stream().sorted(Comparator.comparing(SignTemplateControl::getControlOrder)).collect(Collectors.toList());
            for(SignTemplateControl signTemplateControl : list){
                if(signTemplateControl.getInterfaceParamName() != null && signTemplateControl.getInterfaceParamName().length() > 0){
                    if(signTemplateControl.getType().equals(ControlTypeEnum.CHECKBOX.getName())
                            || signTemplateControl.getType().equals(ControlTypeEnum.RADIO.getName())
                            || signTemplateControl.getType().equals(ControlTypeEnum.DROPDOWN_LIST.getName())){
                        if(signTemplateControl.getProperties() != null && signTemplateControl.getProperties().length() > 0){
                            try {
                                List<ControlWidgetProperty> widgetPropertyList = JSON.parseArray(signTemplateControl.getProperties(), ControlWidgetProperty.class);
                                if(widgetPropertyList != null && widgetPropertyList.size() > 0){
                                    map.put(signTemplateControl.getInterfaceParamName(),widgetPropertyList);
                                }
                            }catch (Exception e){
                                map.put(signTemplateControl.getInterfaceParamName(), "[]");
                            }
                        }else {
                            map.put(signTemplateControl.getInterfaceParamName(), "[]");
                        }
                    }else {
                        map.put(signTemplateControl.getInterfaceParamName(),null);
                    }
                }
            }
        }
        operateLog(request.getTemplateId(),TemplateOperateTypeEnum.PARAM_DOWNLOAD);
        return Result.OK(map) ;
    }

    // @ApiOperation("模版转图片")
    @RequestMapping(value = "/convertImage",method = RequestMethod.POST)
    public Result<List<DocImageVo>> convertImage(@RequestBody TemplateImageConvertRequest request){
        if(request == null || request.getTemplateId() == null || request.getAnnexId() == null){
            return Result.error("参数缺失",null);
        }
        List<DocImageVo> voList = signFileService.saveAndConvertImage(request.getAnnexId(), null);
//        List<ImageConvertVo> imageConvertVos = templateBusinessService.convertImage(request.getTemplateId(), request.getAnnexId());

        return Result.OK(voList) ;
    }



    // @ApiOperation("模版编辑")
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public Result<?> edit(@RequestBody TemplateEditRequest request){
        if(request == null || request.getTemplateId() == null){
            return Result.error("参数缺失");
        }
        SignTemplate signTemplate = templateService.getById(request.getTemplateId());
        if(signTemplate == null){
            return Result.error("模板不存在");
        }
        signTemplate.setTemplateName(request.getTemplateName());
        signTemplate.setBusinessType(request.getBusinessType());
        signTemplate.setNote(request.getNote());
        boolean b = templateService.updateById(signTemplate);
        if(!b){
            return Result.error("操作失败");
        }
        if(request.getAuthVoList() != null && request.getAuthVoList().size() > 0){
            templateAuthService.deleteByTemplateId(signTemplate.getId());
            List<SignTemplateAuth> authList = new ArrayList<>();
            for(TemplateAuthVo authVo : request.getAuthVoList()){
                SignTemplateAuth auth =new SignTemplateAuth();
                BeanUtils.copyProperties(authVo,auth);
                auth.setId(null);
                auth.setDeleteFlag(false);
                authList.add(auth);
            }
            templateAuthService.saveBatch(authList);
        }
        operateLog(request.getTemplateId(),TemplateOperateTypeEnum.EDIT);
        return Result.OK() ;
    }

    // @ApiOperation("模版删除")
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public Result<?> delete(@RequestParam("templateId") String templateId){


        if(templateId == null || templateId.length() == 0){
            return Result.error("参数缺失");
        }
        SignTemplate signTemplate = templateService.getById(templateId);
        if(signTemplate == null){
            return Result.error("模板不存在");
        }
        signTemplate.setDeleteFlag(true);
        signTemplate.setDeleteTime(new Date());
        boolean b = templateService.updateById(signTemplate);
        if(!b){
            return Result.error("操作失败");
        }
//        SignTemplateLogApply templateLogApply = templateLogApplyService.findByTemplateId(templateId);
//
//        if(templateLogApply != null){
//            activitiBusinessService.delete(templateLogApply.getId());
//        }
//        List<SignTemplateLogApply> list = templateLogApplyService.findList(templateId);
//        if(list != null && list.size() > 0){
//            for(SignTemplateLogApply logApply : list){
//                activitiBusinessService.delete(logApply.getId());
//            }
//        }

        return Result.OK() ;
    }

    // @ApiOperation("模版启用")
    @RequestMapping(value = "/enabled",method = RequestMethod.POST)
    public Result<?> enabled(@RequestBody TemplateStatusRequest request){

        if(request == null || request.getTemplateId() == null){
            return Result.error("参数缺失");
        }
        SignTemplate signTemplate = templateService.getById(request.getTemplateId());
        if(signTemplate == null){
            return Result.error("模板不存在");
        }
        signTemplate.setTemplateStatus(TemplateStatusEnum.ENABLED.getCode());
        boolean b = templateService.updateById(signTemplate);
        if(!b){
            return Result.error("操作失败",null);
        }
//        String templateApplyId = templateBusinessService.enabled(signTemplate);
        operateLog(request.getTemplateId(),TemplateOperateTypeEnum.ENABLED);
        return Result.OK("",signTemplate.getId()) ;
    }

    // @ApiOperation("模版停用")
    @RequestMapping(value = "/unenabled",method = RequestMethod.POST)
    public Result<?> unenabled(@RequestBody TemplateStatusRequest request){

        if(request == null || request.getTemplateId() == null){
            return Result.error("参数缺失");
        }
        SignTemplate signTemplate = templateService.getById(request.getTemplateId());
        if(signTemplate == null){
            return Result.error("模板不存在");
        }
        signTemplate.setTemplateStatus(TemplateStatusEnum.UN_ENABLED.getCode());
        boolean b = templateService.updateById(signTemplate);
        if(!b){
            return Result.error("操作失败",null);
        }
//        String templateApplyId = templateBusinessService.unenabled(signTemplate);
        operateLog(request.getTemplateId(),TemplateOperateTypeEnum.UN_ENABLED);
        return Result.OK("",signTemplate.getId()) ;
    }



    // @ApiOperation("模版操作记录列表")
    @RequestMapping(value = "/operateLog/list",method = RequestMethod.GET)
    public Result<IPage<OperateListResponse>> operateLogList(TemplateLogOperateListRequest request){
        LoginUser currentUser = MySecurityUtils.getCurrentUser();

        Page<OperateListResponse> responsePage = new Page<>(request.getPageNo(),request.getPageSize());
        QueryWrapper<SignTemplateLogOperate> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignTemplateLogOperate::getTemplateId,request.getTemplateId());
        //租户隔离
        wrapper.lambda().eq(SignTemplateLogOperate::getSysTenantId,currentUser.getTenantId());
        wrapper.lambda().orderByDesc(SignTemplateLogOperate::getOperateTime);
        //查询
        Page<SignTemplateLogOperate> page = templateLogOperateService.page(new Page<>(request.getPageNo(), request.getPageSize()), wrapper);

        if(page != null && page.getRecords() != null && page.getRecords().size() > 0){
            List<SignTemplateLogOperate> logOperates = page.getRecords();
            List<String> userIdList = logOperates.stream().map(SignTemplateLogOperate::getSysUserId).collect(Collectors.toList());
            List<SysTenantUser> sysTenantUsers = tenantUserService.listByIds(userIdList);
            Map<String ,String> userMap = null ;
            if(sysTenantUsers != null && sysTenantUsers.size() > 0){
                userMap = sysTenantUsers.stream().collect(Collectors.toMap(SysTenantUser::getId,SysTenantUser::getNickName,(k1,k2) -> k1));
            }
            responsePage.setPages(page.getPages());
            responsePage.setCurrent(page.getCurrent());
            responsePage.setTotal(page.getTotal());
            responsePage.setSize(page.getSize());
            List<OperateListResponse> responseList = new ArrayList<>();
            for (SignTemplateLogOperate logOperate : logOperates){
                OperateListResponse response = new OperateListResponse();
                response.setSysTenantUserId(logOperate.getSysUserId());
                if(userMap != null && userMap.containsKey(logOperate.getSysUserId())){
                    String nickName = userMap.get(logOperate.getSysUserId());
                    response.setSysTenantUserName(nickName);
                }
                response.setOperateTime(logOperate.getOperateTime());
                response.setTemplateId(logOperate.getTemplateId());
                response.setTemplateOperateId(logOperate.getId());
                response.setOperateType(logOperate.getOperateType());
                response.setOperateName(TemplateOperateTypeEnum.getName(logOperate.getOperateType()));
                responseList.add(response);
            }
            responsePage.setRecords(responseList);
        }
        return Result.OK(responsePage);
    }

//    // @ApiOperation("模板审批记录列表")
//    @RequestMapping(value = "/applyLog/list",method = RequestMethod.GET)
//    public Result<IPage<ApplyListResponse>> applyLogList(TemplateLogApplyListRequest request){
//
//        Page<ApplyListResponse> responsePage = new Page<>(request.getPageNo(),request.getPageSize());
//        LoginUser currentUser = MySecurityUtils.getCurrentUser();
//
//        QueryWrapper<SignTemplateLogApply> wrapper = new QueryWrapper<>();
//        wrapper.lambda().eq(SignTemplateLogApply::getTemplateId,request.getTemplateId());
//        wrapper.lambda().eq(SignTemplateLogApply::getDeleteFlag,false);
//        //租户隔离
//        wrapper.lambda().eq(SignTemplateLogApply::getSysTenantId,currentUser.getTenantId());
//        wrapper.lambda().orderByDesc(SignTemplateLogApply::getApplyTime);
//        //查询
//        Page<SignTemplateLogApply> page = templateLogApplyService.page(new Page<>(request.getPageNo(), request.getPageSize()), wrapper);
//
//        if(page != null && page.getRecords() != null && page.getRecords().size() > 0){
//            List<SignTemplateLogApply> logApplies = page.getRecords();
//            List<String> userIdList = logApplies.stream().map(SignTemplateLogApply::getSysUserId).collect(Collectors.toList());
//            List<SysTenantUser> sysTenantUsers = tenantUserService.listByIds(userIdList);
//            Map<String ,String> userMap = null ;
//            if(sysTenantUsers != null && sysTenantUsers.size() > 0){
//                userMap = sysTenantUsers.stream().collect(Collectors.toMap(SysTenantUser::getId,SysTenantUser::getNickName,(k1,k2) -> k1));
//            }
//            responsePage.setPages(page.getPages());
//            responsePage.setCurrent(page.getCurrent());
//            responsePage.setTotal(page.getTotal());
//            responsePage.setSize(page.getSize());
//            List<ApplyListResponse> responseList = new ArrayList<>();
//            for(SignTemplateLogApply logApply : logApplies){
//                ApplyListResponse response = new ApplyListResponse();
//                response.setSysTenantUserId(logApply.getSysUserId());
//                if(userMap != null && userMap.containsKey(logApply.getSysUserId())){
//                    String nickName = userMap.get(logApply.getSysUserId());
//                    response.setSysTenantUserName(nickName);
//                }
//                response.setApplyTime(logApply.getApplyTime());
//                response.setTemplateId(logApply.getTemplateId());
//                response.setTemplateApplyId(logApply.getId());
//                response.setOperateType(logApply.getOperateType());
//                response.setOperateName(TemplateOperateTypeEnum.getName(logApply.getOperateType()));
//
//                response.setApplyStatus(logApply.getApplyStatus());
//                response.setApplyName(TemplateApplyStatusEnum.getName(logApply.getApplyStatus()));
//
//                responseList.add(response);
//            }
//
//            responsePage.setRecords(responseList);
//        }
//        return Result.OK(responsePage);
//    }
//
//
//    // @ApiOperation("模板审批记录删除")
//    @RequestMapping(value = "/applyLog/delete",method = RequestMethod.GET)
//    public Result<?> applyLogDelete(@RequestParam(value = "templateApplyId") String templateApplyId){
//
//
//        SignTemplateLogApply templateLogApply = templateLogApplyService.getById(templateApplyId);
//        if(templateLogApply == null){
//            return Result.error("申请记录不存在",null);
//        }
//        if(templateLogApply.getApplyStatus().equals(TemplateApplyStatusEnum.TO_BE_APPROVAL.getCode())){
//            return Result.error("模板申请状态为待审批，不能删除",null);
//        }
//        if(templateLogApply.getApplyStatus().equals(TemplateApplyStatusEnum.APPROVAL_SUCCESS.getCode())){
//            return Result.error("模板申请状态为审批通过，不能删除",null);
//        }
//        templateLogApply.setDeleteFlag(true);
//        boolean updateTemplateApplyLog = templateLogApplyService.updateById(templateLogApply);
//        if(!updateTemplateApplyLog){
//            return Result.error("操作失败",null);
//        }
//        SignTemplate signTemplate = templateService.getById(templateLogApply.getTemplateId());
//        if(templateLogApply == null){
//            return Result.error("模板不存在",null);
//        }
//        if(templateLogApply.getOperateType().equals(TemplateOperateTypeEnum.MAKE.getCode())){
//            signTemplate.setDeleteFlag(true);
//            boolean b = templateService.updateById(signTemplate);
//            if(!b){
//                return Result.error("操作失败",null);
//            }
//        }
//        //删除流程
//        activitiBusinessService.delete(templateLogApply.getId());
//        return Result.OK() ;
//    }




    // @ApiOperation("模版引用记录列表")
    @RequestMapping(value = "/referenceLog/list",method = RequestMethod.GET)
    public Result<IPage<SignTemplateLogReference>> referenceLogList(TemplateLogReferenceListRequest request){
        QueryWrapper<SignTemplateLogReference> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignTemplateLogReference::getTemplateId,request.getTemplateId());
        //查询
        Page<SignTemplateLogReference> page = templateLogReferenceService.page(new Page<>(request.getPageNo(), request.getPageSize()), wrapper);
        return Result.OK(page);
    }


    // @ApiOperation("模版文件夹列表")
    @RequestMapping(value = "/folder/tree",method = RequestMethod.GET)
    public Result<List<FolderNode>> folderTree(){
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        QueryWrapper<SignTemplateFolder> wrapper = new QueryWrapper<>();
        //租户隔离
        wrapper.lambda().eq(SignTemplateFolder::getSysTenantId,currentUser.getTenantId());
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);

        List<SignTemplateFolder> list = templateFolderService.list(wrapper);
        if(list == null || list.size() == 0){
            return Result.OK(new ArrayList<>());
        }
        List<FolderNode> voList = buildTree(list);
        return Result.OK(voList) ;
    }


    public List<FolderNode> buildTree(List<SignTemplateFolder> list){
        List<FolderNode> parentVoList = new ArrayList<>();
        for(SignTemplateFolder templateFolder : list){
            if(templateFolder.getParentTemplateFolderId() == null || templateFolder.getParentTemplateFolderId().length() == 0){
                FolderNode vo = new FolderNode();
                vo.setParentFolderId(templateFolder.getParentTemplateFolderId());
                vo.setKey(templateFolder.getId());
                vo.setLabel(templateFolder.getName());
                vo.setName(templateFolder.getName());
                List<FolderNode> children = findChildren(templateFolder.getId(), list);
                vo.setChildren(children);
                parentVoList.add(vo);
            }
        }
        return parentVoList;
    }

    public List<FolderNode> findChildren(String parentId, List<SignTemplateFolder> list){
        List<FolderNode> children = new ArrayList<>();
        for(SignTemplateFolder templateFolder : list){
            if(templateFolder.getParentTemplateFolderId() != null && templateFolder.getParentTemplateFolderId().length() > 0
                && templateFolder.getParentTemplateFolderId().equals(parentId)){
                FolderNode vo = new FolderNode();
                vo.setParentFolderId(templateFolder.getParentTemplateFolderId());
                vo.setKey(templateFolder.getId());
                vo.setLabel(templateFolder.getName());
                vo.setName(templateFolder.getName());
                List<FolderNode> nextChildren = findChildren(templateFolder.getId(), list);
                vo.setChildren(nextChildren);
                children.add(vo);
            }
        }
        return children ;
    }


//    // @ApiOperation("模版文件夹列表")
//    @RequestMapping(value = "/folder/list",method = RequestMethod.GET)
//    public Result<List<FolderVo>> folderList(@RequestParam("parentTemplateFolderId") String parentTemplateFolderId){
//        List<FolderVo> voList = new ArrayList<>();
//
//        QueryWrapper<SignTemplateFolder> wrapper = new QueryWrapper<>();
//        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
//        if(parentTemplateFolderId != null){
//            wrapper.lambda().eq(SignTemplateFolder::getParentTemplateFolderId,parentTemplateFolderId);
//        }
//
//        List<SignTemplateFolder> list = templateFolderService.list(wrapper);
//        if(list != null && list.size() > 0){
//            for(SignTemplateFolder templateFolder : list){
//                FolderVo vo = new FolderVo();
//                vo.setId(templateFolder.getId());
//                vo.setName(templateFolder.getName());
//                vo.setParentFolderId(templateFolder.getParentTemplateFolderId());
//                voList.add(vo);
//            }
//        }
//
//        return Result.OK(voList) ;
//    }

    // @ApiOperation("模版文件夹添加或编辑")
    @RequestMapping(value = "/folder/save",method = RequestMethod.POST)
    public Result<?> folderSave(@RequestBody FolderVo request){

        SignTemplateFolder folder = null;
        Boolean result = null ;
        if(request.getId() == null || request.getId().length() == 0){
            folder = new SignTemplateFolder();
            folder.setName(request.getName());
            folder.setParentTemplateFolderId(request.getParentFolderId());
            folder.setDeleteFlag(false);
            result = templateFolderService.save(folder);

        }else {
            folder = templateFolderService.getById(request.getId());
            if(folder == null){
                return Result.error("文件夹不存在");
            }
            //更新
            LambdaUpdateWrapper<SignTemplateFolder> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(SignTemplateFolder::getId,request.getId());
            if(request.getName() != null){
                wrapper.set(SignTemplateFolder::getName,request.getName());
            }
            if(request.getParentFolderId() != null && request.getParentFolderId().length() > 0){
                wrapper.set(SignTemplateFolder::getParentTemplateFolderId,request.getParentFolderId());
            }else {
                wrapper.set(SignTemplateFolder::getParentTemplateFolderId,null);
            }
            result = templateFolderService.update(wrapper);
//            folder.setName(request.getName());
//            if(request.getParentFolderId() != null && request.getParentFolderId().length() > 0){
//                folder.setParentTemplateFolderId(request.getParentFolderId());
//            }
//
//            result = templateFolderService.updateById(folder);
        }
        if(!result){
            return Result.error("操作失败");
        }

        return Result.OK() ;
    }

    // @ApiOperation("模版文件夹删除")
    @RequestMapping(value = "/folder/delete",method = RequestMethod.DELETE)
    public Result<?> folderDelete(TemplateFolderDeleteRequest request){
        if(request == null || request.getFolderIdList() == null || request.getFolderIdList().size() == 0){
            return Result.error("参数缺失");
        }

        //校验是否有子文件夹
        Integer countChildren = templateFolderService.countChildren(request.getFolderIdList());
        if(countChildren != null && countChildren > 0){
            return Result.error("存在子文件夹，不能删除");
        }

        //校验是否有关联文件
        Integer countRelation = templateFolderRelationService.count(request.getFolderIdList());
        if(countRelation != null && countRelation > 0){
            return Result.error("存在关联模板，不能删除");
        }
        templateFolderService.delete(request.getFolderIdList());

        return Result.OK() ;
    }

    // @ApiOperation("模版文件夹添加模板")
    @RequestMapping(value = "/folder/join",method = RequestMethod.POST)
    public Result<?> folderJoin(@RequestBody TemplateFolderJoinRequest request){
        if(request == null || request.getFolderId() == null || request.getIds() == null || request.getIds().size() == 0){
            return Result.error("参数缺失");
        }
        //查询
        SignTemplateFolder docFolder = templateFolderService.getById(request.getFolderId());
        if(docFolder == null){
            return Result.error("文件夹不存在");
        }
        //查询是否有已经存在文件夹中的文档
        List<SignTemplateFolderRelation> list = templateFolderRelationService.getList(request.getFolderId(), request.getIds());
        //如果存在已经有关联的文档id
        List<String> existsDocIdList = null ;
        if(list != null && list.size() > 0){
            existsDocIdList = list.stream().map(SignTemplateFolderRelation::getTemplateId).collect(Collectors.toList());
        }
        //循环插入
        for(String templateId : request.getIds()){
            //排除掉已经存在的文档id
            if(existsDocIdList == null || !existsDocIdList.contains(templateId)){
                SignTemplateFolderRelation relation = new SignTemplateFolderRelation();
                relation.setTemplateFolderId(request.getFolderId());
                relation.setTemplateId(templateId);
                relation.setDeleteFlag(false);
                templateFolderRelationService.save(relation);
            }
        }
        return Result.OK() ;

    }

    // @ApiOperation("模版文件夹移动模板")
    @RequestMapping(value = "/folder/move",method = RequestMethod.POST)
    public Result<?> folderMove(@RequestBody TemplateFolderMoveRequest request){
        if(request == null || request.getTargetFolderId() == null
                || request.getIds() == null || request.getIds().size() == 0){
            return Result.error("参数缺失");
        }
        //查询
        LoginUser sysUser = MySecurityUtils.getCurrentUser();
//        SignTemplateFolder sourceTemplateFolder = templateFolderService.getById(request.getSourceFolderId());
//        if(sourceTemplateFolder == null){
//            return Result.error("源文件夹不存在");
//        }
        SignTemplateFolder targetTemplateFolder = templateFolderService.getById(request.getTargetFolderId());
        if(targetTemplateFolder == null){
            return Result.error("目标文件夹不存在");
        }
        //更新原本的文件夹与文件的关系
        Boolean update = templateFolderRelationService.deleteRelation(request.getIds());
        if(!update){
            return Result.error("操作失败");
        }
        //查询是否有已经存在文件夹中的文档
        List<SignTemplateFolderRelation> list = templateFolderRelationService.getList(request.getTargetFolderId(), request.getIds());
        //如果存在已经有关联的文档id
        List<String> existsDocIdList = null ;
        if(list != null && list.size() > 0){
            existsDocIdList = list.stream().map(SignTemplateFolderRelation::getTemplateId).collect(Collectors.toList());
        }
        //循环插入
        for(String templateId : request.getIds()){
            //排除掉已经存在的文档id
            if(existsDocIdList == null || !existsDocIdList.contains(templateId)){
                SignTemplateFolderRelation relation = new SignTemplateFolderRelation();
                relation.setTemplateFolderId(request.getTargetFolderId());
                relation.setTemplateId(templateId);
                relation.setDeleteFlag(false);
                templateFolderRelationService.save(relation);
            }
        }
        return Result.OK() ;

    }

    // @ApiOperation("模版文件夹移除模板")
    @RequestMapping(value = "/folder/remove",method = RequestMethod.POST)
    public Result<?> folderRemove(@RequestBody TemplateFolderRemoveRequest request){


        if(request == null || request.getFolderId() == null || request.getIds() == null || request.getIds().size() == 0){
            return Result.error("参数缺失");
        }
        //查询
        LoginUser sysUser = MySecurityUtils.getCurrentUser();
        SignTemplateFolder docFolder = templateFolderService.getById(request.getFolderId());
        if(docFolder == null){
            return Result.error("文件夹不存在");
        }
        boolean update = templateFolderRelationService.delete(request.getFolderId(),request.getIds());
        if(!update){
            return Result.error("操作失败");
        }
        return Result.OK() ;
    }



    public void operateLog(String templateId,TemplateOperateTypeEnum templateOperateTypeEnum){
        LoginUser currentUser = MySecurityUtils.getCurrentUser();

        SignTemplateLogOperate operate = new SignTemplateLogOperate();
        operate.setTemplateId(templateId);
        operate.setOperateType(templateOperateTypeEnum.getCode());
        operate.setOperateTime(new Date());
        operate.setSysUserId(currentUser.getTenantUserId());
        operate.setSysTenantId(currentUser.getTenantId());
        operate.setSysAccountId(currentUser.getId());
        operate.setSysOrgCode(currentUser.getOrgCode());
        operate.setSysDeptId(currentUser.getDepartId());
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                templateLogOperateService.save(operate);
            }
        });
    }



}