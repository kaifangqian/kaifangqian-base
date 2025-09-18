/**
 * @description 签署报告生成器
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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kaifangqian.modules.opensign.entity.*;
import com.kaifangqian.modules.opensign.enums.*;
import com.kaifangqian.modules.opensign.service.business.vo.SignReportSignSubVO;
import com.kaifangqian.modules.opensign.service.ru.*;
import com.kaifangqian.modules.system.entity.CertificateInfo;
import com.kaifangqian.modules.system.entity.SysTenantInfo;
import com.kaifangqian.modules.system.entity.SysTenantUser;
import com.kaifangqian.modules.system.entity.SysUser;
import com.kaifangqian.modules.system.enums.TenantType;
import com.kaifangqian.modules.system.service.*;
import com.kaifangqian.modules.system.vo.TenantInfoDTO;
import com.kaifangqian.common.base.entity.BaseEntity;
import com.kaifangqian.common.util.DesensitizationUtils;
import com.kaifangqian.config.FileProperties;
import com.kaifangqian.modules.opensign.entity.*;
import com.kaifangqian.modules.opensign.enums.*;
import com.kaifangqian.modules.opensign.service.business.vo.SignReportDocFileVo;
import com.kaifangqian.modules.opensign.service.confirm.IUserConfirmService;
import com.kaifangqian.modules.opensign.service.ru.*;
import com.kaifangqian.modules.opensign.service.tool.SignFileService;
import com.kaifangqian.modules.opensign.service.tool.WatermarkUtil;
import com.kaifangqian.modules.storage.entity.AnnexStorage;
import com.kaifangqian.modules.system.service.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: RuSignReportService
 * @Package: com.kaifangqian.modules.opensign.service.business
 * @ClassName: RuSignReportService
 * @author: FengLai_Gong
 */
@Service
public class RuSignReportService {

    @Autowired
    private SignRuService ruService;
    @Autowired
    private RuBusinessService ruBusinessService;

    @Autowired
    private SignRuSignerService ruSignerService ;
    @Autowired
    private SignRuSenderService ruSenderService ;

    @Autowired
    private SignRuTaskService ruTaskService ;

    @Autowired
    private SignRuDocOperateService ruDocOperateService ;

    @Autowired
    private SignRuOperateRecordService ruOperateRecordService ;
    @Autowired
    private SignRuOperateDocRecordService ruOperateDocRecordService ;

    @Autowired
    private ISysUserService userService ;
    @Autowired
    private ISysTenantInfoService tenantInfoService;
    @Autowired
    private ISysTenantUserService tenantUserService;


    @Autowired
    private CertificateInfoDao certificateInfoDao;


    @Autowired
    private SignFileService signFileService;

    @Autowired
    private IUserConfirmService userConfirmService ;
    @Autowired
    private ITenantInfoExtendService tenantInfoExtendService ;

    @Autowired
    private FileProperties fileProperties;



