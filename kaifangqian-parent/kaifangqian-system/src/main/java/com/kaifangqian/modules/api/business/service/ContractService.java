/**
 * @description 电子签署管理服务
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
package com.kaifangqian.modules.api.business.service;

import com.kaifangqian.modules.api.controller.DataSafeVo;
import com.kaifangqian.modules.api.exception.RequestParamsException;
import com.kaifangqian.modules.api.vo.base.ContractPositionParam;
import com.kaifangqian.modules.api.vo.base.ContractRelationDoc;
import com.kaifangqian.modules.api.vo.base.ContractUser;
import com.kaifangqian.modules.cert.service.CertBusinessService;
import com.kaifangqian.modules.opensign.entity.*;
import com.kaifangqian.modules.opensign.enums.*;
import com.kaifangqian.modules.opensign.service.business.vo.RuCreateData;
import com.kaifangqian.modules.opensign.service.business.vo.RuDataDoc;
import com.kaifangqian.modules.opensign.service.re.*;
import com.kaifangqian.modules.opensign.service.ru.*;
import com.kaifangqian.common.constant.ApiCode;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.api.vo.base.*;
import com.kaifangqian.modules.opensign.entity.*;
import com.kaifangqian.modules.opensign.enums.*;
import com.kaifangqian.modules.opensign.pdfbox.PdfboxService;
import com.kaifangqian.modules.opensign.service.annex.AnnexImageService;
import com.kaifangqian.modules.opensign.service.business.RuBusinessService;
import com.kaifangqian.modules.opensign.service.business.RuCallbackService;
import com.kaifangqian.modules.opensign.service.business.RuDataService;
import com.kaifangqian.modules.opensign.service.business.RuSignService;
import com.kaifangqian.modules.opensign.service.business.vo.*;
import com.kaifangqian.modules.opensign.service.flow.IFlowService;
import com.kaifangqian.modules.opensign.service.re.*;
import com.kaifangqian.modules.opensign.service.ru.*;
import com.kaifangqian.modules.opensign.service.seal.SignPersonSealService;
import com.kaifangqian.modules.opensign.service.template.SignTemplateControlService;
import com.kaifangqian.modules.opensign.service.template.SignTemplateRecordService;
import com.kaifangqian.modules.opensign.service.template.SignTemplateService;
import com.kaifangqian.modules.opensign.service.tool.CalculatePositionService;
import com.kaifangqian.modules.opensign.service.tool.SignFileService;
import com.kaifangqian.modules.opensign.service.tool.pojo.SelectKeywords;
import com.kaifangqian.modules.system.service.IApiDeveloperManageService;
import com.kaifangqian.modules.system.service.ISysTenantInfoService;
import com.kaifangqian.modules.system.service.ISysTenantUserService;
import com.kaifangqian.modules.system.service.ISysUserService;
import com.kaifangqian.utils.UUIDGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @Description: ContractService
 * @Package: com.kaifangqian.modules.api.service
 * @ClassName: ContractService
 * @author: FengLai_Gong
 * @Date: 2024/5/23 10:09
 */
@Slf4j
@Service
public class ContractService {

    @Autowired
    protected SignReService reService ;
    @Autowired
    protected SignReDocService reDocService ;
    @Autowired
    protected SignReSignerService reSignerService ;
    @Autowired
    protected SignReSenderService reSenderService ;
    @Autowired
    protected SignReDocControlService reDocControlService ;
    @Autowired
    protected SignReDocParamService reDocParamService ;
    @Autowired
    protected SignReAuthService reAuthService ;
    @Autowired
    protected SignReDocControlPropertyService reDocControlPropertyService ;

    @Autowired
    protected SignTemplateService templateService ;
    @Autowired
    protected SignTemplateRecordService templateRecordService;
    @Autowired
    protected SignTemplateControlService templateControlService ;

    @Autowired
    protected SignRuService ruService ;
    @Autowired
    protected SignRuDocService ruDocService ;
    @Autowired
    protected SignRuDocControlService ruDocControlService ;
    @Autowired
    protected SignRuSignerService ruSignerService ;
    @Autowired
    protected SignRuSenderService ruSenderService ;
    @Autowired
    protected SignRuDocOperateService ruDocOperateService ;
    @Autowired
    protected SignRuOperatorService ruOperatorService ;
    @Autowired
    protected SignRuRelationService relationService ;
    @Autowired
    protected RuSignService ruSignService ;

    @Autowired
    protected SignRuDocControlPropertyService ruDocControlPropertyService ;

    @Autowired
    protected SignFileService signFileService ;

    @Autowired
    protected RuBusinessService ruBusinessService ;

    @Autowired
    protected SignRuSignConfirmService ruSignConfirmService;

    @Autowired
    protected IApiDeveloperManageService apiDeveloperManageService ;
    @Autowired
    protected ISysUserService sysUserService ;
    @Autowired
    protected ISysTenantInfoService tenantInfoService ;
    @Autowired
    protected ISysTenantUserService tenantUserService ;

    @Autowired
    protected SignRuTaskService ruTaskService ;


    @Autowired
    protected IFlowService iFlowService;
    @Autowired
    protected AnnexImageService annexImageService ;
    @Autowired
    protected RuCallbackService ruCallbackService ;

    @Autowired
    protected RuDataService ruDataService ;
    @Autowired
    protected SignRuOperateRecordService ruOperateRecordService ;

    @Autowired
    protected PdfboxService pdfboxService ;
    @Autowired
    protected CalculatePositionService calculatePositionService ;
    
    @Autowired
    protected UserSealAuthService userSealAuthService ;

    @Autowired
    protected SignPersonSealService personSealService ;
    @Autowired
    protected CertBusinessService certBusinessService ;

    @Transactional(rollbackFor = Exception.class)
    public void saveTask(Integer errorCount, DataSafeVo vo, Integer finalCount){
        try {
            log.info("errorCount = " + errorCount + "，开始");
            SignRuTask ruTask = new SignRuTask();
            ruTask.setId("大贵测试==" + UUID.randomUUID().toString());
            ruTaskService.save(ruTask);
            log.info("errorCount = " + errorCount + "，结束");
            if(errorCount == 3){
                log.info("errorCount = " + errorCount + "，执行报错");
                int a = 1 / 0 ;
            }

        }catch (Exception e){
            log.info("errorCount = " + errorCount + "，设置flag");
            vo.getFlag().set(true);
        }finally {
            log.info("errorCount = " + errorCount + "，增1");
            vo.getCount().incrementAndGet();
        }
        while (true){
            if(vo.getCount().get() == finalCount){
                log.info("errorCount = " + errorCount + "，准备结束");
                if(vo.getFlag().get()){
                    log.info("errorCount = " + errorCount + "，最终报错");
                    throw new PaasException("errorCount" + errorCount);
                }
                log.info("errorCount = " + errorCount + "，完成结束");
                break;
            }
        }

    }




