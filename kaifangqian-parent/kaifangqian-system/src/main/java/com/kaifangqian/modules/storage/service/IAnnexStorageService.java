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
package com.kaifangqian.modules.storage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kaifangqian.modules.storage.dto.StorageDto;
import com.kaifangqian.modules.storage.entity.AnnexStorage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface IAnnexStorageService extends IService<AnnexStorage> {
    String create(AnnexStorage annexStorage, MultipartFile file);

    String createBase64(MultipartFile file);

    String create(byte[] file, String dataCategory, String originalFilename, long fileSize, String fatherId);

    String create(String base64, String fatherId, String dataCategory, String originalFilename);

    void deleteExt(String id);

    List<AnnexStorage> getByEntity(AnnexStorage annexStorage);

    List<StorageDto> getByFatherId(String fatherId);

    List<AnnexStorage> getByFatherIds(List<String> fatherIds);

    List<AnnexStorage> getByFatherIdsAndType(List<String> fatherIds, String dataCategory);

    void updateMainDataFiles(String fatherId, List<StorageDto> files);

    void updateMainDataFilesByType(String fatherId, String dataCategory, List<StorageDto> files);
}
