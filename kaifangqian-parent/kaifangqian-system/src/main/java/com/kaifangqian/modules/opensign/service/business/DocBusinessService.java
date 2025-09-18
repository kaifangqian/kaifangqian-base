package com.kaifangqian.modules.opensign.service.business;

import com.kaifangqian.modules.opensign.service.doc.*;
import com.kaifangqian.modules.opensign.entity.*;
import com.kaifangqian.modules.opensign.enums.*;
import com.kaifangqian.modules.opensign.service.cert.SignCertInfoService;
import com.kaifangqian.modules.opensign.service.doc.*;
import com.kaifangqian.modules.opensign.service.seal.SignEntSealService;
import com.kaifangqian.modules.opensign.service.tool.CalculatePositionService;
//import com.kaifangqian.modules.opensign.service.tool.PdfOperateService;
import com.kaifangqian.modules.opensign.service.tool.SignFileService;
//import com.kaifangqian.modules.opensign.service.tool.SignHandleService;
import com.kaifangqian.modules.opensign.service.tool.pojo.CertificateProperty;
import com.kaifangqian.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Description: DocBusinessService
 * @Package: com.kaifangqian.modules.opensign.service
 * @ClassName: DocBusinessService
 * @author: FengLai_Gong
 */
@Service
public class DocBusinessService {

    @Autowired
    private SignFileService signFileService ;

    @Autowired
    private SignDocService docService ;
    @Autowired
    private SignDocImageConvertService docImageConvertService ;
    @Autowired
    private SignDocControlService docControlService;
    @Autowired
    private SignDocRecordService docRecordService;
    @Autowired
    private SignDocImageRecordService docImageRecordService ;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private SignCertInfoService certInfoService ;
    @Autowired
    private SignEntSealService entSealService ;

    @Autowired
    private CalculatePositionService calculatePositionService;
//    @Autowired
//    private SignHandleService signHandleService;
//    @Autowired
//    private PdfOperateService pdfOperateService ;

    @Autowired
    private RuSignService ruSignService ;

