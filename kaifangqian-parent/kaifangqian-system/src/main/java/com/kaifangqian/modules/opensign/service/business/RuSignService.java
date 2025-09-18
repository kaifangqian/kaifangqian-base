/**
 * @description 业务线实例-签署业务实现
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

import com.alibaba.fastjson.JSON;
import com.kaifangqian.external.sign.request.VerifySignDocumentRequest;
import com.kaifangqian.modules.opensign.pdfbox.PdfboxService;
import com.kaifangqian.modules.opensign.service.business.vo.YundunSignPositionData;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.opensign.entity.SignRuDocControl;
import com.kaifangqian.modules.opensign.enums.ControlPropertyTypePageConfigEnum;
import com.kaifangqian.modules.opensign.enums.ControlTypeEnum;
import com.kaifangqian.modules.opensign.pdfbox.vo.CertificateInfo;
import com.kaifangqian.modules.opensign.service.tool.CalculatePositionService;
//import com.kaifangqian.modules.opensign.service.tool.PdfOperateService;
//import com.kaifangqian.modules.opensign.service.tool.SignHandleService;
import com.kaifangqian.modules.opensign.service.tool.CutImageService;
import com.kaifangqian.modules.opensign.service.tool.pojo.CertificateProperty;
import com.kaifangqian.modules.opensign.service.tool.pojo.ControlWidgetProperty;
import com.kaifangqian.modules.opensign.service.tool.pojo.RealPositionProperty;
import com.kaifangqian.modules.opensign.service.tool.pojo.SourcePositionProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 业务线实例-签署业务实现
 * @Package: com.kaifangqian.modules.opensign.service.business
 * @ClassName: RuSignService
 * @author: FengLai_Gong
 */
@Service
public class RuSignService {


//    @Autowired
//    private PdfOperateService pdfOperateService ;
//    @Autowired
//    private SignHandleService signHandleService;
    @Autowired
    private CalculatePositionService calculatePositionService;
    @Autowired
    private CutImageService cutImageService ;

    @Autowired
    private PdfboxService pdfboxService ;
    @Autowired
    private PdfEncryptionService pdfEncryptionService ;


    /**
     * @Description #签署业务，该方法只能应对单一文件的操作
     * @Param [realPositionProperties 真实签署位置, docFile签署文件,entSealByte签章数据，certificateProperty证书相关数据]
     * @return void
     **/
    public List<YundunSignPositionData> generateSignPosition(SignRuDocControl signRuDocControl, byte[] docFile, byte[] entSealByte, ControlPropertyTypePageConfigEnum pageConfigEnum, String pageConfigValue){
        //创建签署位置属性对象列表
        SourcePositionProperty sourcePositionProperty = new SourcePositionProperty();
        sourcePositionProperty.setOffsetX(Float.valueOf(signRuDocControl.getOffsetX() == null ? "0" : signRuDocControl.getOffsetX()));
        sourcePositionProperty.setOffsetY(Float.valueOf(signRuDocControl.getOffsetY()));
        sourcePositionProperty.setHeight(Float.valueOf(signRuDocControl.getHeight()));
        sourcePositionProperty.setWidth(Float.valueOf(signRuDocControl.getWidth()));
        sourcePositionProperty.setPageHeight(Float.valueOf(signRuDocControl.getPageHeight()));
        sourcePositionProperty.setPageWidth(Float.valueOf(signRuDocControl.getPageWidth()));
        sourcePositionProperty.setControlType(signRuDocControl.getControlType());

        List<YundunSignPositionData> yundunSignPositionDataList = new ArrayList<YundunSignPositionData>();

        try {
            //文件加密
            //newFileBytes = pdfEncryptionService.pdfToEncrypted(newFileBytes);

            //进行签署
            if(signRuDocControl.getControlType().equals(ControlTypeEnum.CHOP_STAMP.getName())){
                List<RealPositionProperty> realPositionProperties = calculatePositionService.calculateChopStampPositionsReal(sourcePositionProperty, docFile, pageConfigEnum, pageConfigValue,entSealByte);
                if(realPositionProperties == null || realPositionProperties.size() == 0){
                    throw new PaasException("签署失败，签署位置计算失败");
                }
                //骑缝签
                List<byte[]> imageList = cutImageService.cutImage(entSealByte, realPositionProperties.size());
                for(int i = 0 ; i < realPositionProperties.size() ; i++){
                    RealPositionProperty realPositionProperty = realPositionProperties.get(i);
                    byte[] cutImageByte = imageList.get(i);
                    YundunSignPositionData yundunSignPositionData = new YundunSignPositionData();
                    yundunSignPositionData.setSealPosition(realPositionProperty);
                    yundunSignPositionData.setSealImgByte(cutImageByte);
                    yundunSignPositionDataList.add(yundunSignPositionData);
                    //newFileBytes = pdfboxService.sign(newFileBytes, cutImageByte, certInfo, realPositionProperty);
                }
            }else {
                List<RealPositionProperty> realPositionProperties = calculatePositionService.calculateSignPositionsReal(sourcePositionProperty, docFile, pageConfigEnum, pageConfigValue,entSealByte);
                if(realPositionProperties == null || realPositionProperties.size() == 0){
                    throw new PaasException("签署失败，签署位置计算失败");
                }
                for(int i = 0 ; i < realPositionProperties.size() ; i++){
                    RealPositionProperty realPositionProperty = realPositionProperties.get(i);
                    YundunSignPositionData yundunSignPositionData = new YundunSignPositionData();
                    yundunSignPositionData.setSealPosition(realPositionProperty);
                    yundunSignPositionData.setSealImgByte(entSealByte);
                    yundunSignPositionDataList.add(yundunSignPositionData);
                }
                //newFileBytes = pdfboxService.signWithHash(newFileBytes, entSealByte, realPositionProperties, signHash);
            }
        } catch (Exception e) {
            throw new PaasException("签署失败,签署位置计算失败");
        }
        return yundunSignPositionDataList ;
    }