    public void checkContractUser(ContractUser contractUser, String message){
        if(contractUser == null){
            throw new RequestParamsException(ApiCode.PARAM_MISSING,message + "-参数缺失");
        }
        if(contractUser.getName() == null || contractUser.getName().length() == 0){
            throw new RequestParamsException(ApiCode.PARAM_MISSING,message + "-姓名-参数缺失");
        }
        if(contractUser.getContactType() == null || contractUser.getContactType().length() == 0){
            throw new RequestParamsException(ApiCode.PARAM_MISSING,message + "-联系类型-参数缺失");
        }
        if(!contractUser.getContactType().equals("MOBILE") && !contractUser.getContactType().equals("EMAIL")){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,联系类型暂时仅支持MOBILE、EMAIL");
        }
        if(contractUser.getContact() == null || contractUser.getContact().length() == 0){
            throw new RequestParamsException(ApiCode.PARAM_MISSING,message + "-联系方式-参数缺失");
        }
    }


    public void checkPositionParam(ContractPositionParam positionParam, String signerNodeType){
        if(positionParam == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签署位置合集缺失");
        }
        if(positionParam.getControlType() == null || positionParam.getControlType().length() == 0){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签署控件类型缺失");
        }
        List<String> apiControlTypeList = ControlTypeEnum.getApiControlTypeList();
        if(!apiControlTypeList.contains(positionParam.getControlType())){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签署控件类型参数错误，不包含当前控件类型" + positionParam.getControlType());
        }

        if(SenderTypeEnum.ENTERPRISE.getApiName().equals(signerNodeType)){
            //如果是组织签章节点
            if(positionParam.getControlType().equals(ControlTypeEnum.SIGNATURE.getApiName())){
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,组织签章节点不能使用个人签章控件");
            }
        }else {
            if(positionParam.getControlType().equals(ControlTypeEnum.SEAL.getApiName()) || positionParam.getControlType().equals(ControlTypeEnum.CHOP_STAMP.getApiName())){
                if(SignerTypeEnum.RECEIVER_PERSONAL.getApiName().equals(signerNodeType)){
                    throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,个人签署方不能使用企业签章和骑缝签章");
                }else if(SenderTypeEnum.OPERATOR.getApiName().equals(signerNodeType)){
                    throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,经办人签字节点不能使用企业签章和骑缝签章");
                }else if(SenderTypeEnum.LEGAL_PERSON.getApiName().equals(signerNodeType)){
                    throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,法人签字节点不能使用企业签章和骑缝签章");
                }else if(SenderTypeEnum.PERSONAL.getApiName().equals(signerNodeType)){
                    throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,个人签字节点不能使用企业签章和骑缝签章");
                }
            }
        }

        if(positionParam.getControlType().equals(ControlTypeEnum.SIGN_DATE.getApiName())){
            if(positionParam.getSignDateFormat() == null || positionParam.getSignDateFormat().length() == 0){
                throw new PaasException("签署日期控件-时间格式不能为空");
            }
            if(!positionParam.getSignDateFormat().equals("YEAR_MONTH_DAY") && !positionParam.getSignDateFormat().equals("LINE") && !positionParam.getSignDateFormat().equals("BACK_SLASH")){
                throw new PaasException("签署日期控件-时间格式错误：" + positionParam.getSignDateFormat());
            }
        }
        if(positionParam.getSignPositionType() == null || positionParam.getSignPositionType().length() == 0){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签署位置设置方式缺失");
        }
        if(!positionParam.getSignPositionType().equals("POSITION") && !positionParam.getSignPositionType().equals("KEYWORD")){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签署位置设置方式不存在");
        }
        if(positionParam.getSignPositionType().equals("KEYWORD")){
            if(positionParam.getKeyword() == null || positionParam.getKeyword().length() == 0){
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,关键字缺失");
            }
            if(positionParam.getKeywordType() == null || positionParam.getKeywordType().length() == 0){
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,关键字搜索类型缺失");
            }
            if(!positionParam.getKeywordType().equals("1") && !positionParam.getKeywordType().equals("0") && !positionParam.getKeywordType().equals("-1")){
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,关键查询方式不存在");
            }
            if(positionParam.getOffsetY() != null){
                Float offsetY = null ;
                try {
                    offsetY = Float.parseFloat(positionParam.getOffsetY());
                }catch (Exception e){
                    throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,关键字纵坐标偏移量异常");
                }
            }
            if(positionParam.getOffsetX() != null){
                Float offsetX = null ;
                try {
                    offsetX = Float.parseFloat(positionParam.getOffsetX());
                }catch (Exception e){
                    throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,关键字横坐标偏移量异常");
                }

            }
        }else {
            if(positionParam.getPageConfig() == null || positionParam.getPageConfig().length() == 0){
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,页码配置缺失");
            }

            if(!positionParam.getControlType().equals(ControlTypeEnum.CHOP_STAMP.getApiName())){
                if(positionParam.getOffsetX() == null || positionParam.getOffsetX().length() == 0){
                    throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,偏移量缺失");
                }
            }
            if(positionParam.getOffsetY() == null || positionParam.getOffsetY().length() == 0){
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,偏移量缺失");
            }
        }


        if(positionParam.getRelationDocList() == null || positionParam.getRelationDocList().size() == 0){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,关联文档列表缺失");
        }
        for(ContractRelationDoc contractRelationDoc : positionParam.getRelationDocList()){
            if(contractRelationDoc.getDocType() == null){
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,关联文档-文档类型参数缺失");
            }
            if(!contractRelationDoc.getDocType().equals("1") && !contractRelationDoc.getDocType().equals("2")){
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,关联文档-文档类型参数异常");
            }
            if(contractRelationDoc.getDocId() == null || contractRelationDoc.getDocId().length() == 0){
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,关联文档-文件id参数缺失");
            }
        }

        if(positionParam.getControlWidth() != null && positionParam.getControlWidth().length() > 0){
            try {
                float i = Float.parseFloat(positionParam.getControlWidth());
                if(i > 120f){
                    throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签章位置宽度取值范围在10-120");
                }
                if(i < 10f){
                    throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签章位置宽度取值范围在10-120");
                }
            }catch (Exception e){
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签章位置宽度解析失败");
            }
        }
        if(positionParam.getControlHeight() != null && positionParam.getControlHeight().length() > 0){
            try {
                float i = Float.parseFloat(positionParam.getControlHeight());
                if(i > 120f){
                    throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签章位置高度取值范围在10-120");
                }
                if(i < 10f){
                    throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签章位置高度取值范围在10-120");
                }
            }catch (Exception e){
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签章位置高度解析失败");
            }
        }


    }

