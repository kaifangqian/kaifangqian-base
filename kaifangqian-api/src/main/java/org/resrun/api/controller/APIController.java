package org.resrun.api.controller;

import org.resrun.api.enums.APIResultEnum;
import org.resrun.api.enums.BusinessErrorEnum;
import org.resrun.api.enums.ParamFormatErrorEnum;
import org.resrun.api.enums.SignTypeEnum;
import org.resrun.api.pdfbox.AssinaturaPDF;
import org.resrun.api.pdfbox.vo.AssinaturaModel;
import org.resrun.api.pdfbox.vo.AssinaturaPosition;
import org.resrun.api.pdfbox.vo.CertificateInfo;
import org.resrun.api.service.CalculatePositionService;
import org.resrun.api.service.CertHttpService;
import org.resrun.api.service.EntSealGenerateService;
import org.resrun.api.service.pojo.*;
import org.resrun.api.utils.Base64;
import org.resrun.api.utils.HttpUtils;
import org.resrun.api.vo.base.SignLocation;
import org.resrun.api.vo.request.CertEventRequest;
import org.resrun.api.vo.request.DocumentSignRequest;
import org.resrun.api.vo.base.Result;
import org.resrun.api.vo.request.SignatureMakeRequest;
import org.resrun.api.vo.response.CertEventResponse;
import org.resrun.api.vo.response.DocumentSignResponse;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.resrun.api.vo.response.SignatureMakeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.*;

/**
 * @Description: APIController
 * @Package: org.resrun.controller
 * @ClassName: APIController
 * @author: FengLai_Gong
 */
@RestController
@RequestMapping("/kaifangqian/openAPI")
public class APIController {


    @Autowired
    private CertHttpService certHttpService ;
    @Autowired
    private CalculatePositionService calculatePositionService ;
    @Autowired
    private EntSealGenerateService entSealGenerateService;

    @PostMapping(value = "/signature/make")
    public Result<SignatureMakeResponse> signatureMake(@RequestBody SignatureMakeRequest request){
        byte [] seal = entSealGenerateService.generateEntSeal(request.getTopText(),request.getMiddleText());
        String sealBase64 = "data:image/png;base64," + Base64.encode(seal);
        SignatureMakeResponse makeResponse = new SignatureMakeResponse(sealBase64);
        return Result.OK(makeResponse,request.getUniqueCode());
    }

