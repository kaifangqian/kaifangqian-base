/**
 * @description 电子印章文件类型枚举
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
package com.kaifangqian.modules.opensign.enums;

/**
 * @Description: 电子印章文件类型枚举
 * @Package: com.kaifangqian.modules.sign.enums
 * @ClassName: SignFileEnum
 * @author: FengLai_Gong
 */
public enum SignFileEnum {


    CERT("jks","jks","cert_cert"),
    JKS("jks","jks","cert_jks"),
    P7B("p7b","p7b","cert_p7b"),
    PFX("pfx","pfx","cert_pfx"),

    SEAL_FILE_ENT("png","png","seal_file_ent"),
    SEAL_FILE_PERSON("png","png","seal_file_person"),

    SEAL_FILE_TEMPORARY("png","png","seal_file_temporary"),

    SIGN_FILE_IMAGE("png","png","sign_file_image"),

    SIGN_FILE_MAIN("pdf","pdf","sign_file_main"),
    SIGN_FILE_OTHER("pdf","pdf","sign_file_other"),



    SIGN_FILE_REPORT("pdf","pdf","sign_file_report"),



    ;

    //文件后缀
    private String suffix ;
    //文件类型
    private String fileType ;
    //附件种类
    private String dataCategory ;

    SignFileEnum(String suffix , String fileType , String dataCategory ){
        this.suffix = suffix ;
        this.fileType = fileType ;
        this.dataCategory = dataCategory ;
    }

    public String getSuffix(){
        return this.suffix ;
    }

    public String getFileType(){
        return this.fileType ;
    }

    public String getDataCategory(){
        return this.dataCategory ;
    }





}