    /**
     * @Description #构建关键字控件
     * @Param [positionParam, createData, signerId]
     * @return void
     **/
    public void buildKeySignControl(ContractPositionParam positionParam, RuCreateData createData, String signerId, Integer originType){
        String keyword = positionParam.getKeyword();
        List<ContractRelationDoc> relationDocList = positionParam.getRelationDocList();

        SignRuKeyword ruKeyword = new SignRuKeyword();
        ruKeyword.setId(UUIDGenerator.generate());
        ruKeyword.setSignerId(signerId);
        ruKeyword.setKeyword(keyword);
        ruKeyword.setRuId(createData.getRuId());
        ruKeyword.setOffsetX(positionParam.getOffsetX());
        ruKeyword.setOffsetY(positionParam.getOffsetY());
        int offsetX = 0;
        int offsetY = 0;
        if(positionParam.getOffsetX() != null){
            offsetX = Integer.parseInt(positionParam.getOffsetX());
        }
        if(positionParam.getOffsetY() != null){
            offsetY = Integer.parseInt(positionParam.getOffsetY());
        }

        //控件宽高
        Float controlWidth = null ;
        Float controlHeight = null ;
        String controlType = null ;
        if(positionParam.getControlWidth() != null){
            controlWidth = Float.parseFloat(positionParam.getControlWidth());
        }
        if(positionParam.getControlHeight() != null){
            controlHeight = Float.parseFloat(positionParam.getControlHeight());
        }
        if(positionParam.getControlType().equals(ControlTypeEnum.SIGNATURE.getApiName())) {
            controlType = ControlTypeEnum.SIGNATURE.getName();
            if(controlWidth == null){
                controlWidth = 112f ;
            }
            if(controlHeight == null){
                controlHeight = 52f ;
            }
        }else if(positionParam.getControlType().equals("SEAL")){
            controlType = ControlTypeEnum.SEAL.getName();
            if(controlWidth == null){
                controlWidth = 120f ;
            }
            if(controlHeight == null){
                controlHeight = 120f ;
            }
        }else if(positionParam.getControlType().equals("SIGN_DATE")){
            controlType = ControlTypeEnum.SIGN_DATE.getName();
            if(controlWidth == null){
                controlWidth = 120f ;
            }
            if(controlHeight == null){
                controlHeight = 30f ;
            }
        }else if(positionParam.getControlType().equals("CHOP_STAMP")){
            controlType = ControlTypeEnum.CHOP_STAMP.getName();
            if(controlWidth == null){
                controlWidth = 120f ;
            }
            if(controlHeight == null){
                controlHeight = 120f ;
            }
        }else {
            controlType = ControlTypeEnum.SIGNATURE.getName();
            if(controlWidth == null){
                controlWidth = 112f ;
            }
            if(controlHeight == null){
                controlHeight = 52f ;
            }
        }
        String format = null;
        if(positionParam.getControlType().equals(ControlTypeEnum.SIGN_DATE.getApiName())){
            if(positionParam.getSignDateFormat().equals("YEAR_MONTH_DAY")){
                format = "yyyy年MM月dd日";
            }else if(positionParam.getSignDateFormat().equals("LINE")){
                format = "yyyy-MM-dd";
            }else if(positionParam.getSignDateFormat().equals("BACK_SLASH")){
                format = "yyyy/MM/dd";
            }
        }

        for(ContractRelationDoc relationDoc : relationDocList){
            String ruDocId = null ;
            if(relationDoc.getDocType().equals("1")){
                // 上传
                ruDocId = createData.getAddDocAnnex2RuDocMap().get(relationDoc.getDocId());
            }else if(relationDoc.getDocType().equals("2")){
                //  模板
                ruDocId = createData.getTemplateId2RuDocMap().get(relationDoc.getDocId());
            }
            if(ruDocId == null){
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签署位置合集关联文档不存在");
            }
            byte[] fileByte = null ;
            for(RuDataDoc dataDoc : createData.getDocList()){
                if(ruDocId.equals(dataDoc.getRuDoc().getId())){
                    fileByte  = dataDoc.getFileByte();
                }
            }
            if(fileByte == null){
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签署位置合集关联文档不存在");
            }
            PDPageTree pages  = null ;
            try {
                PDDocument document = Loader.loadPDF(fileByte);
                pages = document.getPages();
            }catch (Exception e){
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,解析文件失败");
            }
            if(pages == null || pages.getCount() == 0){
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,解析文件失败");
            }
            List<float[]> floats = null ;
            try {
                floats = new SelectKeywords().selectAllKeyword(fileByte, keyword);
            } catch (Exception e) {
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,未查询到关键字" + positionParam.getKeyword());
            }
            if(floats == null || floats.size() == 0){
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,未查询到关键字" + positionParam.getKeyword());
            }
            SignRuKeywordProperty property = new SignRuKeywordProperty();
            property.setId(UUIDGenerator.generate());
            property.setRuId(createData.getRuId());
            property.setKeywordId(ruKeyword.getId());
            property.setPropertyType(KeywordPropertyEnum.RELATION_DOC.getCode());
            property.setPropertyValue(ruDocId);
            if(positionParam.getKeywordType() == null || positionParam.getKeywordType().length() == 0){
                positionParam.setKeywordType("1");
            }

            List<SignRuKeywordProperty> propertyList = null ;
            List<float[]> keywordPosition = new ArrayList<>();
            if(positionParam.getKeywordType().equals("1")){
                //第一个关键字
                float[] floatsPosition = floats.get(0);
                keywordPosition.add(floatsPosition);
                //构建关键字配置参数
                propertyList = buildKeyPropertyAsc(createData.getRuId(), ruDocId);
            }else if(positionParam.getKeywordType().equals("0")){
                //所有关键字
                for(float[] floatsPosition : floats){
                    keywordPosition.add(floatsPosition);
                }
                //构建关键字配置参数
                propertyList = buildKeyPropertyAll(createData.getRuId(), ruDocId);
            }else {
                //最后一个关键字
                float[] floatsPosition = floats.get(floats.size() - 1);
                keywordPosition.add(floatsPosition);
                //构建关键字配置参数
                propertyList = buildKeyPropertyDesc(createData.getRuId(), ruDocId);
            }
            //保存关键字配置参数
            createData.getRuKeywordPropertyList().addAll(propertyList);
            if(keywordPosition.size() == 0){
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,未查询到关键字" + positionParam.getKeyword());
            }

            for(float[] floatsPosition : keywordPosition){
                if(floatsPosition.length != 3 || floatsPosition[0] == 0 || floatsPosition[1] == 0 || floatsPosition[2] == 0){
                    throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,未查询到关键字" + positionParam.getKeyword());
                }
                PDPage pdPage = pages.get((int) floatsPosition[2] - 1);
                if(pdPage == null){
                    throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,未查询到关键字" + positionParam.getKeyword());
                }
                PDRectangle mediaBox = pdPage.getMediaBox();
                if(mediaBox == null){
                    throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,未查询到关键字" + positionParam.getKeyword());
                }
                //真实pdf宽高
                float pdfWidth = mediaBox.getWidth();
                float pdfHeight = mediaBox.getHeight();
//                if(pdPage.getRotation() == 90 || pdPage.getRotation() == 270){
//                    pdfWidth = mediaBox.getHeight();
//                    pdfHeight = mediaBox.getWidth();
//                }

                //控件数据
                SignRuDocControl control = new SignRuDocControl();
                control.setPageWidth(pdfWidth + "");
                control.setPageHeight(pdfHeight + "");
                control.setSetupType("keyword");
                control.setSetupId(ruKeyword.getId());
                float x = floatsPosition[0] ;
                float y = floatsPosition[1] ;
                float page = floatsPosition[2] ;


                control.setControlType(controlType);
                control.setWidth(controlWidth + "");
                control.setHeight(controlHeight + "");
                //根据偏移量计算最后控件坐标
                x = x + offsetX ;
                y = (pdfHeight - y - (controlHeight * 0.618f)) + offsetY;
//                y = y + (controlHeight * 0.618f) - offsetY;
                if(x > pdfWidth - controlWidth){
                    x = pdfWidth - controlWidth ;
                }
                if(x < 0){
                    x = 0 ;
                }
                if(y > pdfHeight - controlHeight){
                    y = pdfHeight - controlHeight ;
                }
                if(y < controlHeight){
                    y = 0 ;
                }
                control.setOffsetX(x + "");
                control.setOffsetY(y + "");

                control.setSignerId(signerId);
                control.setSignRuId(createData.getRuId());
                control.setDeleteFlag(false);
                control.setOriginType(originType);
                //签署日期格式
                control.setFormat(format);
                control.setId(UUIDGenerator.generate());
                //控件属性-关联文档
                SignRuDocControlProperty docControlProperty = new SignRuDocControlProperty();
                docControlProperty.setControlId(UUIDGenerator.generate());
                docControlProperty.setRuId(createData.getRuId());
                docControlProperty.setControlId(control.getId());
                docControlProperty.setPropertyType(ControlPropertyTypeEnum.RELATION_DOC.getCode());
                docControlProperty.setPropertyValue(ruDocId);
                //控件属性-页码
                SignRuDocControlProperty pageProperty = new SignRuDocControlProperty();
                pageProperty.setId(UUIDGenerator.generate());
                pageProperty.setRuId(createData.getRuId());
                pageProperty.setControlId(control.getId());
                pageProperty.setPropertyType(ControlPropertyTypeEnum.PAGE_CONFIG.getCode());
                pageProperty.setPropertyValue(ControlPropertyTypePageConfigEnum.CUSTOM.getCode());
                SignRuDocControlProperty customProperty = new SignRuDocControlProperty();
                customProperty.setId(UUIDGenerator.generate());
                customProperty.setRuId(createData.getRuId());
                customProperty.setControlId(control.getId());
                customProperty.setPropertyType(ControlPropertyTypePageConfigEnum.CUSTOM.getCode());
                int pageInt = (int)page ;
                customProperty.setPropertyValue(pageInt + "");
                //整合数据
                createData.getRuSignControlList().add(control);
                createData.getRuSignControlPropertyList().add(docControlProperty);
                createData.getRuSignControlPropertyList().add(pageProperty);
                createData.getRuSignControlPropertyList().add(customProperty);

            }
        }
        createData.getRuKeywordList().add(ruKeyword);


    }



