/**
 * @Discription:文件存储BASE64接口类
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
package com.kaifangqian.modules.storage;

import com.aliyun.oss.OSS;
import com.kaifangqian.modules.storage.config.StorageProperties;
import com.kaifangqian.exception.PaasException;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "paas.storage", name = "active", havingValue = "minio")
public class MinioStorage implements Storage{

    @Autowired
    private StorageProperties properties;

    @Override
    public boolean store(InputStream inputStream, long contentLength, String contentType, String keyName) {
        try {
//            UploadObjectArgs args = UploadObjectArgs.builder().bucket(bucketName).contentType(contentType).object(inputStream).filename(keyName).build();
            PutObjectArgs args = PutObjectArgs.builder()
                    .stream(inputStream,inputStream.available(),-1)
                    .bucket(properties.getMinio().getBucketName())
                    .object(keyName).build();
            ObjectWriteResponse response = getMinioClient().putObject(args);
            if(response != null){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void delete(String keyName) {
        try {
            getMinioClient().removeObject(RemoveObjectArgs.builder().bucket(properties.getMinio().getBucketName()).object(keyName).build());
        }catch (Exception e){
            log.error("Minio delete [{}] error",keyName,e);
        }
    }

    @Override
    public Resource loadAsResource(String keyName) {
        return null;
    }

    @Override
    public InputStream loadAsInputStream(String keyName, OSS client) {
//        minioClient.getObject()
        try {
            GetObjectArgs args = GetObjectArgs.builder()
                    .bucket(properties.getMinio().getBucketName()).object(keyName).build();
            return getMinioClient().getObject(args);
        }catch (Exception e){
            log.error("Minio download [{}] error",keyName,e.getMessage());
            throw new PaasException("文件不存在");
        }
    }

    @Override
    public String generateUrl(String keyName) {
        return null;
    }

    @Override
    public OSS getOSSClient() {
        return null;
    }


    private MinioClient minioClient;

    private MinioClient getMinioClient(){
        if(ObjectUtils.isEmpty(minioClient)){
            StorageProperties.Minio minio = this.properties.getMinio();
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(minio.getEndpoint())
                    .credentials(minio.getAccessKey(), minio.getSecretKey())
                    .build();
            try {
                //判断桶是否存在  不存在抛出异常
                boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minio.getBucketName()).build());
                if(!bucketExists){
                    minioClient.makeBucket(MakeBucketArgs.builder().bucket(minio.getBucketName()).build());
                }
            }catch (Exception e){
                throw new PaasException("Minio bucket " + minio.getBucketName() + " exists");
            }
            this.minioClient = minioClient;
            return minioClient;
        }else {
            return minioClient;
        }
    }
//    public void setMinioClient(MinioClient minioClient) {
//        this.minioClient = minioClient;
//    }

//    private String bucketName;

//    public void setBucketName(String bucketName) {
//        this.bucketName = bucketName;
//    }
}