    /**
     * @Description #签署业务，该方法只能应对单一文件的操作
     * @Param [realPositionProperties 真实签署位置, docFile签署文件,entSealByte签章数据，certificateProperty证书相关数据]
     * @return void
     **/
    public byte[] signWithYunDun(SignRuDocControl signRuDocControl, byte[] docFile, byte[] entSealByte, ControlPropertyTypePageConfigEnum pageConfigEnum, String pageConfigValue,String signHash){
        byte[] newFileBytes = docFile ;
        //创建签署位置属性对象列表
        SourcePositionProperty sourcePositionProperty = new SourcePositionProperty();
        sourcePositionProperty.setOffsetX(Float.valueOf(signRuDocControl.getOffsetX() == null ? "0" : signRuDocControl.getOffsetX()));
        sourcePositionProperty.setOffsetY(Float.valueOf(signRuDocControl.getOffsetY()));
        sourcePositionProperty.setHeight(Float.valueOf(signRuDocControl.getHeight()));
        sourcePositionProperty.setWidth(Float.valueOf(signRuDocControl.getWidth()));
        sourcePositionProperty.setPageHeight(Float.valueOf(signRuDocControl.getPageHeight()));
        sourcePositionProperty.setPageWidth(Float.valueOf(signRuDocControl.getPageWidth()));
        sourcePositionProperty.setControlType(signRuDocControl.getControlType());
        try {
            //文件加密
            newFileBytes = pdfEncryptionService.pdfToEncrypted(newFileBytes);

            //进行签署
            if(signRuDocControl.getControlType().equals(ControlTypeEnum.CHOP_STAMP.getName())){
                List<RealPositionProperty> realPositionProperties = calculatePositionService.calculateChopStampPositionsReal(sourcePositionProperty, docFile, pageConfigEnum, pageConfigValue,entSealByte);
                if(realPositionProperties == null || realPositionProperties.size() == 0){
                    throw new PaasException("签署失败，签署位置计算失败");
                }
                //骑缝签
                List<byte[]> imageList = cutImageService.cutImage(entSealByte, realPositionProperties.size());
                //newFileBytes = pdfboxService.signWithHashChop(newFileBytes, imageList, realPositionProperties,signHash);
//                for(int i = 0 ; i < realPositionProperties.size() ; i++){
//                    RealPositionProperty realPositionProperty = realPositionProperties.get(i);
//                    byte[] cutImageByte = imageList.get(i);
//                    newFileBytes = pdfboxService.sign(newFileBytes, cutImageByte, certInfo, realPositionProperty);
//                }
            }else {
                List<RealPositionProperty> realPositionProperties = calculatePositionService.calculateSignPositionsReal(sourcePositionProperty, docFile, pageConfigEnum, pageConfigValue,entSealByte);
                if(realPositionProperties == null || realPositionProperties.size() == 0){
                    throw new PaasException("签署失败，签署位置计算失败");
                }
                newFileBytes = pdfboxService.signWithHash(newFileBytes, entSealByte, realPositionProperties, signHash);
            }
        } catch (Exception e) {
            throw new PaasException("签署失败");
//            e.printStackTrace();
//            throw e;
        }
        return newFileBytes ;
    }