    /**
     * @Description #构建坐标位置控件
     * @Param [positionParam, createData, signerId]
     * @return void
     **/
    public void buildPositionControl(ContractPositionParam positionParam, RuCreateData createData,String signerId,Integer originType){
        List<ContractRelationDoc> relationDocList = positionParam.getRelationDocList();

        String format = null;
        if(positionParam.getControlType().equals("SIGN_DATE")){
            if(positionParam.getSignDateFormat().equals("YEAR_MONTH_DAY")){
                format = "yyyy年MM月dd日";
            }else if(positionParam.getSignDateFormat().equals("LINE")){
                format = "yyyy-MM-dd";
            }else if(positionParam.getSignDateFormat().equals("BACK_SLASH")){
                format = "yyyy/MM/dd";
            }
        }

        //控件宽高
        Float controlWidth = null ;
        Float controlHeight = null ;
        String controlType = null ;
        if(positionParam.getControlWidth() != null){
            controlWidth = Float.parseFloat(positionParam.getControlWidth());
        }
        if(positionParam.getControlHeight() != null){
            controlHeight = Float.parseFloat(positionParam.getControlHeight());
        }
        if(positionParam.getControlType().equals(ControlTypeEnum.SIGNATURE.getApiName())) {
            controlType = ControlTypeEnum.SIGNATURE.getName();
            if(controlWidth == null){
                controlWidth = 112f ;
            }
            if(controlHeight == null){
                controlHeight = 52f ;
            }
        }else if(positionParam.getControlType().equals("SEAL")){
            controlType = ControlTypeEnum.SEAL.getName();
            if(controlWidth == null){
                controlWidth = 120f ;
            }
            if(controlHeight == null){
                controlHeight = 120f ;
            }
        }else if(positionParam.getControlType().equals("SIGN_DATE")){
            controlType = ControlTypeEnum.SIGN_DATE.getName();
            if(controlWidth == null){
                controlWidth = 120f ;
            }
            if(controlHeight == null){
                controlHeight = 30f ;
            }
        }else if(positionParam.getControlType().equals("CHOP_STAMP")){
            controlType = ControlTypeEnum.CHOP_STAMP.getName();
            if(controlWidth == null){
                controlWidth = 120f ;
            }
            if(controlHeight == null){
                controlHeight = 120f ;
            }
        }else {
            controlType = ControlTypeEnum.SIGNATURE.getName();
            if(controlWidth == null){
                controlWidth = 112f ;
            }
            if(controlHeight == null){
                controlHeight = 52f ;
            }
        }

        if(positionParam.getControlType().equals("CHOP_STAMP")){
            //骑缝章特殊处理
            //保留4位小数
            String offsetY = positionParam.getOffsetY();
            BigDecimal yRate = null ;
            try{
                yRate = new BigDecimal(offsetY).setScale(4, RoundingMode.HALF_UP);
            }catch (Exception e){
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR, "业务处理失败,偏移量参数解析失败");
            }
            if(yRate == null){
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR, "业务处理失败,偏移量参数异常");
            }
            //计算坐标
            float pdfHeight = 842f;//标准A4页面高度
            float y = new BigDecimal(pdfHeight).multiply(yRate).setScale(4, RoundingMode.HALF_UP).floatValue();
            if(y > (pdfHeight - controlHeight)){
                y = pdfHeight - controlHeight ;
            }
            //控件数据
            SignRuDocControl control = new SignRuDocControl();
            control.setId(UUIDGenerator.generate());
            //坐标
            control.setOffsetY(y + "");
            //真实pdf文件宽高
            control.setPageWidth("595");
            control.setPageHeight("842");
            //控件宽高
            control.setWidth(controlWidth + "");
            control.setHeight(controlHeight + "");
            //控件类型
            control.setControlType(controlType);
            //签署日期格式
            control.setFormat(format);
            //其他数据
            control.setSignerId(signerId);
            control.setSignRuId(createData.getRuId());
            control.setDeleteFlag(false);
            control.setOriginType(originType);
            for(ContractRelationDoc relationDoc : relationDocList) {
                String ruDocId = null;
                if (relationDoc.getDocType().equals("1")) {
                    //上传
                    ruDocId = createData.getAddDocAnnex2RuDocMap().get(relationDoc.getDocId());
                } else if (relationDoc.getDocType().equals("2")) {
                    //模板
                    ruDocId = createData.getTemplateId2RuDocMap().get(relationDoc.getDocId());
                }
                if (ruDocId == null) {
                    throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR, "业务处理失败,签署位置合集关联文档不存在");
                }