    @Autowired
    private DocApplyService docApplyService ;



//    /**
//     * @Description #转换图片并且存入临时表
//     * @Param [docId, annexId]
//     * @return java.util.List<com.kaifangqian.modules.opensign.vo.response.DocImageConvertResponse>
//     **/
//    public List<ImageConvertVo> convertImage(String docId ,String annexId){
//        List<ImageConvertVo> imageConvertVos = convertImageService.convertImage(annexId);
//        if(imageConvertVos != null && imageConvertVos.size() > 0){
//            List<SignDocImageConvert> convertList = new ArrayList<>();
//
//            for(ImageConvertVo response : imageConvertVos){
//                SignDocImageConvert signDocImageConvert = new SignDocImageConvert();
//                signDocImageConvert.setDocId(docId);
//                signDocImageConvert.setAnnexId(response.getImageAnnexId());
//                signDocImageConvert.setPage(response.getPage());
//                signDocImageConvert.setDeleteFlag(false);
//                convertList.add(signDocImageConvert);
//            }
//            //异步存入数据
//            threadPoolTaskExecutor.execute(new Runnable() {
//                @Override
//                public void run() {
//                    docImageConvertService.saveBatch(convertList);
//                }
//            });
//
//        }
//        return imageConvertVos ;
//    }


//    /**
//     * @Description #保存控件列表
//     * @Param [docId, docControlVoList]
//     * @return void
//     **/
//    public void saveControlList(String docId ,List<DocControlVo> docControlVoList){
//        //删除之前的控件
//        docControlService.delete(docId);
//        //新增现有的控件
//        List<SignDocControl> insertList = new ArrayList<>();
//        for(DocControlVo vo : docControlVoList){
//            SignDocControl signDocControl = new SignDocControl();
//            BeanUtils.copyProperties(vo,signDocControl);
//
//            signDocControl.setDocId(docId);
//            signDocControl.setDeleteFlag(false);
//            insertList.add(signDocControl);
//        }
//        docControlService.saveBatch(insertList);
//
//    }


//    /**
//     * @Description #更新文档数据
//     * @Param [request]
//     * @return java.lang.String
//     **/
//    public String updateDoc(DocVo request,SignDoc signDoc){
//
//
//        LoginUser currentUser = MySecurityUtils.getCurrentUser();
//        //更新文档数据
//        BeanUtils.copyProperties(request,signDoc);
////        signDoc.setDocStatus(DocStatusEnum.TO_BE_SUMMIT.getCode());
////        signDoc.setApplyTime(null);
//        boolean updateDoc = docService.updateById(signDoc);
//        if(!updateDoc){
//            return null ;
//        }
//        //维护文档其他附件文件和主文档的关系
//        if(request.getOtherAnnexList() != null && request.getOtherAnnexList().size() > 0){
//            threadPoolTaskExecutor.execute(new Runnable() {
//                @Override
//                public void run() {
//                    //删除旧关联附件
//                    signFileService.deleteAnnexStorage(SignFileEnum.SIGN_FILE_OTHER,signDoc.getId());
//                    //插入新关联附件
//                    for(String otherAnnexId : request.getOtherAnnexList()){
//                        signFileService.updateAnnexStorage(SignFileEnum.SIGN_FILE_OTHER, signDoc.getId(), otherAnnexId);
//                    }
//                }
//            });
//        }
//        if(request.getMainAnnexId() == null || request.getMainAnnexId().length() == 0){
//            return signDoc.getId();
//        }
//        //获取文档记录数据
//        SignDocRecord currentRecord = docRecordService.getCurrent(signDoc.getId());
//        if(currentRecord != null && currentRecord.getAnnexId() != null && currentRecord.getAnnexId().equals(request.getMainAnnexId())){
//            //如果文档主文件没有变更，则直接返回
//            return signDoc.getId() ;
//        }
//        //更新旧的文档记录数据
//        docRecordService.setNotCurrent(signDoc.getId());
//        //创建文档记录数据
//        SignDocRecord signDocRecord = new SignDocRecord();
//        signDocRecord.setDocId(signDoc.getId());
//        signDocRecord.setAnnexId(request.getMainAnnexId());
//        signDocRecord.setSysDeptId(currentUser.getDepartId());
//        signDocRecord.setOperateTime(new Date());
//        signDocRecord.setOperateStatus(SignDocOperateRecordEnum.INIT.getCode());
//        signDocRecord.setIsCurrent(SignCurrentEnum.IS_CURRENT.getCode());
//        signDocRecord.setDeleteFlag(false);
//        boolean saveDocRecord = docRecordService.save(signDocRecord);
//        if(!saveDocRecord){
//            return null ;
//        }
//        //维护文档主文件和文档记录的关系
//        signFileService.updateAnnexStorage(SignFileEnum.SIGN_FILE_MAIN, signDocRecord.getId(), request.getMainAnnexId());
//        //维护文档记录关联的图片数据
//        List<ImageConvertVo> imageConvertVos = convertImageService.convertImage(request.getMainAnnexId());
//        if(imageConvertVos != null && imageConvertVos.size() > 0){
//
//            List<SignDocImageRecord> imageRecordList = new ArrayList<>();
//            for(ImageConvertVo imageConvert : imageConvertVos){
//                SignDocImageRecord imageRecord = new SignDocImageRecord();
//                //文档相关关联属性
//                imageRecord.setDocId(signDoc.getId());
//                imageRecord.setDocRecordId(signDocRecord.getId());
//                //图片相关数据
//                imageRecord.setAnnexId(imageConvert.getImageAnnexId());
//                imageRecord.setDocPage(imageConvert.getPage());
//                //基本数据
//                imageRecord.setIsCurrent(SignCurrentEnum.IS_CURRENT.getCode());
//                imageRecord.setDeleteFlag(false);
//                imageRecordList.add(imageRecord);
//            }
//            //更新之前的图片数据
//            docImageRecordService.updateNotCurrent(signDoc.getId());
//            //插入新的图片数据
//            boolean saveImageRecordList = docImageRecordService.saveBatch(imageRecordList);
//            if(!saveImageRecordList){
//                return null ;
//            }
//        }
//        //如果存在控件数据，删除控件数据
////        threadPoolTaskExecutor.execute(new Runnable() {
////            @Override
////            public void run() {
////                docControlService.delete(signDoc.getId());
////            }
////        });
//
//        return signDoc.getId();
//    }


//    /**
//     * @Description #保存文档数据
//     * @Param [request]
//     * @return java.lang.String
//     **/
//    public String createDoc(DocVo request){
//        LoginUser currentUser = MySecurityUtils.getCurrentUser();
//        //保存文档数据
//        SignDoc signDoc = new SignDoc();
//        BeanUtils.copyProperties(request,signDoc);
//        signDoc.setDocStatus(DocStatusEnum.TO_BE_SUMMIT.getCode());
//        signDoc.setDeleteFlag(false);
//        signDoc.setSysDeptId(currentUser.getDepartId());
//        //申请时间
//        signDoc.setApplyTime(new Date());
//        boolean createDoc = docService.save(signDoc);
//        if(!createDoc){
//            return null ;
//        }
//        //维护文档其他附件文件和主文档的关系
//        if(request.getOtherAnnexList() != null && request.getOtherAnnexList().size() > 0){
//            threadPoolTaskExecutor.execute(new Runnable() {
//                @Override
//                public void run() {
//                    //插入新关联附件
//                    for(String otherAnnexId : request.getOtherAnnexList()){
//                        signFileService.updateAnnexStorage(SignFileEnum.SIGN_FILE_OTHER, signDoc.getId(), otherAnnexId);
//                    }
//                }
//            });
//        }
//        //如果文档文件为空，则直接返回
//        if(request.getMainAnnexId() == null || request.getMainAnnexId().length() == 0){
//            return signDoc.getId();
//        }
//
//        //创建文档记录数据
//        SignDocRecord signDocRecord = new SignDocRecord();
//        signDocRecord.setDocId(signDoc.getId());
//        signDocRecord.setAnnexId(request.getMainAnnexId());
//        signDocRecord.setSysDeptId(currentUser.getDepartId());
//        signDocRecord.setOperateTime(new Date());
//        signDocRecord.setOperateStatus(SignDocOperateRecordEnum.INIT.getCode());
//        signDocRecord.setIsCurrent(SignCurrentEnum.IS_CURRENT.getCode());
//        signDocRecord.setDeleteFlag(false);
//        boolean saveDocRecord = docRecordService.save(signDocRecord);
//        if(!saveDocRecord){
//            return null ;
//        }
//        //维护文档主文件和文档记录的关系
//        signFileService.updateAnnexStorage(SignFileEnum.SIGN_FILE_MAIN, signDocRecord.getId(), request.getMainAnnexId());
//        //维护文档记录关联的图片数据
//        List<ImageConvertVo> imageConvertVos = convertImageService.convertImage(request.getMainAnnexId());
//        if(imageConvertVos != null && imageConvertVos.size() > 0){
//            List<SignDocImageRecord> imageRecordList = new ArrayList<>();
//            for(ImageConvertVo imageConvert : imageConvertVos){
//                SignDocImageRecord imageRecord = new SignDocImageRecord();
//                //文档相关关联属性
//                imageRecord.setDocId(signDoc.getId());
//                imageRecord.setDocRecordId(signDocRecord.getId());
//                //图片相关数据
//                imageRecord.setAnnexId(imageConvert.getImageAnnexId());
//                imageRecord.setDocPage(imageConvert.getPage());
//                //基本数据
//                imageRecord.setIsCurrent(SignCurrentEnum.IS_CURRENT.getCode());
//                imageRecord.setDeleteFlag(false);
//                imageRecordList.add(imageRecord);
//            }
//            //插入新的图片数据
//            boolean saveImageRecordList = docImageRecordService.saveBatch(imageRecordList);
//            if(!saveImageRecordList){
//                return null ;
//            }
//
//        }
//
//        return signDoc.getId();
//    }