    /**
     * @Description #签署业务，该方法只能应对单一文件的操作
     * @Param [realPositionProperties 真实签署位置, docFile签署文件,entSealByte签章数据，certificateProperty证书相关数据]
     * @return void
     **/
    public byte[] sign(SignRuDocControl signRuDocControl, byte[] docFile, byte[] entSealByte, CertificateProperty certificateProperty, ControlPropertyTypePageConfigEnum pageConfigEnum, String pageConfigValue){
        byte[] newFileBytes = docFile ;
        //转换成pdfbox需要的证书对象
        CertificateInfo certInfo = new CertificateInfo();
        certInfo.setCert(certificateProperty.getCertFile());
        certInfo.setPassword(certificateProperty.getPassword());
        certInfo.setCertType(CertificateInfo.CertTypeEnum.PKCS12);
        //创建签署位置属性对象列表
        SourcePositionProperty sourcePositionProperty = new SourcePositionProperty();
        sourcePositionProperty.setOffsetX(Float.valueOf(signRuDocControl.getOffsetX() == null ? "0" : signRuDocControl.getOffsetX()));
        sourcePositionProperty.setOffsetY(Float.valueOf(signRuDocControl.getOffsetY()));
        sourcePositionProperty.setHeight(Float.valueOf(signRuDocControl.getHeight()));
        sourcePositionProperty.setWidth(Float.valueOf(signRuDocControl.getWidth()));
        sourcePositionProperty.setPageHeight(Float.valueOf(signRuDocControl.getPageHeight()));
        sourcePositionProperty.setPageWidth(Float.valueOf(signRuDocControl.getPageWidth()));
        sourcePositionProperty.setControlType(signRuDocControl.getControlType());
        try {
            //文件加密
            newFileBytes = pdfEncryptionService.pdfToEncrypted(newFileBytes);

            //进行签署
            if(signRuDocControl.getControlType().equals(ControlTypeEnum.CHOP_STAMP.getName())){
                List<RealPositionProperty> realPositionProperties = calculatePositionService.calculateChopStampPositionsReal(sourcePositionProperty, docFile, pageConfigEnum, pageConfigValue,entSealByte);
                if(realPositionProperties == null || realPositionProperties.size() == 0){
                    throw new PaasException("签署失败，签署位置计算失败");
                }
                //骑缝签
                List<byte[]> imageList = cutImageService.cutImage(entSealByte, realPositionProperties.size());
                for(int i = 0 ; i < realPositionProperties.size() ; i++){
                    RealPositionProperty realPositionProperty = realPositionProperties.get(i);
                    byte[] cutImageByte = imageList.get(i);
                    newFileBytes = pdfboxService.sign(newFileBytes, cutImageByte, certInfo, realPositionProperty);
                }
            }else {
                List<RealPositionProperty> realPositionProperties = calculatePositionService.calculateSignPositionsReal(sourcePositionProperty, docFile, pageConfigEnum, pageConfigValue,entSealByte);
                if(realPositionProperties == null || realPositionProperties.size() == 0){
                    throw new PaasException("签署失败，签署位置计算失败");
                }
                newFileBytes = pdfboxService.sign(newFileBytes, entSealByte, certInfo, realPositionProperties);
            }
        } catch (Exception e) {
            throw new PaasException("签署失败");
//            e.printStackTrace();
//            throw e;
        }
        return newFileBytes ;
    }