                //控件属性-关联文档
                SignRuDocControlProperty docControlProperty = new SignRuDocControlProperty();
                docControlProperty.setId(UUIDGenerator.generate());
                docControlProperty.setRuId(createData.getRuId());
                docControlProperty.setControlId(control.getId());
                docControlProperty.setPropertyType(ControlPropertyTypeEnum.RELATION_DOC.getCode());
                docControlProperty.setPropertyValue(ruDocId);
                //整合数据
                createData.getRuSignControlPropertyList().add(docControlProperty);
            }
            String pageConfig = positionParam.getPageConfig();
            if(pageConfig.equals("ALL")){
                //控件数据-页码
                SignRuDocControlProperty pageProperty = new SignRuDocControlProperty();
                pageProperty.setId(UUIDGenerator.generate());
                pageProperty.setRuId(createData.getRuId());
                pageProperty.setControlId(control.getId());
                pageProperty.setPropertyType(ControlPropertyTypeEnum.PAGE_CONFIG.getCode());
                pageProperty.setPropertyValue(ControlPropertyTypePageConfigEnum.ALL.getCode());
                //整合数据
                createData.getRuSignControlPropertyList().add(pageProperty);
            }else if(pageConfig.equals("EVEN_NUMBER")){
                //控件数据-页码
                SignRuDocControlProperty pageProperty = new SignRuDocControlProperty();
                pageProperty.setId(UUIDGenerator.generate());
                pageProperty.setRuId(createData.getRuId());
                pageProperty.setControlId(control.getId());
                pageProperty.setPropertyType(ControlPropertyTypeEnum.PAGE_CONFIG.getCode());
                pageProperty.setPropertyValue(ControlPropertyTypePageConfigEnum.EVEN_NUMBER.getCode());
                //整合数据
                createData.getRuSignControlPropertyList().add(pageProperty);
            }else if(pageConfig.equals("ODD_NUMBER")){
                //控件数据-页码
                SignRuDocControlProperty pageProperty = new SignRuDocControlProperty();
                pageProperty.setId(UUIDGenerator.generate());
                pageProperty.setRuId(createData.getRuId());
                pageProperty.setControlId(control.getId());
                pageProperty.setPropertyType(ControlPropertyTypeEnum.PAGE_CONFIG.getCode());
                pageProperty.setPropertyValue(ControlPropertyTypePageConfigEnum.ODD_NUMBER.getCode());
                //整合数据
                createData.getRuSignControlPropertyList().add(pageProperty);
            }else {
                //控件数据-页码
                SignRuDocControlProperty pageProperty = new SignRuDocControlProperty();
                pageProperty.setId(UUIDGenerator.generate());
                pageProperty.setRuId(createData.getRuId());
                pageProperty.setControlId(control.getId());
                pageProperty.setPropertyType(ControlPropertyTypeEnum.PAGE_CONFIG.getCode());
                pageProperty.setPropertyValue(ControlPropertyTypePageConfigEnum.ALL.getCode());
//                SignRuDocControlProperty customProperty = new SignRuDocControlProperty();
//                customProperty.setId(UUIDGenerator.generate());
//                customProperty.setRuId(createData.getRuId());
//                customProperty.setControlId(control.getId());
//                customProperty.setPropertyType(ControlPropertyTypePageConfigEnum.CUSTOM.getCode());
//                customProperty.setPropertyValue("ALL");
                //整合数据
                createData.getRuSignControlPropertyList().add(pageProperty);
//                createData.getRuSignControlPropertyList().add(customProperty);
            }
            //整合数据
            createData.getRuSignControlList().add(control);

        }else {
            //个人签章、组织签章、时间签章
            for(ContractRelationDoc relationDoc : relationDocList) {
                String ruDocId = null;
                if (relationDoc.getDocType().equals("1")) {
                    //上传
                    ruDocId = createData.getAddDocAnnex2RuDocMap().get(relationDoc.getDocId());
                } else if (relationDoc.getDocType().equals("2")) {
                    //模板
                    ruDocId = createData.getTemplateId2RuDocMap().get(relationDoc.getDocId());
                }
                if (ruDocId == null) {
                    throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR, "业务处理失败,签署位置合集关联文档不存在");
                }
                byte[] fileByte = null;
                for (RuDataDoc dataDoc : createData.getDocList()) {
                    if (ruDocId.equals(dataDoc.getRuDoc().getId())) {
                        fileByte = dataDoc.getFileByte();
                    }
                }
                if (fileByte == null) {
                    throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR, "业务处理失败,签署位置合集关联文档不存在");
                }
                //解析文件
                PDPageTree pages  = null ;
                try {
                    PDDocument document = Loader.loadPDF(fileByte);
                    pages = document.getPages();
                }catch (Exception e){
                    throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,解析文件失败");
                }
                if(pages == null || pages.getCount() == 0){
                    throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,解析文件失败");
                }
                //控件属性-页码
                List<Integer> pageList = new ArrayList<>();
                String pageConfig = positionParam.getPageConfig();
                int count = pages.getCount();
                if(pageConfig.equals("ALL")){
                    //全部页
                    for(int i = 0 ; i < count ; i++){
                        pageList.add(i);
                    }
                }else if(pageConfig.equals("EVEN_NUMBER")){
                    //偶数页
                    for(int i = 0 ; i < count ; i++){
                        if(i % 2 == 0){
                            pageList.add(i);
                        }
                    }
                }else if(pageConfig.equals("ODD_NUMBER")){
                    //奇数页
                    for(int i = 0 ; i < count ; i++){
                        if(i % 2 != 0){
                            pageList.add(i);
                        }
                    }
                }else {
                    List<Integer> customPageList = calculatePositionService.parseCustomPage(pageConfig);
                    if(customPageList != null && customPageList.size() > 0){
                        for(Integer page : customPageList){
                            if(page < 0){
                                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,页码配置参数异常");
                            }
                            pageList.add(page);
                        }
                    }
                }
                if(pageList.size() == 0){
                    throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,页码配置参数异常");
                }
                for(Integer page : pageList){
                    Float pdfWidth = null ;
                    Float pdfHeight = null ;
                    PDPage pdPage = pages.get(page);
                    if(pdPage != null){
                        PDRectangle mediaBox = pdPage.getMediaBox();
                        if(mediaBox != null){
                            pdfWidth = mediaBox.getWidth();
                            pdfHeight = mediaBox.getHeight();
                        }
                    }
                    if(pdfWidth == null || pdfHeight == null){
                        throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,页码配置参数异常");
                    }
                    //保留4位小数
                    String offsetX = positionParam.getOffsetX();
                    String offsetY = positionParam.getOffsetY();
                    BigDecimal xRate = null ;
                    BigDecimal yRate = null ;
                    try{
                        if(offsetX != null){
                            xRate = new BigDecimal(offsetX).setScale(4, RoundingMode.HALF_UP);
                        }

                        yRate = new BigDecimal(offsetY).setScale(4, RoundingMode.HALF_UP);
                    }catch (Exception e){
                        throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR, "业务处理失败,偏移量参数解析失败");
                    }
                    if(yRate == null){
                        throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR, "业务处理失败,偏移量参数异常");
                    }
                    //计算坐标
                    float x = pdfWidth - controlWidth;
                    if(xRate != null){
                        x = new BigDecimal(pdfWidth).multiply(xRate).setScale(4, RoundingMode.HALF_UP).floatValue();
                        if(x > (pdfWidth - controlWidth)){
                            x = pdfWidth - controlWidth ;
                        }
                    }
                    float y = new BigDecimal(pdfHeight).multiply(yRate).setScale(4, RoundingMode.HALF_UP).floatValue();
                    if(y > (pdfHeight - controlHeight)){
                        y = pdfHeight - controlHeight ;
                    }
                    //控件数据
                    SignRuDocControl control = new SignRuDocControl();
                    control.setId(UUIDGenerator.generate());
                    //坐标
                    control.setOffsetX(x + "");
                    control.setOffsetY(y + "");
                    //真实pdf文件宽高
                    control.setPageWidth(pdfWidth + "");
                    control.setPageHeight(pdfHeight + "");
                    //控件宽高
                    control.setWidth(controlWidth + "");
                    control.setHeight(controlHeight + "");
                    //控件类型
                    control.setControlType(controlType);
                    //签署日期格式
                    control.setFormat(format);
                    //其他数据
                    control.setSignerId(signerId);
                    control.setSignRuId(createData.getRuId());
                    control.setDeleteFlag(false);
                    control.setOriginType(originType);

                    //控件属性-关联文档
                    SignRuDocControlProperty docControlProperty = new SignRuDocControlProperty();
                    docControlProperty.setId(UUIDGenerator.generate());
                    docControlProperty.setRuId(createData.getRuId());
                    docControlProperty.setControlId(control.getId());
                    docControlProperty.setPropertyType(ControlPropertyTypeEnum.RELATION_DOC.getCode());
                    docControlProperty.setPropertyValue(ruDocId);
                    //控件数据-页码
                    SignRuDocControlProperty pageProperty = new SignRuDocControlProperty();
                    pageProperty.setId(UUIDGenerator.generate());
                    pageProperty.setRuId(createData.getRuId());
                    pageProperty.setControlId(control.getId());
                    pageProperty.setPropertyType(ControlPropertyTypeEnum.PAGE_CONFIG.getCode());
                    pageProperty.setPropertyValue(ControlPropertyTypePageConfigEnum.CUSTOM.getCode());
                    SignRuDocControlProperty customProperty = new SignRuDocControlProperty();
                    customProperty.setId(UUIDGenerator.generate());
                    customProperty.setRuId(createData.getRuId());
                    customProperty.setControlId(control.getId());
                    customProperty.setPropertyType(ControlPropertyTypePageConfigEnum.CUSTOM.getCode());
                    customProperty.setPropertyValue((page + 1) + "");
                    //整合数据
                    createData.getRuSignControlList().add(control);
                    createData.getRuSignControlPropertyList().add(docControlProperty);
                    createData.getRuSignControlPropertyList().add(pageProperty);
                    createData.getRuSignControlPropertyList().add(customProperty);

                }


            }
        }


    }
    public List<SignRuKeywordProperty> buildKeyPropertyAll(String ruId,String keywordId){
        List<SignRuKeywordProperty> propertyList = new ArrayList<>();
        SignRuKeywordProperty property = new SignRuKeywordProperty();
        property.setId(UUIDGenerator.generate());
        property.setRuId(ruId);
        property.setKeywordId(keywordId);
        property.setPropertyType(KeywordPropertyEnum.PAGE_CONFIG.getCode());
        property.setPropertyValue(KeywordPropertyEnum.ALl.getCode());
        propertyList.add(property);
        return propertyList ;
    }

    public List<SignRuKeywordProperty> buildKeyPropertyAsc(String ruId,String keywordId){
        List<SignRuKeywordProperty> propertyList = new ArrayList<>();
        SignRuKeywordProperty property = new SignRuKeywordProperty();
        property.setId(UUIDGenerator.generate());
        property.setRuId(ruId);
        property.setKeywordId(keywordId);
        property.setPropertyType(KeywordPropertyEnum.PAGE_CONFIG.getCode());
        property.setPropertyValue(KeywordPropertyEnum.PART.getCode());
        propertyList.add(property);
        SignRuKeywordProperty property2 = new SignRuKeywordProperty();
        property2.setId(UUIDGenerator.generate());
        property2.setRuId(ruId);
        property2.setKeywordId(keywordId);
        property2.setPropertyType(KeywordPropertyEnum.PAGE_CONFIG.getCode());
        property2.setPropertyValue(KeywordPropertyEnum.ASC.getCode());
        propertyList.add(property2);
        SignRuKeywordProperty property3 = new SignRuKeywordProperty();
        property3.setId(UUIDGenerator.generate());
        property3.setRuId(ruId);
        property3.setKeywordId(keywordId);
        property3.setPropertyType(KeywordPropertyEnum.CUSTOM.getCode());
        property3.setPropertyValue("1");
        propertyList.add(property3);
        return propertyList ;
    }

    public List<SignRuKeywordProperty> buildKeyPropertyDesc(String ruId,String keywordId){
        List<SignRuKeywordProperty> propertyList = new ArrayList<>();
        SignRuKeywordProperty property = new SignRuKeywordProperty();
        property.setId(UUIDGenerator.generate());
        property.setRuId(ruId);
        property.setKeywordId(keywordId);
        property.setPropertyType(KeywordPropertyEnum.PAGE_CONFIG.getCode());
        property.setPropertyValue(KeywordPropertyEnum.PART.getCode());
        propertyList.add(property);
        SignRuKeywordProperty property2 = new SignRuKeywordProperty();
        property2.setId(UUIDGenerator.generate());
        property2.setRuId(ruId);
        property2.setKeywordId(keywordId);
        property2.setPropertyType(KeywordPropertyEnum.PAGE_CONFIG.getCode());
        property2.setPropertyValue(KeywordPropertyEnum.DESC.getCode());
        propertyList.add(property2);
        SignRuKeywordProperty property3 = new SignRuKeywordProperty();
        property3.setId(UUIDGenerator.generate());
        property3.setRuId(ruId);
        property3.setKeywordId(keywordId);
        property3.setPropertyType(KeywordPropertyEnum.CUSTOM.getCode());
        property3.setPropertyValue("1");
        propertyList.add(property3);
        return propertyList ;
    }
















