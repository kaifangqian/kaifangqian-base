/**
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
package com.kaifangqian.modules.storage.controller;

import com.aliyun.oss.OSS;
import com.kaifangqian.config.limit.annotation.Limit;
import com.kaifangqian.config.limit.annotation.LimitHandleType;
import com.kaifangqian.config.limit.annotation.LimitType;
import com.kaifangqian.config.limit.annotation.OperateType;
import com.kaifangqian.external.sign.response.SignAppInfoResponse;
import com.kaifangqian.external.sign.service.SignServiceManageExternal;
import com.kaifangqian.modules.storage.StorageService;
import com.kaifangqian.annotation.ResrunLogMethod;
import com.kaifangqian.annotation.ResrunLogModule;
import com.kaifangqian.common.enums.OperateLogType;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.config.FileProperties;
import com.kaifangqian.inteface.CommonToolsAPI;
import com.kaifangqian.modules.storage.entity.AnnexStorage;
import com.kaifangqian.modules.storage.service.IAnnexBase64Service;
import com.kaifangqian.modules.storage.service.IAnnexStorageService;
import com.kaifangqian.utils.MyStringUtils;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhh
 */
@RestController
@RequestMapping("/file")
// @Api(tags = "文件管理")
@Slf4j
@ResrunLogModule(name = "文件管理模块")
public class FileStorageController {

    @Autowired
    private IAnnexStorageService iAnnexStorageService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private FileProperties fileProperties;
    @Autowired
    private IAnnexBase64Service annexBase64Service;
    @Autowired
    private CommonToolsAPI commonToolsAPI;
    @Autowired
    private SignServiceManageExternal signServiceManageExternal;

    /**
     * base64 png 图片前缀
     */
    private static final String BASE64PNG_PRE = "data:image/png;base64,";

    // @ApiOperation("文件上传，返回id")
    @PostMapping
    @ResrunLogMethod(name = "文件上传", operateType = OperateLogType.OPERATE_TYPE_2)
    @Limit(name = "通用文件上传", prefix = "limit",limitType= LimitType.TOKEN, operateType = OperateType.ALL, count = 5,period=30,limitHandle = LimitHandleType.LOGOUT)
    public Result<?> create(AnnexStorage annexStorage, @RequestParam("file") MultipartFile file) {
        String id = iAnnexStorageService.create(annexStorage, file);
        return Result.OK("success", id);
    }

    // @ApiOperation("base64文件上传，返回id")
    @PostMapping("/base64")
    @ResrunLogMethod(name = "文件上传", operateType = OperateLogType.OPERATE_TYPE_2)
    public Result<?> createBase64(@RequestParam("file") MultipartFile file) {
        String id = iAnnexStorageService.createBase64(file);
        return Result.OK("success", id);
    }


    // @ApiOperation("更新附件信息")
    @PostMapping("/update")
    @ResrunLogMethod(name = "文件更新", operateType = OperateLogType.OPERATE_TYPE_3)
    public Result<?> update(@RequestBody AnnexStorage annexStorage) {
        iAnnexStorageService.updateById(annexStorage);
        return Result.OK();
    }

