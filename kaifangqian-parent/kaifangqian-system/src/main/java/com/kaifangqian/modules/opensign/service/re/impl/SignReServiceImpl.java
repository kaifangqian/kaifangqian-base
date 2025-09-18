/**
 * @description 业务线业务参数校验
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
package com.kaifangqian.modules.opensign.service.re.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.modules.opensign.entity.SignRe;
import com.kaifangqian.modules.opensign.entity.SignReDoc;
import com.kaifangqian.modules.opensign.entity.SignReDocControl;
import com.kaifangqian.modules.opensign.entity.SignReDocParam;
import com.kaifangqian.modules.opensign.service.business.ReBusinessService;
import com.kaifangqian.modules.opensign.service.template.SignTemplateControlService;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.opensign.entity.*;
import com.kaifangqian.modules.opensign.enums.SignReSignerTypeEnum;
import com.kaifangqian.modules.opensign.mapper.SignReMapper;
import com.kaifangqian.modules.opensign.service.re.SignReDocControlService;
import com.kaifangqian.modules.opensign.service.re.SignReDocParamService;
import com.kaifangqian.modules.opensign.service.re.SignReDocService;
import com.kaifangqian.modules.opensign.service.re.SignReService;
import com.kaifangqian.modules.opensign.vo.base.sign.DocSignerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: SignReServiceImpl
 * @Package: com.kaifangqian.modules.opensign.service.re.impl
 * @ClassName: SignReServiceImpl
 * @author: FengLai_Gong
 */
@Service
public class SignReServiceImpl extends ServiceImpl<SignReMapper, SignRe> implements SignReService {

    @Autowired
    private SignReDocService reDocService;
    @Autowired
    private SignReDocParamService signReDocParamService;
    @Autowired
    private SignTemplateControlService signTemplateControlService;
    @Autowired
    private SignReDocControlService reDocControlService ;

    @Autowired
    private ReBusinessService reBusinessService ;

    /**
     * @Description #校验业务线关联模板数据
     * @Param [signReId]
     * @return void
     **/
    @Override
    public void checkTemplatePara(SignRe signRe) {

        //如果业务线配置设定的签署人类型是自定义，则直接返回
        if(signRe.getSignerType().equals(SignReSignerTypeEnum.CUSTOM.getCode())) {
            return;
        }
        //如果业务线配置设定的签署人类型是预设流程，则校验控件是否有关联人
        //获取业务线签署人
        List<DocSignerVo> signerList = reBusinessService.getSignerList(signRe.getId());
//        //校验签章权限
//        Boolean checkAutoSignAuth = reBusinessService.checkAutoSignAuth(signerList);
//        if(checkAutoSignAuth){
//            throw new PaasException("您尚无该印章的使用权限，无法设置自动盖章，请联系企业印章管理员为您授权印章权限");
//        }
        //获取业务线签署相关控件数据
        List<SignReDocControl> signReDocControlList = reDocControlService.listByParam(signRe.getId());
        //校验自动签署相关控件
        Boolean checkAutoSignErrorStatus = reBusinessService.checkAutoSignErrorStatus(signerList, signReDocControlList);
        if(checkAutoSignErrorStatus){
            throw new PaasException("组织签章【自动盖章】的签署节点未指定签署位置，请先指定签署位置");
        }
        //获取业务线文件列表
        List<SignReDoc> docList = reDocService.listByReId(signRe.getId());
        List<SignReDocParam> params = signReDocParamService.listByReId(signRe.getId());

        if(docList != null && docList.size() > 0){
            Boolean checkWriteErrorStatus = reBusinessService.checkWriteErrorStatus(docList, params, signerList);
            if(checkWriteErrorStatus){
                throw new PaasException("文档填写参数存在部分参数未指定填写方，请先指定");
            }
        }
//        if (CollUtil.isNotEmpty(docList)) {
//            //获取模板id列表
//            List<String> templateIds = new ArrayList<>();
//            for(SignReDoc reDoc : docList){
//                if(reDoc.getDocType() == SignFileTypeEnum.TEMPLATE.getCode() && reDoc.getDocOriginId() != null){
//                    templateIds.add(reDoc.getDocOriginId());
//                }
//            }
//            if(templateIds.size() > 0){
//                //统计业务线关联模板控件数量
//                Integer countTemplatePara = signTemplateControlService.count(templateIds);
//                if(countTemplatePara != null && countTemplatePara > 0){
//                    Integer countRePara = 0;
//                    List<SignReDocParam> params = signReDocParamService.listByReId(signRe.getId());
//                    if (CollUtil.isNotEmpty(params)) {
//                        countRePara = params.size();
//                    }
//
//                    if(countRePara == 0){
//                        throw new PaasException("存在填写参数未关联签署人，请完善后再启用");
//                    }
//                    //校验业务线配置的填写控件数量是否和模板的填写控件数量是否一致
//                    if (countRePara != countTemplatePara) {
////                            throw new PaasException("有参模板未设置参数，不可以启用");
//                        throw new PaasException("存在填写参数未关联签署人，请完善后再启用");
//                    }
//
//                    if(signerList == null || signerList.size() == 0){
//                        throw new PaasException("存在填写参数未关联签署人，请完善后再启用");
//                    }
//                    List<String> signerIdList = signerList.stream().map(DocSignerVo::getId).collect(Collectors.toList());
//                    if(signerIdList == null || signerIdList.size() == 0){
//                        throw new PaasException("存在填写参数未关联签署人，请完善后再启用");
//                    }
//                    for(SignReDocParam param : params) {
//                        if(param.getSignerId() == null || param.getSignerId().length() == 0){
//                            throw new PaasException("存在填写参数未关联签署人，请完善后再启用");
//                        }
//                        if(!signerIdList.contains(param.getSignerId())){
//                            throw new PaasException("存在填写参数未关联签署人，请完善后再启用");
//                        }
//                    }
//
//                }
//            }
//        }
    }
}