    /**
     * @Description #无证书签署业务，该方法只能应对单一文件的操作
     * @Param [controlList 签署控件列表, docFile签署文件,entSealByte签章数据]
     * @return void
     **/
    public byte[] signNoCert(SignRuDocControl signRuDocControl, byte[] docFile, byte[] entSealByte, ControlPropertyTypePageConfigEnum pageConfigEnum, String pageConfigValue){

        byte[] newFileBytes = docFile ;
        //创建签署位置属性对象列表
        SourcePositionProperty sourcePositionProperty = new SourcePositionProperty();
        sourcePositionProperty.setOffsetX(Float.valueOf(signRuDocControl.getOffsetX()));
        sourcePositionProperty.setOffsetY(Float.valueOf(signRuDocControl.getOffsetY()));
        sourcePositionProperty.setHeight(Float.valueOf(signRuDocControl.getHeight()));
        sourcePositionProperty.setWidth(Float.valueOf(signRuDocControl.getWidth()));
        sourcePositionProperty.setPageHeight(Float.valueOf(signRuDocControl.getPageHeight()));
        sourcePositionProperty.setPageWidth(Float.valueOf(signRuDocControl.getPageWidth()));
        try {
            //进行签署

            if(signRuDocControl.getControlType().equals(ControlTypeEnum.CHOP_STAMP.getName())){
                List<RealPositionProperty> realPositionProperties = calculatePositionService.calculateChopStampPositionsReal(sourcePositionProperty, docFile, pageConfigEnum, pageConfigValue,entSealByte);
                if(realPositionProperties == null || realPositionProperties.size() == 0){
                    throw new PaasException("签署失败，签署位置计算失败");
                }
                //骑缝签
                List<byte[]> imageList = cutImageService.cutImage(entSealByte, realPositionProperties.size());
                for(int i = 0 ; i < realPositionProperties.size() ; i++){
                    //获取切割后的图片
                    RealPositionProperty realPositionProperty = realPositionProperties.get(i);
                    byte[] cutImageByte = imageList.get(i);

                    List<RealPositionProperty> temp = new ArrayList<>();
                    temp.add(realPositionProperty);

                    newFileBytes = pdfboxService.insertImage(temp,newFileBytes, cutImageByte);
                }
            }else {
                List<RealPositionProperty> realPositionProperties = calculatePositionService.calculateSignPositionsReal(sourcePositionProperty, docFile, pageConfigEnum, pageConfigValue,entSealByte);
                if(realPositionProperties == null || realPositionProperties.size() == 0){
                    throw new PaasException("签署失败，签署位置计算失败");
                }
                newFileBytes = pdfboxService.insertImage( realPositionProperties,newFileBytes, entSealByte);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newFileBytes ;
    }




    /**
     * @Description #填写业务，该方法只能应对单一文件的操作
     * @Param [controlList 填写控件列表, docFile签署文件]
     * @return void
     **/
    public byte[] write(List<SignRuDocControl> controlList,byte[] docFile){
        List<SourcePositionProperty> sourcePositionPropertyList = new ArrayList<>();
        //转换成原始文件坐标对象
        for(SignRuDocControl signDocControl : controlList) {

            //创建签署位置属性对象列表
            SourcePositionProperty sourcePositionProperty = new SourcePositionProperty();
            sourcePositionProperty.setOffsetX(Float.valueOf(signDocControl.getOffsetX()));
            sourcePositionProperty.setOffsetY(Float.valueOf(signDocControl.getOffsetY()));
            sourcePositionProperty.setHeight(Float.valueOf(signDocControl.getHeight()));
            sourcePositionProperty.setWidth(Float.valueOf(signDocControl.getWidth()));
            sourcePositionProperty.setPageHeight(Float.valueOf(signDocControl.getPageHeight()));
            sourcePositionProperty.setPageWidth(Float.valueOf(signDocControl.getPageWidth()));
            sourcePositionProperty.setPage(signDocControl.getPage());

            sourcePositionProperty.setValue(signDocControl.getValue());
            sourcePositionProperty.setAlign(signDocControl.getTextAlign());
            sourcePositionProperty.setFontFamily(signDocControl.getFontFamily());
            sourcePositionProperty.setFontSize(signDocControl.getFontSize());

            sourcePositionProperty.setControlType(signDocControl.getControlType());
            //填写控件内部属性
            if(signDocControl.getProperties() != null && signDocControl.getProperties().length() > 0){
                if(sourcePositionProperty.getControlType() != null &&
                        (sourcePositionProperty.getControlType().equals(ControlTypeEnum.DROPDOWN_LIST.getName()) || sourcePositionProperty.getControlType().equals(ControlTypeEnum.CHECKBOX.getName()) ||
                                sourcePositionProperty.getControlType().equals(ControlTypeEnum.RADIO.getName()))
                ){
                    List<ControlWidgetProperty> widgetPropertyList = JSON.parseArray(signDocControl.getProperties(), ControlWidgetProperty.class);
                    if(widgetPropertyList != null && widgetPropertyList.size() > 0){
                        sourcePositionProperty.setWidgetPropertyList(widgetPropertyList);
                    }
                }
            }

            sourcePositionPropertyList.add(sourcePositionProperty);
        }
        //计算位置
        List<RealPositionProperty> realPositionProperties = calculatePositionService.calculateWritePositionsReal(sourcePositionPropertyList, docFile);
        //进行填写
//        byte[] bytes = pdfOperateService.insertContextByRealPositions(docFile, realPositionProperties);
        byte[] bytes = pdfboxService.write(docFile, realPositionProperties);
        return bytes ;
    }

    public byte[] signForYundun(VerifySignDocumentRequest verifySignDocumentRequest){
        return null;
    }




}