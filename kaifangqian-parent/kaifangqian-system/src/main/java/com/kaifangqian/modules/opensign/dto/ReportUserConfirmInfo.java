/**
 * @description 签署校验参数
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
package com.kaifangqian.modules.opensign.dto;

// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author : zhenghuihan
 * create at:  2024/5/29  14:50
 * @description:
 */
@Data
public class ReportUserConfirmInfo {
    // @ApiModelProperty(value = "校验类型")
    private String confirmType;
    // @ApiModelProperty(value = "签署校验时间")
    private Date confirmTime;
    // @ApiModelProperty(value = "签署校验信息")
    private String confirmInfo;

    // @ApiModelProperty(value = "人脸识别校验通道")
    private String faceChannel;
    // @ApiModelProperty(value = "活体检测分数")
    private String liveRate;
    // @ApiModelProperty(value = "人脸比对分数")
    private String similarity;
}