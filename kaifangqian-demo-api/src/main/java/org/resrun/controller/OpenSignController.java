package org.resrun.controller;

import org.resrun.controller.vo.base.Result;
import org.resrun.controller.vo.request.*;
import org.resrun.controller.vo.response.SealResponse;
import org.resrun.controller.vo.response.SignResponse;
import org.resrun.controller.vo.response.VerifyResponse;
import org.resrun.enums.SignTypeEnum;
import org.resrun.sdk.enums.APIResultEnum;
import org.resrun.sdk.enums.SDKSignTypeEnum;
import org.resrun.sdk.service.CalculatePositionService;
import org.resrun.sdk.service.EntSealGenerateService;
import org.resrun.sdk.service.SDKService;
import org.resrun.sdk.vo.request.CertEventRequest;
import org.resrun.sdk.vo.response.CertEventResponse;
import org.resrun.service.cert.CertService;
import org.resrun.service.image.EntSealClipService;
import org.resrun.sdk.service.pojo.RealPositionProperty;
import org.resrun.sdk.service.pojo.SourcePositionProperty;
import org.resrun.service.verify.SignVerifyService;
import org.resrun.sdk.utils.Base64;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.resrun.sdk.vo.base.SignLocation;
import org.resrun.sdk.vo.request.DocumentSignRequest;
import org.resrun.sdk.vo.response.DocumentSignResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Description: OpenSignController
 * @Package: org.resrun.controller
 * @ClassName: OpenSignController
 * @copyright 北京资源律动科技有限公司
 */
@Api(tags = "开放签-演示demo")
@RestController
public class OpenSignController {


    @Autowired
    private SignVerifyService signVerifyService ;
    @Autowired
    private CalculatePositionService calculatePositionService ;
//    @Autowired
//    private SignService signService ;

    @Autowired
    private SDKService sdkService;

    @Autowired
    private EntSealGenerateService entSealGenerateService ;
    @Autowired
    private EntSealClipService entSealClipService ;
    @Autowired
    private CertService certService ;

    @ApiOperation("生成企业签章-上传生成")
    @RequestMapping(value = "/clip/seal",method = RequestMethod.POST)
    public Result<SealResponse> generateUpload(@RequestBody ClipSealRequest request){

        if(request.getImage() == null || request.getImage().length() == 0){
            return Result.error("图片数据为空",null);
        }
        byte[] decode = Base64.decode(request.getImage());
        if(decode == null || decode.length == 0){
            return Result.error("签章制作失败",null);
        }

        byte[] entSealByte = entSealClipService.clip(decode, request.getColorRange());
        if(entSealByte == null){
            return Result.error("签章制作失败",null);
        }
        String encode = Base64.encode(entSealByte);
        SealResponse response = new SealResponse();
        response.setEntSeal(encode);
        return Result.OK(response) ;

    }

    @ApiOperation("生成企业签章-参数生成")
    @RequestMapping(value = "/generate/seal",method = RequestMethod.POST)
    public Result<SealResponse> seal(@RequestBody GenerateSealRequest request){


        if(request == null || request.getMiddleText() == null || request.getTopText() == null){
            return Result.error("参数缺失",null) ;
        }
        byte[] entSealByte = entSealGenerateService.generateEntSeal(request.getTopText(), request.getMiddleText());
        if(entSealByte == null){
            return Result.error("签章制作失败",null);
        }
        String encode = Base64.encode(entSealByte);
        SealResponse response = new SealResponse();
        response.setEntSeal(encode);
        return Result.OK(response) ;

    }

