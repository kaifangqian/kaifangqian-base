package com.kaifangqian.modules.opensign.pdfbox;

import com.kaifangqian.modules.opensign.pdfbox.vo.CertificateInfo;
import com.kaifangqian.modules.opensign.pdfbox.vo.FontEnum;
import com.kaifangqian.modules.opensign.pdfbox.vo.FormField;
import com.kaifangqian.modules.opensign.pdfbox.vo.Widget;
import com.kaifangqian.config.FileProperties;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.external.sign.request.DocumentInfo;
import com.kaifangqian.external.sign.request.VerifySignDocumentRequest;
import com.kaifangqian.external.sign.response.AuthSignDocumentResponse;
import com.kaifangqian.external.sign.service.SignServiceExternal;
import com.kaifangqian.modules.opensign.enums.ControlTypeEnum;
import com.kaifangqian.modules.opensign.pdfbox.vo.*;
import com.kaifangqian.modules.opensign.service.business.vo.YundunSignPositionArrayData;
import com.kaifangqian.modules.opensign.service.business.vo.YundunSignPositionData;
import com.kaifangqian.modules.opensign.service.tool.SignFileService;
import com.kaifangqian.modules.opensign.service.tool.pojo.ControlWidgetProperty;
import com.kaifangqian.modules.opensign.service.tool.pojo.RealPositionProperty;
import com.kaifangqian.modules.opensign.utils.Base64;
import com.kaifangqian.pdfbox.AddExternalSignature;
import com.kaifangqian.pdfbox.AssinaturaPDF2;
import com.kaifangqian.pdfbox.vo.LateExternalSignData;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.shiro.codec.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kaifangqian.pdfbox.vo.AssinaturaPosition;
import com.kaifangqian.pdfbox.vo.AssinaturaModel;

import java.io.*;
import java.util.*;

/**
 * @Description: PdfSignService
 * @Package: com.kaifangqian.modules.opensign.pdfbox
 * @ClassName: PdfSignService
 * @author: FengLai_Gong
 */
@Slf4j
@Service
public class PdfboxService {


    @Autowired
    private FileProperties fileProperties;
    @Autowired
    private SignFileService signFileService ;
    @Autowired
    private SignServiceExternal signServiceExternal ;


    public Integer getPdfPage(byte[] pdfByte){
        Integer page = 0 ;

        try {
            PDDocument document = Loader.loadPDF(pdfByte);
            if (document == null) {
                throw new PaasException("pdf文件解析失败");
            }
            page = document.getNumberOfPages();
            document.close();
        } catch (Exception e) {
            throw new PaasException("pdf文件异常");
        }

        return page ;
    }

    /**
     * @Description #签署
     * @Param [pdfFile 文档, signByte 签章, certInfo 证书, positions 位置]
     * @return byte[]
     **/
    public byte[] sign(byte[] pdfFile, byte[] signByte, CertificateInfo certInfo, RealPositionProperty realPositionProperty){
//        log.info("开始签署了");
        //开始签署
        byte [] outPdf = pdfFile ;
        //签署所需基础数据
        AssinaturaModel assinatura = new AssinaturaModel();
        assinatura.setLocation("无");
        assinatura.setReason("防伪造防篡改数字校验");
        //文件
        assinatura.setPdf(outPdf);
        //签章
        assinatura.setSignatureImage(signByte);
        //证书
        //assinatura.setCertInfo(certInfo);
        //签署位置
        AssinaturaPosition position = new AssinaturaPosition();
        position.setPage(realPositionProperty.getPageNum());
        //横坐标
        position.setOffsetX(realPositionProperty.getStartx() + "");
        position.setSignWidth((realPositionProperty.getEndx() - realPositionProperty.getStartx()) + "");
        //纵坐标，pdfbox是从下向上计算的
        float signHeight = realPositionProperty.getStarty() - realPositionProperty.getEndy();
        if(signHeight < 0){
            signHeight = realPositionProperty.getEndy() - realPositionProperty.getStarty() ;
        }
        position.setSignHeight(signHeight + "");
        position.setOffsetY((realPositionProperty.getRealPdfHeight() - realPositionProperty.getStarty() - signHeight) + "");


        assinatura.setPosition(position);
        assinatura.setSignatureKey(UUID.randomUUID().toString());
        try {
//            AssinaturaPDF assinaturaPDF = new AssinaturaPDF(assinatura);
//            outPdf = assinaturaPDF.assina();
        } catch (Exception e) {
            throw new PaasException("签署失败");
        }
        if(outPdf == null){
            throw new PaasException("签署失败");
        }
//        log.info("签署完成了");
        return outPdf ;

    }


