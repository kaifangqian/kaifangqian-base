/**
 * @description 电子印章所有文件存储相关业务
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
package com.kaifangqian.modules.opensign.service.tool;

import com.aliyun.oss.OSS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kaifangqian.modules.opensign.service.annex.AnnexImageService;
import com.kaifangqian.modules.opensign.service.business.ConvertImageService;
import com.kaifangqian.modules.storage.StorageService;
import com.kaifangqian.common.base.entity.BaseEntity;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.opensign.entity.AnnexImage;
import com.kaifangqian.modules.opensign.enums.SignFileEnum;
import com.kaifangqian.modules.opensign.vo.base.ImageConvertVo;
import com.kaifangqian.modules.opensign.vo.base.sign.DocImageVo;
import com.kaifangqian.modules.storage.entity.AnnexStorage;
import com.kaifangqian.modules.storage.service.IAnnexStorageService;
import com.kaifangqian.utils.MyFileUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 电子印章所有文件存储相关业务
 * @Package: com.kaifangqian.modules.sign.service.tool
 * @ClassName: SignFileService
 * @author: FengLai_Gong
 */
@Service
public class SignFileService {

    @Autowired
    private StorageService storageService;
    @Autowired
    private IAnnexStorageService iAnnexStorageService;
    @Autowired
    private AnnexImageService annexImageService ;
    @Autowired
    private ConvertImageService convertImageService ;

    @Autowired
    private RedissonClient redissonClient;

    public static final String CONVERT_IMAGE = "CONVERT_IMAGE::";



    public void delete(String annexId){
        iAnnexStorageService.deleteExt(annexId);
    }



