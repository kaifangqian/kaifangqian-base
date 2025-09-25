/**
 * @description API接口合同信息对象
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
package com.kaifangqian.modules.api.vo.request;

import com.kaifangqian.modules.api.base.ReqBaseVO;
import com.kaifangqian.modules.api.vo.base.*;
import com.kaifangqian.modules.api.vo.base.*;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: ContractDraftRequest
 * @Package: com.kaifangqian.modules.api.vo.request
 * @ClassName: ContractDraftRequest
 * @author: FengLai_Gong
 * @Date: 2024/3/19
 */
@Data
//public class ContractDraftRequest implements Serializable {
public class ContractDraftRequest extends ReqBaseVO implements Serializable {

    private static final long serialVersionUID = -7293700917961018375L;

    //基本信息
    // @ApiModelProperty("业务线id")
    private String signReId ;

    // @ApiModelProperty("合同编号")
    private String sn ;

    // @ApiModelProperty("合同主题")
    private String subject ;

    // @ApiModelProperty("签署截止日期，日期格式，且不小于当前日期")
    private String expireDate ;

    //文件信息
    // @ApiModelProperty("签约文件合集 ")
    private List<ContractDocumentFile> signDocumentList;

    // @ApiModelProperty("合同附件id合集")
    private List<ContractAnnexFile> annexList ;

    // @ApiModelProperty("发起方填写的模板参数值")
    private List<ContractTemplate> signTemplateParamList ;

    //签署信息

    //发起人
    // @ApiModelProperty("经办人")
    private ContractUser operator;
    //签署方
    // @ApiModelProperty("签署方信息")
    private List<ContractSigner> signerList ;

    // @ApiModelProperty("签署顺序类型，有序ORDER，无序NO_ORDER")
    private String signOrderType ;

    // @ApiModelProperty("控件变更状态,签署位置方式,NECESSARY_NO_ADD必须，不可增加；NECESSARY_AND_ADD 必须，可增加；NOT_NECESSARY 非必须")
    private String controlChangeFlag ;

    //必要信息
    // @ApiModelProperty("是否直接发起，0 保存草稿，1 直接发起")
    private String send ;

    // @ApiModelProperty("经办人账号")
    private String operatorAccount;

    // @ApiModelProperty("发起方企业")
    private String enterpriseName;

    //发起类型：api(接口发起)；app（应用发起）
    private String sendType ;




}