    /**
     * @Description #签署
     * @Param [pdfFile 文档, signByte 签章, certInfo 证书, positions 位置]
     * @return byte[]
     **/
    public Map<String,byte[]> signWithYundunHash(Map<String,byte[]> newDocFileByteMap, byte[] entSealByte, String orderNo, List<YundunSignPositionArrayData> yundunSignPositionArrayDatas, String signType){
//        log.info("开始签署了");
        //开始签署
        //byte [] outPdf = pdfFile ;

        VerifySignDocumentRequest verifySignDocumentRequest = new VerifySignDocumentRequest();
        List<DocumentInfo> documentList = new ArrayList<>();
        verifySignDocumentRequest.setSeal(Base64.encode(entSealByte));
        verifySignDocumentRequest.setOrderNo(orderNo);

        Map<String,AssinaturaPDF2> asssinaturePdfMap = new HashMap<String,AssinaturaPDF2>();
        //List<AssinaturaPDF2> assinaturas = new ArrayList<>();
        AssinaturaModel assinatura = null;

        //遍历每个文件，设置签署位置，执行签署
        for (Map.Entry<String, byte[]> entry : newDocFileByteMap.entrySet()) {
            byte[] newDocFileByte = null;
            String docId = entry.getKey();
            byte[] docBytes = entry.getValue();


            byte [] newPDF = null;
            //签署所需基础数据
            assinatura = new AssinaturaModel();
            assinatura.setLocation("无");
            assinatura.setReason("开放签电子签名");
            //文件
            assinatura.setPdf(docBytes);
            //签章
            assinatura.setSignatureImage(entSealByte);
            List<AssinaturaPosition> realPositions = new ArrayList<>();

            for(int i = 0 ; i < yundunSignPositionArrayDatas.size() ; i++){

                YundunSignPositionArrayData yundunSignPositionArrayData = yundunSignPositionArrayDatas.get(i);

                if(docId.equals(yundunSignPositionArrayData.getDocId())){
                    List<YundunSignPositionData> yundunSignPositionDataList = yundunSignPositionArrayData.getYundunSignPositionDataList();

                    for (YundunSignPositionData yundunSignPositionData : yundunSignPositionDataList){
                        RealPositionProperty realPositionProperty = yundunSignPositionData.getSealPosition();
                        byte[] sealImgByte = yundunSignPositionData.getSealImgByte();

                        AssinaturaPosition position = new AssinaturaPosition();
                        position.setPage(realPositionProperty.getPageNum());
                        position.setOffsetX(realPositionProperty.getStartx() + "");
                        position.setSignWidth((realPositionProperty.getEndx() - realPositionProperty.getStartx()) + "");
                        //纵坐标，pdfbox是从下向上计算的
                        float signHeight = realPositionProperty.getStarty() - realPositionProperty.getEndy();
                        if(signHeight < 0){
                            signHeight = realPositionProperty.getEndy() - realPositionProperty.getStarty() ;
                        }
                        position.setSignHeight(signHeight + "");
                        position.setOffsetY((realPositionProperty.getRealPdfHeight() - realPositionProperty.getStarty() - signHeight) + "");
                        position.setSeal(sealImgByte);
                        position.setFieldName("Sign");

                        realPositions.add(position);
                    }
                    assinatura.setPositions(realPositions);
                }
                try {
                    AssinaturaPDF2 assinaturaPDF = new AssinaturaPDF2(assinatura, false);
                    assinaturaPDF.setTsaUrl("http://43.139.245.54:8082/tsa/sign?type=RSA");
                    asssinaturePdfMap.put(docId,assinaturaPDF);
                    if (assinaturaPDF.getLateExternalSigning()) {
                        LateExternalSignData signData = assinaturaPDF.getLateExternalSignData();

                        // 构建云盾签署请求
                        DocumentInfo documentInfo = new DocumentInfo();
                        documentInfo.setDocumentId(docId);
                        //documentInfo.setDocumentName();
                        documentInfo.setDocumentHash(org.apache.pdfbox.util.Hex.getString((signData.getFileHash())));
                        documentList.add(documentInfo);

                    }
                }catch (Exception e){
                    e.printStackTrace();
                    throw new PaasException("签署失败");
                }
                verifySignDocumentRequest.setDocuments(documentList);
            }
        }

        try {
            //构建云盾意愿校验签署返回数据
            AuthSignDocumentResponse authSignDocumentResponse = null;
            // 返回云盾签署数据
            authSignDocumentResponse = signServiceExternal.submitAuthHashSign(verifySignDocumentRequest);
            List<DocumentInfo> yundunDocumentList = authSignDocumentResponse.getDocuments();
            if(authSignDocumentResponse != null && yundunDocumentList !=null && yundunDocumentList.size() >0){
                for(DocumentInfo documentInfoTemp : yundunDocumentList){
                    AssinaturaPDF2 assinaturaPDF = asssinaturePdfMap.get(documentInfoTemp.getDocumentId());
                    byte [] outPdf = assinaturaPDF.assina();
                    if (assinaturaPDF.getLateExternalSigning()) {
                        LateExternalSignData signData = assinaturaPDF.getLateExternalSignData();
                        byte[] newPDF = AddExternalSignature.addSignature(outPdf, signData.getOffset(), Hex.decode(documentInfoTemp.getSignature()));
                        newDocFileByteMap.put(documentInfoTemp.getDocumentId(),newPDF);
                    }
                }
            }else{
                throw new PaasException("哈希签署失败");
            }
        } catch (Exception e) {
            throw new PaasException("签署失败");
        }
//        log.info("签署完成了");
        return newDocFileByteMap ;
    }

