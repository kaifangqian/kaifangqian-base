/**
 * @description 控件基础数据查询通用对象
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
package com.kaifangqian.modules.opensign.vo.base.sign;

// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: DocControlListVo
 * @Package: com.kaifangqian.modules.opensign.vo.base.sign
 * @ClassName: DocControlListVo
 * @author: FengLai_Gong
 */
@Data
// @ApiModel("控件基础数据查询通用对象")
public class DocControlListVo implements Serializable {


    private static final long serialVersionUID = -8975152834775997714L;

    // @ApiModelProperty("'业务线实例主表id'")
    private String signRuId ;

//    // @ApiModelProperty("'业务线实例-签约文件id'")
//    private String signRuDocId ;

//    // @ApiModelProperty("'业务线实例签约文件id列表'")
//    private List<String> signRuDocIdList ;



    // @ApiModelProperty("'业务线配置主表id'")
    private String signReId;

//    // @ApiModelProperty("'业务线配置-签约文件id'")
//    private String signReDocId ;

//    // @ApiModelProperty("'业务线配置签约文件id列表'")
//    private List<String> signReDocIdList ;

//    // @ApiModelProperty("'人员类型，1发起方，2接收方'")
//    private Integer signerType ;

    // @ApiModelProperty("'签署方id，signerId或者senderId'")
    private String signerId ;

    // @ApiModelProperty("'控件类型，填写、签署'")
    private String controlType ;


//    // @ApiModelProperty("'签署方id列表，signerId或者senderId'")
//    private List<String> signerIdList ;

    // @ApiModelProperty("'控件类型列表")
    private List<String> controlTypeList;

//    // @ApiModelProperty("''异常状态，1为是，2为否''")
//    private Integer errorStatus ;

    // @ApiModelProperty("是否已经填充，1为已填充，2为未填写")
    private Integer isFilled;

    // @ApiModelProperty("是否为必填项，1为必填项，2为非必填项")
    private Integer isRequired;




}