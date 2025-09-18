/**
 * @description 电子印章--模板文件夹相关接口
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

import com.kaifangqian.annotation.ResrunLogModule;
import com.kaifangqian.modules.opensign.service.template.SignTemplateFolderRelationService;
import com.kaifangqian.modules.opensign.service.template.SignTemplateFolderService;
import com.kaifangqian.modules.opensign.service.template.SignTemplateService;
// import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: TemplateFolderController
 * @Package: com.kaifangqian.modules.sign.controller
 * @ClassName: TemplateFolderController
 * @author: FengLai_Gong
 * @Date: 2023/7/27 10:05
 */
// @Api(tags = "电子印章--模板文件夹相关接口")
@RestController
@RequestMapping("/sign/template/folder")
@Slf4j
@ResrunLogModule(name = "模板文件夹相关接口")
public class SignTemplateFolderController {


    @Autowired
    private SignTemplateFolderService templateFolderService ;
    @Autowired
    private SignTemplateFolderRelationService templateFolderRelationService ;
    @Autowired
    private SignTemplateService templateService ;






}