    /**
     * @Description #哈希签署
     * @Param [pdfFile 文档, signByte 签章, certInfo 证书, positions 位置]
     * @return byte[]
     **/
    public byte[] signWithHash(byte[] pdfFile, byte[] sealByte, List<RealPositionProperty> positions,String signHash){
//        log.info("开始签署了");
        //开始签署
        byte [] outPdf = pdfFile ;
        byte [] newPDF = null;
        //签署所需基础数据
        AssinaturaModel assinatura = new AssinaturaModel();
        assinatura.setLocation("无");
        assinatura.setReason("开放签电子签名");
        //文件
        assinatura.setPdf(pdfFile);
        //签章
        assinatura.setSignatureImage(sealByte);
        List<AssinaturaPosition> realPositions = new ArrayList<>();

        for(RealPositionProperty realPositionProperty : positions){

            AssinaturaPosition position = new AssinaturaPosition();
            position.setPage(realPositionProperty.getPageNum());
            position.setOffsetX(realPositionProperty.getStartx() + "");
            position.setSignWidth((realPositionProperty.getEndx() - realPositionProperty.getStartx()) + "");
            //纵坐标，pdfbox是从下向上计算的
            float signHeight = realPositionProperty.getStarty() - realPositionProperty.getEndy();
            if(signHeight < 0){
                signHeight = realPositionProperty.getEndy() - realPositionProperty.getStarty() ;
            }
            position.setSignHeight(signHeight + "");
            position.setOffsetY((realPositionProperty.getRealPdfHeight() - realPositionProperty.getStarty() - signHeight) + "");

            position.setSeal(sealByte);
            position.setFieldName("Sign");

            realPositions.add(position);

            try {
                AssinaturaPDF2 assinaturaPDF = new AssinaturaPDF2(assinatura,false);
                byte[] outPdfNew = assinaturaPDF.assina();
            } catch (Exception e) {
                throw new PaasException("签署失败");
            }
        }

        assinatura.setPositions(realPositions);

        try {
            AssinaturaPDF2 assinaturaPDF = new AssinaturaPDF2(assinatura,false);
            byte[] outPdfNew = assinaturaPDF.assina();
            if(assinaturaPDF.getLateExternalSigning()){
                LateExternalSignData signData = assinaturaPDF.getLateExternalSignData();
                newPDF = AddExternalSignature.addSignature(outPdfNew, signData.getOffset(), Hex.decode(signHash));
            }
        } catch (Exception e) {
            throw new PaasException("签署失败");
        }
        if(newPDF == null){
            throw new PaasException("签署失败");
        }
//        log.info("签署完成了");
        return newPDF ;
    }