    /**
     * @Description #事件证书签发
     * @Param [request]
     * @return org.resrun.vo.base.Result<?>
     **/
    @RequestMapping(value = "/cert/event",method = RequestMethod.POST)
    public Result<CertEventResponse> certEvent(@RequestBody CertEventRequest request){
        CertEventResponse response = new CertEventResponse();
        response.setCertPassword(request.getCertPassword());
//        if(!request.getCertSubject().contains("C=CN") && !request.getCertSubject().contains("CN=")){
//            return Result.error(APIResultEnum.BUSINESS_ERROR,request.getUniqueCode(), BusinessErrorEnum.CERT_SUBJECT_ERROR.getName());
//        }
        //设置证书主体
        request.setCertSubject("C=CN,CN=" + request.getCertSubject());
        //设置返回信息
        CertificationInfo certificationInfo = new CertificationInfo();
        certificationInfo.setCertDn(request.getCertSubject());
        certificationInfo.setPassword(request.getCertPassword());
        certificationInfo.setKeyName(CertHttpService.keyName);
        try {
            certHttpService.generateP10(certificationInfo);
            if(certificationInfo.getP10() == null){
                return Result.error(APIResultEnum.BUSINESS_ERROR,request.getUniqueCode(), BusinessErrorEnum.P10_GENERATE_ERROR.getName());
            }
        }catch (Exception e){
            return Result.error(APIResultEnum.BUSINESS_ERROR,request.getUniqueCode(), BusinessErrorEnum.P10_GENERATE_ERROR.getName());
        }
        ApplyCertData data = null ;
        try {
            ApplyCert applyCert = certHttpService.applyCert(certificationInfo.getP10(),request.getUniqueCode());
            if(applyCert == null){
                return Result.error(APIResultEnum.SERVICE_CONNECT_ERROR.getCode(),APIResultEnum.SERVICE_CONNECT_ERROR.getTemplate(),request.getUniqueCode());
            }
            if(applyCert.getCode() == null || !applyCert.getCode().equals(APIResultEnum.SUCCESS.getCode()) || applyCert.getData() == null || applyCert.getData().getP7b() == null){
                return Result.error(applyCert.getCode(),applyCert.getMessage(),request.getUniqueCode());
            }
            data = applyCert.getData();
            byte[] p7bByte = Base64.decode(data.getP7b());
            if(p7bByte == null){
                return Result.error(APIResultEnum.BUSINESS_ERROR,request.getUniqueCode(), BusinessErrorEnum.P7B_GENERATE_ERROR.getName());
            }
            certificationInfo.setP7bFile(p7bByte);
        }catch (Exception e){
            return Result.error(APIResultEnum.BUSINESS_ERROR,request.getUniqueCode(), BusinessErrorEnum.P7B_GENERATE_ERROR.getName());
        }
        byte[] pfx = null ;
        try {
            certHttpService.p7bToJks(certificationInfo);
            pfx = certHttpService.coverToPfx(certificationInfo.getCertFile(), certificationInfo.getPassword());
            if(pfx == null){
                return Result.error(APIResultEnum.BUSINESS_ERROR,request.getUniqueCode(), BusinessErrorEnum.PFX_GENERATE_ERROR.getName());
            }
        }catch (Exception e){
            return Result.error(APIResultEnum.BUSINESS_ERROR,request.getUniqueCode(), BusinessErrorEnum.PFX_GENERATE_ERROR.getName());
        }


        response.setCertSubject(certificationInfo.getCertDn());
        response.setCertSN(data.getCertSN());

        response.setCertValidity(data.getCertValidity());
        response.setCertValidityNotAfter(data.getCertValidityNotAfter());
        response.setCertValidityNotBefore(data.getCertValidityNotBefore());

        response.setPfx(Base64.encode(pfx));

        return Result.OK(response,request.getUniqueCode()) ;

    }



