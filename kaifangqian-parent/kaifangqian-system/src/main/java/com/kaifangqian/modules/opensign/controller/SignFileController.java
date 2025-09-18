/**
 * @description 签署文件处理相关接口
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

import com.kaifangqian.modules.storage.StorageService;
import com.kaifangqian.annotation.ResrunLogModule;
import com.kaifangqian.common.aspect.LicenseBean;
import com.kaifangqian.common.system.vo.Authorization;
import com.kaifangqian.common.util.FileType;
import com.kaifangqian.common.util.FileTypeJudge;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.modules.opensign.entity.AnnexImage;
import com.kaifangqian.modules.opensign.service.annex.AnnexImageService;
import com.kaifangqian.modules.opensign.service.business.ConvertImageService;
import com.kaifangqian.modules.opensign.service.business.PdfConvertService;
import com.kaifangqian.modules.opensign.service.business.PdfEncryptionService;
import com.kaifangqian.modules.opensign.service.template.SignTemplateRecordService;
import com.kaifangqian.modules.opensign.service.tool.PdfWaterMarkService;
import com.kaifangqian.modules.opensign.service.tool.SignFileService;
import com.kaifangqian.modules.opensign.vo.base.sign.DocImageVo;
import com.kaifangqian.modules.storage.entity.AnnexStorage;
import com.kaifangqian.modules.storage.service.IAnnexStorageService;
import com.kaifangqian.utils.IOUtils;
import com.kaifangqian.utils.MyFileUtil;
import com.kaifangqian.config.limit.annotation.Limit;
import com.kaifangqian.config.limit.annotation.LimitHandleType;
import com.kaifangqian.config.limit.annotation.LimitType;
import com.kaifangqian.config.limit.annotation.OperateType;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Description: SignFileController
 * @Package: com.kaifangqian.modules.opensign.controller
 * @ClassName: SignFileController
 * @author: FengLai_Gong
 * @Date： 2023/10/10
 */
@Slf4j
@RestController
@RequestMapping("/sign/file")
@ResrunLogModule(name = "签署文件处理相关接口")
// @Api(tags = "签署文件处理相关接口")
public class SignFileController {

    @Autowired
    private SignTemplateRecordService templateRecordService ;
    @Autowired
    private SignFileService signFileService ;
    @Autowired
    private IAnnexStorageService iAnnexStorageService;
    @Autowired
    private AnnexImageService annexImageService ;
    @Autowired
    private StorageService storageService;
    @Autowired
    private ConvertImageService convertImageService ;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private LicenseBean licenseBean ;
    @Autowired
    private PdfWaterMarkService pdfWaterMarkService ;

    @Autowired
    private PdfEncryptionService pdfEncryptionService ;
    @Autowired
    private PdfConvertService pdfConvertService ;

    // @ApiOperation("签署文件-是否配置office转换pdf")
    @RequestMapping(value = "/convertFlag",method = RequestMethod.GET)
    public Result<?> convertFlag() {
        return Result.OK(pdfConvertService.isConvert());
    }