    /**
     * @Description #签署
     * @Param [pdfFile 文档, signByte 签章, certInfo 证书, positions 位置]
     * @return byte[]
     **/
    public byte[] sign(byte[] pdfFile, byte[] signByte, CertificateInfo certInfo, List<RealPositionProperty> positions){
//        log.info("开始签署了");
        //开始签署
        byte [] outPdf = pdfFile ;
        for(RealPositionProperty realPositionProperty : positions){
            //签署所需基础数据
            AssinaturaModel assinatura = new AssinaturaModel();
            assinatura.setLocation("无");
            assinatura.setReason("防伪造防篡改数字校验");
            //文件
            assinatura.setPdf(outPdf);
            //签章
            assinatura.setSignatureImage(signByte);
            //证书
            //assinatura.setCertInfo(certInfo);
            //签署位置
            AssinaturaPosition position = new AssinaturaPosition();
            position.setPage(realPositionProperty.getPageNum());
            //横坐标
            position.setOffsetX(realPositionProperty.getStartx() + "");
            position.setSignWidth((realPositionProperty.getEndx() - realPositionProperty.getStartx()) + "");
            //纵坐标，pdfbox是从下向上计算的
            float signHeight = realPositionProperty.getStarty() - realPositionProperty.getEndy();
            if(signHeight < 0){
                signHeight = realPositionProperty.getEndy() - realPositionProperty.getStarty() ;
            }
            position.setSignHeight(signHeight + "");
            position.setOffsetY((realPositionProperty.getRealPdfHeight() - realPositionProperty.getStarty() - signHeight) + "");


            assinatura.setPosition(position);
            assinatura.setSignatureKey(UUID.randomUUID().toString());
            try {
//                AssinaturaPDF assinaturaPDF = new AssinaturaPDF(assinatura);
//                outPdf = assinaturaPDF.assina();
            } catch (Exception e) {
                throw new PaasException("签署失败");
            }
        }
        if(outPdf == null){
            throw new PaasException("签署失败");
        }
//        log.info("签署完成了");
        return outPdf ;
    }