//    /**
//     * @Description #校验模板数据
//     * @Param [request, contractVo]
//     * @return void
//     **/
//    public void checkTemplate(ContractDraftRequest request,ContractVo contractVo){
//        SignRe signRe = contractVo.getSignRe();
//        String reSignerSenderId = contractVo.getReSignerSenderId();
//        //校验模板数据
//        List<SignReDoc> signReDocList = reDocService.listByReId(signRe.getId());
//        List<SignReDocParam> reParamList = reDocParamService.listByReId(signRe.getId());
//
//        //保存传递业务线填写控件参数数据
//        contractVo.setReParamList(reParamList);
//        //保存传递业务线签约文档列表数据
//        contractVo.setSignReDocList(signReDocList);
//
//        Boolean senderNeedWrite = false ;
//        List<String> senderNeedWriteKeyList = new ArrayList<>();
//        if(reParamList != null && reParamList.size() > 0){
//
//            for(SignReDocParam reDocParam : reParamList){
//                if(reDocParam.getSignerId().equals(reSignerSenderId)){
//                    //发起人需要填写
//                    senderNeedWrite = true ;
//                    //填写控件key
//                    senderNeedWriteKeyList.add(reDocParam.getInterfaceParamName());
//                }
//            }
//        }
//        //如果发起人需要填写
//        if(senderNeedWrite && senderNeedWriteKeyList.size() > 0){
//            //校验需要填写的控件是否为必填项
//            if(signReDocList == null || signReDocList.size() == 0){
//                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,业务线中未配置模板");
//            }
//
//            List<String> reTemplateIdList = new ArrayList<>();
//            for(SignReDoc signReDoc : signReDocList){
//                if(signReDoc.getDocType() == SignFileTypeEnum.TEMPLATE.getCode()){
//                    reTemplateIdList.add(signReDoc.getDocOriginId());
//                }
//            }
//            if(reTemplateIdList.size() == 0){
//                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,业务线中未配置模板");
//            }
//            //获取模板控件数据
//            List<SignTemplateControl> reTemplateControlList = templateControlService.getList(reTemplateIdList);
//            if(reTemplateControlList == null || reTemplateControlList.size() == 0){
//                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,业务线模板未配置控件");
//            }
//            Map<String, List<SignTemplateControl>> reTemplateControlMap = reTemplateControlList.stream().collect(Collectors.groupingBy(SignTemplateControl::getTemplateId));
//            if(reTemplateControlMap == null || reTemplateControlMap.size() == 0){
//                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,业务线模板未配置控件");
//            }
//            Boolean senderHasRequired = false ;
//            List<String> requiredKeyList = new ArrayList<>();
//            //遍历模板控件列表
//            for(SignTemplateControl signTemplateControl : reTemplateControlList){
//                //如果是必填项
//                if(signTemplateControl.getIsRequired() == ControlIsRequiredEnum.IS.getCode()){
//                    //如果发起人需要填写的key中包含必填项
//                    if(senderNeedWriteKeyList.contains(signTemplateControl.getInterfaceParamName())){
//                        senderHasRequired = true ;
//                        requiredKeyList.add(signTemplateControl.getInterfaceParamName());
//                    }
//                }
//            }
//            if(senderHasRequired && requiredKeyList.size() > 0){
//                //发起人存在必填项填写控件
//                List<ContractTemplate> signTemplateParamList = request.getSignTemplateParamList();
//                if(signTemplateParamList == null || signTemplateParamList.size() == 0){
//                    throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,业务线模板参数缺失");
//                }
//                for(ContractTemplate contractTemplate : signTemplateParamList){
//                    if(contractTemplate.getTemplateId() == null || contractTemplate.getTemplateId().length() == 0){
//                        throw new RequestParamsException(ApiCode.PARAM_MISSING,"模板id参数缺失");
//                    }
//                    SignTemplate signTemplate = templateService.getById(contractTemplate.getTemplateId());
//                    if(signTemplate == null){
//                        throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,模板id：" + contractTemplate.getTemplateId() + "数据不存在");
//                    }
//                    if(!reTemplateIdList.contains(signTemplate.getId())){
//                        throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,业务线未配置该模板id" + contractTemplate.getTemplateId());
//                    }
//                    if(contractTemplate.getTemplateParamList() == null || contractTemplate.getTemplateParamList().size() == 0){
//                        throw new RequestParamsException(ApiCode.PARAM_MISSING,"模板参数集合参数缺失");
//                    }
//                }
//                for(String key : requiredKeyList){
//                    //默认有异常
//                    Boolean senderWriteRequiredErrorStatus = true ;
//                    Boolean senderWriteRequiredValueErrorStatus = true ;
//                    for(ContractTemplate contractTemplate : signTemplateParamList){
//                        List<ContractTemplateParam> templateParamList = contractTemplate.getTemplateParamList();
//                        if(templateParamList != null && templateParamList.size() > 0){
//                            for(ContractTemplateParam contractTemplateParam : templateParamList){
//                                if(contractTemplateParam.getParamKey().equals(key)){
//                                    //发起人必填项对应到请求中的参数，则无异常了
//                                    senderWriteRequiredErrorStatus = false ;
//                                    if(contractTemplateParam.getParamValue() != null && contractTemplateParam.getParamValue().length() > 0){
//                                        senderWriteRequiredValueErrorStatus = false ;
//                                    }
//
//                                }
//                            }
//                        }
//                    }
//                    if(senderWriteRequiredErrorStatus){
//                        throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,发起方的必填参数未填写完成，无法发起");
//                    }
//                    if(senderWriteRequiredValueErrorStatus){
//                        throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,发起方的必填参数未填写完成，无法发起");
//                    }
//                }
//            }
//        }
//    }