    /**
     * @Description #更新文件
     * @Param [signFileEnum, fatherId, fileId]
     * @return java.lang.String
     **/
    public void deleteAnnexStorage(SignFileEnum signFileEnum, String fatherId){

        QueryWrapper<AnnexStorage> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AnnexStorage::getFatherId,fatherId);
        wrapper.lambda().eq(AnnexStorage::getDataCategory,signFileEnum.getDataCategory());
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);

        AnnexStorage annexStorage = new AnnexStorage();
        annexStorage.setDeleteFlag(true);

        iAnnexStorageService.update(annexStorage, wrapper);



    }



    /**
     * @Description #更新文件
     * @Param [signFileEnum, fatherId, fileId]
     * @return java.lang.String
     **/
    public String updateAnnexStorage(SignFileEnum signFileEnum, String fatherId, String fileId){
        AnnexStorage annexStorage = iAnnexStorageService.getById(fileId);
        if(annexStorage == null) {
            return null ;
        }
        //文件后缀
        annexStorage.setSuffix(signFileEnum.getSuffix());
        //文件类型
        annexStorage.setType(signFileEnum.getFileType());
        //文件种类
        annexStorage.setDataCategory(signFileEnum.getDataCategory());
        //关联父id
        annexStorage.setFatherId(fatherId) ;
        annexStorage.setStatus(1);
        annexStorage.setDeleteFlag(false);
        //插入文件数据
        boolean save = iAnnexStorageService.updateById(annexStorage);
        if(save){
            //返回id
            return annexStorage.getId();
        }else {
            return null ;
        }
    }

    public String  saveAnnexStorage(AnnexStorage annexStorage){

        //插入文件数据
        boolean save = iAnnexStorageService.save(annexStorage);
        if(save){
            //返回id
            return annexStorage.getId();
        }else {
            return null ;
        }
    }


    public String  copyAnnexStorage(String annexId){

        AnnexStorage sourceAnnex = getAnnexById(annexId);
        if(sourceAnnex == null){
            return null;
        }
        AnnexStorage targetAnnex = new AnnexStorage();
        BeanUtils.copyProperties(sourceAnnex,targetAnnex);
        targetAnnex.setFatherId(null);
        targetAnnex.setId(null);
        targetAnnex.setDeleteFlag(false);
        //插入文件数据
        boolean save = iAnnexStorageService.save(targetAnnex);
        if(save){
            //返回id
            return targetAnnex.getId();
        }else {
            return null ;
        }
    }

    public String  saveAnnexStorage(byte[] fileByte,SignFileEnum signFileEnum,String fatherId,String realName){
        //设定文件名称
        String path = UUID.randomUUID().toString() + "." + signFileEnum.getSuffix();
        path = storageService.buildPrefixPath(path);
        InputStream inputStream = new ByteArrayInputStream(fileByte);
        //存储文件
        boolean result = storageService.store(inputStream, fileByte.length, signFileEnum.getSuffix(), path);
        if (!result) {
            return null;
        }

        //创建附件数据对象
        AnnexStorage annexStorage = new AnnexStorage();
        //文件名
        annexStorage.setName(realName);
        annexStorage.setPath(path);
        annexStorage.setRealName(realName);
        annexStorage.setSize(MyFileUtil.getSize(fileByte.length));
        //文件后缀
        annexStorage.setSuffix(signFileEnum.getSuffix());
        //文件类型
        annexStorage.setType(signFileEnum.getFileType());
        //文件种类
        annexStorage.setDataCategory(signFileEnum.getDataCategory());
        //关联父id
        if(fatherId != null){
            annexStorage.setFatherId(fatherId) ;
        }

        annexStorage.setStatus(1);
        annexStorage.setDeleteFlag(false);
        //插入文件数据
        boolean save = iAnnexStorageService.save(annexStorage);
        if(save){
            //返回id
            return annexStorage.getId();
        }else {
            return null ;
        }
    }


    /**
     * @Description #文件存储
     * @Param [fileByte 文件数组, signFileEnum 文件类型枚举, fatherId 文件关联父id]
     * @return 文件数据id
     **/
    public String  saveAnnexStorage(byte[] fileByte,SignFileEnum signFileEnum,String fatherId){
        //设定文件名称
        String path = UUID.randomUUID().toString() + "." + signFileEnum.getSuffix();
        path = storageService.buildPrefixPath(path);
        InputStream inputStream = new ByteArrayInputStream(fileByte);
        //存储文件
        boolean result = storageService.store(inputStream, fileByte.length, signFileEnum.getSuffix(), path);
        if (!result) {
            return null;
        }

        //创建附件数据对象
        AnnexStorage annexStorage = new AnnexStorage();
        //文件名
        annexStorage.setName(path);
        annexStorage.setPath(path);
        annexStorage.setRealName(path);
        annexStorage.setSize(MyFileUtil.getSize(fileByte.length));
        //文件后缀
        annexStorage.setSuffix(signFileEnum.getSuffix());
        //文件类型
        annexStorage.setType(signFileEnum.getFileType());
        //文件种类
        annexStorage.setDataCategory(signFileEnum.getDataCategory());
        //关联父id
        if(fatherId != null){
            annexStorage.setFatherId(fatherId) ;
        }

        annexStorage.setStatus(1);
        annexStorage.setDeleteFlag(false);
        //插入文件数据
        boolean save = iAnnexStorageService.save(annexStorage);
        if(save){
            //返回id
            return annexStorage.getId();
        }else {
            return null ;
        }
    }

    /**
     * @Description #文件存储
     * @Param [fileByte 文件数组, signFileEnum 文件类型枚举, fatherId 文件关联父id]
     * @return 文件数据id
     **/
    public String  saveAnnexStorage(byte[] fileByte,String fileName,String fileSuffix ,SignFileEnum signFileEnum){
        //设定文件名称
        String path = UUID.randomUUID().toString();
        path = storageService.buildPrefixPath(path);
        if(fileSuffix.contains(".")){
            path = path + fileSuffix ;
        }else {
            path = path + "." + fileSuffix ;
        }
        InputStream inputStream = new ByteArrayInputStream(fileByte);
        //存储文件
        boolean result = storageService.store(inputStream, fileByte.length, fileSuffix, path);
        if (!result) {
            return null;
        }

        //创建附件数据对象
        AnnexStorage annexStorage = new AnnexStorage();
        //文件地址
        annexStorage.setPath(path);
        //文件名
        annexStorage.setName(fileName);
        if(fileSuffix.contains(".")){
            annexStorage.setRealName(fileName + fileSuffix);
            //文件后缀
            annexStorage.setSuffix(fileSuffix.replace(".",""));
        }else {
            annexStorage.setRealName(fileName + "." + fileSuffix);
            //文件后缀
            annexStorage.setSuffix(fileSuffix);
        }
        annexStorage.setSize(MyFileUtil.getSize(fileByte.length));

        //文件类型
        annexStorage.setType(signFileEnum.getFileType());
        //文件种类
        annexStorage.setDataCategory(signFileEnum.getDataCategory());

        annexStorage.setStatus(1);
        annexStorage.setDeleteFlag(false);
        //插入文件数据
        boolean save = iAnnexStorageService.save(annexStorage);
        if(save){
            //返回id
            return annexStorage.getId();
        }else {
            return null ;
        }
    }

    public AnnexStorage findByFatherIdAndDataCategory(SignFileEnum signFileEnum,String fatherId){

        QueryWrapper<AnnexStorage> tWrapper = new QueryWrapper<>();
        tWrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        tWrapper.lambda().eq(AnnexStorage::getFatherId,fatherId + "");
        tWrapper.lambda().eq(AnnexStorage::getDataCategory,signFileEnum.getDataCategory());
        List<AnnexStorage> list = iAnnexStorageService.list(tWrapper);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null ;
    }


    public List<AnnexStorage> findByFatherIdAndDataCategoryList(SignFileEnum signFileEnum,String fatherId){

        QueryWrapper<AnnexStorage> tWrapper = new QueryWrapper<>();
        tWrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        tWrapper.lambda().eq(AnnexStorage::getFatherId,fatherId + "");
        tWrapper.lambda().eq(AnnexStorage::getDataCategory,signFileEnum.getDataCategory());
        List<AnnexStorage> list = iAnnexStorageService.list(tWrapper);
        return list ;
    }


    /**
     * @Description #根据文件类型和关联id直接获取文件的二进制字节文件
     * @Param [signFileEnum, fatherId]
     * @return byte[]
     **/
    public byte[] getByteByFatherIdAndDataCategory(SignFileEnum signFileEnum,String fatherId){
        byte[] bytes = null ;

        QueryWrapper<AnnexStorage> tWrapper = new QueryWrapper<>();
        tWrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        tWrapper.lambda().eq(AnnexStorage::getFatherId,fatherId);
        tWrapper.lambda().eq(AnnexStorage::getDataCategory,signFileEnum.getDataCategory());
        List<AnnexStorage> list = iAnnexStorageService.list(tWrapper);
        if(list != null && list.size() > 0){
            AnnexStorage annexStorage = list.get(0);
            if (annexStorage == null) {
                return null ;
            }
            String filePath = annexStorage.getPath();
            InputStream inputStream = null;
            OSS ossClient = null;
            try {
                //改成阿里云下载
                ossClient = storageService.getOSSClient();
                inputStream = storageService.loadAsInputStream(filePath, ossClient);
                if (inputStream != null) {
                    bytes = com.kaifangqian.utils.IOUtils.stream2Byte(inputStream);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (ossClient != null) {
                    ossClient.shutdown();
                }
            }
            return bytes ;
        }
        return null ;
    }

    public AnnexStorage getAnnexById(String id){
        return iAnnexStorageService.getById(id);
    }


    /**
     * @Description #根据文件id获取文件字节数组
     * @Param [id]
     * @return byte[]
     **/
    public byte[] getByteById(String id){
        byte[] bytes = null ;
        AnnexStorage annexStorage = iAnnexStorageService.getById(id);
        if (annexStorage == null) {
            return null ;
        }
        String filePath = annexStorage.getPath();
        InputStream inputStream = null;
        OSS ossClient = null;
        try {
            //改成阿里云下载
            ossClient = storageService.getOSSClient();
            inputStream = storageService.loadAsInputStream(filePath, ossClient);
            if (inputStream != null) {
                bytes = com.kaifangqian.utils.IOUtils.stream2Byte(inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return bytes ;
    }


    public Boolean deleteByFatherIdAndDataCategory(SignFileEnum signFileEnum,String fatherId){
        //构建查询条件
        QueryWrapper<AnnexStorage> tWrapper = new QueryWrapper<>();
        tWrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        tWrapper.lambda().eq(AnnexStorage::getFatherId,fatherId);
        tWrapper.lambda().eq(AnnexStorage::getDataCategory,signFileEnum.getDataCategory());
        //构建更新对象
        AnnexStorage annexStorage = new AnnexStorage();
        annexStorage.setDeleteFlag(true);
        boolean update = iAnnexStorageService.update(annexStorage,tWrapper);

        return update ;
    }

    public void deleteById(String id){

        iAnnexStorageService.deleteExt(id);
    }


    /**
     * @Description #转图片并且保存图片数据
     * @Param [annexId]
     * @return void
     **/
    public List<DocImageVo> saveAndConvertImage(String annexId, byte[] annexFileByte){
        String convertImageKey = CONVERT_IMAGE + annexId;
        //首次查询，如果存在，直接返回
        List<DocImageVo> voList = getImageList(annexId);
        if(voList != null && voList.size() > 0){
            return voList;
        }
        //对同一个annexId进行分布式锁，防止多线程情况下重复对同一个annexId进行转图片
        RLock lock = redissonClient.getLock(convertImageKey);
        try {
            //获取分布式锁
            if (lock.tryLock(120, 120, TimeUnit.SECONDS)){
                //再次查询，如果存在，直接返回
                voList = getImageList(annexId);
                if(voList != null && voList.size() > 0){
                    return voList;
                }
                voList = convertImage(annexId,annexFileByte);
            }

        }catch (Exception e){

        }finally {
            if(lock.isLocked() && lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }
        return voList ;
    }

    public List<DocImageVo> getImageList(String annexId){
        List<DocImageVo> voList = new ArrayList<>();
        //先查询之前是否已经转换过
        List<AnnexImage> annexImageList = annexImageService.listByAnnexId(annexId);
        if(annexImageList == null || annexImageList.size() == 0){
            return voList ;
        }
        for(AnnexImage annexImage : annexImageList){
            DocImageVo vo = new DocImageVo();
            vo.setId(annexImage.getId());
            vo.setPage(annexImage.getPage());
            vo.setAnnexId(annexImage.getImageAnnexId());
            vo.setImageWidth(annexImage.getImageWidth());
            vo.setImageHeight(annexImage.getImageHeight());
            voList.add(vo);
        }
        return voList ;
    }

    public List<DocImageVo> convertImage(String annexId, byte[] annexFileByte){
        List<DocImageVo> voList = new ArrayList<>();
        if(annexFileByte == null){
            annexFileByte = getByteById(annexId);
        }
        if(annexFileByte == null){
            return voList ;
        }
        long beforeConvertImage = System.currentTimeMillis();
        //最新签约文件转图片
        List<ImageConvertVo> imageConvertVos = convertImageService.convertImage(annexFileByte);
        long afterConvertImage = System.currentTimeMillis();
        //保存图片数据
        if(imageConvertVos != null && imageConvertVos.size() > 0){
            List<AnnexImage> imageList = new ArrayList<>();
            for(ImageConvertVo vo : imageConvertVos){
                AnnexImage image = new AnnexImage();
                image.setImageAnnexId(vo.getImageAnnexId());
                image.setPage(vo.getPage());
                image.setAnnexId(annexId);

                image.setImageWidth(vo.getImageWidth());
                image.setImageHeight(vo.getImageHeight());

                image.setDeleteFlag(false);

                imageList.add(image);
            }
            boolean b = annexImageService.saveBatch(imageList);
            if(b){
                for(AnnexImage annexImage : imageList){
                    DocImageVo vo = new DocImageVo();
                    vo.setId(annexImage.getId());
                    vo.setPage(annexImage.getPage());
                    vo.setAnnexId(annexImage.getImageAnnexId());
                    vo.setImageWidth(annexImage.getImageWidth());
                    vo.setImageHeight(annexImage.getImageHeight());
                    voList.add(vo);
                }
            }
        }
        long saveImageList = System.currentTimeMillis();
        System.out.println("耗时：转图片：" + (afterConvertImage - beforeConvertImage) + "毫秒，存储图片：" +
                (saveImageList - afterConvertImage) + "毫秒");

        return voList ;
    }


    /**
     * @Description #复制原文件的图片数据到目标文件
     * @Param [originFileAnnexId, targetFileAnnexId]
     * @return void
     **/
    public void copyFileImage(String originFileAnnexId,String targetFileAnnexId){
        List<DocImageVo> imageVoList = saveAndConvertImage(originFileAnnexId, null);
        if(imageVoList != null && imageVoList.size() > 0){
            List<AnnexImage> annexImageList = new ArrayList<>();
            for(DocImageVo imageVo : imageVoList){
                AnnexImage annexImage = new AnnexImage();
                annexImage.setAnnexId(targetFileAnnexId);
                annexImage.setPage(imageVo.getPage());
                annexImage.setImageHeight(imageVo.getImageHeight());
                annexImage.setImageWidth(imageVo.getImageWidth());

                String newImageAnnexId = copyAnnexStorage(imageVo.getAnnexId());
                annexImage.setImageAnnexId(newImageAnnexId);
                annexImage.setDeleteFlag(false);

                annexImageList.add(annexImage);
            }
            boolean b = annexImageService.saveBatch(annexImageList);
            if(!b){
                throw new PaasException("保存模板图片文件失败");
            }
        }else {
            saveAndConvertImage(targetFileAnnexId, null);
        }
    }



}