    /**
     * @Description #文档签署
     * @Param [docId, docByte, entSealByte, certByte, docControlList]
     * @return java.lang.String
     **/
//    public String signDoc(String docId , byte[] docByte , byte[] entSealByte , CertificateProperty cert , List<SignDocControl> docControlList){
//
//        for(SignDocControl signDocControl : docControlList){
//
//        }
//
//
//        return docId;
//    }

//    public String autoSign(String docId){
//        //校验文档
//        SignDoc signDoc = docService.getById(docId);
//        if(signDoc == null){
//            docApplyService.errorLog(docId,"文档签署-文档不存在");
//            return null ;
//        }
//        SignDocRecord current = docRecordService.getCurrent(docId);
//        if(current == null || current.getAnnexId() == null){
//            docApplyService.errorLog(docId,"文档签署-文档签署记录不存在");
//            return null ;
//        }
//        byte[] docByte = signFileService.getByteByFatherIdAndDataCategory(SignFileEnum.SIGN_FILE_MAIN, current.getId());
//        if(docByte == null){
//            docApplyService.errorLog(docId,"文档签署-文档文件不存在");
//            return null;
//        }
//        //校验企业签章
//        SignEntSeal entSeal = entSealService.getById(signDoc.getSealId());
//        if(entSeal == null){
//            docApplyService.errorLog(docId,"文档签署-签章不存在");
//            return null;
//        }
//        byte[] entSealByte = signFileService.getByteByFatherIdAndDataCategory(SignFileEnum.SEAL_FILE_ENT, entSeal.getId());
//        if(entSealByte == null){
//            docApplyService.errorLog(docId,"文档签署-签章文件不存在");
//            return null;
//        }
//        //校验证书
////        SignCertInfo validateCert = certInfoService.getValidateCert();
////        if(validateCert == null){
////            return null;
////        }
////        byte[] certByte = signFileService.getByteByFatherIdAndDataCategory(SignFileEnum.CERT, validateCert.getId());
////        if(certByte == null){
////            return null;
////        }
//        CertificateProperty certificateProperty = getCertInfo();
//        //校验文档控件
//        List<SignDocControl> docControlList = docControlService.getList(docId);
//        if(docControlList == null || docControlList.size() == 0){
//            docApplyService.errorLog(docId,"文档签署-控件不存在");
//            return null;
//        }
//        //签署控件列表
//        List<SignDocControl> signControlList = new ArrayList<>();
//        //填写控件列表
//        List<SignDocControl> writeControlList = new ArrayList<>();
//        for(SignDocControl control : docControlList){
//            if(control.getType() != null && control.getType().equals(ControlTypeEnum.SEAL.getName())){
//                //签署控件
//                signControlList.add(control);
//            }else {
//                //填写控件
//                writeControlList.add(control);
//            }
//        }
//        if(signControlList.size() == 0){
//            docApplyService.errorLog(docId,"文档签署-控件不存在");
//            return null ;
//        }//文档填写
//        byte[] newDocByte = null ;
//        if(writeControlList.size() > 0){
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
//            String format = simpleDateFormat.format(new Date());
//            for(SignDocControl control : writeControlList){
//                control.setValue(format);
//            }
////            newDocByte = pdfOperateService.insertContextByPositions(docByte, writeControlList);
//
//            for(){
//
//            }
//            newDocByte = ruSignService.write(writeControlList,docByte);
//        }
//        if(newDocByte == null){
//            docApplyService.errorLog(docId,"文档签署-填写签署时间失败");
//        }
//        List<SourcePositionProperty> sourcePositionProperties = new ArrayList<>();
//        for(SignDocControl control : signControlList){
//            //创建签署位置属性对象列表
//            SourcePositionProperty sourcePositionProperty = new SourcePositionProperty();
//            sourcePositionProperty.setOffsetX(Float.valueOf(control.getOffsetX()));
//            sourcePositionProperty.setOffsetY(Float.valueOf(control.getOffsetY()));
//            sourcePositionProperty.setHeight(Float.valueOf(control.getHeight()));
//            sourcePositionProperty.setWidth(Float.valueOf(control.getWidth()));
//            sourcePositionProperty.setPageHeight(Float.valueOf(control.getPageHeight()));
//            sourcePositionProperty.setPageWidth(Float.valueOf(control.getPageWidth()));
//            sourcePositionProperty.setPage(control.getPage() + 1);
//            sourcePositionProperties.add(sourcePositionProperty);
//        }
//        //计算签署为位置
//        List<RealPositionProperty> realPositionProperties = calculatePositionService.calculatePositions(sourcePositionProperties, newDocByte);
//        if(realPositionProperties == null || realPositionProperties.size() == 0){
//            docApplyService.errorLog(docId,"文档签署-签署位置计算失败");
//        }
//        byte[] newFileBytes = null ;
//        try {
//            //进行签署
////            newFileBytes = signHandleService.signingContract(newDocByte, entSealByte, certificateProperty, realPositionProperties);
//            newFileBytes = signHandleService.signingContract(newDocByte, entSealByte, certificateProperty, realPositionProperties);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if(newFileBytes == null){
//            docApplyService.errorLog(docId,"文档签署-签署失败");
//            return null ;
//        }
//
//        //签署完成，维护关联性数据
//        String newFileAnnexId = signFileService.saveAnnexStorage(newFileBytes, SignFileEnum.SIGN_FILE_MAIN, null);
//        //更新旧的文档记录数据
//        docRecordService.setNotCurrent(signDoc.getId());
//        //创建文档记录数据
//        SignDocRecord signDocRecord = new SignDocRecord();
//        signDocRecord.setDocId(signDoc.getId());
//        signDocRecord.setAnnexId(newFileAnnexId);
//        signDocRecord.setSysDeptId(null);
//        signDocRecord.setOperateTime(new Date());
//        signDocRecord.setOperateStatus(SignDocOperateRecordEnum.SIGN.getCode());
//        signDocRecord.setIsCurrent(SignCurrentEnum.IS_CURRENT.getCode());
//        signDocRecord.setDeleteFlag(false);
//        boolean saveDocRecord = docRecordService.save(signDocRecord);
//        if(!saveDocRecord){
//            docApplyService.errorLog(docId,"文档签署-签署记录插入失败");
//            return null ;
//        }
//        //维护文档主文件和文档记录的关系
//        signFileService.updateAnnexStorage(SignFileEnum.SIGN_FILE_MAIN, signDocRecord.getId(), newFileAnnexId);
//        //维护文档记录关联的图片数据
//        List<ImageConvertVo> imageConvertVos = convertImageService.convertImage(newFileBytes);
//        if(imageConvertVos != null && imageConvertVos.size() > 0){
//            List<SignDocImageRecord> imageRecordList = new ArrayList<>();
//            for(ImageConvertVo imageConvert : imageConvertVos){
//                SignDocImageRecord imageRecord = new SignDocImageRecord();
//                //文档相关关联属性
//                imageRecord.setDocId(signDoc.getId());
//                imageRecord.setDocRecordId(signDocRecord.getId());
//                //图片相关数据
//                imageRecord.setAnnexId(imageConvert.getImageAnnexId());
//                imageRecord.setDocPage(imageConvert.getPage());
//                //基本数据
//                imageRecord.setIsCurrent(SignCurrentEnum.IS_CURRENT.getCode());
//                imageRecord.setDeleteFlag(false);
//                imageRecordList.add(imageRecord);
//            }
//            //更新之前的图片数据
//            docImageRecordService.updateNotCurrent(signDoc.getId());
//            //插入新的图片数据
//            boolean saveImageRecordList = docImageRecordService.saveBatch(imageRecordList);
//            if(!saveImageRecordList){
//                docApplyService.errorLog(docId,"文档签署-签署图片插入失败");
//                return null ;
//            }
//        }
//        return  docId ;
//    }




}