//    public void saveRuWriteControlList(ContractDraftRequest request,String templateId,String reId,RuDataDoc dataDoc){
//        //获取模板控件
//        List<SignTemplateControl> list = templateControlService.getList(templateId);
//        List<ContractTemplateParam> contractTemplateParams = new ArrayList<>();
//        List<ContractTemplate> signTemplateParamList = request.getSignTemplateParamList();
//        if(signTemplateParamList != null && signTemplateParamList.size() > 0){
//            for(ContractTemplate contractTemplate : signTemplateParamList){
//                List<ContractTemplateParam> templateParamList = contractTemplate.getTemplateParamList();
//                if(templateParamList != null && templateParamList.size() > 0){
//                    contractTemplateParams.addAll(templateParamList);
//                }
//            }
//        }
//
//        if(list != null && list.size() > 0){
//            List<SignReDocParam> paramList = reDocParamService.listByReId(reId);
//            //保存模板控件参数
//            for(SignTemplateControl templateControl : list){
//
//                RuDataControl dataControl = new RuDataControl();
//
//                SignRuDocControl ruDocControl = new SignRuDocControl();
//                BeanUtils.copyProperties(templateControl,ruDocControl);
//                //维护业务线配置中配置好的关系
//                if(paramList != null && paramList.size() > 0){
//                    for(SignReDocParam param : paramList){
//                        if(templateControl.getInterfaceParamName().equals(param.getInterfaceParamName())){
//                            dataControl.setReDocId(param.getSignReDocId());
//                            dataControl.setReSignerId(param.getSignerId());
//                        }
//                    }
//                }
//                if(contractTemplateParams != null && contractTemplateParams.size() > 0){
//                    for(ContractTemplateParam param : contractTemplateParams){
//                        if(templateControl.getInterfaceParamName().equals(param.getParamKey())){
//                            if(templateControl.getType() != null & templateControl.getType().equals(ControlTypeEnum.IMAGE.getName())){
//                                byte[] decode = Base64.getDecoder().decode(param.getParamValue());
//                                String annexId = signFileService.saveAnnexStorage(decode, SignFileEnum.SIGN_FILE_IMAGE, null);
//                                ruDocControl.setValue(annexId);
//                            }else {
//                                ruDocControl.setValue(param.getParamValue());
//                            }
//
//                        }
//                    }
//                }
//                //封装控件其他参数
//                ruDocControl.setControlType(templateControl.getType());
//                ruDocControl.setOriginType(ControlOriginTypeEnum.RE.getCode());
//                ruDocControl.setInterfaceParamName(templateControl.getInterfaceParamName());
//                ruDocControl.setDeleteFlag(false);
//                ruDocControl.setId(null);
//
//                dataControl.setRuDocControl(ruDocControl);
//
//                dataDoc.getWriteControlList().add(dataControl);
//            }
//        }
//
//    }
//
//    /**
//     * @Description #保存控件数据
//     * @Param [request, contractVo]
//     * @return void
//     **/
//    public void saveRuControlList(ContractDraftRequest request,ContractVo contractVo,RuCreateData createData){
//
//        //存储签署控件数据
//        List<SignReDocControl> signReDocControlList = reDocControlService.listByParam(contractVo.getSignRe().getId());
//        if(signReDocControlList != null && signReDocControlList.size() > 0){
//            List<String> reControlIdList = signReDocControlList.stream().map(SignReDocControl::getId).collect(Collectors.toList());
//            Map<String, List<SignReDocControlProperty>> rePropertyMap = null ;
//            if(reControlIdList != null && reControlIdList.size() > 0){
//                List<SignReDocControlProperty> reDocControlPropertyList = reDocControlPropertyService.listByControlIdList(reControlIdList);
//                if(reDocControlPropertyList != null && reDocControlPropertyList.size() > 0){
//                    rePropertyMap = reDocControlPropertyList.stream().collect(Collectors.groupingBy(SignReDocControlProperty::getControlId));
//                }
//            }
//            for(SignReDocControl reDocControl : signReDocControlList){
//
//                RuDataControl ruDataControl = new RuDataControl();
//
//                SignRuDocControl ruDocControl = new SignRuDocControl();
//                BeanUtils.copyProperties(reDocControl,ruDocControl);
//                ruDocControl.setOriginType(ControlOriginTypeEnum.RE.getCode());
//                ruDocControl.setDeleteFlag(false);
//                ruDocControl.setId(UUID.randomUUID().toString());
//                if(rePropertyMap != null && rePropertyMap.containsKey(reDocControl.getId())){
//                    List<SignReDocControlProperty> reProperties = rePropertyMap.get(reDocControl.getId());
//                    if(reProperties != null && reProperties.size() > 0){
//                        for(SignReDocControlProperty reDocControlProperty : reProperties){
//
//                            RuDataControlProperty dataControlProperty = new RuDataControlProperty();
//
//                            SignRuDocControlProperty ruDocControlProperty = new SignRuDocControlProperty();
//                            ruDocControlProperty.setControlId(ruDocControl.getId());
//                            ruDocControlProperty.setPropertyType(reDocControlProperty.getPropertyType());
//                            if(ruDocControlProperty.getPropertyType().equals(ControlPropertyTypeEnum.RELATION_DOC.getCode())){
//                                dataControlProperty.setReDocId(reDocControl.getSignReDocId());
//                            }else {
//                                ruDocControlProperty.setPropertyValue(reDocControlProperty.getPropertyValue());
//                            }
//
//                            dataControlProperty.setRuDocControlProperty(ruDocControlProperty);
//                            ruDataControl.getDataControlPropertyList().add(dataControlProperty);
//                        }
//                    }
//                }
//
//                ruDataControl.setReDocId(reDocControl.getSignReDocId());
//                ruDataControl.setReSignerId(reDocControl.getSignerId());
//                ruDataControl.setRuDocControl(ruDocControl);
//                createData.getSignControlList().add(ruDataControl);
//            }
//        }
//    }
}