    /**
     * @return
     * @Description #获取基本信息
     * @Param [ruId]
     **/
    public List<SignReportSignSubVO> baseInfo(String ruId){
        List<SignReportSignSubVO> returnList = new ArrayList<>();

        SignRu signRu = ruService.getById(ruId);
        if(signRu == null){
            return returnList ;
        }
        SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat second = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //基本信息
        SignReportSignSubVO baseInfo = new SignReportSignSubVO();

        List<SignReportDocFileVo> baseInfoList = new ArrayList<>();
        baseInfoList.add(new SignReportDocFileVo("合同主题：",signRu.getSubject()));
        baseInfoList.add(new SignReportDocFileVo("合同编号：",signRu.getCode()));

        //合同发起时间
        List<String> operateTypeList = new ArrayList<>();
        operateTypeList.add(SignRecordOperateTypeEnum.START.getType());
        List<SignRuOperateRecord> signRuOperateRecords = ruOperateRecordService.listByOperateList(signRu.getId(), operateTypeList);
        if(signRuOperateRecords != null && signRuOperateRecords.size() > 0){
            SignRuOperateRecord ruOperateRecord = signRuOperateRecords.get(0);
            if(ruOperateRecord != null && ruOperateRecord.getOperateTime() != null){
                try {
                    String format = second.format(ruOperateRecord.getOperateTime());
                    baseInfoList.add(new SignReportDocFileVo("合同发起时间：",format));
                }catch (Exception e){
                    baseInfoList.add(new SignReportDocFileVo("合同发起时间：",""));
                }
            }
        }
        if(signRu.getExpireDate() != null){
            try {
                String format = day.format(signRu.getExpireDate());
                baseInfoList.add(new SignReportDocFileVo("签署截止时间：",format));
            }catch (Exception e){
                baseInfoList.add(new SignReportDocFileVo("签署截止时间：",""));
            }
        }
        if(signRu.getFinishTime() != null){
            try {
                String format = second.format(signRu.getFinishTime());
                baseInfoList.add(new SignReportDocFileVo("签署完成时间：",format));
            }catch (Exception e){
                baseInfoList.add(new SignReportDocFileVo("签署完成时间：",""));
            }
        }

        List<SignRuOperateRecord> signRuOperateRecordList = ruOperateRecordService.listByRuId(ruId);
        SignRuOperateRecord sender = null ;
        List<SignRuOperateRecord> signList = new ArrayList<>();
        for(SignRuOperateRecord record : signRuOperateRecordList){
            if(record.getActionType() != null ){
                if(record.getActionType().equals(SignRecordActionTypeEnum.START.getType())){
                    sender = record ;
                }else if(record.getActionType().equals(SignRecordActionTypeEnum.SUBMIT_SIGN.getType())
                        || record.getActionType().equals(SignRecordActionTypeEnum.AUTO_SIGN.getType())){
                    signList.add(record);
                }
            }
        }
        baseInfoList.add(new SignReportDocFileVo("签署次数：",signList.size() + ""));
        String senderName = "" ;
        if(sender != null){
            SysTenantInfo tenantInfo = tenantInfoService.getById(sender.getTenantId());
            if(tenantInfo != null){
                baseInfoList.add(new SignReportDocFileVo("发起方：",tenantInfo.getTenantName()));
                senderName = tenantInfo.getTenantName();
            }
        }
        List<SignRuSigner> signerList = ruSignerService.listByRuId(signRu.getId());
        if(signerList != null && signerList.size() > 0){
            signerList = signerList.stream().sorted(Comparator.comparing(SignRuSigner::getSignerOrder)).collect(Collectors.toList());
            String name = "";
            for(int j = 0 ; j < signerList.size() ; j++){
                String signerName = "";
                SignRuSigner signer = signerList.get(j);
                if(signer.getSignerType() == SignerTypeEnum.SENDER.getCode()){
                    //发起方
                    List<SignRuSender> signRuSenderList = ruSenderService.listBySignerId(signer.getId());
                    if(signRuSenderList == null || signRuSenderList.size() == 0){
                        continue;
                    }
                    signRuSenderList = signRuSenderList.stream().sorted(Comparator.comparing(SignRuSender::getSenderOrder)).collect(Collectors.toList());
                    String senderListName = "(";
                    for(int i = 0 ; i < signRuSenderList.size() ; i++){
                        String nodeName = null;
                        SignRuSender ruSender = signRuSenderList.get(i);
                        if(ruSender.getSenderType() == SenderTypeEnum.ENTERPRISE.getCode()){
                            nodeName = SenderTypeEnum.ENTERPRISE.getName() ;
                        }else {
                            String personalTenantName = null ;
                            SignRuTask signTask = findSignTask(signRu.getId(), ruSender.getId());
                            if(signTask != null){
                                String userId = signTask.getUserId();
                                SysTenantUser personalTenantUser = tenantUserService.getPersonalTenantUser(userId);
                                if(personalTenantUser != null){
                                    personalTenantName = personalTenantUser.getNickName();
                                }
                            }
                            if(personalTenantName == null){
                                continue;
                            }
                            if(ruSender.getSenderType() == SenderTypeEnum.OPERATOR.getCode()){
                                nodeName = SenderTypeEnum.OPERATOR.getName() + "[" + personalTenantName + "]";
                            }else if(ruSender.getSenderType() == SenderTypeEnum.LEGAL_PERSON.getCode()){
                                nodeName = SenderTypeEnum.LEGAL_PERSON.getName() + "[" + personalTenantName + "]";
                            }else if(ruSender.getSenderType() == SenderTypeEnum.PERSONAL.getCode()){
                                nodeName = SenderTypeEnum.PERSONAL.getName() + "[" + personalTenantName + "]";
                            }
                        }
                        if(nodeName == null){
                            continue;
                        }
                        if(i == signRuSenderList.size() - 1){
                            senderListName = senderListName + nodeName + ")";
                        }else {
                            senderListName = senderListName + nodeName + "、";
                        }
                    }
                    signerName = senderName + senderListName;
                }else if(signer.getSignerType() == SignerTypeEnum.RECEIVER_PERSONAL.getCode()){
                    //个人接收方
                    SignRuTask signTask = findSignTask(signRu.getId(), signer.getId());
                    if(signTask != null){
                        String userId = signTask.getUserId();
                        SysTenantUser personalTenantUser = tenantUserService.getPersonalTenantUser(userId);
                        if(personalTenantUser != null){
                            signerName = personalTenantUser.getNickName();
                        }
                    }
                }else if(signer.getSignerType() == SignerTypeEnum.RECEIVER_ENT.getCode()){
                    String entSenderName = null ;
                    //企业接收方
                    List<SignRuSender> signRuSenderList = ruSenderService.listBySignerId(signer.getId());
                    if(signRuSenderList == null || signRuSenderList.size() == 0){
                        continue;
                    }
                    signRuSenderList = signRuSenderList.stream().sorted(Comparator.comparing(SignRuSender::getSenderOrder)).collect(Collectors.toList());
                    String entSenderListName = "(";
                    for(int i = 0 ; i < signRuSenderList.size() ; i++){
                        String nodeName = null;
                        SignRuSender ruSender = signRuSenderList.get(i);
                        String personalTenantName = null ;
                        SignRuTask signTask = findSignTask(signRu.getId(), ruSender.getId());
                        if(signTask != null){
                            String userId = signTask.getUserId();
                            SysTenantUser personalTenantUser = tenantUserService.getPersonalTenantUser(userId);
                            if(personalTenantUser != null){
                                personalTenantName = personalTenantUser.getNickName();
                            }
                            if(entSenderName == null){
                                entSenderName = signTask.getTenantName();
                            }
                        }
                        if(personalTenantName == null){
                            continue;
                        }
                        if(ruSender.getSenderType() == SenderTypeEnum.ENTERPRISE.getCode()){
                            nodeName = SenderTypeEnum.ENTERPRISE.getName() ;
                        }else {
                            if(ruSender.getSenderType() == SenderTypeEnum.OPERATOR.getCode()){
                                nodeName = SenderTypeEnum.OPERATOR.getName() + "[" + personalTenantName + "]";
                            }else if(ruSender.getSenderType() == SenderTypeEnum.LEGAL_PERSON.getCode()){
                                nodeName = SenderTypeEnum.LEGAL_PERSON.getName() + "[" + personalTenantName + "]";
                            }else if(ruSender.getSenderType() == SenderTypeEnum.PERSONAL.getCode()){
                                nodeName = SenderTypeEnum.PERSONAL.getName() + "[" + personalTenantName + "]";
                            }
                        }
                        if(nodeName == null){
                            continue;
                        }
                        if(i == signRuSenderList.size() - 1){
                            entSenderListName = entSenderListName + nodeName + ")";
                        }else {
                            entSenderListName = entSenderListName + nodeName + "、";
                        }
                    }
                    signerName = entSenderName + entSenderListName;
                }
                if(j == signerList.size() - 1){
                    name = name + signerName ;
                }else {
                    name = name + signerName + "｜" ;
                }
            }
            baseInfoList.add(new SignReportDocFileVo("签署方：",name));
        }
        baseInfo.setSubReport(baseInfoList);
        returnList.add(baseInfo);
        //文件信息
        List<SignRuDocOperate> ruDocOperateList = ruDocOperateService.listByRuIdCurrent(ruId);
        SignReportSignSubVO fileInfo = new SignReportSignSubVO();
        fileInfo.setTitle("电子文件清单");
        if(ruDocOperateList != null && ruDocOperateList.size() > 0) {

            List<SignReportDocFileVo> fileInfoList = new ArrayList<>();
            for(int i = 0 ; i < ruDocOperateList.size() ; i++){
                SignRuDocOperate ruDocOperate = ruDocOperateList.get(i);
                if(ruDocOperate.getAnnexId() != null){
                    AnnexStorage annex = signFileService.getAnnexById(ruDocOperate.getAnnexId());
                    if(annex != null){
                        fileInfoList.add(new SignReportDocFileVo("文件" + (i + 1) + "：",annex.getRealName()));
                    }
                    byte[] fileByte = signFileService.getByteById(ruDocOperate.getAnnexId());
                    if(fileByte != null){
                        String hash = DigestUtils.sha256Hex(fileByte);
                        fileInfoList.add(new SignReportDocFileVo("完成文件hash：",hash));
                    }
                }
            }
            fileInfo.setSubReport(fileInfoList);
        }
        returnList.add(fileInfo);
        return returnList ;
    }

