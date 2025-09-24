/**
 * @description 业务线基础数据对象
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

import java.io.Serializable;
import java.util.List;

/**
 * @Description: BaseVo
 * @Package: com.kaifangqian.modules.opensign.vo.base.sign
 * @ClassName: BaseVo
 * @author: FengLai_Gong
 */
@Data
// @ApiModel("业务线基础数据对象")
public class BaseVo implements Serializable {


    private static final long serialVersionUID = -2766262946312521349L;

    // @ApiModelProperty("业务线主数据")
    private DocBaseVo baseVo ;

    // @ApiModelProperty("签约方列表数据")
    private List<DocSignerVo> signerList ;

    // @ApiModelProperty("抄送方列表数据")
    private List<DocCcerVo> ccerList ;

    // @ApiModelProperty("审批相关列表")
    private List<DocApproveVo> approveList ;

    // @ApiModelProperty("签约文件列表")
    private List<DocFileVo> fileList ;

    // @ApiModelProperty("签约文件-附件列表")
    private List<DocOtherFileVo> otherFileList ;

    // @ApiModelProperty("发起人id")
    private String startUserId ;

    // @ApiModelProperty("发起人名称")
    private String startUserName ;

}