    /**
     * @Description #填写
     * @Param [pdfFile pdf文件, positions 填写位置列表
     * @return byte[]
     **/
    public byte[] write(byte[] pdfFile, List<RealPositionProperty> positions){
        byte [] outPdf = pdfFile ;
        try {
            //创建填写操作对象
            WritePDFForm writePDFForm = new WritePDFForm(outPdf);
            List<FormField> fields = new ArrayList<>();
//            //设置字体
//            File fontFile = new File(fileProperties.getPath().getPath() + "STSONG.TTF");
//            File fontFile = new File(fileProperties.getPath().getPath() + "simsun.ttf");
//            InputStream fontInputStream = new FileInputStream(fontFile);
//            List<FormField> fields = new ArrayList<>();
//            //构建表单
//            CreatedPDFForm createdPDFForm = new CreatedPDFForm(outPdf);
            for(RealPositionProperty realPositionProperty : positions){

                FormField formField = new FormField();
                //位置坐标
                formField.setOffsetX(realPositionProperty.getStartx() + "");
                formField.setOffsetY(realPositionProperty.getStarty() + "");
                formField.setWidth((realPositionProperty.getEndx() - realPositionProperty.getStartx()) + "");
                formField.setHeight((realPositionProperty.getEndy() - realPositionProperty.getStarty()) + "");
                //页码
                formField.setPage(realPositionProperty.getPageNum());
                //对齐方式
                if(realPositionProperty.getAlign() != null && realPositionProperty.getAlign().length() != 0){
                    formField.setTextAlign(realPositionProperty.getAlign());
                }else {
                    formField.setTextAlign("left");
                }
                //字体大小
                if(realPositionProperty.getFontSize() != null){
                    formField.setFontSize(realPositionProperty.getFontSize());
                }else {
                    formField.setFontSize("0");
                }

                //字体
                if(realPositionProperty.getFontFamily() != null){
//                    if(FontEnum.Arial.getName().equals(realPositionProperty.getFontFamily())){
//                        formField.setFontFamily(FontEnum.Arial.getFontKey());
//                    }else
                    if(FontEnum.SimSun.getName().equals(realPositionProperty.getFontFamily())){
                        formField.setFontFamily(FontEnum.SimSun.getFontKey());
                    }else if(FontEnum.SimHei.getName().equals(realPositionProperty.getFontFamily())){
                        formField.setFontFamily(FontEnum.SimHei.getFontKey());
                    }else if(FontEnum.SimKai.getName().equals(realPositionProperty.getFontFamily())){
                        formField.setFontFamily(FontEnum.SimKai.getFontKey());
                    }else if(FontEnum.SimFang.getName().equals(realPositionProperty.getFontFamily())){
                        formField.setFontFamily(FontEnum.SimFang.getFontKey());
                    }
                }

                formField.setInterfaceParamName(UUID.randomUUID().toString());

                formField.setControlType(realPositionProperty.getControlType());

                if(formField.getControlType().equals(ControlTypeEnum.DROPDOWN_LIST.getName())){
                    //下拉列表
                    if(realPositionProperty.getWidgetPropertyList() != null && realPositionProperty.getWidgetPropertyList().size() > 0 && realPositionProperty.getValue() != null){
                        List<Widget> widgetDropdownList = new ArrayList<>();
                        for(ControlWidgetProperty widgetProperty : realPositionProperty.getWidgetPropertyList()){
                            Widget widget = new Widget(widgetProperty.getN(),widgetProperty.getV());
                            if(realPositionProperty.getValue().contains(widgetProperty.getUid())){
                                widget.setWidgetValue(true);
                            }else {
                                widget.setWidgetValue(false);
                            }
                            widgetDropdownList.add(widget);
                        }
                        formField.setWidgets(widgetDropdownList);
                    }
                }else if(formField.getControlType().equals(ControlTypeEnum.RADIO.getName())){
                    //单选
                    if(realPositionProperty.getWidgetPropertyList() != null && realPositionProperty.getWidgetPropertyList().size() > 0 && realPositionProperty.getValue() != null){
                        List<Widget> widgetRadioList = new ArrayList<>();
                        for(ControlWidgetProperty widgetProperty : realPositionProperty.getWidgetPropertyList()){
                            Widget widget = new Widget(widgetProperty.getX()+ "",widgetProperty.getY() + "",widgetProperty.getN(),widgetProperty.getV());
                            widget.setWidth(widgetProperty.getW());
                            widget.setHeight(widgetProperty.getH());
                            if(realPositionProperty.getValue().contains(widgetProperty.getUid())){
                                widget.setWidgetValue(true);
                            }else {
                                widget.setWidgetValue(false);
                            }
                            widgetRadioList.add(widget);
                        }
                        formField.setWidgets(widgetRadioList);
                    }
                }else if(formField.getControlType().equals(ControlTypeEnum.CHECKBOX.getName())){
                    //复选
                    if(realPositionProperty.getWidgetPropertyList() != null && realPositionProperty.getWidgetPropertyList().size() > 0 && realPositionProperty.getValue() != null) {
                        List<Widget> widgetCheckBoxList = new ArrayList<>();
                        for (ControlWidgetProperty widgetProperty : realPositionProperty.getWidgetPropertyList()) {
                            Widget widget = new Widget(widgetProperty.getX() + "",widgetProperty.getY() + "",widgetProperty.getN(),widgetProperty.getV());
                            if(realPositionProperty.getValue().contains(widgetProperty.getUid())){
                                widget.setWidgetValue(true);
                            }else {
                                widget.setWidgetValue(false);
                            }
                            widgetCheckBoxList.add(widget);
                        }
                        formField.setWidgets(widgetCheckBoxList);
                    }
                }else if(formField.getControlType().equals(ControlTypeEnum.IMAGE.getName())){
                    //图片
                    byte[] imageByte = signFileService.getByteById(realPositionProperty.getValue());
                    formField.setValue(imageByte);
                }else {
                    //单行文本、多行文本、数字、图片
                    //填写值
                    formField.setValue(realPositionProperty.getValue());
                }
                fields.add(formField);
            }
            //输出文件字节数组
//            outPdf = createdPDFForm.addFormsTTF(fields,fontInputStream);
//            log.info("填写参数============================" + JSON.toJSONString(fields));
            outPdf = writePDFForm.addForms(fields,fileProperties.getPath().getPath());
        } catch (Exception e) {
            e.printStackTrace();
            throw new PaasException("填写失败");
        }
        return outPdf ;
    }



//    /**
//     * @Description #填写
//     * @Param [pdfFile pdf文件, positions 填写位置列表
//     * @return byte[]
//     **/
//    public byte[] write(byte[] pdfFile, List<RealPositionProperty> positions){
//        byte [] outPdf = pdfFile ;
//        try {
//
//            for(RealPositionProperty realPositionProperty : positions){
//                //加载pdf文件
//                PDDocument pdDocument = Loader.loadPDF(outPdf);
//                //获取pdf文件指定的页码
//                PDPage page = pdDocument.getPage(realPositionProperty.getPageNum());
//                //创建page数据流对象，PDPageContentStream.AppendMode.APPEND，使用追加类型，否则覆盖或新增页
//                PDPageContentStream contentStream = new PDPageContentStream(pdDocument,page,PDPageContentStream.AppendMode.APPEND,true,false);
//                //设置字体
//                File fontFile = new File(fileProperties.getPath().getPath() + "STSONG.TTF");
//                InputStream fontInputStream = new FileInputStream(fontFile);
//                //embedSubset需要为true，否则会将字体文件也嵌入到文件中导致文件变大
//                PDType0Font font = PDType0Font.load(pdDocument, fontInputStream,true);
////                PDType0Font font = PDType0Font.load(pdDocument, fontInputStream,false);
//                //设置字体和文字大小
//                contentStream.setFont(font,realPositionProperty.getFontSize());
//                //开始写入
//                contentStream.beginText();
//                //设置位置
//                //获取对其方式，计算位置
//                float startX = 0 ;
//                float endX = realPositionProperty.getEndx() ;
//                int length = realPositionProperty.getValue().length();
//                float textWidth = realPositionProperty.getEndx() - realPositionProperty.getStartx() ;
//                if(realPositionProperty.getAlign().equals(ControlAlignment.ALIGN_LEFT.getName())){
//                    //左对其
//                    startX = realPositionProperty.getStartx() ;
//                }else if(realPositionProperty.getAlign().equals(ControlAlignment.ALIGN_RIGHT.getName())){
//                    //右对其
//                    BigDecimal divide = new BigDecimal(textWidth).divide(new BigDecimal(length), 4, RoundingMode.DOWN);
//                    int i = divide.intValue();
//                }else if(realPositionProperty.getAlign().equals(ControlAlignment.ALIGN_CENTER.getName())){
//                    //居中
//
//                }
////                contentStream.newLineAtOffset(realPositionProperty.getStartx(),realPositionProperty.getStarty());
//                contentStream.newLineAtOffset(startX,realPositionProperty.getStarty());
//
//                //写入文字
//                contentStream.showText(realPositionProperty.getValue());
//                //结束写入
//                contentStream.endText();
//                contentStream.close();
//
//                ByteArrayOutputStream out = new ByteArrayOutputStream();
//                pdDocument.save(out);
//                pdDocument.close();
//                //输出文件字节数组
//                outPdf = out.toByteArray();
//            }
//
//
//        } catch (Exception e) {
//            throw new PaasException("填写失败");
//        }
//        return outPdf ;
//    }