    public SignRuTask findSignTask(String ruId,String signerId){
        QueryWrapper<SignRuTask> ruTaskQueryWrapper = new QueryWrapper<>();
        ruTaskQueryWrapper.lambda().eq(SignRuTask::getSignRuId,ruId);
        ruTaskQueryWrapper.lambda().eq(SignRuTask::getUserTaskId,signerId);
        ruTaskQueryWrapper.lambda().eq(SignRuTask::getTaskType, TaskTypeEnum.SIGN_TASK.getCode());
        ruTaskQueryWrapper.lambda().eq(SignRuTask::getTaskStatus,2);
        ruTaskQueryWrapper.lambda().eq(BaseEntity::getDeleteFlag,0);
        List<SignRuTask> list = ruTaskService.list(ruTaskQueryWrapper);
        if(list == null || list.size() == 0){
            return null ;
        }
        return list.get(0);
    }

    /**
     * @return
     * @Description #获取签署流程信息
     * @Param [ruId]
     **/
    public List<SignReportSignSubVO> flowInfo(String ruId) {
        List<SignReportSignSubVO> returnList = new ArrayList<>();

        SignRu signRu = ruService.getById(ruId);
        if(signRu == null){
            return returnList ;
        }
        SimpleDateFormat second = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<SignRuOperateRecord> signRuOperateRecordList = ruOperateRecordService.listByRuId(ruId);

        SignRuOperateRecord starter = null ;
        List<SignRuOperateRecord> signList = new ArrayList<>();
        for(SignRuOperateRecord record : signRuOperateRecordList){
            if(record.getActionType() != null ){
                if(record.getActionType().equals(SignRecordActionTypeEnum.START.getType())){
                    starter = record ;
                }else if(record.getActionType().equals(SignRecordActionTypeEnum.SUBMIT_SIGN.getType())
                        || record.getActionType().equals(SignRecordActionTypeEnum.AUTO_SIGN.getType())){
                    signList.add(record);
                }
            }
        }
        //发起
        if(starter != null){
            SignReportSignSubVO starterVo = new SignReportSignSubVO();
            starterVo.setTitle("发起");
            starterVo.setFontSize(14);
            List<SignReportDocFileVo> startList = new ArrayList<>();
            SysTenantInfo tenantInfo = tenantInfoService.getById(starter.getTenantId());
            if(tenantInfo != null){
                startList.add(new SignReportDocFileVo("操作主体：",tenantInfo.getTenantName()));
                startList.add(new SignReportDocFileVo("操作主体ID：",tenantInfo.getId()));
            }
            SysUser sysUser = userService.getById(starter.getAccountId());
            SysTenantUser tenantUser = tenantUserService.getById(starter.getTenantUserId());
            if(sysUser != null && tenantUser != null){
                startList.add(new SignReportDocFileVo("操作人：",tenantUser.getNickName() + " " + DesensitizationUtils.getPhone(sysUser.getUsername())));
                startList.add(new SignReportDocFileVo("操作人ID：",sysUser.getId()));
            }
            if(starter.getOperateTime() != null){
                try {
                    String format = second.format(starter.getOperateTime());
                    startList.add(new SignReportDocFileVo("操作时间：",format));
                }catch (Exception e){
                    startList.add(new SignReportDocFileVo("操作时间：",""));
                }

            }
            startList.add(new SignReportDocFileVo("IP地址：",starter.getIpAddr()));
            starterVo.setSubReport(startList);
            returnList.add(starterVo);
        }
        //签署人
        if(signList.size() > 0){
            signList = signList.stream().sorted(Comparator.comparing(SignRuOperateRecord::getOperateTime)).collect(Collectors.toList());
            for(int i = 0 ; i < signList.size() ; i++){
                SignRuOperateRecord signOperateRecord = signList.get(i);
                //签署节点
                SignReportSignSubVO signOperateVo = new SignReportSignSubVO();
                signOperateVo.setTitle("签署" + (i + 1));
                signOperateVo.setFontSize(14);
                List<SignReportDocFileVo> signOperateList = new ArrayList<>();
                //操作主体
                SysTenantInfo tenantInfo = tenantInfoService.getById(signOperateRecord.getTenantId());
                if(tenantInfo == null){
                    continue;
                }
                Integer tenantType = null ;
                //签章宽度默认为组织签章，宽度为100
//                Integer imageWidth = 100 ;
                Integer fontSize = 10 ;
                //自动盖章
                if(signOperateRecord.getActionType() != null && signOperateRecord.getActionType().equals(SignRecordActionTypeEnum.AUTO_SIGN.getType())){
                    //操作主体
                    signOperateList.add(new SignReportDocFileVo("操作主体：",tenantInfo.getTenantName()));
                    signOperateList.add(new SignReportDocFileVo("操作主体ID：",tenantInfo.getId()));
                    signOperateList.add(new SignReportDocFileVo("签署类型：","自动盖章"));
                    tenantType = TenantType.GROUP.getType();
                }else {
                    //企业签章
                    if(signOperateRecord.getOperateType() != null && signOperateRecord.getOperateType().equals(SignRecordOperateTypeEnum.ENT_SIGN.getType())){
                        //操作主体
                        signOperateList.add(new SignReportDocFileVo("操作主体：",tenantInfo.getTenantName()));
                        signOperateList.add(new SignReportDocFileVo("操作主体ID：",tenantInfo.getId()));

                        signOperateList.add(new SignReportDocFileVo("操作节点：","企业签章"));
                        signOperateList.add(new SignReportDocFileVo("签署类型：","手动签署"));
                        SysTenantUser tenantUser = tenantUserService.getById(signOperateRecord.getTenantUserId());
                        SysUser sysUser = userService.getById(signOperateRecord.getAccountId());
                        if(tenantUser != null && sysUser != null){
                            signOperateList.add(new SignReportDocFileVo("操作人：",tenantUser.getNickName() + " " + DesensitizationUtils.getPhone(sysUser.getUsername())));
                            signOperateList.add(new SignReportDocFileVo("操作人ID：",signOperateRecord.getTenantUserId()));
                        }
                        tenantType = TenantType.GROUP.getType();
                    }
                    //个人签名
                    else {

//                        SysTenantUser tenantUser = tenantUserService.getById(signOperateRecord.getTenantUserId());
//                        SysUser sysUser = userService.getById(signOperateRecord.getAccountId());

                        SignRuTask task = ruTaskService.getById(signOperateRecord.getTaskId());
                        if(task != null){
                            Integer userType = task.getUserType();
                            if(userType == SignerTypeEnum.RECEIVER_PERSONAL.getCode()){
                                SysTenantUser personalTenantUser = tenantUserService.getPersonalTenantUser(signOperateRecord.getAccountId());
                                if(personalTenantUser != null){
                                    //操作主体
                                    signOperateList.add(new SignReportDocFileVo("操作主体：",personalTenantUser.getNickName()));
                                    signOperateList.add(new SignReportDocFileVo("操作节点：","个人签名"));
                                    TenantInfoDTO tenantInfoExt = tenantInfoExtendService.getTenantInfoExt(personalTenantUser.getTenantId());
                                    if(tenantInfoExt != null){
                                        String name = tenantInfoExt.getName();
                                        if(name == null){
                                            name = "" ;
                                        }
                                        String phone = tenantInfoExt.getPhone();
                                        if(phone != null){
                                            phone = DesensitizationUtils.getPhone(phone);
                                        }else {
                                            phone = "" ;
                                        }
                                        String organizationNo = tenantInfoExt.getOrganizationNo();
                                        if(organizationNo != null){
                                            organizationNo = DesensitizationUtils.getIdNumber(organizationNo);
                                        }else {
                                            organizationNo = "" ;
                                        }

                                        signOperateList.add(new SignReportDocFileVo("操作人：",name + " " + phone + " " + organizationNo));
                                        signOperateList.add(new SignReportDocFileVo("操作人ID：",signOperateRecord.getTenantUserId()));
                                    }
                                }
                            }else {
                                SysTenantInfo sysTenantInfo = tenantInfoService.getById(task.getTenantId());
                                SysTenantUser personalTenantUser = tenantUserService.getPersonalTenantUser(signOperateRecord.getAccountId());
                                if(personalTenantUser != null && sysTenantInfo != null){
                                    //操作主体
                                    signOperateList.add(new SignReportDocFileVo("操作主体：",personalTenantUser.getNickName() + "(" + sysTenantInfo.getTenantName() + ")"));
                                    signOperateList.add(new SignReportDocFileVo("操作节点：","个人签名"));
                                    TenantInfoDTO tenantInfoExt = tenantInfoExtendService.getTenantInfoExt(personalTenantUser.getTenantId());
                                    if(tenantInfoExt != null){
                                        String name = tenantInfoExt.getName();
                                        if(name == null){
                                            name = "" ;
                                        }
                                        String phone = tenantInfoExt.getPhone();
                                        if(phone != null){
                                            phone = DesensitizationUtils.getPhone(phone);
                                        }else {
                                            phone = "" ;
                                        }
                                        String organizationNo = tenantInfoExt.getOrganizationNo();
                                        if(organizationNo != null){
                                            organizationNo = DesensitizationUtils.getIdNumber(organizationNo);
                                        }else {
                                            organizationNo = "" ;
                                        }

                                        signOperateList.add(new SignReportDocFileVo("操作人：",name + " " + phone + " " + organizationNo));
                                        signOperateList.add(new SignReportDocFileVo("操作人ID：",signOperateRecord.getTenantUserId()));
                                    }
                                }
                            }
                        }




                        //个人签章，宽度为60
//                        imageWidth = 60 ;
                        fontSize = 6 ;
                        tenantType = TenantType.PERSONAL.getType();
                    }
                }
                if(signOperateRecord.getOperateTime() != null){
                    try {
                        String format = second.format(signOperateRecord.getOperateTime());
                        signOperateList.add(new SignReportDocFileVo("操作时间：",format));
                    }catch (Exception e){
                        signOperateList.add(new SignReportDocFileVo("操作时间：",""));
                    }
                }
                if(signOperateRecord.getIpAddr() != null){
                    signOperateList.add(new SignReportDocFileVo("IP地址：",signOperateRecord.getIpAddr()));
                }else {
                    signOperateList.add(new SignReportDocFileVo("IP地址：",""));
                }

                if(signOperateRecord.getImageAnnexId() != null){
                    byte[] imageByte = signFileService.getByteById(signOperateRecord.getImageAnnexId());
                    if(imageByte != null){
//                        watermark = WatermarkUtil.resizeAndWaterMark(imageByte, imageWidth ,"仅用于签署记录报告",fontSize);
                        String fontPath = fileProperties.getPath().getPath() + "simsun.ttc";
                        imageByte = WatermarkUtil.watermark(imageByte, "仅用于签署记录报告",fontPath);
                    }
                    if(imageByte != null){
                        signOperateList.add(new SignReportDocFileVo("签章图片：", Base64.getEncoder().encodeToString(imageByte)));
                    }
                }
                signOperateVo.setSubReport(signOperateList);
                returnList.add(signOperateVo);
                //证书

                CertificateInfo certificateInfo = certificateInfoDao.getById(signOperateRecord.getCertId());
                if(certificateInfo != null){
                    SignReportSignSubVO certVo = new SignReportSignSubVO();
                    certVo.setTitle("数字证书信息");
                    List<SignReportDocFileVo> certList = new ArrayList<>();
                    certList.add(new SignReportDocFileVo("证书类型：","事件证书"));
                    certList.add(new SignReportDocFileVo("证书状态：","有效"));
                    certList.add(new SignReportDocFileVo("颁发机构：",certificateInfo.getIssueOrg()));
                    certList.add(new SignReportDocFileVo("签名算法：","SHA256WITHRSA"));
                    certList.add(new SignReportDocFileVo("序列号：",certificateInfo.getCertSuqeNo()));

                    if(tenantType == TenantType.PERSONAL.getType()){
                        SysTenantUser personalTenantUser = tenantUserService.getPersonalTenantUser(signOperateRecord.getAccountId());
                        if(personalTenantUser != null){
                            String tenantId = personalTenantUser.getTenantId();
                            TenantInfoDTO tenantInfoExt = tenantInfoExtendService.getTenantInfoExt(tenantId);
                            if(tenantInfoExt != null){
                                String name = tenantInfoExt.getName();
                                if(name == null){
                                    name = "" ;
                                }
                                String phone = tenantInfoExt.getPhone();
                                if(phone != null){
                                    phone = DesensitizationUtils.getPhone(phone);
                                }else {
                                    phone = "" ;
                                }
                                String organizationNo = tenantInfoExt.getOrganizationNo();
                                if(organizationNo != null){
                                    organizationNo = DesensitizationUtils.getIdNumber(organizationNo);
                                }else {
                                    organizationNo = "" ;
                                }
                                certList.add(new SignReportDocFileVo("主体信息：", name + "@" + phone + "@" + organizationNo));

                            }
                        }

                    }else {
                        //证书主体信息
                        TenantInfoDTO tenantInfoExt = tenantInfoExtendService.getTenantInfoExt(signOperateRecord.getTenantId());
                        if(tenantInfoExt != null){
                            //企业证书
                            String name = tenantInfoExt.getName();
                            if(name == null){
                                name = "" ;
                            }
                            String organizationNo = tenantInfoExt.getOrganizationNo();
                            if(organizationNo != null){
                                organizationNo = DesensitizationUtils.getIdNumber(organizationNo);
                            }else {
                                organizationNo = "" ;
                            }
                            certList.add(new SignReportDocFileVo("主体信息：", name + "@" + organizationNo));
                        }
                    }


                    String startTime = "" ;
                    if(certificateInfo.getTermOfValidityStartTime() != null){
                        try {
                            startTime = second.format(certificateInfo.getTermOfValidityStartTime());
                        }catch (Exception e){
                            startTime = "-" ;
                        }
                    }
                    String endTime = "" ;
                    if(certificateInfo.getTermOfValidityEndTime() != null){
                        try {
                            endTime = second.format(certificateInfo.getTermOfValidityEndTime());
                        }catch (Exception e){
                            endTime = "-" ;
                        }
                    }
                    certList.add(new SignReportDocFileVo("有效期： ",startTime + " 至 " + endTime));

                    certVo.setSubReport(certList);
                    returnList.add(certVo);
                }

                //签署意愿
                if(signOperateRecord.getTaskId() != null){
                    SignReportSignSubVO confirmInfoByTaskId = userConfirmService.getConfirmInfoByTaskId(signOperateRecord.getTaskId());
                    if(confirmInfoByTaskId != null){
                        if(confirmInfoByTaskId.getSubReport() == null){
                            confirmInfoByTaskId.setSubReport(new ArrayList<>());
                        }
                        if(confirmInfoByTaskId.getTitle() == null){
                            confirmInfoByTaskId.setTitle("");
                        }
                        returnList.add(confirmInfoByTaskId);
                    }
                }
                //文件
                List<SignRuOperateDocRecord> operateDocRecordList = ruOperateDocRecordService.listByOperateRecordId(signOperateRecord.getId());
                if(operateDocRecordList != null && operateDocRecordList.size() > 0){
                    SignReportSignSubVO fileVo = new SignReportSignSubVO();
                    fileVo.setTitle("电子文件清单");
                    List<SignReportDocFileVo> fileVoList = new ArrayList<>();
                    for(int fileIndex = 0 ; fileIndex < operateDocRecordList.size() ; fileIndex++){
                        SignRuOperateDocRecord operateDocRecord = operateDocRecordList.get(fileIndex);

                        String fileName = null;
                        String previousHash = null ;
                        String currentHash = null ;

                        if(operateDocRecord.getPreviousDocOperateAnnexId() != null){
                            byte[] fileByte = signFileService.getByteById(operateDocRecord.getPreviousDocOperateAnnexId());
                            if(fileByte != null){
                                previousHash = DigestUtils.sha256Hex(fileByte);
                            }
                        }
                        if(operateDocRecord.getCurrentDocOperateAnnexId() != null){
                            AnnexStorage annex = signFileService.getAnnexById(operateDocRecord.getCurrentDocOperateAnnexId());
                            if(annex != null && annex.getRealName() != null){
                                fileName = annex.getRealName() ;
                            }
                            byte[] fileByte = signFileService.getByteById(operateDocRecord.getCurrentDocOperateAnnexId());
                            if(fileByte != null){
                                currentHash = DigestUtils.sha256Hex(fileByte);
                            }
                        }
                        if(fileName == null){
                            fileName = "" ;
                        }
                        if(previousHash == null){
                            previousHash = "";
                        }
                        if(currentHash == null){
                            currentHash = "" ;
                        }
                        fileVoList.add(new SignReportDocFileVo("文件" + (fileIndex + 1) + "：",fileName));
                        fileVoList.add(new SignReportDocFileVo("签署前文件hash：",previousHash));
                        fileVoList.add(new SignReportDocFileVo("签署后文件hash：" ,currentHash));

                    }
                    fileVo.setSubReport(fileVoList);
                    returnList.add(fileVo);
                }

            }
        }


        return returnList;
    }


