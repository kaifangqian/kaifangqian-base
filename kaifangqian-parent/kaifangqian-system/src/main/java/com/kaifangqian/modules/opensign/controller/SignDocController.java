//package com.kaifangqian.modules.opensign.controller;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.kaifangqian.annotation.ResrunLogModule;
//import com.kaifangqian.common.base.entity.BaseEntity;
//import com.kaifangqian.common.system.vo.LoginUser;
//import com.kaifangqian.common.util.MySecurityUtils;
//import com.kaifangqian.common.vo.Result;
//import com.kaifangqian.modules.opensign.entity.*;
//import com.kaifangqian.modules.opensign.enums.DocBusinessTypeEnum;
//import com.kaifangqian.modules.opensign.enums.DocStatusEnum;
//import com.kaifangqian.modules.opensign.enums.SignFileEnum;
//import com.kaifangqian.modules.opensign.exception.ActivitiNoDataException;
//import com.kaifangqian.modules.opensign.service.business.ActivitiBusinessService;
//import com.kaifangqian.modules.opensign.service.business.DocBusinessService;
//import com.kaifangqian.modules.opensign.service.cert.SignCertInfoService;
//import com.kaifangqian.modules.opensign.service.doc.*;
//import com.kaifangqian.modules.opensign.service.seal.SignEntSealService;
//import com.kaifangqian.modules.opensign.service.tool.SignFileService;
//import com.kaifangqian.modules.opensign.service.tool.pojo.CertificateProperty;
//import com.kaifangqian.modules.opensign.vo.base.*;
//import com.kaifangqian.modules.opensign.vo.request.*;
//import com.kaifangqian.modules.opensign.vo.response.DocInfoResponse;
//import com.kaifangqian.modules.opensign.vo.response.DocListResponse;
//import com.kaifangqian.modules.storage.entity.AnnexStorage;
//import com.kaifangqian.modules.system.entity.SysDepart;
//import com.kaifangqian.modules.system.entity.SysDictItem;
//import com.kaifangqian.modules.system.entity.SysTenantUser;
//import com.kaifangqian.modules.system.service.ISysDepartService;
//import com.kaifangqian.modules.system.service.ISysDictItemService;
//import com.kaifangqian.modules.system.service.ISysTenantUserService;
//// import io.swagger.annotations.Api;
//// import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import net.sf.jasperreports.engine.*;
//import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
//import net.sf.jasperreports.engine.util.JRLoader;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.*;
//import java.net.URLEncoder;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.stream.Collectors;
//
///**
// * @Description: SignDocController
// * @Package: com.kaifangqian.modules.opensign.controller
// * @ClassName: SignDocController
// * @author: FengLai_Gong
// */
//@Slf4j
//@RestController
//@RequestMapping("/sign/doc")
//@ResrunLogModule(name = "文档管理")
//// @Api(tags = "电子印章-文档管理")
//public class SignDocController {
//
//    @Autowired
//    private SignDocService docService ;
//    @Autowired
//    private SignDocControlService docControlService;
//    @Autowired
//    private DocBusinessService docBusinessService ;
//    @Autowired
//    private SignDocRecordService docRecordService;
//    @Autowired
//    private SignDocImageConvertService docImageConvertService;
//    @Autowired
//    private SignDocImageRecordService docImageRecordService;
//    @Autowired
//    private SignCertInfoService certInfoService ;
//    @Autowired
//    private SignEntSealService entSealService ;
//
//    @Autowired
//    private SignFileService signFileService ;
//
//    @Autowired
//    private ISysDepartService departService;
//    @Autowired
//    private ISysTenantUserService tenantUserService ;
//    @Autowired
//    private ActivitiBusinessService activitiBusinessService ;
//
//
//    // @ApiOperation("文档列表")
//    @RequestMapping(value = "/list", method = RequestMethod.GET)
//    public Result<IPage<DocListResponse>> list(DocListRequest request){
//
//        LoginUser currentUser = MySecurityUtils.getCurrentUser();
//        IPage<DocListResponse> responseIPage = new Page<>(request.getPageNo(), request.getPageSize());
//
//        QueryWrapper<SignDoc> wrapper = new QueryWrapper<>();
//        //文档主题
//        if(request.getDocSubject() != null && request.getDocSubject().length() > 0){
//            wrapper.lambda().like(SignDoc::getDocSubject,"%" + request.getDocSubject() + "%");
//        }
//        //文档状态
//        if(request.getDocStatus() != null){
//            wrapper.lambda().eq(SignDoc::getDocStatus,request.getDocStatus());
//        }
//        //用印场景
//        if(request.getSceneType() != null){
//            wrapper.lambda().eq(SignDoc::getSceneType,request.getSceneType());
//        }
//        //业务类型
//        if(request.getBusinessType() != null){
//            wrapper.lambda().eq(SignDoc::getBusinessType,request.getBusinessType());
//        }
//        //发起人
//        if(request.getSender() != null){
//            wrapper.lambda().eq(SignDoc::getSysUserId,request.getSender());
//        }
//        //发起部门
//        if(request.getSendDept() != null){
//            wrapper.lambda().eq(SignDoc::getSysDeptId,request.getSendDept());
//        }
//        //申请日期：
//        if(request.getStartTime() != null){
//            wrapper.lambda().ge(SignDoc::getApplyTime,request.getStartTime());
//        }
//        if(request.getEndTime() != null){
//            wrapper.lambda().le(SignDoc::getApplyTime,request.getEndTime());
//        }
//        //是否删除
//        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
//        //租户隔离
//        wrapper.lambda().eq(SignDoc::getSysTenantId,currentUser.getTenantId());
//        wrapper.lambda().orderByDesc(BaseEntity::getCreateTime);
//        Page<SignDoc> page = docService.page(new Page<>(request.getPageNo(), request.getPageSize()), wrapper);
//        if(page != null && page.getRecords() != null && page.getRecords().size() > 0){
//            responseIPage.setPages(page.getPages());
//            responseIPage.setCurrent(page.getCurrent());
//            responseIPage.setTotal(page.getTotal());
//            responseIPage.setSize(page.getSize());
//            List<DocListResponse> responseList = new ArrayList<>();
//            List<SignDoc> records = page.getRecords();
//            Map<String,String> sendDeptMap = null ;
//            List<String> sendDeptIdList = records.stream().map(SignDoc::getSysDeptId).collect(Collectors.toList());
//            if(sendDeptIdList != null && sendDeptIdList.size() > 0){
//                List<SysDepart> departs = departService.listByIds(sendDeptIdList);
//                if(departs != null && departs.size() > 0){
//                    sendDeptMap = departs.stream().collect(Collectors.toMap(SysDepart::getId,SysDepart::getDepartName,(k1,k2) -> k1));
//                }
//            }
//            Map<String,String> senderMap = null ;
//            List<String> senderIdList = records.stream().map(SignDoc::getSysUserId).collect(Collectors.toList());
//            if(senderIdList != null && senderIdList.size() > 0){
//                List<SysTenantUser> sysTenantUsers = tenantUserService.listByIds(senderIdList);
//                if(sysTenantUsers != null && sysTenantUsers.size() > 0){
//                    senderMap = sysTenantUsers.stream().collect(Collectors.toMap(SysTenantUser::getId,SysTenantUser::getNickName,(k1,k2) -> k1));
//                }
//            }
//            for(SignDoc signDoc : records){
//                DocListResponse response = new DocListResponse();
//
//                response.setId(signDoc.getId());
//
//                response.setDocStatus(signDoc.getDocStatus());
//                response.setApplyTime(signDoc.getApplyTime());
//                response.setBusinessType(signDoc.getBusinessType());
//                response.setDocSubject(signDoc.getDocSubject());
//                if(sendDeptMap != null && sendDeptMap.containsKey(signDoc.getSysDeptId())){
//                    String sendDeptName = sendDeptMap.get(signDoc.getSysDeptId());
//                    response.setSendDept(sendDeptName);
//                }
//                if(senderMap != null && senderMap.containsKey(signDoc.getSysUserId())){
//                    String senderName = senderMap.get(signDoc.getSysUserId());
//                    response.setSender(senderName);
//                }
//                //用印场景
//                response.setSceneType(signDoc.getSceneType());
//
//                response.setDocStatus(signDoc.getDocStatus());
//                responseList.add(response);
//            }
//            responseIPage.setRecords(responseList);
//        }
//
//        return Result.OK(responseIPage) ;
//    }
//
//    // @ApiOperation("文档详情")
//    @RequestMapping(value = "/info", method = RequestMethod.GET)
//    public Result<DocInfoResponse> info(@RequestParam("docId") String docId){
//
//
//        SignDoc signDoc = docService.getById(docId);
//        if(signDoc == null || signDoc.getDeleteFlag()){
//            return Result.error("文档不存在",null);
//        }
//        DocInfoResponse response = new DocInfoResponse();
//        //文档主数据
//        DocInfo docVo = new DocInfo();
//        BeanUtils.copyProperties(signDoc,docVo);
//        //文档主文件
//        SignDocRecord currentRecord = docRecordService.getCurrent(signDoc.getId());
//        if(currentRecord != null && currentRecord.getAnnexId() != null){
//            AnnexStorage annex = signFileService.getAnnexById(currentRecord.getAnnexId());
//            docVo.setMainAnnexId(annex);
//        }
//        //文档其他关联文件
//        List<AnnexStorage> otherAnnexIdList = signFileService.findByFatherIdAndDataCategoryList(SignFileEnum.SIGN_FILE_OTHER, signDoc.getId());
//        docVo.setOtherAnnexList(otherAnnexIdList);
//
//
//        response.setDocVo(docVo);
//        //文档控件数据
//        List<DocControlVo> docControlVoList = new ArrayList<>();
//        List<SignDocControl> list = docControlService.getList(docId);
//        if(list != null && list.size() > 0){
//            for(SignDocControl control : list){
//                DocControlVo controlVo = new DocControlVo();
//                BeanUtils.copyProperties(control,controlVo);
//                docControlVoList.add(controlVo);
//            }
//        }
//        response.setDocControlVoList(docControlVoList);
//        //文档图片数据
//        List<ImageVo> imageVoList = new ArrayList<>();
//        List<SignDocImageRecord> currentList = docImageRecordService.getCurrentList(docId);
//        if(currentList != null && currentList.size() > 0){
//            for(SignDocImageRecord record : currentList){
//                ImageVo vo = new ImageVo();
//                vo.setId(record.getId());
//                vo.setPage(record.getDocPage());
//                vo.setAnnexId(record.getAnnexId());
//                imageVoList.add(vo);
//            }
//        }
//        response.setImageVoList(imageVoList);
//        return Result.OK(response) ;
//    }
//
//
//    // @ApiOperation("文档校验")
//    @RequestMapping(value = "/verify", method = RequestMethod.GET)
//    public Result<?> verify(@RequestParam("docId") String docId){
//        SignDoc signDoc = docService.getById(docId);
//        if(signDoc == null){
//            return Result.error("文档不存在",null);
//        }
//
//        List<SignDocControl> list = docControlService.getList(docId);
//
//        if(list == null || list.size() == 0){
//            return Result.error("文档控件不存在",null);
//        }
//
//        return Result.OK() ;
//    }
//
//
//    // @ApiOperation("文档下载最新文件")
//    @RequestMapping(value = "/downloadCurrent", method = RequestMethod.GET)
//    public Result<?> downloadCurrent(@RequestParam("docId") String docId){
//        SignDoc signDoc = docService.getById(docId);
//        if(signDoc == null){
//            return Result.error("文档不存在",null);
//        }
//        SignDocRecord current = docRecordService.getCurrent(docId);
//        if(current == null || current.getAnnexId() == null){
//            return Result.error("文档不存在",null);
//        }
//
//        return Result.OK("",current.getAnnexId()) ;
//    }
//
//
//
//    // @ApiOperation("文档创建")
//    @RequestMapping(value = "/save", method = RequestMethod.POST)
//    public Result<?> save(@RequestBody DocVo request){
//
//        if(request == null){
//            return Result.error("参数缺失");
//        }
//        String docId = null ;
//        if(request.getId() == null || request.getId().length() == 0){
//            docId  = docBusinessService.createDoc(request);
//        }else {
//            SignDoc signDoc = docService.getById(request.getId());
//            if(signDoc == null){
//                return Result.error("文档不存在，不能更新");
//            }
//            docId = docBusinessService.updateDoc(request,signDoc);
//        }
//        if(docId == null){
//            return Result.error("操作失败");
//        }
//
//        return Result.OK("",docId) ;
//    }
//
//
//
//
//    // @ApiOperation("文档主文件转图片")
//    @RequestMapping(value = "/convertImage", method = RequestMethod.POST)
//    public Result<List<ImageConvertVo>> convertImage(@RequestBody DocImageConvertRequest request){
//
//        if(request == null || request.getDocId() == null || request.getAnnexId() == null){
//            return Result.error("参数缺失",null);
//        }
//        List<ImageConvertVo> responseList = docBusinessService.convertImage(request.getDocId(), request.getAnnexId());
//
//        return Result.OK(responseList) ;
//    }
//
////    // @ApiOperation("文档发布")
////    @RequestMapping(value = "/submit", method = RequestMethod.POST)
////    public Result<?> submit(@RequestBody DocSubmitRequest request){
////
////        if(request == null || request.getId() == null || request.getId().length() == 0){
////            return Result.error("参数缺失");
////        }
////        //校验文档是否存在
////        SignDoc signDoc = docService.getById(request.getId());
////        if(signDoc == null){
////            return Result.error("文档不存在");
////        }
////        //校验文档是否设置过控件
////        Integer controlCount = docControlService.count(request.getId());
////        if(controlCount == null || controlCount == 0){
////            return Result.error("文档未指定签署位置");
////        }
////        //校验是否已经转过图片
////        Integer imageCount = docImageRecordService.countCurrentList(request.getId());
////        if(imageCount == null || imageCount == 0){
////            return Result.error("文档未指定签署位置");
////        }
////        //发起
////        signDoc.setApplyTime(new Date());
////        signDoc.setDocStatus(DocStatusEnum.TO_BE_APPROVAL.getCode());
////        boolean b = docService.updateById(signDoc);
////        if(!b){
////            return Result.error("操作失败");
////        }
////
////        return Result.OK() ;
////    }
//
//    // @ApiOperation("文档保存控件")
//    @RequestMapping(value = "/save/controlList", method = RequestMethod.POST)
//    public Result<?> saveControlList(@RequestBody DocSaveControlListRequest request){
//        if(request == null || request.getDocId() == null || request.getDocControlVoList() == null || request.getDocControlVoList().size() == 0){
//            return Result.error("参数缺失");
//        }
//        docBusinessService.saveControlList(request.getDocId(), request.getDocControlVoList());
//
//        return Result.OK() ;
//    }
//
////    // @ApiOperation("文档签署")
////    @RequestMapping(value = "/sign", method = RequestMethod.PUT)
////    public Result<?> sign(@RequestBody DocSignRequest request){
////
////        if(request == null || request.getDocId() == null){
////            return Result.error("参数缺失");
////        }
////
////        //校验文档
////        SignDoc signDoc = docService.getById(request.getDocId());
////        if(signDoc == null){
////            return Result.error("文档不存在");
////        }
////        SignDocRecord current = docRecordService.getCurrent(request.getDocId());
////        if(current == null || current.getAnnexId() == null){
////            return Result.error("文档记录不存在");
////        }
////        byte[] docByte = signFileService.getByteByFatherIdAndDataCategory(SignFileEnum.SIGN_FILE_MAIN, current.getId());
////        if(docByte == null){
////            return Result.error("文档文件不存在");
////        }
////        //校验企业签章
////        SignEntSeal entSeal = entSealService.getById(signDoc.getSealId());
////        if(entSeal == null){
////            return Result.error("签章不存在");
////        }
////        byte[] entSealByte = signFileService.getByteByFatherIdAndDataCategory(SignFileEnum.SEAL_FILE_ENT, entSeal.getId());
////        if(entSealByte == null){
////            return Result.error("签章文件不存在");
////        }
////        //校验证书
////        SignCertInfo validateCert = certInfoService.getValidateCert();
////        if(validateCert == null){
////            return Result.error("证书不存在");
////        }
////        byte[] certByte = signFileService.getByteByFatherIdAndDataCategory(SignFileEnum.CERT, validateCert.getId());
////        if(certByte == null){
////            return Result.error("证书文件不存在");
////        }
////        CertificateProperty cert = new CertificateProperty();
////        cert.setCertType("PKCS12");
////        cert.setPassword(validateCert.getCertPassword());
////        cert.setCertFile(certByte);
////        //校验文档控件
////        List<SignDocControl> docControlList = docControlService.getList(request.getDocId());
////        if(docControlList == null || docControlList.size() == 0){
////            return Result.error("文档控件不存在");
////        }
////        //签署文件
////        String docId = docBusinessService.signDoc(request.getDocId(), docByte, entSealByte, cert, docControlList);
////        if(docId == null){
////            return Result.error("操作失败");
////        }
////
////        return Result.OK("",docId) ;
////    }
//
//
//
////    // @ApiOperation("文档撤回")
////    @RequestMapping(value = "/revoke", method = RequestMethod.PUT)
////    public Result<?> revoke(@RequestBody DocRevokeRequest request){
////
////        if(request == null || request.getDocId() == null || request.getDocId().length() == 0){
////            return Result.error("参数缺失");
////        }
////        SignDoc signDoc = docService.getById(request.getDocId());
////        if(signDoc == null){
////            return Result.error("文档不存在");
////        }
////        signDoc.setDocStatus(DocStatusEnum.TO_BE_RE_SUMMIT.getCode());
////        boolean b = docService.updateById(signDoc);
////        if(!b){
////            return Result.error("操作失败");
////        }
////        activitiBusinessService.revoke(request.getDocId());
////        return Result.OK() ;
////    }
//
//    // @ApiOperation("文档作废")
//    @RequestMapping(value = "/cancel", method = RequestMethod.PUT)
//    public Result<?> cancel(@RequestBody DocCancelRequest request){
//
//        if(request == null || request.getDocId() == null || request.getDocId().length() == 0){
//            return Result.error("参数缺失");
//        }
//        SignDoc signDoc = docService.getById(request.getDocId());
//        if(signDoc == null){
//            return Result.error("文档不存在");
//        }
//        signDoc.setDocStatus(DocStatusEnum.CANCELED.getCode());
//        boolean b = docService.updateById(signDoc);
//        if(!b){
//            return Result.error("操作失败");
//        }
//        activitiBusinessService.stop(request.getDocId());
//        return Result.OK() ;
//    }
//
//    // @ApiOperation("文档删除")
//    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
//    public Result<?> delete(DocDeleteRequest request){
//
//
//        if(request == null || request.getDocId() == null || request.getDocId().length() == 0){
//            return Result.error("参数缺失");
//        }
//        SignDoc signDoc = docService.getById(request.getDocId());
//        if(signDoc == null){
//            return Result.error("文档不存在");
//        }
//        signDoc.setDeleteFlag(true);
//        signDoc.setDeleteTime(new Date());
//        boolean b = docService.updateById(signDoc);
//        if(!b){
//            return Result.error("操作失败");
//        }
//        activitiBusinessService.delete(request.getDocId());
//        return Result.OK() ;
//    }
//
//
//
//
//    // @ApiOperation("文档列表数据导出")
//    @RequestMapping(value = "/export", method = RequestMethod.GET)
//    public Result<?> export(DocExportRequest request){
//
//
//        return null ;
//    }
//
//
////    /**
////     * 下载
////     */
////    // @ApiOperation(value = "下载审批单", notes = "下载审批单")
////    @GetMapping(value = "/downloadApply")
////    public void downloadApply(@RequestParam(value = "docId") String docId, HttpServletResponse response) {
////        String proRecId = taskBusinessService.getProRecIdByMainDataId(docId);
////        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////        if(proRecId == null || proRecId.length() == 0){
////            throw new ActivitiNoDataException("流程实例数据不存在");
////        }
////
////        SignDoc signDoc = docService.getById(docId);
////        if(signDoc == null){
////            throw new RuntimeException("文档数据不存在");
////        }
////
////        Map<String, Object> parameters = new HashMap<String, Object>(16);
////        parameters.put("docSubject", signDoc.getDocSubject());
////        if(signDoc.getSceneType() != null){
////            if(signDoc.getSceneType().equals(1)){
////                parameters.put("sceneTypeName", "加盖电子印章");
////                parameters.put("useCount",  "");
////            }else {
////                parameters.put("sceneTypeName", "加盖物理印章");
////                if(signDoc.getUseCount() != null){
////                    parameters.put("useCount", signDoc.getUseCount() + "");
////                }else {
////                    parameters.put("useCount",  "0");
////                }
////            }
////        }
////
////        String businessType = signDoc.getBusinessType();
//////        if(signDoc.getBusinessType() != null){
//////            SysDictItem dictItem = dictItemService.getById(signDoc.getBusinessType());
//////            if(dictItem != null && dictItem.getItemText() != null){
//////                businessType = dictItem.getItemText() ;
//////            }
//////        }
////        if(businessType != null && businessType.length() > 0){
////            if(businessType.equals(DocBusinessTypeEnum.CREATE.getCode())){
////                parameters.put("businessType", DocBusinessTypeEnum.CREATE.getName());
////            }else if(businessType.equals(DocBusinessTypeEnum.DELETE.getCode())){
////                parameters.put("businessType", DocBusinessTypeEnum.DELETE.getName());
////            }else if(businessType.equals(DocBusinessTypeEnum.PLAN.getCode())){
////                parameters.put("businessType", DocBusinessTypeEnum.PLAN.getName());
////            }else if(businessType.equals(DocBusinessTypeEnum.STORE.getCode())){
////                parameters.put("businessType", DocBusinessTypeEnum.STORE.getName());
////            }
////        }else {
////            parameters.put("businessType", "");
////        }
////
////        if(signDoc.getSendDept() != null){
////            parameters.put("sendDept", signDoc.getSendDept());
////        }
////        if(signDoc.getSysUserId() != null){
////            SysTenantUser tenantUser = tenantUserService.getById(signDoc.getSysUserId());
////            if(tenantUser != null){
////                parameters.put("sysUser", tenantUser.getNickName());
////            }
////        }
////        if(signDoc.getApplyTime() != null){
////
////            String applyTime = s.format(signDoc.getApplyTime());
////            parameters.put("applyTime", applyTime);
////        }else {
////            parameters.put("applyTime", "");
////        }
////        if(signDoc.getExpireTime() != null){
////            String expireTime = s.format(signDoc.getExpireTime());
////            parameters.put("expireTime", expireTime);
////        } else {
////            parameters.put("expireTime", "");
////        }
////        if(signDoc.getUpdateTime() != null){
////            String finishedTime = s.format(signDoc.getUpdateTime());
////            parameters.put("finishedTime", finishedTime);
////        }else {
////            parameters.put("finishedTime", "");
////        }
////
////        if(signDoc.getSealType() != null){
////            if(signDoc.getSealType() == 1){
////                parameters.put("sealType", "公章");
////            }else if(signDoc.getSealType() == 2){
////                parameters.put("sealType", "法人章");
////            }else if (signDoc.getSealType() == 3){
////                parameters.put("sealType", "发票章");
////            }else if(signDoc.getSealType() == 4){
////                parameters.put("sealType", "投标章");
////            }
////        }else {
////            parameters.put("sealType", "");
////        }
////        if(signDoc.getSysDeptId() != null){
////            SysDepart depart = departService.getById(signDoc.getSysDeptId());
////            if(depart != null){
////                parameters.put("sysDept", depart.getDepartName());
////            }
////        }
////        if(signDoc.getNote() != null){
////            parameters.put("note", signDoc.getNote());
////        }else {
////            parameters.put("note", "");
////        }
////        setResponse(response, UUID.randomUUID().toString() + ".pdf");
////
////        List<ExTaskOperateVO> list = taskBusinessService.getOperateProcessRecForExport(proRecId);
////        InputStream jasperStream = null ;
////        OutputStream outStream = null;
////        try {
////            File file = new File("/resrun/opensign/jasper/approvalForm.jasper");
////            if (!file.exists()) {
////                return;
////            }
////            jasperStream = new FileInputStream(file);
//////            jasperStream = SignDocController.class.getClassLoader().getResourceAsStream("approvalForm.jasper");
////            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
////
////            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
////            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
////            outStream = response.getOutputStream();
////            JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
////        } catch (JRException e) {
////            e.printStackTrace();
////        } catch (IOException e) {
////            e.printStackTrace();
////        } finally {
////            if (outStream != null) {
////                try {
////                    outStream.close();
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////            }
////            if (jasperStream != null) {
////                try {
////                    jasperStream.close();
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////            }
////        }
////    }
//
//
//    void setResponse(HttpServletResponse response, String fileName) {
//        try {
//            String fileNameencode = URLEncoder.encode(fileName, "UTF-8");
//            response.setCharacterEncoding("utf-8");
//            response.setContentType("application/pdf");
//            response.setHeader("Content-Disposition", "inline;fileName=" + fileNameencode);
//            response.setHeader("fileName", fileNameencode);
//            response.setHeader("Access-Control-Expose-Headers", "fileName");
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//}