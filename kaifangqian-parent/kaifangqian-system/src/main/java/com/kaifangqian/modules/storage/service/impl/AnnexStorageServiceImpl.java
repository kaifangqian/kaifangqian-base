/**
 * @Discription:文件存储接口类
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
package com.kaifangqian.modules.storage.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.modules.storage.StorageService;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.storage.dto.StorageDto;
import com.kaifangqian.modules.storage.entity.AnnexBase64;
import com.kaifangqian.modules.storage.entity.AnnexStorage;
import com.kaifangqian.modules.storage.mapper.AnnexStorageMapper;
import com.kaifangqian.modules.storage.service.IAnnexBase64Service;
import com.kaifangqian.modules.storage.service.IAnnexStorageService;
import com.kaifangqian.utils.IOUtils;
import com.kaifangqian.utils.MyFileUtil;
import com.kaifangqian.utils.MyStringUtils;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class AnnexStorageServiceImpl extends ServiceImpl<AnnexStorageMapper, AnnexStorage> implements IAnnexStorageService {

    @Autowired
    private StorageService storageService;
    @Autowired
    private IAnnexBase64Service annexBase64Service;

    @Override
    public String create(AnnexStorage annexStorage, MultipartFile file) {
        String suffix = MyFileUtil.getExtensionName(file.getOriginalFilename());
        String type = MyFileUtil.getFileType(suffix);
        String path = UUID.randomUUID().toString() + "." + suffix;
        try {
            boolean result = storageService.store(file.getInputStream(), file.getSize(), file.getContentType(), path);
            if (!result) {
                return null;
            }
            String filenName = annexStorage.getName();
            filenName = MyStringUtils.isBlank(filenName) ? MyFileUtil.getFileNameNoEx(file.getOriginalFilename()) : filenName;
            annexStorage.setName(filenName);
            annexStorage.setPath(path);
            annexStorage.setRealName(file.getOriginalFilename());
            annexStorage.setSize(MyFileUtil.getSize(file.getSize()));
            annexStorage.setSuffix(suffix);
            annexStorage.setType(type);
            annexStorage.setStatus(1);
            annexStorage.setDeleteFlag(false);

            this.save(annexStorage);
            return annexStorage.getId();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String createBase64(MultipartFile file) {
        // 将文件转为Base64字符串
        String base64 = null;
        try {
            base64 = java.util.Base64.getEncoder().encodeToString(file.getBytes()).trim();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (MyStringUtils.isBlank(base64)) {
            throw new PaasException("文件上传失败");
        }
        AnnexBase64 annexBase64 = new AnnexBase64();
        annexBase64.setFileBase64(base64);
        annexBase64Service.save(annexBase64);

        return annexBase64.getId();
    }

    @Override
    public String create(byte[] file, String dataCategory, String originalFilename, long fileSize, String fatherId) {
        String suffix = MyFileUtil.getExtensionName(originalFilename);
        String type = MyFileUtil.getFileType(suffix);
        String path = UUID.randomUUID().toString() + "." + suffix;
        try {
            boolean result = storageService.store(IOUtils.byte2InputStream(file), 0, null, path);
            if (!result) {
                return null;
            }
            AnnexStorage annexStorage = new AnnexStorage();
            annexStorage.setFatherId(fatherId);
            annexStorage.setDataCategory(dataCategory);
            annexStorage.setName(MyFileUtil.getFileNameNoEx(originalFilename));
            annexStorage.setPath(path);
            annexStorage.setRealName(originalFilename);
            annexStorage.setSize(MyFileUtil.getSize(fileSize));
            annexStorage.setSuffix(suffix);
            annexStorage.setType(type);
            annexStorage.setStatus(1);
            annexStorage.setDeleteFlag(false);

            this.save(annexStorage);
            return annexStorage.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String create(String base64, String fatherId, String dataCategory, String originalFilename) {
        // 解码 Base64 字符串
        byte[] decodedBytes = Base64.decodeBase64(base64);
        // 获取解码后的字节数组的大小
        int fileSize = decodedBytes.length;
        // 创建 ByteArrayInputStream
        InputStream inputStream = new ByteArrayInputStream(decodedBytes);
        String suffix = MyFileUtil.getExtensionName(originalFilename);
        String type = MyFileUtil.getFileType(suffix);
        String path = UUID.randomUUID().toString() + "." + suffix;
        AnnexStorage annexStorage = new AnnexStorage();
        try {
            boolean result = storageService.store(inputStream, 0, null, path);
            if (!result) {
                return null;
            }
            annexStorage.setDataCategory(dataCategory);
            annexStorage.setName(MyFileUtil.getFileNameNoEx(originalFilename));
            annexStorage.setFatherId(fatherId);
            annexStorage.setPath(path);
            annexStorage.setRealName(originalFilename);
            annexStorage.setSize(MyFileUtil.getSize(fileSize));
            annexStorage.setSuffix(suffix);
            annexStorage.setType(type);
            annexStorage.setStatus(1);
            annexStorage.setDeleteFlag(false);
            this.save(annexStorage);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return annexStorage.getId();
    }

    @Override
    public void deleteExt(String id) {
        //1、删除文件
        AnnexStorage delete = this.getById(id);
        if (MyStringUtils.isNotBlank(delete.getPath())) {
            storageService.delete(delete.getPath());
        }
        //2、更新记录
        LoginUser user = MySecurityUtils.getCurrentUser();
        AnnexStorage add = new AnnexStorage();
        add.setId(id);
        add.setDeleteFlag(true);
        add.setDeleteBy(user.getUsername());
        add.setDeleteTime(new Date());

        this.updateById(add);
    }

    @Override
    public List<AnnexStorage> getByEntity(AnnexStorage annexStorage) {
        LambdaQueryWrapper<AnnexStorage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MyStringUtils.isNotBlank(annexStorage.getFatherId()), AnnexStorage::getFatherId, annexStorage.getFatherId()).eq(MyStringUtils.isNotBlank(annexStorage.getDataCategory()), AnnexStorage::getDataCategory, annexStorage.getDataCategory());

        return this.list(queryWrapper);
    }

    @Override
    public List<StorageDto> getByFatherId(String fatherId) {
        List<StorageDto> storageDtos = new ArrayList<>();
        if (MyStringUtils.isNotBlank(fatherId)) {
            LambdaQueryWrapper<AnnexStorage> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AnnexStorage::getFatherId, fatherId);
            List<AnnexStorage> annexStorages = this.list(queryWrapper);
            if (CollUtil.isNotEmpty(annexStorages)) {
                annexStorages.forEach(a -> {
                    StorageDto storageDto = new StorageDto();
                    BeanUtils.copyProperties(a, storageDto);

                    storageDtos.add(storageDto);
                });
            }
        }
        return storageDtos;
    }

    @Override
    public List<AnnexStorage> getByFatherIds(List<String> fatherIds) {
        LambdaQueryWrapper<AnnexStorage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(AnnexStorage::getFatherId, fatherIds);

        return this.list(queryWrapper);
    }

    @Override
    public List<AnnexStorage> getByFatherIdsAndType(List<String> fatherIds, String dataCategory) {
        LambdaQueryWrapper<AnnexStorage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(AnnexStorage::getFatherId, fatherIds).eq(AnnexStorage::getDataCategory, dataCategory);

        return this.list(queryWrapper);
    }

    @Override
    public void updateMainDataFiles(String fatherId, List<StorageDto> files) {
        if (MyStringUtils.isNotBlank(fatherId)) {
            //1、删除旧的关联关系
            LambdaQueryWrapper<AnnexStorage> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AnnexStorage::getFatherId, fatherId);
            List<AnnexStorage> storages = this.list(queryWrapper);
            if (CollUtil.isNotEmpty(storages)) {
                storages.forEach(s -> {
                    s.setFatherId(fatherId + "_old");
                });
                this.updateBatchById(storages);
            }

            //2、新增新的关联关系
            if (CollUtil.isNotEmpty(files)) {
                List<AnnexStorage> annexStorages = new ArrayList<>();
                files.forEach(f -> {
                    if (f != null) {
                        AnnexStorage annexStorage = new AnnexStorage();
                        annexStorage.setId(f.getId());
                        annexStorage.setFatherId(fatherId);

                        annexStorages.add(annexStorage);
                    }
                });
                this.updateBatchById(annexStorages);
            }
        }
    }

    @Override
    public void updateMainDataFilesByType(String fatherId, String dataCategory, List<StorageDto> files) {
        if (MyStringUtils.isNotBlank(fatherId) && MyStringUtils.isNotBlank(dataCategory)) {
            //1、删除旧的关联关系
            LambdaQueryWrapper<AnnexStorage> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AnnexStorage::getFatherId, fatherId).eq(AnnexStorage::getDataCategory, dataCategory);
            List<AnnexStorage> storages = this.list(queryWrapper);
            if (CollUtil.isNotEmpty(storages)) {
                storages.forEach(s -> {
                    s.setFatherId(fatherId + "_old");
                });
                this.updateBatchById(storages);
            }

            //2、新增新的关联关系
            if (CollUtil.isNotEmpty(files)) {
                List<AnnexStorage> annexStorages = new ArrayList<>();
                files.forEach(f -> {
                    AnnexStorage annexStorage = new AnnexStorage();
                    annexStorage.setId(f.getId());
                    annexStorage.setFatherId(fatherId);

                    annexStorages.add(annexStorage);
                });
                this.updateBatchById(annexStorages);
            }
        }
    }
}