    @ApiOperation("签署")
    @RequestMapping(value = "/sign",method = RequestMethod.POST)
    public Result<SignResponse> sign(@RequestBody SignRequest request){

        String fileName = "开源工具版说明.pdf" ;
        byte[] signFileBytes = null ;
        byte[] entSealBytes = null ;
        byte[] personalBytes = null ;
        org.resrun.sdk.vo.base.Result<CertEventResponse> entCert = null ;
        org.resrun.sdk.vo.base.Result<CertEventResponse> personalCert = null ;
        List<RealPositionProperty> entPositionList = null;
        List<RealPositionProperty> personalPositionList = null;
        int entSealWidth = 200 ;
        int entSealHeight = 200 ;
        int personalSealWidth = 150 ;
        int personalSealHeight = 70 ;
        //获取本地签署文件
        try {
            signFileBytes = getResourceFiles(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(signFileBytes == null){
            return Result.error("签署失败",null);
        }
        //生成企业证书和个人证书
        try {
            if(request.getEntName() != null && request.getEntName().length() > 0){
                CertEventRequest certEventRequest = new CertEventRequest();
                certEventRequest.setCertSubject("开放签@" + request.getEntName());
                certEventRequest.setCertPassword("123456");
                certEventRequest.setUniqueCode(UUID.randomUUID().toString());
                entCert =  sdkService.certEvent(certEventRequest);
                if(entCert == null || !entCert.getCode().equals(APIResultEnum.SUCCESS.getCode())){
                    return Result.error(entCert.getMessage(),null);
                }
            }
            if(request.getPersonalName() != null && request.getPersonalName().length() > 0){
                CertEventRequest certEventRequest = new CertEventRequest();
                certEventRequest.setCertSubject("开放签@" + request.getPersonalName());
                certEventRequest.setCertPassword("123456");
                certEventRequest.setUniqueCode(UUID.randomUUID().toString());
                personalCert =  sdkService.certEvent(certEventRequest);
                if(personalCert == null || !personalCert.getCode().equals(APIResultEnum.SUCCESS.getCode())){
                    return Result.error(personalCert.getMessage(),null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //生成企业签章和个人签章
        if(request.getEntSeal() != null){
            entSealBytes = Base64.decode(request.getEntSeal());
        }
        if(request.getPersonalSeal() != null){
            personalBytes = Base64.decode(request.getPersonalSeal());
        }
        //进行签署操作
        byte[] operationByte = signFileBytes ;

        //计算企业签署位置和个人签署位置
        if(SignTypeEnum.POSITION.getCode().equals(request.getSignType())){
            DocumentSignRequest signRequest = new DocumentSignRequest();
            signRequest.setUniqueCode(UUID.randomUUID().toString());
            signRequest.setSignType(SDKSignTypeEnum.POSITION.getCode());
            if((request.getEntPositionList() == null || request.getEntPositionList().size() == 0 ) &&
                    (request.getPersonalPositionList() == null || request.getPersonalPositionList().size() == 0)){
                return Result.error("签署失败",null);
            }
            //计算企业签署位置
            if(request.getEntPositionList() != null && request.getEntPositionList().size() > 0){
                signRequest.setCertPassword(entCert.getData().getCertPassword());
                signRequest.setPfx(entCert.getData().getPfx());
                signRequest.setSignatureFile(Base64.encode(entSealBytes));
                signRequest.setDocumentFile(Base64.encode(operationByte));

                List<SignLocation> signLocations = new ArrayList<>();
                for(PositionRequest positionRequest : request.getEntPositionList()){
                    SignLocation signLocation = new SignLocation();
                    signLocation.setOffsetX(Float.valueOf(positionRequest.getOffsetX()));
                    signLocation.setOffsetY(Float.valueOf(positionRequest.getOffsetY()));
                    signLocation.setPage(positionRequest.getPage());
                    signLocation.setPageHeight(Float.valueOf(positionRequest.getPageHeight()));
                    signLocation.setPageWidth(Float.valueOf(positionRequest.getPageWidth()));
                    signLocation.setSignHeight(Float.valueOf(positionRequest.getHeight()));
                    signLocation.setSignWidth(Float.valueOf(positionRequest.getWidth()));
                    signLocations.add(signLocation);
                }
                signRequest.setSignLocationList(signLocations);
                org.resrun.sdk.vo.base.Result<DocumentSignResponse> signResponse = sdkService.documentSign(signRequest);
                if(signResponse.getCode().equals(APIResultEnum.SUCCESS.getCode())){
                    String [] file = signResponse.getData().getDocumentFile().split(",");
                    operationByte = Base64.decode(file[1]);
                }else {
                    return Result.error("签署失败",null);
                }

            }
            //计算个人签署位置
            if(request.getPersonalPositionList() != null && request.getPersonalPositionList().size() > 0){

                signRequest.setCertPassword(personalCert.getData().getCertPassword());
                signRequest.setPfx(personalCert.getData().getPfx());
                signRequest.setSignatureFile(Base64.encode(personalBytes));
                signRequest.setDocumentFile(Base64.encode(operationByte));

                List<SignLocation> signLocations = new ArrayList<>();
                for(PositionRequest positionRequest : request.getPersonalPositionList()){
                    SignLocation signLocation = new SignLocation();
                    signLocation.setOffsetX(Float.valueOf(positionRequest.getOffsetX()));
                    signLocation.setOffsetY(Float.valueOf(positionRequest.getOffsetY()));
                    signLocation.setPage(positionRequest.getPage());
                    signLocation.setPageHeight(Float.valueOf(positionRequest.getPageHeight()));
                    signLocation.setPageWidth(Float.valueOf(positionRequest.getPageWidth()));
                    signLocation.setSignHeight(Float.valueOf(positionRequest.getHeight()));
                    signLocation.setSignWidth(Float.valueOf(positionRequest.getWidth()));
                    signLocations.add(signLocation);
                }
                signRequest.setSignLocationList(signLocations);
                org.resrun.sdk.vo.base.Result<DocumentSignResponse> signResponse = sdkService.documentSign(signRequest);
                if(signResponse.getCode().equals(APIResultEnum.SUCCESS.getCode())){
                    String [] file = signResponse.getData().getDocumentFile().split(",");
                    operationByte = Base64.decode(file[1]);
                }else {
                    return Result.error("签署失败",null);
                }
            }

        }else if(SignTypeEnum.KEYWORDS.getCode().equals(request.getSignType())){
            if((request.getEntKeyword() == null || request.getEntKeyword().length() == 0 ) &&
                    (request.getPersonalKeyword() == null || request.getPersonalKeyword().length() == 0)){
                return Result.error("签署失败",null);
            }
            DocumentSignRequest signRequest = new DocumentSignRequest();
            signRequest.setUniqueCode(UUID.randomUUID().toString());
            signRequest.setSignType(SDKSignTypeEnum.KEYWORDS.getCode());
            //根据关键字计算所有企业签署位置
            if(request.getEntKeyword() != null && request.getEntKeyword().length() > 0){
                entPositionList = calculatePositionService.getAllPositionByKeyWords(signFileBytes, request.getEntKeyword(), entSealWidth, entSealHeight);
                signRequest.setCertPassword(entCert.getData().getCertPassword());
                signRequest.setPfx(entCert.getData().getPfx());
                signRequest.setSignatureFile(Base64.encode(entSealBytes));
                signRequest.setDocumentFile(Base64.encode(operationByte));
                signRequest.setKeywords(request.getEntKeyword());
                org.resrun.sdk.vo.base.Result<DocumentSignResponse> signResponse = sdkService.documentSign(signRequest);
                if(signResponse.getCode().equals(APIResultEnum.SUCCESS.getCode())){
                    String [] file = signResponse.getData().getDocumentFile().split(",");
                    operationByte = Base64.decode(file[1]);
                }else {
                    return Result.error("签署失败",null);
                }
            }
            //根据关键字计算所有个人签署位置
            if(request.getPersonalKeyword() != null && request.getPersonalKeyword().length() > 0){
                personalPositionList = calculatePositionService.getAllPositionByKeyWords(signFileBytes,request.getPersonalKeyword(),personalSealWidth,personalSealHeight);
                signRequest.setCertPassword(personalCert.getData().getCertPassword());
                signRequest.setPfx(personalCert.getData().getPfx());
                signRequest.setSignatureFile(Base64.encode(personalBytes));
                signRequest.setDocumentFile(Base64.encode(operationByte));
                signRequest.setKeywords(request.getPersonalKeyword());
                org.resrun.sdk.vo.base.Result<DocumentSignResponse> signResponse = sdkService.documentSign(signRequest);
                if(signResponse.getCode().equals(APIResultEnum.SUCCESS.getCode())){
                    String [] file = signResponse.getData().getDocumentFile().split(",");
                    operationByte = Base64.decode(file[1]);
                }else {
                    return Result.error("签署失败",null);
                }
            }

            if((personalPositionList == null || personalPositionList.size() == 0 ) &&
                    (entPositionList == null || entPositionList.size() == 0)){
                return Result.error("签署失败！签署关键字在文件中不存在，请准确设置关键字后再签署",null);
            }
        }

        String encode = Base64.encode(operationByte);
        SignResponse response = new SignResponse();
        response.setSignFile(encode);
        return Result.OK(response);

    }

    @ApiOperation("文件验签")
    @RequestMapping(value = "/verify",method = RequestMethod.POST)
    public Result<VerifyResponse> verify(@RequestBody VerifyRequest request){

        String verifyFile = request.getVerifyFile();
        byte[] decode = Base64.decode(verifyFile);
        VerifyResponse verify = signVerifyService.verify(decode, request.getFileName());
        return Result.OK(verify) ;
    }


    public List<SourcePositionProperty> convert(List<PositionRequest> positionRequestList){
        List<SourcePositionProperty> list = new ArrayList<>();
        for(PositionRequest request : positionRequestList){
            SourcePositionProperty position = new SourcePositionProperty();
            position.setOffsetX(Float.valueOf(request.getOffsetX()));
            position.setOffsetY(Float.valueOf(request.getOffsetY()));
            position.setPage(request.getPage());
            position.setWidth(Float.valueOf(request.getWidth()));
            position.setHeight(Float.valueOf(request.getHeight()));
            position.setPageHeight(Float.valueOf(request.getPageHeight()));
            position.setPageWidth(Float.valueOf(request.getPageWidth()));
            list.add(position);
        }
        return list ;
    }


    public byte [] getResourceFiles(String path) {
        try {
            InputStream inputStream = ResourceUtils.class.getClassLoader()
                    .getResourceAsStream(path);
            return read(inputStream);
        }catch (Exception e){
            System.err.println(path);
            e.printStackTrace();
        }
        return null;
    }


    public byte[] read(InputStream inputStream) throws IOException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int num = inputStream.read(buffer);
            while (num != -1) {
                baos.write(buffer, 0, num);
                num = inputStream.read(buffer);
            }
            baos.flush();
            return baos.toByteArray();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

}