    // @ApiOperation("获取文件列表")
    @GetMapping
    @ResrunLogMethod(name = "文件列表", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<?> selectCharge(@RequestParam(value = "fatherId", required = true) String fatherId, String dataCategory) {
        AnnexStorage query = new AnnexStorage();
        query.setFatherId(fatherId);
        query.setDataCategory(dataCategory);
        List<AnnexStorage> annexStorages = iAnnexStorageService.getByEntity(query);
        return Result.OK(annexStorages);
    }

    // @ApiOperation("删除附件")
    @DeleteMapping
    @ResrunLogMethod(name = "文件删除", operateType = OperateLogType.OPERATE_TYPE_4)
    public Result<?> delete(String id) {
        iAnnexStorageService.deleteExt(id);
        return Result.OK();
    }

    /**
     * 文件下载 文件流
     */
    @ResrunLogMethod(name = "文件下载", operateType = OperateLogType.OPERATE_TYPE_0)
    @RequestMapping(value = "/downloadFileStream/{fileId}", method = RequestMethod.GET)
    public void downloadFileStream(@PathVariable String fileId, HttpServletResponse response, HttpServletRequest request) {
        if (fileId == null) {
            return;
        }
        AnnexStorage annexStorage = iAnnexStorageService.getById(fileId);
        if (annexStorage == null) {
            return;
        }
        String filePath = annexStorage.getPath();
        String fileName = annexStorage.getRealName();
        OutputStream outputStream = null;
        InputStream inputStream = null;
        OSS ossClient = null;
        try {
//            fileName = fileName.replaceAll("#", "%23").replaceAll("&", "%26").replaceAll("$", "%24");
            String fileNameencode = URLEncoder.encode(fileName, "UTF-8");
//            fileNameencode = FilenameUtils.normalize(fileNameencode);
//            String fileNameencode = FilenameUtils.normalize(fileName);
//            String fileNameencode = fileNameencode = URLEncoder.encode(fileName, "UTF-8");
//            if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
//                fileNameencode = URLEncoder.encode(fileName, "UTF-8");
//            } else {
//                fileNameencode = new String(fileName.getBytes("GBK"), "ISO8859-1");
//            }

            response.setCharacterEncoding("utf-8");
            response.setContentType("application/octet-stream");
//            response.setHeader("Content-disposition", "attachment;filename=" + fileNameencode + ";filename*=utf-8''" + fileNameencode);
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileNameencode);
            response.setHeader("fileName", fileNameencode);
            response.setHeader("Access-Control-Expose-Headers", "fileName");

            outputStream = response.getOutputStream();
            if (filePath == null) {
                return;
            }
            //改成阿里云下载
            ossClient = storageService.getOSSClient();
            inputStream = storageService.loadAsInputStream(filePath, ossClient);

            if (inputStream == null) {
                return;
            }
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
    }


    /**
     * 文件下载 文件流2服务器下载
     */
    @ResrunLogMethod(name = "文件流2服务器下载", operateType = OperateLogType.OPERATE_TYPE_0)
    @RequestMapping(value = "/downloadFileStream2/{name}", method = RequestMethod.GET)
    public void downloadFileStream2(@PathVariable String name, HttpServletResponse response) {
        OutputStream outputStream = null;
        try {
            String fileNameencode = URLEncoder.encode(name, "UTF-8");
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileNameencode);
            response.setHeader("fileName", fileNameencode);
            response.setHeader("Access-Control-Expose-Headers", "fileName");

            outputStream = response.getOutputStream();
            //下载
            FileInputStream inputStream = new FileInputStream(fileProperties.getPath().getPath() + name);
            byte[] buffer = new byte[1024];

            int len;
            while ((len = inputStream.read(buffer)) != -1) {

                outputStream.write(buffer, 0, len);

            }
            inputStream.close();
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 文件下载 base64
     */
    @ResrunLogMethod(name = "文件下载", operateType = OperateLogType.OPERATE_TYPE_0)
    @RequestMapping(value = "/downloadFileBase/{fileId}", method = RequestMethod.GET)
    public Result downloadFilebBase64(@PathVariable String fileId) {
        if (MyStringUtils.isBlank(fileId)) {
            return Result.error("文件ID能为空");
        }
        AnnexStorage annexStorage = iAnnexStorageService.getById(fileId);
        if (annexStorage == null) {
            return Result.error("文件不存在");
        }
        String filePath = annexStorage.getPath();
        InputStream inputStream = null;
        OSS ossClient = null;
        try {
            //改成阿里云下载
            ossClient = storageService.getOSSClient();
            inputStream = storageService.loadAsInputStream(filePath, ossClient);
            if (inputStream == null) {
                return Result.error("文件不存在");
            }
            //转换成字节
            byte[] bytes = com.kaifangqian.utils.IOUtils.stream2Byte(inputStream);
            //转换成base64串
            String base64 = Base64.getEncoder().encodeToString(bytes).trim();
            base64 = base64.replaceAll("\n", "").replaceAll("\r", "");

            Map<String, String> result = new HashMap<>();
            result.put("image", BASE64PNG_PRE + base64);
            return Result.OK("success", result);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
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
        return Result.error("error");
    }

    /**
     * 文件下载 字节数组
     */
    @ResrunLogMethod(name = "文件下载", operateType = OperateLogType.OPERATE_TYPE_0)
    @RequestMapping(value = "/downloadFileByte/{fileId}", method = RequestMethod.GET)
    public Result downloadFileByte(@PathVariable String fileId) {
        if (MyStringUtils.isBlank(fileId)) {
            return Result.error("文件ID能为空");
        }
        AnnexStorage annexStorage = iAnnexStorageService.getById(fileId);
        if (annexStorage == null) {
            return Result.error("文件不存在");
        }
        String filePath = annexStorage.getPath();
        InputStream inputStream = null;
        OSS ossClient = null;
        try {
            //改成阿里云下载
            ossClient = storageService.getOSSClient();
            inputStream = storageService.loadAsInputStream(filePath, ossClient);

            if (inputStream == null) {
                return Result.error("文件不存在");
            }
            byte[] bytes = com.kaifangqian.utils.IOUtils.stream2Byte(inputStream);
            return Result.OK("success", bytes);
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
        return Result.error("error");
    }


    /**
     * SysConfig 文件下载 base64
     */
    @ResrunLogMethod(name = "文件下载", operateType = OperateLogType.OPERATE_TYPE_0)
    @RequestMapping(value = "/downloadFileBase64Type/{type}", method = RequestMethod.GET)
    public Result downloadFileBase64Type(@PathVariable String type) {
//        if (MyStringUtils.isBlank(type)) {
//            return Result.error("类型不能为空");
//        }
//        SysConfig sysConfig = commonToolsAPI.getConfigByType(type);
//        if (sysConfig == null || MyStringUtils.isBlank(sysConfig.getValue())) {
//            return Result.error("文件不存在");
//        }
//        AnnexBase64 annexBase64 = annexBase64Service.getById(sysConfig.getValue());
//        if (annexBase64 == null) {
//            return Result.error("文件不存在");
//        }
//        if (MyStringUtils.isBlank(annexBase64.getFileBase64())) {
//            return Result.error("文件不存在");
//        }
//        Map<String, String> result = new HashMap<>();
//        result.put("image", BASE64PNG_PRE + annexBase64.getFileBase64());
//        return Result.OK("success", result);
        try {
            SignAppInfoResponse signAppInfoResponse = signServiceManageExternal.querySignAppInfo();
            Map<String, String> result = new HashMap<>();
            result.put("image", BASE64PNG_PRE + signAppInfoResponse.getAppLogo());
            return Result.OK("success", result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}