    // @ApiOperation("签署文件-文件上传，返回真实文件id")
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @Limit(name = "签署文件上传", prefix = "limit",limitType= LimitType.TOKEN, operateType = OperateType.ALL, count = 5,period=30,limitHandle = LimitHandleType.LOGOUT)
    public Result<?> create(AnnexStorage annexStorage, @RequestParam("file") MultipartFile file) {


        String suffix = MyFileUtil.getExtensionName(file.getOriginalFilename());
        String fileNameNoExt = MyFileUtil.getFileNameNoEx(file.getOriginalFilename());
//        String type = MyFileUtil.getFileType(suffix);
        String path = UUID.randomUUID().toString() + ".pdf";
        try {
            byte[] fileByte = IOUtils.stream2Byte(file.getInputStream());

            //校验文件类型
            FileType fileType = null;
            try {
                fileType = FileTypeJudge.getType(fileByte);
            }catch (Exception e){
                e.printStackTrace();
            }
            if(fileType == null){
                return Result.error("不支持的文件类型");
            }
            //只能是pdf、excel、word、
            if(!fileType.getValue().equals(FileType.PDF.getValue()) && !fileType.getValue().equals(FileType.OFFICE_OLD.getValue()) && !fileType.getValue().equals(FileType.OFFICE_NEW.getValue())){
                return Result.error("不支持的文件类型");
            }
            byte[] pdfByte = null ;
            //如果是excel、word，查看是否配置的转换
            if(fileType.getValue().equals(FileType.OFFICE_OLD.getValue()) || fileType.getValue().equals(FileType.OFFICE_NEW.getValue())){
                //判读是否开始office转换
                Boolean convert = pdfConvertService.isConvert();
                if(!convert){
                    return Result.error("不支持的文件类型");
                }
                //开始转换
                if(suffix.equals("doc") || suffix.equals("docx")){
                    //word转pdf
                    pdfByte = pdfConvertService.wordConvertPdf(fileByte);
                }else if(suffix.equals("xls") || suffix.equals("xlsx")){
                    //excel转pdf
                    pdfByte = pdfConvertService.excelConvertPdf(fileByte);
                }
                if(pdfByte == null){
                    Result.error("文件转换失败");
                }
            }else {
                pdfByte = fileByte ;
            }
            if(pdfByte == null){
                Result.error("文件不存在或损坏");
            }
            //开始处理业务
            Authorization grant = licenseBean.getGrant();
            if(grant != null){
                String environment = grant.getEnvironment();
                if(environment != null && environment.equals("test")){
                    pdfByte = pdfWaterMarkService.addTextWatermark(pdfByte);
                }
            }
//            byte[] originPdfByte = pdfByte ;
//            //文件加密
//            pdfByte = pdfEncryptionService.pdfToEncrypted(pdfByte);
//            if(pdfByte == null){
//                Result.error("文件加密失败");
//            }

            byte[] bytes = pdfByte ;
            ByteArrayInputStream pdfFileByte = new ByteArrayInputStream(bytes);
            //1、上传真实PDF文件到阿里云
            long step01 = System.currentTimeMillis();
            boolean result = storageService.store(pdfFileByte, bytes.length, file.getContentType(), path);
            long step02 = System.currentTimeMillis();

            if (!result) {
                return Result.error("文件上传失败");
            }
//            String filenName = annexStorage.getName();
//            filenName = MyStringUtils.isBlank(filenName) ? MyFileUtil.getFileNameNoEx(file.getOriginalFilename()) : filenName;
            annexStorage.setName(fileNameNoExt);
            annexStorage.setPath(path);
            annexStorage.setRealName(fileNameNoExt + ".pdf");
            annexStorage.setSize(MyFileUtil.getSize(bytes.length));
//            annexStorage.setSize(MyFileUtil.getSize(file.getSize()));
            annexStorage.setSuffix("pdf");
            annexStorage.setType("pdf");
            annexStorage.setStatus(1);
            annexStorage.setDeleteFlag(false);
            //2、存储真实PDF文件数据到数据库
            iAnnexStorageService.save(annexStorage);
            long step03 = System.currentTimeMillis();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    signFileService.saveAndConvertImage(annexStorage.getId(),bytes);
//                    //3、PDF文件转换图片
//                    List<ImageConvertVo> imageConvertVos = convertImageService.convertImageAsync(bytes);
//                    long step04 = System.currentTimeMillis();
//
//                    if(imageConvertVos == null || imageConvertVos.size() == 0){
//                        return;
//                    }
//                    System.out.println("上传文件=================================" + (step02 - step01) + "毫秒");
//                    System.out.println("存储文件=================================" + (step03 - step02) + "毫秒");
//                    System.out.println("转图片=================================" + (step04 - step03) + "毫秒");
//                    for(ImageConvertVo imageConvertVo : imageConvertVos){
//                        threadPoolTaskExecutor.execute(new Runnable() {
//                            @Override
//                            public void run() {
//                                //4、上传图片数据到阿里云
//                                long imageStep01 = System.currentTimeMillis();
//                                String annexId = signFileService.saveAnnexStorage(imageConvertVo.getImageByte(), SignFileEnum.SIGN_FILE_IMAGE, null);
//                                long imageStep02 = System.currentTimeMillis();
//
//                                //5、存储图片文件数据到数据库
//                                if(annexId != null){
//                                    AnnexImage annexImage = new AnnexImage();
//                                    annexImage.setAnnexId(annexStorage.getId());
//                                    annexImage.setPage(imageConvertVo.getPage());
//                                    annexImage.setImageAnnexId(annexId);
//                                    annexImage.setImageWidth(imageConvertVo.getImageWidth());
//                                    annexImage.setImageHeight(imageConvertVo.getImageHeight());
//                                    annexImage.setDeleteFlag(false);
//                                    annexImageService.save(annexImage);
//                                }
//                                long imageStep03 = System.currentTimeMillis();
//                                System.out.println("上传图片，第" + imageConvertVo.getPage() + "页=================================" + (imageStep02 - imageStep01) + "毫秒");
//                                System.out.println("存储图片，第" + imageConvertVo.getPage() + "页=================================" + (imageStep03 - imageStep02) + "毫秒");
//                            }
//                        });
//                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("文件上传失败");
        }


        return Result.OK("success", annexStorage.getId());
    }


    // @ApiOperation("签署文件-图片列表，根据真实文件id获取")
    @RequestMapping(value = "/list/image/annex",method = RequestMethod.GET)
    public Result<List<DocImageVo>> listImageFile(@RequestParam("annexId") String annexId){
        List<DocImageVo> voList = new ArrayList<>();
        List<AnnexImage> annexImageList = annexImageService.listByAnnexId(annexId);
        if(annexImageList != null && annexImageList.size() > 0){
            for(AnnexImage annexImage : annexImageList){
                DocImageVo vo = new DocImageVo();
                vo.setId(annexImage.getId());
                vo.setPage(annexImage.getPage());
                vo.setAnnexId(annexImage.getImageAnnexId());
                vo.setImageWidth(annexImage.getImageWidth());
                vo.setImageHeight(annexImage.getImageHeight());
                //设置默认值
                if(vo.getImageWidth() == null){
                    vo.setImageWidth("595.3");
                }
                if(vo.getImageHeight() == null){
                    vo.setImageHeight("841.9");
                }
                voList.add(vo);
            }
        }
        return Result.OK(voList) ;

//        List<DocImageVo> voList = signFileService.saveAndConvertImage(annexId, null);
//        if(voList == null || voList.size() == 0){
//            //临时修改业务，后期正常维护，不能使用这种方式处理
//            SignTemplateRecord current = templateRecordService.getCurrent(annexId);
//            if(current != null && current.getAnnexId() != null){
//                annexId = current.getAnnexId() ;
//            }
//            voList = signFileService.saveAndConvertImage(annexId, null);
//        }
//        return Result.OK(voList) ;
    }











}