    /**
     * @Description #文件签署
     * @Param [request]
     * @return org.resrun.vo.base.Result<?>
     **/
    @RequestMapping(value = "/document/sign",method = RequestMethod.POST)
    public Result<DocumentSignResponse> documentSign(@RequestBody DocumentSignRequest request){
        //获取文档数据
        if(isBlank(request.getDocumentFile()) && isBlank(request.getDocumentNetworkURL())){
            return Result.error(APIResultEnum.BUSINESS_ERROR,request.getUniqueCode(), BusinessErrorEnum.FILE_BASE64_URL_BLANK.getName());
        }
        if(isBlank(request.getSignatureFile()) && isBlank(request.getSignatureNetworkURL())){
            return Result.error(APIResultEnum.BUSINESS_ERROR, request.getUniqueCode(), BusinessErrorEnum.SIGNATURE_BASE64_URL_BLANK.getName());
        }
        if(isBlank(request.getPfx()) && isBlank(request.getPfxNetworkURL())){
            return Result.error(APIResultEnum.BUSINESS_ERROR, request.getUniqueCode(), BusinessErrorEnum.CERT_BASE64_URL_BLANK.getName());
        }
        byte[] docFileByte = null ;
        String docParam = null ;
        if(isNotBlank(request.getDocumentNetworkURL())){
            docParam = "documentNetworkURL" ;
            if(request.getDocumentNetworkURL().length() > 1000){
                return Result.error(APIResultEnum.PARAM_FORMAT,request.getUniqueCode(), docParam,ParamFormatErrorEnum.LENGTH_ERROR.getName());
            }
        }
        //获取签章数据
        byte[] sealByte = null;
        String sealParam = null ;
        if(isNotBlank(request.getSignatureNetworkURL())){
            sealParam = "signatureNetworkURL";
            if(request.getSignatureNetworkURL().length() > 1000){
                return Result.error(APIResultEnum.PARAM_FORMAT,request.getUniqueCode(), sealParam,ParamFormatErrorEnum.LENGTH_ERROR.getName());
            }
        }
        //获取证书数据
        byte[] certByte = null;
        String certParam = null ;
        if(isNotBlank(request.getPfxNetworkURL())){
            certParam = "pfxNetworkURL";
            if(request.getPfxNetworkURL().length() > 1000){
                return Result.error(APIResultEnum.PARAM_FORMAT,request.getUniqueCode(), certParam,ParamFormatErrorEnum.LENGTH_ERROR.getName());
            }
        }

        if(isNotBlank(request.getDocumentNetworkURL())){
            docParam = "documentNetworkURL" ;
            docFileByte = HttpUtils.download(request.getDocumentNetworkURL());
            if(docFileByte == null || docFileByte.length == 0){
                return Result.error(APIResultEnum.BUSINESS_ERROR,request.getUniqueCode(),docParam + BusinessErrorEnum.DOWNLOAD_FILE_ERROR.getName());
            }
        }else {
            docParam = "documentFile" ;
            try {
                String documentFile = request.getDocumentFile();
                if(documentFile.contains("base64,")){
                    String[] split = documentFile.split("base64,");
                    if(split.length == 2){
                        documentFile = split[1];
                    }else {
                        return Result.error(APIResultEnum.BUSINESS_ERROR,request.getUniqueCode() , docParam + BusinessErrorEnum.FILE_PARSE_BASE64_ERROR.getName());
                    }
                }
                docFileByte = Base64.decode(documentFile);
            }catch (Exception e){
                return Result.error(APIResultEnum.BUSINESS_ERROR,request.getUniqueCode() , docParam + BusinessErrorEnum.FILE_PARSE_BASE64_ERROR.getName());
            }
            if(docFileByte == null || docFileByte.length == 0){
                return Result.error(APIResultEnum.BUSINESS_ERROR,request.getUniqueCode() , docParam + BusinessErrorEnum.FILE_PARSE_BASE64_ERROR.getName());
            }
        }

        //校验参数
        if(request.getSignType().equals(SignTypeEnum.POSITION.getCode())){
            List<SignLocation> signLocationList = request.getSignLocationList();
            if(signLocationList == null || signLocationList.size() == 0){
                return Result.error(APIResultEnum.PARAM_MISSING,request.getUniqueCode(),  "signLocationList");
            }
            for(SignLocation signLocation : signLocationList){
                if(signLocation.getPage() == null){
                    return Result.error(APIResultEnum.PARAM_MISSING,request.getUniqueCode(),  "page");
                }
                if(signLocation.getPage() > 9999){
                    return Result.error(APIResultEnum.PARAM_FORMAT,request.getUniqueCode(),  "page",ParamFormatErrorEnum.LENGTH_ERROR.getName());
                }

                if(signLocation.getOffsetY() == null){
                    return Result.error(APIResultEnum.PARAM_MISSING,request.getUniqueCode(),  "offsetY");
                }
                if(signLocation.getOffsetY().floatValue() > 9999){
                    return Result.error(APIResultEnum.PARAM_FORMAT,request.getUniqueCode(),  "offsetY",ParamFormatErrorEnum.LENGTH_ERROR.getName());
                }
                if(signLocation.getOffsetX() == null){
                    return Result.error(APIResultEnum.PARAM_MISSING,request.getUniqueCode(),  "offsetX");
                }
                if(signLocation.getOffsetX().floatValue() > 9999){
                    return Result.error(APIResultEnum.PARAM_FORMAT,request.getUniqueCode(),  "offsetX",ParamFormatErrorEnum.LENGTH_ERROR.getName());
                }
                if(signLocation.getSignWidth() == null){
                    return Result.error(APIResultEnum.PARAM_MISSING,request.getUniqueCode(),  "signWidth");
                }
                if(signLocation.getSignWidth().floatValue() > 9999){
                    return Result.error(APIResultEnum.PARAM_FORMAT,request.getUniqueCode(),  "signWidth",ParamFormatErrorEnum.LENGTH_ERROR.getName());
                }
                if(signLocation.getSignHeight() == null){
                    return Result.error(APIResultEnum.PARAM_MISSING,request.getUniqueCode(),  "signHeight");
                }
                if(signLocation.getSignHeight().floatValue() > 9999){
                    return Result.error(APIResultEnum.PARAM_FORMAT,request.getUniqueCode(),  "signHeight",ParamFormatErrorEnum.LENGTH_ERROR.getName());
                }
                if(signLocation.getPageWidth() == null){
                    return Result.error(APIResultEnum.PARAM_MISSING,request.getUniqueCode(),  "pageWidth");
                }
                if(signLocation.getPageWidth().floatValue() > 9999){
                    return Result.error(APIResultEnum.PARAM_FORMAT,request.getUniqueCode(),  "pageWidth",ParamFormatErrorEnum.LENGTH_ERROR.getName());
                }
                if(signLocation.getPageHeight() == null){
                    return Result.error(APIResultEnum.PARAM_MISSING,request.getUniqueCode(),  "pageHeight");
                }
                if(signLocation.getPageHeight().floatValue() > 9999){
                    return Result.error(APIResultEnum.PARAM_FORMAT,request.getUniqueCode(),  "pageHeight",ParamFormatErrorEnum.LENGTH_ERROR.getName());
                }
                if(signLocation.getSignWidth().floatValue() > signLocation.getPageWidth().floatValue()){
                    return Result.error(APIResultEnum.BUSINESS_ERROR,request.getUniqueCode(),  BusinessErrorEnum.SIGN_WIDTH_GT_PAGE_WIDTH_ERROR.getName());
                }
                if(signLocation.getSignHeight().floatValue() > signLocation.getPageHeight().floatValue()){
                    return Result.error(APIResultEnum.BUSINESS_ERROR,request.getUniqueCode(),  BusinessErrorEnum.SIGN_HEIGHT_GT_PAGE_HEIGHT_ERROR.getName());
                }
                if(signLocation.getOffsetX().floatValue() > signLocation.getPageWidth().floatValue()){
                    return Result.error(APIResultEnum.BUSINESS_ERROR,request.getUniqueCode(),  BusinessErrorEnum.OFFSET_X_GT_PAGE_WIDTH_ERROR.getName());
                }
                if(signLocation.getOffsetY().floatValue() > signLocation.getPageHeight().floatValue()){
                    return Result.error(APIResultEnum.BUSINESS_ERROR,request.getUniqueCode(),  BusinessErrorEnum.OFFSET_Y_GT_PAGE_HEIGHT_ERROR.getName());
                }
            }
            try {
                PDDocument document = Loader.loadPDF(docFileByte);
                int numberOfPages = document.getNumberOfPages();
                if(numberOfPages == 0){
                    return Result.error(APIResultEnum.BUSINESS_ERROR,request.getUniqueCode(), docParam + BusinessErrorEnum.FILE_PARSE_BASE64_ERROR.getName());
                }
                for(SignLocation signLocation : signLocationList){
                    if(signLocation.getPage() == 0){
                        return Result.error(APIResultEnum.BUSINESS_ERROR,request.getUniqueCode(), BusinessErrorEnum.PAGE_NUMBER_ERROR.getName());
                    }
                    if(signLocation.getPage() > numberOfPages){
                        return Result.error(APIResultEnum.BUSINESS_ERROR,request.getUniqueCode(),  BusinessErrorEnum.PAGE_NUMBER_ERROR.getName());
                    }
                }
            }catch (Exception e){
                return Result.error(APIResultEnum.BUSINESS_ERROR,request.getUniqueCode(), docParam + BusinessErrorEnum.FILE_PARSE_BASE64_ERROR.getName());
            }
        }else if(request.getSignType().equals(SignTypeEnum.KEYWORDS.getCode())){
            //不能为null
            if(request.getKeywords() == null){
                return Result.error(APIResultEnum.PARAM_MISSING,request.getUniqueCode(),"keywords");
            }
            //不能空字符串
            if(request.getKeywords().length() == 0){
                return Result.error(APIResultEnum.PARAM_BLANK,request.getUniqueCode(),"keywords");
            }
            //不能超过50位长度
            if(request.getKeywords().length() > 50){
                return Result.error(APIResultEnum.PARAM_FORMAT,request.getUniqueCode(),"keywords",ParamFormatErrorEnum.LENGTH_ERROR.getName());
            }
            //不能包含空格
            if(request.getKeywords().contains(" ")){
                return Result.error(APIResultEnum.PARAM_FORMAT,request.getUniqueCode(),"keywords",ParamFormatErrorEnum.HAVE_BLANK_ERROR.getName());
            }

        }


        //获取签章数据
        if(isNotBlank(request.getSignatureNetworkURL())){
            sealParam = "signatureNetworkURL";
            sealByte = HttpUtils.download(request.getSignatureNetworkURL());
            if(sealByte == null || sealByte.length == 0){
                return Result.error(APIResultEnum.BUSINESS_ERROR,request.getUniqueCode() , sealParam + BusinessErrorEnum.DOWNLOAD_FILE_ERROR.getName());
            }
        }else {
            sealParam = "signatureFile";
            try {
                String signatureFile = request.getSignatureFile();
                if(signatureFile.contains("base64,")){
                    String[] split = signatureFile.split("base64,");
                    if(split.length == 2){
                        signatureFile = split[1];
                    }else {
                        return Result.error(APIResultEnum.BUSINESS_ERROR,request.getUniqueCode() , sealParam + BusinessErrorEnum.FILE_PARSE_BASE64_ERROR.getName());
                    }
                }
                sealByte = Base64.decode(signatureFile);

            }catch (Exception e){
                return Result.error(APIResultEnum.BUSINESS_ERROR,request.getUniqueCode() , sealParam + BusinessErrorEnum.FILE_PARSE_BASE64_ERROR.getName());
            }
            if(sealByte == null || sealByte.length == 0){
                return Result.error(APIResultEnum.BUSINESS_ERROR,request.getUniqueCode() , sealParam + BusinessErrorEnum.FILE_PARSE_BASE64_ERROR.getName());
            }
        }
        BufferedImage read = null;
        try {
            read = ImageIO.read(new ByteArrayInputStream(sealByte));
        } catch (IOException e) {
            return Result.error(APIResultEnum.BUSINESS_ERROR,request.getUniqueCode() , sealParam + BusinessErrorEnum.FILE_PARSE_BASE64_ERROR.getName());
        }
        if(read == null){
            return Result.error(APIResultEnum.BUSINESS_ERROR,request.getUniqueCode() , sealParam + BusinessErrorEnum.FILE_PARSE_BASE64_ERROR.getName());
        }

        //获取证书数据
        if(isNotBlank(request.getPfxNetworkURL())){
            certParam = "pfxNetworkURL";
            certByte = HttpUtils.download(request.getPfxNetworkURL());
            if(certByte == null || certByte.length == 0){
                return Result.error(APIResultEnum.BUSINESS_ERROR,request.getUniqueCode(), certParam + BusinessErrorEnum.DOWNLOAD_FILE_ERROR.getName());
            }
        }else {
            certParam = "pfx";
            try {
                String pfx = request.getPfx();
                if(pfx.contains("base64,")){
                    String[] split = pfx.split("base64,");
                    if(split.length == 2){
                        pfx = split[1];
                    }else {
                        return Result.error(APIResultEnum.BUSINESS_ERROR,request.getUniqueCode(), certParam + BusinessErrorEnum.FILE_PARSE_BASE64_ERROR.getName());
                    }
                }
                certByte = Base64.decode(pfx);
            }catch (Exception e){
                return Result.error(APIResultEnum.BUSINESS_ERROR,request.getUniqueCode(), certParam + BusinessErrorEnum.FILE_PARSE_BASE64_ERROR.getName());
            }
            if(certByte == null || certByte.length == 0){
                return Result.error(APIResultEnum.BUSINESS_ERROR,request.getUniqueCode(), certParam + BusinessErrorEnum.FILE_PARSE_BASE64_ERROR.getName());
            }
        }
        //验证证书
        KeyStore ks = null ;
        ByteArrayInputStream fis = new ByteArrayInputStream(certByte);
        try {
            ks = KeyStore.getInstance("PKCS12");
            ks.load(fis, request.getCertPassword().toCharArray());
            fis.close();
            Enumeration enumas = ks.aliases();
            String keyAlias = null;
            if (enumas.hasMoreElements()){
                keyAlias = (String)enumas.nextElement();
            }
            Certificate cert = ks.getCertificate(keyAlias);

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            ByteArrayInputStream inputStream = new ByteArrayInputStream(cert.getEncoded());
            X509Certificate instance = (X509Certificate) cf.generateCertificate(inputStream);

            Date notAfter = instance.getNotAfter();
            //证书过期
            if(notAfter.before(new Date())){
                return Result.error(APIResultEnum.BUSINESS_ERROR,BusinessErrorEnum.CERT_INVALID_ERROR);
            }
        } catch (Exception e){
            //证书解析异常
            return Result.error(APIResultEnum.BUSINESS_ERROR,BusinessErrorEnum.CERT_PARSE_ERROR);
        }
        //证书
        CertificateInfo certificateInfo = new CertificateInfo();
        certificateInfo.setCert(certByte);
        certificateInfo.setPassword(request.getCertPassword());
        certificateInfo.setCertType(CertificateInfo.CertTypeEnum.PKCS12);

        //计算签署位置
        Integer signType = request.getSignType();
        List<RealPositionProperty> realPositionPropertyList = null ;
        if(signType.equals(SignTypeEnum.KEYWORDS.getCode())){
            Integer width,height;
            if(request.getKeywordSealWidth() != null){
                //关键字签署签章固定宽度  高度根据图片宽高比自适应
                width = request.getKeywordSealWidth();
                int imageH = read.getHeight();
                int imageW = read.getWidth();
                BigDecimal imageHDecimal = new BigDecimal(imageH);
                BigDecimal imageWDecimal = new BigDecimal(imageW);
                BigDecimal widthDecimal = new BigDecimal(width);

                BigDecimal divide = imageHDecimal.divide(imageWDecimal, 4, RoundingMode.DOWN);
                BigDecimal multiply = divide.multiply(widthDecimal);
                height = multiply.intValue() ;
            }else if(request.getKeywordSealHeight() != null){
                // 关键字签署签章固定高度  宽度根据图片宽高比自适应
                height = request.getKeywordSealHeight();
                int imageH = read.getHeight();
                int imageW = read.getWidth();
                BigDecimal imageHDecimal = new BigDecimal(imageH);
                BigDecimal imageWDecimal = new BigDecimal(imageW);

                BigDecimal heightDecimal = new BigDecimal(height);

                BigDecimal divide = imageHDecimal.divide(imageWDecimal, 4, RoundingMode.DOWN);
                BigDecimal multiply = heightDecimal.divide(divide,4, RoundingMode.DOWN);
                width = multiply.intValue() ;
            }else{
                width = 232;
                int imageH = read.getHeight();
                int imageW = read.getWidth();
                BigDecimal imageHDecimal = new BigDecimal(imageH);
                BigDecimal imageWDecimal = new BigDecimal(imageW);
                BigDecimal widthDecimal = new BigDecimal(width);
                BigDecimal divide = imageHDecimal.divide(imageWDecimal, 4, RoundingMode.DOWN);
                BigDecimal multiply = divide.multiply(widthDecimal);
                height = multiply.intValue() ;
            }


            realPositionPropertyList = calculatePositionService.getAllPositionByKeyWords(docFileByte, request.getKeywords(), width, height);
            if(realPositionPropertyList == null || realPositionPropertyList.size() == 0){
                return Result.error(APIResultEnum.BUSINESS_ERROR,request.getUniqueCode(),  BusinessErrorEnum.KEYWORDS_NO_EXISTS_ERROR.getName());
            }
        }else if(signType.equals(SignTypeEnum.POSITION.getCode())){
            List<SourcePositionProperty> sourcePositionPropertyList = new ArrayList<>();
            for(SignLocation signLocation : request.getSignLocationList()){
                SourcePositionProperty sourcePositionProperty = new SourcePositionProperty();
                sourcePositionProperty.setOffsetX(signLocation.getOffsetX().floatValue());
                sourcePositionProperty.setOffsetY(signLocation.getOffsetY().floatValue());
                sourcePositionProperty.setPage(signLocation.getPage());
                sourcePositionProperty.setPageHeight(signLocation.getPageHeight().floatValue());
                sourcePositionProperty.setPageWidth(signLocation.getPageWidth().floatValue());
                sourcePositionProperty.setHeight(signLocation.getSignHeight().floatValue());
                sourcePositionProperty.setWidth(signLocation.getSignWidth().floatValue());
                sourcePositionPropertyList.add(sourcePositionProperty);
            }
            realPositionPropertyList = calculatePositionService.calculatePositions(sourcePositionPropertyList, docFileByte);
        }
        if(realPositionPropertyList == null || realPositionPropertyList.size() == 0){
            return Result.error(APIResultEnum.BUSINESS_ERROR,request.getUniqueCode(), BusinessErrorEnum.SIGN_POSITION_CALCULATE_ERROR.getName());
        }
        //开始签署
        byte [] outPdf = docFileByte ;
        for(RealPositionProperty realPositionProperty : realPositionPropertyList){
            //签署所需基础数据
            AssinaturaModel assinatura = new AssinaturaModel();
            assinatura.setLocation("location");
            assinatura.setReason("防伪造防篡改数字校验");
            //文件
            assinatura.setPdf(outPdf);
            //签章
            assinatura.setSignatureImage(sealByte);
            //证书
            assinatura.setCertInfo(certificateInfo);
            //签署位置
            AssinaturaPosition position = new AssinaturaPosition();
            position.setPage(realPositionProperty.getPageNum() - 1);
            //横坐标
            position.setOffsetX(realPositionProperty.getStartx() + "");
            position.setSignWidth((realPositionProperty.getEndx() - realPositionProperty.getStartx()) + "");
            //纵坐标，pdfbox是从下向上计算的

            if(signType.equals(SignTypeEnum.POSITION.getCode())){

                float signHeight = realPositionProperty.getStarty() - realPositionProperty.getEndy();
                if(signHeight < 0){
                    signHeight = realPositionProperty.getEndy() - realPositionProperty.getStarty() ;
                }
                position.setSignHeight(signHeight + "");
                position.setOffsetY((realPositionProperty.getRealPdfHeight() - realPositionProperty.getStarty() - signHeight) + "");
            }else {
                position.setOffsetY((realPositionProperty.getRealPdfHeight() - realPositionProperty.getStarty()) + "");
                position.setSignHeight(Math.abs(realPositionProperty.getStarty() - realPositionProperty.getEndy()) + "");
            }

            assinatura.setPosition(position);
            assinatura.setSignatureKey(UUID.randomUUID().toString());
            try {
                AssinaturaPDF assinaturaPDF = new AssinaturaPDF(assinatura);
                outPdf = assinaturaPDF.assina();
            } catch (Exception e) {
                e.printStackTrace();
                return Result.error(APIResultEnum.BUSINESS_ERROR,request.getUniqueCode(), BusinessErrorEnum.SIGN_FAILED_ERROR.getName());
            }
        }
        if(outPdf == null){
            return Result.error(APIResultEnum.BUSINESS_ERROR,request.getUniqueCode(), BusinessErrorEnum.SIGN_FAILED_ERROR.getName());
        }
        //返回
        DocumentSignResponse response = new DocumentSignResponse();
        response.setDocumentFile("data:application/pdf;base64," + Base64.encode(outPdf));

        return Result.OK(response,request.getUniqueCode()) ;

    }




    public Boolean isBlank(String value){
        if(value == null || value.length() == 0){
            return true ;
        }
        return false ;

    }

    public Boolean isBlank(Collection collection){
        if(collection == null || collection.size() == 0){
            return true ;
        }
        return false ;
    }

    public Boolean isNotBlank(String value){
        if(value == null || value.length() == 0){
            return false ;
        }
        return true ;

    }

    public Boolean isNotBlank(Collection collection){
        if(collection == null || collection.size() == 0){
            return false ;
        }
        return true ;
    }

    public static String header(byte[] file){
        StringBuilder header = new StringBuilder();
        for (int i = 0; i < Math.min(file.length, 8); i++) {
            header.append(String.format("%02X", file[i])); // 转换为十六进制字符串
        }
        return header.toString() ;
    }
}