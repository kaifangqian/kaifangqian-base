/**
 * @description 文件存储工具类
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
package com.kaifangqian.modules.opensign.util;

import com.kaifangqian.modules.opensign.enums.FileEnum;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.UUID;
import java.nio.file.Path;

/**
 * @Description: 文件存储工具类
 * @Package: com.kaifangqian.modules.opensign.util
 * @ClassName: FileUtils
 * @author: Fusion
 * CreateTime:  2023/8/22  14:53
 * @copyright 北京资源律动科技有限公司
 */

public class FileUtils {


    /**操作的文件目录，可自定选其他目录*/
    private static final String path = "/Applications/mqz/seal-builder/";

    public static final String base64Prefix="data:image/png;base64,";

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    /**
     * 创建文件夹路径
     * @param fileEnum
     * @return
     */
    public static String makeDir(FileEnum fileEnum) {
        String filePath = path + TimeUtils.getYearToHou() + getUUID() + fileEnum.getPrefix();
        return filePath;
    }

    /**
     * base64转文件
     * @param base64
     * @param fileEnum
     * @return
     * @throws IOException
     */
    public static String base64ToFile(String base64, FileEnum fileEnum) throws IOException {
        String filePath = makeDir(fileEnum);
        Files.write(Paths.get(filePath), Base64.getDecoder().decode(base64), StandardOpenOption.CREATE);
        return filePath;
    }

    /**
     * 文件路径转base64
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String pathToBase64(String filePath) throws IOException {
        return Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(filePath)));
    }

    /**
     * 文件转base64
     * @param file
     * @return
     * @throws IOException
     */
    public static String fileToBase64(File file) throws IOException {
        return base64Prefix + pathToBase64(file.getAbsolutePath());
    }


    /**
     * 文件下载到本地
     * @param contractUrl
     * @param fileEnum
     * @return
     * @throws Exception
     */
    public static String downLoad(String contractUrl, FileEnum fileEnum) throws Exception {
        String filePath = makeDir(fileEnum);
        URL imgUrl = new URL(contractUrl);
        org.apache.commons.io.FileUtils.copyURLToFile(imgUrl, new File(filePath));
        return filePath;
    }

    /**
     * 文件删除
     * @param path
     */
    public static void deleteFile(String path) {
        String frontPath = FontUtils.SEAL_SAVE_PATH;
        if(!path.contains(frontPath)){
            path =  frontPath + path;
        }
        try {
            File file = new File(path);
            if (file.exists()) {
                boolean result = file.delete();
            }
        } catch (Exception e) {

        }
    }

    public static void deleteDirectory(File directory) throws IOException {
        Files.walk(directory.toPath())
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .forEach(File::delete);
    }

    public static void main(String[] args) throws IOException {
        File directory = new File("/Users/huaiyong/Documents/JetBrains/Codeup/resrun-paas-parent/resrun-paas-system/src/main/resources/upload/B.pdf");
        deleteDirectory(directory);
    }
}
