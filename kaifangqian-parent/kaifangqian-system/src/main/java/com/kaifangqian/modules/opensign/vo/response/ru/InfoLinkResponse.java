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
package com.kaifangqian.modules.opensign.vo.response.ru;

import com.fasterxml.jackson.annotation.JsonFormat;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: InfoLinkResponse
 * @Package: com.kaifangqian.modules.opensign.vo.response.ru
 * @ClassName: InfoLinkResponse
 * @author: FengLai_Gong
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
// @ApiModel("业务线实例-短信链接数据详情-返回数据")
public class InfoLinkResponse implements Serializable {

    private static final long serialVersionUID = -8227874145137753158L;
//    @ApiModelProperty("文档主题")
    private String subject ;

//    @ApiModelProperty("文档编号")
    private String docNo;

//    @ApiModelProperty("发送方名称")
    private String senderName ;

//    @ApiModelProperty("签署方名称")
    private String signerName ;

//    @ApiModelProperty("签署方名称")
    private List<SignRuSignerResponse> signers;

//    @ApiModelProperty("签署状态")
    private Integer signStatus ;

//    @ApiModelProperty("任务状态")
    private Integer taskStatus;

//    @ApiModelProperty("办理动作类型：approve,reject")
    private String checkMenuType;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @ApiModelProperty("'签署截止时间'")
    private Date expireDate ;

//    @ApiModelProperty("文件份数")
    private Integer fileSum;

//    @ApiModelProperty("文件名称")
    private List<String> signFileNames;



}