    /**
     * @Description #无证书签署（将图片插入到pdf文件中）
     * @Param [realPositionProperties 位置列表 , docFile pdf文件 , entSealByte 签章图片]
     * @return byte[]
     **/
    public byte[] insertImage(List<RealPositionProperty> realPositionProperties, byte[] docFile, byte[] entSealByte){
        byte [] outPdf = docFile ;
        try {

            for(RealPositionProperty realPositionProperty : realPositionProperties){
                //加载pdf文件
                PDDocument pdDocument = Loader.loadPDF(outPdf);
                //获取pdf文件指定页码
                PDPage page = pdDocument.getPage(realPositionProperty.getPageNum());
                //创建page数据流对象，PDPageContentStream.AppendMode.APPEND，使用追加类型，否则覆盖或新增页
                PDPageContentStream contentStream = new PDPageContentStream(pdDocument,page,PDPageContentStream.AppendMode.APPEND,true,false);
                //创建要插入的图片数据
                PDImageXObject imageXObject = PDImageXObject.createFromByteArray(pdDocument, entSealByte, UUID.randomUUID().toString());
                float height = 0 ;
                if(realPositionProperty.getEndy() > realPositionProperty.getStarty()){
                    height = realPositionProperty.getEndy() - realPositionProperty.getStarty() ;
                }else {
                    height = realPositionProperty.getStarty() - realPositionProperty.getEndy() ;
                }
                float width = realPositionProperty.getEndx() - realPositionProperty.getStartx() ;

                contentStream.drawImage(imageXObject,realPositionProperty.getStartx() ,realPositionProperty.getStarty() ,width ,height);
                contentStream.close();

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                pdDocument.save(out);
                pdDocument.close();
                //输出文件字节数组
                outPdf = out.toByteArray();
            }

        } catch (Exception e) {
            throw new PaasException("无证书签署失败");
        }

        return outPdf ;
    }
}