    /**
     * @return byte[]
     * @Description #签署报告盖章
     * @Param [reportByte]
     **/
    public byte[] signReport(byte[] reportByte) {
        byte[] bytes = null ;
        try {
            bytes = ruBusinessService.signReportFile(reportByte);
        }catch (Exception e){
            e.printStackTrace();
        }
        return bytes;
    }


    /**
     * @return
     * @Description #获取签署流程中所有租户ID
     * @Param [ruId]
     **/
    public Set<String> getAllTenantIds(String ruId) {
        List<SignRuOperateRecord> signRuOperateRecordList = ruOperateRecordService.listByRuId(ruId);

        Set<String> tenantIdSet = new HashSet<>();
        for(SignRuOperateRecord record : signRuOperateRecordList){
            if(record.getActionType() != null ){

                if(record.getOperateType().equals(SignRecordOperateTypeEnum.ENT_SIGN.getType())){
                    //组织签署，使用企业租户id
                    tenantIdSet.add(record.getTenantId());
                }else if(record.getOperateType().equals(SignRecordOperateTypeEnum.PRIVATE_SIGN.getType())){
                    //个人签章，使用个人租户id
                    SysTenantUser personalTenantUser = tenantUserService.getPersonalTenantUser(record.getAccountId());
                    if(personalTenantUser != null){
                        tenantIdSet.add(personalTenantUser.getTenantId());
                    }
                }
            }
        }

        return tenantIdSet;
    }


}